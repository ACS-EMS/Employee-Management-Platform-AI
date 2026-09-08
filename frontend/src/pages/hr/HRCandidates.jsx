import React, { useEffect, useState } from "react";
import HRSidebar from "./HRSidebar";
import "./HRDashboard.css";
import "./HRJobs.css";
import "./HRCandidates.css";
import { fetchCandidates, uploadResume } from "./hrApi";

function HRCandidates() {
  const [candidates, setCandidates] = useState({});
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState("");
  const [search, setSearch] = useState("");

  const [file, setFile] = useState(null);
  const [source, setSource] = useState("");
  const [uploadMsg, setUploadMsg] = useState("");
  const [uploading, setUploading] = useState(false);

  async function loadCandidates() {
    setLoading(true);
    setLoadError("");
    try {
      const data = await fetchCandidates();
      setCandidates(data);
    } catch (err) {
      setLoadError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCandidates();
  }, []);

  async function handleUpload(e) {
    e.preventDefault();
    if (!file) {
      setUploadMsg("Choose a resume file first.");
      return;
    }
    setUploading(true);
    setUploadMsg("Uploading and parsing…");
    try {
      const result = await uploadResume(file, source);
      setUploadMsg(
        `Uploaded "${file.name}" — candidate ID ${result.candidate_id}. Skills detected: ${
          result.profile.skills.length ? result.profile.skills.join(", ") : "none"
        }.`
      );
      setFile(null);
      setSource("");
      loadCandidates();
    } catch (err) {
      setUploadMsg(err.message);
    } finally {
      setUploading(false);
    }
  }

  const candidateEntries = Object.entries(candidates).filter(([id, c]) => {
    const q = search.toLowerCase();
    if (!q) return true;
    return (
      c.filename.toLowerCase().includes(q) ||
      id.toLowerCase().includes(q) ||
      c.skills.some((s) => s.toLowerCase().includes(q))
    );
  });

  return (
    <div className="hr-dashboard">
      <HRSidebar />

      <main className="hr-main">
        <header className="hr-header">
          <div>
            <h1>Candidates</h1>
            <p>Upload resumes and view every candidate parsed by the AI engine.</p>
          </div>
        </header>

        <section className="jobs-panel candidate-upload-panel">
          <div className="jobs-panel-header">
            <h2>Upload a resume</h2>
          </div>

          <form className="candidate-upload-form" onSubmit={handleUpload}>
            <div className="candidate-upload-row">
              <input
                type="file"
                accept=".pdf,.docx,.txt"
                onChange={(e) => setFile(e.target.files[0] || null)}
              />
              <input
                type="text"
                placeholder="Source (LinkedIn, Referral, Careers page...)"
                value={source}
                onChange={(e) => setSource(e.target.value)}
              />
              <button type="submit" className="create-job-button" disabled={uploading}>
                {uploading ? "Uploading…" : "Upload & Parse"}
              </button>
            </div>
            {uploadMsg && <p className="candidate-upload-msg">{uploadMsg}</p>}
          </form>
        </section>

        <section className="jobs-panel">
          <div className="jobs-panel-header">
            <h2>All Candidates</h2>
            <input
              type="text"
              placeholder="Search by name, ID, or skill..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          {loading ? (
            <div className="no-jobs">Loading candidates…</div>
          ) : loadError ? (
            <div className="no-jobs">
              {loadError} — is the AI backend running at http://127.0.0.1:8000 ?
            </div>
          ) : candidateEntries.length === 0 ? (
            <div className="no-jobs">No candidates uploaded yet.</div>
          ) : (
            <div className="job-table">
              <div className="job-row job-heading candidate-row-grid">
                <span>Candidate ID</span>
                <span>File</span>
                <span>Source</span>
                <span>Skills</span>
              </div>

              {candidateEntries.map(([id, c]) => (
                <div className="job-row candidate-row-grid" key={id}>
                  <span className="candidate-id-cell">{id}</span>
                  <span>{c.filename}</span>
                  <span>{c.source || "Not specified"}</span>
                  <span className="candidate-skills-cell">
                    {c.skills.length ? (
                      c.skills.map((s) => (
                        <span className="skill-chip" key={s}>
                          {s}
                        </span>
                      ))
                    ) : (
                      <span className="no-skills-text">No skills detected</span>
                    )}
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

export default HRCandidates;
