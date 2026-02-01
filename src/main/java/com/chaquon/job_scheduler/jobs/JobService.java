package com.chaquon.job_scheduler.jobs;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.ArrayList;

@Repository
public class JobService {
    private final DataSource dataSource;

    public JobService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Job initJob(ResultSet rs) throws SQLException {
        Array argsArray = rs.getArray("args");
        String[] args = argsArray != null
                ? (String[]) argsArray.getArray()
                : new String[0];

        return new Job(
                rs.getLong("id"),
                rs.getString("type"),
                args,
                JobStatus.valueOf(rs.getString("status")),
                rs.getInt("attempts"),
                rs.getInt("max_attempts"),
                rs.getTimestamp("next_run_at") != null
                        ? rs.getTimestamp("next_run_at")
                        : null,
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("last_error")
        );
    }

    public long createJob(JobRequest jobRequest) throws SQLException {
        String query = """
                INSERT INTO jobs (
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    next_run_at,
                    created_at,
                    updated_at,
                    last_error
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    0,
                    3,
                    NULL,
                    now(),
                    now(),
                    NULL
                )
                RETURNING id
                """;
        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Array sqlArgs = conn.createArrayOf("text", jobRequest.args());

            ps.setString(1, jobRequest.jobType());
            ps.setArray(2, sqlArgs);
            ps.setString(3, "PENDING");

            sqlArgs.free();

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong("id");
        }
    }

    public Optional<Job> getJobById(long jobId) throws SQLException {
        String query = """
                SELECT
                    id,
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    next_run_at,
                    created_at,
                    updated_at,
                    last_error
                FROM jobs
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                Job job = initJob(rs);

                return Optional.of(job);
            }
        }
    }

    public ArrayList<Job> getJobsByType(String jobType, int numJobs)
            throws SQLException {

        ArrayList<Job> jobs = new ArrayList<>();

        String query = """
                SELECT
                    id,
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    next_run_at,
                    created_at,
                    updated_at,
                    last_error
                FROM jobs
                WHERE type = ?
                LIMIT ?;
                """;

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, jobType);
            ps.setInt(2, numJobs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = initJob(rs);
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    public ArrayList<Job> getJobsByStatus(String status, int numJobs)
            throws SQLException {

        ArrayList<Job> jobs = new ArrayList<>();

        String query = """
                SELECT
                    id,
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    next_run_at,
                    created_at,
                    updated_at,
                    last_error
                FROM jobs
                WHERE status = ?
                LIMIT ?;
                """;

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, numJobs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = initJob(rs);
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }
}
