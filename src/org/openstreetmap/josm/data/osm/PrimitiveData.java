// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.osm;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * GWT
 *
 * changelog
 *  made class gwt-serializable
 *   field 'keys': private final -> public
 *                 Set -> HashSet
 *   other fields: private -> public
 *  
 * TODO
 *  missing method: getFilteredList
 */

/**
 *
 * This class can be used to save properties of OsmPrimitive. The main difference between PrimitiveData
 * and OsmPrimitive is that PrimitiveData is not part of the dataset and changes in PrimitiveData are not
 * reported by events
 *
 */
public abstract class PrimitiveData implements Tagged, PrimitiveId, IsSerializable {

    // Useful?
    //private boolean disabled;
    //private boolean filtered;
    //private boolean selected;
    //private boolean highlighted;

    public PrimitiveData() {
        id = OsmPrimitive.generateUniqueId();
    }

    public PrimitiveData(PrimitiveData data) {
        this.keys.putAll(data.keys);
        this.modified = data.modified;
        this.visible = data.visible;
        this.deleted = data.deleted;
        this.id = data.id;
        this.user = data.user;
        this.version = data.version;
        this.timestamp = data.timestamp;
        this.incomplete = data.incomplete;
    }

    public /* private final */ HashMap<String, String> keys = new HashMap<String, String>();
    public /* private */ boolean modified;
    public /* private */ boolean visible = true;
    public /* private */ boolean deleted;
    public /* private */ boolean incomplete;
    public /* private */ long id;
    public /* private */ User user;
    public /* private */ int version;
    public /* private */ Date timestamp = new Date();
    public /* private */ int changesetId;

    public boolean isModified() {
        return modified;
    }
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public long getId() {
        return id > 0 ? id : 0;
    }
    public void setId(long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getChangesetId() {
        return changesetId;
    }

    public void setChangesetId(int changesetId) {
        this.changesetId = changesetId;
    }

    public Map<String, String> getKeys() {
        return keys;
    }
    public boolean isIncomplete() {
        return incomplete;
    }
    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    public void clearOsmId() {
        id = OsmPrimitive.generateUniqueId();
    }

    public abstract PrimitiveData makeCopy();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(id).append(keys);
        if (modified) {
            builder.append("M");
        }
        if (visible) {
            builder.append("V");
        }
        if (deleted) {
            builder.append("D");
        }
        if (incomplete) {
            builder.append("I");
        }

        return builder.toString();
    }

    // Tagged implementation

    public String get(String key) {
        return keys.get(key);
    }

    public boolean hasKeys() {
        return !keys.isEmpty();
    }

    public Collection<String> keySet() {
        return keys.keySet();
    }

    public void put(String key, String value) {
        keys.put(key, value);
    }

    public void remove(String key) {
        keys.remove(key);
    }

    public void removeAll() {
        keys.clear();
    }

    public void setKeys(Map<String, String> keys) {
        this.keys.clear();
        this.keys.putAll(keys);
    }

//    @SuppressWarnings("unchecked")
//    static public <T extends PrimitiveData>  List<T> getFilteredList(Collection<T> list, OsmPrimitiveType type) {
//        List<T> ret = new ArrayList<T>();
//        for(PrimitiveData p: list) {
//            if (type.getDataClass().isInstance(p)) {
//                ret.add((T)p);
//            }
//        }
//        return ret;
//    }

    protected void setKeysAsList(String... keys) {
        assert keys.length % 2 == 0;
        for (int i=0; i<keys.length/2; i++) {
            this.keys.put(keys[i * 2], keys[i * 2 + 1]);
        }
    }

    /**
     * PrimitiveId implementation. Returns the same value as getId()
     */
    public long getUniqueId() {
        return id;
    }

    /**
     * Returns a PrimitiveId object for this primitive
     *
     * @return the PrimitiveId for this primitive
     */
    public PrimitiveId getPrimitiveId() {
        return new SimplePrimitiveId(getUniqueId(), getType());
    }

    public boolean isNew() {
        return id <= 0;
    }

    public abstract OsmPrimitiveType getType();
}
