package com.chaquon.job_scheduler.worker;


import com.chaquon.job_scheduler.jobs.Job;
import com.chaquon.job_scheduler.jobs.JobStatus;
import com.chaquon.job_scheduler.jobs.JobType;
import com.chaquon.job_scheduler.worker.tasks.*;

public class JobHandler {
    public static void handleJob(Job job) {
        boolean success;
        switch (job.getType()) {
            case JobType.SLEEP:
                success = Sleep.run();
                break;
            case JobType.HASH:
                success = Hash.run(job.getArgs());
                break;
            case JobType.PING:
                success = Ping.run();
                break;
            default:
                return;
        }
        if (success) {
            job.setStatus(JobStatus.COMPLETED);
        } else {
            job.setStatus(JobStatus.FAILED);
        }
    }
}
