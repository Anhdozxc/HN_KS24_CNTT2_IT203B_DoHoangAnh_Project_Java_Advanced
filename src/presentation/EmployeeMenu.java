package presentation;

import model.*;
import service.*;
import util.InputUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMenu {
    private User currentUser;
    private UserService userService;
    private RoomService roomService;
    private EquipmentService equipmentService;
    private ServiceService serviceService;
    private BookingService bookingService;
    private DateTimeFormatter dateFormatter;

    public EmployeeMenu(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.equipmentService = new EquipmentService();
        this.serviceService = new ServiceService();
        this.bookingService = new BookingService();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  MENU NHÂN VIÊN");
            System.out.println("========================================");
            System.out.println("Xin chào: " + currentUser.getFullname());

            System.out.println("\n-- Các chức năng --");
            System.out.println("1. Đặt phòng mới");
            System.out.println("2. Xem lịch đặt phòng của tôi");
            System.out.println("3. Hủy lịch đặt phòng");
            System.out.println("4. Xem phòng khả dụng");
            System.out.println("5. Xem dịch vụ");
            System.out.println("6. Cập nhật thông tin cá nhân");
            System.out.println("7. Đăng xuất");

            int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 1, 7);

            switch (choice) {
                case 1:
                    createNewBooking();
                    break;
                case 2:
                    viewMyBookings();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewAvailableRooms();
                    break;
                case 5:
                    viewAvailableServices();
                    break;
                case 6:
                    showProfileUpdate();
                    break;
                case 7:
                    System.out.println(" Đã đăng xuất. Tạm biệt!");
                    return;
            }
        }
    }

    private void createNewBooking() {
        System.out.println("\n===== ĐẶT PHÒNG MỚI =====");

        // Lấy danh sách phòng khả dụng
        List<Room> rooms = roomService.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("Lỗi: Không có phòng khả dụng!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        // Hiển thị danh sách phòng
        System.out.println("\nCác phòng khả dụng:");
        for (Room r : rooms) {
            System.out.println("ID: " + r.getId() + " - " + r.getName() + 
                             " (Sức chứa: " + r.getCapacity() + ", Vị trí: " + r.getLocation() + ")");
        }

        // Chọn phòng
        int roomId = InputUtil.inputPositiveInt("\nChọn ID phòng: ");
        
        Room selectedRoom = roomService.getRoomById(roomId);
        if (selectedRoom == null || !"AVAILABLE".equals(selectedRoom.getStatus())) {
            System.out.println("Lỗi: Phòng không tồn tại hoặc không khả dụng!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        // Nhập thời gian
        System.out.println("\nNhập thời gian đặt (định dạng: yyyy-MM-dd HH:mm)");
        LocalDateTime startTime = InputUtil.inputDateTime("Thời gian bắt đầu: ");
        LocalDateTime endTime = InputUtil.inputDateTime("Thời gian kết thúc: ");

        // Nhập số người
        System.out.println("Sức chứa phòng: " + selectedRoom.getCapacity() + " người");
        int participantCount = InputUtil.inputPositiveInt("Số người tham dự: ");

        // Nhập ghi chú
        String notes = InputUtil.inputNonEmptyString("Ghi chú (có thể bỏ qua): ");

        // Tạo booking
        int bookingId = bookingService.createBooking(
                currentUser.getId(), roomId, startTime, endTime, participantCount, 
                notes != null && !notes.isEmpty() ? notes : ""
        );

        if (bookingId > 0) {
            System.out.println("Thành công: Đặt phòng ID: " + bookingId);
            System.out.println("Trạng thái: PENDING (chờ duyệt)");
        } else {
            System.out.println("Lỗi: Đặt phòng thất bại! Vui lòng kiểm tra lại thông tin.");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void viewMyBookings() {
        System.out.println("\n===== LỊCH ĐẶT PHÒNG CỦA TÔI =====");
        
        List<Booking> bookings = bookingService.getBookingsByUserId(currentUser.getId());
        
        if (bookings.isEmpty()) {
            System.out.println("\nBạn chưa có phiếu đặt phòng nào");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            
            System.out.println("\n" + "-".repeat(50));
            System.out.println("ID: " + booking.getId());
            System.out.println("Phòng: " + (room != null ? room.getName() : "N/A"));
            System.out.println("Từ: " + booking.getStartTime().format(dateFormatter));
            System.out.println("Đến: " + booking.getEndTime().format(dateFormatter));
            System.out.println("Số người: " + booking.getParticipantCount());
            System.out.println("Trạng thái: " + booking.getStatus());
            if (booking.getNotes() != null && !booking.getNotes().isEmpty()) {
                System.out.println("Ghi chú: " + booking.getNotes());
            }
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void cancelBooking() {
        System.out.println("\n===== HỦY LỊCH ĐẶT PHÒNG =====");
        
        List<Booking> bookings = bookingService.getBookingsByUserId(currentUser.getId());
        
        // Lọc chỉ hiển thị booking PENDING
        List<Booking> pendingBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if ("PENDING".equals(b.getStatus())) {
                pendingBookings.add(b);
            }
        }
        
        if (pendingBookings.isEmpty()) {
            System.out.println("Không có lịch đặt phòng ở trạng thái PENDING để hủy!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        System.out.println("Các lịch đặt có thể hủy:");
        for (Booking b : pendingBookings) {
            Room room = roomService.getRoomById(b.getRoomId());
            System.out.println("ID: " + b.getId() + " - " + (room != null ? room.getName() : "N/A") +
                             " (" + b.getStartTime().format(dateFormatter) + ")");
        }

        int bookingId = InputUtil.inputPositiveInt("\nNhập ID lịch cần hủy: ");
        
        if (bookingService.cancelBooking(bookingId)) {
            System.out.println("Thành công: Đã hủy lịch đặt phòng!");
        } else {
            System.out.println("Lỗi: Hủy lịch thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void viewAvailableRooms() {
        System.out.println("\n===== DANH SÁCH PHÒNG KHẢ DỤNG =====");
        
        List<Room> rooms = roomService.getAvailableRooms();
        
        if (rooms.isEmpty()) {
            System.out.println("\nKhông có phòng khả dụng!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        for (Room room : rooms) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("ID: " + room.getId());
            System.out.println("Tên: " + room.getName());
            System.out.println("Sức chứa: " + room.getCapacity() + " người");
            System.out.println("Vị trí: " + room.getLocation());
            System.out.println("Thiết bị cố định: " + room.getFixedEquipment());
            System.out.println("Trạng thái: " + room.getStatus());
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void viewAvailableServices() {
        System.out.println("\n===== DANH SÁCH DỊCH VỤ =====");
        
        List<Service> services = serviceService.getAllServices();
        
        if (services.isEmpty()) {
            System.out.println("\nKhông có dịch vụ nào!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        for (Service service : services) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("ID: " + service.getId());
            System.out.println("Tên: " + service.getName());
            System.out.println("Giá: " + service.getPrice());
            System.out.println("Mô tả: " + service.getDescription());
            System.out.println("Trạng thái: " + service.getStatus());
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void showProfileUpdate() {
        while (true) {
            System.out.println("\n===== CẬP NHẬT THÔNG TIN CÁ NHÂN =====");
            System.out.println("1. Cập nhật hồ sơ");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("0. Quay lại");

            int choice = InputUtil.inputChoice("\nChọn: ", 0, 2);

            if (choice == 1) {
                updateProfile();
            } else if (choice == 2) {
                changePassword();
            } else {
                break;
            }
        }
    }

    private void updateProfile() {
        System.out.println("\n===== CẬP NHẬT HỒ SƠ =====");
        System.out.println("Tên hiện tại: " + currentUser.getFullname());
        System.out.println("Điện thoại hiện tại: " + currentUser.getPhone());
        System.out.println("Phòng ban hiện tại: " + currentUser.getDepartment());
        
        String name = InputUtil.inputFullName("Tên mới: ");
        String phone = InputUtil.inputPhone("Điện thoại mới: ");
        String department = InputUtil.inputNonEmptyString("Phòng ban mới: ");

        if (userService.updateProfile(currentUser.getId(), name, phone, department)) {
            System.out.println("Thành công: Cập nhật hồ sơ!");
            currentUser.setFullname(name);
            currentUser.setPhone(phone);
            currentUser.setDepartment(department);
        } else {
            System.out.println("Lỗi: Cập nhật thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void changePassword() {
        System.out.println("\n===== ĐỔI MẬT KHẨU =====");
        String oldPassword = InputUtil.inputPassword("Mật khẩu cũ: ");
        String newPassword = InputUtil.inputPassword("Mật khẩu mới: ");
        String confirmPassword = InputUtil.inputPassword("Xác nhận mật khẩu mới: ");

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Lỗi: Mật khẩu xác nhận không khớp!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        if (userService.changePassword(currentUser.getId(), oldPassword, newPassword)) {
            System.out.println("Thành công: Đổi mật khẩu!");
        } else {
            System.out.println("Lỗi: Đổi mật khẩu thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }
}