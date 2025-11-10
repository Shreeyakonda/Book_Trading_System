-- Book Trading System Database Schema
-- MySQL/PostgreSQL Compatible

CREATE DATABASE IF NOT EXISTS book_trading_system;
USE book_trading_system;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Books Table
CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20),
    description TEXT,
    condition VARCHAR(50) DEFAULT 'Good',
    category VARCHAR(50),
    status ENUM('AVAILABLE', 'TRADED', 'REMOVED') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_owner (owner_id),
    INDEX idx_status (status),
    INDEX idx_title (title),
    INDEX idx_author (author),
    FULLTEXT idx_search (title, author, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Trades Table
CREATE TABLE IF NOT EXISTS trades (
    trade_id INT AUTO_INCREMENT PRIMARY KEY,
    requester_id INT NOT NULL,
    book_id INT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    INDEX idx_requester (requester_id),
    INDEX idx_book (book_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    trade_id INT,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (trade_id) REFERENCES trades(trade_id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert sample data (optional)
INSERT INTO users (username, email, password_hash, first_name, last_name) VALUES
('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User'),
('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe'),
('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Smith');

-- Note: Password hash above is for 'password' (bcrypt)

INSERT INTO books (owner_id, title, author, isbn, description, condition, category) VALUES
(2, 'The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'A classic American novel', 'Good', 'Fiction'),
(2, 'To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 'A gripping tale of racial injustice', 'Excellent', 'Fiction'),
(3, '1984', 'George Orwell', '978-0451524935', 'Dystopian novel', 'Good', 'Fiction'),
(3, 'Pride and Prejudice', 'Jane Austen', '978-0141439518', 'Classic romance novel', 'Very Good', 'Fiction');

