// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.conflict;

/**
 * GWT ok
 */

public interface IConflictListener {
    public void onConflictsAdded(ConflictCollection conflicts);
    public void onConflictsRemoved(ConflictCollection conflicts);
}
