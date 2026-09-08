import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import HRSidebar from "./HRSidebar";
import "./HRJobs.css";

function HRJobs() {
  const navigate = useNavigate();

  const [jobs, setJobs] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  // Load jobs from localStorage
  useEffect(() => {
    const savedJobs =
      JSON.parse(localStorage.getItem("hrJobs")) || [];

    setJobs(savedJobs);
  }, []);

  // Close an active job
  const handleCloseJob = (jobId) => {
    const updatedJobs = jobs.map((job) =>
      job.id === jobId
        ? { ...job, status: "Closed" }
        : job
    );

    setJobs(updatedJobs);

    localStorage.setItem(
      "hrJobs",
      JSON.stringify(updatedJobs)
    );
  };

  // Activate a closed job
  const handleActivateJob = (jobId) => {
    const updatedJobs = jobs.map((job) =>
      job.id === jobId
        ? { ...job, status: "Active" }
        : job
    );

    setJobs(updatedJobs);

    localStorage.setItem(
      "hrJobs",
      JSON.stringify(updatedJobs)
    );
  };

  // Search jobs
  const filteredJobs = jobs.filter((job) =>
    job.title
      ?.toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

  // Count active jobs
  const activeJobs = jobs.filter(
    (job) => job.status === "Active"
  ).length;

  return (
    <div className="hr-dashboard">

      {/* Sidebar */}
      <HRSidebar />

      {/* Main Content */}
      <main className="hr-main">

        {/* ================= HEADER ================= */}

        <header className="hr-header">

          <div>
            <h1>Jobs</h1>

            <p>
              Manage your organization's job openings.
            </p>
          </div>

          <button
            className="create-job-button"
            onClick={() => navigate("/hr/jobs/create")}
          >
            + Create Job
          </button>

        </header>


        {/* ================= STATS ================= */}

        <section className="jobs-stats">

          <div className="job-stat-card">
            <span>Open Positions</span>
            <strong>{activeJobs}</strong>
          </div>

          <div className="job-stat-card">
            <span>Active Jobs</span>
            <strong>{activeJobs}</strong>
          </div>

          <div className="job-stat-card">
            <span>Total Applications</span>
            <strong>
              {jobs.reduce(
                (total, job) =>
                  total + (Number(job.applications) || 0),
                0
              )}
            </strong>
          </div>

        </section>


        {/* ================= JOBS PANEL ================= */}

        <section className="jobs-panel">

          <div className="jobs-panel-header">

            <h2>Job Openings</h2>

            <input
              type="text"
              placeholder="Search jobs..."
              value={searchTerm}
              onChange={(e) =>
                setSearchTerm(e.target.value)
              }
            />

          </div>


          {/* ================= JOB TABLE ================= */}

          <div className="job-table">

            {/* Table Header */}

            <div className="job-row job-heading">

              <span>Job Title</span>

              <span>Department</span>

              <span>Applications</span>

              <span>Status</span>

              <span>Actions</span>

            </div>


            {/* ================= JOB LIST ================= */}

            {filteredJobs.length === 0 ? (

              <div className="no-jobs">
                No jobs have been created yet.
              </div>

            ) : (

              filteredJobs.map((job) => (

                <div
                  className="job-row"
                  key={job.id}
                >

                  {/* Job Title */}

                  <span>
                    {job.title}
                  </span>


                  {/* Department */}

                  <span>
                    {job.department}
                  </span>


                  {/* Applications */}

                  <span>
                    {job.applications || 0}
                  </span>


                  {/* Status */}

                  <span
                    className={`job-status ${
                      job.status?.toLowerCase()
                    }`}
                  >
                    {job.status}
                  </span>


                  {/* ================= ACTION ================= */}

                  <span className="job-actions">

                    {job.status === "Active" ? (

                      <button
                        type="button"
                        className="close-job-button"
                        onClick={() =>
                          handleCloseJob(job.id)
                        }
                        title="Close Position"
                      >

                        <svg
                          width="18"
                          height="18"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        >
                          <path d="M18 6L6 18" />
                          <path d="M6 6L18 18" />
                        </svg>

                        <span>
                          Close
                        </span>

                      </button>

                    ) : (

                      <button
                        type="button"
                        className="activate-job-button"
                        onClick={() =>
                          handleActivateJob(job.id)
                        }
                        title="Activate Position"
                      >

                        <svg
                          width="18"
                          height="18"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        >
                          <path d="M5 12l5 5L20 7" />
                        </svg>

                        <span>
                          Activate
                        </span>

                      </button>

                    )}

                  </span>

                </div>

              ))

            )}

          </div>

        </section>

      </main>

    </div>
  );
}

export default HRJobs;