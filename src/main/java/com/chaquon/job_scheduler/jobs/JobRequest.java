package com.chaquon.job_scheduler.jobs;

public record JobRequest(
        String jobType,
        String[] args
) {}
