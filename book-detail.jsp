<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.booktrading.model.Book" %>
<%@ page import="com.booktrading.model.User" %>
<%
    Book book = (Book) request.getAttribute("book");
    String error = (String) request.getAttribute("error");
    User user = (User) session.getAttribute("user");
    
    if (book == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= book.getTitle() %> - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <% if (error != null) { %>
                <div class="alert alert-error"><%= error %></div>
            <% } %>
            
            <div class="book-detail">
                <div class="book-detail-header">
                    <h1><%= book.getTitle() %></h1>
                    <p class="book-author">by <%= book.getAuthor() %></p>
                </div>
                
                <div class="book-detail-content">
                    <div class="book-detail-info">
                        <div class="info-item">
                            <strong>Owner:</strong> <%= book.getOwnerName() != null ? book.getOwnerName() : "Unknown" %>
                        </div>
                        <% if (book.getIsbn() != null && !book.getIsbn().isEmpty()) { %>
                            <div class="info-item">
                                <strong>ISBN:</strong> <%= book.getIsbn() %>
                            </div>
                        <% } %>
                        <div class="info-item">
                            <strong>Condition:</strong> <%= book.getCondition() %>
                        </div>
                        <% if (book.getCategory() != null && !book.getCategory().isEmpty()) { %>
                            <div class="info-item">
                                <strong>Category:</strong> <%= book.getCategory() %>
                            </div>
                        <% } %>
                        <div class="info-item">
                            <strong>Status:</strong> 
                            <span class="status-<%= book.getStatusString().toLowerCase() %>"><%= book.getStatusString() %></span>
                        </div>
                        <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                            <div class="info-item">
                                <strong>Description:</strong>
                                <p><%= book.getDescription() %></p>
                            </div>
                        <% } %>
                    </div>
                </div>
                
                <div class="book-detail-actions">
                    <% if (user != null && user.getUserId() == book.getOwnerId()) { %>
                        <p>This is your book.</p>
                    <% } else if (user != null && book.getStatus() == Book.BookStatus.AVAILABLE) { %>
                        <button onclick="showTradeForm()" class="btn btn-primary">Request Trade</button>
                        <div id="trade-form" class="trade-form" style="display: none;">
                            <form action="<%= request.getContextPath() %>/trade/request" method="post">
                                <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                                <div class="form-group">
                                    <label for="message">Message (optional):</label>
                                    <textarea id="message" name="message" rows="4" placeholder="Add a message to the book owner..."></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Send Request</button>
                                <button type="button" onclick="hideTradeForm()" class="btn btn-secondary">Cancel</button>
                            </form>
                        </div>
                    <% } else if (user == null) { %>
                        <p><a href="<%= request.getContextPath() %>/login">Login</a> to request a trade.</p>
                    <% } else { %>
                        <p>This book is not available for trade.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
    <script src="<%= request.getContextPath() %>/js/book-detail.js"></script>
</body>
</html>

