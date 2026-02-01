package com.chaquon.job_scheduler.worker.tasks;

public class Ping {
    public static boolean run() {
        System.out.println("pinging on thread " + Thread.currentThread().getName());
        return true;
    }
}
