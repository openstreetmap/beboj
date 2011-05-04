// License: GPL. For details, see LICENSE file
package javax.swing;

import java.awt.event.ActionEvent;

public interface Action {
    public void actionPerformed(ActionEvent e);
    public void putValue(String key, Object val);
}
