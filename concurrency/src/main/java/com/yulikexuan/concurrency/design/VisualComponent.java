//: com.yulikexuan.concurrency.design.VisualComponent.java

package com.yulikexuan.concurrency.design;


import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * VisualComponent
 *
 * Delegating thread safety to multiple underlying state variables
 *
 * We can also delegate thread safety to more than one underlying state variable
 * as long as those underlying state variables are independent, meaning that the
 * composite class does not impose any invariants involving the multiple state
 * variables
 *
 * VisualComponent allows clients to register listeners for mouse and keystroke
 * events
 *
 * There is no relationship between the set of mouse listeners and key listeners
 * The two are independent, and therefore VisualComponent can delegate its
 * thread safety obligations to two underlying thread-safe lists
 *
 * Each List is thread-safe, and because there are no constraints coupling the
 * state of one to the state of the other, VisualComponent can delegate its
 * thread safety responsibilities to the underlying mouseListeners and
 * keyListeners objects
 *
 * @author Brian Goetz and Tim Peierls
 */
public class VisualComponent {

    private final List<KeyListener> keyListeners
            = new CopyOnWriteArrayList<KeyListener>();

    private final List<MouseListener> mouseListeners
            = new CopyOnWriteArrayList<MouseListener>();

    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        mouseListeners.remove(listener);
    }

}///:~