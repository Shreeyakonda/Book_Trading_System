package com.booktrading.model;

import java.sql.Timestamp;

public class Trade {
    private int tradeId;
    private int requesterId;
    private String requesterName;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private int ownerId;
    private String ownerName;
    private TradeStatus status;
    private String message;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum TradeStatus {
        PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
    }

    public Trade() {
    }

    public Trade(int requesterId, int bookId, String message) {
        this.requesterId = requesterId;
        this.bookId = bookId;
        this.message = message;
        this.status = TradeStatus.PENDING;
    }

    // Getters and Setters
    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public String getStatusString() {
        return status != null ? status.name() : TradeStatus.PENDING.name();
    }

    public void setStatusString(String status) {
        this.status = TradeStatus.valueOf(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

