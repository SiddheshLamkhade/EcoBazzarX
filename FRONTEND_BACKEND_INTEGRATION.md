# Frontend-Backend Integration Guide

## ✅ Integration Complete

The frontend authentication (login and register pages) has been successfully integrated with the Spring Boot backend API.

## Changes Made

### 1. **authService.js** (`FrontEnd/src/services/authService.js`)

#### Updated Configuration
- Set `MOCK_MODE = false` to use real backend API instead of mock data
- API endpoints proxy through Vite to `http://localhost:8080`

#### Updated `login()` Method
- Sends login request to `/api/auth/login` (proxied to backend `/login`)
- Supports both email and username login
- Request format:
  ```javascript
  {
    email: "user@example.com",
    username: "user@example.com",  // Supports email or username
    password: "password123"
  }
  ```
- Response handling:
  ```javascript
  {
    success: true,
    message: "Login successful",
    token: "jwt-token-here",
    user: {
      id: 1,
      username: "john_doe",
      email: "john@example.com",
      firstName: "John",
      lastName: "Doe",
      phone: "1234567890",
      role: "USER",
      createdAt: "2025-11-15T..."
    }
  }
  ```

#### Updated `signup()` Method
- Sends registration request to `/api/auth/signup` (proxied to backend `/signup`)
- Transforms frontend form data to backend RegisterRequest format
- Automatically generates username from email
- Splits full name into firstName and lastName
- Request format:
  ```javascript
  {
    username: "john_doe",  // Generated from email
    email: "john@example.com",
    password: "password123",
    firstName: "John",
    lastName: "Doe",
    phone: "1234567890",
    role: "USER"  // or "SELLER"
  }
  ```
- Response handling: Same format as login response

### 2. **Vite Proxy Configuration** (`FrontEnd/vite.config.js`)
Already configured to proxy API requests:
```javascript
proxy: {
  '/api/auth': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api\/auth/, ''),
  }
}
```

## Backend API Endpoints

### POST `/register` or `/signup`
- Creates new user account
- Automatically creates user profile for leaderboard
- Returns JWT token and user data
- Validates unique username and email

### POST `/login`
- Authenticates user with username or email + password
- Returns JWT token and user data
- Token stored in localStorage as `ecobazaarx_token`

## User Roles
- **USER**: Regular customer account (immediate access)
- **SELLER**: Seller account (PENDING status until admin approval)
- **ADMIN**: Admin account (cannot signup, must login with predefined credentials)

### Admin Credentials
```
Email: admin@ecobazaarx.com
Password: EcoAdmin@2024
```

## Testing the Integration

### Prerequisites
1. PostgreSQL database running on `localhost:5432`
2. Database: `EcoBazarX_db`
3. Spring Boot backend running on port 8080
4. React frontend running on port 3000

### Start Backend
```bash
cd d:\EcoBazaar
mvnw spring-boot:run
```

### Start Frontend
```bash
cd d:\EcoBazaar\FrontEnd
npm run dev
```

### Test Registration Flow
1. Navigate to http://localhost:3000/signup
2. Fill in the registration form:
   - Name: Your Full Name
   - Email: your@email.com
   - Phone: 1234567890
   - Password: Strong@Password123
   - Role: USER or SELLER
3. Click "Create Account"
4. On success, you'll be automatically logged in and redirected to your dashboard

### Test Login Flow
1. Navigate to http://localhost:3000/login
2. Enter credentials:
   - Email: your@email.com (or username)
   - Password: Your password
3. Click "Login"
4. On success, you'll be redirected to your role-specific dashboard:
   - ADMIN → `/admin`
   - SELLER → `/seller`
   - USER → `/user`

## Token Management
- JWT token stored in `localStorage` as `ecobazaarx_token`
- Automatically attached to API requests via `Authorization: Bearer <token>` header
- Session timeout: 30 minutes of inactivity
- Token included in apiClient.js for all authenticated requests

## Error Handling
- Invalid credentials: "Invalid email or password"
- Duplicate email: "Email already exists"
- Duplicate username: "Username already exists"
- All errors displayed via toast notifications

## Data Flow

### Registration
```
SignupPage.jsx 
  → AuthContext.signup()
    → authService.signup()
      → POST /api/auth/signup (Vite proxy)
        → POST /signup (Spring Boot)
          → AuthService.register()
            → Save user + profile to DB
            → Generate JWT token
            → Return RegisterResponse
```

### Login
```
LoginPage.jsx 
  → AuthContext.login()
    → authService.login()
      → POST /api/auth/login (Vite proxy)
        → POST /login (Spring Boot)
          → AuthService.login()
            → Authenticate user
            → Generate JWT token
            → Return LoginResponse
```

## Next Steps
- Social login (Google/Facebook OAuth) - Still using mock implementation
- OTP login - Needs SMS service integration
- Password reset functionality
- Email verification

## Troubleshooting

### Backend not responding
- Check if Spring Boot is running: `http://localhost:8080`
- Verify database connection in `application.properties`
- Check for errors in backend console

### CORS errors
- Backend has `@CrossOrigin(origins = "*")` enabled
- Vite proxy should handle CORS automatically

### Token not persisting
- Check browser localStorage for `ecobazaarx_token`
- Verify token is being returned in login/signup response
- Check browser console for errors

### Session expires immediately
- Check `lastActivity` timestamp in localStorage
- Verify SESSION_TIMEOUT in AuthContext.jsx (30 minutes)

## Security Notes
⚠️ **Important for Production:**
1. Remove OpenAI API key from `application.properties`
2. Use environment variables for sensitive data
3. Enable HTTPS
4. Configure proper CORS origins (remove wildcard)
5. Implement refresh token mechanism
6. Add rate limiting on auth endpoints
7. Hash database credentials
