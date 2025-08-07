-- SQL Script สำหรับสร้างตารางทั้งสาม
-- Database: home_work

-- ====================================
-- 1. สร้างตาราง owners
-- ====================================
CREATE TABLE IF NOT EXISTS owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT 'ชื่อเจ้าของ',
    sur_name VARCHAR(50) NOT NULL COMMENT 'นามสกุลเจ้าของ',
    address VARCHAR(255) NOT NULL COMMENT 'ที่อยู่เจ้าของ',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ตารางเจ้าของฟาร์ม';

-- ====================================
-- 2. สร้างตาราง farms
-- ====================================
CREATE TABLE IF NOT EXISTS farms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'ชื่อฟาร์ม',
    owner_id BIGINT NOT NULL COMMENT 'ID ของเจ้าของฟาร์ม',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraint
    CONSTRAINT fk_farms_owner_id 
        FOREIGN KEY (owner_id) REFERENCES owners(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
        
    -- Index for performance
    INDEX idx_farms_owner_id (owner_id),
    INDEX idx_farms_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ตารางฟาร์ม';

-- ====================================
-- 3. สร้างตาราง cows
-- ====================================
CREATE TABLE IF NOT EXISTS cows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cow_name VARCHAR(50) NOT NULL COMMENT 'ชื่อวัว',
    birth DATE NOT NULL COMMENT 'วันเกิดวัว',
    type VARCHAR(50) NOT NULL COMMENT 'ประเภทวัว',
    farm_id BIGINT NOT NULL COMMENT 'ID ของฟาร์ม',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraint
    CONSTRAINT fk_cows_farm_id 
        FOREIGN KEY (farm_id) REFERENCES farms(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
        
    -- Index for performance
    INDEX idx_cows_farm_id (farm_id),
    INDEX idx_cows_name (cow_name),
    INDEX idx_cows_type (type),
    INDEX idx_cows_birth (birth)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ตารางวัว';

-- ====================================
-- 4. สร้างข้อมูลตัวอย่าง (Sample Data)
-- ====================================

-- Insert sample owners
INSERT IGNORE INTO owners (id, name, sur_name, address) VALUES
(1, 'สมชาย', 'รักษาดิน', '123 หมู่ 1 ตำบลหนองแก อำเภอเมือง จังหวัดกำแพงเพชร'),
(2, 'สมหญิง', 'ใจดี', '456 หมู่ 2 ตำบลบางกระเทียม อำเภอเมือง จังหวัดสุพรรณบุรี'),
(3, 'สมศักดิ์', 'มั่นคง', '789 หมู่ 3 ตำบลโคกหินลาด อำเภอเมือง จังหวัดอุดรธานี');

-- Insert sample farms
INSERT IGNORE INTO farms (id, name, owner_id) VALUES
(1, 'ฟาร์มสมชายมีความสุข', 1),
(2, 'ฟาร์มอินทรีย์สมหญิง', 2),
(3, 'ฟาร์มโคนมสมศักดิ์', 3),
(4, 'ฟาร์มหลังบ้านสมชาย', 1);

-- Insert sample cows
INSERT IGNORE INTO cows (id, cow_name, birth, type, farm_id) VALUES
(1, 'แดงเด่น', '2020-03-15', 'โคนม', 1),
(2, 'ขาวขจร', '2019-08-22', 'โคเนื้อ', 1),
(3, 'นางฟ้า', '2021-01-10', 'โคนม', 2),
(4, 'เทพธิดา', '2020-11-05', 'โคนม', 2),
(5, 'ราชสีห์', '2019-12-18', 'โคเนื้อ', 3),
(6, 'มหาราช', '2021-06-30', 'โคเนื้อ', 3),
(7, 'สวยงาม', '2020-09-12', 'โคนม', 4),
(8, 'งามสง่า', '2021-04-25', 'โคพันธุ์', 1);

-- ====================================
-- 5. สร้าง Views สำหรับการ Query ที่ซับซ้อน
-- ====================================

-- View สำหรับดูข้อมูลวัวพร้อมรายละเอียดฟาร์มและเจ้าของ
CREATE OR REPLACE VIEW vw_cows_with_details AS
SELECT 
    c.id AS cow_id,
    c.cow_name,
    c.birth,
    c.type,
    YEAR(CURDATE()) - YEAR(c.birth) AS age_years,
    f.id AS farm_id,
    f.name AS farm_name,
    o.id AS owner_id,
    o.name AS owner_name,
    o.sur_name AS owner_surname,
    CONCAT(o.name, ' ', o.sur_name) AS owner_full_name,
    o.address AS owner_address
FROM cows c
INNER JOIN farms f ON c.farm_id = f.id
INNER JOIN owners o ON f.owner_id = o.id
ORDER BY c.cow_name;

-- View สำหรับสถิติฟาร์ม
CREATE OR REPLACE VIEW vw_farm_statistics AS
SELECT 
    f.id AS farm_id,
    f.name AS farm_name,
    o.name AS owner_name,
    o.sur_name AS owner_surname,
    CONCAT(o.name, ' ', o.sur_name) AS owner_full_name,
    COUNT(c.id) AS total_cows,
    COUNT(CASE WHEN c.type = 'โคนม' THEN 1 END) AS dairy_cows,
    COUNT(CASE WHEN c.type = 'โคเนื้อ' THEN 1 END) AS beef_cows,
    COUNT(CASE WHEN c.type = 'โคพันธุ์' THEN 1 END) AS breeding_cows
FROM farms f
INNER JOIN owners o ON f.owner_id = o.id
LEFT JOIN cows c ON f.id = c.farm_id
GROUP BY f.id, f.name, o.id, o.name, o.sur_name
ORDER BY total_cows DESC;

-- ====================================
-- 6. สร้าง Indexes เพิ่มเติมสำหรับ Performance
-- ====================================

-- Composite indexes for better query performance
CREATE INDEX idx_cows_farm_name ON cows(farm_id, cow_name);
CREATE INDEX idx_cows_type_birth ON cows(type, birth);
CREATE INDEX idx_farms_owner_name ON farms(owner_id, name);

-- ====================================
-- 7. Query Examples สำหรับทดสอบ
-- ====================================

-- ตัวอย่างการ Query วัวทั้งหมดในฟาร์ม
-- SELECT * FROM vw_cows_with_details WHERE farm_id = 1;

-- ตัวอย่างการค้นหาวัวตามชื่อ
-- SELECT * FROM vw_cows_with_details WHERE cow_name LIKE '%แดง%';

-- ตัวอย่างการดูสถิติฟาร์ม
-- SELECT * FROM vw_farm_statistics;

-- ตัวอย่างการนับวัวแต่ละประเภท
-- SELECT type, COUNT(*) as count FROM cows GROUP BY type;

-- ตัวอย่างการหาวัวที่อายุมากกว่า 3 ปี
-- SELECT cow_name, birth, age_years FROM vw_cows_with_details WHERE age_years > 3;
