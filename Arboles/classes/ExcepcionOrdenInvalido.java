/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Arboles.classes;

/**
 *
 * @author Willy
 */
public class ExcepcionOrdenInvalido extends Exception {

    /**
     * Creates a new instance of <code>NewException</code> without detail
     * message.
     */
    public ExcepcionOrdenInvalido() {
    }

    /**
     * Constructs an instance of <code>NewException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionOrdenInvalido(String msg) {
        super(msg);
    }
}
