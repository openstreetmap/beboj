// License: GPL. For details, see LICENSE file
package java.beans;

import java.util.concurrent.CopyOnWriteArrayList;

public class PropertyChangeSupport {

    private final Object src;

    public PropertyChangeSupport(Object src) {
        this.src = src;
    }

    private CopyOnWriteArrayList<PropertyChangeListener> listeners = new CopyOnWriteArrayList<PropertyChangeListener>();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    public void firePropertyChange(PropertyChangeEvent e) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(e);
        }
    }

    public void firePropertyChange(String prop, Object oldVal, Object newVal) {
        firePropertyChange(new PropertyChangeEvent(src, prop, oldVal, newVal));
    }

}
