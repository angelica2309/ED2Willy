/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author Willy
 * @param <T>
 */
public class GrafoPDirigido<T extends Comparable<T>> extends GrafoP<T> {

    public GrafoPDirigido() {
        super(true);
    }

    public boolean esFuertementeConexo() {
        if (super.cantidadDeVertices() == 0) {
            return false;
        }
        int nroDeVertices = super.cantidadDeVertices();
        for (T unVertice : super.listaDeVertices) {
            List<T> recorrido = recorridoDfs(unVertice);
            if (recorrido.size() != nroDeVertices) {
                return false;
            }
        }
        return true;
    }

    public boolean esDebilmenteConexo() {
        return (!this.esFuertementeConexo()) && this.islas().size() == 1;
    }

    /*
    public boolean esDebilmenteConexo(){
        
    }
    
    public boolean esDebilmenteConexo(List<Boolean> marcados){
        
    }*/
    public double costoDelCamino(T[] camino) {
        double ret = 0;
        for (int i = 0; i < camino.length - 1; i++) {
            if (super.existeArista(camino[i], camino[i + 1])) {
                int pos1 = this.posicionDelVertice(camino[i]);
                int pos2 = this.posicionDelVertice(camino[i + 1]);
                ret += this.pesoEntreVertices(pos1, pos2);
            }else{
                return GrafoP.INFINITO;
            }
        }
        return ret;
    }

    public double costoMinimo(T verticeA, T verticeB) {
        if (!super.existeVertice(verticeA) || !super.existeVertice(verticeB)) {
            return GrafoP.INFINITO;
        }
        int posVerticeA = super.posicionDelVertice(verticeA);
        int posVerticeB = super.posicionDelVertice(verticeB);
        List<Double> costos = this.listaDeCostos(posVerticeA);
        List<Boolean> marcados = super.inicializarMarcados();
        List<Integer> predecesores = this.listaDePredecesores();
        this.algoritmoDeDijkstra(costos, marcados, predecesores, posVerticeA, posVerticeB);
        return costos.get(posVerticeB);
    }

    public List<T> caminoDeCostoMinimo(T verticeA, T verticeB) {
        List<T> caminoMinimo = new LinkedList<>();
        if (!super.existeVertice(verticeA) || !super.existeVertice(verticeB)) {
            return caminoMinimo;
        }
        int posVerticeA = super.posicionDelVertice(verticeA);
        int posVerticeB = super.posicionDelVertice(verticeB);
        List<Double> costos = this.listaDeCostos(posVerticeA);
        List<Boolean> marcados = super.inicializarMarcados();
        List<Integer> predecesores = this.listaDePredecesores();
        this.algoritmoDeDijkstra(costos, marcados, predecesores, posVerticeA, posVerticeB);
        if (costos.get(posVerticeB) != GrafoP.INFINITO) {
            int i = posVerticeB;
            while (i != GrafoP.POSICION_INVALIDA) {
                T vertice = super.listaDeVertices.get(i);
                caminoMinimo.add(vertice);
                i = predecesores.get(i);
            }
            Collections.reverse(caminoMinimo);
        }
        return caminoMinimo;
    }

    protected void algoritmoDeDijkstra(List<Double> costos, List<Boolean> marcados,
            List<Integer> predecesores, int indiceDePartida, int indiceDeDestino) {
        int posVertMenorCostoNoMarcado = indiceDePartida;
        do {
            double costoV = costos.get(posVertMenorCostoNoMarcado);
            for (int i = 0; i < this.cantidadDeVertices(); i++) {
                if (!super.estaMarcadoElVertice(marcados, i)) {
                    double costoA = costos.get(i);
                    double pesoV_A = this.pesoEntreVertices(posVertMenorCostoNoMarcado, i);
                    if (this.comparacionAlgDijkstra(costoA, costoV, pesoV_A)) {
                        this.cambiarCostos(costos, i, costoV + pesoV_A);
                        this.cambiarPredecesores(predecesores, i, posVertMenorCostoNoMarcado);
                    }
                }
            }
            //marcando en vertice de menor costo
            super.marcarVertice(marcados, posVertMenorCostoNoMarcado);
            posVertMenorCostoNoMarcado = this.verticeMenorCostoNoMarcado(costos, marcados);
        } while (!super.estaMarcadoElVertice(marcados, indiceDeDestino) && posVertMenorCostoNoMarcado != -1);
    }

    protected boolean comparacionAlgDijkstra(double costoA, double costoV, double pesoV_A) {
        if (costoV == GrafoP.INFINITO || pesoV_A == GrafoP.INFINITO) {
            return false;
        }
        return costoA > (costoV + pesoV_A);
    }

    protected double pesoEntreVertices(int posVerticeA, int posVerticeB) {
        double retorno = GrafoP.INFINITO;
        List<AdyacenteConPeso> listaAdyVerticeA = super.listasDeAdyacencias.get(posVerticeA);
        if (listaAdyVerticeA.contains(new AdyacenteConPeso(posVerticeB))) {
            int indice = listaAdyVerticeA.indexOf(new AdyacenteConPeso(posVerticeB));
            AdyacenteConPeso adyacente = listaAdyVerticeA.get(indice);
            retorno = adyacente.getPeso();
        }
        return retorno;
    }

    protected int verticeMenorCostoNoMarcado(List<Double> costos, List<Boolean> marcados) {
        double menor = -1;
        int posicion = -1;
        for (int i = 0; i < costos.size(); i++) {
            if (!marcados.get(i)) {
                double costoIter = costos.get(i);
                if (posicion == -1) {
                    posicion = i;
                    menor = costos.get(i);
                } else if (costoIter < menor) {
                    posicion = i;
                    menor = costos.get(i);
                }
            }
        }
        return posicion;
    }

    protected List<Double> listaDeCostos(int posicionVerticePartida) {
        List<Double> costos = new LinkedList<>();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            if (i == posicionVerticePartida) {
                costos.add((Double) 0.0);
            } else {
                costos.add(INFINITO);
            }
        }
        return costos;
    }

    protected void cambiarCostos(List<Double> costos, int posicion, double valor) {
        costos.set(posicion, (Double) valor);
    }

    protected List<Integer> listaDePredecesores() {
        List<Integer> predecesores = new LinkedList<>();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            predecesores.add(-1);
        }
        return predecesores;
    }

    protected void cambiarPredecesores(List<Integer> predecesores, int posicion, int valor) {
        predecesores.set(posicion, valor);
    }

    protected Double[][] matrizInicialDeCostos() {
        int n = super.cantidadDeVertices();
        Double[][] matriz = new Double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = GrafoP.INFINITO;
            }
        }
        for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
            int f = i;
            matriz[f][f] = 0.0;
            List<AdyacenteConPeso> lista = this.listasDeAdyacencias.get(i);
            for (int j = 0; j < lista.size(); j++) {
                int c = lista.get(j).getIndiceDeVertice();
                matriz[f][c] = this.pesoEntreVertices(f, c);
            }
        }
        return matriz;
    }

    protected Integer[][] matrizInicialPredecesores() {
        int n = super.cantidadDeVertices();
        Integer[][] matriz = new Integer[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = GrafoP.POSICION_INVALIDA;
            }
        }
        return matriz;
    }

    protected void algoritmoDeFloyd(Double[][] costos, Integer[][] predecesores) {
        int n = super.cantidadDeVertices();
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double costoA = costos[i][j];
                    double costoV = costos[i][k];
                    double pesoV_A = costos[k][j];
                    if (this.comparacionAlgDijkstra(costoA, costoV, pesoV_A)) {
                        costos[i][j] = Math.min(costoA, costoV + pesoV_A);
                        predecesores[i][j] = k;
                    }
                }
            }
        }
    }

    public void showAlgoritmoDeFloyd(JTextArea text) {
        text.setText("Algoritmo de Floyd\nMatriz de Costos minimos\n");
        Double[][] matrizFloyd = this.matrizInicialDeCostos();
        Integer[][] predecesores = this.matrizInicialPredecesores();
        this.algoritmoDeFloyd(matrizFloyd, predecesores);
        int n = this.cantidadDeVertices();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Integer elem = matrizFloyd[i][j].intValue();
                String e = matrizFloyd[i][j] == GrafoP.INFINITO ? "∞" : elem.toString();
                text.setText(text.getText() + e + "   ");
            }
            text.setText(text.getText() + "\n");
        }
        text.setText(text.getText() + "\n\nMatriz de \n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Integer elem = predecesores[i][j];
                String e = elem.toString();
                text.setText(text.getText() + e + "   ");
            }
            text.setText(text.getText() + "\n");
        }
    }

    public List<T> caminoCostoMinimoFloyd(T verticeA, T verticeB) {
        List<T> camino = new LinkedList<>();
        int indA = super.posicionDelVertice(verticeA);
        int indB = super.posicionDelVertice(verticeB);
        if (indA >= 0 && indB >= 0) {
            Double[][] matrizFloyd = this.matrizInicialDeCostos();
            Integer[][] predecesores = this.matrizInicialPredecesores();
            this.algoritmoDeFloyd(matrizFloyd, predecesores);
            if (matrizFloyd[indA][indB] != GrafoP.INFINITO) {
                camino.add(verticeA);
                this.verificarIntermedio(indA, indB, camino, predecesores);
                camino.add(verticeB);
            }
        }
        return camino;
    }

    public void verificarIntermedio(int indA, int indB, List<T> camino, Integer[][] pred) {
        int puente = pred[indA][indB];
        if (puente != GrafoP.POSICION_INVALIDA) {
            this.verificarIntermedio(indA, puente, camino, pred);
            T vertice = super.listaDeVertices.get(puente);
            camino.add(vertice);
            this.verificarIntermedio(puente, indB, camino, pred);
        }
    }

    @Override
    public boolean hayCiclo(T verticeDePartida) {
        if (!existeVertice(verticeDePartida)) {
            return false;
        }
        List<T> recorrido = super.recorridoDfs(verticeDePartida);
        for (int i = 1; i < recorrido.size(); i++) {
            if (recorrido.get(i).compareTo(verticeDePartida) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hayCicloEnElGrafo() {
        boolean hayCiclo = false;
        int n = super.cantidadDeVertices();
        for (int i = 0; i < n && !hayCiclo; i++) {
            hayCiclo = hayCiclo || this.hayCiclo(listaDeVertices.get(i));
        }
        return hayCiclo;
    }

}
