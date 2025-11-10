package com.booktrading.servlet;

import com.booktrading.dao.BookDAO;
import com.booktrading.model.Book;
import com.booktrading.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/book/*")
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        if (pathInfo.equals("/add")) {
            request.getRequestDispatcher("/add-book.jsp").forward(request, response);
        } else if (pathInfo.equals("/detail")) {
            String bookId = request.getParameter("id");
            if (bookId != null) {
                try {
                    Book book = bookDAO.getBookById(Integer.parseInt(bookId));
                    if (book != null) {
                        request.setAttribute("book", book);
                        request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (pathInfo.equals("/delete")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String bookId = request.getParameter("id");
            if (bookId != null) {
                try {
                    Book book = bookDAO.getBookById(Integer.parseInt(bookId));
                    User user = (User) session.getAttribute("user");
                    if (book != null && book.getOwnerId() == user.getUserId()) {
                        bookDAO.deleteBook(book.getBookId());
                    }
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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

        if (pathInfo != null && pathInfo.equals("/add")) {
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String isbn = request.getParameter("isbn");
            String description = request.getParameter("description");
            String condition = request.getParameter("condition");
            String category = request.getParameter("category");

            if (title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty()) {
                request.setAttribute("error", "Title and Author are required");
                request.getRequestDispatcher("/add-book.jsp").forward(request, response);
                return;
            }

            Book book = new Book();
            book.setOwnerId(user.getUserId());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setIsbn(isbn != null ? isbn.trim() : "");
            book.setDescription(description != null ? description.trim() : "");
            book.setCondition(condition != null ? condition.trim() : "Good");
            book.setCategory(category != null ? category.trim() : "");

            if (bookDAO.createBook(book)) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Failed to add book. Please try again.");
                request.getRequestDispatcher("/add-book.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

