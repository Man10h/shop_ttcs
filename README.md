
# TEST API
Deploy Webservice Render: https://shop-ttcs-b6zr.onrender.com/

Database: FreeSQLDatabase 

## 1. Trang chủ

```bash
curl -X GET https://shop-ttcs-b6zr.onrender.com/home/hello
```

Trả về chuỗi (string)

---

## 2. Đăng nhập

```bash
curl -X POST -H "Content-Type: application/json" -d "{\"username\": \"nguyenmanhlc10\", \"password\": \"1\"}" https://shop-ttcs-b6zr.onrender.com/home/login
```

Trả về token (string)

---

## 3. Đăng ký

```bash
curl -X POST -H "Content-Type: application/json" -d '{
    \"username\":\"user\",
    \"firstName\":\"user\",
    \"lastName\":\"name\",
    \"password\":\"1\",
    \"confirmPassword\":\"1\",
    \"age\":\"20\",
    \"sex\":\"male\",
    \"phoneNumber\":\"04313\",
    \"email\":\"youremail@gmail.com\",
    \"roleId\":\"2\"
}' https://shop-ttcs-b6zr.onrender.com/home/register
```

Trả về chuỗi thông báo đăng kí thành công hay thất bại

---

## 4. Lấy thông tin từ token

```bash
curl -X GET https://shop-ttcs-b6zr.onrender.com/home/infoToken?token=TOKEN
```

Trả về JSON thông tin người dùng

---

## 5. Verify người dùng

```bash
curl -X GET https://shop-ttcs-b6zr.onrender.com/home/verify?email=youremail@gmail.com&verificationCode=yourcode
```

Trả về chuỗi thông báo verify thành công hay thất bại

---

