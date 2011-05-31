// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

/**
 * GWT ok
 */

public interface IRelation extends IPrimitive {

    int getMembersCount();
    long getMemberId(int idx);
    String getRole(int idx);
    OsmPrimitiveType getMemberType(int idx);

}
