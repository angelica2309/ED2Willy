/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafos.UI;

import Grafos.classes.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Willy
 */
public class Test {

    public static void main(String[] args) {
        //PriorityQueue<Integer> cola = new PriorityQueue();
        Grafo grafo;
        grafo = new GrafoNoDirigido();
        //   menuGrafo(grafo);
    }
    
    public static void menuGrafo(Grafo grafo) {
        Scanner in = new Scanner(System.in);
        System.out.println("1.Insertar Vertice");
        System.out.println("2.Insertar Arista");
        System.out.println("3.Eliminar Vertice");
        System.out.println("4.Eliminar Arista");
        System.out.println("5.Cantidad de Vertices");
        System.out.println("6.Cantidad de Aristas");
        System.out.println("7.Recorrido Bfs");
        System.out.println("8.Recorrido Dfs");
        System.out.println("9.Salir");
        System.out.println("10.Prueba");
        int opcion;
        System.out.println("Opcion :");
        opcion = in.nextInt();
        switch (opcion) {
            case 1:
                System.out.println("Vertice a insertar: ");
                Integer e = (Integer) in.nextInt();
                System.out.println("Vertice insertado : " + grafo.insertarVertice(e));
                break;
            case 2:
                System.out.println("Vertice a: ");
                Integer a = (Integer) in.nextInt();
                System.out.println("Vertice b: ");
                Integer b = (Integer) in.nextInt();
                System.out.println("Arista insertada : " + grafo.insertarArista(a, b));
                break;
            case 3:
                System.out.println("Vertice a eliminar: ");
                Integer c = (Integer) in.nextInt();
                System.out.println("Vertice eliminado : " + grafo.eliminarVertice(c));
                break;
            case 4:
                System.out.println("Vertice a: ");
                Integer d = (Integer) in.nextInt();
                System.out.println("Vertice b: ");
                Integer f = (Integer) in.nextInt();
                System.out.println("Arista eliminada : " + grafo.eliminarArista(d, f));
                break;
            case 5:
                System.out.println("Cantidad de vertices: " + grafo.cantidadDeVertices());
                break;
            case 6:
                System.out.println("Cantidad de aristas: " + grafo.cantidadDeAristas());
                break;
            case 7:
                System.out.println("Vertice de partida");
                Integer v = in.nextInt();
                System.out.println("Recorrido Bfs" + grafo.recorridoBfs(v));
                break;
            case 8:
                System.out.println("Vertice de partida");
                Integer w = in.nextInt();
                System.out.println("Recorrido Dfs" + grafo.recorridoDfs(w));
                break;
            case 9:
                System.out.println("Finalizando programa...");
                break;
            case 10:
                System.out.println("Vertice de partida :");
                Integer x = (Integer) in.nextInt();
                /*List<List<Integer>> r = ((GrafoDirigido)grafo).islas();
                for(int i = 0;i < r.size();i++){
                    System.out.println(r.get(i));
                }*/
                System.out.println(((GrafoNoDirigido) grafo).hayCiclo(x));
                break;
            default:;
        }
        if (opcion != 9) {
            menuGrafo(grafo);
        }
    }

    public static void menuGrafoP(GrafoP grafo) {
        Scanner in = new Scanner(System.in);
        System.out.println("1.Insertar Vertice");
        System.out.println("2.Insertar Arista");
        System.out.println("3.Eliminar Vertice");
        System.out.println("4.Eliminar Arista");
        System.out.println("5.Cantidad de Vertices");
        System.out.println("6.Cantidad de Aristas");
        System.out.println("7.Recorrido Bfs");
        System.out.println("8.Recorrido Dfs");
        System.out.println("9.Salir");
        System.out.println("10.Prueba");
        System.out.println("Opcion :");
        int opcion = in.nextInt();
        switch (opcion) {
            case 1:
                System.out.println("Vertice a insertar: ");
                Integer e = (Integer) in.nextInt();
                System.out.println("Vertice insertado : " + grafo.insertarVertice(e));
                break;
            case 2:
                System.out.println("Vertice a: ");
                Integer a = (Integer) in.nextInt();
                System.out.println("Vertice b: ");
                Integer b = (Integer) in.nextInt();
                System.out.println("Costo :");
                int p = in.nextInt();
                System.out.println("Arista insertada : " + grafo.insertarArista(a, b, p));
                break;
            case 3:
                System.out.println("Vertice a eliminar: ");
                Integer c = (Integer) in.nextInt();
                System.out.println("Vertice eliminado : " + grafo.eliminarVertice(c));
                break;
            case 4:
                System.out.println("Vertice a: ");
                Integer d = (Integer) in.nextInt();
                System.out.println("Vertice b: ");
                Integer f = (Integer) in.nextInt();
                System.out.println("Arista eliminada : " + grafo.eliminarArista(d, f));
                break;
            case 5:
                System.out.println("Cantidad de vertices: " + grafo.cantidadDeVertices());
                break;
            case 6:
                System.out.println("Cantidad de aristas: " + grafo.cantidadDeAristas());
                break;
            case 7:
                System.out.println("Vertice de partida");
                Integer v = in.nextInt();
                System.out.println("Recorrido Bfs" + grafo.recorridoBfs(v));
                break;
            case 8:
                System.out.println("Vertice de partida");
                Integer w = in.nextInt();
                System.out.println("Recorrido Dfs" + grafo.recorridoDfs(w));
                break;
            case 9:
                System.out.println("Finalizando programa...");
                break;
            case 10:
                System.out.println("Vertice de partida :");
                Integer x = (Integer) in.nextInt();
                System.out.println("Vertice de llegada :");
                Integer y = (Integer) in.nextInt();
                /*List<List<Integer>> r = ((GrafoDirigido)grafo).islas();
                for(int i = 0;i < r.size();i++){
                    System.out.println(r.get(i));
                }*/
                List<Integer> camino = ((GrafoPDirigido) grafo).caminoDeCostoMinimo(x, y);
                System.out.println("Camino de Costo Minimo de:" + x + "->" + y + camino);
                break;
            default:;
        }
        if (opcion != 9) {
            menuGrafoP(grafo);
        }
    }
}
