// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.shared.data.osm;

import java.util.HashMap;

import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.io.DiffResultEntry;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UploadResponseData implements IsSerializable {

    public HashMap<SimplePrimitiveId, DiffResultEntry> diffResults = new HashMap<SimplePrimitiveId, DiffResultEntry>();

}
