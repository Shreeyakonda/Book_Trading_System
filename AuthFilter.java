package com.booktrading.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/dashboard/*", "/book/*", "/trade/*", "/api/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI();
        String pathInfo = httpRequest.getPathInfo();
        
        // Allow access to public pages
        if (path.endsWith("/") || path.contains("/login") || path.contains("/register") || 
            path.contains("/browse") || path.contains("/book-detail") || path.contains("/search")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Allow public access to book detail pages
        if (pathInfo != null && pathInfo.equals("/detail")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

