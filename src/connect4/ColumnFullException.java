/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

/**
 *
 * @author Andrea
 */
public class ColumnFullException extends Exception {

    /**
     * Creates a new instance of <code>ColumnFullException</code> without detail
     * message.
     */
    public ColumnFullException() {
    }

    /**
     * Constructs an instance of <code>ColumnFullException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ColumnFullException(String msg) {
        super(msg);
    }
}
