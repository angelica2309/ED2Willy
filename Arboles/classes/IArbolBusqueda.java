/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.List;

/**
 *
 * @author Willy
 * @param <T>
 */
public interface IArbolBusqueda <T extends Comparable<T>>{
    boolean insertar(T dato);
    boolean eliminar(T dato);
    T buscar(T dato);
    boolean contiene(T dato);
    List<T> recorridoEnInOrden();
    List<T> recorridoEnPreOrden();
    List<T> recorridoEnPostOrden();
    List<T> recorridoPorNiveles();
    int size();
    int altura();
    void vaciar();
    boolean esArbolVacio();
    int nivel();
    String arbolGrafico();
}