package com.chaquon.job_scheduler.jobs;

import java.util.ArrayList;

public record JobRequest(
        String jobType,
        ArrayList<String> args
) {}
