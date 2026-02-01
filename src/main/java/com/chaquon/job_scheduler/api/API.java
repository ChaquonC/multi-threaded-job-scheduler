package com.chaquon.job_scheduler.api;

import com.chaquon.job_scheduler.jobs.Job;
import com.chaquon.job_scheduler.jobs.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class API {
    private final AtomicLong counter = new AtomicLong();
    @PostMapping("/jobs")
    public ResponseEntity<Job> postJob(@RequestBody JobRequest req) {
        // add job to the queue and return the job
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable long id) {
        // get job metadata from SQL
    }

    @GetMapping("/jobs/status/{type}")
    public ResponseEntity<ArrayList<Job>> getJobsByStatus(@PathVariable String statusType, @PathVariable int numJobs){
        // grab numJobs jons of statusType or most recent 20 if no numJobs specified
    }

    @PutMapping("/jobs/{id}/cancel")
    public ResponseEntity<Job> cancelJob(@PathVariable long id) {
        // cancels the job specified
    }
}
