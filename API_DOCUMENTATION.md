# EcoBazaar API Documentation

## Base URL
```
http://localhost:8080
```

---

## Table of Contents
1. [Authentication Endpoints](#authentication-endpoints)
2. [User Endpoints](#user-endpoints)
3. [Admin Endpoints](#admin-endpoints)
4. [Seller Endpoints](#seller-endpoints)
5. [Product Endpoints](#product-endpoints)
6. [Cart Endpoints](#cart-endpoints)
7. [Checkout Endpoints](#checkout-endpoints)
8. [Carbon Footprint Endpoints](#carbon-footprint-endpoints)
9. [Leaderboard Endpoints](#leaderboard-endpoints)
10. [Database Tables & Sample Data](#database-tables--sample-data)

---

## Authentication Endpoints

### 1. Register User
**POST** `/register`

#### Request Body - Customer/User Registration
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

#### Request Body - Admin Registration
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

#### Request Body - Seller Registration
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

#### Response (200 OK)
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890",
  "role": "USER",
  "createdAt": "2025-11-12T10:30:00"
}
```

---

### 2. Login
**POST** `/login`

#### Request Body (Login with Username)
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

#### Request Body (Login with Email)
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTYzOTk5OTk5OX0.abcdefghijklmnopqrstuvwxyz123456",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890",
    "role": "USER",
    "createdAt": "2025-11-12T10:30:00"
  }
}
```

#### Response (401 Unauthorized)
```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

#### Database Tables Involved:

**USERS Table**
| id | username | password | email | first_name | last_name | phone | role | created_at |
|----|----------|----------|-------|------------|-----------|-------|------|------------|
| 1 | john_doe | $2a$10$xyz... | john.doe@example.com | John | Doe | 1234567890 | USER | 2025-11-12 10:30:00 |
| 2 | eco_seller | $2a$10$abc... | seller@ecobazaar.com | Eco | Seller | 5551234567 | SELLER | 2025-11-12 11:00:00 |
| 3 | admin_user | $2a$10$def... | admin@ecobazaar.com | Admin | User | 9876543210 | ADMIN | 2025-11-12 09:00:00 |
| 4 | jane_smith | $2a$10$ghi... | jane@example.com | Jane | Smith | 9998887777 | USER | 2025-11-12 12:00:00 |

---

## User Endpoints

### 3. Get All Products
**GET** `/all-products`

**Headers:** None required (public endpoint)

#### Response (200 OK)
```json
[
  {
    "productId": 1,
    "name": "Organic Cotton T-Shirt",
    "description": "100% organic cotton t-shirt",
    "category": "Clothing",
    "price": 29.99,
    "stockQuantity": 100,
    "imageUrl": "https://example.com/tshirt.jpg",
    "carbonFootprint": 2.5,
    "carbonExplanation": "Low carbon footprint due to organic cotton and local production",
    "carbonPoints": 30,
    "volume": "1L",
    "weight": "0.2kg",
    "materialComposition": "100% Organic Cotton",
    "manufacturingLocation": "India",
    "electricityType": "Renewable",
    "manufacturingEnergyUsed": "3 kWh",
    "packagingDetails": "Recycled cardboard, no plastic",
    "shippingMode": "Sea Freight",
    "seaFreightDistance": 8000.0,
    "truckDistance": 200.0,
    "lifespan": "2 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 95.0,
    "biodegradabilityRate": 100.0,
    "createdAt": "2025-11-10T08:00:00",
    "updatedAt": "2025-11-10T08:00:00"
  }
]
```

#### Database Tables Involved:

**PRODUCTS Table**
| product_id | name | description | category | price | stock_quantity | image_url | carbon_footprint | carbon_explanation | carbon_points | posted_by | created_at | updated_at |
|------------|------|-------------|----------|-------|----------------|-----------|------------------|-------------------|---------------|-----------|------------|------------|
| 1 | Organic Cotton T-Shirt | 100% organic cotton t-shirt | Clothing | 29.99 | 100 | https://example.com/tshirt.jpg | 2.5 | Low carbon footprint due to organic cotton | 30 | 2 | 2025-11-10 08:00:00 | 2025-11-10 08:00:00 |
| 2 | Bamboo Water Bottle | Eco-friendly bamboo water bottle | Home & Kitchen | 24.99 | 50 | https://example.com/bottle.jpg | 1.8 | Renewable bamboo, solar manufacturing | 50 | 2 | 2025-11-11 09:00:00 | 2025-11-11 09:00:00 |
| 3 | Recycled Plastic Shoes | Shoes made from recycled ocean plastic | Footwear | 79.99 | 30 | https://example.com/shoes.jpg | 8.5 | Higher carbon due to plastic processing | 10 | 5 | 2025-11-11 10:00:00 | 2025-11-11 10:00:00 |
| 4 | Solar Charger | Portable solar panel charger | Electronics | 49.99 | 25 | https://example.com/charger.jpg | 12.0 | Electronic components have higher carbon | 5 | 5 | 2025-11-11 11:00:00 | 2025-11-11 11:00:00 |
| 5 | Reusable Shopping Bag | 100% recycled cotton bag | Accessories | 9.99 | 200 | https://example.com/bag.jpg | 1.2 | Very low carbon, recycled materials | 50 | 2 | 2025-11-11 12:00:00 | 2025-11-11 12:00:00 |

**USERS Table** (posted_by references this)
| id | username | email | first_name | last_name | role |
|----|----------|-------|------------|-----------|------|
| 2 | eco_seller | seller@ecobazaar.com | Eco | Seller | SELLER |
| 5 | green_seller | green@ecobazaar.com | Green | Seller | SELLER |

---

## Admin Endpoints

**Note:** All admin endpoints require `Authorization: Bearer <token>` header with ADMIN role.

### 4. Get All Users
**GET** `/admin/all-users`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890",
    "role": "USER",
    "createdAt": "2025-11-12T10:30:00"
  },
  {
    "id": 2,
    "username": "eco_seller",
    "email": "seller@ecobazaar.com",
    "firstName": "Eco",
    "lastName": "Seller",
    "phone": "5551234567",
    "role": "SELLER",
    "createdAt": "2025-11-12T11:00:00"
  }
]
```

#### Database Tables Involved:

**USERS Table**
| id | username | password | email | first_name | last_name | phone | role | created_at |
|----|----------|----------|-------|------------|-----------|-------|------|------------|
| 1 | john_doe | $2a$10$xyz... | john.doe@example.com | John | Doe | 1234567890 | USER | 2025-11-12 10:30:00 |
| 2 | eco_seller | $2a$10$abc... | seller@ecobazaar.com | Eco | Seller | 5551234567 | SELLER | 2025-11-12 11:00:00 |
| 3 | admin_user | $2a$10$def... | admin@ecobazaar.com | Admin | User | 9876543210 | ADMIN | 2025-11-12 09:00:00 |
| 4 | jane_smith | $2a$10$ghi... | jane@example.com | Jane | Smith | 9998887777 | USER | 2025-11-12 12:00:00 |
| 5 | green_seller | $2a$10$jkl... | green@ecobazaar.com | Green | Seller | 4445556666 | SELLER | 2025-11-12 13:00:00 |

---

### 5. Get All Sellers
**GET** `/admin/all-sellers`

#### Response (200 OK)
```json
[
  {
    "id": 2,
    "username": "eco_seller",
    "email": "seller@ecobazaar.com",
    "firstName": "Eco",
    "lastName": "Seller",
    "phone": "5551234567",
    "role": "SELLER",
    "createdAt": "2025-11-12T11:00:00"
  }
]
```

---

### 6. Get All Customers
**GET** `/admin/all-customers`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "1234567890",
    "role": "USER",
    "createdAt": "2025-11-12T10:30:00"
  }
]
```

---

### 7. Get All Admins
**GET** `/admin/all-admins`

#### Response (200 OK)
```json
[
  {
    "id": 3,
    "username": "admin_user",
    "email": "admin@ecobazaar.com",
    "firstName": "Admin",
    "lastName": "User",
    "phone": "9876543210",
    "role": "ADMIN",
    "createdAt": "2025-11-12T09:00:00"
  }
]
```

---

### 8. Change User Role
**PUT** `/admin/change-role/{username}?role=SELLER`

**Path Variable:** `username` - Username of the user

**Query Parameter:** `role` - New role (USER, SELLER, ADMIN)

#### Example Request
```
PUT /admin/change-role/john_doe?role=SELLER
```

#### Response (200 OK)
```json
"Role for user 'john_doe' updated to: SELLER"
```

---

### 9. Delete User
**DELETE** `/admin/delete-user/{id}`

**Path Variable:** `id` - User ID

#### Example Request
```
DELETE /admin/delete-user/5
```

#### Response (200 OK)
```json
"User deleted successfully (ID: 5)"
```

---

### 10. Update User Details
**PUT** `/admin/update-user/{id}`

**Path Variable:** `id` - User ID

#### Request Body
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "9998887777"
}
```

#### Response (200 OK)
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "jane.smith@example.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phone": "9998887777",
  "role": "USER",
  "createdAt": "2025-11-12T10:30:00"
}
```

---

### 11. Get All Orders (Admin)
**GET** `/admin/all-orders`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "totalPrice": 79.97,
    "totalCarbon": 7.5,
    "status": "PENDING",
    "createdAt": "2025-11-12T14:30:00",
    "items": [
      {
        "id": 1,
        "productName": "Organic Cotton T-Shirt",
        "quantity": 2,
        "priceAtPurchase": 29.99,
        "carbonAtPurchase": 2.5
      }
    ]
  }
]
```

#### Database Tables Involved:

**ORDERS Table**
| id | user_id | total_price | total_carbon | status | created_at |
|----|---------|-------------|--------------|--------|------------|
| 1 | 1 | 84.97 | 6.8 | PENDING | 2025-11-12 14:30:00 |
| 2 | 4 | 49.98 | 3.6 | DELIVERED | 2025-11-10 10:00:00 |
| 3 | 1 | 79.99 | 8.5 | SHIPPED | 2025-11-11 15:00:00 |
| 4 | 4 | 149.95 | 12.5 | CONFIRMED | 2025-11-12 09:00:00 |

**ORDER_ITEM Table**
| id | order_id | product_id | product_name | quantity | price_at_purchase | carbon_at_purchase |
|----|----------|------------|--------------|----------|-------------------|-------------------|
| 1 | 1 | 1 | Organic Cotton T-Shirt | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | Bamboo Water Bottle | 1 | 24.99 | 1.8 |
| 3 | 2 | 2 | Bamboo Water Bottle | 2 | 24.99 | 1.8 |
| 4 | 3 | 3 | Recycled Plastic Shoes | 1 | 79.99 | 8.5 |
| 5 | 4 | 1 | Organic Cotton T-Shirt | 5 | 29.99 | 2.5 |

---

## Seller Endpoints

**Note:** All seller endpoints require `Authorization: Bearer <token>` header with SELLER role.

### 12. Add Product
**POST** `/seller/add-product`

#### Request Body
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

#### Response (200 OK)
```json
"✅ Product added successfully by eco_seller | Carbon footprint: 1.8 kg CO₂e | Points: 50"
```

#### Database Tables Involved:

**PRODUCTS Table** (New product row created)
| product_id | name | description | category | price | stock_quantity | carbon_footprint | carbon_explanation | carbon_points | posted_by |
|------------|------|-------------|----------|-------|----------------|------------------|-------------------|---------------|-----------|
| 6 | Bamboo Water Bottle | Eco-friendly bamboo water bottle with stainless steel interior | Home & Kitchen | 24.99 | 50 | 1.8 | Low carbon due to renewable bamboo, solar manufacturing, and efficient shipping | 50 | 2 |

**CARBON_DETAILS** (Embedded in Products table)
| product_id | product_name | material_composition | manufacturing_location | electricity_type | shipping_mode | recyclability_rate | biodegradability_rate |
|------------|--------------|---------------------|----------------------|------------------|---------------|-------------------|---------------------|
| 6 | Bamboo Water Bottle | 60% Bamboo, 40% Stainless Steel | Vietnam | Solar | Sea Freight | 80.0 | 60.0 |

---

### 13. Get My Products (Seller)
**GET** `/seller/my-products`

#### Response (200 OK)
```json
[
  {
    "productId": 1,
    "name": "Bamboo Water Bottle",
    "description": "Eco-friendly bamboo water bottle with stainless steel interior",
    "category": "Home & Kitchen",
    "price": 24.99,
    "stockQuantity": 50,
    "imageUrl": "https://example.com/bamboo-bottle.jpg",
    "carbonFootprint": 1.8,
    "carbonExplanation": "Low carbon due to renewable bamboo, solar manufacturing, and efficient shipping",
    "carbonPoints": 50,
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
    "biodegradabilityRate": 60.0,
    "createdAt": "2025-11-12T09:00:00",
    "updatedAt": "2025-11-12T09:00:00"
  }
]
```

#### Database Tables Involved:

**PRODUCTS Table** (Filtered by posted_by = current seller's user_id)
| product_id | name | category | price | stock_quantity | carbon_footprint | carbon_points | posted_by | created_at |
|------------|------|----------|-------|----------------|------------------|---------------|-----------|------------|
| 1 | Organic Cotton T-Shirt | Clothing | 29.99 | 100 | 2.5 | 30 | 2 | 2025-11-10 08:00:00 |
| 2 | Bamboo Water Bottle | Home & Kitchen | 24.99 | 50 | 1.8 | 50 | 2 | 2025-11-11 09:00:00 |
| 5 | Reusable Shopping Bag | Accessories | 9.99 | 200 | 1.2 | 50 | 2 | 2025-11-11 12:00:00 |

**USERS Table** (Current seller)
| id | username | role |
|----|----------|------|
| 2 | eco_seller | SELLER |

---

### 14. Update Product
**PUT** `/seller/update-product/{productId}`

**Path Variable:** `productId` - Product ID

#### Request Body
```json
{
  "price": 22.99,
  "stockQuantity": 75,
  "description": "Updated description - Eco-friendly bamboo water bottle"
}
```

#### Response (200 OK)
```json
"Product updated successfully!"
```

---

### 15. Delete Product
**DELETE** `/seller/delete-product/{productId}`

**Path Variable:** `productId` - Product ID

#### Response (200 OK)
```json
"Product deleted successfully!"
```

---

### 16. Get Orders for Seller's Products
**GET** `/seller/my-orders`

#### Response (200 OK)
```json
[
  {
    "orderId": 1,
    "productName": "Bamboo Water Bottle",
    "quantity": 3,
    "buyerUsername": "john_doe",
    "totalPrice": 74.97,
    "orderDate": "2025-11-12T14:00:00",
    "status": "PENDING"
  }
]
```

---

## Product Endpoints

### 17. Filter Products
**GET** `/api/products`

**Query Parameters:**
- `name` (optional) - Product name (partial match)
- `category` (optional) - Product category
- `minPrice` (optional) - Minimum price
- `maxPrice` (optional) - Maximum price
- `maxCarbon` (optional) - Maximum carbon footprint

#### Example Request
```
GET /api/products?category=Clothing&maxPrice=50&maxCarbon=5
```

#### Response (200 OK)
```json
[
  {
    "productId": 1,
    "name": "Organic Cotton T-Shirt",
    "description": "100% organic cotton t-shirt",
    "category": "Clothing",
    "price": 29.99,
    "stockQuantity": 100,
    "imageUrl": "https://example.com/tshirt.jpg",
    "carbonFootprint": 2.5,
    "carbonExplanation": "Low carbon footprint due to organic cotton and local production",
    "carbonPoints": 30,
    "volume": "1L",
    "weight": "0.2kg",
    "materialComposition": "100% Organic Cotton",
    "manufacturingLocation": "India",
    "electricityType": "Renewable",
    "manufacturingEnergyUsed": "3 kWh",
    "packagingDetails": "Recycled cardboard, no plastic",
    "shippingMode": "Sea Freight",
    "seaFreightDistance": 8000.0,
    "truckDistance": 200.0,
    "lifespan": "2 years",
    "powerUsage": "0 kWh",
    "recyclabilityRate": 95.0,
    "biodegradabilityRate": 100.0,
    "createdAt": "2025-11-10T08:00:00",
    "updatedAt": "2025-11-10T08:00:00"
  }
]
```

#### Database Tables Involved:

**PRODUCTS Table** (Filtered by query parameters)
| product_id | name | category | price | carbon_footprint | carbon_points | stock_quantity |
|------------|------|----------|-------|------------------|---------------|----------------|
| 1 | Organic Cotton T-Shirt | Clothing | 29.99 | 2.5 | 30 | 100 |
| 5 | Reusable Shopping Bag | Accessories | 9.99 | 1.2 | 50 | 200 |

---

## Cart Endpoints

**Note:** All cart endpoints require `Authorization: Bearer <token>` header.

### 18. Add to Cart
**POST** `/api/cart/add/{productId}/{quantity}`

**Path Variables:**
- `productId` - Product ID
- `quantity` - Quantity to add

#### Example Request
```
POST /api/cart/add/1/2
```

#### Response (200 OK)
```json
{
  "id": 1,
  "items": [
    {
      "id": 1,
      "productName": "Organic Cotton T-Shirt",
      "quantity": 2,
      "price": 29.99,
      "carbonFootprint": 2.5
    }
  ],
  "totalPrice": 59.98,
  "totalCarbon": 5.0
}
```

#### Database Tables Involved:

**CART Table** (Updated or created)
| id | user_id | total_price | total_carbon |
|----|---------|-------------|--------------|
| 1 | 1 | 59.98 | 5.0 |

**CART_ITEM Table** (New item added)
| id | cart_id | product_id | quantity | price | carbon_footprint |
|----|---------|------------|----------|-------|------------------|
| 1 | 1 | 1 | 2 | 29.99 | 2.5 |

**PRODUCTS Table** (Referenced for product details)
| product_id | name | price | carbon_footprint | stock_quantity |
|------------|------|-------|------------------|----------------|
| 1 | Organic Cotton T-Shirt | 29.99 | 2.5 | 100 |

---

### 19. View Cart
**GET** `/api/cart/view`

#### Response (200 OK)
```json
{
  "id": 1,
  "items": [
    {
      "id": 1,
      "productName": "Organic Cotton T-Shirt",
      "quantity": 2,
      "price": 29.99,
      "carbonFootprint": 2.5
    },
    {
      "id": 2,
      "productName": "Bamboo Water Bottle",
      "quantity": 1,
      "price": 24.99,
      "carbonFootprint": 1.8
    }
  ],
  "totalPrice": 84.97,
  "totalCarbon": 6.8
}
```

#### Database Tables Involved:

**CART Table**
| id | user_id | total_price | total_carbon |
|----|---------|-------------|--------------|
| 1 | 1 | 84.97 | 6.8 |

**CART_ITEM Table**
| id | cart_id | product_id | quantity | price | carbon_footprint |
|----|---------|------------|----------|-------|------------------|
| 1 | 1 | 1 | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | 1 | 24.99 | 1.8 |

---

### 20. Remove from Cart
**DELETE** `/api/cart/remove/{productId}`

**Path Variable:** `productId` - Product ID

#### Response (200 OK)
```json
{
  "id": 1,
  "items": [
    {
      "id": 2,
      "productName": "Bamboo Water Bottle",
      "quantity": 1,
      "price": 24.99,
      "carbonFootprint": 1.8
    }
  ],
  "totalPrice": 24.99,
  "totalCarbon": 1.8
}
```

---

### 21. Clear Cart
**DELETE** `/api/cart/clear`

#### Response (200 OK)
```json
"Cart cleared successfully for user: john_doe"
```

---

### 22. Update Cart Item Quantity
**PUT** `/api/cart/update/{productId}/{quantity}`

**Path Variables:**
- `productId` - Product ID
- `quantity` - New quantity

#### Example Request
```
PUT /api/cart/update/1/5
```

#### Response (200 OK)
```json
{
  "id": 1,
  "items": [
    {
      "id": 1,
      "productName": "Organic Cotton T-Shirt",
      "quantity": 5,
      "price": 29.99,
      "carbonFootprint": 2.5
    }
  ],
  "totalPrice": 149.95,
  "totalCarbon": 12.5
}
```

---

## Checkout Endpoints

**Note:** All checkout endpoints require `Authorization: Bearer <token>` header.

### 23. Checkout (Create Order)
**POST** `/api/checkout/add-checkout`

**No request body required** - Uses authenticated user's cart

#### Response (200 OK)
```json
{
  "id": 1,
  "totalPrice": 84.97,
  "totalCarbon": 6.8,
  "status": "PENDING",
  "createdAt": "2025-11-12T14:30:00",
  "items": [
    {
      "id": 1,
      "productName": "Organic Cotton T-Shirt",
      "quantity": 2,
      "priceAtPurchase": 29.99,
      "carbonAtPurchase": 2.5
    },
    {
      "id": 2,
      "productName": "Bamboo Water Bottle",
      "quantity": 1,
      "priceAtPurchase": 24.99,
      "carbonAtPurchase": 1.8
    }
  ]
}
```

#### Database Tables Involved:

**Flow:**
1. Read CART and CART_ITEM for current user
2. Create new ORDER record
3. Copy cart items to ORDER_ITEM
4. Clear CART and CART_ITEM
5. Update USER_PROFILES with carbon points

**ORDERS Table** (New order created)
| id | user_id | total_price | total_carbon | status | created_at |
|----|---------|-------------|--------------|--------|------------|
| 1 | 1 | 84.97 | 6.8 | PENDING | 2025-11-12 14:30:00 |

**ORDER_ITEM Table** (Items copied from cart)
| id | order_id | product_id | product_name | quantity | price_at_purchase | carbon_at_purchase |
|----|----------|------------|--------------|----------|-------------------|-------------------|
| 1 | 1 | 1 | Organic Cotton T-Shirt | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | Bamboo Water Bottle | 1 | 24.99 | 1.8 |

**CART Table** (Cleared after checkout)
| id | user_id | total_price | total_carbon |
|----|---------|-------------|--------------|
| 1 | 1 | 0.00 | 0.0 |

**USER_PROFILES Table** (Updated with carbon points)
| id | user_id | carbon_points | carbon_saved | badge |
|----|---------|---------------|--------------|-------|
| 1 | 1 | 80 | 6.8 | Bronze |

---

### 24. Get My Orders
**GET** `/api/checkout/my-orders`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "totalPrice": 84.97,
    "totalCarbon": 6.8,
    "status": "PENDING",
    "createdAt": "2025-11-12T14:30:00",
    "items": [
      {
        "id": 1,
        "productName": "Organic Cotton T-Shirt",
        "quantity": 2,
        "priceAtPurchase": 29.99,
        "carbonAtPurchase": 2.5
      }
    ]
  },
  {
    "id": 2,
    "totalPrice": 49.98,
    "totalCarbon": 3.6,
    "status": "DELIVERED",
    "createdAt": "2025-11-10T10:00:00",
    "items": [
      {
        "id": 3,
        "productName": "Bamboo Water Bottle",
        "quantity": 2,
        "priceAtPurchase": 24.99,
        "carbonAtPurchase": 1.8
      }
    ]
  }
]
```

#### Database Tables Involved:

**ORDERS Table** (Filtered by user_id)
| id | user_id | total_price | total_carbon | status | created_at |
|----|---------|-------------|--------------|--------|------------|
| 1 | 1 | 84.97 | 6.8 | PENDING | 2025-11-12 14:30:00 |
| 3 | 1 | 79.99 | 8.5 | SHIPPED | 2025-11-11 15:00:00 |

**ORDER_ITEM Table**
| id | order_id | product_id | product_name | quantity | price_at_purchase | carbon_at_purchase |
|----|----------|------------|--------------|----------|-------------------|-------------------|
| 1 | 1 | 1 | Organic Cotton T-Shirt | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | Bamboo Water Bottle | 1 | 24.99 | 1.8 |
| 4 | 3 | 3 | Recycled Plastic Shoes | 1 | 79.99 | 8.5 |

---

### 25. Update Order Status
**PUT** `/api/checkout/status/{orderId}?status=SHIPPED`

**Path Variable:** `orderId` - Order ID

**Query Parameter:** `status` - New status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)

#### Example Request
```
PUT /api/checkout/status/1?status=SHIPPED
```

#### Response (200 OK)
```json
{
  "id": 1,
  "totalPrice": 84.97,
  "totalCarbon": 6.8,
  "status": "SHIPPED",
  "createdAt": "2025-11-12T14:30:00"
}
```

---

## Carbon Footprint Endpoints

### 26. Estimate Carbon Footprint
**POST** `/api/carbon/estimate`

#### Request Body
```json
{
  "carbonDetails": {
    "productName": "Reusable Shopping Bag",
    "volume": "15L",
    "weight": "0.2kg",
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

#### Response (200 OK)
```json
{
  "estimatedCarbonFootprint": 1.2,
  "explanation": "The reusable shopping bag has a low carbon footprint of 1.2 kg CO₂e due to:\n- 100% recycled cotton material\n- Renewable energy in manufacturing\n- Minimal packaging\n- High recyclability (100%) and biodegradability (90%)\n- Long lifespan (3 years) reducing replacement frequency"
}
```

#### Database Tables Involved:

**CARBON_INSIGHT Table** (New record created if user is authenticated)
| id | user_id | product_name | estimated_carbon | recorded_at |
|----|---------|--------------|------------------|-------------|
| 6 | 1 | Reusable Shopping Bag | 1.2 | 2025-11-12 15:00:00 |

**Note:** This endpoint uses OpenAI API to calculate carbon footprint based on provided details. The AI analyzes material composition, manufacturing, transportation, and lifecycle factors.

---

### 27. Get User Carbon History
**GET** `/api/carbon/user/history`

#### Response (200 OK)
```json
{
  "username": "john_doe",
  "totalRecords": 5,
  "insights": [
    {
      "id": 1,
      "productName": "Organic Cotton T-Shirt",
      "estimatedCarbon": 2.5,
      "recordedAt": "2025-11-12T10:00:00"
    },
    {
      "id": 2,
      "productName": "Bamboo Water Bottle",
      "estimatedCarbon": 1.8,
      "recordedAt": "2025-11-12T11:30:00"
    }
  ]
}
```

#### Database Tables Involved:

**CARBON_INSIGHT Table** (Filtered by user_id, ordered by recorded_at DESC)
| id | user_id | product_name | estimated_carbon | recorded_at |
|----|---------|--------------|------------------|-------------|
| 1 | 1 | Organic Cotton T-Shirt | 2.5 | 2025-11-12 10:00:00 |
| 2 | 1 | Bamboo Water Bottle | 1.8 | 2025-11-12 11:30:00 |
| 4 | 1 | Recycled Plastic Shoes | 8.5 | 2025-11-11 14:00:00 |

**USERS Table**
| id | username |
|----|----------|
| 1 | john_doe |

---

## Leaderboard Endpoints

### 28. Get Top Eco Users
**GET** `/leaderboard/top`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "user": {
      "username": "eco_champion",
      "firstName": "Eco",
      "lastName": "Champion"
    },
    "carbonPoints": 250,
    "carbonSaved": 45.5,
    "badge": "Gold"
  },
  {
    "id": 2,
    "user": {
      "username": "john_doe",
      "firstName": "John",
      "lastName": "Doe"
    },
    "carbonPoints": 180,
    "carbonSaved": 32.0,
    "badge": "Silver"
  }
]
```

#### Database Tables Involved:

**USER_PROFILES Table** (Ordered by carbon_points DESC or carbon_saved DESC)
| id | user_id | carbon_points | carbon_saved | badge |
|----|---------|---------------|--------------|-------|
| 2 | 4 | 250 | 45.5 | Gold |
| 1 | 1 | 180 | 32.0 | Silver |
| 3 | 2 | 120 | 22.5 | Bronze |
| 4 | 5 | 90 | 15.0 | Bronze |

**USERS Table**
| id | username | first_name | last_name |
|----|----------|------------|-----------|
| 4 | jane_smith | Jane | Smith |
| 1 | john_doe | John | Doe |
| 2 | eco_seller | Eco | Seller |
| 5 | green_seller | Green | Seller |

---

### 29. Get User Rank
**GET** `/leaderboard/rank/{userId}`

**Path Variable:** `userId` - User ID

#### Response (200 OK)
```json
{
  "rank": 5,
  "carbonPoints": 120,
  "carbonSaved": 22.5,
  "badge": "Bronze",
  "username": "john_doe"
}
```

---

### 30. Get Top by Carbon Points
**GET** `/leaderboard/top-points`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "carbonPoints": 250,
    "carbonSaved": 45.5,
    "badge": "Gold"
  }
]
```

---

### 31. Get Top by Carbon Saved
**GET** `/leaderboard/top-carbon`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "carbonPoints": 250,
    "carbonSaved": 45.5,
    "badge": "Gold"
  }
]
```

---

### 32. Get User Stats
**GET** `/leaderboard/user/{username}`

**Path Variable:** `username` - Username

#### Response (200 OK)
```json
{
  "username": "john_doe",
  "rank": 5,
  "carbonPoints": 120,
  "carbonSaved": 22.5,
  "badge": "Bronze",
  "totalOrders": 8
}
```

#### Database Tables Involved:

**USER_PROFILES Table** (Joined with USERS)
| id | user_id | carbon_points | carbon_saved | badge |
|----|---------|---------------|--------------|-------|
| 1 | 1 | 180 | 32.0 | Silver |

**USERS Table**
| id | username | first_name | last_name |
|----|----------|------------|-----------|
| 1 | john_doe | John | Doe |

**ORDERS Table** (Count for totalOrders)
| id | user_id | status |
|----|---------|--------|
| 1 | 1 | PENDING |
| 3 | 1 | SHIPPED |

---

## Complete Database Schema Reference

This section provides a complete overview of all database tables with sample data for reference.

### **USERS** Table
| id | username | password | email | first_name | last_name | phone | role | created_at |
|----|----------|----------|-------|------------|-----------|-------|------|------------|
| 1 | john_doe | $2a$10$xyz... | john.doe@example.com | John | Doe | 1234567890 | USER | 2025-11-12 10:30:00 |
| 2 | eco_seller | $2a$10$abc... | seller@ecobazaar.com | Eco | Seller | 5551234567 | SELLER | 2025-11-12 11:00:00 |
| 3 | admin_user | $2a$10$def... | admin@ecobazaar.com | Admin | User | 9876543210 | ADMIN | 2025-11-12 09:00:00 |
| 4 | jane_smith | $2a$10$ghi... | jane@example.com | Jane | Smith | 9998887777 | USER | 2025-11-12 12:00:00 |
| 5 | green_seller | $2a$10$jkl... | green@ecobazaar.com | Green | Seller | 4445556666 | SELLER | 2025-11-12 13:00:00 |

### **PRODUCTS** Table
| product_id | name | description | category | price | stock_quantity | carbon_footprint | carbon_points | posted_by |
|------------|------|-------------|----------|-------|----------------|------------------|---------------|-----------|
| 1 | Organic Cotton T-Shirt | 100% organic cotton t-shirt | Clothing | 29.99 | 100 | 2.5 | 30 | 2 |
| 2 | Bamboo Water Bottle | Eco-friendly bamboo water bottle | Home & Kitchen | 24.99 | 50 | 1.8 | 50 | 2 |
| 3 | Recycled Plastic Shoes | Shoes made from recycled ocean plastic | Footwear | 79.99 | 30 | 8.5 | 10 | 5 |
| 4 | Solar Charger | Portable solar panel charger | Electronics | 49.99 | 25 | 12.0 | 5 | 5 |
| 5 | Reusable Shopping Bag | 100% recycled cotton bag | Accessories | 9.99 | 200 | 1.2 | 50 | 2 |

### **CART & CART_ITEM** Tables
**CART:**
| id | user_id | total_price | total_carbon |
|----|---------|-------------|--------------|
| 1 | 1 | 84.97 | 6.8 |
| 2 | 4 | 34.98 | 4.3 |

**CART_ITEM:**
| id | cart_id | product_id | quantity | price | carbon_footprint |
|----|---------|------------|----------|-------|------------------|
| 1 | 1 | 1 | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | 1 | 24.99 | 1.8 |
| 3 | 2 | 5 | 3 | 9.99 | 1.2 |

### **ORDERS & ORDER_ITEM** Tables
**ORDERS:**
| id | user_id | total_price | total_carbon | status | created_at |
|----|---------|-------------|--------------|--------|------------|
| 1 | 1 | 84.97 | 6.8 | PENDING | 2025-11-12 14:30:00 |
| 2 | 4 | 49.98 | 3.6 | DELIVERED | 2025-11-10 10:00:00 |
| 3 | 1 | 79.99 | 8.5 | SHIPPED | 2025-11-11 15:00:00 |

**ORDER_ITEM:**
| id | order_id | product_id | product_name | quantity | price_at_purchase | carbon_at_purchase |
|----|----------|------------|--------------|----------|-------------------|-------------------|
| 1 | 1 | 1 | Organic Cotton T-Shirt | 2 | 29.99 | 2.5 |
| 2 | 1 | 2 | Bamboo Water Bottle | 1 | 24.99 | 1.8 |
| 3 | 2 | 2 | Bamboo Water Bottle | 2 | 24.99 | 1.8 |
| 4 | 3 | 3 | Recycled Plastic Shoes | 1 | 79.99 | 8.5 |

### **USER_PROFILES** Table (Leaderboard)
| id | user_id | carbon_points | carbon_saved | badge |
|----|---------|---------------|--------------|-------|
| 1 | 1 | 180 | 32.0 | Silver |
| 2 | 4 | 250 | 45.5 | Gold |
| 3 | 2 | 120 | 22.5 | Bronze |
| 4 | 5 | 90 | 15.0 | Bronze |

### **CARBON_INSIGHT** Table (Carbon Tracking History)
| id | user_id | product_name | estimated_carbon | recorded_at |
|----|---------|--------------|------------------|-------------|
| 1 | 1 | Organic Cotton T-Shirt | 2.5 | 2025-11-12 10:00:00 |
| 2 | 1 | Bamboo Water Bottle | 1.8 | 2025-11-12 11:30:00 |
| 3 | 4 | Reusable Shopping Bag | 1.2 | 2025-11-12 09:00:00 |
| 4 | 1 | Recycled Plastic Shoes | 8.5 | 2025-11-11 14:00:00 |

---

## Enum Values

### Role Enum
- `USER`
- `SELLER`
- `ADMIN`

### OrderStatus Enum
- `PENDING`
- `CONFIRMED`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

---

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token_here>
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request data"
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

### 403 Forbidden
```json
{
  "error": "Access denied"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error occurred"
}
```

---

## Notes

1. **Password Security**: All passwords are hashed using BCrypt before storage
2. **JWT Token Expiration**: Tokens expire after 30 hours (108000000 ms)
3. **Carbon Points Calculation**:
   - Carbon footprint ≤ 1 kg CO₂e: 50 points
   - Carbon footprint ≤ 3 kg CO₂e: 30 points
   - Carbon footprint ≤ 5 kg CO₂e: 20 points
   - Carbon footprint ≤ 10 kg CO₂e: 10 points
   - Carbon footprint > 10 kg CO₂e: 5 points
4. **Database**: PostgreSQL (Supabase)
5. **Base URL**: Update to your deployed URL in production

---

**Last Updated:** November 12, 2025
