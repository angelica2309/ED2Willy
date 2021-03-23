 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

import java.util.Stack;

/**
 *
 * @author Willy
 * @param <T>
 */
public class ArbolB<T extends Comparable<T>> extends ArbolMViasBusqueda<T> {

    private int nroMaxDatos;
    private int nroMinDatos;
    private int nroMinHijos;

    public ArbolB() throws ExcepcionOrdenInvalido {
        this(3);
    }

    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden + 1);
        this.nroMaxDatos = orden - 1;
        this.nroMinDatos = this.nroMaxDatos / 2;
        this.nroMinHijos = this.nroMinDatos + 1;
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(orden, datoAInsertar);
            return true;
        }
        Stack<NodoMVias<T>> pilaDeAncestros = new Stack<>();
        NodoMVias<T> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeDatoEnNodo(nodoActual, datoAInsertar)) {
                return false;
            }
            if (nodoActual.esHoja()) {
                this.insertarDatoEnNodo(nodoActual, datoAInsertar);
                if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaxDatos) {
                    dividirNodo(nodoActual, pilaDeAncestros);
                }
                //esto se hace para que el ciclo termine
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posPorDondeBajar = this.posicionPorDondeBajar(nodoActual, datoAInsertar);
                pilaDeAncestros.push(nodoActual);
                nodoActual = nodoActual.getHijo(posPorDondeBajar);
            }
        }
        return true;
    }

    private NodoMVias<T> subNodo(NodoMVias<T> nodoActual, int desde, int hasta) {
        NodoMVias<T> nuevoNodo = new NodoMVias<>(orden);
        int c = 0;
        for (int i = desde; i < hasta; i++) {
            nuevoNodo.setDato(c, nodoActual.getDato(i));
            nuevoNodo.setHijo(c, nodoActual.getHijo(i));
            c++;
        }
        nuevoNodo.setHijo(c, nodoActual.getHijo(hasta));
        return nuevoNodo;
    }

    private void dividirNodo(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDeAncestros) {
        int posQueSube = this.nroMinDatos;
        T datoQueSube = nodoActual.getDato(posQueSube);
        NodoMVias<T> nodoPadre = pilaDeAncestros.isEmpty() ? new NodoMVias<>(orden) : pilaDeAncestros.pop();
        NodoMVias<T> nodoHijoIzq = this.subNodo(nodoActual, 0, posQueSube);
        NodoMVias<T> nodoHijoDer = this.subNodo(nodoActual, posQueSube + 1, orden - 1);
        int pos = super.posicionPorDondeBajar(nodoPadre, datoQueSube);
        super.insertarDatoEnNodo(nodoPadre, datoQueSube);
        this.recorrerHijosDivision(nodoPadre, pos);
        nodoPadre.setHijo(pos, nodoHijoIzq);
        nodoPadre.setHijo(pos + 1, nodoHijoDer);
        if (pilaDeAncestros.isEmpty()) {
            raiz = nodoPadre;
        }
        if (nodoPadre.cantidadDeDatosNoVacios() > this.nroMaxDatos) {
            this.dividirNodo(nodoPadre, pilaDeAncestros);
        }
    }

    private void recorrerHijosDivision(NodoMVias<T> nodoActual, int pos) {
        for (int i = nodoActual.cantidadDeDatosNoVacios(); i > pos; i--) {
            nodoActual.setHijo(i, nodoActual.getHijo(i - 1));
        }
    }

    private boolean datoEnNodo(NodoMVias<T> nodoActual, T dato) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            if (dato.compareTo(nodoActual.getDato(i)) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(T datoAEliminar) {
        Stack<NodoMVias<T>> pilaDeAncestros = new Stack<>();
        NodoMVias<T> nodoActual = raiz;
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return false;
        }
        if (!super.contiene(datoAEliminar)) {
            return false;
        }
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDelDato = super.posicionPorDondeBajar(nodoActual, datoAEliminar) - 1;
            if (nodoActual.esHoja()) {
                //  T datoSucesor = super.sucesorInOrden(raiz, datoAEliminar);
                super.eliminarDatoDeNodo(nodoActual, posicionDelDato);
                if (nodoActual.cantidadDeDatosNoVacios() < this.nroMinDatos) {
                    if (pilaDeAncestros.isEmpty()) {
                        if (nodoActual.cantidadDeDatosNoVacios() == 0) { //nodoActual es la raiz
                            super.vaciar();
                        }
                    } else {
                        this.prestarseOFusionarse(nodoActual, pilaDeAncestros);
                    }
                }
                return true;
            } else {
                if (this.datoEnNodo(nodoActual, datoAEliminar)) {
                    T datoPredecesor = super.predecesorInOrden(nodoActual, datoAEliminar);
                    NodoMVias<T> nodoDelPredecesor = buscarNodoDelPredecesor(nodoActual,
                            datoPredecesor, pilaDeAncestros);
                    int posDelPredecesor = nodoDelPredecesor.cantidadDeDatosNoVacios() - 1;
                    nodoActual.setDato(posicionDelDato, datoPredecesor);
                    super.eliminarDatoDeNodo(nodoDelPredecesor, posDelPredecesor);
                    if (nodoDelPredecesor.cantidadDeDatosNoVacios() < this.nroMinDatos) {
                        this.prestarseOFusionarse(nodoDelPredecesor, pilaDeAncestros);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int pos = super.posicionPorDondeBajar(nodoActual, datoAEliminar);
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(pos);
                }
            }
        }
        return true;
    }

    private int posicionHijoNulo(NodoMVias<T> nodoActual, int cant) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }
        for (int i = 0; i <= cant; i++) {
            NodoMVias<T> nodoHijo = nodoActual.getHijo(i);
            if (nodoHijo.cantidadDeDatosNoVacios() < this.nroMinDatos) {
                return i;
            }
        }
        return -1;
    }

    private int indiceDeFusion(NodoMVias<T> nodoPadre, int posNodoHijo) {
        int cant = nodoPadre.cantidadDeDatosNoVacios();
        NodoMVias<T> nodoHermanoDer = posNodoHijo != cant ? nodoPadre.getHijo(posNodoHijo + 1)
                : NodoMVias.nodoVacio();
        return !NodoMVias.esNodoVacio(nodoHermanoDer) ? 1 : -1;
    }

    private int indiceDePrestamo(NodoMVias<T> nodoPadre, int posNodoHijo) {
        int cant = nodoPadre.cantidadDeDatosNoVacios();
        NodoMVias<T> nodoHermanoDer = posNodoHijo != cant ? nodoPadre.getHijo(posNodoHijo + 1)
                : NodoMVias.nodoVacio();
        NodoMVias<T> nodoHermanoIzq = posNodoHijo > 0 ? nodoPadre.getHijo(posNodoHijo - 1)
                : NodoMVias.nodoVacio();
        if (!NodoMVias.esNodoVacio(nodoHermanoDer)
                && nodoHermanoDer.cantidadDeDatosNoVacios() > this.nroMinDatos) {
            return 1;
        } else if (!NodoMVias.esNodoVacio(nodoHermanoIzq)
                && nodoHermanoIzq.cantidadDeDatosNoVacios() > this.nroMinDatos) {
            return -1;
        }
        return 0;
    }

    private NodoMVias<T> buscarNodoDelPredecesor(NodoMVias<T> nodoActual, T dato,
            Stack<NodoMVias<T>> pilaDeAncestros) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return pilaDeAncestros.pop();
        }
        int pos = super.posicionPorDondeBajar(nodoActual, dato);
        pilaDeAncestros.push(nodoActual);
        return this.buscarNodoDelPredecesor(nodoActual.getHijo(pos), dato, pilaDeAncestros);
    }

    private void prestarseOFusionarse(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDeAncestros) {
        NodoMVias<T> nodoPadre = pilaDeAncestros.peek();
        int posNodoEliminado = this.posicionHijoNulo(nodoPadre, nodoPadre.cantidadDeDatosNoVacios());
        int indicePrestamo = this.indiceDePrestamo(nodoPadre, posNodoEliminado);
        if (indicePrestamo != 0 && nodoActual.esHoja()) {
            this.prestamoDato(nodoActual, indicePrestamo, pilaDeAncestros);
        } else {
            int indFusion = this.indiceDeFusion(nodoPadre, posNodoEliminado);
            if (indFusion == 1) {
                this.fusionPorDerecha(nodoActual, pilaDeAncestros);
            } else {
                this.fusionPorIzquierda(nodoActual, pilaDeAncestros);
            }
        }
    }

    private void prestamoDato(NodoMVias<T> nodoActual, int indicePrestamo,
            Stack<NodoMVias<T>> pilaDeAncestros) {
        if (indicePrestamo == 1) {
            System.out.println("Prestamo por der");
        } else {
            System.out.println("Prestamo por izq");
        }
        NodoMVias<T> nodoPadre = pilaDeAncestros.pop();
        int posNodoEliminado = this.posicionHijoNulo(nodoPadre, nodoPadre.cantidadDeDatosNoVacios());
        //int cant = nodoPadre.cantidadDeDatosNoVacios();
        NodoMVias<T> nodoHermano = nodoPadre.getHijo(posNodoEliminado + indicePrestamo);
        int posDatoPadre = indicePrestamo > 0 ? posNodoEliminado
                : posNodoEliminado - 1;
        T datoPadre = nodoPadre.getDato(posDatoPadre);
        int posDatoHermano = indicePrestamo > 0 ? 0 : nodoHermano.cantidadDeDatosNoVacios() - 1;
        T datoHermano = nodoHermano.getDato(posDatoHermano);
        super.eliminarDatoDeNodo(nodoPadre, posDatoPadre);
        super.eliminarDatoDeNodo(nodoHermano, posDatoHermano);
        super.insertarDatoEnNodo(nodoActual, datoPadre);
        super.insertarDatoEnNodo(nodoPadre, datoHermano);
    }

    private void eliminarHijo(NodoMVias<T> nodoPadre, int posicion, int cantHijos) {
        for (int i = posicion; i < cantHijos; i++) {
            nodoPadre.setHijo(i, nodoPadre.getHijo(i + 1));
        }
    }

    private void insertarHijosFusion(NodoMVias<T> nodoAInsertar, NodoMVias<T> nodoDeLosHijos,
            int posAInsertar, int posPrimerHijo) {
        int cant2 = nodoDeLosHijos.cantidadDeHijosNoNulos();
        int cont = posAInsertar;
        for (int i = posPrimerHijo; i < cant2; i++) {
            nodoAInsertar.setHijo(cont, nodoDeLosHijos.getHijo(i));
            cont++;
        }
    }

    private void insertarDatosFusion(NodoMVias<T> nodoActual, NodoMVias<T> nodoHermano) {
        for (int i = 0; i < nodoHermano.cantidadDeDatosNoVacios(); i++) {
            super.insertarDatoEnNodo(nodoActual, nodoHermano.getDato(i));
        }
    }

    private void fusionPorDerecha(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDeAncestros) {
        NodoMVias<T> nodoPadre = pilaDeAncestros.pop();
        int cantDatosPadre = nodoPadre.cantidadDeDatosNoVacios();
        int cantHijosPadre = cantDatosPadre + 1;
        int posNodoNulo = this.posicionHijoNulo(nodoPadre, cantDatosPadre);
        T datoPadre = nodoPadre.getDato(posNodoNulo);
        NodoMVias<T> nodoHermano = nodoPadre.getHijo(posNodoNulo + 1);
        T datoHermano = nodoHermano.getDato(0);
        NodoMVias<T> nodoPredecesorPadre = nodoHermano.getHijo(0);
        super.insertarDatoEnNodo(nodoActual, datoPadre);
        super.insertarDatoEnNodo(nodoActual, datoHermano);
        super.eliminarDatoDeNodo(nodoPadre, posNodoNulo);
        super.eliminarDatoDeNodo(nodoHermano, 0);
        if (nodoActual.esHoja()) {
            this.eliminarHijo(nodoPadre, posNodoNulo + 1, cantHijosPadre);
        } else {
            nodoActual.setHijo(1, nodoPredecesorPadre);
            this.insertarDatosFusion(nodoActual, nodoHermano);
            this.insertarHijosFusion(nodoActual, nodoHermano, 2, 1);
            this.eliminarHijo(nodoPadre, posNodoNulo + 1, cantHijosPadre);
        }
        if (nodoPadre.cantidadDeDatosNoVacios() == 0) {
            if (nodoPadre == raiz) {
                raiz = nodoActual;
                nodoPadre = nodoActual;
            }
        }
        if (nodoPadre.cantidadDeDatosNoVacios() < this.nroMinDatos) {
            this.prestarseOFusionarse(nodoPadre, pilaDeAncestros);
        } else if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaxDatos) {
            pilaDeAncestros.push(nodoPadre);
            this.dividirNodo(nodoActual, pilaDeAncestros);
        }

    }

    private void fusionPorIzquierda(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDeAncestros) {
        NodoMVias<T> nodoPadre = pilaDeAncestros.pop();
        int cantDatosPadre = nodoPadre.cantidadDeDatosNoVacios();
        int cantHijosPadre = cantDatosPadre + 1;
        int posNodoNulo = this.posicionHijoNulo(nodoPadre, cantDatosPadre);
        T datoPadre = nodoPadre.getDato(cantDatosPadre - 1);
        NodoMVias<T> nodoHermano = nodoPadre.getHijo(posNodoNulo - 1);
        super.insertarDatoEnNodo(nodoHermano, datoPadre);
        super.eliminarDatoDeNodo(nodoPadre, cantDatosPadre - 1);
        if (nodoActual.esHoja()) {
            this.eliminarHijo(nodoPadre, posNodoNulo, cantHijosPadre);
        } else {
            this.insertarHijosFusion(nodoHermano, nodoActual, nodoHermano.cantidadDeDatosNoVacios(),
                    nodoActual.cantidadDeDatosNoVacios());
        }
        if (nodoPadre.cantidadDeDatosNoVacios() == 0) {
            if (nodoPadre == raiz) {
                raiz = nodoHermano;
                nodoPadre = nodoHermano;
            }
        }
        if (nodoPadre.cantidadDeDatosNoVacios() < this.nroMinDatos) {
            this.prestarseOFusionarse(nodoPadre, pilaDeAncestros);
        }
    }

}
