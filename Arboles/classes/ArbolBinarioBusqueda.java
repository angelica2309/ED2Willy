/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Willy
 * @param <T>
 *
 */
public class ArbolBinarioBusqueda<T extends Comparable<T>>
        implements IArbolBusqueda<T> {

    protected NodoBinario<T> raiz;

    public ArbolBinarioBusqueda() {
    }

    public ArbolBinarioBusqueda(List<T> recIn, List<T> recP, boolean tipoRec) {
        this.raiz = this.ReconstruirArbol(recIn, recP, tipoRec);
    }

    /**
     * Precondicion : la Listas deben tener los recorridos correctos.
     *
     * @param recIn es el recorrido InOrden del arbol a construir
     * @param recP es el recorrido PreOrden o PostOrden del arbol a construir
     * @param tipoRec es el tipo de rec <STRONG>Verdadero<STRONG> = recPreOrden,
     * false = recPostOrden arbol a construir
     * @return
     *
     */
    private NodoBinario<T> ReconstruirArbol(List<T> recIn, List<T> recP, boolean tipoRec) {
        if (recIn.isEmpty() && recP.isEmpty()) {
            return NodoBinario.nodoVacio();
        }
        if (tipoRec) {
            return this.ReconstruirConPreOrden(recIn, recP);
        } else {
            return this.ReconstruirConPostOrden(recIn, recP);
        }
    }

    protected NodoBinario<T> ReconstruirConPreOrden(List<T> recIn, List<T> recPre) {
        if (!recIn.isEmpty() && !recPre.isEmpty()) {
            if (recIn.size() == 1 && recPre.size() == 1) {
                NodoBinario<T> nodoActual = new NodoBinario(recIn.get(0));
                return nodoActual;
            }
            int indiceUltimo = recPre.size() - 1;
            T datoActual = recPre.get(0);
            //NodoBinario<T> nodoActual = new NodoBinario(datoActual);
            int indiceDelDatoActualInOrden = indiceDe(recIn, datoActual);
            //int indiceDelDatoInOrden = recIn.indexOf(datoActual);
            List<T> nuevoRecPreIzq = recPre.subList(1, indiceDelDatoActualInOrden + 1);
            List<T> nuevoRecInIzq = recIn.subList(0, indiceDelDatoActualInOrden);
            NodoBinario<T> hijoIzq = this.ReconstruirConPreOrden(nuevoRecInIzq, nuevoRecPreIzq);
            List<T> nuevoRecPreDer = recPre.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            List<T> nuevoRecInDer = recIn.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            NodoBinario<T> hijoDer = this.ReconstruirConPreOrden(nuevoRecInDer, nuevoRecPreDer);
            NodoBinario<T> nodoActual = new NodoBinario<>(datoActual);
            nodoActual.setHijoIzquierdo(hijoIzq);
            nodoActual.setHijoDerecho(hijoDer);
            return nodoActual;
        } else {
            return NodoBinario.nodoVacio();
        }
    }

    protected int indiceDe(List<T> recorrido, T datoActual) {
        if (recorrido.isEmpty()) {
            return -1;
        }
        int c = 0;
        List<T> recorridoNuevo = new LinkedList<>();
        recorridoNuevo.addAll(recorrido);
        while (!recorridoNuevo.isEmpty()) {
            if (recorridoNuevo.get(0).compareTo(datoActual) == 0) {
                return c;
            }
            c++;
            recorridoNuevo.remove(0);
        }
        return -1;
    }

    protected NodoBinario<T> ReconstruirConPostOrden(List<T> recIn, List<T> recPost) {
        if (!recIn.isEmpty() && !recPost.isEmpty()) {
            if (recIn.size() == 1 && recPost.size() == 1) {
                NodoBinario<T> nodoActual = new NodoBinario<>(recIn.get(0));
                return nodoActual;
            }
            int indiceUltimo = recPost.size() - 1;
            T datoActual = recPost.get(indiceUltimo);
            //NodoBinario<T> nodoActual = new NodoBinario(datoActual);
            int indiceDelDatoActualInOrden = indiceDe(recIn, datoActual);
            //int indiceDelDatoInOrden = recIn.indexOf(datoActual);
            List<T> nuevoRecPostIzq = recPost.subList(0, indiceDelDatoActualInOrden);
            List<T> nuevoRecInIzq = recIn.subList(0, indiceDelDatoActualInOrden);
            NodoBinario<T> hijoIzq = this.ReconstruirConPostOrden(nuevoRecInIzq, nuevoRecPostIzq);
            List<T> nuevoRecPostDer = recPost.subList(indiceDelDatoActualInOrden, indiceUltimo);
            List<T> nuevoRecInDer = recIn.subList(indiceDelDatoActualInOrden + 1, indiceUltimo + 1);
            NodoBinario<T> hijoDer = this.ReconstruirConPostOrden(nuevoRecInDer, nuevoRecPostDer);
            NodoBinario<T> nodoActual = new NodoBinario<>(datoActual);
            nodoActual.setHijoIzquierdo(hijoIzq);
            nodoActual.setHijoDerecho(hijoDer);
            return nodoActual;
            //return ReconstruironPostOrden(nuevoRecIn nuevoRecPost);
        } else {
            return NodoBinario.nodoVacio();
        }
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(datoAInsertar);
//            this.raiz = new NodoBinario<T>(datoAInsertar);
            return true;
        } else {
            NodoBinario<T> nodoAnterior = NodoBinario.nodoVacio();
            NodoBinario<T> nodoActual = this.raiz;
            while (!NodoBinario.esNodoVacio(nodoActual)) {
                T datoActual = nodoActual.getDato();
                if (datoAInsertar.compareTo(datoActual) == 0) {
                    return false;
                }
                if (datoAInsertar.compareTo(datoActual) < 0) {
                    nodoAnterior = nodoActual;
                    nodoActual = nodoActual.getHijoIzquierdo();
                } else {
                    nodoAnterior = nodoActual;
                    nodoActual = nodoActual.getHijoDerecho();
                }
            }
            NodoBinario<T> nuevoNodo = new NodoBinario<>(datoAInsertar);
            T datoDelPadre = nodoAnterior.getDato();
            if (datoAInsertar.compareTo(datoDelPadre) < 0) {
                nodoAnterior.setHijoIzquierdo(nuevoNodo);
            } else {
                nodoAnterior.setHijoDerecho(nuevoNodo);
            }
            return true;
        }
    }

    private boolean insertarRec(NodoBinario<T> unNodo, T datoAInsertar) {
        if (this.esArbolVacio()) {
            raiz = new NodoBinario<>(datoAInsertar);
            return true;
        }
        if (NodoBinario.esNodoVacio(unNodo)) {
            unNodo.setDato(datoAInsertar);
            return true;
        } else {
            if (datoAInsertar.compareTo(unNodo.getDato()) == 0) {
                return false;
            }
            NodoBinario<T> nodoActual = new NodoBinario<>(datoAInsertar);
            if (datoAInsertar.compareTo(unNodo.getDato()) < 0) {
                if (unNodo.esHijoIzquierdoVacio()) {
                    unNodo.setHijoIzquierdo(nodoActual);
                    return true;
                } else {
                    return insertarRec(unNodo.getHijoIzquierdo(), datoAInsertar);
                }
            } else {
                if (unNodo.esHijoDerechoVacio()) {
                    unNodo.setHijoDerecho(nodoActual);
                    return true;
                } else {
                    return insertarRec(unNodo.getHijoDerecho(), datoAInsertar);
                }
            }
        }
    }

    public boolean insertarRecursivo(T datoAInsertar) {
        return this.insertarRec(raiz, datoAInsertar);
    }

    public void insertar(T[] Vect) {
        for (T Vect1 : Vect) {
            this.insertarRec(raiz, Vect1);
        }
    }

    @Override
    public boolean eliminar(T dato) {
        try {
            this.raiz = eliminar(this.raiz, dato);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private NodoBinario<T> eliminar(NodoBinario<T> nodoActual, T datoAEliminar) throws Exception {
        // throws Exception {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            throw new Exception();
            //return null;
        }
        T datoActual = nodoActual.getDato();
        if (datoAEliminar.compareTo(datoActual) < 0) {
            NodoBinario<T> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), datoAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return nodoActual;
        }
        if (datoAEliminar.compareTo(datoActual) > 0) {
            NodoBinario<T> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), datoAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return nodoActual;
        }
        //caso 1
        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }
        //caso 2
        if (!nodoActual.esHijoIzquierdoVacio() && nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoIzquierdo();
        }
        if (nodoActual.esHijoIzquierdoVacio() && !nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoDerecho();
        }
        //caso 3
        T datoSucesor = buscarSucesorInOrden(nodoActual.getHijoDerecho());
        NodoBinario<T> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), datoSucesor);
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setDato(datoSucesor);
        return nodoActual;
        //    }
    }

    protected T buscarSucesorInOrden(NodoBinario<T> nodoActual) {
        //NodoBinario<T> nodoNuevo = nodoActual.getHijoDerecho();
        NodoBinario<T> nodoNuevo = nodoActual;
        while (!NodoBinario.esNodoVacio(nodoNuevo.getHijoIzquierdo())) {
            nodoNuevo = nodoNuevo.getHijoIzquierdo();
        }
        return nodoNuevo.getDato();
    }

    @Override
    public T buscar(T datoABuscar) {
        NodoBinario<T> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            T datoActual = nodoActual.getDato();
            if (datoABuscar.compareTo(datoActual) == 0) {
                return datoActual;
            }
            if (datoABuscar.compareTo(datoActual) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        return null;
    }

    @Override
    public boolean contiene(T dato) {
        return this.buscar(dato) != null;
    }

    @Override
    public List<T> recorridoEnInOrden() {
        List<T> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<T>> pilaDeNodos = new Stack<>();
        NodoBinario<T> nodoActual = raiz.getHijoIzquierdo();
        pilaDeNodos.push(raiz);
        while (!NodoBinario.esNodoVacio(nodoActual) || !pilaDeNodos.isEmpty()) {
            if (!NodoBinario.esNodoVacio(nodoActual)) {
                pilaDeNodos.push(nodoActual);
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getDato());
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
        return recorrido;
    }

    @Override
    public List<T> recorridoEnPreOrden() {  //Usa una pila
        List<T> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Stack<NodoBinario<T>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(raiz);    //push ingresa un dato a la pila
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = pilaDeNodos.pop(); // pop elimina y retorna el tope de la pila
            recorrido.add(nodoActual.getDato());
            if (!nodoActual.esHijoDerechoVacio()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esHijoIzquierdoVacio()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }

        }
        return recorrido;
    }

    public List<T> recorridoEnPreOrdenVersionR() {
        List<T> recorrido = new ArrayList<>();
        this.recorridoEnPreOrdenVersionR(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrdenVersionR(NodoBinario<T> nodoActual, List<T> recorrido) {
        //simulador n == 0
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        recorrido.add(nodoActual.getDato());
        recorridoEnPreOrdenVersionR(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoEnPreOrdenVersionR(nodoActual.getHijoDerecho(), recorrido);
    }

    public List<T> recorridoEnInOrdenVersionRec() {
        List<T> recorrido = new ArrayList<>();
        this.recorridoEnInOrdenRec(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrdenRec(NodoBinario<T> nodoActual, List<T> recorrido) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoEnInOrdenRec(nodoActual.getHijoIzquierdo(), recorrido);
        recorrido.add(nodoActual.getDato());
        recorridoEnInOrdenRec(nodoActual.getHijoDerecho(), recorrido);
    }

    public List<T> recorridoEnPostOrdenVersionRec() {
        List<T> recorrido = new ArrayList<>();
        this.recorridoEnPostOrdenRec(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrdenRec(NodoBinario<T> nodoActual, List<T> recorrido) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoEnPostOrdenRec(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoEnPostOrdenRec(nodoActual.getHijoDerecho(), recorrido);
        recorrido.add(nodoActual.getDato());
    }

    @Override
    public List<T> recorridoEnPostOrden() {//Implementar este metodo día jueves
        List<T> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        if (this.size() == 1) {
            recorrido.add(raiz.getDato());
            return recorrido;
        }
        Stack<NodoBinario<T>> pilaDeNodos = new Stack<>();
        NodoBinario<T> nodoActual;
        NodoBinario<T> nodoDelTope;
        meterPostOrden(pilaDeNodos, raiz);
        nodoActual = pilaDeNodos.peek();
        recorrido.add(nodoActual.getDato());
        pilaDeNodos.pop();
        nodoDelTope = pilaDeNodos.peek();
        while (!pilaDeNodos.isEmpty()) {
            if (!nodoDelTope.esHijoDerechoVacio()) {
                if (nodoActual != nodoDelTope.getHijoDerecho()) {
                    meterPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());
                    nodoActual = pilaDeNodos.peek();
                    recorrido.add(nodoActual.getDato());
                    pilaDeNodos.pop();
                    if (!pilaDeNodos.isEmpty()) {
                        nodoDelTope = pilaDeNodos.peek();
                    }
                } else {
                    recorrido.add(nodoDelTope.getDato());
                    nodoActual = pilaDeNodos.pop();
                    if (!pilaDeNodos.isEmpty()) {
                        nodoDelTope = pilaDeNodos.peek();
                    }
                }
            } else {
                recorrido.add(nodoDelTope.getDato());
                nodoActual = pilaDeNodos.pop();
                if (!pilaDeNodos.isEmpty()) {
                    nodoDelTope = pilaDeNodos.peek();
                }
            }

        }
        return recorrido;
    }

    private void meterPostOrden(Stack<NodoBinario<T>> pilaDeNodos, NodoBinario<T> nodoActual) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esHijoIzquierdoVacio()) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }

//    public List<T> recorridoEnPostOrdenV2() {  //Usa una pila
//        List<T> recorrido = new ArrayList<>();
//        if (this.esArbolVacio()) {
//            return recorrido;
//        }
//        Stack<NodoBinario<T>> pilaDeNodos = new Stack<>();
//        NodoBinario<T> nodoActual = raiz;
//        boolean b = false;
//        do {
//            b = false;
//            if (!NodoBinario.esNodoVacio(nodoActual.getHijoIzquierdo())) {
//                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
//                nodoActual = nodoActual.getHijoIzquierdo();
//                if (!NodoBinario.esNodoVacio(nodoActual.getHijoIzquierdo())) {
//                    pilaDeNodos.push(nodoActual.getHijoIzquierdo());
//                } else if (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())) {
//                    pilaDeNodos.push(nodoActual.getHijoDerecho());
//                } else {
//                    recorrido.add(nodoActual.getDato());
//                    b = true;
//                }
////                if (b) {
////                    nodoActual = 
////                } else {
//                nodoActual = pilaDeNodos.peek();
////                }
//            } else if (!NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())) {
//                nodoActual = nodoActual.getHijoDerecho();
//            }
//        } while (!pilaDeNodos.isEmpty());
//        return recorrido;
//    }
    @Override
    public List<T> recorridoPorNiveles() {  //Usa una cola
        List<T> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
            recorrido.add(nodoActual.getDato());
            if (!nodoActual.esHijoIzquierdoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    @Override
    public int size() {
        int cantidadDeNodos = 0;
        if (this.esArbolVacio()) {
            return 0;
        }
        Queue<NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
            cantidadDeNodos++;
            if (!nodoActual.esHijoIzquierdoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return cantidadDeNodos;
    }

    public int sizeRec() {
        return this.sizeRecursivo(raiz);
    }

    protected int sizeRecursivo(NodoBinario<T> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        } else {
            int sizePorDerecha = sizeRecursivo(nodoActual.getHijoDerecho());
            int sizePorIzquierda = sizeRecursivo(nodoActual.getHijoIzquierdo());
            return sizePorDerecha + sizePorIzquierda + 1;
        }
    }

    @Override
    public int altura() {
        return this.alturaRecursivo(raiz);
    }

    public int alturaIterativo() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int alturaDelArbol = 0;
        Queue<NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            int nroDeNodosDelNivel = colaDeNodos.size();
            while (nroDeNodosDelNivel > 0) {
                NodoBinario<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                nroDeNodosDelNivel--;
            }
            alturaDelArbol++;
        }
        return alturaDelArbol;
    }

    protected int alturaRecursivo(NodoBinario<T> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int altDer = this.alturaRecursivo(nodoActual.getHijoDerecho());
        int altIzq = this.alturaRecursivo(nodoActual.getHijoIzquierdo());
        return 1 + Integer.max(altDer, altIzq);
    }

    @Override
    public void vaciar() {
        raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return this.altura() - 1;
    }

    public void mostrar() {
        this.mostrar(raiz, 0, true);
    }

    protected void mostrar(NodoBinario<T> nodoActual, int cont, boolean izquierdo) {
        if (nodoActual == raiz) {
            System.out.println("Arbol forma grafica");
        }
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            mostrar(nodoActual.getHijoDerecho(), cont + 1, false);
            for (int i = 0; i < cont; i++) {
                System.out.print("        ");
            }
            String hijo = nodoActual == raiz ? "[R]" : izquierdo ? "[I]" : "[D]";
            String impre = nodoActual.getDato().toString();
            System.out.println(impre + hijo);
            mostrar(nodoActual.getHijoIzquierdo(), cont + 1, true);
        }
    }

    @Override
    public String arbolGrafico() {
        return arbolGrafico(raiz, 0, true);
    }

    protected String arbolGrafico(NodoBinario<T> nodoActual, int cont, boolean izquierdo) {
        String ret = "";
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            ret += arbolGrafico(nodoActual.getHijoDerecho(), cont + 1, false);
            for (int i = 0; i < cont; i++) {
                ret += "\t";
            }
            String hijo = nodoActual == raiz ? "[R]" : izquierdo ? "[I]" : "[D]";
            String impre = nodoActual.getDato().toString();
            ret += impre + hijo + "\n";
            ret += arbolGrafico(nodoActual.getHijoIzquierdo(), cont + 1, true);
        }
        return ret;
    }

    public int cantidadDeNodosCompletosRec() {//Metodos que retorne cuantos nodos tienen ambos hijos
        return this.cantidadDeNodosCompletosRec(raiz);
    }

    private int cantidadDeNodosCompletosRec(NodoBinario<T> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int ret = 0;
        if (!nodoActual.esHijoDerechoVacio() && !nodoActual.esHijoIzquierdoVacio()) {
            ret++;
        }
        int hijosXDer = this.cantidadDeNodosCompletosRec(nodoActual.getHijoDerecho());
        int hijosXIzq = this.cantidadDeNodosCompletosRec(nodoActual.getHijoIzquierdo());
        return hijosXDer + hijosXIzq + ret;
    }

    public int cantidadDeNodosCompletosIterativo() {
        if (NodoBinario.esNodoVacio(raiz)) {
            return 0;
        }
        int ret = 0;
        Queue<NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            int nroDeNodosDelNivel = colaDeNodos.size();
            while (nroDeNodosDelNivel > 0) {
                NodoBinario<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
                boolean b = false;
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    b = true;
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                    if (b) {
                        ret++;
                    }
                }
                nroDeNodosDelNivel--;
            }
        }
        return ret;
    }

    protected int NivelV2(NodoBinario<T> nodoActual) {
        return this.alturaRecursivo(raiz) - this.alturaRecursivo(nodoActual);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ArbolBinarioBusqueda) {
            ArbolBinarioBusqueda<T> otroArbol = (ArbolBinarioBusqueda<T>) obj;
            if (this.size() == otroArbol.size()) {
                if (this.altura() == otroArbol.altura()) {
                    return this.nodosIguales(this.raiz, otroArbol.raiz);
                }
            }
        }
        return false;
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final ArbolBinarioBusqueda<?> other = (ArbolBinarioBusqueda<?>) obj;
//        if (!Objects.equals(this.raiz, other.raiz)) {
//            return false;
//        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.raiz);
        return hash;
    }

    public int cantidadHijosIzquierdos() {
        return this.cantidadHijosIzquierdos(raiz);
    }

    private int cantidadHijosIzquierdos(NodoBinario<T> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int c = !nodoActual.esHijoIzquierdoVacio() ? 1 : 0;
        int cantXIzq = this.cantidadHijosIzquierdos(nodoActual.getHijoIzquierdo());
        int cantXDer = this.cantidadHijosIzquierdos(nodoActual.getHijoDerecho());
        return cantXIzq + cantXDer + c;
    }

    public int cantidadHijosIzquierdosIterativo() {
        if (NodoBinario.esNodoVacio(raiz)) {
            return 0;
        }
        int ret = 0;
        Queue<NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            int nroDeNodosDelNivel = colaDeNodos.size();
            while (nroDeNodosDelNivel > 0) {
                NodoBinario<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
                boolean b = false;
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    ret++;
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                nroDeNodosDelNivel--;
            }
        }
        return ret;
    }

    public void invertirArbol() {
        if (NodoBinario.esNodoVacio(raiz)) {
            return;
        }
        this.invertirArbol(raiz);
    }

    private void invertirArbol(NodoBinario<T> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        NodoBinario<T> hijoIzq = nodoActual.getHijoIzquierdo();
        NodoBinario<T> hijoDer = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(hijoIzq);
        nodoActual.setHijoIzquierdo(hijoDer);
        this.invertirArbol(nodoActual.getHijoIzquierdo());
        this.invertirArbol(nodoActual.getHijoDerecho());
    }

    public boolean arbolesSimilares(ArbolBinarioBusqueda otroArbol) {
        if (this.size() == otroArbol.size()) {
            if (this.altura() == otroArbol.altura()) {
                return this.nodosIguales(this.raiz, otroArbol.raiz);
            }
        }
        return false;
    }

    private boolean nodosIguales(NodoBinario<T> nodo, NodoBinario<T> otroNodo) {
        if (NodoBinario.esNodoVacio(nodo) && NodoBinario.esNodoVacio(otroNodo)) {
            return true;
        }
        if (NodoBinario.esNodoVacio(nodo) ^ NodoBinario.esNodoVacio(otroNodo)) {
            return false;
        }
        boolean b1 = this.nodosIguales(nodo.getHijoIzquierdo(), otroNodo.getHijoIzquierdo());
        boolean b2 = this.nodosIguales(nodo.getHijoDerecho(), otroNodo.getHijoDerecho());
        return b1 && b2;
    }
}
