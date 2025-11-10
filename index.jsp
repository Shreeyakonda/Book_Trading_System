<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.booktrading.model.Book" %>
<%
    List<Book> books = (List<Book>) request.getAttribute("books");
    if (books == null) books = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Trading System - Home</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <section class="hero">
            <div class="container">
                <h1>Welcome to Book Trading System</h1>
                <p>Trade your books with others - No money needed!</p>
                <div class="hero-actions">
                    <a href="<%= request.getContextPath() %>/browse" class="btn btn-primary">Browse Books</a>
                    <% if (session.getAttribute("user") == null) { %>
                        <a href="<%= request.getContextPath() %>/register" class="btn btn-secondary">Get Started</a>
                    <% } %>
                </div>
            </div>
        </section>

        <section class="featured-books">
            <div class="container">
                <h2>Available Books</h2>
                <div class="search-bar">
                    <form action="<%= request.getContextPath() %>/browse" method="get">
                        <input type="text" name="search" placeholder="Search books by title, author, or description..." class="search-input">
                        <button type="submit" class="btn btn-primary">Search</button>
                    </form>
                </div>
                
                <% if (books.isEmpty()) { %>
                    <p class="no-results">No books available at the moment.</p>
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
        </section>
    </main>

    <jsp:include page="common/footer.jsp" />
</body>
</html>

