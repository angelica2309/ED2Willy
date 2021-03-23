/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Willy
 * @param <T>
 */
public class ArbolMViasBusqueda<T extends Comparable<T>> implements IArbolBusqueda<T> {

    protected NodoMVias<T> raiz;
    protected int orden;

    public ArbolMViasBusqueda() {
        this.orden = 3;
    }

    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido {
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido("El orden de su árbol debe ser mayor a tres");
        }
        this.orden = orden;
    }

    public NodoMVias<T> getRaiz() {
        return this.raiz;
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(orden, datoAInsertar);
            return true;
        }
        NodoMVias<T> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeDatoEnNodo(nodoActual, datoAInsertar)) {
                return false;
            }
            if (nodoActual.esHoja()) {
                if (nodoActual.estanDatosLLenos()) {
                    int posicionNuevoHijo = this.posicionPorDondeBajar(nodoActual, datoAInsertar);
                    NodoMVias<T> nuevoNodo = new NodoMVias<>(orden, datoAInsertar);
                    nodoActual.setHijo(posicionNuevoHijo, nuevoNodo);
                } else {
                    this.insertarDatoEnNodo(nodoActual, datoAInsertar);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posPorDondeBajar = this.posicionPorDondeBajar(nodoActual, datoAInsertar);
                if (nodoActual.esHijoVacio(posPorDondeBajar)) {
                    NodoMVias<T> nuevoHijo = new NodoMVias<>(orden, datoAInsertar);
                    nodoActual.setHijo(posPorDondeBajar, nuevoHijo);
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    nodoActual = nodoActual.getHijo(posPorDondeBajar);
                }
            }
        }
        return true;
    }

    protected void insertarDatoEnNodo(NodoMVias<T> nodoActual, T datoAInsertar) {
        if (datoAInsertar == NodoMVias.datoVacio()) {
            return;
        }
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            int cant = nodoActual.cantidadDeDatosNoVacios();
            for (int i = 0; i < cant; i++) {
                T datoActual = nodoActual.getDato(i);
                if (datoAInsertar.compareTo(datoActual) < 0) {
                    for (int k = cant; k > i; k--) {
                        T dato = nodoActual.getDato(k - 1);
                        nodoActual.setDato(k, dato);
                    }
                    nodoActual.setDato(i, datoAInsertar);
                    return;
                }
            }
            nodoActual.setDato(cant, datoAInsertar);
        }
    }

    protected void insertarDatoEnNodo1(NodoMVias<T> nodoActual, T datoAInsertar) {
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            int cant = nodoActual.cantidadDeDatosNoVacios();
            int i;
            for (i = cant - 1; i >= 0 && datoAInsertar.compareTo(nodoActual.getDato(i)) < 0; i--) {
                nodoActual.setDato(i + 1, nodoActual.getDato(i));
            }
            nodoActual.setDato(i, datoAInsertar);
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

    private NodoMVias<T> eliminar(NodoMVias<T> nodoActual, T datoAEliminar)
            throws Exception {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new Exception();
        }
        if (!contiene(datoAEliminar)) {
            throw new Exception();
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            T datoActual = nodoActual.getDato(i);
            if (datoAEliminar.compareTo(datoActual) == 0) {
                if (nodoActual.esHoja()) {
                    eliminarDatoDeNodo(nodoActual, i);
                    if (nodoActual.estanDatosVacios()) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                //el dato a eliminar está es un nodo no hoja
                T datoDeReemplazo;
                if (hayHijosMasAdelante(nodoActual, i)) {
                    datoDeReemplazo = sucesorInOrden(nodoActual, datoAEliminar);
                } else {
                    datoDeReemplazo = predecesorInOrden(nodoActual, datoAEliminar);
                }
                nodoActual = eliminar(nodoActual, datoDeReemplazo);
                nodoActual.setDato(i, datoDeReemplazo);
                return nodoActual;
            }
            if (datoAEliminar.compareTo(datoActual) < 0) {
                NodoMVias<T> supuestoNuevoHijo = eliminar(nodoActual.getHijo(i), datoAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }//fin del for
        NodoMVias<T> supuestoNuevoHijo = eliminar(nodoActual.getHijo(orden - 1), datoAEliminar);
        nodoActual.setHijo(orden - 1, supuestoNuevoHijo);
        return nodoActual;
    }

    private boolean hayHijosMasAdelante(NodoMVias<T> nodoActual, int i) {
        for (int j = i + 1; j < orden; j++) {
            if (!NodoMVias.esNodoVacio(nodoActual.getHijo(j))) {
                return true;
            }
        }
        return false;
    }

    protected void eliminarDatoDeNodo(NodoMVias<T> nodoActual, int i) {
        int cant = nodoActual.cantidadDeDatosNoVacios();
        for (int k = i; k < cant - 1; k++) {
            T datoNuevo = nodoActual.getDato(k + 1);
            nodoActual.setDato(k, datoNuevo);
        }
        nodoActual.setDato(cant - 1, (T) NodoMVias.datoVacio());
    }

    @Override
    public T buscar(T datoABuscar) {
        NodoMVias<T> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            NodoMVias<T> nodoAnterior = nodoActual;
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios()
                    && nodoAnterior == nodoActual; i++) {
                T datoActual = nodoActual.getDato(i);
                if (datoABuscar.compareTo(datoActual) == 0) {
                    return datoActual;
                }
                if (datoABuscar.compareTo(datoActual) < 0) {
                    if (nodoActual.esHijoVacio(i)) {
                        return (T) NodoMVias.datoVacio();
                    }
                    //el hijo no es vacio
                    nodoActual = nodoActual.getHijo(i);
                }
            }//fin for
            if (nodoAnterior == nodoActual) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
            }
        }
        return (T) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(T dato) {
        return this.buscar(dato) != NodoMVias.nodoVacio();
    }

    protected boolean existeDatoEnNodo(NodoMVias<T> nodoActual, T dato) {
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            NodoMVias<T> nodo = nodoActual;
            while (!NodoMVias.esNodoVacio(nodo)) {
                NodoMVias<T> nodoAnterior = nodo;
                for (int i = 0; i < nodo.cantidadDeDatosNoVacios() && nodoAnterior == nodo; i++) {
                    T datoActual = nodo.getDato(i);
                    if (dato.compareTo(datoActual) == 0) {
                        return true;
                    }
                    if (dato.compareTo(datoActual) < 0) {
                        if (nodo.esHijoVacio(i)) {
                            return false;
                        }
                        nodo = nodo.getHijo(i);
                    }
                }
                if (nodo == nodoAnterior) {
                    nodo = nodo.getHijo(orden - 1);
                }
            }
        }
        return false;
    }

    @Override
    public List<T> recorridoEnInOrden() {
        List<T> recorrido = new LinkedList<>();
        this.recorrridoEnInOrden(raiz, recorrido);
        return recorrido;
    }

    private void recorrridoEnInOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        int cant = nodoActual.cantidadDeDatosNoVacios();
        for (int i = 0; i < cant; i++) {
            T datoActual = nodoActual.getDato(i);
            this.recorrridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(datoActual);
        }
        this.recorrridoEnInOrden(nodoActual.getHijo(cant), recorrido);

    }

    @Override
    public List<T> recorridoEnPreOrden() {
        List<T> recorrido = new LinkedList<>();
        this.recorridoEnPreOrden(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        int cant = nodoActual.cantidadDeDatosNoVacios();
        for (int i = 0; i < cant; i++) {
            recorrido.add(nodoActual.getDato(i));
            this.recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        this.recorridoEnPreOrden(nodoActual.getHijo(cant), recorrido);
    }

    @Override
    public List<T> recorridoEnPostOrden() {
        List<T> recorrido = new LinkedList<>();
        this.recorridoEnPostOrden(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        int cant = nodoActual.cantidadDeDatosNoVacios();
        //this.recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < cant; i++) {
            T datoActual = nodoActual.getDato(i);
            if (i == 0) {
                this.recorridoEnPostOrden(nodoActual.getHijo(i), recorrido);
            }
            this.recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(datoActual);
        }
    }

    @Override
    public List<T> recorridoPorNiveles() {
        List<T> recorrido = new ArrayList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoMVias<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);    //offer ingresa un dato
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<T> nodoActual = colaDeNodos.poll(); // pool elimina y retorna
            int cant = nodoActual.cantidadDeDatosNoVacios();
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                recorrido.add(nodoActual.getDato(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
                if (!nodoActual.esHijoVacio(cant)) {//orden - 1
                    colaDeNodos.offer(nodoActual.getHijo(cant));
                }
            }
        }
        return recorrido;
    }

    @Override
    public int size() {
        return this.sizeNodos(raiz);
    }

    protected int sizeHijos(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int v = nodoActual.cantidadDeDatosNoVacios();
        int suma = 0;
        for (int i = 0; i < v; i++) {
            suma = !nodoActual.esDatoVacio(i) ? suma + 1 : suma;
            suma += sizeHijos(nodoActual.getHijo(i));
        }
        suma += sizeHijos(nodoActual.getHijo(v));
        return suma;
    }

    protected int sizeNodos(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int suma = 0;
        int v = nodoActual.cantidadDeDatosNoVacios() + 1;
        for (int i = 0; i < v; i++) {
            suma += sizeNodos(nodoActual.getHijo(i));
        }
        return suma + 1;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    protected int altura(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int[] alturas = new int[orden];
        //int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            //int alturaDeHijo = altura(nodoActual.getHijo(i));
            //alturaMayor = alturaDeHijo > alturaMayor ? alturaDeHijo : alturaMayor;
            alturas[i] = altura(nodoActual.getHijo(i));
        }
        return mayor(alturas) + 1;
    }

    private int mayor(int[] datos) {
        if (datos.length > 0) {
            int may = datos[0];
            for (int i = 1; i < datos.length; i++) {
                may = datos[i] > may ? datos[i] : may;
            }
            return may;
        }
        return -1;
    }

    private int indiceDelMayor(int[] datos) {
        if (datos.length > 0) {
            int may = 0;
            for (int i = 1; i < datos.length; i++) {
                may = datos[i] > datos[may] ? i : may;
            }
            return may;
        }
        return -1;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return altura() - 1;
    }

    protected int nivel(NodoMVias<T> nodoActual) {
        return this.nivel(raiz, nodoActual, 0);
    }

    private int nivel(NodoMVias<T> nodoRaiz, NodoMVias<T> nodoActual, int ret) {
        if (NodoMVias.esNodoVacio(nodoRaiz) || NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }
        if (nodoRaiz == nodoActual) {
            return ret;
        } else {
            int niveles[] = new int[orden];
            for (int i = 0; i < orden; i++) {
                niveles[i] = nivel(nodoRaiz.getHijo(i), nodoActual, ret);
            }
            int j = indiceDelMayor(niveles);
            int niv = nivel(nodoRaiz.getHijo(j), nodoActual, ret + 1);
            return niv;
        }
    }

    List<NodoMVias<T>> nodosDelNivel(int n) {
        List<NodoMVias<T>> nodos = new LinkedList<>();
        this.añadirNodosDelNivel(nodos, n, raiz);
        return nodos;
    }

    void añadirNodosDelNivel(List<NodoMVias<T>> nodos, int n, NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < orden; i++) {
            this.añadirNodosDelNivel(nodos, n, nodoActual.getHijo(i));
        }
        if (nivel(nodoActual) == n) {
            nodos.add(nodoActual);
        }
    }

    protected int posicionPorDondeBajar(NodoMVias<T> nodoActual, T dato) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            T datoActual = nodoActual.getDato(i);
            if (dato.compareTo(datoActual) < 0) {
                return i;
            }
        }
        return nodoActual.cantidadDeDatosNoVacios();
    }

    public void mostrar() {
        this.mostrar(raiz, 0);
    }

    protected void mostrar(NodoMVias<T> nodoActual, int cont) {
        if (nodoActual == raiz) {
            System.out.println("Arbol de Forma Grafica");
        }
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            int cant = nodoActual.cantidadDeDatosNoVacios();
            for (int i = cant; i >= 0; i--) {
                this.mostrar(nodoActual.getHijo(i), cont + 1);
                if (i > 0) {
                    for (int j = 0; j < cont; j++) {
                        System.out.print("                   ");
                    }
                    System.out.println("[" + String.valueOf(i) + "]|" + nodoActual.getDato(i - 1));
                }
            }
        }
    }

    @Override
    public String arbolGrafico() {
        return arbolGrafico(raiz, 0);
    }

    protected String arbolGrafico(NodoMVias<T> nodoActual, int cont) {
        String ret = "";
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            int cant = nodoActual.cantidadDeDatosNoVacios();
            for (int i = cant; i >= 0; i--) {
                ret += arbolGrafico(nodoActual.getHijo(i), cont + 1);
                if (i > 0) {
                    for (int j = 0; j < cont; j++) {
                        ret += "\t\t";
                    }
                    ret += "[" + String.valueOf(i) + "]|" + nodoActual.getDato(i - 1) + "\n";
                }
            }
        }
        return ret;
    }

    protected T sucesorInOrden(NodoMVias<T> nodoActual, T dato) {
        if (dato == null) {
            return null;
        }
        if (this.contiene(dato)) {
            List<T> datos = new LinkedList<>();
            this.recorrridoEnInOrden(nodoActual, datos);
            int ind = indiceDe(datos, dato);
            return datos.get(ind + 1);
        }
        return (T) NodoMVias.datoVacio();
    }

    protected T predecesorInOrden(NodoMVias<T> nodoActual, T dato) {
        if (this.contiene(dato)) {
            List<T> datos = new LinkedList<>();
            this.recorrridoEnInOrden(nodoActual, datos);
            int ind = indiceDe(datos, dato);
            return datos.get(ind - 1);
        }
        return (T) NodoMVias.datoVacio();
    }

    private int indiceDe(List<T> datos, T dato) {
        for (int i = 0; i < datos.size(); i++) {
            T datoActual = datos.get(i);
            if (dato.compareTo(datoActual) == 0) {
                return i;
            }
        }
        return -1;
    }

    public int cantHojasApartirDeNivel(int nivel) {
        if (nivel < altura()) {
            int cont = 0;
            for (int i = nivel; i < altura(); i++) {
                cont += this.cantHojasDelNivel(i);
            }
            return cont;
        }
        return 0;
    }

    public int cantHojasDelNivel(int nivel) {
        if (nivel < altura()) {
            int cont = 0;
            List<NodoMVias<T>> nodosDelNivel = this.nodosDelNivel(nivel);
            for (int i = 0; i < nodosDelNivel.size(); i++) {
                NodoMVias<T> nodoActual = nodosDelNivel.get(i);
                cont = nodoActual.esHoja() ? cont + 1 : cont;
            }
            return cont;
        }
        return 0;
    }

    public int hojasApartirDelNivel(int nivel) {
        return hojasApartirDelNivel(raiz, nivel, 0);
    }

    public int hojasApartirDelNivel(NodoMVias<T> nodoActual, int nivel, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelActual <= nivel) {
            if (nodoActual.esHoja()) {
                return 1;
            }
        }
        int cantHojas = 0;
        for (int i = 0; i < orden; i++) {
            cantHojas += hojasApartirDelNivel(nodoActual.getHijo(i), nivel, nivelActual + 1);
        }
        return cantHojas;
    }
}
