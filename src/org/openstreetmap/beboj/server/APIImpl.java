// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.server;

import org.openstreetmap.beboj.client.io.API;
import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.projection.Mercator;
import org.openstreetmap.josm_server.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm_server.io.BoundingBoxDownloader;
import org.openstreetmap.josm_server.io.OsmTransferException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class APIImpl extends RemoteServiceServlet implements API {

    @Override
    public String greetServer(String input)
            throws IllegalArgumentException {
        return String.format("Hi %s!", input);
    }

    @Override
    public SimpleDataSet downloadOsmData(double minlat, double minlon, double maxlat, double maxlon) {
        initialize();
        BoundingBoxDownloader down = new BoundingBoxDownloader(new Bounds(minlat, minlon, maxlat, maxlon));
        DataSet ds = null;
        try {
            ds = down.parseOsm(NullProgressMonitor.INSTANCE);
        } catch (OsmTransferException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDataSet sds = SimpleDataSet.fromDataSet(ds);
        return sds;
    }

    private void initialize() {
        if (Main.pref == null) {
            Main.pref = new Preferences();
        }
        if (Main.proj == null) {
            Main.proj = new Mercator();
        }
    }
}
