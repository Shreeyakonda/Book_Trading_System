<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Book Trading System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <jsp:include page="common/header.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="error-page">
                <h1>Oops! Something went wrong</h1>
                <p>We're sorry, but something went wrong. Please try again later.</p>
                <a href="<%= request.getContextPath() %>/" class="btn btn-primary">Go Home</a>
            </div>
        </div>
    </main>

    <jsp:include page="common/footer.jsp" />
</body>
</html>

