import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./CreateJob.css";

function CreateJob() {
  const navigate = useNavigate();

  const [jobData, setJobData] = useState({
    title: "",
    department: "",
    location: "",
    jobType: "",
    experience: "",
    salary: "",
    description: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setJobData({
      ...jobData,
      [e.target.name]: e.target.value,
    });

    setError("");
  };

  const handleCreateJob = (e) => {
    e.preventDefault();

    if (
      !jobData.title ||
      !jobData.department ||
      !jobData.location ||
      !jobData.jobType ||
      !jobData.description
    ) {
      setError("Please fill in all required fields.");
      return;
    }

    const existingJobs =
  JSON.parse(localStorage.getItem("hrJobs")) || [];

const newJob = {
  id: Date.now(),
  title: jobData.title,
  department: jobData.department,
  location: jobData.location,
  jobType: jobData.jobType,
  experience: jobData.experience,
  salary: jobData.salary,
  description: jobData.description,
  applications: 0,
  status: "Active",
};

localStorage.setItem(
  "hrJobs",
  JSON.stringify([...existingJobs, newJob])
);
    alert("Job created successfully!");

    navigate("/hr/jobs");
  };

  return (
    <div className="hr-create-job-page">

      <main className="create-job-main">

        <div className="create-job-header">
          <div>
            <h1>Create Job</h1>
            <p>Create a new job opening for your organization.</p>
          </div>

          <button
            className="back-button"
            onClick={() => navigate("/hr/jobs")}
          >
            ← Back to Jobs
          </button>
        </div>

        <div className="create-job-card">

          <form onSubmit={handleCreateJob}>

            <div className="form-section">
              <h2>Job Information</h2>
              <p>Enter the basic information about this position.</p>
            </div>

            <div className="form-grid">

              <div className="job-form-group">
                <label>
                  Job Title *
                </label>

                <input
                  type="text"
                  name="title"
                  placeholder="e.g. Software Engineer"
                  value={jobData.title}
                  onChange={handleChange}
                />
              </div>

              <div className="job-form-group">
                <label>
                  Department *
                </label>

                <input
                  type="text"
                  name="department"
                  placeholder="e.g. Engineering"
                  value={jobData.department}
                  onChange={handleChange}
                />
              </div>

              <div className="job-form-group">
                <label>
                  Location *
                </label>

                <input
                  type="text"
                  name="location"
                  placeholder="e.g. Hyderabad"
                  value={jobData.location}
                  onChange={handleChange}
                />
              </div>

              <div className="job-form-group">
                <label>
                  Job Type *
                </label>

                <select
                  name="jobType"
                  value={jobData.jobType}
                  onChange={handleChange}
                >
                  <option value="">Select job type</option>
                  <option value="Full Time">Full Time</option>
                  <option value="Part Time">Part Time</option>
                  <option value="Contract">Contract</option>
                  <option value="Internship">Internship</option>
                </select>
              </div>

              <div className="job-form-group">
                <label>
                  Experience
                </label>

                <input
                  type="text"
                  name="experience"
                  placeholder="e.g. 2-4 years"
                  value={jobData.experience}
                  onChange={handleChange}
                />
              </div>

              <div className="job-form-group">
                <label>
                  Salary
                </label>

                <input
                  type="text"
                  name="salary"
                  placeholder="e.g. ₹6 - ₹10 LPA"
                  value={jobData.salary}
                  onChange={handleChange}
                />
              </div>

            </div>

            <div className="job-form-group">
              <label>
                Job Description *
              </label>

              <textarea
                name="description"
                rows="6"
                placeholder="Describe the job responsibilities, requirements, and qualifications..."
                value={jobData.description}
                onChange={handleChange}
              />
            </div>

            {error && (
              <div className="create-job-error">
                {error}
              </div>
            )}

            <div className="create-job-actions">

              <button
                type="button"
                className="cancel-button"
                onClick={() => navigate("/hr/jobs")}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="submit-job-button"
              >
                Create Job →
              </button>

            </div>

          </form>

        </div>

      </main>

    </div>
  );
}

export default CreateJob;