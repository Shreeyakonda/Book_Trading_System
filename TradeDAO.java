package com.booktrading.dao;

import com.booktrading.model.Trade;
import com.booktrading.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TradeDAO {
    private DatabaseConnection dbConnection;

    public TradeDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean createTrade(Trade trade) {
        String sql = "INSERT INTO trades (requester_id, book_id, status, message) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, trade.getRequesterId());
            pstmt.setInt(2, trade.getBookId());
            pstmt.setString(3, trade.getStatusString());
            pstmt.setString(4, trade.getMessage());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        trade.setTradeId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating trade: " + e.getMessage());
        }
        return false;
    }

    public Trade getTradeById(int tradeId) {
        String sql = "SELECT t.*, " +
                     "r.first_name as requester_first_name, r.last_name as requester_last_name, " +
                     "b.title as book_title, b.author as book_author, b.owner_id, " +
                     "o.first_name as owner_first_name, o.last_name as owner_last_name " +
                     "FROM trades t " +
                     "LEFT JOIN users r ON t.requester_id = r.user_id " +
                     "LEFT JOIN books b ON t.book_id = b.book_id " +
                     "LEFT JOIN users o ON b.owner_id = o.user_id " +
                     "WHERE t.trade_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tradeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrade(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting trade by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Trade> getTradesByRequester(int requesterId) {
        String sql = "SELECT t.*, " +
                     "r.first_name as requester_first_name, r.last_name as requester_last_name, " +
                     "b.title as book_title, b.author as book_author, b.owner_id, " +
                     "o.first_name as owner_first_name, o.last_name as owner_last_name " +
                     "FROM trades t " +
                     "LEFT JOIN users r ON t.requester_id = r.user_id " +
                     "LEFT JOIN books b ON t.book_id = b.book_id " +
                     "LEFT JOIN users o ON b.owner_id = o.user_id " +
                     "WHERE t.requester_id = ? ORDER BY t.created_at DESC";
        
        return getTradesFromQuery(sql, requesterId);
    }

    public List<Trade> getTradesByOwner(int ownerId) {
        String sql = "SELECT t.*, " +
                     "r.first_name as requester_first_name, r.last_name as requester_last_name, " +
                     "b.title as book_title, b.author as book_author, b.owner_id, " +
                     "o.first_name as owner_first_name, o.last_name as owner_last_name " +
                     "FROM trades t " +
                     "LEFT JOIN users r ON t.requester_id = r.user_id " +
                     "LEFT JOIN books b ON t.book_id = b.book_id " +
                     "LEFT JOIN users o ON b.owner_id = o.user_id " +
                     "WHERE b.owner_id = ? ORDER BY t.created_at DESC";
        
        return getTradesFromQuery(sql, ownerId);
    }

    public List<Trade> getPendingTradesByOwner(int ownerId) {
        String sql = "SELECT t.*, " +
                     "r.first_name as requester_first_name, r.last_name as requester_last_name, " +
                     "b.title as book_title, b.author as book_author, b.owner_id, " +
                     "o.first_name as owner_first_name, o.last_name as owner_last_name " +
                     "FROM trades t " +
                     "LEFT JOIN users r ON t.requester_id = r.user_id " +
                     "LEFT JOIN books b ON t.book_id = b.book_id " +
                     "LEFT JOIN users o ON b.owner_id = o.user_id " +
                     "WHERE b.owner_id = ? AND t.status = 'PENDING' ORDER BY t.created_at DESC";
        
        return getTradesFromQuery(sql, ownerId);
    }

    public boolean updateTradeStatus(int tradeId, Trade.TradeStatus status) {
        String sql = "UPDATE trades SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE trade_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, tradeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating trade status: " + e.getMessage());
        }
        return false;
    }

    public boolean hasPendingTrade(int requesterId, int bookId) {
        String sql = "SELECT COUNT(*) FROM trades WHERE requester_id = ? AND book_id = ? AND status = 'PENDING'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requesterId);
            pstmt.setInt(2, bookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking pending trade: " + e.getMessage());
        }
        return false;
    }

    private List<Trade> getTradesFromQuery(String sql, int userId) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Trade> trades = new ArrayList<>();
                while (rs.next()) {
                    trades.add(mapResultSetToTrade(rs));
                }
                return trades;
            }
        } catch (SQLException e) {
            System.err.println("Error executing trade query: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private Trade mapResultSetToTrade(ResultSet rs) throws SQLException {
        Trade trade = new Trade();
        trade.setTradeId(rs.getInt("trade_id"));
        trade.setRequesterId(rs.getInt("requester_id"));
        trade.setBookId(rs.getInt("book_id"));
        trade.setStatusString(rs.getString("status"));
        trade.setMessage(rs.getString("message"));
        trade.setCreatedAt(rs.getTimestamp("created_at"));
        trade.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set requester name
        try {
            String firstName = rs.getString("requester_first_name");
            String lastName = rs.getString("requester_last_name");
            if (firstName != null && lastName != null) {
                trade.setRequesterName(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            // Skip if not in result set
        }
        
        // Set book info
        try {
            trade.setBookTitle(rs.getString("book_title"));
            trade.setBookAuthor(rs.getString("book_author"));
        } catch (SQLException e) {
            // Skip if not in result set
        }
        
        // Set owner info
        try {
            trade.setOwnerId(rs.getInt("owner_id"));
            String firstName = rs.getString("owner_first_name");
            String lastName = rs.getString("owner_last_name");
            if (firstName != null && lastName != null) {
                trade.setOwnerName(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            // Skip if not in result set
        }
        
        return trade;
    }
}

