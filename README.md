# Book Trading System

A full-stack web application for peer-to-peer book trading without monetary transactions. Users can list their books, search for books from others, request trades, and manage their trade requests.

## Technology Stack

- **Backend**: Java Servlets, JSP
- **Database**: MySQL (PostgreSQL supported)
- **Frontend**: HTML5, CSS3, JavaScript (Pure JS, no frameworks)
- **Application Server**: Apache Tomcat 10+
- **Build Tool**: Maven
- **Password Hashing**: BCrypt

## Features

- User registration and authentication
- Add and manage books
- Browse and search books
- Trade request system
- Notifications for trade approvals/rejections
- Responsive design
- SQL injection prevention
- Password encryption

## Prerequisites

- Java JDK 11 or higher
- Apache Tomcat 10 or higher
- MySQL 8.0+ (or PostgreSQL 12+)
- Maven 3.6+

## Setup Instructions

### 1. Database Setup

#### MySQL Setup

1. Install MySQL if not already installed
2. Create the database:
   ```sql
   CREATE DATABASE book_trading_system;
   ```

3. Run the schema script:
   ```bash
   mysql -u root -p book_trading_system < database/schema.sql
   ```

4. Update database credentials in `src/main/java/com/booktrading/util/DatabaseConnection.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/book_trading_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

#### PostgreSQL Setup (Alternative)

1. Install PostgreSQL if not already installed
2. Create the database:
   ```sql
   CREATE DATABASE book_trading_system;
   ```

3. Update the schema.sql file to use PostgreSQL syntax (remove `ENGINE=InnoDB` and adjust `AUTO_INCREMENT` to `SERIAL`)
4. Run the schema script:
   ```bash
   psql -U postgres -d book_trading_system -f database/schema.sql
   ```

5. Update `DatabaseConnection.java` to use PostgreSQL connection method

### 2. Build the Project

1. Clone or download the project
2. Navigate to the project directory
3. Build the project using Maven:
   ```bash
   mvn clean package
   ```

This will create a WAR file in the `target` directory: `book-trading-system.war`

### 3. Deploy to Tomcat

#### Option 1: Manual Deployment

1. Copy the WAR file to Tomcat's `webapps` directory:
   ```bash
   cp target/book-trading-system.war $CATALINA_HOME/webapps/
   ```

2. Start Tomcat:
   ```bash
   $CATALINA_HOME/bin/startup.sh  # Linux/Mac
   # or
   $CATALINA_HOME/bin/startup.bat  # Windows
   ```

#### Option 2: Using Tomcat Maven Plugin

1. Run the application using Maven:
   ```bash
   mvn tomcat7:run
   ```

   The application will be available at `http://localhost:8080`


### 5. Default Test Users

The schema includes sample users with the password `password`:
- Username: `admin`, Email: `admin@example.com`
- Username: `john_doe`, Email: `john@example.com`
- Username: `jane_smith`, Email: `jane@example.com`

**Note**: Change these passwords in production!

## Project Structure

```
Book_Trading_System/
├── database/
│   ├── schema.sql          # Database schema
│   └── init.sql            # Database initialization script
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── booktrading/
│       │           ├── dao/              # Data Access Objects
│       │           ├── filter/           # Servlet Filters
│       │           ├── model/            # Java Beans (POJOs)
│       │           ├── servlet/          # Servlets (Controllers)
│       │           └── util/             # Utility classes
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml              # Web application configuration
│           ├── common/                  # Common JSP fragments
│           ├── css/                     # Stylesheets
│           ├── js/                      # JavaScript files
│           └── *.jsp                    # JSP pages (Views)
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

## Configuration

### Database Connection

Edit `src/main/java/com/booktrading/util/DatabaseConnection.java` to configure:
- Database URL
- Database username
- Database password
- Connection pool settings

### Application Settings

- Session timeout: Configured in `web.xml` (default: 30 minutes)
- Password requirements: Minimum 6 characters (configured in `RegisterServlet.java`)

## Usage

### For Users

1. **Register**: Create a new account
2. **Login**: Access your dashboard
3. **Add Books**: List books you want to trade
4. **Browse Books**: Search and view available books
5. **Request Trade**: Send trade requests for books you want
6. **Manage Trades**: Approve/reject trade requests for your books
7. **View Notifications**: See trade request updates

### For Administrators

- Use the admin account to manage the system
- Monitor user activities through database queries
- Manage books and trades if needed

## Security Features

- **Password Hashing**: BCrypt with salt
- **SQL Injection Prevention**: Prepared statements
- **Session Management**: HttpSession with timeout
- **Input Validation**: Server-side and client-side validation
- **Authentication Filter**: Protects restricted pages

## Performance Considerations

- Database connection pooling (configured in DatabaseConnection)
- Auto-reconnect on database failure
- Indexed database queries
- Efficient JSP rendering

## Troubleshooting

### Database Connection Issues

1. Verify database is running
2. Check database credentials in `DatabaseConnection.java`
3. Ensure database exists and schema is loaded
4. Check firewall settings

### Tomcat Deployment Issues

1. Verify Tomcat version (must be 10+)
2. Check port availability (default: 8080)
3. Review Tomcat logs: `$CATALINA_HOME/logs/catalina.out`
4. Ensure Java version is 11+

### Build Issues

1. Verify Maven is installed: `mvn --version`
2. Check Java version: `java -version`
3. Clean and rebuild: `mvn clean package`
4. Check for dependency issues in `pom.xml`

## Future Enhancements

- Chat/messaging system between users
- Book rating and reviews
- Advanced search filters
- Email notifications
- Book image uploads
- Trade history tracking
- User profiles
- Wishlist functionality

---

**Note**: For production use, consider:
- Adding HTTPS/SSL
- Implementing rate limiting
- Adding comprehensive logging
- Setting up backup strategies
- Implementing monitoring and alerts
- Adding unit and integration tests

