package com.booktrading.servlet;

import com.booktrading.dao.BookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/browse")
public class BrowseServlet extends HttpServlet {
    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        
        if (search != null && !search.trim().isEmpty()) {
            request.setAttribute("books", bookDAO.searchBooks(search.trim()));
            request.setAttribute("searchTerm", search.trim());
        } else {
            request.setAttribute("books", bookDAO.getAllAvailableBooks());
        }
        
        request.getRequestDispatcher("/browse.jsp").forward(request, response);
    }
}

