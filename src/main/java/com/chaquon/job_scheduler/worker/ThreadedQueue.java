package com.chaquon.job_scheduler.worker;

import com.chaquon.job_scheduler.jobs.Job;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadedQueue {
    private final ConcurrentLinkedQueue<Job> queue;
    public ThreadedQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void add(Job job) {
        this.queue.add(job);
    }

    public Optional<Job> getNext() {
        Job nextJob = this.queue.poll();
        if (nextJob == null) {
            System.out.println("No items in queue");
        }
        return Optional.ofNullable(nextJob);
    }
}
