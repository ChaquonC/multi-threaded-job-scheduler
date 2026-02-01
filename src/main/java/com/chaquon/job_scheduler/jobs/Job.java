package com.chaquon.job_scheduler.jobs;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Job {
    private final long id;
    private final JobType type;
    private final String[] args;
    private JobStatus status;
    private int attempts;
    private final int maxAttempts;
    private final Timestamp createdAt;
    private Timestamp updatedAt;
    private String lastError;
    private Object result;

    public Job(long id, JobType type, String[] args, JobStatus status, int attempts, int maxAttempts,
               Timestamp createdAt, Timestamp updatedAt, String lastError, Object result) {
        this.id = id;
        this.type = type;
        this.args = args;
        this.status = status;
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastError = lastError;
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public JobType getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }

    public JobStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getLastError() {
        return lastError;
    }

    public Object getResult() {
        return result;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("type", type.name());
        map.put("args", args);
        map.put("status", status.name());
        map.put("attempts", attempts);
        map.put("max_attempts", maxAttempts);
        map.put("created_at", createdAt);
        map.put("updated_at", updatedAt);
        map.put("last_error", lastError);
        map.put("result", result);

        return map;
    }
}
