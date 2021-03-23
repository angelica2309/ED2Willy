/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.UI;

import Arboles.classes.ArbolExpresion;
import Arboles.classes.Variable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Willy
 */
public class TestExpresion {
    public static void main(String[] args) {
        ArbolExpresion arb = new ArbolExpresion(2);
        arb.probar();
        List<Variable> vars = new LinkedList<>();
        vars.add(new Variable("a",2));
        vars.add(new Variable("b",5));
        vars.add(new Variable("c",7));
        System.out.println(arb.recorridoInOrden());
        System.out.println(arb.evaluarExpresionVariable(vars));
        System.out.println(arb.listaOperandos());
        System.out.println(arb.arbolGrafico());
    }
}
