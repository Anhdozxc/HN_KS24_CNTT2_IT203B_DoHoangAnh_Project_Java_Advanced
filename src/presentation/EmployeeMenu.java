package presentation;

import model.*;
import service.*;
import util.InputUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Lớp Menu cho Nhân viên (Employee)
 * Đặt phòng, xem booking, quản lý hồ sơ cá nhân
 */
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

    /**
     * Hiển thị menu chính của nhân viên
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  MENU NHÂN VIÊN");
            System.out.println("========================================");
            System.out.println("Xin chào: " + currentUser.getFullname());
            System.out.println("\n-- Chức năng --");
            System.out.println("1. Đặt phòng họp mới");
            System.out.println("2. Xem lịch đặt phòng của tôi");
            System.out.println("3. Hủy lịch đặt phòng");
            System.out.println("4. Xem danh sách phòng khả dụng");
            System.out.println("5. Xem danh sách dịch vụ");
            System.out.println("6. Cập nhật hồ sơ cá nhân");
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
                    System.out.println("✅ Đã đăng xuất. Tạm biệt!");
                    return;
            }
        }
    }

    // ========== ĐẶT PHÒNG MỚI ==========
    private void createNewBooking() {
        System.out.println("\n========================================");
        System.out.println("  ĐẶT PHÒNG MỚI");
        System.out.println("========================================");
        
        List<Room> availableRooms = roomService.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            System.out.println("Lỗi: Không có phòng khả dụng!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\nDanh sách phòng khả dụng:");
        for (Room room : availableRooms) {
            System.out.println("   ID: " + room.getId() + " | " + room.getName() + 
                             " | Sức chứa: " + room.getCapacity());
        }
        
        int roomId = InputUtil.inputPositiveInt("\nChọn ID phòng: ");
        Room selectedRoom = roomService.getRoomById(roomId);
        if (selectedRoom == null) {
            System.out.println("Lỗi: Phòng không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\nNhập thời gian đặt (định dạng: yyyy-MM-dd HH:mm)");
        LocalDateTime startTime = InputUtil.inputDateTime("Thời gian bắt đầu: ");
        LocalDateTime endTime = InputUtil.inputDateTime("Thời gian kết thúc: ");
        
        int participantCount = InputUtil.inputPositiveInt("Số người tham dự dự kiến: ");
        
        String notes = InputUtil.inputNonEmptyString("Ghi chú (có thể để trống): ");
        
        int bookingId = bookingService.createBooking(currentUser.getId(), roomId, startTime, 
                                                     endTime, participantCount, notes);
        
        if (bookingId <= 0) {
            System.out.println("Lỗi: Không thể tạo phiếu đặt!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("Thành công: Tạo phiếu đặt thành công! ID: " + bookingId);
        
        System.out.print("\nBạn có muốn thêm thiết bị vào phiếu đặt này? (Y/N): ");
        String addEquipment = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(addEquipment)) {
            addEquipmentToBooking(bookingId);
        }
        
        System.out.print("\nBạn có muốn thêm dịch vụ vào phiếu đặt này? (Y/N): ");
        String addService = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(addService)) {
            addServiceToBooking(bookingId);
        }
        
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void addEquipmentToBooking(int bookingId) {
        List<Equipment> equipments = equipmentService.getAvailableEquipment();
        if (equipments.isEmpty()) {
            System.out.println("\n❌ Không có thiết bị nào khả dụng!");
            return;
        }
        
        System.out.println("\n🔧 Danh sách thiết bị khả dụng:");
        for (Equipment equipment : equipments) {
            System.out.println("   ID: " + equipment.getId() + " | " + equipment.getName() + 
                             " | Khả dụng: " + equipment.getAvailableQuantity());
        }
        
        while (true) {
            int equipmentId = InputUtil.inputPositiveInt("🔍 Chọn ID thiết bị (nhập 0 để dừng): ");
            if (equipmentId == 0) break;
            
            Equipment equipment = equipmentService.getEquipmentById(equipmentId);
            if (equipment == null) {
                System.out.println("❌ Thiết bị không tồn tại!");
                continue;
            }
            
            int quantity = InputUtil.inputPositiveInt("📦 Số lượng: ");
            
            if (bookingService.addEquipmentToBooking(bookingId, equipmentId, quantity)) {
                System.out.println("✅ Thêm thiết bị thành công!");
            } else {
                System.out.println("❌ Thêm thiết bị thất bại!");
            }
        }
    }

    private void addServiceToBooking(int bookingId) {
        List<Service> services = serviceService.getActiveServices();
        if (services.isEmpty()) {
            System.out.println("\n❌ Không có dịch vụ nào!");
            return;
        }
        
        System.out.println("\n☕ Danh sách dịch vụ:");
        for (Service service : services) {
            System.out.println("   ID: " + service.getId() + " | " + service.getName() + 
                             " | Giá: " + service.getPrice());
        }
        
        while (true) {
            int serviceId = InputUtil.inputPositiveInt("🔍 Chọn ID dịch vụ (nhập 0 để dừng): ");
            if (serviceId == 0) break;
            
            Service service = serviceService.getServiceById(serviceId);
            if (service == null) {
                System.out.println("❌ Dịch vụ không tồn tại!");
                continue;
            }
            
            int quantity = InputUtil.inputPositiveInt("📦 Số lượng: ");
            
            if (bookingService.addServiceToBooking(bookingId, serviceId, quantity)) {
                System.out.println("✅ Thêm dịch vụ thành công!");
            } else {
                System.out.println("❌ Thêm dịch vụ thất bại!");
            }
        }
    }

    // ========== XEM LỊCH ĐẶT CỦA TÔI ==========
    private void viewMyBookings() {
        List<Booking> bookings = bookingService.getBookingsByUserId(currentUser.getId());
        if (bookings.isEmpty()) {
            System.out.println("\n❌ Bạn chưa có phiếu đặt nào!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              LỊCH ĐẶT PHÒNG CỦA TÔI                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            User supportStaff = booking.getSupportStaffId() != null ? 
                              userService.getUserById(booking.getSupportStaffId()) : null;
            
            System.out.println("\n📅 Phiếu ID: " + booking.getId());
            System.out.println("   Phòng: " + (room != null ? room.getName() : "N/A"));
            System.out.println("   Từ: " + booking.getStartTime().format(dateFormatter));
            System.out.println("   Đến: " + booking.getEndTime().format(dateFormatter));
            System.out.println("   Trạng thái: " + booking.getStatus());
            System.out.println("   Nhân viên hỗ trợ: " + (supportStaff != null ? supportStaff.getFullname() : "Chưa phân công"));
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== HỦY LỊCH ĐẶT ==========
    private void cancelBooking() {
        List<Booking> bookings = bookingService.getBookingsByUserId(currentUser.getId());
        
        // Lọc chỉ phiếu đặt ở trạng thái PENDING
        List<Booking> cancelableBookings = new java.util.ArrayList<>();
        for (Booking booking : bookings) {
            if ("PENDING".equals(booking.getStatus())) {
                cancelableBookings.add(booking);
            }
        }
        
        if (cancelableBookings.isEmpty()) {
            System.out.println("\n❌ Không có phiếu đặt nào có thể hủy!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          HỦY LỊCH ĐẶT PHÒNG                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("\n📋 Danh sách phiếu đặt có thể hủy:");
        
        for (Booking booking : cancelableBookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            System.out.println("   ID: " + booking.getId() + " | " + 
                             (room != null ? room.getName() : "N/A") +
                             " | Từ: " + booking.getStartTime().format(dateFormatter));
        }
        
        int bookingId = InputUtil.inputPositiveInt("\n🔍 Chọn ID phiếu đặt cần hủy: ");
        
        System.out.print("⚠️  Bạn có chắc chắn muốn hủy phiếu đặt này? (Y/N): ");
        String confirm = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(confirm)) {
            if (bookingService.cancelBooking(bookingId)) {
                System.out.println("✅ Hủy phiếu đặt thành công!");
            } else {
                System.out.println("❌ Hủy phiếu đặt thất bại!");
            }
        } else {
            System.out.println("❌ Đã hủy thao tác!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== XEM PHÒNG KHẢ DỤNG ==========
    private void viewAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("\n❌ Không có phòng khả dụng!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          DANH SÁCH PHÒNG KHẢ DỤNG                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (Room room : rooms) {
            System.out.println("🚪 Phòng ID: " + room.getId());
            System.out.println("   Tên: " + room.getName() + " | Sức chứa: " + room.getCapacity() + " người");
            System.out.println("   Vị trí: " + room.getLocation());
            System.out.println("   Thiết bị: " + room.getFixedEquipment() + "\n");
        }
        
        InputUtil.inputString("📌 Nhấn Enter để tiếp tục...");
    }

    // ========== XEM DỊCH VỤ KHẢ DỤNG ==========
    private void viewAvailableServices() {
        List<Service> services = serviceService.getActiveServices();
        if (services.isEmpty()) {
            System.out.println("\n❌ Không có dịch vụ nào!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          DANH SÁCH DỊCH VỤ KHẢ DỤNG                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (Service service : services) {
            System.out.println("☕ Dịch vụ ID: " + service.getId());
            System.out.println("   Tên: " + service.getName() + " | Giá: " + service.getPrice());
            System.out.println("   Mô tả: " + service.getDescription() + "\n");
        }
        
        InputUtil.inputString("📌 Nhấn Enter để tiếp tục...");
    }

    // ========== CẬP NHẬT HỒ SƠ ==========
    private void showProfileUpdate() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║            CẬP NHẬT HỒ SƠ CÁ NHÂN                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("\n👤 Thông tin hiện tại:");
        System.out.println("   Họ và tên: " + currentUser.getFullname());
        System.out.println("   Điện thoại: " + currentUser.getPhone());
        System.out.println("   Phòng ban: " + currentUser.getDepartment());
        
        System.out.println("\n┌─ MENU CẬP NHẬT ────────────────────────────────────────┐");
        System.out.println("│ 1. Cập nhật thông tin cá nhân                          │");
        System.out.println("│ 2. Đổi mật khẩu                                        │");
        System.out.println("│ 0. Quay lại                                            │");
        System.out.println("└────────────────────────────────────────────────────────┘");
        
        int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 2);
        
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
        String fullname = InputUtil.inputFullName("👤 Họ và tên mới: ");
        String phone = InputUtil.inputPhone("☎️  Số điện thoại mới: ");
        String department = InputUtil.inputNonEmptyString("🏢 Phòng ban mới: ");
        
        if (userService.updateProfile(currentUser.getId(), fullname, phone, department)) {
            System.out.println("✅ Cập nhật hồ sơ thành công!");
            currentUser.setFullname(fullname);
            currentUser.setPhone(phone);
            currentUser.setDepartment(department);
        } else {
            System.out.println("❌ Cập nhật hồ sơ thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void changePassword() {
        String oldPassword = InputUtil.inputPassword("🔐 Mật khẩu cũ: ");
        String newPassword = InputUtil.inputPassword("🔐 Mật khẩu mới: ");
        String confirmPassword = InputUtil.inputPassword("🔐 Xác nhận mật khẩu mới: ");
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("❌ Mật khẩu xác nhận không khớp!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        if (userService.changePassword(currentUser.getId(), oldPassword, newPassword)) {
            System.out.println("✅ Đổi mật khẩu thành công!");
        } else {
            System.out.println("❌ Đổi mật khẩu thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }
}



