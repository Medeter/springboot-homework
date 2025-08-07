# API Testing Examples for Spring Boot Homework

## 🐄 Cow Management API Examples

### 1. **GET /api/getCowList/{farm}** - ดูข้อมูลฟาร์มและรายชื่อวัวในฟาร์ม

```bash
GET http://localhost:8080/api/getCowList/1
```

**Response Example:**
```json
{
  "success": true,
  "message": "✅ ดึงข้อมูลฟาร์ม 'ฟาร์มสมชายมีความสุข' และวัวในฟาร์มสำเร็จ",
  "farm": {
    "id": 1,
    "name": "ฟาร์มสมชายมีความสุข",
    "owner": {
      "id": 1,
      "name": "สมชาย",
      "surname": "รักษาดิน",
      "address": "123 หมู่ 1 ตำบลหนองแก อำเภอเมือง จังหวัดกำแพงเพชร",
      "fullName": "สมชาย รักษาดิน"
    },
    "cows": [
      {
        "id": 1,
        "name": "แดงเด่น",
        "birth": "2020-03-15",
        "type": "โคนม",
        "age": 5
      },
      {
        "id": 2,
        "name": "ขาวขจร",
        "birth": "2019-08-22",
        "type": "โคเนื้อ",
        "age": 6
      },
      {
        "id": 8,
        "name": "งามสง่า",
        "birth": "2021-04-25",
        "type": "โคพันธุ์",
        "age": 4
      }
    ],
    "cowCount": 3
  }
}
```

### 2. **POST /api/cowSearch** - ค้นหาวัวพร้อมข้อมูลฟาร์ม

```bash
POST http://localhost:8080/api/cowSearch
Content-Type: application/json

{
  "cowName": "แดง"
}
```

**Response Example:**
```json
{
  "success": true,
  "message": "✅ พบวัวที่มีชื่อ 'แดง' จำนวน 1 ตัว",
  "cows": [
    {
      "id": 1,
      "name": "แดงเด่น",
      "birth": "2020-03-15",
      "type": "โคนม",
      "age": 5,
      "farm": {
        "id": 1,
        "name": "ฟาร์มสมชายมีความสุข",
        "owner": {
          "id": 1,
          "name": "สมชาย",
          "surname": "รักษาดิน",
          "address": "123 หมู่ 1 ตำบลหนองแก อำเภอเมือง จังหวัดกำแพงเพชร",
          "fullName": "สมชาย รักษาดิน"
        }
      }
    }
  ],
  "count": 1,
  "searchTerm": "แดง"
}
```

### 3. **GET /api/cows** - ดูวัวทั้งหมดพร้อมข้อมูลฟาร์ม

```bash
GET http://localhost:8080/api/cows
```

**Response Example:**
```json
{
  "success": true,
  "message": "✅ ดึงข้อมูลวัวทั้งหมดพร้อมข้อมูลฟาร์มสำเร็จ",
  "cows": [
    {
      "id": 1,
      "name": "แดงเด่น",
      "birth": "2020-03-15",
      "type": "โคนม",
      "age": 5,
      "farm": {
        "id": 1,
        "name": "ฟาร์มสมชายมีความสุข",
        "owner": {
          "id": 1,
          "name": "สมชาย",
          "surname": "รักษาดิน",
          "address": "123 หมู่ 1 ตำบลหนองแก อำเภอเมือง จังหวัดกำแพงเพชร",
          "fullName": "สมชาย รักษาดิน"
        }
      }
    },
    {
      "id": 3,
      "name": "นางฟ้า",
      "birth": "2021-01-10",
      "type": "โคนม",
      "age": 4,
      "farm": {
        "id": 2,
        "name": "ฟาร์มอินทรีย์สมหญิง",
        "owner": {
          "id": 2,
          "name": "สมหญิง",
          "surname": "ใจดี",
          "address": "456 หมู่ 2 ตำบลบางกระเทียม อำเภอเมือง จังหวัดสุพรรณบุรี",
          "fullName": "สมหญิง ใจดี"
        }
      }
    }
  ],
  "count": 8
}
```

### 4. **POST /api/cowSearch** - ค้นหาวัวทั้งหมด (ไม่ระบุชื่อ)

```bash
POST http://localhost:8080/api/cowSearch
Content-Type: application/json

{
  "cowName": ""
}
```

**หรือ**

```bash
POST http://localhost:8080/api/cowSearch
Content-Type: application/json

{}
```

---

## 🎯 ข้อมูลที่แสดงในแต่ละ API

### **ข้อมูลวัว (Cow Information):**
- `id` - ID ของวัว
- `cowName` - ชื่อวัว
- `birth` - วันเกิดวัว
- `type` - ประเภทวัว (โคนม, โคเนื้อ, โคพันธุ์)
- `age` - อายุวัว (คำนวณจากวันเกิด)

### **ข้อมูลฟาร์ม (Farm Information):**
- `farmId` - ID ของฟาร์ม
- `farmName` - ชื่อฟาร์ม

### **ข้อมูลเจ้าของ (Owner Information):**
- `ownerId` - ID ของเจ้าของ
- `ownerName` - ชื่อเจ้าของ
- `ownerSurName` - นามสกุลเจ้าของ
- `ownerFullName` - ชื่อเต็มของเจ้าของ
- `ownerAddress` - ที่อยู่เจ้าของ

---

## 🔍 การทดสอบเพิ่มเติม

### **ทดสอบ Edge Cases:**

1. **ฟาร์มที่ไม่มีวัว:**
```bash
GET http://localhost:8080/api/getCowList/999
```

2. **ค้นหาวัวที่ไม่มี:**
```bash
POST http://localhost:8080/api/cowSearch
Content-Type: application/json

{
  "cowName": "ไม่มีวัวชื่อนี้"
}
```

3. **ฟาร์มอื่นๆ:**
```bash
GET http://localhost:8080/api/getCowList/2  # ฟาร์มสมหญิง
GET http://localhost:8080/api/getCowList/3  # ฟาร์มสมศักดิ์
GET http://localhost:8080/api/getCowList/4  # ฟาร์มหลังบ้านสมชาย
```

---

## 🗄️ วิธีการ Setup Database

### 1. รัน SQL Script:
```sql
-- รันไฟล์ database_setup.sql ใน MySQL
source d:/springboot-homework/database_setup.sql;
```

### 2. ตรวจสอบข้อมูล:
```sql
-- ดูข้อมูลทั้งหมด
SELECT * FROM vw_cows_with_details;

-- ดูสถิติฟาร์ม
SELECT * FROM vw_farm_statistics;
```

---

## 📚 Student Management API Examples

### 1. **GET /student** - ดูนักเรียนทั้งหมด

```bash
GET http://localhost:8080/student
```

### 2. **GET /student/filter** - ค้นหานักเรียนแบบครอบคลุม

```bash
GET http://localhost:8080/student/filter?name=สม&minAge=20&maxAge=25&sortBy=age&sortDir=asc
```

### 3. **POST /student** - เพิ่มนักเรียนใหม่

```bash
POST http://localhost:8080/student
Content-Type: application/x-www-form-urlencoded

name=สมชาย&age=22&phone=0812345678&email=somchai@example.com
```

### 4. **GET /student/age** - ค้นหาตามอายุและชื่อ

```bash
GET http://localhost:8080/student/age?name=สม&minAge=20&maxAge=23&sortBy=name&sortDir=asc
```
