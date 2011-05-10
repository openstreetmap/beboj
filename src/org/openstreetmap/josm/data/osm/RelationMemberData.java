// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * GWT
 * 
 * changelog
 *  made class gwt-serializable 
 *   - fields 'role', 'memberId', 'memberType': private final -> public
 *   - added no arg constructor
 */

public class RelationMemberData implements IsSerializable, PrimitiveId {

    public /* private final */ String role;
    public /* private final */ long memberId;
    public /* private final */ OsmPrimitiveType memberType;

    /** no arg constructor for GWT RPC serialization */
    @SuppressWarnings("unused")
    private RelationMemberData() {
    }

    public RelationMemberData(String role, OsmPrimitiveType type, long id) {
        this.role = role == null?"":role;
        this.memberType = type;
        this.memberId = id;
    }

    public RelationMemberData(String role, PrimitiveId primitive) {
        this(role, primitive.getType(), primitive.getUniqueId());
    }

    public long getMemberId() {
        return memberId;
    }
    public String getRole() {
        return role;
    }
    public OsmPrimitiveType getMemberType() {
        return memberType;
    }

    public boolean hasRole() {
        return !"".equals(role);
    }

    @Override
    public String toString() {
        return memberType.getAPIName() + " " + memberId;
    }

    /**
     * PrimitiveId implementation. Returns the same value as {@link #getMemberType()}
     */
    public OsmPrimitiveType getType() {
        return memberType;
    }

    /**
     * PrimitiveId implementation. Returns the same value as {@link #getMemberId()()}
     */
    public long getUniqueId() {
        return memberId;
    }

    public boolean isNew() {
        return memberId <= 0;
    }
}
