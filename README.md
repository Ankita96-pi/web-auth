# Web Authentication System

A full-stack web authentication system built with Spring Boot (Backend) and React (Frontend). This system provides secure user authentication with features like email verification, password reset, and JWT-based authentication.
# Demo Link 
   https://www.youtube.com/watch?v=UjwaFpyYSjE
## Features

- User registration with email verification
- Secure login with JWT authentication
- Password reset functionality
- Rate limiting for security
- CSRF protection
- Responsive Material-UI design
- Form validation
- Protected routes

## Tech Stack

### Backend

- Spring Boot 3.4.4
- Spring Security
- Spring Data JPA
- MariaDB
- JWT Authentication
- Spring Mail
- Lombok
- Validation API
- Bucket4j (Rate Limiting)

### Frontend

- React 18
- Material-UI
- React Router
- Formik & Yup
- Axios
- React Toastify

## Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- MariaDB
- Maven
- npm or yarn

## Database Setup

1. Install MariaDB if not already installed
2. Create a new database:

   ```sql
   CREATE DATABASE web_auth;
   ```

3. Create the users table:
   ```sql
   CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(50) NOT NULL,
       email VARCHAR(255) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL,
       phone_number VARCHAR(15) NOT NULL,
       enabled BOOLEAN DEFAULT FALSE,
       verification_token VARCHAR(255),
       reset_token VARCHAR(255),
       reset_token_expiry DATETIME,
       created_at DATETIME,
       updated_at DATETIME,
       is_verified BOOLEAN DEFAULT FALSE
   );
   ```

## Backend Setup

1. Clone the repository
2. Navigate to the project root directory
3. Configure the database connection in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/web_auth
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. Configure email settings in `application.properties`:

   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_specific_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

5. Configure JWT settings:

   ```properties
   jwt.secret=your_jwt_secret_key
   jwt.expiration=86400000
   ```

6. Build and run the backend:
   ```bash
   mvn clean install
   mvn spring:boot run
   ```

The backend will start on `http://localhost:8080`

## Frontend Setup

1. Navigate to the frontend directory:

   ```bash
   cd frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend will start on `http://localhost:5173`

## API Routes

### Authentication Endpoints

1. **Register User**

   - URL: `/api/auth/register`
   - Method: POST
   - Description: Register a new user
   - Required Fields: name, email, password, phoneNumber

2. **Login**

   - URL: `/api/auth/login`
   - Method: POST
   - Description: Authenticate user and return JWT token
   - Required Fields: email, password

3. **Verify Email**

   - URL: `/api/auth/verify-email`
   - Method: GET
   - Description: Verify user's email address
   - Required Query: token

4. **Forgot Password**

   - URL: `/api/auth/forgot-password`
   - Method: POST
   - Description: Send password reset email
   - Required Fields: email

5. **Reset Password**
   - URL: `/api/auth/reset-password`
   - Method: POST
   - Description: Reset password using token
   - Required Fields: token, newPassword

## Security Features

- JWT-based authentication
- Password hashing with BCrypt
- Email verification
- Password reset functionality
- Rate limiting on sensitive endpoints
- CSRF protection
- Input validation
- Secure password requirements

