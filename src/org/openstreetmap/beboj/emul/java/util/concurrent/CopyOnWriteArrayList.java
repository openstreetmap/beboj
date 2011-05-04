// License: GPL. For details, see LICENSE file.
package java.util.concurrent;

import java.util.ArrayList;

/**
 * This is not really a CopyOnWriteArrayList but an ordinary ArrayList
 * with addIfAbsent method.
 *
 * Make sure, that the semantic of CopyOnWriteArrayList is not
 * required in the entire project. (Otherwise this class has to be fixed.)
 */
public class CopyOnWriteArrayList<T> extends ArrayList<T> {
    public boolean addIfAbsent(T obj) {
        if (!contains(obj)) {
            add(obj);
            return true;
        }
        return false;
    }
}
