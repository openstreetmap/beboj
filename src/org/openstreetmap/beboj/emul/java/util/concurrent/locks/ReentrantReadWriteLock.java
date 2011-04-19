// License: GPL. For details, see LICENSE file.
package java.util.concurrent.locks;

/**
 * this is just an empty shell, so JOSM core code does not need to be fixed
 */
public class ReentrantReadWriteLock implements ReadWriteLock {

    public static class ReadLock implements Lock {
        public void lock() {

        }
        public void unlock() {

        }
    }
    public static class WriteLock implements Lock {
        public void lock() {

        }
        public void unlock() {

        }
    }

    Lock rl = new ReadLock();
    Lock wl = new WriteLock();

    public Lock readLock() {
        return rl;
    }

    public Lock writeLock() {
        return wl;
    }
}
