/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;


/**
 *
 * @author Willy
 * @param <T>
 */
public class NodoBinario<T> {

    private T dato;
    private NodoBinario<T> hijoIzquierdo;
    private NodoBinario<T> hijoDerecho;

    public NodoBinario() {
    }

    public NodoBinario(NodoBinario<T> nodoCopia) {
        if (!NodoBinario.esNodoVacio(nodoCopia)) {
            this.dato = nodoCopia.dato;
            this.hijoIzquierdo = nodoCopia.hijoIzquierdo;
            this.hijoDerecho = nodoCopia.hijoDerecho;
        }
    }

    public NodoBinario(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoBinario<T> getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoBinario<T> hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoBinario<T> getHijoDerecho() {
        return hijoDerecho;
    }

    public void setHijoDerecho(NodoBinario<T> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    public static NodoBinario nodoVacio() { //Metodos estaticos no son genericos
        return null;
    }

    public void setHijoDerechoVacio() {
        this.setHijoDerecho(NodoBinario.nodoVacio());
    }

    public void setHijoIzquierdoVacio() {
        this.setHijoIzquierdo(NodoBinario.nodoVacio());
    }

    public static boolean esNodoVacio(NodoBinario unNodo) {
        return unNodo == NodoBinario.nodoVacio();
    }

    public boolean esHijoIzquierdoVacio() {
        return NodoBinario.esNodoVacio(this.hijoIzquierdo);
    }

    public boolean esHijoDerechoVacio() {
        return NodoBinario.esNodoVacio(this.hijoDerecho);
    }

    public boolean esHoja() {
        return this.esHijoIzquierdoVacio() && this.esHijoDerechoVacio();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj instanceof NodoBinario) {
            NodoBinario<T> otroNodo = (NodoBinario<T>) obj;
            if (this.dato.equals(otroNodo.dato)) {
                boolean b1 = false;
                if ((NodoBinario.esNodoVacio(this.hijoIzquierdo)
                        && NodoBinario.esNodoVacio(otroNodo.hijoIzquierdo))
                        || (this.hijoIzquierdo.equals(otroNodo.hijoIzquierdo))) {
                    b1 = true;
                } else {
                    return false;
                }
                if (b1) {
                    return (NodoBinario.esNodoVacio(this.hijoDerecho)
                            && NodoBinario.esNodoVacio(otroNodo.hijoDerecho))
                            || (this.hijoDerecho.equals(otroNodo.hijoDerecho));
                }
            }
        }
        return false;

//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final NodoBinario<?> other = (NodoBinario<?>) obj;
//        if (!Objects.equals(this.dato, other.dato)) {
//            return false;
//        }
//        if (!Objects.equals(this.hijoIzquierdo, other.hijoIzquierdo)) {
//            return false;
//        }
//        if (!Objects.equals(this.hijoDerecho, other.hijoDerecho)) {
//            return false;
//        }
//        return true;
    }
    
    public void clonar(NodoBinario<T> nodoClon){
        this.dato = nodoClon.dato;
        this.hijoDerecho = nodoClon.hijoDerecho;
        this.hijoIzquierdo = nodoClon.hijoIzquierdo;
    }

}
