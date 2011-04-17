// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.coor;


import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class LatLonTest extends GWTTestCase {
    public String getModuleName() {
        return "org.openstreetmap.josm.JOSM";
    }

    final private double EPSILON = 1e-9;

    @Test
    public void testIsValidLon() {
        assertFalse(LatLon.isValidLon(190));
    }

    @Test
    public void testLatLonDoubleDouble() {
        LatLon ll = new LatLon(1.2, 3.4);
        assertTrue(ll.lat() == 1.2);
        assertTrue(ll.lon() == 3.4);
    }

    @Test
    public void testGreatCircleDistance() {
        LatLon ll1 = new LatLon(35.2, 123.4);
        LatLon ll2 = new LatLon(50.1, 123.7);
        double gcd = ll1.greatCircleDistance(ll2);
        //System.err.print(gcd);
        //GWT.log("gcd"+gcd);
        assertEquals(gcd, 1658838.1065105733, EPSILON);
    }
}
