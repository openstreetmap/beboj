// License: GPL. For details, see LICENSE file
package java.util;

/**
 * stub, just return some arbitrary values
 */
public class Locale {

    private static Locale def = new Locale();

    public final static Locale US = null;

    public static Locale getDefault() {
        return def;
    }

    public String getLanguage() {
        return "en";
    }

    public String getCountry() {
        return "GB";
    }
}
