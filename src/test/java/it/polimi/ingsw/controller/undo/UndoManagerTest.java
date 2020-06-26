package it.polimi.ingsw.controller.undo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UndoManagerTest {

    @Test
    void run() {

        Object lock = new Object();

        UndoManager undoManager = new UndoManager(lock);

        assertFalse(undoManager.isActive());

        final boolean b1[] = {false};
        final boolean[] b2 = {false};

        undoManager.run();

        assertTrue(undoManager.isActive());

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            assertTrue(undoManager.isActive());

            b2[0] = true;
            undoManager.setActive(false);

        }).start();


        Object mt = new Object();

        synchronized (mt){
            try {
                mt.wait(4500);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertTrue(undoManager.isActive());

        b1[0] = true;

        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);
        assertFalse(undoManager.isActive());

    }

    @Test
    void undoReceived() {
        Object lock = new Object();

        UndoManager undoManager = new UndoManager(lock);

        final boolean b1[] = {false};
        final boolean[] b2 = {false};

        assertFalse(undoManager.isActive());

        undoManager.run();

        assertTrue(undoManager.isActive());

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            assertTrue(undoManager.isActive());

            b2[0] = true;

        }).start();


        Object mt = new Object();

        synchronized (mt){
            try {
                mt.wait(4500);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertTrue(undoManager.isActive());

        b1[0] = true;

        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);
        assertTrue(undoManager.isActive());

        undoManager.undoReceived();

        assertFalse(undoManager.isActive());


        assertFalse(undoManager.isActive());

        undoManager.run();

        assertTrue(undoManager.isActive());

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            assertFalse(undoManager.isActive());

            b2[0] = true;

        }).start();

        b1[0] = b2[0] = false;


        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertTrue(undoManager.isActive());
        b1[0] = true;
        undoManager.undoReceived();



        synchronized (mt){
            try {
                mt.wait(500);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);
        assertFalse(undoManager.isActive());

        undoManager.undoReceived();

        assertFalse(undoManager.isActive());
    }

    @Test
    void stop() {

        Object lock = new Object();

        UndoManager undoManager = new UndoManager(lock);

        assertFalse(undoManager.isActive());

        final boolean b1[] = {false};
        final boolean[] b2 = {false};

        undoManager.run();

        assertTrue(undoManager.isActive());

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait(10000);
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            assertTrue(undoManager.isActive());

            b2[0] = true;
            undoManager.setActive(false);

        }).start();


        Object mt = new Object();

        synchronized (mt){
            try {
                mt.wait(4500);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertTrue(undoManager.isActive());

        b1[0] = true;

        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);
        assertFalse(undoManager.isActive());



        b1[0] = b2[0] = false;

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait(10000);
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            b2[0] = true;
        }).start();

        undoManager.run();

        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertTrue(undoManager.isActive());

        undoManager.stop();

        assertFalse(undoManager.isActive());

        b1[0] = true;
        b2[0] = true;

        synchronized (mt){
            try {
                mt.wait(7000);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);

        b2[0] = false;
    }
}