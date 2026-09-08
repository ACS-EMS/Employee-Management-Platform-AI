/**
 * hrApi.js
 * -------------------------------------------------------------------
 * Shared helper functions connecting the HR pages to our FastAPI AI
 * backend (api.py). Doesn't touch CreateJob.jsx, HRDashboard.jsx,
 * HRJobs.jsx, or HRSidebar.jsx — this is a new file, imported by the
 * new pages only.
 *
 * IMPORTANT BRIDGE NOTE:
 * CreateJob.jsx saves jobs to localStorage under the key "hrJobs" —
 * it never calls our backend, so our backend has no idea those jobs
 * exist. Since /match, /rank, and /skill-gap all require a real
 * job_id from our database, ensureBackendJobId() below solves this
 * WITHOUT modifying CreateJob.jsx: the first time a locally-created
 * job is used for AI screening, this quietly registers it with our
 * backend via /set-job and remembers the mapping (in a separate
 * localStorage key, "hrJobBackendIds") so it only happens once per job.
 */

const API_BASE = "http://127.0.0.1:8000";

// ---- Candidates ("Manage Candidates") ----

export async function fetchCandidates() {
  const res = await fetch(`${API_BASE}/candidates`);
  if (!res.ok) throw new Error("Failed to load candidates from the AI backend.");
  return res.json();
}

export async function uploadResume(file, source) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("source", source || "Not specified");

  const res = await fetch(`${API_BASE}/upload-resume`, { method: "POST", body: formData });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.detail || "Resume upload failed.");
  }
  return res.json();
}

// ---- Jobs bridge (see file header note above) ----

export function getLocalJobs() {
  return JSON.parse(localStorage.getItem("hrJobs")) || [];
}

export async function ensureBackendJobId(localJob) {
  const mapKey = "hrJobBackendIds";
  const map = JSON.parse(localStorage.getItem(mapKey)) || {};

  if (map[localJob.id]) {
    return map[localJob.id]; // already registered with the backend
  }

  const description = [
    localJob.description,
    localJob.experience ? `Experience required: ${localJob.experience}.` : "",
  ].filter(Boolean).join(" ");

  const res = await fetch(`${API_BASE}/set-job`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      job_title: localJob.title,
      job_description: description,
      location: localJob.location || "",
      preferred_skills: [],
    }),
  });
  if (!res.ok) throw new Error("Could not register this job with the AI backend.");

  const data = await res.json();
  map[localJob.id] = data.job_id;
  localStorage.setItem(mapKey, JSON.stringify(map));
  return data.job_id;
}

// ---- AI Screening ----

export async function matchCandidate(candidateId, jobId) {
  const res = await fetch(`${API_BASE}/match/${candidateId}/${jobId}`, { method: "POST" });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.detail || "Match request failed.");
  }
  return res.json();
}

export async function getSkillGap(candidateId, jobId) {
  const res = await fetch(`${API_BASE}/skill-gap/${candidateId}/${jobId}`, { method: "POST" });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.detail || "Skill gap request failed.");
  }
  return res.json();
}

// ---- AI Rankings ----

export async function rankCandidatesForJob(jobId) {
  const res = await fetch(`${API_BASE}/rank/${jobId}`);
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.detail || "Ranking request failed.");
  }
  return res.json();
}

// ---- HR Analytics ----

export async function getAnalytics() {
  const res = await fetch(`${API_BASE}/analytics`);
  if (!res.ok) throw new Error("Failed to load analytics from the AI backend.");
  return res.json();
}
