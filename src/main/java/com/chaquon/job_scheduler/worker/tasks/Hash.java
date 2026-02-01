package com.chaquon.job_scheduler.worker.tasks;

public class Hash {
    public static boolean run(String[] args) {
        try {
            for (String arg : args) {
                System.out.println("Hashing " + arg + " in thread " + Thread.currentThread().getName());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error occured: " + e);
            return false;
        }
    }
}
