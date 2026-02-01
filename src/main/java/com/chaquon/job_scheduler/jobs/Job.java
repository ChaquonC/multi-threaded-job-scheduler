package com.chaquon.job_scheduler.jobs;

import java.sql.Timestamp;
import java.util.ArrayList;

public record Job(
        long id,
        String type,
        ArrayList<String> args,
        JobType status, int attempts,
        int maxAttempts,
        Timestamp nextRunAt,
        Timestamp createdAt,
        Timestamp updatedAt,
        Exception lastError
) {}
