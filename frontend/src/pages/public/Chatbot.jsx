import { useState } from "react";
import {
  Bot,
  Send,
  User,
  MessageCircle,
  Sparkles,
} from "lucide-react";

function Chatbot() {
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: "bot",
      text:
        "Hi! I'm TalentAI Assistant. I can help you with job openings, applications, recruitment process and general platform questions.",
    },
  ]);

  const [input, setInput] = useState("");

  const quickQuestions = [
    "Show me available jobs",
    "How do I apply for a job?",
    "What happens after I apply?",
    "How does AI screening work?",
  ];

  const getBotReply = (message) => {
    const text = message.toLowerCase();

    if (
      text.includes("job") ||
      text.includes("opening")
    ) {
      return "You can view all currently available positions from the Jobs page. Select a job to view its details and apply.";
    }

    if (
      text.includes("apply") ||
      text.includes("application")
    ) {
      return "To apply, open a job from the Jobs page, click Apply Now, complete the application form and upload your resume.";
    }

    if (
      text.includes("resume") ||
      text.includes("screening") ||
      text.includes("ai")
    ) {
      return "The platform is designed to use AI-assisted resume screening to analyze skills, education and experience against job requirements.";
    }

    if (
      text.includes("interview")
    ) {
      return "Shortlisted candidates may move through HR, technical, managerial or final interview stages depending on the role.";
    }

    if (
      text.includes("contact") ||
      text.includes("support")
    ) {
      return "You can reach the recruitment team using the Contact page available in the navigation bar.";
    }

    return "I can help with jobs, applications, resume screening, interviews and recruitment-related questions. Try asking me about one of those topics.";
  };

  const sendMessage = (messageText = input) => {
    const trimmedMessage = messageText.trim();

    if (!trimmedMessage) {
      return;
    }

    const userMessage = {
      id: Date.now(),
      sender: "user",
      text: trimmedMessage,
    };

    setMessages((prev) => [
      ...prev,
      userMessage,
    ]);

    setInput("");

    setTimeout(() => {
      const botMessage = {
        id: Date.now() + 1,
        sender: "bot",
        text: getBotReply(trimmedMessage),
      };

      setMessages((prev) => [
        ...prev,
        botMessage,
      ]);
    }, 500);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    sendMessage();
  };

  return (
    <div className="chatbot-page">

      <section className="chatbot-hero">
        <div className="container chatbot-hero-content">

          <div className="chatbot-hero-icon">
            <Bot size={30} />
          </div>

          <span className="section-label">
            AI ASSISTANT
          </span>

          <h1>
            How Can We Help You?
          </h1>

          <p>
            Ask questions about jobs, applications,
            recruitment stages and the TalentAI platform.
          </p>

        </div>
      </section>


      <section className="chatbot-section">
        <div className="container">

          <div className="chatbot-layout">

            <aside className="chatbot-sidebar">

              <div className="chatbot-info-card">

                <Sparkles size={24} />

                <h3>
                  TalentAI Assistant
                </h3>

                <p>
                  Get quick answers about recruitment
                  and job applications.
                </p>

              </div>


              <div className="quick-questions">

                <h4>
                  Suggested Questions
                </h4>

                {quickQuestions.map((question) => (
                  <button
                    key={question}
                    type="button"
                    onClick={() =>
                      sendMessage(question)
                    }
                  >
                    <MessageCircle size={16} />

                    {question}
                  </button>
                ))}

              </div>

            </aside>


            <div className="chatbot-window">

              <div className="chatbot-header">

                <div className="chatbot-avatar">
                  <Bot size={22} />
                </div>

                <div>
                  <h3>
                    TalentAI Assistant
                  </h3>

                  <span>
                    Online
                  </span>
                </div>

              </div>


              <div className="chatbot-messages">

                {messages.map((message) => (
                  <div
                    key={message.id}
                    className={
                      message.sender === "user"
                        ? "chat-message user-message"
                        : "chat-message bot-message"
                    }
                  >

                    <div className="chat-message-avatar">

                      {message.sender === "user" ? (
                        <User size={18} />
                      ) : (
                        <Bot size={18} />
                      )}

                    </div>


                    <div className="chat-message-content">

                      <span>
                        {message.sender === "user"
                          ? "You"
                          : "TalentAI"}
                      </span>

                      <p>
                        {message.text}
                      </p>

                    </div>

                  </div>
                ))}

              </div>


              <form
                className="chatbot-input-area"
                onSubmit={handleSubmit}
              >

                <input
                  type="text"
                  placeholder="Ask TalentAI a question..."
                  value={input}
                  onChange={(e) =>
                    setInput(e.target.value)
                  }
                />

                <button
                  type="submit"
                  aria-label="Send message"
                >
                  <Send size={19} />
                </button>

              </form>

            </div>

          </div>

        </div>
      </section>

    </div>
  );
}

export default Chatbot;