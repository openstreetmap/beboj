// License: GPL. For details, see LICENSE file.
package java.util.concurrent.locks;

/**
 * classpath emulation
 */
public interface ReadWriteLock {
    public Lock readLock();
    public Lock writeLock();
}
