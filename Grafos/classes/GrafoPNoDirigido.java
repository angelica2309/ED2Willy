/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Willy
 * @param <T>
 */
public class GrafoPNoDirigido<T extends Comparable<T>> extends GrafoP<T> {

    public GrafoPNoDirigido() {
        super();
    }
   
    private void copiarVertices(GrafoPNoDirigido grafo) {
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            T vertice = this.listaDeVertices.get(i);
            grafo.insertarVertice(vertice);
        }
    }

    public boolean esConexo() {
        if (super.cantidadDeVertices() == 0) {
            return false;
        }
        List<T> vertices = super.recorridoDfs(super.listaDeVertices.get(0));
        return vertices.size() == super.listaDeVertices.size();
    }

    @Override
    protected PriorityQueue<AristaConPeso> colaDeCostos() {
        PriorityQueue<AristaConPeso> cola = new PriorityQueue<>();
        int n = cantidadDeVertices();
        boolean[][] marcados = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            List<AdyacenteConPeso> lista = listasDeAdyacencias.get(i);
            for (int j = 0; j < lista.size(); j++) {
                AdyacenteConPeso ady = lista.get(j);
                int ind = ady.getIndiceDeVertice();
                if (!marcados[i][ind] && !marcados[ind][i]) {
                    T verticeA = this.listaDeVertices.get(i);
                    T verticeB = this.listaDeVertices.get(ind);
                    AristaConPeso arista = new AristaConPeso(verticeA, verticeB, ady.getPeso());
                    cola.offer(arista);
                    marcados[i][ind] = marcados[ind][i] = true;
                }
            }
        }
        return cola;
    }

    public GrafoPNoDirigido<T> arbolDeExpansionKruskal() {
        if (!this.esConexo()) {
            return null;
        }
        GrafoPNoDirigido<T> grafoRes = new GrafoPNoDirigido<>();//Creando grafo auxiliar
        super.copiarVertices(grafoRes);                  //insertando todos los vertices
        this.algoritmoDeKruskal(grafoRes);
        return grafoRes;
    }

    private void algoritmoDeKruskal(GrafoPNoDirigido<T> grafoAux) {
        PriorityQueue<AristaConPeso> aristas = this.colaDeCostos();//Estructura con las aristas ordenadas
        while (!aristas.isEmpty()) {
            AristaConPeso arista = aristas.poll();
            T verticeA = (T) arista.getVerticeA();
            T verticeB = (T) arista.getVerticeB();
            int peso = arista.getPeso();
            grafoAux.insertarArista(verticeA, verticeB, peso);
            if (grafoAux.hayCiclo(verticeA)) {
                grafoAux.eliminarArista(verticeA, verticeB);
            }
        }
    }

    protected PriorityQueue<AristaConPeso<T>> colaAristaConCostos(int indiceDelVertice, List<Boolean> marcados) {
        PriorityQueue<AristaConPeso<T>> cola = new PriorityQueue<>();
        List<AdyacenteConPeso> lista = listasDeAdyacencias.get(indiceDelVertice);
        T verticeA = listaDeVertices.get(indiceDelVertice);
        for (int i = 0; i < lista.size(); i++) {
            AdyacenteConPeso adyacente = lista.get(i);
            int indiceVertB = adyacente.getIndiceDeVertice();
            if (!marcados.get(indiceVertB)) {
                T verticeB = listaDeVertices.get(indiceVertB);
                int peso = adyacente.getPeso();
                AristaConPeso arista = new AristaConPeso(verticeA, verticeB, peso);
                cola.offer(arista);
            }
        }
        return cola;
    }

    public GrafoPNoDirigido<T> bosqueDeExpansionPrim() {
        GrafoPNoDirigido grafoRes = new GrafoPNoDirigido();
        List<Boolean> marcados = this.inicializarMarcados();
        do {
            T vertice = super.verticeNoMarcado(marcados);
            this.algoritmoDePrim(vertice, grafoRes, marcados);
        } while (!super.todosMarcados(marcados));
        return grafoRes;
    }

    public GrafoPNoDirigido<T> arbolDeExpansionPrim(T verticeInicial) {
        GrafoPNoDirigido grafoRes = new GrafoPNoDirigido();
        List<Boolean> marcados = this.inicializarMarcados();
        this.algoritmoDePrim(verticeInicial, grafoRes, marcados);
        return grafoRes;
    }

    private void algoritmoDePrim(T verticeInicial, GrafoPNoDirigido<T> grafoW, List<Boolean> marcados) {
        grafoW.insertarVertice(verticeInicial);             //Inserta el grafo de partida
        T verticeEnTurno = verticeInicial;
        PriorityQueue<AristaConPeso<T>> colaDeCostos = new PriorityQueue<>();
        while (!colaDeCostos.isEmpty() || verticeInicial == verticeEnTurno) {
            int indiceDelVertice = super.posicionDelVertice(verticeEnTurno);
            if (!marcados.get(indiceDelVertice)) {
                colaDeCostos.addAll(this.colaAristaConCostos(indiceDelVertice, marcados));
                super.marcarVertice(marcados, indiceDelVertice);
            }
            AristaConPeso<T> arista = colaDeCostos.poll();
            verticeEnTurno = arista.getVerticeB();
            if (!grafoW.existeVertice(verticeEnTurno)) {
                T verticeA = arista.getVerticeA();
                T verticeB = arista.getVerticeB();
                int peso = arista.getPeso();
                grafoW.insertarVertice(verticeB);
                grafoW.insertarArista(verticeA, verticeB, peso);
            }
        }
    }

    @Override
    public boolean hayCiclo(T verticeDePartida) {
        if (!this.existeVertice(verticeDePartida)) {
            return false;
        }
        GrafoPNoDirigido grafoAux = new GrafoPNoDirigido();
        super.copiarVertices(grafoAux);
        List<Boolean> marcados = super.inicializarMarcados();
        return this.hayCiclo(verticeDePartida, verticeDePartida, marcados, grafoAux);
    }

    protected boolean hayCiclo(T verticeBusq, T vertice, List<Boolean> marcados, GrafoPNoDirigido<T> grafoAux) {
        int posVert = super.posicionDelVertice(verticeBusq);
        int pos = super.posicionDelVertice(vertice);
        super.marcarVertice(marcados, pos);
        List<AdyacenteConPeso> lista = super.listasDeAdyacencias.get(pos);
        boolean hayCiclo = false;
        for (int i = 0; i < lista.size() && !hayCiclo; i++) {
            T verticeA = vertice;
            int indB = lista.get(i).getIndiceDeVertice();
            T verticeB = listaDeVertices.get(indB);
            int peso = lista.get(i).getPeso();
            if (!grafoAux.existeArista(verticeA, verticeB)) {
                grafoAux.insertarArista(verticeA, verticeB,peso);
                if (posVert == indB && super.estaMarcadoElVertice(marcados, indB)) {
                    hayCiclo = true;
                } else if (!super.estaMarcadoElVertice(marcados, indB)) {
                    hayCiclo = hayCiclo || this.hayCiclo(verticeBusq,
                            verticeB, marcados, grafoAux);
                }
            }
        }
        return hayCiclo;
    }
    
    @Override
    public boolean hayCicloEnElGrafo(){
        if(super.cantidadDeVertices() == 0){
            return false;
        }
        T verticeEnTurno;
        List<Boolean> marcados = super.inicializarMarcados();
        boolean hayCiclo = false;
        GrafoPNoDirigido grafoAux = new GrafoPNoDirigido();
        super.copiarVertices(grafoAux);
        do{
            verticeEnTurno = super.verticeNoMarcado(marcados);
            hayCiclo = hayCiclo || this.hayCiclo(verticeEnTurno, verticeEnTurno, marcados, grafoAux);
        }while(!super.todosMarcados(marcados) && !hayCiclo);
        return hayCiclo;
    }
}
