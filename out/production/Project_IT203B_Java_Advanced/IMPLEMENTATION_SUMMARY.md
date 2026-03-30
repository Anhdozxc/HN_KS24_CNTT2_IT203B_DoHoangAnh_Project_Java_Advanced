# Tóm Tắt Implement Day 3 & Day 4

## DAY 3 - BOOKING LOGIC

### 1. BookingDao.java
- **Thêm method:** `getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime)`
  - Lấy danh sách ID phòng không bị trùng lịch trong khoảng thời gian
  - Logic: Lọc phòng AVAILABLE và không có booking PENDING/APPROVED
  - Trả về List<Integer> chứa ID các phòng trống

### 2. BookingService.java
- **Thêm import:** `model.Room`, `ArrayList`
- **Thêm method:** `getAvailableRoomsByTime(LocalDateTime startTime, LocalDateTime endTime)`
  - Gọi `bookingDao.getAvailableRooms()` để lấy danh sách ID phòng
  - Convert ID thành đối tượng Room đầy đủ thông tin
  - Trả về List<Room> để display

### 3. EmployeeMenu.java
- **Cập nhật method:** `createNewBooking()`
  - **Thứ tự mới:** 
    1. Nhập startTime + endTime trước
    2. Lấy phòng trống theo thời gian (gọi `getAvailableRoomsByTime()`)
    3. Chọn phòng
    4. Nhập số người + ghi chú
    5. **NEW:** Chọn thiết bị (gọi `addEquipmentToBooking()`)
    6. **NEW:** Chọn dịch vụ (gọi `addServiceToBooking()`)
  - Lặp cho phép chọn nhiều thiết bị/dịch vụ
  
- **Cập nhật method:** `viewMyBookings()`
  - **NEW:** Hiển thị trạng thái duyệt: "Chờ duyệt", "Đã duyệt", "Từ chối"
  - **NEW:** Hiển thị trạng thái chuẩn bị: "Chưa cập nhật", "Đang chuẩn bị", "Sẵn sàng", "Thiếu thiết bị"
  - Thêm helper methods: `getApprovalStatus()`, `getPreparationStatus()`

---

## DAY 4 - WORKFLOW

### 1. AdminMenu.java
- **Thêm service:** `BookingService bookingService`, `DateTimeFormatter dateFormatter`
- **Cập nhật method:** `showMenu()`
  - Thêm option "5. Duyệt lịch đặt phòng (Day 4)"
  - Thêm option "6. Đăng xuất" (đặt lại số hiệu)
  - Gọi `approveBookingMenu()`

- **Thêm method:** `approveBookingMenu()`
  - Menu duyệt/từ chối booking
  - Hiển thị số lượng booking PENDING
  - 3 lựa chọn: Xem, Duyệt, Từ chối

- **Thêm method:** `viewPendingBookings()`
  - Hiển thị danh sách phiếu chờ duyệt
  - Thông tin: ID, Nhân viên, Phòng, Thời gian, Số người, Ghi chú

- **Thêm method:** `approveBooking()`
  - Liệt kê phiếu PENDING
  - Chọn phiếu cần duyệt
  - Lấy danh sách support staff (gọi `getAllSupportStaff()`)
  - Chọn support staff để phân công
  - Gọi `bookingService.approveBooking(bookingId, supportStaffId)`
  - Cập nhật status = APPROVED + gán support_staff_id

- **Thêm method:** `rejectBooking()`
  - Liệt kê phiếu PENDING
  - Chọn phiếu cần từ chối
  - Gọi `bookingService.rejectBooking(bookingId)`
  - Cập nhật status = REJECTED

### 2. SupportStaffMenu.java
- **Cập nhật method:** `updateBookingStatus()`
  - **NEW:** Lưu trạng thái chuẩn bị vào notes
  - 3 trạng thái: "Preparing" | "Ready" | "Missing Equipment"
  - Có ghi chú thêm (optional)
  - Format: `"[Status] | [Additional Notes]"` hoặc chỉ `"[Status]"`
  - Gọi `updateNotes(bookingId, fullNotes)`

---

## WORKFLOW TỔNG HỢP

### Employee (Nhân viên)
1. Đặt phòng mới → Chọn thời gian → Phòng trống hiển thị → Chọn thiết bị + dịch vụ
2. Xem lịch của tôi → Hiển thị trạng thái duyệt + trạng thái chuẩn bị
3. Hủy phiếu (chỉ PENDING)

### Admin
1. Xem phiếu chờ duyệt
2. Duyệt phiếu → Phân công support staff → status = APPROVED
3. Từ chối phiếu → status = REJECTED

### Support Staff
1. Xem phiếu được phân công (status = APPROVED)
2. Cập nhật trạng thái chuẩn bị
   - Preparing (Đang chuẩn bị)
   - Ready (Sẵn sàng)
   - Missing Equipment (Thiếu thiết bị)
3. Lưu trạng thái vào booking.notes

---

## KIỂM TRA XUNG ĐỘT

Điều kiện không cho phép booking:
```
start < existing_end AND end > existing_start
```

Logic kiểm tra:
- Chỉ kiểm tra booking có status = PENDING hoặc APPROVED
- Query trong `isRoomBooked()` (BookingDao)
- Được gọi trong `createBooking()` (BookingService)

---

## KIỂM TRA KHÁC

1. **Thời gian không ở quá khứ:** `ValidationUtil.isStartTimeValid()`
2. **Không vượt sức chứa phòng:** `roomService.isRoomCapacitySufficient()`
3. **Không trùng lịch:** `bookingDao.isRoomBooked()`

---

## GHI CHÚ

- Tất cả code đã viết tiếng Việt (comments + messages)
- Không dùng framework (chỉ Java Core + JDBC)
- Cấu trúc project không thay đổi
- Chỉ thêm/sửa methods cần thiết

