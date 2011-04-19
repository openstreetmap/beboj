// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.coor.LatLon;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Some unit test cases for basic tag management on {@see OsmPrimitive}. Uses
 * {@see Node} for the tests, {@see OsmPrimitive} is abstract.
 *
 */
public class OsmPrimitiveKeyHandling extends GWTTestCase {
    public String getModuleName() {
        return "org.openstreetmap.josm.JOSM";
    }

    @Override
    protected void gwtSetUp() {
        Main.pref = new Preferences();
    }

    /**
     * test query and get methods on a node withouth keys
     */
    public void testEmptyNode() {
        Node n = new Node();
        assertTrue(n.getKeys().size() == 0);
        assertTrue(!n.hasKeys());
        assertTrue(!n.hasKey("nosuchkey"));
        assertTrue(n.keySet().isEmpty());

        n.remove("nosuchkey"); // should work
    }

    /**
     * Add a tag to an empty node and test the query and get methods.
     *
     */
    public void testPut() {
        Node n = new Node();
        n.put("akey", "avalue");
        assertTrue(n.get("akey").equals("avalue"));
        assertTrue(n.getKeys().size() == 1);

        assertTrue(n.keySet().size() == 1);
        assertTrue(n.keySet().contains("akey"));
    }

    /**
     * Add two tags to an empty node and test the query and get methods.
     */
    public void testPut2() {
        Node n = new Node();
        n.put("key.1", "value.1");
        n.put("key.2", "value.2");
        assertTrue(n.get("key.1").equals("value.1"));
        assertTrue(n.get("key.2").equals("value.2"));
        assertTrue(n.getKeys().size() == 2);
        assertTrue(n.hasKeys());
        assertTrue(n.hasKey("key.1"));
        assertTrue(n.hasKey("key.2"));
        assertTrue(!n.hasKey("nosuchkey"));
    }

    /**
     * Remove tags from a node with two tags and test the state of the node.
     *
     */
    public void testRemove() {
        Node n = new Node();
        n.put("key.1", "value.1");
        n.put("key.2", "value.2");

        n.remove("nosuchkey");               // should work
        assertTrue(n.getKeys().size() == 2); // still 2 tags ?

        n.remove("key.1");
        assertTrue(n.getKeys().size() == 1);
        assertTrue(!n.hasKey("key.1"));
        assertTrue(n.get("key.1") == null);
        assertTrue(n.hasKey("key.2"));
        assertTrue(n.get("key.2").equals("value.2"));

        n.remove("key.2");
        assertTrue(n.getKeys().size() == 0);
        assertTrue(!n.hasKey("key.1"));
        assertTrue(n.get("key.1") == null);
        assertTrue(!n.hasKey("key.2"));
        assertTrue(n.get("key.2") == null);
    }

    /**
     * Remove all tags from a node
     *
     */
    public void testRemoveAll() {
        Node n = new Node();

        n.put("key.1", "value.1");
        n.put("key.2", "value.2");

        n.removeAll();
        assertTrue(n.getKeys().size() == 0);
    }

    /**
     * Test hasEqualSemanticAttributes on two nodes whose identical tags are added
     * in different orders.
     */
    public void testHasEqualSemanticAttributes() {
        Node n1 = new Node(1);
        n1.setCoor(new LatLon(0,0));
        n1.put("key.1", "value.1");
        n1.put("key.2", "value.2");

        Node n2 = new Node(1);
        n2.setCoor(new LatLon(0,0));
        n2.put("key.2", "value.2");
        n2.put("key.1", "value.1");

        assertTrue(n1.hasEqualSemanticAttributes(n2));
    }

    /**
     * Test hasEqualSemanticAttributes on two nodes with different tags.
     */
    public void testHasEqualSemanticAttributes_2() {
        Node n1 = new Node(1);
        n1.setCoor(new LatLon(0,0));
        n1.put("key.1", "value.1");
        n1.put("key.2", "value.3");

        Node n2 = new Node(1);
        n2.setCoor(new LatLon(0,0));
        n2.put("key.1", "value.1");
        n2.put("key.2", "value.4");

        assertTrue(!n1.hasEqualSemanticAttributes(n2));
    }

}
