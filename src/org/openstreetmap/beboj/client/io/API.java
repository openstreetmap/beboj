// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.io;

import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("api")
public interface API extends RemoteService {

    String greetServer(String input) throws IllegalArgumentException;

    SimpleDataSet downloadOsmData(double minlat, double minlon, double maxlat, double maxlon);

}
