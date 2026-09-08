import { Link } from "react-router-dom";
import {
  Search,
  MapPin,
  Briefcase,
  Users,
  Brain,
  FileCheck,
  CalendarCheck,
  ArrowRight,
} from "lucide-react";

function HomePage() {
  const featuredJobs = [
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
  ];

  return (
    <div className="home-page">

      {/* HERO SECTION */}
      <section className="hero-section">
        <div className="container hero-content">

          <div className="hero-text">
            <span className="hero-badge">
              AI-Powered Recruitment Platform
            </span>

            <h1>
              Find the right opportunity.
              <span> Build your future.</span>
            </h1>

            <p>
              Discover career opportunities that match your
              skills, experience, and ambitions.
            </p>

            <div className="hero-buttons">
              <Link to="/jobs" className="btn btn-primary">
                Explore Jobs
                <ArrowRight size={18} />
              </Link>

              <Link to="/about" className="btn btn-secondary">
                Learn More
              </Link>
            </div>
          </div>

          <div className="hero-card">
            <div className="hero-card-icon">
              <Users size={34} />
            </div>

            <h3>Smart Recruitment</h3>

            <p>
              AI-assisted screening helps organizations
              discover the right candidates faster.
            </p>

            <div className="hero-stat">
              <strong>1000+</strong>
              <span>Applications Processed</span>
            </div>

            <div className="hero-stat">
              <strong>500+</strong>
              <span>Candidates Screened</span>
            </div>
          </div>

        </div>
      </section>


      {/* JOB SEARCH */}
      <section className="job-search-section">
        <div className="container">

          <div className="job-search-box">

            <div className="search-field">
              <Search size={20} />

              <input
                type="text"
                placeholder="Job title or keyword"
              />
            </div>

            <div className="search-field">
              <MapPin size={20} />

              <input
                type="text"
                placeholder="Location"
              />
            </div>

            <Link to="/jobs" className="search-button">
              Search Jobs
            </Link>

          </div>

        </div>
      </section>


      {/* FEATURED JOBS */}
      <section className="section">
        <div className="container">

          <div className="section-header">
            <div>
              <span className="section-label">
                CAREER OPPORTUNITIES
              </span>

              <h2>Featured Jobs</h2>

              <p>
                Explore some of our latest opportunities.
              </p>
            </div>

            <Link to="/jobs" className="view-all-link">
              View All Jobs
              <ArrowRight size={18} />
            </Link>
          </div>

          <div className="jobs-grid">

            {featuredJobs.map((job) => (
              <div className="job-card" key={job.id}>

                <div className="job-card-top">

                  <div className="job-icon">
                    <Briefcase size={22} />
                  </div>

                  <span className="job-type">
                    {job.type}
                  </span>

                </div>

                <h3>{job.title}</h3>

                <p className="department">
                  {job.department}
                </p>

                <div className="job-info">

                  <span>
                    <MapPin size={16} />
                    {job.location}
                  </span>

                  <span>
                    <Briefcase size={16} />
                    {job.experience}
                  </span>

                </div>

                <div className="skills">

                  {job.skills.map((skill) => (
                    <span key={skill}>
                      {skill}
                    </span>
                  ))}

                </div>

                <Link
                  to={`/jobs/${job.id}`}
                  className="job-link"
                >
                  View Job
                  <ArrowRight size={17} />
                </Link>

              </div>
            ))}

          </div>

        </div>
      </section>


      {/* WHY CHOOSE US */}
      <section className="section why-section">
        <div className="container">

          <div className="center-heading">
            <span className="section-label">
              SMARTER RECRUITMENT
            </span>

            <h2>
              A Better Hiring Experience
            </h2>

            <p>
              Technology that makes recruitment simpler,
              faster and more transparent.
            </p>
          </div>

          <div className="features-grid">

            <div className="feature-card">
              <Brain size={30} />

              <h3>
                AI-Powered Screening
              </h3>

              <p>
                Candidates are evaluated based on
                relevant skills and job requirements.
              </p>
            </div>

            <div className="feature-card">
              <FileCheck size={30} />

              <h3>
                Smart Resume Analysis
              </h3>

              <p>
                Resume information can be processed
                into structured candidate profiles.
              </p>
            </div>

            <div className="feature-card">
              <CalendarCheck size={30} />

              <h3>
                Organized Interviews
              </h3>

              <p>
                Interview scheduling and evaluations
                stay organized throughout hiring.
              </p>
            </div>

          </div>

        </div>
      </section>


      {/* HOW IT WORKS */}
      <section className="section">
        <div className="container">

          <div className="center-heading">
            <span className="section-label">
              YOUR JOURNEY
            </span>

            <h2>
              How Our Recruitment Process Works
            </h2>
          </div>

          <div className="process-grid">

            <div className="process-item">
              <span>01</span>
              <h3>Find a Job</h3>
              <p>
                Browse available opportunities
                matching your interests.
              </p>
            </div>

            <div className="process-item">
              <span>02</span>
              <h3>Apply Online</h3>
              <p>
                Submit your information and upload
                your resume.
              </p>
            </div>

            <div className="process-item">
              <span>03</span>
              <h3>Get Screened</h3>
              <p>
                Your profile progresses through
                screening and shortlisting.
              </p>
            </div>

            <div className="process-item">
              <span>04</span>
              <h3>Interview</h3>
              <p>
                Attend scheduled interviews with
                the hiring team.
              </p>
            </div>

            <div className="process-item">
              <span>05</span>
              <h3>Join the Team</h3>
              <p>
                Successful candidates continue to
                offer and onboarding.
              </p>
            </div>

          </div>

        </div>
      </section>


      {/* CTA */}
      <section className="cta-section">

        <div className="container cta-content">

          <div>
            <h2>
              Ready to take the next step?
            </h2>

            <p>
              Explore available positions and find
              an opportunity that fits you.
            </p>
          </div>

          <Link to="/jobs" className="cta-button">
            Browse Open Positions
            <ArrowRight size={18} />
          </Link>

        </div>

      </section>

    </div>
  );
}

export default HomePage;