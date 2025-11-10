package com.booktrading.servlet;

import com.booktrading.dao.BookDAO;
import com.booktrading.dao.NotificationDAO;
import com.booktrading.dao.TradeDAO;
import com.booktrading.model.Book;
import com.booktrading.model.Notification;
import com.booktrading.model.Trade;
import com.booktrading.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/trade/*")
public class TradeServlet extends HttpServlet {
    private TradeDAO tradeDAO;
    private BookDAO bookDAO;
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        tradeDAO = new TradeDAO();
        bookDAO = new BookDAO();
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/request")) {
            // Create trade request
            String bookIdStr = request.getParameter("bookId");
            String message = request.getParameter("message");

            if (bookIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                int bookId = Integer.parseInt(bookIdStr);
                Book book = bookDAO.getBookById(bookId);

                if (book == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                if (book.getOwnerId() == user.getUserId()) {
                    request.setAttribute("error", "You cannot request your own book");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
                    return;
                }

                if (book.getStatus() != Book.BookStatus.AVAILABLE) {
                    request.setAttribute("error", "This book is not available for trade");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
                    return;
                }

                // Check if already has pending request
                if (tradeDAO.hasPendingTrade(user.getUserId(), bookId)) {
                    request.setAttribute("error", "You already have a pending request for this book");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
                    return;
                }

                Trade trade = new Trade(user.getUserId(), bookId, message != null ? message.trim() : "");
                if (tradeDAO.createTrade(trade)) {
                    // Create notification for book owner
                    Notification notification = new Notification(
                        book.getOwnerId(),
                        trade.getTradeId(),
                        user.getFullName() + " requested to trade for " + book.getTitle(),
                        "TRADE_REQUEST"
                    );
                    notificationDAO.createNotification(notification);

                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    request.setAttribute("error", "Failed to create trade request");
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else if (pathInfo != null && pathInfo.equals("/approve")) {
            // Approve trade request
            String tradeIdStr = request.getParameter("tradeId");
            if (tradeIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                int tradeId = Integer.parseInt(tradeIdStr);
                Trade trade = tradeDAO.getTradeById(tradeId);

                if (trade == null || trade.getOwnerId() != user.getUserId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (tradeDAO.updateTradeStatus(tradeId, Trade.TradeStatus.APPROVED)) {
                    // Update book status
                    bookDAO.updateBookStatus(trade.getBookId(), Book.BookStatus.TRADED);

                    // Create notification for requester
                    Notification notification = new Notification(
                        trade.getRequesterId(),
                        tradeId,
                        "Your trade request for " + trade.getBookTitle() + " has been approved!",
                        "TRADE_APPROVED"
                    );
                    notificationDAO.createNotification(notification);

                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else if (pathInfo != null && pathInfo.equals("/reject")) {
            // Reject trade request
            String tradeIdStr = request.getParameter("tradeId");
            if (tradeIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                int tradeId = Integer.parseInt(tradeIdStr);
                Trade trade = tradeDAO.getTradeById(tradeId);

                if (trade == null || trade.getOwnerId() != user.getUserId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (tradeDAO.updateTradeStatus(tradeId, Trade.TradeStatus.REJECTED)) {
                    // Create notification for requester
                    Notification notification = new Notification(
                        trade.getRequesterId(),
                        tradeId,
                        "Your trade request for " + trade.getBookTitle() + " has been rejected.",
                        "TRADE_REJECTED"
                    );
                    notificationDAO.createNotification(notification);

                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else if (pathInfo != null && pathInfo.equals("/cancel")) {
            // Cancel trade request
            String tradeIdStr = request.getParameter("tradeId");
            if (tradeIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                int tradeId = Integer.parseInt(tradeIdStr);
                Trade trade = tradeDAO.getTradeById(tradeId);

                if (trade == null || trade.getRequesterId() != user.getUserId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (tradeDAO.updateTradeStatus(tradeId, Trade.TradeStatus.CANCELLED)) {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

