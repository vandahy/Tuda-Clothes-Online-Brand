# 🚀 Hướng dẫn Deploy TudaBrand lên Railway

## 📋 Các file đã được chuẩn bị

### Backend:
- ✅ `application-prod.properties` - Cấu hình production với biến môi trường
- ✅ `system.properties` - Chỉ định Java 17
- ✅ `railway.json` - Cấu hình build và deploy
- ✅ `.railwayignore` - Loại bỏ file không cần thiết
- ✅ `SecurityConfig.java` - CORS cho Railway + Swagger UI enabled

### Frontend:
- ✅ `.env` - Development environment
- ✅ `.env.production` - Production environment (cần update URL)
- ✅ `.env.example` - Template
- ✅ `api.js` - Tự động dùng biến môi trường

---

## 🎯 Bước 1: Deploy Backend + Database trên Railway

### 1.1. Tạo tài khoản Railway
1. Vào https://railway.app
2. Đăng ký/Đăng nhập bằng GitHub
3. Verify email nếu cần

### 1.2. Tạo Project mới
1. Click **"New Project"**
2. Chọn **"Deploy from GitHub repo"**
3. Authorize Railway truy cập GitHub của bạn
4. Chọn repository **TudaBrand**

### 1.3. Thêm MySQL Database
1. Trong project, click **"+ New"** → **"Database"** → **"Add MySQL"**
2. Railway sẽ tự động tạo MySQL instance
3. Copy các thông tin kết nối (sẽ dùng ở bước sau)

### 1.4. Cấu hình Backend Service
1. Click vào service **backend** (hoặc tên repo)
2. Vào tab **"Settings"**:
   - **Root Directory**: `/backend`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`

3. Vào tab **"Variables"**:
   Click **"Add Variable"** và thêm:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=true&serverTimezone=UTC
   DATABASE_USER=${{MySQL.MYSQLUSER}}
   DATABASE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   PORT=8080
   ```
   
   **Lưu ý**: Railway tự động thay thế `${{MySQL.*}}` bằng giá trị thực tế

4. Vào tab **"Deployments"**:
   - Click **"Redeploy"** nếu cần
   - Chờ build hoàn tất (3-5 phút)

### 1.5. Lấy URL Backend
1. Sau khi deploy thành công, vào tab **"Settings"**
2. Scroll xuống **"Networking"** → **"Generate Domain"**
3. Copy URL (ví dụ: `https://tudabrand-backend-production.up.railway.app`)

### 1.6. Import Database Schema
**Cách 1: Từ Railway CLI**
```bash
# Cài Railway CLI
npm i -g @railway/cli

# Login
railway login

# Link project
railway link

# Connect MySQL
railway run mysql -u $MYSQLUSER -p$MYSQLPASSWORD -h $MYSQLHOST -P $MYSQLPORT $MYSQLDATABASE < your-database.sql
```

**Cách 2: Qua Railway Web UI**
1. Vào MySQL service → Tab "Data"
2. Chọn "Query" → Paste SQL schema
3. Execute

**Cách 3: MySQL Workbench/DBeaver**
- Dùng thông tin từ Railway MySQL variables để connect từ máy local
- Import file SQL thông thường

---

## 🎨 Bước 2: Deploy Frontend (2 lựa chọn)

### **Option A: Deploy Frontend trên Railway (Đơn giản hơn)**

1. Trong cùng project Railway, click **"+ New"** → **"Empty Service"**
2. Connect GitHub repo (cùng repo TudaBrand)
3. Settings:
   - **Root Directory**: `/frontend`
   - **Build Command**: `npm install && npm run build`
   - **Start Command**: `npm run preview -- --host 0.0.0.0 --port $PORT`

4. Variables:
   ```
   VITE_API_URL=https://your-backend-url.up.railway.app
   ```
   (Thay bằng URL backend từ bước 1.5)

5. Generate Domain cho frontend
6. Truy cập và test!

### **Option B: Deploy Frontend trên Vercel (Tách biệt)**

1. Vào https://vercel.com → Login bằng GitHub
2. Click **"Add New"** → **"Project"**
3. Import repository **TudaBrand**
4. Settings:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   
5. Environment Variables:
   ```
   VITE_API_URL=https://your-backend-url.up.railway.app
   ```

6. Deploy!

---

## 🔧 Bước 3: Cập nhật CORS (Nếu dùng Option B - Vercel)

Nếu deploy frontend trên Vercel, cần update CORS trong `SecurityConfig.java`:

```java
config.setAllowedOriginPatterns(List.of(
    "http://localhost:*",
    "http://127.0.0.1:*",
    "https://*.railway.app",
    "https://*.up.railway.app",
    "https://*.vercel.app"  // Thêm dòng này
));
```

Sau đó commit và push → Railway tự động redeploy.

---

## ✅ Bước 4: Kiểm tra

### Backend:
1. Truy cập: `https://your-backend.up.railway.app/api/products`
2. Swagger UI: `https://your-backend.up.railway.app/swagger-ui/index.html`

### Frontend:
1. Truy cập URL từ Railway/Vercel
2. Test đăng ký/đăng nhập
3. Test CRUD operations

---

## 🐛 Troubleshooting

### Backend không chạy được:
```bash
# Xem logs trong Railway UI
# Hoặc dùng CLI:
railway logs
```

**Lỗi thường gặp:**
- ❌ Database connection: Kiểm tra biến môi trường `DATABASE_*`
- ❌ Port binding: Đảm bảo có `PORT` variable
- ❌ Build failed: Check Java version (cần Java 17)

### Frontend không kết nối được backend:
- ✅ Kiểm tra `VITE_API_URL` trong Environment Variables
- ✅ Kiểm tra CORS trong `SecurityConfig.java`
- ✅ Rebuild frontend sau khi đổi env var

### CORS Error:
- ✅ Thêm domain frontend vào `SecurityConfig.java`
- ✅ Đảm bảo `withCredentials: true` trong `api.js`

---

## 💰 Chi phí

**Railway Free Tier:**
- $5 credit/tháng
- ~ 500 hours runtime
- Đủ cho dev/testing

**Vercel Free Tier:**
- Unlimited deployments
- 100GB bandwidth/tháng

---

## 📚 Resources

- [Railway Docs](https://docs.railway.app)
- [Vercel Docs](https://vercel.com/docs)
- [Railway MySQL Guide](https://docs.railway.app/databases/mysql)

---

## 🎉 Hoàn tất!

Sau khi hoàn thành, bạn sẽ có:
- ✅ Backend Java Spring Boot trên Railway
- ✅ MySQL Database trên Railway
- ✅ Frontend Vue.js trên Railway/Vercel
- ✅ HTTPS tự động
- ✅ CI/CD tự động (mỗi lần push code)

**URL để share:**
- Frontend: `https://your-app.up.railway.app` hoặc `https://your-app.vercel.app`
- API: `https://your-backend.up.railway.app`
- Swagger: `https://your-backend.up.railway.app/swagger-ui/index.html`
