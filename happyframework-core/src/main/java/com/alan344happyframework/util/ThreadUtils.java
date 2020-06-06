package com.alan344happyframework.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    /**
     * 线程等待时间
     */
    private static final int AWAIT_TIME = 20000;

    public static void shutdownAndAwaitTermination(ExecutorService es) {
        es.shutdown(); // Disable new tasks from being submitted  不再接受新的提交
        try {
            // Wait a while for existing tasks to terminate
            if (!es.awaitTermination(AWAIT_TIME, TimeUnit.MILLISECONDS)) {
                es.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!es.awaitTermination(AWAIT_TIME, TimeUnit.MILLISECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            es.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
