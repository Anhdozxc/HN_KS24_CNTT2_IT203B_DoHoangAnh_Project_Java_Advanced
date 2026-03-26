package presentation;

import model.*;
import service.*;
import util.InputUtil;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Lớp Menu cho Quản trị viên (Admin)
 * Quản lý phòng, thiết bị, người dùng, duyệt đặt phòng...
 */
public class AdminMenu {
    private User currentUser;
    private UserService userService;
    private RoomService roomService;
    private EquipmentService equipmentService;
    private ServiceService serviceService;
    private BookingService bookingService;
    private DateTimeFormatter dateFormatter;

    public AdminMenu(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.equipmentService = new EquipmentService();
        this.serviceService = new ServiceService();
        this.bookingService = new BookingService();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    /**
     * Hiển thị menu chính của Admin
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  MENU QUẢN TRỊ VIÊN");
            System.out.println("========================================");
            System.out.println("Xin chào: " + currentUser.getFullname());
            System.out.println("\n-- Chức năng --");
            System.out.println("1. Quản lý Phòng họp");
            System.out.println("2. Quản lý Thiết bị");
            System.out.println("3. Quản lý Dịch vụ");
            System.out.println("4. Quản lý Người dùng");
            System.out.println("5. Duyệt & Phân công Đặt phòng");
            System.out.println("6. Xem Thống kê");
            System.out.println("7. Cập nhật Hồ sơ cá nhân");
            System.out.println("8. Đăng xuất");
            
            int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 1, 8);
            
            switch (choice) {
                case 1:
                    showRoomManagement();
                    break;
                case 2:
                    showEquipmentManagement();
                    break;
                case 3:
                    showServiceManagement();
                    break;
                case 4:
                    showUserManagement();
                    break;
                case 5:
                    showBookingApproval();
                    break;
                case 6:
                    showStatistics();
                    break;
                case 7:
                    showProfileUpdate();
                    break;
                case 8:
                    System.out.println("✅ Đã đăng xuất. Tạm biệt!");
                    return;
            }
        }
    }

    // ========== QUẢN LÝ PHÒNG ==========
    private void showRoomManagement() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              QUẢN LÝ PHÒNG HỌP                        ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("\n┌─ MENU PHÒNG ───────────────────────────────────────────┐");
            System.out.println("│ 1. Xem danh sách phòng                                 │");
            System.out.println("│ 2. Thêm phòng mới                                      │");
            System.out.println("│ 3. Cập nhật thông tin phòng                            │");
            System.out.println("│ 4. Cập nhật trạng thái phòng                           │");
            System.out.println("│ 5. Xóa phòng                                           │");
            System.out.println("│ 0. Quay lại                                            │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 5);
            
            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
                case 2:
                    addNewRoom();
                    break;
                case 3:
                    updateRoomInfo();
                    break;
                case 4:
                    updateRoomStatus();
                    break;
                case 5:
                    deleteRoom();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("❌ Không có phòng nào trong hệ thống!");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              DANH SÁCH PHÒNG HỌP                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (Room room : rooms) {
            System.out.println("🚪 Phòng ID: " + room.getId());
            System.out.println("   Tên: " + room.getName() + " | Sức chứa: " + room.getCapacity() + " người");
            System.out.println("   Vị trí: " + room.getLocation() + " | Thiết bị: " + room.getFixedEquipment());
            System.out.println("   Trạng thái: " + room.getStatus() + "\n");
        }
        System.out.println("✅ Tổng cộng: " + rooms.size() + " phòng");
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void addNewRoom() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              THÊM PHÒNG MỚI                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        String name = InputUtil.inputNonEmptyString("\n🚪 Tên phòng: ");
        int capacity = InputUtil.inputPositiveInt("👥 Sức chứa (số người): ");
        String location = InputUtil.inputNonEmptyString("📍 Vị trí phòng: ");
        String fixedEquipment = InputUtil.inputNonEmptyString("🔧 Thiết bị cố định: ");
        
        if (roomService.addRoom(name, capacity, location, fixedEquipment)) {
            System.out.println("✅ Thêm phòng thành công!");
        } else {
            System.out.println("❌ Thêm phòng thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateRoomInfo() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("❌ Không có phòng nào!");
            return;
        }
        
        viewAllRooms();
        int roomId = InputUtil.inputPositiveInt("🔍 Nhập ID phòng cần cập nhật: ");
        Room room = roomService.getRoomById(roomId);
        
        if (room == null) {
            System.out.println("❌ Phòng không tồn tại!");
            return;
        }
        
        String name = InputUtil.inputNonEmptyString("🚪 Tên phòng mới: ");
        int capacity = InputUtil.inputPositiveInt("👥 Sức chứa mới: ");
        String location = InputUtil.inputNonEmptyString("📍 Vị trí mới: ");
        String equipment = InputUtil.inputNonEmptyString("🔧 Thiết bị cố định mới: ");
        
        if (roomService.updateRoom(roomId, name, capacity, location, equipment, room.getStatus())) {
            System.out.println("✅ Cập nhật phòng thành công!");
        } else {
            System.out.println("❌ Cập nhật phòng thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateRoomStatus() {
        viewAllRooms();
        int roomId = InputUtil.inputPositiveInt("🔍 Nhập ID phòng: ");
        
        System.out.println("\n🔄 Trạng thái phòng:");
        System.out.println("1. AVAILABLE (Khả dụng)");
        System.out.println("2. MAINTENANCE (Bảo trì)");
        int choice = InputUtil.inputChoice("👉 Chọn trạng thái: ", 1, 2);
        
        String status = choice == 1 ? "AVAILABLE" : "MAINTENANCE";
        
        if (roomService.updateRoomStatus(roomId, status)) {
            System.out.println("✅ Cập nhật trạng thái phòng thành công!");
        } else {
            System.out.println("❌ Cập nhật trạng thái thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void deleteRoom() {
        viewAllRooms();
        int roomId = InputUtil.inputPositiveInt("🔍 Nhập ID phòng cần xóa: ");
        
        System.out.print("⚠️  Bạn có chắc chắn muốn xóa phòng này? (Y/N): ");
        String confirm = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(confirm)) {
            if (roomService.deleteRoom(roomId)) {
                System.out.println("✅ Xóa phòng thành công!");
            } else {
                System.out.println("❌ Xóa phòng thất bại!");
            }
        } else {
            System.out.println("❌ Đã hủy xóa!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== QUẢN LÝ THIẾT BỊ ==========
    private void showEquipmentManagement() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              QUẢN LÝ THIẾT BỊ                        ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("\n┌─ MENU THIẾT BỊ ────────────────────────────────────────┐");
            System.out.println("│ 1. Xem danh sách thiết bị                              │");
            System.out.println("│ 2. Thêm thiết bị mới                                   │");
            System.out.println("│ 3. Cập nhật số lượng khả dụng                          │");
            System.out.println("│ 4. Cập nhật trạng thái thiết bị                        │");
            System.out.println("│ 5. Xóa thiết bị                                        │");
            System.out.println("│ 0. Quay lại                                            │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 5);
            
            switch (choice) {
                case 1:
                    viewAllEquipment();
                    break;
                case 2:
                    addNewEquipment();
                    break;
                case 3:
                    updateEquipmentQuantity();
                    break;
                case 4:
                    updateEquipmentStatus();
                    break;
                case 5:
                    deleteEquipment();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewAllEquipment() {
        List<Equipment> equipments = equipmentService.getAllEquipment();
        if (equipments.isEmpty()) {
            System.out.println("❌ Không có thiết bị nào trong hệ thống!");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              DANH SÁCH THIẾT BỊ                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (Equipment equipment : equipments) {
            System.out.println("🔧 Thiết bị ID: " + equipment.getId());
            System.out.println("   Tên: " + equipment.getName());
            System.out.println("   Tổng số: " + equipment.getTotalQuantity() + " | Khả dụng: " + equipment.getAvailableQuantity());
            System.out.println("   Trạng thái: " + equipment.getStatus() + "\n");
        }
        System.out.println("✅ Tổng cộng: " + equipments.size() + " loại thiết bị");
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void addNewEquipment() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              THÊM THIẾT BỊ MỚI                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        String name = InputUtil.inputNonEmptyString("\n🔧 Tên thiết bị: ");
        int quantity = InputUtil.inputPositiveInt("📦 Số lượng: ");
        
        if (equipmentService.addEquipment(name, quantity, "ACTIVE")) {
            System.out.println("✅ Thêm thiết bị thành công!");
        } else {
            System.out.println("❌ Thêm thiết bị thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateEquipmentQuantity() {
        viewAllEquipment();
        int equipmentId = InputUtil.inputPositiveInt("🔍 Nhập ID thiết bị: ");
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        
        if (equipment == null) {
            System.out.println("❌ Thiết bị không tồn tại!");
            return;
        }
        
        System.out.println("\n📦 Thông tin hiện tại:");
        System.out.println("   Tổng số lượng: " + equipment.getTotalQuantity());
        System.out.println("   Số lượng khả dụng: " + equipment.getAvailableQuantity());
        
        int newQuantity = InputUtil.inputInt("📝 Nhập số lượng khả dụng mới: ");
        
        if (equipmentService.updateAvailableQuantity(equipmentId, newQuantity)) {
            System.out.println("✅ Cập nhật số lượng thành công!");
        } else {
            System.out.println("❌ Cập nhật số lượng thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateEquipmentStatus() {
        viewAllEquipment();
        int equipmentId = InputUtil.inputPositiveInt("🔍 Nhập ID thiết bị: ");
        
        System.out.println("\n🔄 Trạng thái thiết bị:");
        System.out.println("1. ACTIVE (Hoạt động)");
        System.out.println("2. INACTIVE (Không hoạt động)");
        int choice = InputUtil.inputChoice("👉 Chọn trạng thái: ", 1, 2);
        
        String status = choice == 1 ? "ACTIVE" : "INACTIVE";
        
        if (equipmentService.updateEquipmentStatus(equipmentId, status)) {
            System.out.println("✅ Cập nhật trạng thái thành công!");
        } else {
            System.out.println("❌ Cập nhật trạng thái thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void deleteEquipment() {
        viewAllEquipment();
        int equipmentId = InputUtil.inputPositiveInt("🔍 Nhập ID thiết bị cần xóa: ");
        
        System.out.print("⚠️  Bạn có chắc chắn muốn xóa thiết bị này? (Y/N): ");
        String confirm = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(confirm)) {
            if (equipmentService.deleteEquipment(equipmentId)) {
                System.out.println("✅ Xóa thiết bị thành công!");
            } else {
                System.out.println("❌ Xóa thiết bị thất bại!");
            }
        } else {
            System.out.println("❌ Đã hủy xóa!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== QUẢN LÝ DỊCH VỤ ==========
    private void showServiceManagement() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              QUẢN LÝ DỊCH VỤ                         ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("\n┌─ MENU DỊCH VỤ ─────────────────────────────────────────┐");
            System.out.println("│ 1. Xem danh sách dịch vụ                               │");
            System.out.println("│ 2. Thêm dịch vụ mới                                    │");
            System.out.println("│ 3. Cập nhật giá dịch vụ                                │");
            System.out.println("│ 4. Cập nhật trạng thái dịch vụ                         │");
            System.out.println("│ 5. Xóa dịch vụ                                         │");
            System.out.println("│ 0. Quay lại                                            │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 5);
            
            switch (choice) {
                case 1:
                    viewAllServices();
                    break;
                case 2:
                    addNewService();
                    break;
                case 3:
                    updateServicePrice();
                    break;
                case 4:
                    updateServiceStatus();
                    break;
                case 5:
                    deleteService();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewAllServices() {
        List<Service> services = serviceService.getAllServices();
        if (services.isEmpty()) {
            System.out.println("❌ Không có dịch vụ nào trong hệ thống!");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              DANH SÁCH DỊCH VỤ                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (Service service : services) {
            System.out.println("☕ Dịch vụ ID: " + service.getId());
            System.out.println("   Tên: " + service.getName() + " | Giá: " + service.getPrice());
            System.out.println("   Mô tả: " + service.getDescription());
            System.out.println("   Trạng thái: " + service.getStatus() + "\n");
        }
        System.out.println("✅ Tổng cộng: " + services.size() + " loại dịch vụ");
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void addNewService() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              THÊM DỊCH VỤ MỚI                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        String name = InputUtil.inputNonEmptyString("\n☕ Tên dịch vụ: ");
        double price = InputUtil.inputDouble("💰 Giá dịch vụ: ");
        String description = InputUtil.inputNonEmptyString("📝 Mô tả: ");
        
        if (serviceService.addService(name, price, description)) {
            System.out.println("✅ Thêm dịch vụ thành công!");
        } else {
            System.out.println("❌ Thêm dịch vụ thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateServicePrice() {
        viewAllServices();
        int serviceId = InputUtil.inputPositiveInt("🔍 Nhập ID dịch vụ: ");
        double newPrice = InputUtil.inputDouble("💰 Giá dịch vụ mới: ");
        
        if (serviceService.updatePrice(serviceId, newPrice)) {
            System.out.println("✅ Cập nhật giá dịch vụ thành công!");
        } else {
            System.out.println("❌ Cập nhật giá dịch vụ thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void updateServiceStatus() {
        viewAllServices();
        int serviceId = InputUtil.inputPositiveInt("🔍 Nhập ID dịch vụ: ");
        
        System.out.println("\n🔄 Trạng thái dịch vụ:");
        System.out.println("1. ACTIVE (Hoạt động)");
        System.out.println("2. INACTIVE (Không hoạt động)");
        int choice = InputUtil.inputChoice("👉 Chọn trạng thái: ", 1, 2);
        
        String status = choice == 1 ? "ACTIVE" : "INACTIVE";
        
        if (serviceService.updateServiceStatus(serviceId, status)) {
            System.out.println("✅ Cập nhật trạng thái thành công!");
        } else {
            System.out.println("❌ Cập nhật trạng thái thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void deleteService() {
        viewAllServices();
        int serviceId = InputUtil.inputPositiveInt("🔍 Nhập ID dịch vụ cần xóa: ");
        
        System.out.print("⚠️  Bạn có chắc chắn muốn xóa dịch vụ này? (Y/N): ");
        String confirm = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(confirm)) {
            if (serviceService.deleteService(serviceId)) {
                System.out.println("✅ Xóa dịch vụ thành công!");
            } else {
                System.out.println("❌ Xóa dịch vụ thất bại!");
            }
        } else {
            System.out.println("❌ Đã hủy xóa!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== QUẢN LÝ NGƯỜI DÙNG ==========
    private void showUserManagement() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              QUẢN LÝ NGƯỜI DÙNG                      ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("\n┌─ MENU NGƯỜI DÙNG ──────────────────────────────────────┐");
            System.out.println("│ 1. Xem danh sách nhân viên hỗ trợ                      │");
            System.out.println("│ 2. Tạo tài khoản nhân viên hỗ trợ                      │");
            System.out.println("│ 3. Vô hiệu hóa tài khoản                               │");
            System.out.println("│ 4. Kích hoạt tài khoản                                 │");
            System.out.println("│ 5. Xóa tài khoản                                       │");
            System.out.println("│ 0. Quay lại                                            │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 5);
            
            switch (choice) {
                case 1:
                    viewAllSupportStaff();
                    break;
                case 2:
                    createSupportStaffAccount();
                    break;
                case 3:
                    deactivateAccount();
                    break;
                case 4:
                    activateAccount();
                    break;
                case 5:
                    deleteAccount();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewAllSupportStaff() {
        List<User> staffs = userService.getAllSupportStaff();
        if (staffs.isEmpty()) {
            System.out.println("❌ Không có nhân viên hỗ trợ nào!");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║        DANH SÁCH NHÂN VIÊN HỖ TRỢ                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝\n");
        
        for (User staff : staffs) {
            System.out.println("👤 ID: " + staff.getId() + " | " + staff.getFullname() +
                             " | Username: " + staff.getUsername() +
                             " | Điện thoại: " + staff.getPhone() +
                             " | Trạng thái: " + staff.getStatus());
        }
        System.out.println("\n✅ Tổng cộng: " + staffs.size() + " nhân viên hỗ trợ");
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void createSupportStaffAccount() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║      TẠO TÀI KHOẢN NHÂN VIÊN HỖ TRỢ                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        String username = InputUtil.inputUsername("\n📝 Tên đăng nhập: ");
        String password = InputUtil.inputPassword("🔐 Mật khẩu: ");
        String fullname = InputUtil.inputFullName("👤 Họ và tên: ");
        String phone = InputUtil.inputPhone("☎️  Số điện thoại: ");
        String department = InputUtil.inputNonEmptyString("🏢 Phòng ban: ");
        
        if (userService.createSupportStaffAccount(username, password, fullname, phone, department)) {
            System.out.println("✅ Tạo tài khoản nhân viên hỗ trợ thành công!");
        } else {
            System.out.println("❌ Tạo tài khoản thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void deactivateAccount() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n👤 Danh sách người dùng:");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " | " + user.getFullname() +
                             " | Trạng thái: " + user.getStatus());
        }
        
        int userId = InputUtil.inputPositiveInt("\n🔍 Nhập ID người dùng cần vô hiệu hóa: ");
        
        if (userService.deactivateAccount(userId)) {
            System.out.println("✅ Vô hiệu hóa tài khoản thành công!");
        } else {
            System.out.println("❌ Vô hiệu hóa tài khoản thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void activateAccount() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n👤 Danh sách người dùng:");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " | " + user.getFullname() +
                             " | Trạng thái: " + user.getStatus());
        }
        
        int userId = InputUtil.inputPositiveInt("\n🔍 Nhập ID người dùng cần kích hoạt: ");
        
        if (userService.activateAccount(userId)) {
            System.out.println("✅ Kích hoạt tài khoản thành công!");
        } else {
            System.out.println("❌ Kích hoạt tài khoản thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void deleteAccount() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n👤 Danh sách người dùng:");
        for (User user : users) {
            System.out.println("ID: " + user.getId() + " | " + user.getFullname());
        }
        
        int userId = InputUtil.inputPositiveInt("\n🔍 Nhập ID người dùng cần xóa: ");
        
        System.out.print("⚠️  Bạn có chắc chắn muốn xóa tài khoản này? (Y/N): ");
        String confirm = InputUtil.inputNonEmptyString("").toUpperCase();
        
        if ("Y".equals(confirm)) {
            if (userService.deleteAccount(userId)) {
                System.out.println("✅ Xóa tài khoản thành công!");
            } else {
                System.out.println("❌ Xóa tài khoản thất bại!");
            }
        } else {
            System.out.println("❌ Đã hủy xóa!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== DUYỆT & PHÂN CÔNG ĐẶT PHÒNG ==========
    private void showBookingApproval() {
        while (true) {
            List<Booking> pendingBookings = bookingService.getPendingBookings();
            
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║         DUYỆT & PHÂN CÔNG ĐẶT PHÒNG                   ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("📋 Số phiếu đặt chờ duyệt: " + pendingBookings.size());
            
            System.out.println("\n┌─ MENU DUYỆT ───────────────────────────────────────────┐");
            System.out.println("│ 1. Xem phiếu đặt chờ duyệt                             │");
            System.out.println("│ 2. Duyệt phiếu đặt & phân công                         │");
            System.out.println("│ 3. Từ chối phiếu đặt                                   │");
            System.out.println("│ 4. Xem tất cả phiếu đặt                                │");
            System.out.println("│ 0. Quay lại                                            │");
            System.out.println("└────────────────────────────────────────────────────────┘");
            
            int choice = InputUtil.inputChoice("👉 Nhập lựa chọn: ", 0, 4);
            
            switch (choice) {
                case 1:
                    viewPendingBookings();
                    break;
                case 2:
                    approveBooking();
                    break;
                case 3:
                    rejectBooking();
                    break;
                case 4:
                    viewAllBookings();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewPendingBookings() {
        List<Booking> bookings = bookingService.getPendingBookings();
        if (bookings.isEmpty()) {
            System.out.println("✅ Không có phiếu đặt chờ duyệt!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║      DANH SÁCH PHIẾU ĐẶT PHÒNG CHỜ DUYỆT              ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            User user = userService.getUserById(booking.getUserId());
            
            System.out.println("\n📅 Phiếu ID: " + booking.getId());
            System.out.println("   Nhân viên: " + (user != null ? user.getFullname() : "N/A"));
            System.out.println("   Phòng: " + (room != null ? room.getName() : "N/A"));
            System.out.println("   Từ: " + booking.getStartTime().format(dateFormatter));
            System.out.println("   Đến: " + booking.getEndTime().format(dateFormatter));
            System.out.println("   Số người: " + booking.getParticipantCount());
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void approveBooking() {
        viewPendingBookings();
        int bookingId = InputUtil.inputPositiveInt("🔍 Nhập ID phiếu đặt cần duyệt: ");
        
        List<User> supportStaffs = userService.getAllSupportStaff();
        if (supportStaffs.isEmpty()) {
            System.out.println("❌ Chưa có nhân viên hỗ trợ nào!");
            InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("\n👥 Danh sách nhân viên hỗ trợ:");
        for (User staff : supportStaffs) {
            System.out.println("ID: " + staff.getId() + " | " + staff.getFullname());
        }
        
        int supportStaffId = InputUtil.inputPositiveInt("🔍 Nhập ID nhân viên hỗ trợ: ");
        
        if (bookingService.approveBooking(bookingId, supportStaffId)) {
            System.out.println("✅ Duyệt phiếu đặt & phân công thành công!");
        } else {
            System.out.println("❌ Duyệt phiếu đặt thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void rejectBooking() {
        viewPendingBookings();
        int bookingId = InputUtil.inputPositiveInt("🔍 Nhập ID phiếu đặt cần từ chối: ");
        
        if (bookingService.rejectBooking(bookingId)) {
            System.out.println("✅ Từ chối phiếu đặt thành công!");
        } else {
            System.out.println("❌ Từ chối phiếu đặt thất bại!");
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    private void viewAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("❌ Không có phiếu đặt nào!");
            return;
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║          DANH SÁCH TẤT CẢ PHIẾU ĐẶT PHÒNG             ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        for (Booking booking : bookings) {
            Room room = roomService.getRoomById(booking.getRoomId());
            System.out.println("\n📅 Phiếu ID: " + booking.getId() +
                             " | Phòng: " + (room != null ? room.getName() : "N/A") +
                             " | Trạng thái: " + booking.getStatus());
            System.out.println("   Từ: " + booking.getStartTime().format(dateFormatter) +
                             " Đến: " + booking.getEndTime().format(dateFormatter));
        }
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
    }

    // ========== THỐNG KÊ ==========
    private void showStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║              THỐNG KÊ HỆ THỐNG                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        System.out.println("\n📊 THỐNG KÊ:");
        System.out.println("   👥 Tổng số người dùng: " + userService.getTotalUsers());
        System.out.println("   🚪 Tổng số phòng: " + roomService.getTotalRooms());
        System.out.println("   🔧 Tổng số thiết bị: " + equipmentService.getTotalEquipment());
        System.out.println("   ☕ Tổng số dịch vụ: " + serviceService.getTotalServices());
        System.out.println("   📅 Tổng số phiếu đặt: " + bookingService.getTotalBookings());
        System.out.println("      - Chờ duyệt: " + bookingService.countByStatus("PENDING"));
        System.out.println("      - Đã duyệt: " + bookingService.countByStatus("APPROVED"));
        System.out.println("      - Từ chối: " + bookingService.countByStatus("REJECTED"));
        System.out.println("      - Hoàn thành: " + bookingService.countByStatus("DONE"));
        
        InputUtil.inputString("\n📌 Nhấn Enter để tiếp tục...");
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



