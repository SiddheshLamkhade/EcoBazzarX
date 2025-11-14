# EcoBazaar Backend

A Spring Boot REST API for an eco-friendly e-commerce platform that tracks carbon footprints and rewards sustainable shopping.

## Features

- **User Management**: Registration, login with JWT authentication, role-based access (USER, SELLER, ADMIN)
- **Product Management**: CRUD operations, carbon footprint tracking, filtering
- **Shopping Cart**: Add/remove items, update quantities
- **Order Management**: Checkout, order history, status tracking
- **Carbon Footprint**: AI-powered carbon estimation using OpenAI API
- **Leaderboard**: User rankings based on eco-friendly purchases
- **Security**: JWT-based authentication with role-based authorization

## Tech Stack

- Java 17
- Spring Boot 3.5.7
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Lombok
- OpenAI API (for carbon footprint estimation)

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- Maven
- OpenAI API key (optional, has fallback calculation)

## Setup Instructions

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE ecobazaar;
```

### 2. Configure Application Properties

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ecobazaar
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

# JWT Configuration
jwt.secret=yourSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong

# OpenAI Configuration (optional)
openai.api.key=your_openai_api_key_here
```

### 3. Build and Run

```bash
# Build the project
mvnw clean install

# Run the application
mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /register` - Register a new user
- `POST /login` - Login and get JWT token

### Public Endpoints
- `GET /all-products` - Get all products
- `GET /api/products` - Filter products by name, category, price, carbon

### Admin Endpoints (ROLE_ADMIN)
- `GET /admin/all-users` - Get all users
- `GET /admin/all-sellers` - Get all sellers
- `GET /admin/all-customers` - Get all customers
- `GET /admin/all-admins` - Get all admins
- `PUT /admin/change-role/{username}` - Change user role
- `DELETE /admin/delete-user/{id}` - Delete user
- `PUT /admin/update-user/{id}` - Update user details
- `GET /admin/all-orders` - Get all orders

### Seller Endpoints (ROLE_SELLER)
- `POST /seller/add-product` - Add new product
- `GET /seller/my-products` - Get seller's products
- `PUT /seller/update-product/{productId}` - Update product
- `DELETE /seller/delete-product/{productId}` - Delete product

### Cart Endpoints (Authenticated)
- `POST /api/cart/add/{productId}/{quantity}` - Add to cart
- `GET /api/cart/view` - View cart
- `DELETE /api/cart/remove/{productId}` - Remove from cart
- `DELETE /api/cart/clear` - Clear cart
- `PUT /api/cart/update/{productId}/{quantity}` - Update quantity

### Checkout Endpoints (Authenticated)
- `POST /api/checkout/add-checkout` - Create order from cart
- `GET /api/checkout/my-orders` - Get user's orders
- `PUT /api/checkout/status/{orderId}` - Update order status

### Carbon Footprint Endpoints
- `POST /api/carbon/estimate` - Estimate carbon footprint
- `GET /api/carbon/user/history` - Get user's carbon history (Authenticated)

### Leaderboard Endpoints
- `GET /leaderboard/top` - Get top eco users
- `GET /leaderboard/rank/{userId}` - Get user rank
- `GET /leaderboard/top-points` - Top by carbon points
- `GET /leaderboard/top-carbon` - Top by carbon saved
- `GET /leaderboard/user/{username}` - Get user stats

## Authentication

For protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Sample User Registration

### Customer
```json
{
  "username": "john_doe",
  "password": "password123",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890",
  "role": "USER"
}
```

### Seller
```json
{
  "username": "eco_seller",
  "password": "seller@123",
  "email": "seller@ecobazaar.com",
  "firstName": "Eco",
  "lastName": "Seller",
  "phone": "5551234567",
  "role": "SELLER"
}
```

### Admin
```json
{
  "username": "admin_user",
  "password": "admin@2024",
  "email": "admin@ecobazaar.com",
  "firstName": "Admin",
  "lastName": "User",
  "phone": "9876543210",
  "role": "ADMIN"
}
```

## Project Structure

```
src/main/java/EcoBazaarX/
├── controller/          # REST API controllers
├── service/            # Business logic layer
├── repository/         # Data access layer
├── entity/             # JPA entities
├── dto/                # Data transfer objects
├── security/           # Security configuration and JWT
└── Application.java    # Main application class
```

## Database Schema

The application automatically creates the following tables:
- `users` - User accounts
- `products` - Product catalog
- `cart` & `cart_item` - Shopping cart
- `orders` & `order_item` - Order management
- `user_profiles` - Leaderboard data
- `carbon_insight` - Carbon tracking history

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK` - Successful request
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required or invalid credentials
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Carbon Points System

Products are awarded carbon points based on their footprint:
- ≤ 1 kg CO₂e: 50 points
- ≤ 3 kg CO₂e: 30 points
- ≤ 5 kg CO₂e: 20 points
- ≤ 10 kg CO₂e: 10 points
- > 10 kg CO₂e: 5 points

## Badge System

Users earn badges based on accumulated carbon points:
- **Bronze**: < 200 points
- **Silver**: 200-499 points
- **Gold**: ≥ 500 points

## Notes

- JWT tokens expire after 30 hours
- All passwords are hashed using BCrypt
- OpenAI integration is optional; fallback calculation is available
- CORS is enabled for all origins (configure for production)

