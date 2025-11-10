<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.booktrading.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Book - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <h1>Add New Book</h1>
            <% if (error != null) { %>
                <div class="alert alert-error"><%= error %></div>
            <% } %>
            <div class="form-container">
                <form action="<%= request.getContextPath() %>/book/add" method="post" class="book-form">
                    <div class="form-group">
                        <label for="title">Title *</label>
                        <input type="text" id="title" name="title" required autofocus>
                    </div>
                    <div class="form-group">
                        <label for="author">Author *</label>
                        <input type="text" id="author" name="author" required>
                    </div>
                    <div class="form-group">
                        <label for="isbn">ISBN</label>
                        <input type="text" id="isbn" name="isbn">
                    </div>
                    <div class="form-group">
                        <label for="category">Category</label>
                        <input type="text" id="category" name="category" placeholder="e.g., Fiction, Non-Fiction, Science">
                    </div>
                    <div class="form-group">
                        <label for="condition">Condition</label>
                        <select id="condition" name="condition">
                            <option value="Excellent">Excellent</option>
                            <option value="Very Good" selected>Very Good</option>
                            <option value="Good">Good</option>
                            <option value="Fair">Fair</option>
                            <option value="Poor">Poor</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" rows="5" placeholder="Describe the book..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Book</button>
                    <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-secondary">Cancel</a>
                </form>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
</body>
</html>

