// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.io;

import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;
import org.openstreetmap.beboj.shared.data.osm.UploadRequestData;
import org.openstreetmap.beboj.shared.data.osm.UploadResponseData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface APIAsync {

    void greetServer(String input, AsyncCallback<String> callback);

    void downloadOsmData(double minlat, double minlon, double maxlat, double maxlon, AsyncCallback<SimpleDataSet> callback);

    void uploadOsmData(UploadRequestData request, AsyncCallback<UploadResponseData> callback);

}
