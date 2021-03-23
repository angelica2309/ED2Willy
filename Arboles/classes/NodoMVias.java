/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Willy
 * @param <T>
 */
public class NodoMVias<T> {

    private List<T> listaDeDatos;
    private List<NodoMVias<T>> listaDeHijos;
    
    public NodoMVias(int orden) {
        this.listaDeDatos = new LinkedList<>();
        this.listaDeHijos = new LinkedList<>();
        for (int i = 0; i < orden - 1; i++) {
            listaDeDatos.add((T) NodoMVias.datoVacio());
            listaDeHijos.add(NodoMVias.nodoVacio());
        }
        listaDeHijos.add(NodoMVias.nodoVacio());
    }

    public NodoMVias(int orden, T dato) {
        this(orden);
        listaDeDatos.set(0, dato);
    }

    public static NodoMVias nodoVacio() {
        return null;
    }

    public static Object datoVacio() {
        return null;
    }

    public T getDato(int posicion) {
        return listaDeDatos.get(posicion);
    }

    public NodoMVias<T> getHijo(int posicion) {
        return listaDeHijos.get(posicion);
    }

    public void setDato(int posicion, T dato) {
        this.listaDeDatos.set(posicion, dato);
    }

    public void setHijo(int posicion, NodoMVias<T> nodoHijo) {
        this.listaDeHijos.set(posicion, nodoHijo);
    }

    public boolean esDatoVacio(int posicion) {
        return this.getDato(posicion) == NodoMVias.datoVacio();
    }

    public boolean esHijoVacio(int posicion) {
        return this.getHijo(posicion) == NodoMVias.nodoVacio();
    }

    public static boolean esNodoVacio(NodoMVias nodo) {
        return nodo == NodoMVias.nodoVacio();
    }

    public boolean esHoja() {
        for (int i = 0; i < listaDeHijos.size(); i++) {
            if (!this.esHijoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean estanDatosLLenos() {
        for (T unDato : this.listaDeDatos) {
            if (unDato == NodoMVias.datoVacio()) {
                return false;
            }
        }
        return true;
    }

    public boolean estanDatosVacios() {
        for (int i = 0; i < listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                return false;
            }
        }
        return true;
    }

    public int cantidadDeDatosNoVacios() {
        int cantidadDeDatos = 0;
        for (int i = 0; i < listaDeDatos.size(); i++) {
            if (!this.esDatoVacio(i)) {
                cantidadDeDatos++;
            }
        }
        return cantidadDeDatos;
    }   
    
    public int cantidadDeHijosNoNulos(){
        int cantidadDeHijos = 0;
        for(int i = 0;i < listaDeHijos.size();i++){
            NodoMVias<T> hijo = this.getHijo(i);
            if(!NodoMVias.esNodoVacio(hijo) && hijo.cantidadDeDatosNoVacios() > 0){
                cantidadDeHijos++;
            }
        }
        return cantidadDeHijos;
    }
}
