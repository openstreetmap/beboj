// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.io;

import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface APIAsync {

    void greetServer(String input, AsyncCallback<String> callback);

    void downloadOsmData(double minlat, double minlon, double maxlat, double maxlon, AsyncCallback<SimpleDataSet> callback);

}
