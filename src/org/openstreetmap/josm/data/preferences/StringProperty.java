// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.data.preferences;

import org.openstreetmap.josm.Main;

/**
 * GWT ok
 */

public class StringProperty extends AbstractProperty {

    protected final String defaultValue;

    public StringProperty(String key, String defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public String get() {
        return Main.pref.get(getKey(), getDefaultValue());
    }

    public boolean put(String value) {
        return Main.pref.put(getKey(), value);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
