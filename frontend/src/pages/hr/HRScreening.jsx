import React, { useEffect, useState } from "react";
import HRSidebar from "./HRSidebar";
import "./HRDashboard.css";
import "./HRJobs.css";
import "./HRScreening.css";
import {
  fetchCandidates,
  getLocalJobs,
  ensureBackendJobId,
  matchCandidate,
  getSkillGap,
  rankCandidatesForJob,
} from "./hrApi";

function scoreColor(score) {
  if (score >= 80) return "#15803d";
  if (score >= 60) return "#b45309";
  return "#b91c1c";
}

function HRScreening() {
  const [candidates, setCandidates] = useState({});
  const jobs = getLocalJobs(); // jobs created via CreateJob.jsx (localStorage)

  // ---- Screen a single candidate ----
  const [selectedJobId, setSelectedJobId] = useState("");
  const [selectedCandidateId, setSelectedCandidateId] = useState("");
  const [screening, setScreening] = useState(false);
  const [screenError, setScreenError] = useState("");
  const [matchResult, setMatchResult] = useState(null);
  const [gapResult, setGapResult] = useState(null);

  // ---- Rank all candidates for a job ----
  const [rankJobId, setRankJobId] = useState("");
  const [ranking, setRanking] = useState(false);
  const [rankError, setRankError] = useState("");
  const [rankResult, setRankResult] = useState(null);

  useEffect(() => {
    fetchCandidates()
      .then(setCandidates)
      .catch(() => setCandidates({}));
  }, []);

  async function handleScreen(e) {
    e.preventDefault();
    if (!selectedJobId || !selectedCandidateId) {
      setScreenError("Select both a job and a candidate.");
      return;
    }
    setScreening(true);
    setScreenError("");
    setMatchResult(null);
    setGapResult(null);
    try {
      const localJob = jobs.find((j) => String(j.id) === selectedJobId);
      const backendJobId = await ensureBackendJobId(localJob);
      const [match, gap] = await Promise.all([
        matchCandidate(selectedCandidateId, backendJobId),
        getSkillGap(selectedCandidateId, backendJobId),
      ]);
      setMatchResult(match);
      setGapResult(gap);
    } catch (err) {
      setScreenError(err.message);
    } finally {
      setScreening(false);
    }
  }

  async function handleRank(e) {
    e.preventDefault();
    if (!rankJobId) {
      setRankError("Select a job to rank candidates against.");
      return;
    }
    setRanking(true);
    setRankError("");
    setRankResult(null);
    try {
      const localJob = jobs.find((j) => String(j.id) === rankJobId);
      const backendJobId = await ensureBackendJobId(localJob);
      const ranked = await rankCandidatesForJob(backendJobId);
      setRankResult(ranked);
    } catch (err) {
      setRankError(err.message);
    } finally {
      setRanking(false);
    }
  }

  return (
    <div className="hr-dashboard">
      <HRSidebar />

      <main className="hr-main">
        <header className="hr-header">
          <div>
            <h1>AI Screening</h1>
            <p>Run AI candidate matching, skill gap checks, and rankings for your open jobs.</p>
          </div>
        </header>

        {jobs.length === 0 && (
          <div className="jobs-panel">
            <div className="no-jobs">
              No jobs created yet. Go to <strong>Jobs → Create Job</strong> first, then come back here.
            </div>
          </div>
        )}

        {jobs.length > 0 && (
          <>
            {/* ---- Screen a candidate ---- */}
            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>Screen a Candidate</h2>
              </div>

              <form className="screening-form" onSubmit={handleScreen}>
                <select value={selectedJobId} onChange={(e) => setSelectedJobId(e.target.value)}>
                  <option value="">Select a job…</option>
                  {jobs.map((j) => (
                    <option key={j.id} value={j.id}>
                      {j.title} ({j.department})
                    </option>
                  ))}
                </select>

                <select value={selectedCandidateId} onChange={(e) => setSelectedCandidateId(e.target.value)}>
                  <option value="">Select a candidate…</option>
                  {Object.entries(candidates).map(([id, c]) => (
                    <option key={id} value={id}>
                      {c.filename} ({id})
                    </option>
                  ))}
                </select>

                <button type="submit" className="create-job-button" disabled={screening}>
                  {screening ? "Screening…" : "Run AI Screening"}
                </button>
              </form>

              {screenError && <p className="screening-error">{screenError}</p>}

              {matchResult && (
                <div className="screening-result">
                  <div className="match-score-block">
                    <div
                      className="match-score-circle"
                      style={{ background: scoreColor(matchResult.final_score) }}
                    >
                      {matchResult.final_score}%
                    </div>
                    <p className="match-explanation">{matchResult.explanation}</p>
                  </div>

                  <div className="sub-score-bars">
                    {Object.entries(matchResult.sub_scores).map(([key, val]) => (
                      <div className="sub-score-row" key={key}>
                        <div className="sub-score-label">
                          <span>{key}</span>
                          <span>{val}%</span>
                        </div>
                        <div className="sub-score-track">
                          <div
                            className="sub-score-fill"
                            style={{ width: `${val}%`, background: scoreColor(val) }}
                          />
                        </div>
                      </div>
                    ))}
                  </div>

                  {gapResult && (
                    <div className="skill-gap-block">
                      <p>
                        <strong>Matched skills:</strong>{" "}
                        {gapResult.matched_skills.length ? gapResult.matched_skills.join(", ") : "None"}
                      </p>
                      <p>
                        <strong>Missing required skills:</strong>{" "}
                        {gapResult.missing_critical.length ? gapResult.missing_critical.join(", ") : "None"}
                      </p>
                      <p className="skill-gap-verdict">{gapResult.verdict}</p>
                    </div>
                  )}
                </div>
              )}
            </section>

            {/* ---- Rank all candidates for a job ---- */}
            <section className="jobs-panel">
              <div className="jobs-panel-header">
                <h2>AI Rankings</h2>
              </div>

              <form className="screening-form" onSubmit={handleRank}>
                <select value={rankJobId} onChange={(e) => setRankJobId(e.target.value)}>
                  <option value="">Select a job to rank candidates for…</option>
                  {jobs.map((j) => (
                    <option key={j.id} value={j.id}>
                      {j.title} ({j.department})
                    </option>
                  ))}
                </select>

                <button type="submit" className="create-job-button" disabled={ranking}>
                  {ranking ? "Ranking…" : "Rank Candidates"}
                </button>
              </form>

              {rankError && <p className="screening-error">{rankError}</p>}

              {rankResult && (
                <div className="job-table" style={{ marginTop: 16 }}>
                  <div className="job-row job-heading ranking-row-grid">
                    <span>Rank</span>
                    <span>Candidate ID</span>
                    <span>Match Score</span>
                    <span>Missing Required Skills</span>
                  </div>
                  {rankResult.map((r) => (
                    <div className="job-row ranking-row-grid" key={r.candidate_id}>
                      <span className={`rank-badge ${r.rank === 1 ? "rank-first" : ""}`}>#{r.rank}</span>
                      <span className="candidate-id-cell">{r.candidate_id}</span>
                      <span style={{ color: scoreColor(r.match_score), fontWeight: 700 }}>
                        {r.match_score}%
                      </span>
                      <span>
                        {r.skill_gap.missing_critical.length
                          ? r.skill_gap.missing_critical.join(", ")
                          : "None"}
                      </span>
                    </div>
                  ))}
                </div>
              )}
            </section>
          </>
        )}
      </main>
    </div>
  );
}

export default HRScreening;
