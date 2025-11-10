<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.booktrading.model.Book" %>
<%@ page import="com.booktrading.model.Trade" %>
<%@ page import="com.booktrading.model.Notification" %>
<%@ page import="com.booktrading.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Book> myBooks = (List<Book>) request.getAttribute("myBooks");
    List<Trade> pendingRequests = (List<Trade>) request.getAttribute("pendingRequests");
    List<Trade> myTradeRequests = (List<Trade>) request.getAttribute("myTradeRequests");
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
    Integer unreadCount = (Integer) request.getAttribute("unreadCount");
    
    if (myBooks == null) myBooks = new java.util.ArrayList<>();
    if (pendingRequests == null) pendingRequests = new java.util.ArrayList<>();
    if (myTradeRequests == null) myTradeRequests = new java.util.ArrayList<>();
    if (notifications == null) notifications = new java.util.ArrayList<>();
    if (unreadCount == null) unreadCount = 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <h1>Welcome, <%= user.getFullName() %>!</h1>
            
            <% if (unreadCount > 0) { %>
                <div class="alert alert-info">
                    You have <%= unreadCount %> unread notification(s)
                </div>
            <% } %>

            <div class="dashboard-tabs">
                <button class="tab-btn active" onclick="showTab('my-books', this)">My Books</button>
                <button class="tab-btn" onclick="showTab('pending-requests', this)">Pending Requests (<%= pendingRequests.size() %>)</button>
                <button class="tab-btn" onclick="showTab('my-requests', this)">My Requests</button>
                <button class="tab-btn" onclick="showTab('notifications', this)">Notifications (<%= unreadCount %>)</button>
            </div>

            <div id="my-books" class="tab-content active">
                <h2>My Books</h2>
                <a href="<%= request.getContextPath() %>/book/add" class="btn btn-primary">Add New Book</a>
                <% if (myBooks.isEmpty()) { %>
                    <p class="no-results">You haven't added any books yet.</p>
                <% } else { %>
                    <div class="books-grid">
                        <% for (Book book : myBooks) { %>
                            <div class="book-card">
                                <div class="book-info">
                                    <h3><%= book.getTitle() %></h3>
                                    <p class="book-author">by <%= book.getAuthor() %></p>
                                    <p class="book-status">Status: <span class="status-<%= book.getStatusString().toLowerCase() %>"><%= book.getStatusString() %></span></p>
                                    <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                                        <p class="book-description"><%= book.getDescription().length() > 100 ? 
                                            book.getDescription().substring(0, 100) + "..." : book.getDescription() %></p>
                                    <% } %>
                                </div>
                                <div class="book-actions">
                                    <a href="<%= request.getContextPath() %>/book/detail?id=<%= book.getBookId() %>" class="btn btn-small">View</a>
                                    <% if (book.getStatus() == Book.BookStatus.AVAILABLE) { %>
                                        <a href="<%= request.getContextPath() %>/book/delete?id=<%= book.getBookId() %>" 
                                           class="btn btn-small btn-danger" 
                                           onclick="return confirm('Are you sure you want to remove this book?')">Remove</a>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>

            <div id="pending-requests" class="tab-content">
                <h2>Pending Trade Requests</h2>
                <% if (pendingRequests.isEmpty()) { %>
                    <p class="no-results">No pending requests.</p>
                <% } else { %>
                    <div class="trades-list">
                        <% for (Trade trade : pendingRequests) { %>
                            <div class="trade-card">
                                <div class="trade-info">
                                    <h3><%= trade.getBookTitle() %></h3>
                                    <p>Requested by: <strong><%= trade.getRequesterName() %></strong></p>
                                    <% if (trade.getMessage() != null && !trade.getMessage().isEmpty()) { %>
                                        <p class="trade-message">Message: <%= trade.getMessage() %></p>
                                    <% } %>
                                    <p class="trade-date">Requested on: <%= trade.getCreatedAt() %></p>
                                </div>
                                <div class="trade-actions">
                                    <form action="<%= request.getContextPath() %>/trade/approve" method="post" style="display: inline;">
                                        <input type="hidden" name="tradeId" value="<%= trade.getTradeId() %>">
                                        <button type="submit" class="btn btn-success">Approve</button>
                                    </form>
                                    <form action="<%= request.getContextPath() %>/trade/reject" method="post" style="display: inline;">
                                        <input type="hidden" name="tradeId" value="<%= trade.getTradeId() %>">
                                        <button type="submit" class="btn btn-danger">Reject</button>
                                    </form>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>

            <div id="my-requests" class="tab-content">
                <h2>My Trade Requests</h2>
                <% if (myTradeRequests.isEmpty()) { %>
                    <p class="no-results">You haven't made any trade requests yet.</p>
                <% } else { %>
                    <div class="trades-list">
                        <% for (Trade trade : myTradeRequests) { %>
                            <div class="trade-card">
                                <div class="trade-info">
                                    <h3><%= trade.getBookTitle() %></h3>
                                    <p>Owner: <strong><%= trade.getOwnerName() %></strong></p>
                                    <p class="trade-status">Status: <span class="status-<%= trade.getStatusString().toLowerCase() %>"><%= trade.getStatusString() %></span></p>
                                    <% if (trade.getMessage() != null && !trade.getMessage().isEmpty()) { %>
                                        <p class="trade-message">Message: <%= trade.getMessage() %></p>
                                    <% } %>
                                    <p class="trade-date">Requested on: <%= trade.getCreatedAt() %></p>
                                </div>
                                <div class="trade-actions">
                                    <% if (trade.getStatus() == Trade.TradeStatus.PENDING) { %>
                                        <form action="<%= request.getContextPath() %>/trade/cancel" method="post" style="display: inline;">
                                            <input type="hidden" name="tradeId" value="<%= trade.getTradeId() %>">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to cancel this request?')">Cancel</button>
                                        </form>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>

            <div id="notifications" class="tab-content">
                <h2>Notifications</h2>
                <% if (notifications.isEmpty()) { %>
                    <p class="no-results">No notifications.</p>
                <% } else { %>
                    <div class="notifications-list">
                        <% for (Notification notification : notifications) { %>
                            <div class="notification-card <%= notification.isRead() ? "read" : "unread" %>">
                                <div class="notification-info">
                                    <p><%= notification.getMessage() %></p>
                                    <p class="notification-date"><%= notification.getCreatedAt() %></p>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
    <script src="<%= request.getContextPath() %>/js/dashboard.js"></script>
</body>
</html>

