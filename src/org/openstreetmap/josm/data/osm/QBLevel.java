// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.coor.QuadTiling;

/**
 * GWT
 *
 * note
 *  in JOSM core, this is an inner class of QuadBuckets
 *
 * changelog
 *  added generic type parameter
 *  replaced reflection by cast
 *      - QBLevel[] result = (QBLevel[]) Array.newInstance(this.getClass(), QuadTiling.TILES_PER_LEVEL);
 *      + @SuppressWarnings("unchecked") QBLevel[] result = (QBLevel[]) new Object[QuadTiling.TILES_PER_LEVEL];
 *  added reference to parent QuadBuckets object to constructor (required to set the search_cache field)
 *  getChildren, search_contents and search: private -> package private
 * 
 */

class QBLevel<T extends OsmPrimitive>
{
    private static boolean debug = false;
    private static final boolean consistency_testing = false;
    private static final int NW_INDEX = 1;
    private static final int NE_INDEX = 3;
    private static final int SE_INDEX = 2;
    private static final int SW_INDEX = 0;
    public static int MAX_OBJECTS_PER_LEVEL = 16;
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


    private final QuadBuckets<T> parentBuckets;
    final int level;
    private final BBox bbox;
    final long quad;
    final QBLevel<T> parent;
    private boolean isLeaf = true;

    public List<T> content;
    public QBLevel<T> nw, ne, sw, se;

    private QBLevel<T> getChild(int index) {
        switch (index) {
        case NE_INDEX:
            if (ne == null) {
                ne = new QBLevel<T>(parentBuckets, this, index);
            }
            return ne;
        case NW_INDEX:
            if (nw == null) {
                nw = new QBLevel<T>(parentBuckets, this, index);
            }
            return nw;
        case SE_INDEX:
            if (se == null) {
                se = new QBLevel<T>(parentBuckets, this, index);
            }
            return se;
        case SW_INDEX:
            if (sw == null) {
                sw = new QBLevel<T>(parentBuckets, this, index);
            }
            return sw;
        default:
            return null;
        }
    }

    QBLevel<T>[] getChildren() {
        // This is ugly and hackish.  But, it seems to work,
        // and using an ArrayList here seems to cost us
        // a significant performance penalty -- 50% in my
        // testing.  Child access is one of the single
        // hottest code paths in this entire class.
        @SuppressWarnings("unchecked") QBLevel<T>[] result = (QBLevel<T>[]) new Object[QuadTiling.TILES_PER_LEVEL];
        result[NW_INDEX] = nw;
        result[NE_INDEX] = ne;
        result[SW_INDEX] = sw;
        result[SE_INDEX] = se;
        return result;
    }

    @Override
    public String toString()  {
        return super.toString()+ "["+level+"]: " + bbox();
    }
    /**
     * Constructor for root node
     */
    public QBLevel(QuadBuckets<T> parentBuckets) {
        this.parentBuckets = parentBuckets;
        level = 0;
        quad = 0;
        parent = null;
        bbox = new BBox(-180, 90, 180, -90);
    }

    public QBLevel(QuadBuckets<T> parentBuckets, QBLevel<T> parent, int parent_index) {
        this.parentBuckets = parentBuckets;
        this.parent = parent;
        this.level = parent.level + 1;
        int shift = (QuadTiling.NR_LEVELS - level) * 2;
        long mult = 1;
        // Java blows the big one.  It seems to wrap when
        // you shift by > 31
        if (shift >= 30) {
            shift -= 30;
            mult = 1<<30;
        }
        long this_quadpart = mult * (parent_index << shift);
        this.quad = parent.quad | this_quadpart;
        this.bbox = calculateBBox(); // calculateBBox reference quad
        if (debug) {
            out("new level["+this.level+"] bbox["+parent_index+"]: " + this.bbox()
                    + " coor: " + this.coor()
                    + " quadpart: " + Long.toHexString(this_quadpart)
                    + " quad: " + Long.toHexString(this.quad));
        }
    }

    private BBox calculateBBox() {
        LatLon bottom_left = this.coor();
        double lat = bottom_left.lat() + parent.height() / 2;
        double lon = bottom_left.lon() + parent.width() / 2;
        LatLon top_right = new LatLon(lat, lon);
        return new BBox(bottom_left, top_right);
    }

    QBLevel<T> findBucket(BBox bbox) {
        if (!hasChildren())
            return this;
        else {
            int index = get_index(bbox, level);
            if (index == -1)
                return this;
            return getChild(index).findBucket(bbox);
        }
    }

    boolean remove_content(T o)
    {
        // If two threads try to remove item at the same time from different buckets of this QBLevel,
        // it might happen that one thread removes bucket but don't remove parent because it still sees
        // another bucket set. Second thread do the same. Due to thread memory caching, it's possible that
        // changes made by threads will show up in children array too late, leading to QBLevel with all children
        // set to null
        if (content == null)
            return false;
        boolean ret = this.content.remove(o);
        if (this.content.size() == 0) {
            this.content = null;
        }
        if (this.canRemove()) {
            this.remove_from_parent();
        }
        return ret;
    }
    // Get the correct index for the given primitive
    // at the given level.  If the primitive can not
    // fit into a single quad at this level, return -1
    int get_index(BBox bbox, int level) {
        int index = -1;
        for (LatLon c : bbox.points()) {
            if (debug) {
                out("getting index for point: " + c);
            }
            if (index == -1) {
                index = QuadTiling.index(c, level);
                if (debug) {
                    out("set initial index to: " + index);
                }
                continue;
            }
            int another_index = QuadTiling.index(c, level);
            if (debug) {
                out("other point index: " + another_index);
            }
            if (another_index != index)
                return -1;
        }
        return index;
    }
    /*
     * There is a race between this and qb.nextContentNode().
     * If nextContentNode() runs into this bucket, it may
     * attempt to null out 'children' because it thinks this
     * is a dead end.
     */
    void __split() {
        if (debug) {
            out("splitting "+this.bbox()+" level "+level+" with "
                    + content.size() + " entries (my dimensions: "
                    + this.bbox().width()+", "+this.bbox().height()+")");
        }
        List<T> tmpcontent = content;
        content = null;

        for (T o: tmpcontent) {
            int index = get_index(o.getBBox(), level);
            if (index == -1) {
                __add_content(o);
            } else {
                getChild(index).doAdd(o);
            }
        }
        isLeaf = false; // It's not enough to check children because all items could end up in this level (index == -1)
    }

    boolean __add_content(T o)
    {
        boolean ret = false;
        // The split_lock will keep two concurrent
        // calls from overwriting content
        if (content == null) {
            content = new ArrayList<T>();
        }
        ret = content.add(o);
        if (debug && !this.isLeaf()) {
            pout("added "+o.getClass().getName()+" to non-leaf with size: " + content.size());
        }
        return ret;
    }
    boolean matches(T o, BBox search_bbox)
    {
        // This can be optimized when o is a node
        //return search_bbox.bounds(coor));
        return o.getBBox().intersects(search_bbox);
    }
    void search_contents(BBox search_bbox, List<T> result)
    {
        if (debug) {
            out("searching contents (size: " + content == null?"<null>":content.size() + ") for " + search_bbox);
        }
        /*
         * It is possible that this was created in a split
         * but never got any content populated.
         */
        if (content == null)
            return;

        for (T o : content) {
            if (matches(o, search_bbox)) {
                result.add(o);
            }
        }
        if (debug) {
            out("done searching quad " + Long.toHexString(this.quad));
        }
    }
    /*
     * This is stupid.  I tried to have a QBLeaf and QBBranch
     * class decending from a QBLevel.  It's more than twice
     * as slow.  So, this throws OO out the window, but it
     * is fast.  Runtime type determination must be slow.
     */
    boolean isLeaf() {
        return isLeaf;
    }
    boolean hasChildren() {
        return nw != null || ne != null || sw != null || se != null;
    }

    QBLevel<T> next_sibling()
    {
        boolean found_me = false;
        if (parent == null)
            return null;
        int __nr = 0;
        for (QBLevel<T> sibling : parent.getChildren()) {
            __nr++;
            int nr = __nr-1;
            if (sibling == null) {
                if (debug) {
                    out("[" + this.level + "] null child nr: " + nr);
                }
                continue;
            }
            // We're looking for the *next* child
            // after us.
            if (sibling == this) {
                if (debug) {
                    out("[" + this.level + "] I was child nr: " + nr);
                }
                found_me = true;
                continue;
            }
            if (found_me) {
                if (debug) {
                    out("[" + this.level + "] next sibling was child nr: " + nr);
                }
                return sibling;
            }
            if (debug) {
                out("[" + this.level + "] nr: " + nr + " is before me, ignoring...");
            }
        }
        return null;
    }
    boolean hasContent()
    {
        return content != null;
    }
    QBLevel<T> nextSibling()
    {
        QBLevel<T> next = this;
        QBLevel<T> sibling = next.next_sibling();
        // Walk back up the tree to find the
        // next sibling node.  It may be either
        // a leaf or branch.
        while (sibling == null) {
            if (debug) {
                out("no siblings at level["+next.level+"], moving to parent");
            }
            next = next.parent;
            if (next == null) {
                break;
            }
            sibling = next.next_sibling();
        }
        next = sibling;
        return next;
    }
    QBLevel<T> firstChild()
    {
        QBLevel<T> ret = null;
        for (QBLevel<T> child : getChildren()) {
            if (child == null) {
                continue;
            }
            ret = child;
            break;
        }
        return ret;
    }
    QBLevel<T> nextNode()
    {
        if (!this.hasChildren())
            return this.nextSibling();
        return this.firstChild();
    }
    QBLevel<T> nextContentNode()
    {
        QBLevel<T> next = this.nextNode();
        if (next == null)
            return next;
        if (next.hasContent())
            return next;
        return next.nextContentNode();
    }

    void doAdd(T o) {
        if (consistency_testing) {
            if (!matches(o, this.bbox())) {
                out("-----------------------------");
                debug = true;
                get_index(o.getBBox(), level);
                get_index(o.getBBox(), level-1);
                int nr = 0;
                for (QBLevel<T> sibling : parent.getChildren()) {
                    out("sibling["+ (nr++) +"]: " + sibling.bbox() + " this: " + (this==sibling));
                }
                abort("\nobject " + o + " does not belong in node at level: " + level + " bbox: " + this.bbox());
            }
        }
        __add_content(o);
        if (isLeaf() && content.size() > MAX_OBJECTS_PER_LEVEL && level < QuadTiling.NR_LEVELS) {
            __split();
        }
    }

    void add(T o) {
        findBucket(o.getBBox()).doAdd(o);
    }

    void search(BBox search_bbox, List<T> result)
    {
        if (debug) {
            System.out.print("[" + level + "] qb bbox: " + this.bbox() + " ");
        }
        if (!this.bbox().intersects(search_bbox)) {
            if (debug) {
                out("miss " + Long.toHexString(this.quad));
                //QuadTiling.tile2xy(this.quad);
            }
            return;
        } else if (bbox().bounds(search_bbox)) {
            parentBuckets.search_cache = this;
        }

        if (this.hasContent()) {
            search_contents(search_bbox, result);
        }

        if (debug) {
            out("hit " + this.quads());
        }

        if (debug) {
            out("[" + level + "] not leaf, moving down");
        }

        //TODO Coincidence vector should be calculated here and only buckets that match search_bbox should be checked

        if (nw != null) {
            nw.search(search_bbox, result);
        }
        if (ne != null) {
            ne.search(search_bbox, result);
        }
        if (se != null) {
            se.search(search_bbox, result);
        }
        if (sw != null) {
            sw.search(search_bbox, result);
        }
    }
    public String quads()
    {
        return Long.toHexString(quad);
    }
    int index_of(QBLevel<T> find_this)
    {
        QBLevel<T>[] children = getChildren();
        for (int i = 0; i < QuadTiling.TILES_PER_LEVEL; i++) {
            if (children[i] == find_this)
                return i;
        }
        return -1;
    }
    double width() {
        return bbox.width();
    }

    double height() {
        return bbox.height();
    }

    public BBox bbox() {
        return bbox;
    }
    /*
     * This gives the coordinate of the bottom-left
     * corner of the box
     */
    LatLon coor()
    {
        return QuadTiling.tile2LatLon(this.quad);
    }
    void remove_from_parent()
    {
        if (parent == null)
            return;

        if (!canRemove()) {
            abort("attempt to remove non-empty child: " + this.content + " " + Arrays.toString(this.getChildren()));
        }

        if (parent.nw == this) {
            parent.nw = null;
        } else if (parent.ne == this) {
            parent.ne = null;
        } else if (parent.sw == this) {
            parent.sw = null;
        } else if (parent.se == this) {
            parent.se = null;
        }

        if (parent.canRemove()) {
            parent.remove_from_parent();
        }
    }
    boolean canRemove()
    {
        if (content != null && content.size() > 0)
            return false;
        if (this.hasChildren())
            return false;
        return true;
    }
}

