package com.chaquon.job_scheduler.api;

import com.chaquon.job_scheduler.jobs.*;
import com.chaquon.job_scheduler.worker.ThreadedQueue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class API {
    private final JobService jobService;
    private final ThreadedQueue queue;

    public API(JobService jobService, ThreadedQueue queue) {
        this.jobService = jobService;
        this.queue = queue;
    }

    @PostMapping("/jobs")
    public ResponseEntity<Job> postJob(@RequestBody JobRequest req) throws SQLException {
        Job createdJob = jobService.createJob(req);
        queue.add(createdJob);
        return ResponseEntity.status(201).body(createdJob);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJob(@PathVariable long id) throws SQLException {
        Job acquiredJob = jobService.getJobById(id);
        return ResponseEntity.status(200).body(acquiredJob);
    }

    @GetMapping("/jobs/status/{type}")
    public ResponseEntity<ArrayList<Job>> getJobsByStatus(
            @PathVariable String statusType,
            @RequestParam(name = "limit", defaultValue = "20") int limit
    ) throws SQLException {
        JobStatus type = JobStatus.valueOf(statusType);
        ArrayList<Job> jobs = jobService.getJobsByStatus(type, limit);
        return ResponseEntity.status(200).body(jobs);
    }

//    @PutMapping("/jobs/{id}/cancel")
//    public ResponseEntity<Job> cancelJob(@PathVariable long id) {
//        // cancels the job specified
//    }

    @GetMapping("/health")
    public void healthCheck() {
        System.out.println("DB user = " + System.getenv("DB_USER"));
    }
}
