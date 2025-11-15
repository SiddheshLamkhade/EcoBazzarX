# Quick Test Guide - Authentication Integration

## 🚀 Quick Start

### 1. Start Backend (Terminal 1)
```powershell
cd d:\EcoBazaar
.\mvnw.cmd spring-boot:run
```
Wait for: `Started EcoBazaarXApplication in X seconds`

### 2. Start Frontend (Terminal 2)
```powershell
cd d:\EcoBazaar\FrontEnd
npm run dev
```
Open: http://localhost:3000

## 🧪 Test Scenarios

### Test 1: Register New User
1. Go to http://localhost:3000/signup
2. Fill form:
   ```
   Name: Test User
   Email: testuser@example.com
   Phone: 1234567890
   Password: Test@123456
   Confirm Password: Test@123456
   Role: Customer (USER)
   ```
3. Click "Create Account"
4. ✅ Should redirect to `/user` dashboard
5. Check localStorage: `ecobazaarx_token` and `ecobazaarx_user`

### Test 2: Register Seller
1. Go to http://localhost:3000/signup
2. Fill form with Role: "Seller (SELLER)"
3. ✅ Should see: "Account created! Your seller account is pending admin approval."
4. ✅ Should redirect to `/seller` dashboard

### Test 3: Login with Email
1. Go to http://localhost:3000/login
2. Email Tab:
   ```
   Email: testuser@example.com
   Password: Test@123456
   ```
3. ✅ Should redirect to `/user` dashboard

### Test 4: Login Admin
1. Go to http://localhost:3000/login
2. Email Tab:
   ```
   Email: admin@ecobazaarx.com
   Password: EcoAdmin@2024
   ```
3. ✅ Should redirect to `/admin` dashboard

### Test 5: Error Cases
1. Try duplicate email → ❌ "Email already exists"
2. Try wrong password → ❌ "Invalid email or password"
3. Try weak password → ❌ "Please choose a stronger password"
4. Try non-matching passwords → ❌ "Passwords do not match"

## 🔍 Debug Checklist

### Backend Health Check
```powershell
# Check if backend is running
curl http://localhost:8080
```

### Frontend Proxy Check
- Open DevTools → Network tab
- Login attempt should show:
  - Request: `http://localhost:3000/api/auth/login`
  - Proxied to: `http://localhost:8080/login`

### Database Check
```sql
-- Check registered users
SELECT id, username, email, role, created_at FROM users;
```

### Browser Console
Look for:
- ✅ "Login successful!"
- ✅ Token in localStorage
- ❌ Any CORS errors
- ❌ 401 Unauthorized
- ❌ 500 Server errors

## 📊 Expected API Responses

### Successful Registration
```json
{
  "success": true,
  "message": "Registration successful",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "testuser",
    "email": "testuser@example.com",
    "firstName": "Test",
    "lastName": "User",
    "phone": "1234567890",
    "role": "USER",
    "createdAt": "2025-11-15T..."
  }
}
```

### Successful Login
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": { /* same as registration */ }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Email already exists",
  "token": null,
  "user": null
}
```

## 🎯 Integration Points Verified

✅ Frontend sends correct request format  
✅ Vite proxy routes to backend  
✅ Backend validates and processes request  
✅ JWT token generated and returned  
✅ Token stored in localStorage  
✅ User data mapped correctly  
✅ Role-based redirect works  
✅ Error messages displayed via toast  
✅ Session management active  

## 📞 Support

If issues persist:
1. Check backend logs in Terminal 1
2. Check browser DevTools Console
3. Verify database connection
4. Confirm ports 3000 & 8080 are available
