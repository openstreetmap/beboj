// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Mercator;

import com.google.gwt.junit.client.GWTTestCase;

public class RelationTest extends GWTTestCase {
    public String getModuleName() {
        return "org.openstreetmap.josm.JOSM";
    }

    @Override
    protected void gwtSetUp() {
        Main.proj = new Mercator();
        Main.pref = new Preferences();
    }

    public void testCreateNewRelation() {
        boolean thrown = false;
        try {
            new Relation(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    public void testEqualSemenaticsToNull() {
        Relation relation = new Relation();
        assertFalse(relation.hasEqualTechnicalAttributes(null));
    }

    public void testBBox() {
        DataSet ds = new DataSet();

        Node n1 = new Node(new LatLon(10, 10));
        Node n2 = new Node(new LatLon(20, 20));
        Node n3 = new Node(new LatLon(30, 30));
        Way w1 = new Way();
        w1.addNode(n1);
        w1.addNode(n2);
        Relation r1 = new Relation();
        Relation r2 = new Relation();
        ds.addPrimitive(r1);
        ds.addPrimitive(r2);
        ds.addPrimitive(n1);
        ds.addPrimitive(n2);
        ds.addPrimitive(n3);
        ds.addPrimitive(w1);
        r1.addMember(new RelationMember("", n1));
        r1.addMember(new RelationMember("", w1));
        r1.addMember(new RelationMember("", r1));
        r1.addMember(new RelationMember("", r2));
        r2.addMember(new RelationMember("", r1));
        r2.addMember(new RelationMember("", n3));

        BBox bbox = new BBox(w1);
        bbox.add(n3.getBBox());
        assertEquals(bbox, r1.getBBox());
        assertEquals(bbox, r2.getBBox());

        n3.setCoor(new LatLon(40, 40));
        bbox.add(n3.getBBox());
        assertEquals(bbox, r1.getBBox());
        assertEquals(bbox, r2.getBBox());

        r1.removeMembersFor(r2);
        assertEquals(w1.getBBox(), r1.getBBox());
        assertEquals(bbox, r2.getBBox());

        w1.addNode(n3);
        assertEquals(w1.getBBox(), r1.getBBox());
        assertEquals(w1.getBBox(), r2.getBBox());
    }

    public void testBBoxNotInDataset() {
        Node n1 = new Node(new LatLon(10, 10));
        Node n2 = new Node(new LatLon(20, 20));
        Way w1 = new Way();
        w1.addNode(n1);
        w1.addNode(n2);
        Relation r1 = new Relation();
        r1.getBBox();
        r1.addMember(new RelationMember("", w1));

        assertEquals(new BBox(w1), r1.getBBox());

        DataSet ds = new DataSet();
        ds.addPrimitive(n1);
        ds.addPrimitive(n2);
        ds.addPrimitive(w1);
        ds.addPrimitive(r1);

        assertEquals(new BBox(w1), r1.getBBox());

        ds.removePrimitive(r1);

        n1.setCoor(new LatLon(30, 40));
        assertEquals(new BBox(w1), r1.getBBox());

        ds.addPrimitive(r1);
        assertEquals(new BBox(w1), r1.getBBox());
    }

}