/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import javax.swing.JTextArea;

/**
 *
 * @author Willy
 * @param <T>
 */
public class Grafo<T extends Comparable<T>> {

    List<T> listaDeVertices;
    List<List<Integer>> listasDeAdyacencias;
    boolean esDirigido;
    static final int POSICION_INVALIDA = -1;

    public Grafo() {
        this(false);
    }

    public Grafo(boolean esDirigido) {
        this.esDirigido = esDirigido;
        listaDeVertices = new LinkedList<>();
        listasDeAdyacencias = new LinkedList<>();
    }

    protected void copiarVertices(Grafo<T> grafo) {
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            T vertice = this.listaDeVertices.get(i);
            grafo.insertarVertice(vertice);
        }
    }

    public static void mostrarVerticesYAristas(JTextArea text, Grafo grafoNoPesado) {
        int n = grafoNoPesado.cantidadDeVertices();
        for (int i = 0; i < n; i++) {
            String vertice = grafoNoPesado.listaDeVertices.get(i).toString();
            String linea = vertice + "  |  " + i + "->" + grafoNoPesado.listasDeAdyacencias.get(i);
            text.setText(text.getText() + linea + "\n");
        }
    }

    public boolean hayCicloEnElGrafo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean hayCiclo(T verticeDePartida) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean insertarVertice(T vertice) {
        if (this.existeVertice(vertice)) {
            return false;
        }
        listaDeVertices.add(vertice);
        listasDeAdyacencias.add(new LinkedList<>());
        return true;
    }

    public boolean insertarArista(T verticeOrigen, T verticeDestino) {
        if (!this.existeVertice(verticeOrigen) || !this.existeVertice(verticeDestino)) {
            return false;
        }
        if (this.existeArista(verticeOrigen, verticeDestino)) {
            return false;
        }
        int posicionVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        int posicionVerticeDestino = this.posicionDelVertice(verticeDestino);
        List<Integer> listaAdyacenciaOrigen = listasDeAdyacencias.get(posicionVerticeOrigen);
        listaAdyacenciaOrigen.add(posicionVerticeDestino);
        Collections.sort(listaAdyacenciaOrigen);
        if (!this.esDirigido && posicionVerticeOrigen != posicionVerticeDestino) {
            List<Integer> listaAdyacenciaDestino = listasDeAdyacencias.get(posicionVerticeDestino);
            listaAdyacenciaDestino.add(posicionVerticeOrigen);
            Collections.sort(listaAdyacenciaDestino);
        }
        return true;
    }

    public boolean existeArista(T verticeOrigen, T verticeDestino) {
        int posicionVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        if (posicionVerticeOrigen == -1) {
            return false;
        }
        int posicionVerticeDestino = this.posicionDelVertice(verticeDestino);
        List<Integer> listaAdyacenciaOrigen = listasDeAdyacencias.get(posicionVerticeOrigen);
        return listaAdyacenciaOrigen.contains(posicionVerticeDestino);
    }

    public int cantidadDeVertices() {
        return listaDeVertices.size();
    }

    public int cantidadDeAristas() {
        int cantidad = 0;
        for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
            List<Integer> lista = this.listasDeAdyacencias.get(i);
            int size = lista.size();
            cantidad += lista.contains((Integer) i) ? size - 1 : size;
        }
        if(!this.esDirigido){
            cantidad /= 2;
        }
        return cantidad;
    }

    protected int posicionDelVertice(T vertice) {
        for (int i = 0; i < listaDeVertices.size(); i++) {
            if (vertice.compareTo(listaDeVertices.get(i)) == 0) {
                return i;
            }
        }
        return Grafo.POSICION_INVALIDA;
    }

    public boolean existeVertice(T vertice) {
        return this.posicionDelVertice(vertice) != Grafo.POSICION_INVALIDA;
    }

    public boolean eliminarVertice(T vertice) {
        if (!existeVertice(vertice)) {
            return false;
        }
        int posicionDelVerticeAEliminar = this.posicionDelVertice(vertice);
        this.listaDeVertices.remove(posicionDelVerticeAEliminar);
        this.listasDeAdyacencias.remove(posicionDelVerticeAEliminar);
        for (List<Integer> adyacentesDeVertice : this.listasDeAdyacencias) {
            if (adyacentesDeVertice.contains(posicionDelVerticeAEliminar)) {
                //Eliminamos de la lista el objeto y no basandonos en la posicion
                adyacentesDeVertice.remove((Integer) posicionDelVerticeAEliminar);
            }
            for (int i = 0; i < adyacentesDeVertice.size(); i++) {
                int indiceDelAdyacente = adyacentesDeVertice.get(i);
                if (indiceDelAdyacente > posicionDelVerticeAEliminar) {
                    indiceDelAdyacente--;
                    adyacentesDeVertice.set(i, indiceDelAdyacente);
                }
            }
        }
        return true;
    }

    public boolean eliminarArista(T verticeOrigen, T verticeDestino) {
        if (!this.existeArista(verticeOrigen, verticeDestino)) {
            return false;
        }
        int posicionDelVerticeOrigen = this.posicionDelVertice(verticeOrigen);
        List<Integer> listaAdyacente = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        Integer posicionDelVerticeDestino = (Integer) this.posicionDelVertice(verticeDestino);
        listaAdyacente.remove(posicionDelVerticeDestino);
        if (!this.esDirigido && posicionDelVerticeOrigen != posicionDelVerticeDestino) {
            this.eliminarArista(verticeDestino, verticeOrigen);
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
            List<Integer> adyacentes = listasDeAdyacencias.get(posicionDelVerticeEnTurno);
            for (int i = 0; i < adyacentes.size(); i++) {
                if (!estaMarcadoElVertice(marcados, adyacentes.get(i))) {
                    T verticeAMeter = this.listaDeVertices.get(adyacentes.get(i));
                    colaDeVertices.offer(verticeAMeter);
                    marcarVertice(marcados, adyacentes.get(i));
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
        List<Integer> adyacentes = listasDeAdyacencias.get(posicionVerticeEnTurno);
        for (int i = 0; i < adyacentes.size(); i++) {
            if (!estaMarcadoElVertice(marcados, adyacentes.get(i))) {
                this.recursivoDfs(adyacentes.get(i), recorrido, marcados);
            }
        }
    }

    public List<T> recorridoDfsIterativo(T verticeDePartida) {
        List<T> recorrido = new LinkedList<>();
        if (this.cantidadDeVertices() == 0) {
            return recorrido;
        }
        if (!this.existeVertice(verticeDePartida)) {
            return recorrido;
        }
        List<Boolean> marcados = inicializarMarcados();
        Stack<T> pilaDeVertices = new Stack<>();
        pilaDeVertices.push(verticeDePartida);
        int posicionVertice = this.posicionDelVertice(verticeDePartida);
        this.marcarVertice(marcados, posicionVertice);
        do {
            T verticeEnTurno = pilaDeVertices.pop();
            recorrido.add(verticeEnTurno);
            int posVerticeEnTurno = this.posicionDelVertice(verticeEnTurno);
            marcarVertice(marcados, posVerticeEnTurno);
            List<Integer> adyacencia = this.listasDeAdyacencias.get(posVerticeEnTurno);
            int n = adyacencia.size();
            for (int i = n - 1; i >= 0; i--) {
                if (!estaMarcadoElVertice(marcados, adyacencia.get(i))) {
                    T verticeAMeter = this.listaDeVertices.get(adyacencia.get(i));
                    pilaDeVertices.push(verticeAMeter);
                    marcarVertice(marcados, adyacencia.get(i));
                }
            }
        } while (!pilaDeVertices.isEmpty());
        return recorrido;
    }

    protected List<Boolean> inicializarMarcados() {
        List<Boolean> lista = new LinkedList<>();
        for (int i = 0; i < listaDeVertices.size(); i++) {
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

    protected List<Integer> adyacentesAlVertice(int posicionDelVertice) {
        List<Integer> adyacentes = new LinkedList<>();
        if (posicionDelVertice != Grafo.POSICION_INVALIDA) {
            for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
                List<Integer> lista = this.listasDeAdyacencias.get(i);
                if (lista.contains((Integer) posicionDelVertice)) {
                    adyacentes.add(i);
                }
            }
        }
        return adyacentes;
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
        List<Integer> adyacentesDelVertice = listasDeAdyacencias.get(posDelVertice);
        List<Integer> adyacentesAlVertice = adyacentesAlVertice(posDelVertice);
        marcarVertice(marcados, posDelVertice);
        listaDeLaIsla.add(vertice);
        for (int i = 0; i < adyacentesDelVertice.size(); i++) {
            int indice = adyacentesDelVertice.get(i);
            if (!estaMarcadoElVertice(marcados, indice)) {
                T verticeAdy = listaDeVertices.get(indice);
                this.islaDelVertice(verticeAdy, listaDeLaIsla, marcados);
            }
        }
        for (int j = 0; j < adyacentesAlVertice.size(); j++) {
            int indice = adyacentesAlVertice.get(j);
            if (!estaMarcadoElVertice(marcados, indice)) {
                T verticeAdy = listaDeVertices.get(indice);
                this.islaDelVertice(verticeAdy, listaDeLaIsla, marcados);
            }
        }
    }
    
}
