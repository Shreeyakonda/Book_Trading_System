package com.booktrading.dao;

import com.booktrading.model.Notification;
import com.booktrading.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private DatabaseConnection dbConnection;

    public NotificationDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, trade_id, message, type, is_read) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notification.getUserId());
            if (notification.getTradeId() != null) {
                pstmt.setInt(2, notification.getTradeId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, notification.getMessage());
            pstmt.setString(4, notification.getType());
            pstmt.setBoolean(5, notification.isRead());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
        }
        return false;
    }

    public List<Notification> getNotificationsByUser(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
                return notifications;
            }
        } catch (SQLException e) {
            System.err.println("Error getting notifications: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Notification> getUnreadNotificationsByUser(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = false ORDER BY created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
                return notifications;
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread notifications: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
        }
        return false;
    }

    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
        }
        return false;
    }

    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = false";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
        }
        return 0;
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        int tradeId = rs.getInt("trade_id");
        notification.setTradeId(rs.wasNull() ? null : tradeId);
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreatedAt(rs.getTimestamp("created_at"));
        return notification;
    }
}

