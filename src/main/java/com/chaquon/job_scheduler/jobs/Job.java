package com.chaquon.job_scheduler.jobs;

import java.sql.Timestamp;

public record Job(
        long id,
        String type,
        String[] args,
        JobStatus status,
        int attempts,
        int maxAttempts,
        Timestamp nextRunAt,
        Timestamp createdAt,
        Timestamp updatedAt,
        String lastError
) {}
