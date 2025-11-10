<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.booktrading.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    String contextPath = request.getContextPath();
%>
<header class="header">
    <div class="container">
        <nav class="navbar">
            <div class="nav-brand">
                <a href="<%= contextPath %>/">📚 Book Trading System</a>
            </div>
            <ul class="nav-menu">
                <li><a href="<%= contextPath %>/">Home</a></li>
                <li><a href="<%= contextPath %>/browse">Browse Books</a></li>
                <% if (user != null) { %>
                    <li><a href="<%= contextPath %>/dashboard">Dashboard</a></li>
                    <li><a href="<%= contextPath %>/book/add">Add Book</a></li>
                    <li class="user-menu">
                        <span class="username"><%= user.getFullName() %></span>
                        <a href="<%= contextPath %>/logout" class="btn-logout">Logout</a>
                    </li>
                <% } else { %>
                    <li><a href="<%= contextPath %>/login" class="btn-primary">Login</a></li>
                    <li><a href="<%= contextPath %>/register" class="btn-secondary">Register</a></li>
                <% } %>
            </ul>
        </nav>
    </div>
</header>

