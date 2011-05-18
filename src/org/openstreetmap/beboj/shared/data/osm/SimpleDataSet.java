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

    public ArrayList<NodeData> nodes;
    public ArrayList<WayData> ways;
    public ArrayList<RelationData> relations;
    public String version;

//    /**
//     * All data sources of this DataSet.
//     */
//    public Collection<DataSource> dataSources = new LinkedList<DataSource>();

    protected SimpleDataSet() {
    }

    public SimpleDataSet(String input) {
        version = input;
        nodes = new ArrayList<NodeData>();
        ways = new ArrayList<WayData>();
        relations = new ArrayList<RelationData>();
    }

    public static SimpleDataSet fromDataSet(DataSet ds) {
        SimpleDataSet s = new SimpleDataSet();

        s.nodes = new ArrayList<NodeData>();
        s.ways = new ArrayList<WayData>();
        s.relations = new ArrayList<RelationData>();

        for (Node n : ds.getNodes()) {
            NodeData nd = n.save();
            s.nodes.add(nd);
        }
        for (Way w : ds.getWays()) {
            WayData wd = w.save();
            s.ways.add(wd);
        }
        for (Relation r : ds.getRelations()) {
            RelationData rd = r.save();
            s.relations.add(rd);
        }

        s.version = ds.getVersion();

        return s;
    }

    public DataSet toDataSet() {
        DataSet ds = new DataSet();
        for (NodeData nd : nodes) {
            Node n = new Node(nd.getUniqueId());
            ds.addPrimitive(n);
            n.load(nd);
        }
        for (WayData wd : ways) {
            Way w = new Way(wd.getUniqueId());
            ds.addPrimitive(w);
            w.load(wd);
        }
        for (RelationData rd : relations) {
            Relation r = new Relation(rd.getUniqueId());
            ds.addPrimitive(r);
            r.load(rd);
        }
        return ds;
    }

    @Override
    public String toString() {
        return version+nodes.size()+"|";
    }
}
