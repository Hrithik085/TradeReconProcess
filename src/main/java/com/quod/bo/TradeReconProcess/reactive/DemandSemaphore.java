package com.quod.bo.TradeReconProcess.reactive;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

public class DemandSemaphore {

    private final NonfairSync sync;

    static class NonfairSync extends AbstractQueuedLongSynchronizer {
        NonfairSync(long permits) {
            setState(permits);
        }

        @Override
        protected final long tryAcquireShared(long acquires) {
            for (;;) {
                long available = getState();
                long remaining = available - acquires;
                if (remaining < 0 || compareAndSetState(available, remaining)) {
                    return remaining;
                }
            }
        }

        @Override
        protected final boolean tryReleaseShared(long releases) {
            for (;;) {
                long current = getState();
                long next = current + releases;
                if (next < current) { // overflow
                    next = Long.MAX_VALUE;
                }
                if (compareAndSetState(current, next)) {
                    return true;
                }
            }
        }
    }

    public DemandSemaphore(long permits) {
        sync = new NonfairSync(permits);
    }

    public void acquire() throws InterruptedException {
        sync.acquireSharedInterruptibly(1L);
    }

    public void release() {
        sync.releaseShared(1);
    }

    public void acquire(long permits) throws InterruptedException {
        if (permits < 0L) {
            throw new IllegalArgumentException();
        }
        sync.acquireSharedInterruptibly(permits);
    }

    public void release(long permits) {
        if (permits < 0) {
            throw new IllegalArgumentException();
        }
        sync.releaseShared(permits);
    }
}
