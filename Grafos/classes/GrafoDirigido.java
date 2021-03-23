/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.classes;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author Willy
 * @param <T>
 */
public class GrafoDirigido<T extends Comparable<T>> extends Grafo<T> {

    public GrafoDirigido() {
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

    @Override
    public boolean hayCiclo(T verticeDePartida) {
        if (!existeVertice(verticeDePartida)) {
            return false;
        }
        int pos = this.posicionDelVertice(verticeDePartida);
        Boolean[][] matriz = this.matrizDeCaminosInicial();
        this.algoritmoDeWarshall(matriz);
        return matriz[pos][pos];
    }

    public boolean hayCicloDfs(T verticeDePartida) {
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
        if (super.cantidadDeVertices() == 0) {
            return false;
        }
        int n = this.cantidadDeVertices();
        Boolean[][] matriz = this.matrizDeCaminosInicial();
        this.algoritmoDeWarshall(matriz);
        boolean hayCiclo = false;
        for (int i = 0; i < n && !hayCiclo; i++) {
            hayCiclo = hayCiclo || matriz[i][i];

        }
        return hayCiclo;
    }

    protected Boolean[][] matrizDeCaminosInicial() {
        int n = this.cantidadDeVertices();
        Boolean[][] matrizInicial = new Boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizInicial[i][j] = Boolean.FALSE;
            }
        }
        for (int i = 0; i < this.listasDeAdyacencias.size(); i++) {
            int f = i;
            List<Integer> lista = this.listasDeAdyacencias.get(i);
            for (int j = 0; j < lista.size(); j++) {
                int c = lista.get(j);
                matrizInicial[f][c] = Boolean.TRUE;
            }
        }
        return matrizInicial;
    }

    private void algoritmoDeWarshall(Boolean[][] M) {
        int n = this.cantidadDeVertices();
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    M[i][j] = M[i][j] || (M[i][k] && M[k][j]);
                }
            }
        }
    }

    public void showAlgoritmoDeWarshall() {
        Boolean[][] matrizWarshall = this.matrizDeCaminosInicial();
        this.algoritmoDeWarshall(matrizWarshall);
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            Boolean[] fila = matrizWarshall[i];
            for (int j = 0; j < fila.length; j++) {
                int e = fila[j] ? 1 : 0;
                System.out.print(e + "  ");
            }
            System.out.println("");
        }
    }

    public void showAlgortimoDeWarshall(JTextArea text) {
        text.setText("Algoritmo de Warshall\n");
        Boolean[][] matrizWarshall = this.matrizDeCaminosInicial();
        this.algoritmoDeWarshall(matrizWarshall);
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            Boolean[] fila = matrizWarshall[i];
            for (int j = 0; j < fila.length; j++) {
                int e = fila[j] ? 1 : 0;
                text.setText(text.getText() + e + "   ");
            }
            text.setText(text.getText() + "\n");
        }
    }

    public List<T> ordenamientoTopologico() {
        List<T> ordenado = new LinkedList<>();
        List<Integer> gradosDeEntrada = this.gradosDeEntrada();
        List<Boolean> marcados = super.inicializarMarcados();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            int posicionAMeter = this.posicionDelVerticeGradoEntradaMenor(gradosDeEntrada, marcados);
            T verticeAMeter = this.listaDeVertices.get(posicionAMeter);
            ordenado.add(verticeAMeter);
        }
        return ordenado;
    }

    protected List<Integer> gradosDeEntrada() {
        List<Integer> grados = new LinkedList<>();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            grados.add(i, super.adyacentesAlVertice(i).size());
        }
        return grados;
    }

    protected int posicionDelVerticeGradoEntradaMenor(List<Integer> grados, List<Boolean> marcados) {
        if (grados.isEmpty()) {
            return POSICION_INVALIDA;
        }
        T verticeInicial = super.verticeNoMarcado(marcados);
        int retorno = super.posicionDelVertice(verticeInicial);
        Integer menor = grados.get(retorno);
        for (int i = 0; i < grados.size(); i++) {
            if (!super.estaMarcadoElVertice(marcados, i)) {
                if (grados.get(i) < menor) {
                    retorno = i;
                }
            }
        }
        super.marcarVertice(marcados, retorno);
        return retorno;
    }

}
