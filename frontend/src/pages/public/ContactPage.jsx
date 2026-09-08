import { useState } from "react";
import {
  Mail,
  Phone,
  MapPin,
  Send,
  MessageSquare,
} from "lucide-react";

function ContactPage() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });

  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    console.log("Contact message:", formData);

    setSubmitted(true);

    setFormData({
      name: "",
      email: "",
      subject: "",
      message: "",
    });
  };

  return (
    <div className="contact-page">

      <section className="contact-hero">
        <div className="container contact-hero-content">

          <span className="section-label">
            CONTACT US
          </span>

          <h1>
            We'd Love to Hear From You
          </h1>

          <p>
            Have a question about jobs, applications or our
            recruitment platform? Send us a message and our team
            will get back to you.
          </p>

        </div>
      </section>


      <section className="contact-content-section">
        <div className="container contact-layout">

          <div className="contact-info">

            <span className="section-label">
              GET IN TOUCH
            </span>

            <h2>
              Let's Start a Conversation
            </h2>

            <p className="contact-description">
              Whether you're a candidate looking for an
              opportunity or an organization using our recruitment
              platform, we're here to help.
            </p>


            <div className="contact-info-list">

              <div className="contact-info-item">

                <div className="contact-info-icon">
                  <Mail size={22} />
                </div>

                <div>
                  <span>Email</span>

                  <strong>
                    support@talentai.com
                  </strong>
                </div>

              </div>


              <div className="contact-info-item">

                <div className="contact-info-icon">
                  <Phone size={22} />
                </div>

                <div>
                  <span>Phone</span>

                  <strong>
                    +91 98765 43210
                  </strong>
                </div>

              </div>


              <div className="contact-info-item">

                <div className="contact-info-icon">
                  <MapPin size={22} />
                </div>

                <div>
                  <span>Office</span>

                  <strong>
                    Hyderabad, Telangana
                  </strong>
                </div>

              </div>

            </div>


            <div className="contact-help-card">

              <MessageSquare size={25} />

              <div>
                <h3>
                  Need Recruitment Support?
                </h3>

                <p>
                  Our team can assist with applications,
                  recruitment workflows and platform-related
                  questions.
                </p>
              </div>

            </div>

          </div>


          <div className="contact-form-card">

            <h2>
              Send Us a Message
            </h2>

            <p>
              Fill in the form below and we'll contact you.
            </p>


            {submitted && (
              <div className="contact-success-message">
                Your message has been submitted successfully.
              </div>
            )}


            <form onSubmit={handleSubmit}>

              <div className="contact-form-group">

                <label htmlFor="name">
                  Full Name *
                </label>

                <input
                  id="name"
                  type="text"
                  name="name"
                  placeholder="Enter your full name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="contact-form-group">

                <label htmlFor="email">
                  Email Address *
                </label>

                <input
                  id="email"
                  type="email"
                  name="email"
                  placeholder="example@email.com"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="contact-form-group">

                <label htmlFor="subject">
                  Subject *
                </label>

                <input
                  id="subject"
                  type="text"
                  name="subject"
                  placeholder="How can we help you?"
                  value={formData.subject}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="contact-form-group">

                <label htmlFor="message">
                  Message *
                </label>

                <textarea
                  id="message"
                  name="message"
                  rows="6"
                  placeholder="Write your message..."
                  value={formData.message}
                  onChange={handleChange}
                  required
                />

              </div>


              <button
                type="submit"
                className="contact-submit-button"
              >
                Send Message
                <Send size={18} />
              </button>

            </form>

          </div>

        </div>
      </section>

    </div>
  );
}

export default ContactPage;