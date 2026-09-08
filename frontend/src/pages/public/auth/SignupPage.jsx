import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./SignupPage.css";

function SignupPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // ==============================
  // HANDLE INPUT
  // ==============================
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

    setError("");
    setSuccess("");
  };

  // ==============================
  // HANDLE SIGNUP
  // ==============================
  const handleSignup = (e) => {
    e.preventDefault();

    const email = formData.email.trim().toLowerCase();
    const password = formData.password;
    const confirmPassword = formData.confirmPassword;

    // Check empty fields
    if (!email || !password || !confirmPassword) {
      setError("Please fill in all fields.");
      return;
    }

    // Check password length
    if (password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    // Check passwords
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    // ==============================
    // GET REGISTERED USERS
    // ==============================

    let registeredUsers = [];

    try {
      const savedUsers =
        localStorage.getItem("registeredUsers");

      if (savedUsers) {
        registeredUsers = JSON.parse(savedUsers);
      }

      // Make sure it is an array
      if (!Array.isArray(registeredUsers)) {
        registeredUsers = [];
      }
    } catch (error) {
      registeredUsers = [];
    }

    // ==============================
    // CHECK EXISTING EMAIL
    // ==============================

    const existingUser = registeredUsers.find(
      (user) =>
        user.email &&
        user.email.trim().toLowerCase() === email
    );

    if (existingUser) {
      setError("This email is already registered.");
      return;
    }

    // ==============================
    // CREATE NEW USER
    // ==============================

    const newUser = {
      email: email,
      password: password,
    };

    const updatedUsers = [
      ...registeredUsers,
      newUser,
    ];

    // ==============================
    // SAVE USER
    // ==============================

    localStorage.setItem(
      "registeredUsers",
      JSON.stringify(updatedUsers)
    );

    console.log(
      "Account created:",
      newUser
    );

    // Success message
    setSuccess(
      "Account created successfully! Redirecting to login..."
    );

    // Go to login
    setTimeout(() => {
      navigate("/login");
    }, 1000);
  };

  return (
    <div className="signup-page">

      <div className="signup-card">

        <h1>Create Account</h1>

        <p className="signup-subtitle">
          Create your TalentAI account
        </p>

        <form onSubmit={handleSignup}>

          {/* EMAIL */}

          <div className="form-group">

            <label htmlFor="email">
              Email
            </label>

            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
            />

          </div>

          {/* PASSWORD */}

          <div className="form-group">

            <label htmlFor="password">
              Password
            </label>

            <input
              type="password"
              id="password"
              name="password"
              placeholder="Create a password"
              value={formData.password}
              onChange={handleChange}
            />

          </div>

          {/* CONFIRM PASSWORD */}

          <div className="form-group">

            <label htmlFor="confirmPassword">
              Confirm Password
            </label>

            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              placeholder="Confirm your password"
              value={formData.confirmPassword}
              onChange={handleChange}
            />

          </div>

          {/* ERROR */}

          {error && (
            <div className="signup-error">
              {error}
            </div>
          )}

          {/* SUCCESS */}

          {success && (
            <div className="signup-success">
              {success}
            </div>
          )}

          {/* CREATE ACCOUNT */}

          <button
            type="submit"
            className="signup-button"
          >
            Create Account
          </button>

        </form>

        <p className="login-text">
          Already have an account?{" "}

          <Link to="/login">
            Login
          </Link>
        </p>

      </div>

    </div>
  );
}

export default SignupPage;