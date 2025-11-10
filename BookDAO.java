package com.booktrading.dao;

import com.booktrading.model.Book;
import com.booktrading.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private DatabaseConnection dbConnection;

    public BookDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean createBook(Book book) {
        String sql = "INSERT INTO books (owner_id, title, author, isbn, description, condition, category, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, book.getOwnerId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getIsbn());
            pstmt.setString(5, book.getDescription());
            pstmt.setString(6, book.getCondition());
            pstmt.setString(7, book.getCategory());
            pstmt.setString(8, book.getStatusString());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
        return false;
    }

    public Book getBookById(int bookId) {
        String sql = "SELECT b.*, u.first_name, u.last_name FROM books b " +
                     "LEFT JOIN users u ON b.owner_id = u.user_id WHERE b.book_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Book> getAllAvailableBooks() {
        String sql = "SELECT b.*, u.first_name, u.last_name FROM books b " +
                     "LEFT JOIN users u ON b.owner_id = u.user_id " +
                     "WHERE b.status = 'AVAILABLE' ORDER BY b.created_at DESC";
        
        return getBooksFromQuery(sql);
    }

    public List<Book> getBooksByOwner(int ownerId) {
        String sql = "SELECT b.*, u.first_name, u.last_name FROM books b " +
                     "LEFT JOIN users u ON b.owner_id = u.user_id " +
                     "WHERE b.owner_id = ? ORDER BY b.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ownerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
                return books;
            }
        } catch (SQLException e) {
            System.err.println("Error getting books by owner: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Book> searchBooks(String searchTerm) {
        String sql = "SELECT b.*, u.first_name, u.last_name FROM books b " +
                     "LEFT JOIN users u ON b.owner_id = u.user_id " +
                     "WHERE b.status = 'AVAILABLE' AND " +
                     "(b.title LIKE ? OR b.author LIKE ? OR b.description LIKE ?) " +
                     "ORDER BY b.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
                return books;
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, description = ?, " +
                     "condition = ?, category = ?, status = ? WHERE book_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getDescription());
            pstmt.setString(5, book.getCondition());
            pstmt.setString(6, book.getCategory());
            pstmt.setString(7, book.getStatusString());
            pstmt.setInt(8, book.getBookId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteBook(int bookId) {
        String sql = "UPDATE books SET status = 'REMOVED' WHERE book_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
        return false;
    }

    public boolean updateBookStatus(int bookId, Book.BookStatus status) {
        String sql = "UPDATE books SET status = ? WHERE book_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book status: " + e.getMessage());
        }
        return false;
    }

    private List<Book> getBooksFromQuery(String sql) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setOwnerId(rs.getInt("owner_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setDescription(rs.getString("description"));
        book.setCondition(rs.getString("condition"));
        book.setCategory(rs.getString("category"));
        book.setStatusString(rs.getString("status"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        book.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set owner name if available
        try {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            if (firstName != null && lastName != null) {
                book.setOwnerName(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            // Owner name not in result set, skip
        }
        
        return book;
    }
}

