import React, { useEffect, useState } from "react";
import HRSidebar from "./HRSidebar";
import "./HRDashboard.css";
import "./HRJobs.css";
import "./HRAnalytics.css";
import { getAnalytics } from "./hrApi";

function HRAnalytics() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  function load() {
    setLoading(true);
    setError("");
    getAnalytics()
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <div className="hr-dashboard">
      <HRSidebar />

      <main className="hr-main">
        <header className="hr-header">
          <div>
            <h1>Analytics</h1>
            <p>AI-generated insights from every candidate screening run so far.</p>
          </div>
          <button className="create-job-button" onClick={load}>
            Refresh
          </button>
        </header>

        {loading ? (
          <div className="jobs-panel">
            <div className="no-jobs">Loading analytics…</div>
          </div>
        ) : error ? (
          <div className="jobs-panel">
            <div className="no-jobs">{error} — is the AI backend running?</div>
          </div>
        ) : data.total_applications === 0 ? (
          <div className="jobs-panel">
            <div className="no-jobs">{data.message}</div>
          </div>
        ) : (
          <>
            <section className="jobs-stats">
              <div className="job-stat-card">
                <span>Total Screenings</span>
                <strong>{data.total_applications}</strong>
              </div>
              <div className="job-stat-card">
                <span>Jobs Screened</span>
                <strong>{data.jobs_screened}</strong>
              </div>
              <div className="job-stat-card">
                <span>Candidates Screened</span>
                <strong>{data.candidates_screened}</strong>
              </div>
            </section>

            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Applications per Job</h2>
              </div>
              <div className="job-table">
                <div className="job-row job-heading analytics-row-grid-2">
                  <span>Job</span>
                  <span>Screenings</span>
                </div>
                {data.applications_per_job.map((j) => (
                  <div className="job-row analytics-row-grid-2" key={j.job_id}>
                    <span>{j.job_title}</span>
                    <span>{j.applications}</span>
                  </div>
                ))}
              </div>
            </section>

            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Rejection Rate per Job</h2>
              </div>
              <div className="job-table">
                <div className="job-row job-heading analytics-row-grid-3">
                  <span>Job</span>
                  <span>Rejection Rate</span>
                  <span>Total Screened</span>
                </div>
                {data.rejection_rate_per_job.map((j) => (
                  <div className="job-row analytics-row-grid-3" key={j.job_id}>
                    <span>{j.job_title}</span>
                    <span>{j.rejection_rate_pct}%</span>
                    <span>{j.total_screened}</span>
                  </div>
                ))}
              </div>
            </section>

            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Hardest-to-Find Skills</h2>
              </div>
              <div className="skill-chip-row">
                {data.hardest_to_find_skills.length ? (
                  data.hardest_to_find_skills.map((s) => (
                    <span className="analytics-skill-chip" key={s.skill}>
                      {s.skill} · missing {s.times_missing}×
                    </span>
                  ))
                ) : (
                  <span className="no-jobs">No skill gaps recorded yet.</span>
                )}
              </div>
            </section>

            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Source Performance</h2>
              </div>
              <div className="job-table">
                <div className="job-row job-heading analytics-row-grid-3">
                  <span>Source</span>
                  <span>Avg. Match Score</span>
                  <span>Candidates</span>
                </div>
                {data.source_performance.map((s) => (
                  <div className="job-row analytics-row-grid-3" key={s.source}>
                    <span>{s.source}</span>
                    <span>{s.average_match_score}%</span>
                    <span>{s.candidates_screened}</span>
                  </div>
                ))}
              </div>
            </section>

            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Time to Hire</h2>
              </div>
              <p className="analytics-note">{data.time_to_hire.reason}</p>
            </section>
          </>
        )}
      </main>
    </div>
  );
}

export default HRAnalytics;
