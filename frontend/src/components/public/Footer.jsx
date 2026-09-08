import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer className="footer">

      <div className="container footer-grid">

        <div>
          <h3>TalentAI</h3>

          <p>
            Intelligent recruitment and
            employee management platform.
          </p>
        </div>

        <div>
          <h4>Quick Links</h4>

          <Link to="/">Home</Link>
          <Link to="/jobs">Jobs</Link>
          <Link to="/about">About</Link>
          <Link to="/contact">Contact</Link>
        </div>

        <div>
          <h4>For Candidates</h4>

          <Link to="/jobs">
            Browse Jobs
          </Link>

          <Link to="/login">
            Login
          </Link>
        </div>

      </div>

      <div className="footer-bottom">
        © 2026 TalentAI. All rights reserved.
      </div>

    </footer>
  );
}

export default Footer;