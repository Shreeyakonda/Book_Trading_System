<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.booktrading.model.Book" %>
<%
    List<Book> books = (List<Book>) request.getAttribute("books");
    String searchTerm = (String) request.getAttribute("searchTerm");
    if (books == null) books = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Books - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <h1>Browse Books</h1>
            <div class="search-bar">
                <form action="<%= request.getContextPath() %>/browse" method="get">
                    <input type="text" name="search" 
                           value="<%= searchTerm != null ? searchTerm : "" %>" 
                           placeholder="Search books by title, author, or description..." 
                           class="search-input">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <% if (searchTerm != null && !searchTerm.isEmpty()) { %>
                        <a href="<%= request.getContextPath() %>/browse" class="btn btn-secondary">Clear</a>
                    <% } %>
                </form>
            </div>
            
            <% if (searchTerm != null && !searchTerm.isEmpty()) { %>
                <p class="search-results">Search results for "<%= searchTerm %>"</p>
            <% } %>
            
            <% if (books.isEmpty()) { %>
                <p class="no-results">No books found.</p>
            <% } else { %>
                <div class="books-grid">
                    <% for (Book book : books) { %>
                        <div class="book-card">
                            <div class="book-info">
                                <h3><%= book.getTitle() %></h3>
                                <p class="book-author">by <%= book.getAuthor() %></p>
                                <% if (book.getCategory() != null && !book.getCategory().isEmpty()) { %>
                                    <span class="book-category"><%= book.getCategory() %></span>
                                <% } %>
                                <p class="book-condition">Condition: <%= book.getCondition() %></p>
                                <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                                    <p class="book-description"><%= book.getDescription().length() > 100 ? 
                                        book.getDescription().substring(0, 100) + "..." : book.getDescription() %></p>
                                <% } %>
                                <p class="book-owner">Owner: <%= book.getOwnerName() != null ? book.getOwnerName() : "Unknown" %></p>
                            </div>
                            <div class="book-actions">
                                <a href="<%= request.getContextPath() %>/book/detail?id=<%= book.getBookId() %>" class="btn btn-primary">View Details</a>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
</body>
</html>

