package presentation;

import model.*;
import service.*;
import util.InputUtil;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Lớp Menu cho Nhân viên hỗ trợ (Support Staff)
 * Xem phiếu đặt được phân công, cập nhật trạng thái chuẩn bị
 */
public class SupportStaffMenu {
    private User currentUser;
    private UserService userService;
    private RoomService roomService;
    private BookingService bookingService;
    private DateTimeFormatter dateFormatter;

    public SupportStaffMenu(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    /**
     * Hiển thị menu chính của nhân viên hỗ trợ
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  MENU NHÂN VIÊN HỖ TRỢ");
            System.out.println("========================================");
            System.out.println("Xin chào: " + currentUser.getFullname());
            
            List<Booking> myBookings = bookingService.getBookingsBySupportStaff(currentUser.getId());
            System.out.println("Số phiếu đặt được phân công: " + myBookings.size());
            
            System.out.println("\n-- Chức năng --");
            System.out.println("1. Xem danh sách phiếu đặt được phân công");
            System.out.println("2. Cập nhật trạng thái chuẩn bị");
            System.out.println("3. Cập nhật hồ sơ cá nhân");
            System.out.println("4. Đăng xuất");
            
            int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 1, 4);
            
            switch (choice) {
                case 1:
                    viewMyAssignedBookings();
                    break;
                case 2:
                    updateBookingStatus();
                    break;
                case 3:
                    showProfileUpdate();
                    break;
                case 4:
                    System.out.println("✅ Đã đăng xuất. Tạm biệt!");
                    return;
            }
        }
    }

    // ========== XEM PHIẾU ĐẶT ĐƯỢC PHÂN CÔNG ==========
    private void viewMyAssignedBookings() {
        List<Booking> bookings = bookingService.getBookingsBySupportStaff(currentUser.getId());
        if (bookings.isEmpty()) {
            System.out.println("\nLỗi: Bạn chưa được phân công phiếu đặt nào!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n========================================");
        System.out.println("  DANH SÁCH PHIẾU ĐẶT ĐƯỢC PHÂN CÔNG");
        System.out.println("========================================");
        
        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            User employee = userService.getUserById(booking.getUserId());
            
            System.out.println("\nPhiếu ID: " + booking.getId());
            System.out.println("   Nhân viên: " + (employee != null ? employee.getFullname() : "N/A"));
            System.out.println("   Phòng: " + (room != null ? room.getName() : "N/A"));
            System.out.println("   Từ: " + booking.getStartTime().format(dateFormatter));
            System.out.println("   Đến: " + booking.getEndTime().format(dateFormatter));
            System.out.println("   Số người: " + booking.getParticipantCount());
            System.out.println("   Trạng thái: " + booking.getStatus());
            
            if (booking.getNotes() != null && !booking.getNotes().isEmpty()) {
                System.out.println("   Ghi chú: " + booking.getNotes());
            }
        }
        
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    // ========== CẬP NHẬT TRẠNG THÁI CHUẨN BỊ ==========
    private void updateBookingStatus() {
        List<Booking> bookings = bookingService.getBookingsBySupportStaff(currentUser.getId());
        if (bookings.isEmpty()) {
            System.out.println("\nLỗi: Bạn chưa được phân công phiếu đặt nào!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n========================================");
        System.out.println("  CẬP NHẬT TRẠNG THÁI CHUẨN BỊ");
        System.out.println("========================================");
        System.out.println("\nDanh sách phiếu đặt:");
        
        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            System.out.println("   ID: " + booking.getId() + " | " + 
                             (room != null ? room.getName() : "N/A") +
                             " | Từ: " + booking.getStartTime().format(dateFormatter));
        }
        
        int bookingId = InputUtil.inputPositiveInt("\nChọn ID phiếu đặt cần cập nhật: ");
        
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null || booking.getSupportStaffId() != currentUser.getId()) {
            System.out.println("Lỗi: Phiếu đặt không tồn tại hoặc không được phân công cho bạn!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\nChọn trạng thái chuẩn bị:");
        System.out.println("1. Preparing (Đang chuẩn bị)");
        System.out.println("2. Ready (Sẵn sàng)");
        System.out.println("3. Missing Equipment (Thiếu thiết bị)");
        int choice = InputUtil.inputChoice("Chọn trạng thái: ", 1, 3);
        
        String newStatus;
        switch (choice) {
            case 1:
                newStatus = "Preparing";
                break;
            case 2:
                newStatus = "Ready";
                break;
            case 3:
                newStatus = "Missing Equipment";
                break;
            default:
                newStatus = "Preparing";
        }
        
        String notes = InputUtil.inputNonEmptyString("Ghi chú (có thể để trống): ");
        
        if (bookingService.updateNotes(bookingId, notes)) {
            System.out.println("Thành công: Cập nhật trạng thái chuẩn bị thành công!");
            System.out.println("   Trạng thái: " + newStatus);
        } else {
            System.out.println("Lỗi: Cập nhật trạng thái thất bại!");
        }
        
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    // ========== CẬP NHẬT HỒ SƠ ==========
    private void showProfileUpdate() {
        System.out.println("\n========================================");
        System.out.println("  CẬP NHẬT HỒ SƠ CÁ NHÂN");
        System.out.println("========================================");
        System.out.println("\nThông tin hiện tại:");
        System.out.println("   Họ và tên: " + currentUser.getFullname());
        System.out.println("   Điện thoại: " + currentUser.getPhone());
        System.out.println("   Phòng ban: " + currentUser.getDepartment());
        
        System.out.println("\n-- Menu cập nhật --");
        System.out.println("1. Cập nhật thông tin cá nhân");
        System.out.println("2. Đổi mật khẩu");
        System.out.println("0. Quay lại");
        
        int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 0, 2);
        
        switch (choice) {
            case 1:
                updateProfile();
                break;
            case 2:
                changePassword();
                break;
        }
    }

    private void updateProfile() {
        String fullname = InputUtil.inputFullName("Họ và tên mới: ");
        String phone = InputUtil.inputPhone("Số điện thoại mới: ");
        String department = InputUtil.inputNonEmptyString("Phòng ban mới: ");
        
        if (userService.updateProfile(currentUser.getId(), fullname, phone, department)) {
            System.out.println("Thành công: Cập nhật hồ sơ thành công!");
            currentUser.setFullname(fullname);
            currentUser.setPhone(phone);
            currentUser.setDepartment(department);
        } else {
            System.out.println("Lỗi: Cập nhật hồ sơ thất bại!");
        }
        
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void changePassword() {
        String oldPassword = InputUtil.inputPassword("Mật khẩu cũ: ");
        String newPassword = InputUtil.inputPassword("Mật khẩu mới: ");
        String confirmPassword = InputUtil.inputPassword("Xác nhận mật khẩu mới: ");
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Lỗi: Mật khẩu xác nhận không khớp!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        if (userService.changePassword(currentUser.getId(), oldPassword, newPassword)) {
            System.out.println("Thành công: Đổi mật khẩu thành công!");
        } else {
            System.out.println("Lỗi: Đổi mật khẩu thất bại!");
        }
        
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }
}


