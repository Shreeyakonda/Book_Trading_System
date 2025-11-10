<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="auth-container">
                <div class="auth-card">
                    <h2>Register</h2>
                    <% if (error != null) { %>
                        <div class="alert alert-error"><%= error %></div>
                    <% } %>
                    <form action="<%= request.getContextPath() %>/register" method="post" class="auth-form" id="registerForm">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" required autofocus>
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="firstName">First Name</label>
                                <input type="text" id="firstName" name="firstName" required>
                            </div>
                            <div class="form-group">
                                <label for="lastName">Last Name</label>
                                <input type="text" id="lastName" name="lastName" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required minlength="6">
                            <small>Password must be at least 6 characters</small>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Confirm Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Register</button>
                    </form>
                    <p class="auth-link">Already have an account? <a href="<%= request.getContextPath() %>/login">Login here</a></p>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
    <script src="<%= request.getContextPath() %>/js/register.js"></script>
</body>
</html>

