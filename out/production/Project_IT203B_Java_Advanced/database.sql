
-- HỆ THỐNG QUẢN LÝ ĐẶT PHÒNG HỌP & DỊCH VỤ VĂN PHÒNG
-- Database: meeting_db

-- Xóa database cũ nếu tồn tại (tuỳ chọn)
DROP DATABASE IF EXISTS meeting_db;

-- Tạo database mới
CREATE DATABASE IF NOT EXISTS meeting_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE meeting_db;


-- BẢNG NGƯỜI DÙNG (Users)
-- Vai trò: EMPLOYEE (Nhân viên), SUPPORT_STAFF (Nhân viên hỗ trợ), ADMIN (Quản trị viên)

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã hash (SHA-256)',
    role VARCHAR(20) NOT NULL COMMENT 'EMPLOYEE, SUPPORT_STAFF, ADMIN',
    fullname VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE'
) COMMENT 'Bảng quản lý người dùng hệ thống';


-- BẢNG PHÒNG HỌP (Rooms)

CREATE TABLE IF NOT EXISTS rooms (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    capacity INT NOT NULL COMMENT 'Sức chứa phòng',
    location VARCHAR(100) NOT NULL,
    fixed_equipment VARCHAR(255) COMMENT 'Thiết bị cố định (máy chiếu, bảng trắng...)',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT 'AVAILABLE, MAINTENANCE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT 'Bảng quản lý phòng họp';


-- BẢNG THIẾT BỊ DI ĐỘNG (Equipment)

CREATE TABLE IF NOT EXISTS equipment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    total_quantity INT NOT NULL,
    available_quantity INT NOT NULL COMMENT 'Số lượng khả dụng hiện tại',
    status VARCHAR(50) NOT NULL COMMENT 'ACTIVE, INACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT 'Bảng quản lý thiết bị di động';


-- BẢNG DỊCH VỤ (Services)

CREATE TABLE IF NOT EXISTS services (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    price DOUBLE NOT NULL COMMENT 'Giá dịch vụ',
    description VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT 'Bảng quản lý dịch vụ (nước, trà, bánh, vệ sinh...)';


-- BẢNG ĐẶT PHÒNG (Bookings)
-- Trạng thái: PENDING (chờ duyệt), APPROVED (đã duyệt), REJECTED (từ chối), DONE (hoàn thành)

CREATE TABLE IF NOT EXISTS bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED, DONE',
    support_staff_id INT COMMENT 'Nhân viên hỗ trợ được phân công',
    participant_count INT COMMENT 'Số người tham dự dự kiến',
    notes VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (support_staff_id) REFERENCES users(id) ON DELETE SET NULL
) COMMENT 'Bảng quản lý đặt phòng';

-- BẢNG CHI TIẾT DỊCH VỤ ĐẶT PHÒNG (Booking_Services)
CREATE TABLE IF NOT EXISTS booking_services (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    service_id INT NOT NULL,
    quantity INT NOT NULL COMMENT 'Số lượng dịch vụ',
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
) COMMENT 'Bảng chi tiết dịch vụ của mỗi đặt phòng';

-- BẢNG CHI TIẾT THIẾT BỊ ĐẶT PHÒNG (Booking_Equipment)
CREATE TABLE IF NOT EXISTS booking_equipment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    equipment_id INT NOT NULL,
    quantity INT NOT NULL COMMENT 'Số lượng thiết bị cần',
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) COMMENT 'Bảng chi tiết thiết bị của mỗi đặt phòng';

-- DỮ LIỆU KHỞI TẠO (Seed Data)

-- Tạo tài khoản mẫu (mật khẩu hash: 123456 bằng SHA-256)
INSERT INTO users (username, password, role, fullname, phone, department, status) VALUES
('admin', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'ADMIN', 'Nguyễn Thị A - Quản trị viên', '0123456789', 'Admin', 'ACTIVE'),
('nv001', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'EMPLOYEE', 'Trần Văn B - Nhân viên', '0111111111', 'Bán hàng', 'ACTIVE'),
('nv002', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'EMPLOYEE', 'Lê Thị C - Nhân viên', '0222222222', 'Kỹ thuật', 'ACTIVE'),
('support01', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'SUPPORT_STAFF', 'Phạm Văn D - Nhân viên hỗ trợ', '0333333333', 'Hỗ trợ', 'ACTIVE');

-- Tạo mẫu phòng họp
INSERT INTO rooms (name, capacity, location, fixed_equipment, status) VALUES
('Phòng A1', 20, 'Tầng 1', 'Máy chiếu, Bảng trắng', 'AVAILABLE'),
('Phòng A2', 30, 'Tầng 1', 'Máy chiếu, TV màn hình', 'AVAILABLE'),
('Phòng B1', 15, 'Tầng 2', 'Bảng tương tác', 'AVAILABLE'),
('Phòng B2', 25, 'Tầng 2', 'Máy chiếu, Loa', 'AVAILABLE'),
('Phòng C1', 50, 'Tầng 3', 'Máy chiếu, Hệ thống âm thanh', 'AVAILABLE');

-- Tạo mẫu thiết bị di động
INSERT INTO equipment (name, total_quantity, available_quantity, status) VALUES
('Máy chiếu cầm tay', 5, 5, 'ACTIVE'),
('Loa di động', 8, 8, 'ACTIVE'),
('Laptop', 3, 3, 'ACTIVE'),
('Micro không dây', 6, 6, 'ACTIVE'),
('Bảng ghim', 4, 4, 'ACTIVE');

-- Tạo mẫu dịch vụ
INSERT INTO services (name, price, description, status) VALUES
('Nước suối', 0.5, 'Nước suối loại 500ml', 'ACTIVE'),
('Nước trà', 1.0, 'Nước trà nóng/lạnh', 'ACTIVE'),
('Bánh nhẹ', 2.0, 'Bánh quy, bánh mì nhẹ', 'ACTIVE'),
('Lau dọn sau họp', 5.0, 'Dịch vụ lau dọn phòng', 'ACTIVE'),
('Đồ ăn nhẹ (Combo)', 3.5, 'Combo bánh + nước', 'ACTIVE');
