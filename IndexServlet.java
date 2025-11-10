package com.booktrading.servlet;

import com.booktrading.dao.BookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get recent available books
        request.setAttribute("books", bookDAO.getAllAvailableBooks());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

