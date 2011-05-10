// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.server;

import org.openstreetmap.beboj.client.io.API;
import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class APIImpl extends RemoteServiceServlet implements API {

    @Override
    public SimpleDataSet greetServer(String input)
            throws IllegalArgumentException {
        return new SimpleDataSet(input + " .");
    }

}
