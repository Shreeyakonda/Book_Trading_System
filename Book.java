package com.booktrading.model;

import java.sql.Timestamp;

public class Book {
    private int bookId;
    private int ownerId;
    private String ownerName;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String condition;
    private String category;
    private BookStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum BookStatus {
        AVAILABLE, TRADED, REMOVED
    }

    public Book() {
    }

    public Book(int ownerId, String title, String author, String isbn, String description, 
                String condition, String category) {
        this.ownerId = ownerId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.condition = condition;
        this.category = category;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getStatusString() {
        return status != null ? status.name() : BookStatus.AVAILABLE.name();
    }

    public void setStatusString(String status) {
        this.status = BookStatus.valueOf(status);
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

