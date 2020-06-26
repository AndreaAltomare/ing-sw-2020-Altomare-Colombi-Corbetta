package it.polimi.ingsw.controller.undo;

import org.junit.jupiter.api.Test;

import java.util.Timer;

import static org.junit.jupiter.api.Assertions.*;

class TimeoutCounterTest {

    @Test
    void run() {

        Object lock = new Object();

        final boolean b1[] = {false};
        final boolean[] b2 = {false};
        boolean b3 = false;

        Timer timer = new Timer();

        /* 2- Start the Timer */
        TimeoutCounter task = new TimeoutCounter(3, lock);
        timer.schedule(task, 0, 1000);

        new Thread(() -> {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException ignore) {
                }
            }
            assertTrue(b1[0]);
            assertFalse(b2[0]);
            assertFalse(b3);

            b2[0] = true;

        }).start();


        Object mt = new Object();

        synchronized (mt){
            try {
                mt.wait(2000);
            } catch (InterruptedException ignore) {
            }
        }

        assertFalse(b1[0]);
        assertFalse(b2[0]);
        assertFalse(b3);

        b1[0] = true;

        synchronized (mt){
            try {
                mt.wait(1500);
            } catch (InterruptedException ignore) {
            }
        }

        assertTrue(b1[0]);
        assertTrue(b2[0]);
        assertFalse(b3);

    }
}