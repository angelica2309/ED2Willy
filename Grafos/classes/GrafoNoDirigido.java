/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Willy
 * @param <T>
 */
public class GrafoNoDirigido<T extends Comparable<T>> extends Grafo<T> {

    public GrafoNoDirigido() {
        super();
    }

    public boolean esConexo() {
        if (super.cantidadDeVertices() == 0) {
            return false;
        }
        List<T> vertices = super.recorridoDfs(super.listaDeVertices.get(0));
        return vertices.size() == super.listaDeVertices.size();
    }

    public int cantidadDeIslas() {
        if (super.cantidadDeVertices() == 0) {
            return 0;
        }
        int nroDeIslas = 0;
        List<Boolean> marcados = super.inicializarMarcados();
        do {
            List<T> recorrido = new LinkedList<>();
            T verticeNoMarcado = this.verticeNoMarcado(marcados);
            int posicionDelVerticeNoMarcado = super.posicionDelVertice(verticeNoMarcado);
            super.recursivoDfs(posicionDelVerticeNoMarcado, recorrido, marcados);
            nroDeIslas++;
        } while (!this.todosMarcados(marcados));
        return nroDeIslas;
    }

    @Override
    public boolean hayCiclo(T verticeDePartida) {
        if (!this.existeVertice(verticeDePartida)) {
            return false;
        }
        GrafoNoDirigido grafoAux = new GrafoNoDirigido();
        super.copiarVertices(grafoAux);
        List<Boolean> marcados = super.inicializarMarcados();
        return this.hayCiclo(verticeDePartida, verticeDePartida, marcados, grafoAux);
    }

    protected boolean hayCiclo(T verticeBusq, T verticeActual, List<Boolean> marcados, GrafoNoDirigido<T> grafoAux) {
        int posVert = super.posicionDelVertice(verticeBusq);
        int posVertActual = super.posicionDelVertice(verticeActual);
        //Marco el vertice actual para indicar que ya lo visite
        super.marcarVertice(marcados, posVertActual);
        List<Integer> lista = super.listasDeAdyacencias.get(posVertActual);
        boolean hayCiclo = false;
        for (int i = 0; i < lista.size() && !hayCiclo; i++) {
            T verticeA = verticeActual;
            int indB = lista.get(i);
            T verticeB = listaDeVertices.get(indB);
            if (!grafoAux.existeArista(verticeA, verticeB)) {
                grafoAux.insertarArista(verticeA, verticeB);
                //Aqui indica que encontramos el vertice,dando un ciclo,debido a que ya estaba marcado y el verticeA no lo estaba
                if (posVert == indB  && estaMarcadoElVertice(marcados, indB)) {
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
        GrafoNoDirigido grafoAux = new GrafoNoDirigido();
        super.copiarVertices(grafoAux);
        do{
            verticeEnTurno = super.verticeNoMarcado(marcados);
            hayCiclo = hayCiclo || this.hayCiclo(verticeEnTurno, verticeEnTurno, marcados, grafoAux);
        }while(!super.todosMarcados(marcados) && !hayCiclo);
        return hayCiclo;
    }
    
}
