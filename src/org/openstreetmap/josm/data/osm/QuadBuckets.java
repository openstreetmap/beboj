// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * GWT
 *
 * changelog
 *  moved inner class QBLevel to separate file to work around gwt bug
 *    (see http://code.google.com/p/google-web-toolkit/issues/detail?id=5483)
 *  QBLevel now requires type parameter and reference to 'this' in constructor
 *  search_cache: private -> package private
 */

/**
 * Note: bbox of primitives added to QuadBuckets has to stay the same. In case of coordinate change, primitive must
 * be removed and readded.
 *
 * This class is (no longer) thread safe.
 *
 */
public class QuadBuckets<T extends OsmPrimitive> implements Collection<T>
{
    private static boolean debug = false;

    static void abort(String s)
    {
        throw new AssertionError(s);
    }
    static void out(String s)
    {
        System.out.println(s);
    }
    // periodic output
    long last_out = -1;
    void pout(String s)
    {
        long now = System.currentTimeMillis();
        if (now - last_out < 300)
            return;
        last_out = now;
        System.out.print(s + "\r");
    }
    void pout(String s, int i, int total)
    {
        long now = System.currentTimeMillis();
        if ((now - last_out < 300) &&
                ((i+1) < total))
            return;
        last_out = now;
        // cast to float to keep the output size down
        System.out.print(s + " " + (float)((i+1)*100.0/total) + "% done    \r");
    }

    public static int MAX_OBJECTS_PER_LEVEL = 16;

    private QBLevel<T> root;
    QBLevel<T> search_cache;
    private int size;

    public QuadBuckets()
    {
        clear();
    }
    public void clear()  {
        root = new QBLevel<T>(this);
        search_cache = null;
        size = 0;
        if (debug) {
            out("QuadBuckets() cleared: " + this);
            out("root: " + root + " level: " + root.level + " bbox: " + root.bbox());
        }
    }
    public boolean add(T n) {
        root.add(n);
        size++;
        return true;
    }

    public void unsupported()
    {
        out("unsupported operation");
        throw new UnsupportedOperationException();
    }
    public boolean retainAll(Collection<?> objects)
    {
        for (T o : this) {
            if (objects.contains(o)) {
                continue;
            }
            if (!this.remove(o))
                return false;
        }
        return true;
    }
    public boolean removeAll(Collection<?> objects)
    {
        boolean changed = false;
        for (Object o : objects) {
            changed = changed | remove(o);
        }
        return changed;
    }
    public boolean addAll(Collection<? extends T> objects)
    {
        boolean changed = false;
        for (T o : objects) {
            changed = changed | this.add(o);
        }
        return changed;
    }
    public boolean containsAll(Collection<?> objects)
    {
        for (Object o : objects) {
            if (!this.contains(o))
                return false;
        }
        return true;
    }
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked") T t = (T) o;
        search_cache = null; // Search cache might point to one of removed buckets
        QBLevel<T> bucket = root.findBucket(t.getBBox());
        if (bucket.remove_content(t)) {
            size--;
            return true;
        } else
            return false;
    }
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked") T t = (T) o;
        QBLevel<T> bucket = root.findBucket(t.getBBox());
        return bucket != null && bucket.content != null && bucket.content.contains(t);
    }

    public ArrayList<T> toArrayList()
    {
        ArrayList<T> a = new ArrayList<T>();
        for (T n : this) {
            a.add(n);
        }
        if (debug) {
            out("returning array list with size: " + a.size());
        }
        return a;
    }
    public Object[] toArray()
    {
        return this.toArrayList().toArray();
    }
    public <A> A[] toArray(A[] template)
    {
        return this.toArrayList().toArray(template);
    }
    class QuadBucketIterator implements Iterator<T>
    {
        QBLevel<T> current_node;
        int content_index;
        int iterated_over;
        QBLevel<T> next_content_node(QBLevel<T> q)
        {
            if (q == null)
                return null;
            QBLevel<T> orig = q;
            QBLevel<T> next;
            next = q.nextContentNode();
            //if (consistency_testing && (orig == next))
            if (orig == next) {
                abort("got same leaf back leaf: " + q.isLeaf());
            }
            return next;
        }
        public QuadBucketIterator(QuadBuckets<T> qb)
        {
            if (debug) {
                out(this + " is a new iterator qb: " + qb + " size: " + qb.size());
            }
            if (!qb.root.hasChildren() || qb.root.hasContent()) {
                current_node = qb.root;
            } else {
                current_node = next_content_node(qb.root);
            }
            if (debug) {
                out("\titerator first leaf: " + current_node);
            }
            iterated_over = 0;
        }
        public boolean hasNext()
        {
            if (this.peek() == null) {
                if (debug) {
                    out(this + " no hasNext(), but iterated over so far: " + iterated_over);
                }
                return false;
            }
            return true;
        }
        T peek()
        {
            if (current_node == null) {
                if (debug) {
                    out("null current leaf, nowhere to go");
                }
                return null;
            }
            while((current_node.content == null) ||
                    (content_index >= current_node.content.size())) {
                if (debug) {
                    out("moving to next leaf");
                }
                content_index = 0;
                current_node = next_content_node(current_node);
                if (current_node == null) {
                    break;
                }
            }
            if (current_node == null || current_node.content == null) {
                if (debug) {
                    out("late nowhere to go " + current_node);
                }
                return null;
            }
            return current_node.content.get(content_index);
        }
        public T next()
        {
            T ret = peek();
            content_index++;
            if (debug) {
                out("iteration["+iterated_over+"] " + content_index + " leaf: " + current_node);
            }
            iterated_over++;
            if (ret == null) {
                if (debug) {
                    out(this + " no next node, but iterated over so far: " + iterated_over);
                }
            }
            return ret;
        }
        public void remove()
        {
            // two uses
            // 1. Back up to the thing we just returned
            // 2. move the index back since we removed
            //    an element
            content_index--;
            T object = peek();
            current_node.remove_content(object);
        }
    }
    public Iterator<T> iterator()
    {
        return new QuadBucketIterator(this);
    }
    public int size() {
        return size;
    }

    public boolean isEmpty()
    {
        if (this.size() == 0)
            return true;
        return false;
    }
    public List<T> search(BBox search_bbox) {
        if (debug) {
            out("qb root search at " + search_bbox);
            out("root bbox: " + root.bbox());
        }
        List<T> ret = new ArrayList<T>();
        // Doing this cuts down search cost on a real-life data
        // set by about 25%
        boolean cache_searches = true;
        if (cache_searches) {
            if (search_cache == null) {
                search_cache = root;
            }
            // Walk back up the tree when the last
            // search spot can not cover the current
            // search
            while (!search_cache.bbox().bounds(search_bbox)) {
                if (debug) {
                    out("bbox: " + search_bbox);
                }
                if (debug) {
                    out("search_cache: " + search_cache + " level: " + search_cache.level);
                    out("search_cache.bbox(): " + search_cache.bbox());
                }
                search_cache = search_cache.parent;
                if (debug) {
                    out("new search_cache: " + search_cache);
                }
            }
        } else {
            search_cache = root;
        }

        // Save parent because search_cache might change during search call
        QBLevel<T> tmp = search_cache.parent;

        search_cache.search(search_bbox, ret);

        // A way that spans this bucket may be stored in one
        // of the nodes which is a parent of the search cache
        while (tmp != null) {
            tmp.search_contents(search_bbox, ret);
            tmp = tmp.parent;
        }
        if (debug) {
            out("search of QuadBuckets for " + search_bbox + " ret len: " + ret.size());
        }
        return ret;
    }

    public void printTree() {
        printTreeRecursive(root, 0);
    }

    private void printTreeRecursive(QBLevel<T> level, int indent) {
        if (level == null) {
            printIndented(indent, "<empty child>");
            return;
        }
        printIndented(indent, level);
        if (level.hasContent()) {
            for (T o:level.content) {
                printIndented(indent, o);
            }
        }
        for (QBLevel<T> child:level.getChildren()) {
            printTreeRecursive(child, indent + 2);
        }
    }

    private void printIndented(int indent, Object msg) {
        for (int i=0; i<indent; i++) {
            System.out.print(' ');
        }
        System.out.println(msg);
    }
}
