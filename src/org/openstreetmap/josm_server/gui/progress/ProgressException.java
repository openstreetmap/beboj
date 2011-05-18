// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm_server.gui.progress;

/**
 * GWT ok
 */

public class ProgressException extends RuntimeException {

    public ProgressException(String message, Object... args) {
        super(String.format(message, args));
    }

}
