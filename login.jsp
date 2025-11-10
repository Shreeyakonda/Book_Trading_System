<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="auth-container">
                <div class="auth-card">
                    <h2>Login</h2>
                    <% if (error != null) { %>
                        <div class="alert alert-error"><%= error %></div>
                    <% } %>
                    <% if (success != null) { %>
                        <div class="alert alert-success"><%= success %></div>
                    <% } %>
                    <form action="<%= request.getContextPath() %>/login" method="post" class="auth-form">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" required autofocus>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Login</button>
                    </form>
                    <p class="auth-link">Don't have an account? <a href="<%= request.getContextPath() %>/register">Register here</a></p>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
</body>
</html>

