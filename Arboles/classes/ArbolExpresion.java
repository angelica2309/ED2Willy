package Arboles.classes;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

;

/**
 *
 * @author Willy
 */
public class ArbolExpresion {

    private NodoExpresion raiz;
    private int tipoExpresion;
    public static final int EXP_NUMERICA = 0;
    public static final int EXP_VARIABLE = 1;
    public static final int EXP_MIXTA = 2;

    private class NodoExpresion extends NodoBinario<String> {

        public NodoExpresion() {
            super();
        }

        public NodoExpresion(String dato) {
            super(dato);
        }

        public void setHijoDerecho(NodoExpresion hijoDerecho) {
            super.setHijoDerecho(hijoDerecho);
        }

        public void setHijoIzquierdo(NodoExpresion hijoIzquierdo) {
            super.setHijoIzquierdo(hijoIzquierdo);
        }

        @Override
        public NodoExpresion getHijoDerecho() {
            return (NodoExpresion) super.getHijoDerecho();
        }

        @Override
        public NodoExpresion getHijoIzquierdo() {
            return (NodoExpresion) super.getHijoIzquierdo();
        }

    }

    private class OperadorPos {

        public char operador;
        public int posicion;

        public OperadorPos(char o, int p) {
            this.operador = o;
            this.posicion = p;
        }

        @Override
        public String toString() {
            return "{" + operador + "," + posicion + "}";
        }
    }

    public ArbolExpresion() {
        this(ArbolExpresion.EXP_NUMERICA);
    }

    public ArbolExpresion(int tipoExp) {
        raiz = new NodoExpresion();
        if (tipoExp >= 0 && tipoExp < 3) {
            tipoExpresion = tipoExp;
        } else {
            tipoExpresion = 0;
        }
    }

    public ArbolExpresion(int tipoExp, String expresion) {
        this(ArbolExpresion.EXP_NUMERICA);
        analizar(raiz, expresion);
    }

    private boolean esOperador(String c) {
        int esta = "+-*/^".indexOf(c);
        return esta >= 0;
    }

    public String arbolGrafico() {
        return arbolGrafico(raiz, 0, true);
    }

    protected String arbolGrafico(NodoExpresion nodoActual, int cont, boolean izquierdo) {
        String ret = "";
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            ret += arbolGrafico((NodoExpresion) nodoActual.getHijoDerecho(), cont + 1, false);
            for (int i = 0; i < cont; i++) {
                ret += "\t";
            }
            //String hijo = nodoActual == raiz ? "[R]" : izquierdo ? "[I]" : "[D]";
            String hijo = "";
            String impre = nodoActual.getDato();
            ret += impre + hijo + "\n";
            ret += arbolGrafico((NodoExpresion) nodoActual.getHijoIzquierdo(), cont + 1, true);
        }
        return ret;
    }

    private int prioridad(char operador) {
        String[] opers = {"+-", "*/", "^"};
        for (int i = 0; i < opers.length; i++) {
            String ex = opers[i];
            int pos = ex.indexOf(operador);
            if (pos >= 0) {
                return i + 1;
            }
        }
        return 0;
    }

    public void analizar(String expresion) {
        if (expresionCorrecta(expresion)) {
            raiz = new NodoExpresion();
            this.analizar(raiz, expresion);
        } else {
            throw new IllegalArgumentException("Expresion incorrecta");
        }
    }

    private void analizar(NodoExpresion nodo, String expresion) {
        if (expresion.length() == 0) {
            return;
        }
        expresion = this.minParentesis(expresion);
        List<OperadorPos> list = this.operadoresYPrioridades(expresion);
        if (!list.isEmpty()) {
            int posMenor = this.menorPrioridad(list);
            int pos = list.get(posMenor).posicion;
            String oper = "" + expresion.charAt(pos);
            String parteIzq = expresion.substring(0, pos);
            String parteDer = expresion.substring(pos + 1, expresion.length());
            nodo.setDato(oper);
            nodo.setHijoIzquierdo(new NodoExpresion());
            nodo.setHijoDerecho(new NodoExpresion());
            this.analizar(nodo.getHijoIzquierdo(), parteIzq);
            this.analizar(nodo.getHijoDerecho(), parteDer);
        } else {
            String operando = expresion;
            nodo.setDato(operando);
        }
    }

    private List<OperadorPos> operadoresYPrioridades(String expresion) {
        List<OperadorPos> res = new LinkedList<>();
        Stack<Character> pilaParentesis = new Stack<>();
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (c == '(') {
                pilaParentesis.push(c);
            } else if (c == ')') {
                pilaParentesis.pop();
            } else if (pilaParentesis.isEmpty() && this.esOperador("" + c)) {
                OperadorPos nuevo = new OperadorPos(c, i);
                res.add(nuevo);
            }
        }
        return res;
    }

    private int menorPrioridad(List<OperadorPos> opers) {
        if (opers.isEmpty()) {
            return -1;
        }
        int menor = 0;
        for (int i = 1; i < opers.size(); i++) {
            OperadorPos op = opers.get(i);
            int p1 = prioridad(op.operador);
            int p2 = prioridad(opers.get(menor).operador);
            if (p1 <= p2) {
                menor = i;
            }
        }
        return menor;
    }

    public String minParentesis(String exp) {
        if (exp.length() > 0) {
            if (exp.charAt(0) == '('
                    && exp.charAt(exp.length() - 1) == ')') {
                String par = exp.substring(1, exp.length() - 1);
                Stack<Character> p = new Stack<>();
                for (int i = 0; i < par.length(); i++) {
                    Character c = par.charAt(i);
                    if (c == '(') {
                        p.push(c);
                    } else if (c == ')') {
                        if (p.isEmpty()) {
                            return exp;
                        }
                        p.pop();
                    }
                }
                return p.isEmpty() ? par : exp;
            }
        }
        return exp;
    }

    public int cantidadDeHojas() {
        int ret = 0;
        Stack<NodoExpresion> pilaNodos = new Stack<>();
        pilaNodos.push(raiz);
        while (!pilaNodos.isEmpty()) {
            NodoExpresion nodoActual = pilaNodos.pop();
            if (!NodoBinario.esNodoVacio(nodoActual)) {
                if (nodoActual.esHoja()) {
                    ret++;
                } else {
                    if (!nodoActual.esHijoIzquierdoVacio()) {
                        pilaNodos.push(nodoActual.getHijoIzquierdo());
                    }
                    if (!nodoActual.esHijoDerechoVacio()) {
                        pilaNodos.push(nodoActual.getHijoDerecho());
                    }
                }
            }
        }
        return ret;
    }

    public String recorridoPreOrden() {
        return preOrden(raiz);
    }

    private String preOrden(NodoExpresion nodoActual) {
        String ret = "";
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            ret += nodoActual.getDato() + preOrden(nodoActual.getHijoIzquierdo())
                    + preOrden(nodoActual.getHijoDerecho());
        }
        return ret;
    }

    public String recorridoInOrden() {
        return inOrden(raiz);
    }

    private String inOrden(NodoExpresion nodoActual) {
        String ret = "";
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            ret += inOrden(nodoActual.getHijoIzquierdo()) + nodoActual.getDato()
                    + inOrden(nodoActual.getHijoDerecho());
        }
        return ret;
    }

    public String recorridoPostOrden() {
        return postOrden(raiz);
    }

    private String postOrden(NodoExpresion nodoActual) {
        String ret = "";
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            ret += postOrden(nodoActual.getHijoIzquierdo())
                    + postOrden(nodoActual.getHijoDerecho()) + nodoActual.getDato();
        }
        return ret;
    }

    public int evaluarExpresionNumerica() {
        if (tipoExpresion == EXP_NUMERICA) {
            return evaluar(raiz);
        } else {
            System.err.println("Evaluacion solo para expresiones numerica");
            return 0;
            //throw new IllegalArgumentException("Evaluacion solo para expresiones numericas");
        }
    }

    private int evaluar(NodoExpresion nodoActual) {
        int ret = 0;
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            String datoIzq = nodoActual.getHijoIzquierdo().getDato();
            String datoDer = nodoActual.getHijoDerecho().getDato();
            String datoPadre = nodoActual.getDato();
            int di, dd;
            if (!esOperador(datoIzq) && !esOperador(datoDer)) {
                di = Integer.parseInt(datoIzq);
                dd = Integer.parseInt(datoDer);
                ret = operacion(datoPadre, di, dd);
            } else if (!esOperador(datoIzq)) {
                di = Integer.parseInt(datoIzq);
                ret = operacion(datoPadre, di, evaluar(nodoActual.getHijoDerecho()));
            } else if (!esOperador(datoDer)) {
                dd = Integer.parseInt(datoDer);
                ret = operacion(datoPadre, evaluar(nodoActual.getHijoIzquierdo()), dd);
            } else {
                ret = operacion(datoPadre, evaluar(nodoActual.getHijoIzquierdo()),
                        evaluar(nodoActual.getHijoDerecho()));
            }
        }
        return ret;
    }

    public int evaluarExpresionVariable(List<Variable> valores) {
        if (tipoExpresion == EXP_VARIABLE) {
            return evaluarVar(raiz, valores);
        } else {
            System.err.println("Evaluacion solo para expresiones variables");
            return 0;
            //throw new IllegalArgumentException("Evaluacion solo para expresiones numericas");
        }
    }

    private int evaluarVar(NodoExpresion nodoActual, List<Variable> valores) {
        int ret = 0;
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            String datoIzq = nodoActual.getHijoIzquierdo().getDato();
            String datoDer = nodoActual.getHijoDerecho().getDato();
            String datoPadre = nodoActual.getDato();
            Variable varIzq = new Variable(datoIzq);
            Variable varDer = new Variable(datoDer);
            int di, dd;
            if (!esOperador(datoIzq) && !esOperador(datoDer)) {
                if (valores.contains(varIzq) && valores.contains(varDer)) {
                    di = valorVariable(datoIzq, valores);
                    dd = valorVariable(datoDer, valores);
                    ret = operacion(datoPadre, di, dd);
                } else {
                    return 0;
                }
            } else if (!esOperador(datoIzq)) {
                if (valores.contains(varIzq)) {
                    di = valorVariable(datoIzq, valores);
                    ret = operacion(datoPadre, di, evaluarVar(nodoActual.getHijoDerecho(), valores));
                } else {
                    return 0;
                }
            } else if (!esOperador(datoDer)) {
                if (valores.contains(varDer)) {
                    dd = valorVariable(datoDer, valores);
                    ret = operacion(datoPadre, evaluarVar(nodoActual.getHijoIzquierdo(), valores), dd);
                } else {
                    return 0;
                }
            } else {
                ret = operacion(datoPadre, evaluarVar(nodoActual.getHijoIzquierdo(), valores),
                        evaluarVar(nodoActual.getHijoDerecho(), valores));
            }
        }
        return ret;
    }

    private int valorVariable(String var, List<Variable> valores) {
        Variable x = new Variable(var);
        Variable v = valores.get(valores.indexOf(x));
        return v.getValor();
    }

    private int operacion(String o, int a, int b) {
        int res = 0;
        char op = o.charAt(0);
        switch (op) {
            case '+':
                res = a + b;
                break;
            case '-':
                res = a - b;
                break;
            case '*':
                res = a * b;
                break;
            case '/':
                res = a / b;
                break;
            case '^':
                res = (int) Math.pow(a, b);
                break;
        }
        return res;
    }

    private boolean expresionCorrecta(String expresion) {
        Stack<Character> pila = new Stack<>();
        for (int i = 0; i < expresion.length(); i++) {
            char c = Character.toUpperCase(expresion.charAt(i));
            if (c == '(') {
                pila.push(c);
            } else if (c == ')') {
                if (pila.isEmpty()) {
                    return false;
                }
                pila.pop();
            } else if (!"+-*/^".contains("" + c)) {
                String letras = "ABCDEFGHIJKLMNÑOQPRSTUVWXYZ";
                String nums = "0123456789";
                switch (tipoExpresion) {
                    case EXP_NUMERICA:
                        if (!nums.contains("" + c)) {
                            return false;
                        }
                        break;
                    case EXP_VARIABLE:
                        if (!letras.contains("" + c)) {
                            return false;
                        }
                        break;
                    default:
                        String alfaNum = letras + nums;
                        if (!alfaNum.contains("" + c)) {
                            return false;
                        }
                        break;
                }
            }
        }
        return pila.isEmpty();
    }

    public List<String> listaOperandos() {
        List<String> lista = new LinkedList<>();
        listaOperandos(raiz, lista);
        return lista;
    }

    private void listaOperandos(NodoExpresion nodoActual, List<String> operandos) {
        if (!NodoBinario.esNodoVacio(nodoActual)) {
            if (nodoActual.esHoja()) {
                String operando = nodoActual.getDato();
                if (!operandos.contains(operando)) {
                    operandos.add(operando);
                }
            } else {
                listaOperandos(nodoActual.getHijoIzquierdo(), operandos);
                listaOperandos(nodoActual.getHijoDerecho(), operandos);
            }
        }
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoExpresion nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaIzq = this.altura(nodoActual.getHijoIzquierdo());
        int alturaDer = this.altura(nodoActual.getHijoDerecho());
        return 1 + Integer.max(alturaIzq, alturaDer);
    }

    public void probar() {
        String exp = "(a+b)*c-d";
        String exp1 = "(4^2)-3*(2+3)";
        String exp2 = "((x^2)*3-5*5+sen(x)/(a-b))";
        String exp3 = "a+b+c+d";
        String exp4 = "(((1+2)+3)+4)";
        String exp5 = "3*sen(x)+4*(x^2)";
        this.analizar(exp2);
        System.out.println(this.arbolGrafico());
        System.out.println(this.cantidadDeHojas());
    }

}
