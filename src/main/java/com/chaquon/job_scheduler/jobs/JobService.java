package com.chaquon.job_scheduler.jobs;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Service
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
                JobType.valueOf(rs.getString("type")),
                args,
                JobStatus.valueOf(rs.getString("status")),
                rs.getInt("attempts"),
                rs.getInt("max_attempts"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                rs.getString("last_error"),
                rs.getObject("result")
        );
    }

    public Job createJob(JobRequest jobRequest) throws SQLException {
        String query = """
                INSERT INTO jobs (
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    created_at,
                    updated_at,
                    last_error,
                    result
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    0,
                    3,
                    now(),
                    now(),
                    NULL,
                    NULL
                )
                RETURNING id
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Array sqlArgs = conn.createArrayOf("text", jobRequest.args());

            ps.setString(1, jobRequest.jobType());
            ps.setArray(2, sqlArgs);
            ps.setString(3, JobStatus.PENDING.name());

            sqlArgs.free();

            ResultSet rs = ps.executeQuery();
            rs.next();
            long id = rs.getLong("id");
            return getJobById(id);
        }
    }

    public Job getJobById(long jobId) throws SQLException {
        String query = """
                SELECT
                    id,
                    type,
                    args,
                    status,
                    attempts,
                    max_attempts,
                    created_at,
                    updated_at,
                    last_error,
                    result
                FROM jobs
                WHERE id = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, jobId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return initJob(rs);
            }
        }
    }

    public ArrayList<Job> getJobsByType(JobType jobType, int numJobs)
            throws SQLException {

        ArrayList<Job> jobs = new ArrayList<>();

        String query = """
                SELECT
                    id,
                    type,
                    args,
                    status,
                    attempts,
                    created_at,
                    updated_at,
                    last_error,
                    result
                FROM jobs
                WHERE type = ?
                LIMIT ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, jobType.name());
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

    public ArrayList<Job> getJobsByStatus(JobStatus status, int numJobs)
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
                    last_error,
                    result
                FROM jobs
                WHERE status = ?
                LIMIT ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, status.name());
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

    public void updateJob(Job job) throws SQLException {
        String query = """
                UPDATE jobs
                SET
                    status = ?
                    updated_at = ?
                    last_error = ?
                    result = ?
                WHERE id = ?
                """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, job.getStatus().name());
            ps.setTimestamp(2, job.getUpdatedAt());
            ps.setString(3, job.getLastError());
            ps.setObject(4, job.getResult());
            ps.setLong(5, job.getId());
        }
    }
}
