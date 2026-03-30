# HƯỚNG DẪN SỬ DỤNG DAY 3 & DAY 4

## I. DAY 3 - BOOKING LOGIC

### 1. Nhân viên (EMPLOYEE) - Đặt phòng mới

**Flow:**
```
Menu > 1. Đặt phòng mới
  ├─ Nhập startTime (yyyy-MM-dd HH:mm)
  ├─ Nhập endTime (yyyy-MM-dd HH:mm)
  ├─ Hệ thống hiển thị phòng trống (Day 3)
  ├─ Chọn phòng ID
  ├─ Nhập số người
  ├─ Nhập ghi chú (optional)
  ├─ Booking được tạo (PENDING)
  ├─ Chọn thiết bị (Day 3 - nhiều lần, 0 để dừng)
  │  ├─ Danh sách thiết bị hiển thị
  │  ├─ Chọn ID thiết bị
  │  ├─ Nhập số lượng
  │  └─ Lặp lại nếu thêm tiếp
  └─ Chọn dịch vụ (Day 3 - nhiều lần, 0 để dừng)
     ├─ Danh sách dịch vụ hiển thị
     ├─ Chọn ID dịch vụ
     ├─ Nhập số lượng
     └─ Lặp lại nếu thêm tiếp
```

**Kiểm chứng:**
- ✓ Thời gian không ở quá khứ
- ✓ Không trùng lịch (PENDING/APPROVED)
- ✓ Số người ≤ sức chứa phòng
- ✓ Thiết bị đủ sẵn có

**Kết quả:** Phiếu đặt ID:xxx (PENDING)

---

### 2. Xem phòm khả dụng theo thời gian

**Flow:**
```
EmployeeMenu.createNewBooking()
  └─ bookingService.getAvailableRoomsByTime(startTime, endTime)
     └─ bookingDao.getAvailableRooms()
        └─ SQL: SELECT từ rooms WHERE không có booking trùng
```

**SQL Logic:**
```sql
SELECT DISTINCT r.id FROM rooms r
WHERE r.status = 'AVAILABLE'
  AND r.id NOT IN (
    SELECT DISTINCT room_id FROM bookings
    WHERE status IN ('PENDING', 'APPROVED')
      AND ((start_time < ? AND end_time > ?)
           OR (start_time < ? AND end_time > ?)
           OR (start_time >= ? AND end_time <= ?))
  )
```

---

## II. DAY 4 - WORKFLOW

### 1. Admin - Duyệt lịch đặt phòng

**Flow:**
```
AdminMenu.showMenu()
  ├─ 5. Duyệt lịch đặt phòng (Day 4)
  │
  └─ approveBookingMenu()
     ├─ 1. Xem danh sách phiếu chờ duyệt
     │  └─ getPendingBookings() → Hiển thị (ID, Nhân viên, Phòng, Thời gian)
     │
     ├─ 2. Duyệt phiếu đặt
     │  ├─ Liệt kê PENDING
     │  ├─ Chọn phiếu ID
     │  ├─ Liệt kê support staff (getAllSupportStaff())
     │  ├─ Chọn support staff ID
     │  └─ approveBooking(bookingId, supportStaffId)
     │     └─ Cập nhật:
     │        ├─ status = APPROVED
     │        └─ support_staff_id = [ID]
     │
     └─ 3. Từ chối phiếu đặt
        ├─ Liệt kê PENDING
        ├─ Chọn phiếu ID
        └─ rejectBooking(bookingId)
           └─ Cập nhật: status = REJECTED
```

**Kết quả:**
- Phiếu APPROVED → Support Staff nhận được
- Phiếu REJECTED → Nhân viên thấy "Từ chối"

---

### 2. Support Staff - Cập nhật trạng thái chuẩn bị

**Flow:**
```
SupportStaffMenu.showMenu()
  ├─ Hiển thị: Số phiếu được phân công
  │
  ├─ 1. Xem danh sách phiếu được phân công
  │  └─ getBookingsBySupportStaff(currentUser.getId())
  │     └─ Hiển thị: ID, Phòng, Thời gian
  │
  └─ 2. Cập nhật trạng thái chuẩn bị (Day 4)
     ├─ Liệt kê phiếu được phân công (status=APPROVED)
     ├─ Chọn phiếu ID
     ├─ Chọn trạng thái:
     │  ├─ 1. Preparing (Đang chuẩn bị)
     │  ├─ 2. Ready (Sẵn sàng)
     │  └─ 3. Missing Equipment (Thiếu thiết bị)
     ├─ Nhập ghi chú thêm (optional)
     └─ updateNotes(bookingId, fullNotes)
        └─ Lưu vào booking.notes:
           └─ Format: "Preparing" hoặc "Preparing | [additional notes]"
```

**Hiển thị:**
- Support Staff xem phiếu: Trạng thái = "Preparing", "Ready", hoặc "Missing Equipment"
- Employee xem phiếu: Trạng thái chuẩn bị = "Đang chuẩn bị", "Sẵn sàng", hoặc "Thiếu thiết bị"

---

### 3. Employee - Xem trạng thái booking (Day 4)

**Flow:**
```
EmployeeMenu.showMenu()
  └─ 2. Xem lịch đặt phòng của tôi
     └─ viewMyBookings()
        ├─ getBookingsByUserId(currentUser.getId())
        │
        └─ Hiển thị mỗi booking:
           ├─ ID
           ├─ Phòng
           ├─ Thời gian (Từ - Đến)
           ├─ Số người
           ├─ Trạng thái duyệt (getApprovalStatus)
           │  ├─ PENDING → "Chờ duyệt"
           │  ├─ APPROVED → "Đã duyệt"
           │  ├─ REJECTED → "Từ chối"
           │  └─ DONE → "Hoàn thành"
           │
           ├─ Trạng thái chuẩn bị (getPreparationStatus)
           │  ├─ notes = null → "Chưa cập nhật"
           │  ├─ notes contain "Preparing" → "Đang chuẩn bị"
           │  ├─ notes contain "Ready" → "Sẵn sàng"
           │  └─ notes contain "Missing Equipment" → "Thiếu thiết bị"
           │
           └─ Ghi chú
```

---

## III. DATABASE SCHEMA

### Bảng quan trọng:

**bookings:**
```sql
id INT PRIMARY KEY
user_id INT (FK users)
room_id INT (FK rooms)
start_time DATETIME
end_time DATETIME
status VARCHAR(20) -- PENDING, APPROVED, REJECTED, DONE
support_staff_id INT (FK users) -- NULL → chưa phân công
participant_count INT
notes VARCHAR(255) -- Lưu trạng thái chuẩn bị (Day 4)
created_date TIMESTAMP
```

**booking_equipment:**
```sql
booking_id INT (FK)
equipment_id INT (FK)
quantity INT
```

**booking_services:**
```sql
booking_id INT (FK)
service_id INT (FK)
quantity INT
```

---

## IV. TESTING CHECKLIST

### Day 3 - Booking Logic

- [ ] Test đặt phòng với thời gian khác nhau
- [ ] Verify phòng trống hiển thị đúng
- [ ] Test xung đột lịch (phòng bị chiếm)
- [ ] Test vượt sức chứa (error)
- [ ] Test thời gian quá khứ (error)
- [ ] Test chọn nhiều thiết bị
- [ ] Test chọn nhiều dịch vụ
- [ ] Verify booking_equipment & booking_services lưu đúng
- [ ] Verify booking status = PENDING

### Day 4 - Workflow

**Admin:**
- [ ] Xem danh sách PENDING đúng
- [ ] Duyệt phiếu → status = APPROVED + support_staff_id
- [ ] Từ chối phiếu → status = REJECTED

**Support Staff:**
- [ ] Xem phiếu được phân công (status=APPROVED, support_staff_id=currentUser)
- [ ] Cập nhật status "Preparing" → notes lưu "Preparing"
- [ ] Cập nhật status "Ready" → notes lưu "Ready"
- [ ] Cập nhật status "Missing Equipment" → notes lưu "Missing Equipment"
- [ ] Cập nhật với ghi chú thêm → notes lưu "Status | Notes"

**Employee:**
- [ ] Xem trạng thái duyệt (PENDING→"Chờ duyệt", APPROVED→"Đã duyệt")
- [ ] Xem trạng thái chuẩn bị (notes → "Đang chuẩn bị" / "Sẵn sàng")
- [ ] Verify helper methods: getApprovalStatus(), getPreparationStatus()

---

## V. LƯU Ý QUAN TRỌNG

1. **Thứ tự nhập thời gian:** Employee phải nhập startTime, endTime TRƯỚC khi chọn phòng (Day 3)
2. **Format ghi chú:** Support Staff lưu trạng thái vào notes (Day 4)
3. **Chỉ lấy phòng AVAILABLE:** Query có `WHERE r.status = 'AVAILABLE'`
4. **Chỉ check PENDING/APPROVED:** Xung đột lịch không check REJECTED, DONE
5. **Support Staff chỉ xem APPROVED:** Query `WHERE support_staff_id = ? AND status = 'APPROVED'`

---

## VI. QUICK REFERENCE

### Files thay đổi:
1. BookingDao.java - `getAvailableRooms()`
2. BookingService.java - `getAvailableRoomsByTime()`
3. EmployeeMenu.java - `createNewBooking()`, `viewMyBookings()`, helper methods
4. AdminMenu.java - `approveBookingMenu()`, `viewPendingBookings()`, `approveBooking()`, `rejectBooking()`
5. SupportStaffMenu.java - `updateBookingStatus()`

### Key methods:
- `bookingService.getAvailableRoomsByTime(startTime, endTime)` → List<Room>
- `bookingService.createBooking(userId, roomId, startTime, endTime, participantCount, notes)` → int (bookingId)
- `bookingService.addEquipmentToBooking(bookingId, equipmentId, quantity)` → boolean
- `bookingService.addServiceToBooking(bookingId, serviceId, quantity)` → boolean
- `bookingService.approveBooking(bookingId, supportStaffId)` → boolean
- `bookingService.rejectBooking(bookingId)` → boolean
- `bookingService.updateNotes(bookingId, notes)` → boolean

