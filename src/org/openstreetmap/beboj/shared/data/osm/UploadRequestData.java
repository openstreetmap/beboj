// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.shared.data.osm;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UploadRequestData implements IsSerializable {

    public String username;
    public String password;

    public String changeSetComment;

    public SimpleDataSet data;

}
