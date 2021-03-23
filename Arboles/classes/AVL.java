/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.List;

/**
 *
 * @author Estudiante
 * @param <T>
 */
public final class AVL<T extends Comparable<T>> extends ArbolBinarioBusqueda<T> {

    private static final short RANGO_PERMITIDO = 1;
    
    public AVL(){
        
    }
    
    public AVL(List<T> recIn, List<T> recP, boolean tipoRec) {
            this.raiz = this.ReconstruirArbol(recIn, recP, tipoRec);
    }

    @Override
    protected NodoBinario<T> ReconstruirConPostOrden(List<T> recIn, List<T> recPost) {
        if (!recIn.isEmpty() && !recPost.isEmpty()) {
            if (recIn.size() == 1 && recPost.size() == 1) {
                NodoBinario<T> nodoActual = new NodoBinario<>(recIn.get(0));
                return balancear(nodoActual);
            }
            int indiceUltimo = recPost.size() - 1;
            T datoActual = recPost.get(indiceUltimo);
            //NodoBinario<T> nodoActual = new NodoBinario(datoActual);
            int indiceDelDatoActualInOrden = indiceDe(recIn, datoActual);
            //int indiceDelDatoInOrden = recIn.indexOf(datoActual);
            List<T> nuevoRecPostIzq = recPost.subList(0, indiceDelDatoActualInOrden);
            List<T> nuevoRecInIzq = recIn.subList(0, indiceDelDatoActualInOrden);
            NodoBinario<T> hijoIzq = balancear(this.ReconstruirConPostOrden(nuevoRecInIzq, nuevoRecPostIzq));
            List<T> nuevoRecPostDer = recPost.subList(indiceDelDatoActualInOrden, indiceUltimo);
            List<T> nuevoRecInDer = recIn.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            NodoBinario<T> hijoDer = balancear(this.ReconstruirConPostOrden(nuevoRecInDer, nuevoRecPostDer));
            NodoBinario<T> nodoActual = new NodoBinario<>(datoActual);
            nodoActual.setHijoIzquierdo(hijoIzq);
            nodoActual.setHijoDerecho(hijoDer);
            return balancear(nodoActual);
            //return ReconstruironPostOrden(nuevoRecIn nuevoRecPost);
        } else {
            return NodoBinario.nodoVacio();
        }
    }

    @Override
    protected NodoBinario<T> ReconstruirConPreOrden(List<T> recIn, List<T> recPre) {
        if (!recIn.isEmpty() && !recPre.isEmpty()) {
            if (recIn.size() == 1 && recPre.size() == 1) {
                NodoBinario<T> nodoActual = new NodoBinario(recIn.get(0));
                return balancear(nodoActual);
            }
            int indiceUltimo = recPre.size() - 1;
            T datoActual = recPre.get(0);
            //NodoBinario<T> nodoActual = new NodoBinario(datoActual);
            int indiceDelDatoActualInOrden = indiceDe(recIn, datoActual);
            //int indiceDelDatoInOrden = recIn.indexOf(datoActual);
            List<T> nuevoRecPreIzq = recPre.subList(1, indiceDelDatoActualInOrden + 1);
            List<T> nuevoRecInIzq = recIn.subList(0, indiceDelDatoActualInOrden);
            NodoBinario<T> hijoIzq = balancear(this.ReconstruirConPreOrden(nuevoRecInIzq, nuevoRecPreIzq));
            List<T> nuevoRecPreDer = recPre.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            List<T> nuevoRecInDer = recIn.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            NodoBinario<T> hijoDer = balancear(this.ReconstruirConPreOrden(nuevoRecInDer, nuevoRecPreDer));
            NodoBinario<T> nodoActual = new NodoBinario<>(datoActual);
            nodoActual.setHijoIzquierdo(hijoIzq);
            nodoActual.setHijoDerecho(hijoDer);
            return balancear(nodoActual);
        } else {
            return NodoBinario.nodoVacio();
        }
    }

    private NodoBinario<T> ReconstruirArbol(List<T> recIn, List<T> recP, boolean tipoRec) {
        if (recIn.isEmpty() && recP.isEmpty()) {
            return NodoBinario.nodoVacio();
        }
        return tipoRec ? ReconstruirConPreOrden(recIn, recP) : ReconstruirConPostOrden(recIn, recP);
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        try {
            this.raiz = this.insertarRec(raiz, datoAInsertar);
            //return this.insertarRec(raiz, datoAInsertar);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private NodoBinario<T> insertarRec(NodoBinario<T> unNodo, T datoAInsertar)
            throws Exception {
        if (this.esArbolVacio()) {
            raiz = new NodoBinario<>(datoAInsertar);
            return raiz;
        }
        if (NodoBinario.esNodoVacio(unNodo)) {
            unNodo.setDato(datoAInsertar);
            //unNodo = new NodoBinario<T>(datoAInsertar);
            return unNodo;
        } else {
            if (datoAInsertar.compareTo(unNodo.getDato()) == 0) {
                throw new Exception();
                //lanza una expecion para que retorne falso en la insercion
            }
            NodoBinario<T> nodoActual = new NodoBinario<>(datoAInsertar);
            if (datoAInsertar.compareTo(unNodo.getDato()) < 0) {
                if (unNodo.esHijoIzquierdoVacio()) {
                    unNodo.setHijoIzquierdo(nodoActual);
                    return balancear(unNodo);
                } else {
                    NodoBinario<T> nodoABalancear = insertarRec(unNodo.getHijoIzquierdo(), datoAInsertar);
                    unNodo.setHijoIzquierdo(nodoABalancear);
                    return balancear(unNodo);
                }
            } else {
                if (unNodo.esHijoDerechoVacio()) {
                    unNodo.setHijoDerecho(nodoActual);
                    return balancear(unNodo);
                } else {
                    //NodoBinario<T> nodoBalanceado = balancear(unNodo);
                    NodoBinario<T> nodoABalancear = insertarRec(unNodo.getHijoDerecho(), datoAInsertar);
                    unNodo.setHijoDerecho(nodoABalancear);
                    return balancear(unNodo);
                }
            }
        }
    }

    @Override
    public boolean eliminar(T datoAEliminar) {
        try {
            this.raiz = eliminar(this.raiz, datoAEliminar);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private NodoBinario<T> eliminar(NodoBinario<T> nodoActual, T datoAEliminar) throws Exception {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            throw new Exception();
            //return null;
        }
        T datoActual = nodoActual.getDato();
        if (datoAEliminar.compareTo(datoActual) < 0) {
            NodoBinario<T> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), datoAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }
        if (datoAEliminar.compareTo(datoActual) > 0) {
            NodoBinario<T> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), datoAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }
        //caso 1
        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }
        //caso 2
        if (!nodoActual.esHijoIzquierdoVacio() && nodoActual.esHijoDerechoVacio()) {
            return balancear(nodoActual.getHijoIzquierdo());
        }
        if (nodoActual.esHijoIzquierdoVacio() && !nodoActual.esHijoDerechoVacio()) {
            return balancear(nodoActual.getHijoDerecho());
        }
        //caso 3
        T datoSucesor = buscarSucesorInOrden(nodoActual.getHijoDerecho());
        NodoBinario<T> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), datoSucesor);
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setDato(datoSucesor);
        return balancear(nodoActual);
    }

    private NodoBinario<T> balancear(NodoBinario<T> nodoActual) {
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            int dif = this.diferenciaAlturaDeNodosHijos(nodoActual);
            if (dif > RANGO_PERMITIDO
                    || dif < -RANGO_PERMITIDO) {
                if (dif > 0) {
                    NodoBinario<T> nodoHijo = nodoActual.getHijoIzquierdo();
                    int difIzq = Math.abs(this.diferenciaAlturaDeNodosHijos(nodoHijo.getHijoIzquierdo()));
                    int difDer = Math.abs(this.diferenciaAlturaDeNodosHijos(nodoHijo.getHijoDerecho()));
                    if (difIzq >= difDer) {
                        return this.rotacionSimpleDer(nodoActual);
                    } else {
                        return this.rotacionDobleIzq(nodoActual);
                    }
                } else {
                    NodoBinario<T> nodoHijo = nodoActual.getHijoDerecho();
                    int difIzq = Math.abs(this.diferenciaAlturaDeNodosHijos(nodoHijo.getHijoIzquierdo()));
                    int difDer = Math.abs(this.diferenciaAlturaDeNodosHijos(nodoHijo.getHijoDerecho()));
                    if (difDer >= difIzq) {
                        return this.rotacionSimpleDer(nodoActual);
                    } else {
                        return this.rotacionDobleIzq(nodoActual);
                    }
                }
            }
            return nodoActual;
        }
        return NodoBinario.nodoVacio();
    }

    private int diferenciaAlturaDeNodosHijos(NodoBinario<T> nodoActual) {
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            int altHijoDerecho = this.alturaRecursivo(nodoActual.getHijoDerecho());
            int altHijoIzquierdo = this.alturaRecursivo(nodoActual.getHijoIzquierdo());
            return altHijoIzquierdo - altHijoDerecho;
        }
        return 0;
    }
    
    private NodoBinario<T> rotacionSimpleIzq(NodoBinario nodoARotar) {
        NodoBinario<T> nodoRotarAux = new NodoBinario<>(nodoARotar);
        NodoBinario<T> nodoNuevoPadre = new NodoBinario<>(nodoARotar.getHijoDerecho());
        if (!nodoNuevoPadre.esHijoIzquierdoVacio()) {
            NodoBinario<T> nodoRotHijoDer = new NodoBinario<>(nodoNuevoPadre.getHijoIzquierdo());
            nodoRotarAux.setHijoDerecho(nodoRotHijoDer);
        } else {
            nodoRotarAux.setHijoDerechoVacio();
        }
        nodoARotar = nodoARotar.getHijoDerecho();
        nodoARotar.setHijoIzquierdo(nodoRotarAux);
        //raiz.setHijoDerecho(nodoARotar);
        return nodoARotar;
    }

    private NodoBinario<T> rotacionSimpleDer(NodoBinario<T> nodoARotar) {
        NodoBinario<T> nodoRotarAux = new NodoBinario<>(nodoARotar);
        NodoBinario<T> nodoNuevoPadre = new NodoBinario<>(nodoARotar.getHijoIzquierdo());
        if (!nodoNuevoPadre.esHijoDerechoVacio()) {
            NodoBinario<T> nodoRotHijoIzq = new NodoBinario<>(nodoNuevoPadre.getHijoDerecho());
            nodoRotarAux.setHijoIzquierdo(nodoRotHijoIzq);
        } else {
            nodoRotarAux.setHijoIzquierdoVacio();
        }
        nodoARotar = nodoARotar.getHijoIzquierdo();
        nodoARotar.setHijoDerecho(nodoRotarAux);
        //raiz.setHijoIzquierdo(nodoARotar);
        return nodoARotar;
    }

    private NodoBinario<T> rotacionDobleIzq(NodoBinario<T> nodoARotar) {
        NodoBinario<T> nodoRotacion1 = rotacionSimpleDer(nodoARotar.getHijoDerecho());
        nodoARotar.setHijoDerecho(nodoRotacion1);
        NodoBinario<T> nodoRotacion2 = rotacionSimpleIzq(nodoARotar);
        return nodoRotacion2;
    }

    private NodoBinario<T> rotacionDobleDer(NodoBinario<T> nodoARotar) {
        NodoBinario<T> nodoRotacion1 = rotacionSimpleIzq(nodoARotar.getHijoIzquierdo());
        nodoARotar.setHijoIzquierdo(nodoRotacion1);
        NodoBinario<T> nodoRotacion2 = rotacionSimpleDer(nodoARotar);
        //raiz.setHijoDerecho(nodoRotacion2);
        return nodoRotacion2;
    }

}
