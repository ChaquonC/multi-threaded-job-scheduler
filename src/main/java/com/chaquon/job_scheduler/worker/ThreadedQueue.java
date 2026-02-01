package com.chaquon.job_scheduler.worker;

import com.chaquon.job_scheduler.jobs.Job;
import com.chaquon.job_scheduler.jobs.JobService;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ThreadedQueue {
    private final LinkedBlockingQueue<Job> queue;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final int maxWorkers = 4;
    private Thread worker;
    private final JobService jobService;

    public ThreadedQueue(JobService jobService) {
        this.queue = new LinkedBlockingQueue<>(100);
        this.jobService = jobService;
    }

    public void add(Job job) {
        boolean result = queue.offer(job);
        if (!result) {
            //insert log statement later
        }
    }

    public void start() {
        if (!running.compareAndSet(false, true)) return;

        for (int i = 0; i < maxWorkers; i++) {
            String threadName = String.format("job-worker-%d", i);
            worker = new Thread(this::run, threadName);
            worker.setDaemon(true);
            worker.start();
        }
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) return;

        for (int i = 0; i < maxWorkers; i++) {
            if (worker != null) {
                worker.interrupt();
            }
        }
    }

    private void run() {
        while(running.get()) {
            try {
                Job job = queue.take();
                JobHandler.handleJob(job);
                jobService.updateJob(job);
            } catch (InterruptedException e) {
                if (!running.get()) break;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                // handle exception and clean thread without shutting down others
            }
        }
    }
}
