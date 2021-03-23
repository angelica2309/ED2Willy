/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.UI;

/**
 *
 * @author Willy
 */
import Arboles.classes.*;
import java.util.*;

public class TestArbol {

    public static void main(String[] args) throws ExcepcionOrdenInvalido {
        /*AVL<Integer> arbol = new AVL<>();
        arbol.insertar(4);
        arbol.insertar(2);
        arbol.insertar(6);
        arbol.insertar(3);
        arbol.insertar(5);
        arbol.insertar(1);
        arbol.insertar(17);
        arbol.insertar(12);
        arbol.insertar(10);
        arbol.insertar(11);
        arbol.mostrar();
        arbol.eliminar(12);
        arbol.eliminar(10);
        arbol.mostrar();
        return;*/
        IArbolBusqueda<Integer> arbol;
        System.out.println("Elija un tipo de arbol: ABB,AVL,AMV ó AB");
        Scanner in = new Scanner(System.in);
        String opcion = in.nextLine().toUpperCase();
        switch (opcion) {
            case "ABB":
                arbol = new ArbolBinarioBusqueda<>();
                break;
            case "AVL":
                arbol = new AVL<>();
                break;
            case "AMV":
                arbol = new ArbolMViasBusqueda<>();
                break;
            case "AB":
                arbol = new ArbolB<>(4);
                break;
            default:
                arbol = new ArbolBinarioBusqueda<>();

        }
        Integer[] datos = {300, 500, 100, 50, 400, 800, 90, 91, 70, 75, 99};
        Integer[] datos1 = {90, 850, 70, 100, 400, 870, 920, 50, 75, 91, 99, 300, 500, 800, 855, 862, 868, 890, 950, 960};
        Integer[] datosElim = {100, 300, 400, 91, 800, 70, 90, 500};
        Integer[] datosElim1 = {800, 300, 100, 890, 920, 855, 500, 960, 862, 91, 400, 50, 870, 99, 91};
        Integer[] delArbol1 = {90, 70, 100, 75, 95, 110};
        Integer[] delArbol2 = {40, 10, 50, 20, 45, 70};
        ArbolBinarioBusqueda<Integer> arbol1 = new ArbolBinarioBusqueda<>();
        ArbolBinarioBusqueda<Integer> arbol2 = new ArbolBinarioBusqueda<>();
        /*for (int i = 0; i < 6; i++) {
            arbol1.insertar(delArbol1[i]);
            arbol2.insertar(delArbol2[i]);
        }
        System.out.println("Arboles similares: " + arbol1.arbolesSimilares(arbol2));*/
        //((ArbolB)arbol).Prueba();
        for (Integer dato : datos1) {
            arbol.insertar(dato);
        }
        ((ArbolB) arbol).mostrar();/*
//        ((ArbolB) arbol).setDatosDePrueba();
        for (Integer dato : datosElim1) {
            arbol.eliminar(dato);
            System.out.println("Dato eliminado " + String.valueOf(dato));
            ((ArbolB) arbol).mostrar();
        }*/
        menu(arbol);
    }

    public static void menu(IArbolBusqueda a) {
        System.out.println("1.Insertar");
        System.out.println("2.Eliminar");
        System.out.println("3.Mostrar");
        System.out.println("4.Recorrido por Niveles");
        System.out.println("5.Recorrido PreOrden");
        System.out.println("6.Recorrido InOrden");
        System.out.println("7.Recorrido PostOrden");
        System.out.println("8.Altura");
        System.out.println("9.Size");
        System.out.println("10.Nivel");
        System.out.println("11.Salir");
        int opcion;
        Scanner in = new Scanner(System.in);
        opcion = in.nextInt();
        switch (opcion) {
            case 1: {
                System.out.println("Ingrese el elemento a ingresar:");
                Integer elemento = in.nextInt();
                a.insertar(elemento);
            }
            break;
            case 2: {
                System.out.println("Ingrese el elemento a eliminar:");
                Integer elemento = in.nextInt();
                a.eliminar(elemento);
            }
            break;
            case 3: {
                if (a instanceof ArbolBinarioBusqueda || a instanceof AVL) {
                    //((ArbolBinarioBusqueda) a).mostrar();
                    System.out.println(a.arbolGrafico());
                } else if (a instanceof ArbolMViasBusqueda) {
                    ((ArbolMViasBusqueda) a).mostrar();
                }
            }
            break;
            case 4:
                System.out.println("Recorrido por niveles: " + a.recorridoPorNiveles());
                break;
            case 5:
                System.out.println("Recorrido en PreOrden: " + a.recorridoEnPreOrden());
                break;
            case 6:
                System.out.println("Recorrido en InOrden: " + a.recorridoEnInOrden());
                break;
            case 7:
                System.out.println("Recorrido en PostOrden: " + a.recorridoEnPostOrden());
                break;
            case 8:
                System.out.println("Altura: " + a.altura());
                ((ArbolBinarioBusqueda) a).invertirArbol();
                break;
            case 9:
                System.out.println("Size: " + a.size());
                break;
            case 10:
                System.out.println("Nivel: " + a.nivel());
                break;
            case 11:
                System.out.println("Finalizando programa...");
                break;
            default:
                menu(a);
        }
        if (opcion != 11) {
            menu(a);
        }
    }

}
