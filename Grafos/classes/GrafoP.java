/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.swing.JTextArea;

/**
 *
 * @author Willy
 * @param <T>
 */
public class GrafoP<T extends Comparable<T>> {

    public class AdyacenteConPeso implements Comparable<AdyacenteConPeso> {

        protected int indiceDeVertice;
        protected int peso;

        public AdyacenteConPeso(int indiceDeVertice) {
            this.indiceDeVertice = indiceDeVertice;
        }

        public AdyacenteConPeso(int indiceDeVertice, int peso) {
            this.indiceDeVertice = indiceDeVertice;
            this.peso = peso;
        }

        public int getIndiceDeVertice() {
            return indiceDeVertice;
        }

        public int getPeso() {
            return peso;
        }

        public void setIndiceDeVertice(int indiceDeVertice) {
            this.indiceDeVertice = indiceDeVertice;
        }

        public void setPeso(int peso) {
            this.peso = peso;
        }

        @Override
        public int compareTo(AdyacenteConPeso otro) {
            if (otro == null) {
                return -1;
            }
            if (this.indiceDeVertice > otro.indiceDeVertice) {
                return 1;
            }
            if (this.indiceDeVertice < otro.indiceDeVertice) {
                return -1;
            }
            return 0;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + this.indiceDeVertice;
            hash = 11 * hash + this.peso;
            /*List<Integer> list = new LinkedList<>();
            list.*/
            return hash;
        }

        @Override
        public boolean equals(Object otro) {
            if (otro == null) {
                return false;
            }
            if (getClass() != otro.getClass()) {
                return false;
            }
            return this.indiceDeVertice == ((AdyacenteConPeso) otro).indiceDeVertice;
        }

        @Override
        public String toString() {
            return "{" + indiceDeVertice + "," + peso + '}';
        }
    }

    public class PesoConAdyacente extends AdyacenteConPeso {

        public PesoConAdyacente(int indiceDeVertice) {
            super(indiceDeVertice);
        }

        public PesoConAdyacente(int indiceDeVertice, int peso) {
            super(indiceDeVertice, peso);
        }

        @Override
        public int compareTo(AdyacenteConPeso otro) {
            if (otro == null) {
                return -1;
            }
            return this.peso - otro.peso;
        }

        @Override
        public boolean equals(Object otro) {
            if (otro == null) {
                return false;
            }
            if (getClass() != otro.getClass()) {
                return false;
            }
            return this.peso == ((PesoConAdyacente) otro).peso;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            return hash;
        }
    }

    public class AristaConPeso<T extends Comparable<T>> implements Comparable<AristaConPeso> {

        protected T verticeA;
        protected T verticeB;
        protected int peso;

        public void setVerticeA(T verticeA) {
            this.verticeA = verticeA;
        }

        public void setVerticeB(T verticeB) {
            this.verticeB = verticeB;
        }

        public void setPeso(int peso) {
            this.peso = peso;
        }

        public T getVerticeA() {
            return verticeA;
        }

        public T getVerticeB() {
            return verticeB;
        }

        public int getPeso() {
            return peso;
        }

        public AristaConPeso(T verticeA, T verticeB, int peso) {
            this.verticeA = verticeA;
            this.verticeB = verticeB;
            this.peso = peso;
        }

        @Override
        public String toString() {
            return "{" + this.getVerticeA() + "," + this.getVerticeB() + "," + this.getPeso() + "}";
        }

        @Override
        public int compareTo(AristaConPeso otro) {
            if (otro == null) {
                return -1;
            }
            return this.peso - otro.peso;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + Objects.hashCode(this.verticeA);
            hash = 71 * hash + Objects.hashCode(this.verticeB);
            hash = 71 * hash + this.peso;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AristaConPeso<?> other = (AristaConPeso<?>) obj;
            return this.peso == other.peso;
        }

    }

    List<T> listaDeVertices;
    List<List<AdyacenteConPeso>> listasDeAdyacencias;
    boolean esDirigido;
    public static final double INFINITO = Double.MAX_VALUE;
    static final int POSICION_INVALIDA = -1;

    public GrafoP() {
        this(false);
    }

    public GrafoP(boolean esDirigido) {
        this.esDirigido = esDirigido;
        listaDeVertices = new LinkedList<>();
        listasDeAdyacencias = new LinkedList<>();
    }

    public static void mostrarVerticesYAristas(JTextArea text, GrafoP grafoPesado) {
        text.setText("");
        int n = grafoPesado.cantidadDeVertices();
        for (int i = 0; i < n; i++) {
            String vertice = grafoPesado.listaDeVertices.get(i).toString();
            String linea = vertice + "  |  " + i + "->" + grafoPesado.listasDeAdyacencias.get(i);
            text.setText(text.getText() + linea + "\n");
        }
    }

    public boolean insertarVertice(T vertice) {
        if (this.existeVertice(vertice)) {
            return false;
        }
        listaDeVertices.add(vertice);
        List<AdyacenteConPeso> nuevaLista = new LinkedList<>();
        listasDeAdyacencias.add(nuevaLista);
        return true;
    }

    public boolean existeVertice(T vertice) {
        return this.posicionDelVertice(vertice) != Grafo.POSICION_INVALIDA;
    }

    protected int posicionDelVertice(T vertice) {
        for (int i = 0; i < listaDeVertices.size(); i++) {
            if (vertice.compareTo(listaDeVertices.get(i)) == 0) {
                return i;
            }
        }
        return GrafoP.POSICION_INVALIDA;
    }

    public int cantidadDeVertices() {
        return listaDeVertices.size();
    }

    public int cantidadDeAristas() {
        int cantidad = 0;
        for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
            List<AdyacenteConPeso> lista = this.listasDeAdyacencias.get(i);
            int size = lista.size();
            //AdyacenteConPeso busq = new AdyacenteConPeso(i);
            cantidad += lista.contains(new AdyacenteConPeso(i)) ? size - 1 : size;
        }
        if(!this.esDirigido){
            cantidad /= 2;
        }
        return cantidad;
    }

    public boolean existeArista(T verticeOrigen, T verticeDestino) {
        int posicionVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        if (posicionVerticeOrigen == -1) {
            return false;
        }
        int posicionVerticeDestino = this.posicionDelVertice(verticeDestino);
        List<AdyacenteConPeso> listaAdyacenciaOrigen = listasDeAdyacencias.get(posicionVerticeOrigen);
        return listaAdyacenciaOrigen.contains(new AdyacenteConPeso(posicionVerticeDestino));
    }

    public boolean insertarArista(T verticeOrigen, T verticeDestino, int peso) {
        if (!this.existeVertice(verticeOrigen) || !this.existeVertice(verticeDestino)) {
            return false;
        }
        if (this.existeArista(verticeOrigen, verticeDestino)) {
            return false;
        }
        int posicionVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        int posicionVerticeDestino = this.posicionDelVertice(verticeDestino);
        List<AdyacenteConPeso> listaAdyacenciaOrigen
                = listasDeAdyacencias.get(posicionVerticeOrigen);
        AdyacenteConPeso ad = new AdyacenteConPeso(posicionVerticeDestino, peso);
        listaAdyacenciaOrigen.add(ad);
        Collections.sort(listaAdyacenciaOrigen);
        if (!this.esDirigido && posicionVerticeOrigen != posicionVerticeDestino) {
            List<AdyacenteConPeso> listaAdyacenciaDestino = listasDeAdyacencias.get(posicionVerticeDestino);
            AdyacenteConPeso ad1 = new AdyacenteConPeso(posicionVerticeOrigen, peso);
            listaAdyacenciaDestino.add(ad1);
            Collections.sort(listaAdyacenciaDestino);
        }
        return true;
    }

    public boolean eliminarArista(T verticeOrigen, T verticeDestino) {
        if (!this.existeArista(verticeOrigen, verticeDestino)) {
            return false;
        }
        int posicionDelVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        List<AdyacenteConPeso> listaAdyacente = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        int posicionDelVerticeDestino = this.posicionDelVertice(verticeDestino);
        listaAdyacente.remove(new AdyacenteConPeso(posicionDelVerticeDestino));
        if (!this.esDirigido && posicionDelVerticeOrigen != posicionDelVerticeDestino) {
            this.eliminarArista(verticeDestino, verticeOrigen);
        }
        return true;
    }

    public boolean eliminarVertice(T vertice) {
        if (!existeVertice(vertice)) {
            return false;
        }
        int posicionDelVerticeAEliminar = this.posicionDelVertice(vertice);
        this.listaDeVertices.remove(posicionDelVerticeAEliminar);
        this.listasDeAdyacencias.remove(posicionDelVerticeAEliminar);
        for (List<AdyacenteConPeso> adyacentesDeVertice : this.listasDeAdyacencias) {
            AdyacenteConPeso ad = new AdyacenteConPeso(posicionDelVerticeAEliminar);
            if (adyacentesDeVertice.contains(ad)) {
                //Eliminamos de la lista el objeto y no basandonos en la posicion
                adyacentesDeVertice.remove(ad);
            }
            for (int i = 0; i < adyacentesDeVertice.size(); i++) {
                int indiceDelAdyacente = adyacentesDeVertice.get(i).indiceDeVertice;
                if (indiceDelAdyacente > posicionDelVerticeAEliminar) {
                    indiceDelAdyacente--;
                    adyacentesDeVertice.get(i).setIndiceDeVertice(indiceDelAdyacente);
                }
            }
        }
        return true;
    }

    public List<T> recorridoBfs(T verticeDePartida) {
        List<T> recorrido = new LinkedList<>();
        if (this.cantidadDeVertices() == 0) {
            return recorrido;
        }
        if (!this.existeVertice(verticeDePartida)) {
            return recorrido;
        }
        List<Boolean> marcados = inicializarMarcados();
        Queue<T> colaDeVertices = new LinkedList<>();
        colaDeVertices.offer(verticeDePartida);
        int posicionDelVertice = this.posicionDelVertice(verticeDePartida);
        marcarVertice(marcados, posicionDelVertice);
        do {
            T verticeEnTurno = colaDeVertices.poll();
            recorrido.add(verticeEnTurno);
            int posicionDelVerticeEnTurno = this.posicionDelVertice(verticeEnTurno);
            marcarVertice(marcados, posicionDelVerticeEnTurno);
            List<AdyacenteConPeso> adyacentes = listasDeAdyacencias.get(posicionDelVerticeEnTurno);
            for (int i = 0; i < adyacentes.size(); i++) {
                if (!estaMarcadoElVertice(marcados, adyacentes.get(i).getIndiceDeVertice())) {
                    T verticeAMeter = this.listaDeVertices.get(adyacentes.get(i).getIndiceDeVertice());
                    colaDeVertices.offer(verticeAMeter);
                    marcarVertice(marcados, adyacentes.get(i).getIndiceDeVertice());
                }
            }
        } while (!colaDeVertices.isEmpty());
        return recorrido;
    }

    public List<T> recorridoDfs(T verticeDePartida) {
        List<T> recorrido = new LinkedList<>();
        if (this.cantidadDeVertices() == 0) {
            return recorrido;
        }
        if (!this.existeVertice(verticeDePartida)) {
            return recorrido;
        }
        List<Boolean> marcados = inicializarMarcados();
        int posicionDelVerticeDePartida = this.posicionDelVertice(verticeDePartida);
        this.recursivoDfs(posicionDelVerticeDePartida, recorrido, marcados);
        return recorrido;
    }

    protected void recursivoDfs(int posicionVerticeEnTurno, List<T> recorrido, List<Boolean> marcados) {
        marcarVertice(marcados, posicionVerticeEnTurno);
        recorrido.add(this.listaDeVertices.get(posicionVerticeEnTurno));
        List<AdyacenteConPeso> adyacentes = listasDeAdyacencias.get(posicionVerticeEnTurno);
        for (int i = 0; i < adyacentes.size(); i++) {
            if (!estaMarcadoElVertice(marcados, adyacentes.get(i).getIndiceDeVertice())) {
                this.recursivoDfs(adyacentes.get(i).getIndiceDeVertice(), recorrido, marcados);
            }
        }
    }

    protected List<Boolean> inicializarMarcados() {
        List<Boolean> lista = new LinkedList<>();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            lista.add(Boolean.FALSE);
        }
        return lista;
    }

    protected void marcarVertice(List<Boolean> marcados, int posicion) {
        marcados.set(posicion, Boolean.TRUE);
    }

    protected Boolean estaMarcadoElVertice(List<Boolean> marcados, int posicion) {
        return marcados.get(posicion);
    }

    protected List<AdyacenteConPeso> adyacentesAlVertice(int posicionDelVertice) {
        List<AdyacenteConPeso> adyacentes = new LinkedList<>();
        if (posicionDelVertice != Grafo.POSICION_INVALIDA) {
            for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
                List<AdyacenteConPeso> lista = this.listasDeAdyacencias.get(i);
                AdyacenteConPeso ad = new AdyacenteConPeso(posicionDelVertice, 0);
                if (lista.contains(ad)) {
                    int indice = lista.indexOf(ad);
                    int peso = lista.get(indice).getPeso();
                    AdyacenteConPeso nuevo = new AdyacenteConPeso(i,peso);
                    adyacentes.add(nuevo);
                }
            }
        }
        return adyacentes;
    }

    protected boolean todosMarcados(List<Boolean> marcados) {
        for (int i = 0; i < marcados.size(); i++) {
            if (marcados.get(i).equals(Boolean.FALSE)) {
                return false;
            }
        }
        return true;
    }

    protected T verticeNoMarcado(List<Boolean> marcados) {
        for (int i = 0; i < marcados.size(); i++) {
            if (!marcados.get(i)) {
                return this.listaDeVertices.get(i);
            }
        }
        return null;
    }

    public boolean hayCicloEnElGrafo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean hayCiclo(T verticeDePartida) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected PriorityQueue<PesoConAdyacente> colaDeCostos(int indiceDelVertice) {
        PriorityQueue<PesoConAdyacente> cola = new PriorityQueue<>();
        List<AdyacenteConPeso> lista = listasDeAdyacencias.get(indiceDelVertice);
        for (int i = 0; i < lista.size(); i++) {
            AdyacenteConPeso adyacente = lista.get(i);
            PesoConAdyacente peso = new PesoConAdyacente(adyacente.getIndiceDeVertice(), adyacente.getPeso());
            cola.offer(peso);
        }
        return cola;
    }

    protected PriorityQueue<AristaConPeso> colaDeCostos() {
        PriorityQueue<AristaConPeso> cola = new PriorityQueue<>();
        int n = cantidadDeVertices();
        for (int i = 0; i < n; i++) {
            List<AdyacenteConPeso> lista = listasDeAdyacencias.get(i);
            for (int j = 0; j < lista.size(); j++) {
                AdyacenteConPeso ady = lista.get(j);
                int ind = ady.getIndiceDeVertice();
                T verticeA = this.listaDeVertices.get(i);
                T verticeB = this.listaDeVertices.get(ind);
                AristaConPeso arista = new AristaConPeso(verticeA, verticeB, ady.getPeso());
                cola.offer(arista);
            }
        }
        return cola;
    }

    public List<AristaConPeso> listaDeAristasOrdenada() {
        List<AristaConPeso> aristas = new LinkedList<>();
        int n = cantidadDeVertices();
        for (int i = 0; i < n; i++) {
            List<AdyacenteConPeso> lista = listasDeAdyacencias.get(i);
            for (int j = 0; j < lista.size(); j++) {
                AdyacenteConPeso ad = lista.get(j);
                AristaConPeso ar = new AristaConPeso(i, ad.getIndiceDeVertice(), ad.getPeso());
                aristas.add(ar);
            }
        }
        Collections.sort(aristas);
        return aristas;
    }

    protected void copiarVertices(GrafoP<T> grafo) {
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            T vertice = this.listaDeVertices.get(i);
            grafo.insertarVertice(vertice);
        }
    }
    
    //imprime las islas de un grafo dirigido
    public List<List<T>> islas() {
        List<List<T>> islas = new LinkedList<>();
        List<Boolean> marcados = inicializarMarcados();
        do {
            T vertice = verticeNoMarcado(marcados);
            List<T> isla = new LinkedList<>();
            this.islaDelVertice(vertice, isla, marcados);
            islas.add(isla);
        } while (!todosMarcados(marcados));
        return islas;
    }
    
    public void imprimirIsla(){
        if(cantidadDeVertices() > 0){
            T verticeEnTurno = listaDeVertices.get(0);
            List<Boolean> marcados = inicializarMarcados();
            int i = 1;
            do{
                List<T> isla = new LinkedList<>();
                this.islaDelVertice(verticeEnTurno,isla,marcados);
                System.out.println("Isla n° "+String.valueOf(i)+": "+isla);
                i++;
                verticeEnTurno = this.verticeNoMarcado(marcados);
            }while(!todosMarcados(marcados));
        }
    }

    public List<T> islaDelVertice(T vertice) {
        List<T> listaDeLaLista = new LinkedList<>();
        if (!existeVertice(vertice)) {
            return listaDeLaLista;
        }
        List<Boolean> marcados = this.inicializarMarcados();
        this.islaDelVertice(vertice, listaDeLaLista, marcados);
        return listaDeLaLista;
    }

    public void islaDelVertice(T vertice, List<T> listaDeLaIsla, List<Boolean> marcados) {
        int posDelVertice = posicionDelVertice(vertice);
        marcarVertice(marcados, posDelVertice);
        listaDeLaIsla.add(vertice);
        List<AdyacenteConPeso> adyacentesDelVertice = listasDeAdyacencias.get(posDelVertice);
        List<AdyacenteConPeso> adyacentesAlVertice = adyacentesAlVertice(posDelVertice);
        for (int i = 0; i < adyacentesDelVertice.size(); i++) {
            int indice = adyacentesDelVertice.get(i).getIndiceDeVertice();
            if (!estaMarcadoElVertice(marcados, indice)) {
                T verticeAdy = listaDeVertices.get(indice);
                this.islaDelVertice(verticeAdy, listaDeLaIsla, marcados);
            }
        }
        for (int j = 0; j < adyacentesAlVertice.size(); j++) {
            int indice = adyacentesAlVertice.get(j).getIndiceDeVertice();
            if (!estaMarcadoElVertice(marcados, indice)) {
                T verticeAdy = listaDeVertices.get(indice);
                this.islaDelVertice(verticeAdy, listaDeLaIsla, marcados);
            }
        }
    }

}
