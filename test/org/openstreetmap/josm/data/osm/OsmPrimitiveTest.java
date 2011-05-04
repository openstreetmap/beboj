// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import java.util.Arrays;
import java.util.HashSet;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;

import com.google.gwt.junit.client.GWTTestCase;

public class OsmPrimitiveTest extends GWTTestCase {
    public String getModuleName() {
        return "org.openstreetmap.josm.JOSM";
    }

    private void compareReferrers(OsmPrimitive actual, OsmPrimitive... expected) {
        assertEquals(new HashSet<OsmPrimitive>(Arrays.asList(expected)),
                new HashSet<OsmPrimitive>(actual.getReferrers()));
    }

    private DataSet dataSet = new DataSet();


    @Override
    protected void gwtSetUp() {
//        Main.init(); FIXME
    }

    public void testSimpleReferrers() {
        Node n1 = new Node(new LatLon(0.0, 0.0));
        Way w1 = new Way();
        w1.addNode(n1);
        dataSet.addPrimitive(n1);
        dataSet.addPrimitive(w1);
        compareReferrers(n1, w1);
    }

    public void testAddAndRemoveReferrer() {
        Node n1 = new Node(new LatLon(0.0, 0.0));
        Node n2 = new Node(new LatLon(0.0, 0.0));
        Way w1 = new Way();
        w1.addNode(n1);
        w1.addNode(n2);
        w1.addNode(n1);
        w1.removeNode(n1);
        dataSet.addPrimitive(n1);
        dataSet.addPrimitive(n2);
        dataSet.addPrimitive(w1);
        compareReferrers(n1);
        compareReferrers(n2, w1);
    }

    public void testMultipleReferrers() {
        Node n1 = new Node(new LatLon(0.0, 0.0));
        Way w1 = new Way();
        Way w2 = new Way();
        Relation r1 = new Relation();
        w1.addNode(n1);
        w2.addNode(n1);
        r1.addMember(new RelationMember("", n1));
        dataSet.addPrimitive(n1);
        dataSet.addPrimitive(w1);
        dataSet.addPrimitive(w2);
        dataSet.addPrimitive(r1);
        compareReferrers(n1, w1, w2, r1);
    }

    public void testRemoveMemberFromRelationReferrer() {
        Node n1 = new Node(new LatLon(0, 0));
        Relation r1 = new Relation();
        r1.addMember(new RelationMember("", n1));
        r1.addMember(new RelationMember("", n1));
        r1.removeMember(0);
        dataSet.addPrimitive(n1);
        dataSet.addPrimitive(r1);
        compareReferrers(n1, r1);
    }

    public void testSetRelationMemberReferrer() {
        Node n1 = new Node(new LatLon(0, 0));
        Node n2 = new Node(new LatLon(0, 0));
        Relation r1 = new Relation();
        Relation r2 = new Relation();
        r1.addMember(new RelationMember("", n1));
        r2.addMember(new RelationMember("", n2));
        r2.setMember(0, r1.getMember(0));
        dataSet.addPrimitive(n1);
        dataSet.addPrimitive(n2);
        dataSet.addPrimitive(r1);
        dataSet.addPrimitive(r2);
        compareReferrers(n1, r1, r2);
        compareReferrers(n2);
    }

    public void testRemovePrimitiveReferrer() {
        Node n1 = new Node(new LatLon(0.0, 0.0));
        Way w1 = new Way();
        w1.addNode(n1);
        w1.setDeleted(true);
        dataSet.addPrimitive(n1);
        compareReferrers(n1);
        w1.setDeleted(false);
        dataSet.addPrimitive(w1);

        compareReferrers(n1, w1);

        Relation r1 = new Relation();
        r1.addMember(new RelationMember("", w1));
        r1.setDeleted(true);
        dataSet.addPrimitive(r1);
        compareReferrers(w1);
        r1.setDeleted(false);
        compareReferrers(w1, r1);
    }

    public void testNodeFromMultipleDatasets() {
        // n has two referrers - w1 and w2. But only w1 is returned because it is in the same dataset as n
        Node n = new Node(new LatLon(0.0, 0.0));

        Way w1 = new Way();
        w1.addNode(n);
        dataSet.addPrimitive(n);
        dataSet.addPrimitive(w1);
        new Way(w1);

        assertEquals(n.getReferrers().size(), 1);
        assertEquals(n.getReferrers().get(0), w1);
    }

    public void testCheckMustBeInDatasate() {
        Node n = new Node();
        boolean thrown = false;
        try {
            n.getReferrers();
        } catch (DataIntegrityProblemException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
