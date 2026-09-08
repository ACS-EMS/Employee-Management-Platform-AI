import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import {
  Search,
  MapPin,
  Briefcase,
  Building2,
  ArrowRight,
} from "lucide-react";

function JobsPage() {
  const jobs = [
    {
      id: 1,
      title: "Frontend Developer",
      department: "Engineering",
      location: "Hyderabad",
      type: "Full Time",
      experience: "1 - 3 Years",
      skills: ["React", "JavaScript", "CSS"],
    },
    {
      id: 2,
      title: "Java Developer",
      department: "Engineering",
      location: "Bangalore",
      type: "Full Time",
      experience: "2 - 4 Years",
      skills: ["Java", "Spring Boot", "PostgreSQL"],
    },
    {
      id: 3,
      title: "HR Executive",
      department: "Human Resources",
      location: "Hyderabad",
      type: "Full Time",
      experience: "1 - 3 Years",
      skills: ["Recruitment", "Communication", "HRMS"],
    },
    {
      id: 4,
      title: "Backend Developer",
      department: "Engineering",
      location: "Chennai",
      type: "Full Time",
      experience: "2 - 5 Years",
      skills: ["Java", "Spring Boot", "REST API"],
    },
    {
      id: 5,
      title: "UI/UX Designer",
      department: "Design",
      location: "Hyderabad",
      type: "Full Time",
      experience: "1 - 3 Years",
      skills: ["Figma", "UI Design", "UX Research"],
    },
    {
      id: 6,
      title: "Data Analyst",
      department: "Analytics",
      location: "Bangalore",
      type: "Full Time",
      experience: "1 - 4 Years",
      skills: ["SQL", "Power BI", "Excel"],
    },
  ];

  const [searchTerm, setSearchTerm] = useState("");
  const [department, setDepartment] = useState("");
  const [location, setLocation] = useState("");
  const [jobType, setJobType] = useState("");

  const filteredJobs = useMemo(() => {
    return jobs.filter((job) => {
      const matchesSearch =
        job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        job.skills.some((skill) =>
          skill.toLowerCase().includes(searchTerm.toLowerCase())
        );

      const matchesDepartment =
        !department || job.department === department;

      const matchesLocation =
        !location || job.location === location;

      const matchesType =
        !jobType || job.type === jobType;

      return (
        matchesSearch &&
        matchesDepartment &&
        matchesLocation &&
        matchesType
      );
    });
  }, [searchTerm, department, location, jobType]);

  const clearFilters = () => {
    setSearchTerm("");
    setDepartment("");
    setLocation("");
    setJobType("");
  };

  return (
    <div className="jobs-page">
      <section className="jobs-hero">
        <div className="container">
          <span className="section-label">CAREERS</span>

          <h1>Find Your Next Opportunity</h1>

          <p>
            Explore open positions and discover roles that match
            your skills, experience, and career goals.
          </p>
        </div>
      </section>

      <section className="jobs-content-section">
        <div className="container">
          <div className="jobs-filter-box">

            <div className="jobs-search-input">
              <Search size={19} />

              <input
                type="text"
                placeholder="Search by job title or skill"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>

            <select
              value={department}
              onChange={(e) => setDepartment(e.target.value)}
            >
              <option value="">All Departments</option>
              <option value="Engineering">Engineering</option>
              <option value="Human Resources">Human Resources</option>
              <option value="Design">Design</option>
              <option value="Analytics">Analytics</option>
            </select>

            <select
              value={location}
              onChange={(e) => setLocation(e.target.value)}
            >
              <option value="">All Locations</option>
              <option value="Hyderabad">Hyderabad</option>
              <option value="Bangalore">Bangalore</option>
              <option value="Chennai">Chennai</option>
            </select>

            <select
              value={jobType}
              onChange={(e) => setJobType(e.target.value)}
            >
              <option value="">All Job Types</option>
              <option value="Full Time">Full Time</option>
              <option value="Full Time">Part Time</option>
              <option value="Full Time">Internship</option>
            </select>

          </div>

          <div className="jobs-results-header">
            <div>
              <h2>Open Positions</h2>
              <p>{filteredJobs.length} jobs found</p>
            </div>

            <button
              type="button"
              className="clear-filters-button"
              onClick={clearFilters}
            >
              Clear Filters
            </button>
          </div>

          {filteredJobs.length > 0 ? (
            <div className="jobs-list-grid">
              {filteredJobs.map((job) => (
                <article
                  className="jobs-list-card"
                  key={job.id}
                >
                  <div className="jobs-list-card-header">

                    <div className="jobs-company-icon">
                      <Building2 size={24} />
                    </div>

                    <span className="job-status-badge">
                      Hiring
                    </span>

                  </div>

                  <h3>{job.title}</h3>

                  <p className="jobs-department">
                    {job.department}
                  </p>

                  <div className="jobs-meta">

                    <span>
                      <MapPin size={16} />
                      {job.location}
                    </span>

                    <span>
                      <Briefcase size={16} />
                      {job.experience}
                    </span>

                    <span>
                      <Briefcase size={16} />
                      {job.type}
                    </span>

                  </div>

                  <div className="jobs-skills">
                    {job.skills.map((skill) => (
                      <span key={skill}>
                        {skill}
                      </span>
                    ))}
                  </div>

                  <div className="jobs-card-footer">

                    <Link
                      to={`/jobs/${job.id}`}
                      className="view-job-button"
                    >
                      View Job
                      <ArrowRight size={17} />
                    </Link>

                  </div>
                </article>
              ))}
            </div>
          ) : (
            <div className="no-jobs-state">
              <Briefcase size={42} />

              <h3>No jobs found</h3>

              <p>
                Try changing your search or filters.
              </p>

              <button
                type="button"
                onClick={clearFilters}
              >
                Clear Filters
              </button>
            </div>
          )}
        </div>
      </section>
    </div>
  );
}

export default JobsPage;