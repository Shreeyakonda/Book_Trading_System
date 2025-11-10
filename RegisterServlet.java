package com.booktrading.servlet;

import com.booktrading.dao.UserDAO;
import com.booktrading.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        // Validation
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Check if username or email already exists
        if (userDAO.usernameExists(username)) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Create user
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(userDAO.hashPassword(password));
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());

        if (userDAO.createUser(user)) {
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}

