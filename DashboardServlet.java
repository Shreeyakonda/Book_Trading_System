package com.booktrading.servlet;

import com.booktrading.dao.BookDAO;
import com.booktrading.dao.NotificationDAO;
import com.booktrading.dao.TradeDAO;
import com.booktrading.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private BookDAO bookDAO;
    private TradeDAO tradeDAO;
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
        tradeDAO = new TradeDAO();
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();

        // Get user's books
        request.setAttribute("myBooks", bookDAO.getBooksByOwner(userId));

        // Get pending trade requests (books requested by others)
        request.setAttribute("pendingRequests", tradeDAO.getPendingTradesByOwner(userId));

        // Get user's trade requests (books user requested)
        request.setAttribute("myTradeRequests", tradeDAO.getTradesByRequester(userId));

        // Get notifications
        request.setAttribute("notifications", notificationDAO.getNotificationsByUser(userId));
        request.setAttribute("unreadCount", notificationDAO.getUnreadCount(userId));

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}

