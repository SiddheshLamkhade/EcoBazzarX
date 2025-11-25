# EcoBazaar API - Postman Testing Guide

Complete step-by-step guide to test all endpoints in Postman.

## Base URL
```
http://localhost:8080
```

---

## Testing Order & Setup

### Step 1: Register Users (No Auth Required)

#### 1.1 Register Admin
**POST** `http://localhost:8080/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
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

#### 1.2 Register Seller
**POST** `http://localhost:8080/register`

**Body (raw JSON):**
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

#### 1.3 Register Customer
**POST** `http://localhost:8080/register`

**Body (raw JSON):**
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

---

### Step 2: Login & Get JWT Tokens

#### 2.1 Login as Admin
**POST** `http://localhost:8080/login`

**Body (raw JSON):**
```json
{
  "username": "admin_user",
  "password": "admin@2024"
}
```

**Save the token from response:** Copy the `token` value - you'll need it for admin endpoints.

#### 2.2 Login as Seller
**POST** `http://localhost:8080/login`

**Body (raw JSON):**
```json
{
  "username": "eco_seller",
  "password": "seller@123"
}
```

**Save the token:** Copy the `token` value for seller endpoints.

#### 2.3 Login as User
**POST** `http://localhost:8080/login`

**Body (raw JSON):**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Save the token:** Copy the `token` value for user endpoints.

**Alternative Login (with email):**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

---

### Step 3: Seller - Add Products (Use Seller Token)

**POST** `http://localhost:8080/seller/add-product`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_SELLER_TOKEN_HERE
```

#### 3.1 Add Organic Cotton T-Shirt
**Body (raw JSON):**
```json
{
  "name": "Organic Cotton T-Shirt",
  "description": "100% organic cotton t-shirt, eco-friendly and comfortable",
  "category": "Clothing",
  "price": 29.99,
  "stockQuantity": 100,
  "imageUrl": "https://example.com/tshirt.jpg",
  "carbonDetails": {
    "productName": "Organic Cotton T-Shirt",
    "volume": "Medium",
    "weight": "0.2kg",
    "materialComposition": "100% Organic Cotton",
    "manufacturingLocation": "India",
    "electricityType": "Renewable",
    "manufacturingEnergyUsed": "3 kWh",
    "packagingDetails": "Recycled cardboard, biodegradable plastic",
    "shippingMode": "Sea Freight",
    "seaFreightDistance": 8000.0,
    "truckDistance": 150.0,
    "lifespan": "2 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 90.0,
    "biodegradabilityRate": 95.0
  }
}
```

#### 3.2 Add Bamboo Water Bottle
**Body (raw JSON):**
```json
{
  "name": "Bamboo Water Bottle",
  "description": "Eco-friendly bamboo water bottle with stainless steel interior",
  "category": "Home & Kitchen",
  "price": 24.99,
  "stockQuantity": 50,
  "imageUrl": "https://example.com/bamboo-bottle.jpg",
  "carbonDetails": {
    "productName": "Bamboo Water Bottle",
    "volume": "750ml",
    "weight": "0.3kg",
    "materialComposition": "60% Bamboo, 40% Stainless Steel",
    "manufacturingLocation": "Vietnam",
    "electricityType": "Solar",
    "manufacturingEnergyUsed": "2 kWh",
    "packagingDetails": "Recycled cardboard box, no plastic",
    "shippingMode": "Sea Freight",
    "seaFreightDistance": 5000.0,
    "truckDistance": 200.0,
    "lifespan": "5 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 80.0,
    "biodegradabilityRate": 60.0
  }
}
```

#### 3.3 Add Reusable Shopping Bag
**Body (raw JSON):**
```json
{
  "name": "Reusable Shopping Bag",
  "description": "100% recycled cotton reusable shopping bag",
  "category": "Accessories",
  "price": 9.99,
  "stockQuantity": 200,
  "imageUrl": "https://example.com/bag.jpg",
  "carbonDetails": {
    "productName": "Reusable Shopping Bag",
    "volume": "15L",
    "weight": "0.15kg",
    "materialComposition": "100% Recycled Cotton",
    "manufacturingLocation": "India",
    "electricityType": "Renewable",
    "manufacturingEnergyUsed": "1 kWh",
    "packagingDetails": "Minimal paper packaging",
    "shippingMode": "Sea Freight",
    "seaFreightDistance": 8000.0,
    "truckDistance": 150.0,
    "lifespan": "3 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 100.0,
    "biodegradabilityRate": 90.0
  }
}
```

---

### Step 4: Public Product Endpoints (No Auth Required)

#### 4.1 Get All Products
**GET** `http://localhost:8080/all-products`

**Headers:** None required

#### 4.2 Filter Products
**GET** `http://localhost:8080/api/products?category=Clothing&maxPrice=50&maxCarbon=5`

**Query Parameters:**
- `name` (optional): Search by product name
- `category` (optional): Filter by category
- `minPrice` (optional): Minimum price filter
- `maxPrice` (optional): Maximum price filter
- `maxCarbon` (optional): Maximum carbon footprint

**Examples:**
```
http://localhost:8080/api/products?category=Clothing
http://localhost:8080/api/products?maxCarbon=3
http://localhost:8080/api/products?name=bamboo&maxPrice=30
```

---

### Step 5: Seller - Manage Products (Use Seller Token)

#### 5.1 Get My Products
**GET** `http://localhost:8080/seller/my-products`

**Headers:**
```
Authorization: Bearer YOUR_SELLER_TOKEN_HERE
```

#### 5.2 Update Product
**PUT** `http://localhost:8080/seller/update-product/1`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_SELLER_TOKEN_HERE
```

**Body (raw JSON):**
```json
{
  "price": 27.99,
  "stockQuantity": 150,
  "description": "Updated description - Premium organic cotton t-shirt"
}
```

#### 5.3 Delete Product
**DELETE** `http://localhost:8080/seller/delete-product/1`

**Headers:**
```
Authorization: Bearer YOUR_SELLER_TOKEN_HERE
```

---

### Step 6: User - Cart Operations (Use User Token)

#### 6.1 Add to Cart
**POST** `http://localhost:8080/api/cart/add/1/2`

**URL Parameters:**
- `productId`: 1
- `quantity`: 2

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

#### 6.2 Add More Items to Cart
**POST** `http://localhost:8080/api/cart/add/2/1`

(Add product ID 2, quantity 1)

#### 6.3 View Cart
**GET** `http://localhost:8080/api/cart/view`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

#### 6.4 Update Cart Item Quantity
**PUT** `http://localhost:8080/api/cart/update/1/5`

**URL Parameters:**
- `productId`: 1
- `quantity`: 5

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

#### 6.5 Remove from Cart
**DELETE** `http://localhost:8080/api/cart/remove/2`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

#### 6.6 Clear Cart
**DELETE** `http://localhost:8080/api/cart/clear`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

---

### Step 7: User - Checkout & Orders (Use User Token)

#### 7.1 Checkout (Create Order)
**POST** `http://localhost:8080/api/checkout/add-checkout`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

**Body:** None (uses current cart)

#### 7.2 Get My Orders
**GET** `http://localhost:8080/api/checkout/my-orders`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

#### 7.3 Update Order Status
**PUT** `http://localhost:8080/api/checkout/status/1?status=SHIPPED`

**Query Parameters:**
- `status`: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

---

### Step 8: Carbon Footprint Endpoints

#### 8.1 Estimate Carbon Footprint (Public)
**POST** `http://localhost:8080/api/carbon/estimate`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "carbonDetails": {
    "productName": "Solar Panel Charger",
    "volume": "Small",
    "weight": "0.5kg",
    "materialComposition": "Silicon, Aluminum, Glass",
    "manufacturingLocation": "China",
    "electricityType": "Coal",
    "manufacturingEnergyUsed": "10 kWh",
    "packagingDetails": "Plastic packaging",
    "shippingMode": "Air Freight",
    "seaFreightDistance": 0.0,
    "truckDistance": 500.0,
    "lifespan": "10 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 60.0,
    "biodegradabilityRate": 10.0
  }
}
```

#### 8.2 Get User Carbon History (Authenticated)
**GET** `http://localhost:8080/api/carbon/user/history`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

---

### Step 9: Leaderboard Endpoints (Public)

#### 9.1 Get Top Eco Users
**GET** `http://localhost:8080/leaderboard/top`

#### 9.2 Get User Rank
**GET** `http://localhost:8080/leaderboard/rank/1`

**URL Parameter:** `userId` (e.g., 1)

#### 9.3 Top by Carbon Points
**GET** `http://localhost:8080/leaderboard/top-points`

#### 9.4 Top by Carbon Saved
**GET** `http://localhost:8080/leaderboard/top-carbon`

#### 9.5 Get User Stats
**GET** `http://localhost:8080/leaderboard/user/john_doe`

**URL Parameter:** `username` (e.g., john_doe)

---

### Step 10: Admin Endpoints (Use Admin Token)

#### 10.1 Get All Users
**GET** `http://localhost:8080/admin/all-users`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.2 Get All Sellers
**GET** `http://localhost:8080/admin/all-sellers`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.3 Get All Customers
**GET** `http://localhost:8080/admin/all-customers`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.4 Get All Admins
**GET** `http://localhost:8080/admin/all-admins`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.5 Change User Role
**PUT** `http://localhost:8080/admin/change-role/john_doe?role=SELLER`

**Query Parameter:** `role` (USER, SELLER, ADMIN)

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.6 Update User Details
**PUT** `http://localhost:8080/admin/update-user/3`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

**Body (raw JSON):**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "9998887777"
}
```

#### 10.7 Delete User
**DELETE** `http://localhost:8080/admin/delete-user/5`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

#### 10.8 Get All Orders
**GET** `http://localhost:8080/admin/all-orders`

**Headers:**
```
Authorization: Bearer YOUR_ADMIN_TOKEN_HERE
```

---

## Postman Environment Setup

Create a Postman Environment with these variables:

| Variable | Initial Value | Current Value |
|----------|--------------|---------------|
| `base_url` | `http://localhost:8080` | |
| `admin_token` | | (Set after admin login) |
| `seller_token` | | (Set after seller login) |
| `user_token` | | (Set after user login) |
| `product_id` | | (Set after creating product) |
| `order_id` | | (Set after checkout) |

### How to Set Tokens Automatically

After each login request, add this to the **Tests** tab in Postman:

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    if (jsonData.token) {
        // Change variable name based on login type
        pm.environment.set("user_token", jsonData.token);
    }
}
```

Then use `{{user_token}}` in Authorization headers.

---

## Testing Workflow Summary

1. **Setup Phase:**
   - Register Admin → Login Admin → Save token
   - Register Seller → Login Seller → Save token
   - Register User → Login User → Save token

2. **Product Phase:**
   - Seller adds 3-5 products
   - Test getting all products (public)
   - Test filtering products

3. **Shopping Phase:**
   - User adds items to cart
   - View cart
   - Update quantities
   - Checkout

4. **Admin Phase:**
   - View all users
   - View all orders
   - Change user roles
   - Update user details

5. **Leaderboard Phase:**
   - Check top users
   - Check user rankings
   - View user stats

---

## Common Status Codes

- `200 OK` - Success
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing/invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Tips for Testing

1. **Always check the response** - Save IDs for future requests
2. **Use environment variables** - Store tokens and IDs
3. **Test in order** - Some endpoints depend on others
4. **Check database** - Verify data is actually saved
5. **Test error cases** - Try invalid inputs, expired tokens, wrong roles
6. **Clear cart before checkout** - Test multiple checkout flows

---

## Troubleshooting

### 401 Unauthorized
- Check if token is valid
- Ensure "Bearer " prefix in Authorization header
- Token might be expired (30 hours expiry)

### 403 Forbidden
- User doesn't have required role
- Admin endpoint needs ADMIN role
- Seller endpoint needs SELLER role

### 400 Bad Request
- Check JSON syntax
- Verify all required fields are present
- Check data types match

### Product Stock Issues
- Ensure product has sufficient stock before adding to cart
- Stock is reduced after checkout

---

## Sample Test Collection Order

1. Register 3 users (Admin, Seller, User)
2. Login all 3 users (save tokens)
3. Seller: Add 3 products
4. Public: Get all products
5. Public: Filter products
6. User: Add items to cart (2-3 items)
7. User: View cart
8. User: Update cart quantity
9. User: Checkout
10. User: View my orders
11. Seller: View my products
12. Admin: View all users
13. Admin: View all orders
14. Leaderboard: Check top users
15. Carbon: Estimate new product
16. User: Check carbon history

This completes the full testing cycle! 🚀
