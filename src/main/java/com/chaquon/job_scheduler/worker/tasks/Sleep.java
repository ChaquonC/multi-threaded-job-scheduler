package com.chaquon.job_scheduler.worker.tasks;

public class Sleep {
    public static boolean run() {
        System.out.println("Sleeping thread " + Thread.currentThread().getName() + " for 10 seconds");
        return true;
    }
}
