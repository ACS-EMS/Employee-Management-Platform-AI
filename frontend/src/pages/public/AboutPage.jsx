import {
  Brain,
  BriefcaseBusiness,
  Users,
  BarChart3,
  ShieldCheck,
  Workflow,
  CheckCircle2,
} from "lucide-react";

function AboutPage() {
  const features = [
    {
      icon: Brain,
      title: "AI-Powered Screening",
      description:
        "Analyze candidate profiles, extract skills and help recruitment teams identify suitable candidates faster.",
    },
    {
      icon: BriefcaseBusiness,
      title: "Smart Recruitment",
      description:
        "Manage jobs, applications, screening, interviews, offers and onboarding through one connected workflow.",
    },
    {
      icon: Users,
      title: "Employee Management",
      description:
        "Manage employee records, attendance, leave, performance and other employee activities in one platform.",
    },
    {
      icon: BarChart3,
      title: "Recruitment Analytics",
      description:
        "Track recruitment activity and gain useful insights through dashboards, reports and hiring metrics.",
    },
    {
      icon: ShieldCheck,
      title: "Role-Based Access",
      description:
        "Provide secure access to HR teams, recruiters, hiring managers, interviewers and employees based on their roles.",
    },
    {
      icon: Workflow,
      title: "Complete Hiring Workflow",
      description:
        "Connect every stage from job creation and candidate application to selection, onboarding and employee management.",
    },
  ];

  return (
    <div className="about-page">

      <section className="about-hero">
        <div className="container about-hero-content">

          <span className="section-label">
            ABOUT TALENTAI
          </span>

          <h1>
            Smarter Recruitment.
            <br />
            Better Workforce Management.
          </h1>

          <p>
            TalentAI is an AI-powered recruitment and employee
            management platform designed to simplify hiring,
            improve candidate screening and manage the complete
            employee lifecycle.
          </p>

        </div>
      </section>


      <section className="about-platform-section">
        <div className="container about-platform-grid">

          <div className="about-platform-content">

            <span className="section-label">
              OUR PLATFORM
            </span>

            <h2>
              One Platform for Recruitment and Employee Management
            </h2>

            <p>
              Traditional recruitment often involves multiple
              tools, manual resume screening and disconnected
              processes. TalentAI brings recruitment and employee
              management together into one centralized platform.
            </p>

            <p>
              From creating a job opening to selecting candidates,
              conducting interviews, onboarding employees and
              managing their workplace activities, the platform
              supports the complete workflow.
            </p>

          </div>


          <div className="about-workflow-card">

            <h3>Recruitment Journey</h3>

            <div className="about-workflow-list">

              <div>
                <span>01</span>
                <p>Job Creation</p>
              </div>

              <div>
                <span>02</span>
                <p>Candidate Application</p>
              </div>

              <div>
                <span>03</span>
                <p>AI Resume Screening</p>
              </div>

              <div>
                <span>04</span>
                <p>Candidate Ranking</p>
              </div>

              <div>
                <span>05</span>
                <p>Interview & Evaluation</p>
              </div>

              <div>
                <span>06</span>
                <p>Selection & Offer</p>
              </div>

              <div>
                <span>07</span>
                <p>Onboarding</p>
              </div>

              <div>
                <span>08</span>
                <p>Employee Management</p>
              </div>

            </div>

          </div>

        </div>
      </section>


      <section className="about-features-section">
        <div className="container">

          <div className="section-heading-center">
            <span className="section-label">
              WHAT WE PROVIDE
            </span>

            <h2>
              Built for Modern Recruitment Teams
            </h2>

            <p>
              Powerful tools to simplify hiring and employee
              management.
            </p>
          </div>


          <div className="about-features-grid">

            {features.map((feature) => {
              const Icon = feature.icon;

              return (
                <article
                  className="about-feature-card"
                  key={feature.title}
                >
                  <div className="about-feature-icon">
                    <Icon size={25} />
                  </div>

                  <h3>{feature.title}</h3>

                  <p>
                    {feature.description}
                  </p>
                </article>
              );
            })}

          </div>
        </div>
      </section>


      <section className="about-ai-section">
        <div className="container about-ai-grid">

          <div className="about-ai-icon">
            <Brain size={70} />
          </div>


          <div className="about-ai-content">

            <span className="section-label">
              AI IN RECRUITMENT
            </span>

            <h2>
              Make Candidate Screening More Efficient
            </h2>

            <p>
              TalentAI supports AI-assisted recruitment by helping
              analyze candidate information and compare profiles
              against job requirements.
            </p>

            <div className="about-ai-points">

              <div>
                <CheckCircle2 size={19} />
                Resume information extraction
              </div>

              <div>
                <CheckCircle2 size={19} />
                Skills and experience matching
              </div>

              <div>
                <CheckCircle2 size={19} />
                Candidate ranking
              </div>

              <div>
                <CheckCircle2 size={19} />
                Explainable candidate scores
              </div>

            </div>

          </div>

        </div>
      </section>


      <section className="about-cta-section">
        <div className="container about-cta-content">

          <h2>
            Connecting Great Talent with Great Opportunities
          </h2>

          <p>
            A smarter recruitment experience for candidates,
            recruiters and organizations.
          </p>

        </div>
      </section>

    </div>
  );
}

export default AboutPage;