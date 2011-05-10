// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.shared.data.osm;

import java.util.ArrayList;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.NodeData;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationData;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.WayData;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Just the plain data from a DataSet; for RPC transfer.
 */
public class SimpleDataSet implements IsSerializable {

    public ArrayList<NodeData> nd;
    public ArrayList<WayData> wd;
    public ArrayList<RelationData> rd;
    public String version;
//    /**
//     * All data sources of this DataSet.
//     */
//    public Collection<DataSource> dataSources = new LinkedList<DataSource>();

    protected SimpleDataSet() {
    }

    public SimpleDataSet(String input) {
        version = input;
        nd = new ArrayList<NodeData>();
        wd = new ArrayList<WayData>();
        rd = new ArrayList<RelationData>();

//        ArrayList<RelationMemberData> memberData = new ArrayList<RelationMemberData>();
//        memberData.add(new RelationMemberData("outer", OsmPrimitiveType.WAY, 123l));
//        memberData.add(new RelationMemberData("inner", OsmPrimitiveType.WAY, 1234l));
//        rd.setMembers(memberData);
    }

    public static SimpleDataSet fromDataSet(DataSet ds) {
        SimpleDataSet s = new SimpleDataSet();

        s.nd = new ArrayList<NodeData>();
        s.wd = new ArrayList<WayData>();
        s.rd = new ArrayList<RelationData>();

        for (Node n : ds.getNodes()) {
            NodeData nd = n.save();
            s.nd.add(nd);
        }
        for (Way w : ds.getWays()) {
            WayData wd = w.save();
            s.wd.add(wd);
        }
        for (Relation r : ds.getRelations()) {
            RelationData rd = r.save();
            s.rd.add(rd);
        }

        s.version = ds.getVersion();

        return s;
    }

    public DataSet toDataSet() {
        return null;
    }

    @Override
    public String toString() {
        return version+nd.size();//data+" "+keys+" "+d+" "+u+" WAY: "+wd+" RELATION: "+rd;
    }
}
