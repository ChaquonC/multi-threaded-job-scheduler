package com.chaquon.job_scheduler.api;

public record APIError(
        String code,
        String message
) {}

