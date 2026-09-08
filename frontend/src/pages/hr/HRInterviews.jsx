import React, { useEffect, useState } from "react";
import HRSidebar from "./HRSidebar";
import "./HRDashboard.css";
import "./HRJobs.css";
import "./HRInterviews.css";
import { fetchCandidates, getLocalJobs } from "./hrApi";

/**
 * HRInterviews.jsx
 * -------------------------------------------------------------------
 * NOTE: This is NOT an AI feature. It's plain scheduling/record-keeping
 * (Module 22 — Interview Management in the report), built the same way
 * CreateJob.jsx already handles jobs: saved to localStorage under the
 * key "hrInterviews", no backend database involved.
 *
 * This is a reasonable stopgap for a student project, same as
 * CreateJob's approach — but worth flagging honestly: a real production
 * version needs this backed by an actual database table (so interviews
 * persist properly, survive across browsers, and can be queried by the
 * Interviewer role too), not localStorage on one HR person's browser.
 */

const INTERVIEW_TYPES = ["HR", "TECHNICAL", "MANAGERIAL", "FINAL"];

function HRInterviews() {
  const [candidates, setCandidates] = useState({});
  const jobs = getLocalJobs();
  const [interviews, setInterviews] = useState([]);

  const [form, setForm] = useState({
    candidateId: "",
    jobId: "",
    interviewType: "",
    interviewer: "",
    date: "",
    time: "",
    meetingLink: "",
  });
  const [error, setError] = useState("");

  useEffect(() => {
    fetchCandidates()
      .then(setCandidates)
      .catch(() => setCandidates({}));

    const saved = JSON.parse(localStorage.getItem("hrInterviews")) || [];
    setInterviews(saved);
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  }

  function handleSchedule(e) {
    e.preventDefault();
    if (!form.candidateId || !form.jobId || !form.interviewType || !form.date || !form.time) {
      setError("Please fill in candidate, job, interview type, date, and time.");
      return;
    }

    const candidate = candidates[form.candidateId];
    const job = jobs.find((j) => String(j.id) === form.jobId);

    const newInterview = {
      id: Date.now(),
      candidateId: form.candidateId,
      candidateLabel: candidate ? candidate.filename : form.candidateId,
      jobTitle: job ? job.title : "Unknown job",
      interviewType: form.interviewType,
      interviewer: form.interviewer || "Not assigned",
      date: form.date,
      time: form.time,
      meetingLink: form.meetingLink,
      status: "Scheduled",
    };

    const updated = [...interviews, newInterview];
    setInterviews(updated);
    localStorage.setItem("hrInterviews", JSON.stringify(updated));

    setForm({
      candidateId: "",
      jobId: "",
      interviewType: "",
      interviewer: "",
      date: "",
      time: "",
      meetingLink: "",
    });
  }

  function updateStatus(id, status) {
    const updated = interviews.map((iv) => (iv.id === id ? { ...iv, status } : iv));
    setInterviews(updated);
    localStorage.setItem("hrInterviews", JSON.stringify(updated));
  }

  return (
    <div className="hr-dashboard">
      <HRSidebar />

      <main className="hr-main">
        <header className="hr-header">
          <div>
            <h1>Interviews</h1>
            <p>Schedule and track candidate interviews.</p>
          </div>
        </header>

        <section className="jobs-panel">
          <div className="jobs-panel-header">
            <h2>Schedule an Interview</h2>
          </div>

          <form className="interview-form" onSubmit={handleSchedule}>
            <div className="interview-form-grid">
              <select name="candidateId" value={form.candidateId} onChange={handleChange}>
                <option value="">Select candidate…</option>
                {Object.entries(candidates).map(([id, c]) => (
                  <option key={id} value={id}>
                    {c.filename} ({id})
                  </option>
                ))}
              </select>

              <select name="jobId" value={form.jobId} onChange={handleChange}>
                <option value="">Select job…</option>
                {jobs.map((j) => (
                  <option key={j.id} value={j.id}>
                    {j.title}
                  </option>
                ))}
              </select>

              <select name="interviewType" value={form.interviewType} onChange={handleChange}>
                <option value="">Interview type…</option>
                {INTERVIEW_TYPES.map((t) => (
                  <option key={t} value={t}>
                    {t}
                  </option>
                ))}
              </select>

              <input
                type="text"
                name="interviewer"
                placeholder="Interviewer name"
                value={form.interviewer}
                onChange={handleChange}
              />

              <input type="date" name="date" value={form.date} onChange={handleChange} />
              <input type="time" name="time" value={form.time} onChange={handleChange} />

              <input
                type="text"
                name="meetingLink"
                placeholder="Meeting link (optional)"
                value={form.meetingLink}
                onChange={handleChange}
                className="interview-form-wide"
              />
            </div>

            {error && <p className="interview-error">{error}</p>}

            <button type="submit" className="create-job-button">
              Schedule Interview
            </button>
          </form>
        </section>

        <section className="jobs-panel">
          <div className="jobs-panel-header">
            <h2>Scheduled Interviews</h2>
          </div>

          {interviews.length === 0 ? (
            <div className="no-jobs">No interviews scheduled yet.</div>
          ) : (
            <div className="job-table">
              <div className="job-row job-heading interview-row-grid">
                <span>Candidate</span>
                <span>Job</span>
                <span>Type</span>
                <span>Interviewer</span>
                <span>When</span>
                <span>Status</span>
              </div>
              {interviews.map((iv) => (
                <div className="job-row interview-row-grid" key={iv.id}>
                  <span>{iv.candidateLabel}</span>
                  <span>{iv.jobTitle}</span>
                  <span>{iv.interviewType}</span>
                  <span>{iv.interviewer}</span>
                  <span>
                    {iv.date} {iv.time}
                  </span>
                  <span>
                    <select
                      className="status-select"
                      value={iv.status}
                      onChange={(e) => updateStatus(iv.id, e.target.value)}
                    >
                      <option value="Scheduled">Scheduled</option>
                      <option value="Completed">Completed</option>
                      <option value="Cancelled">Cancelled</option>
                      <option value="Rescheduled">Rescheduled</option>
                    </select>
                  </span>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default HRInterviews;
