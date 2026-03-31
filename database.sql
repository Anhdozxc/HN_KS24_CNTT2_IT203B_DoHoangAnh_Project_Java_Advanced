-- Xóa database cũ nếu tồn tại
DROP DATABASE IF EXISTS meeting_db;

-- Tạo database mới
CREATE DATABASE IF NOT EXISTS meeting_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE meeting_db;

-- BẢNG NGƯỜI DÙNG (Users)
-- Vai trò: EMPLOYEE (Nhân viên), SUPPORT_STAFF (Nhân viên hỗ trợ), ADMIN (Quản trị viên)
-- USERS
CREATE TABLE users
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(20)  NOT NULL,
    fullname     VARCHAR(100) NOT NULL,
    phone        VARCHAR(20),
    department   VARCHAR(50),
    created_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(20) DEFAULT 'ACTIVE'
);

-- ROOMS
CREATE TABLE rooms
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(100) NOT NULL UNIQUE,
    capacity        INT          NOT NULL,
    location        VARCHAR(100) NOT NULL,
    fixed_equipment VARCHAR(255),
    status          VARCHAR(20) DEFAULT 'AVAILABLE',
    created_date    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- EQUIPMENT
CREATE TABLE equipment
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    name               VARCHAR(100) NOT NULL UNIQUE,
    total_quantity     INT          NOT NULL,
    available_quantity INT          NOT NULL,
    status             VARCHAR(50)  NOT NULL,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SERVICES
CREATE TABLE services
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL UNIQUE,
    price        DOUBLE       NOT NULL,
    description  VARCHAR(255),
    status       VARCHAR(20) DEFAULT 'ACTIVE',
    created_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- BOOKINGS
CREATE TABLE bookings
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    user_id           INT      NOT NULL,
    room_id           INT      NOT NULL,
    start_time        DATETIME NOT NULL,
    end_time          DATETIME NOT NULL,
    status            VARCHAR(20) DEFAULT 'PENDING',
    preparation_status VARCHAR(30) DEFAULT 'Preparing',
    preparation_note  TEXT,
    support_staff_id  INT,
    participant_count INT,
    notes             VARCHAR(255),
    created_date      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE,
    FOREIGN KEY (support_staff_id) REFERENCES users (id) ON DELETE SET NULL
);

-- BOOKING SERVICES
CREATE TABLE booking_services
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    service_id INT,
    quantity   INT,
    FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE CASCADE
);

-- BOOKING EQUIPMENT
CREATE TABLE booking_equipment
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    booking_id   INT,
    equipment_id INT,
    quantity     INT,
    FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment (id) ON DELETE CASCADE
);

INSERT INTO users (username, password, role, fullname, phone, department)
VALUES ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ADMIN', 'Admin', '0123456789',
        'Admin'),
       ('nv001', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'EMPLOYEE', 'Nhan vien 1',
        '0111111111', 'Sale'),
       ('support01', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'SUPPORT_STAFF', 'Support 1',
        '0222222222', 'Support');

-- ROOMS
INSERT INTO rooms (name, capacity, location, fixed_equipment)
VALUES ('Phong A1', 20, 'Tang 1', 'May chieu'),
       ('Phong A2', 30, 'Tang 1', 'TV'),
       ('Phong B1', 15, 'Tang 2', 'Bang');

-- EQUIPMENT
INSERT INTO equipment (name, total_quantity, available_quantity, status)
VALUES ('May chieu', 5, 5, 'ACTIVE'),
       ('Loa', 5, 5, 'ACTIVE');

-- SERVICES
INSERT INTO services (name, price, description)
VALUES ('Nuoc', 1.0, 'Nuoc suoi'),
       ('Banh', 2.0, 'Banh nhe')