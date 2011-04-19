// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm;

import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm.data.projection.Projection;

/**
 * GWT
 *
 * FIXME
 *  stub
 */

public class Main {

    public static Projection proj;
    public static Preferences pref;

    public static void init() {
        proj = new Mercator();
        pref = new Preferences();
    }
}
