package presentation;

import model.*;
import service.*;
import util.InputUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminMenu {
    private User currentUser;
    private UserService userService;
    private RoomService roomService;
    private EquipmentService equipmentService;

    public AdminMenu(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.equipmentService = new EquipmentService();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  MENU QUẢN TRỊ VIÊN");
            System.out.println("========================================");
            System.out.println("Xin chào: " + currentUser.getFullname());

            System.out.println("\n1. Quản lý phòng họp");
            System.out.println("2. Quản lý thiết bị");
            System.out.println("3. Quản lý nhân viên hỗ trợ");
            System.out.println("4. Đăng xuất");

            int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 1, 4);

            switch (choice) {
                case 1:
                    roomMenu();
                    break;
                case 2:
                    equipmentMenu();
                    break;
                case 3:
                    userMenu();
                    break;
                case 4:
                    System.out.println(" Đã đăng xuất. Tạm biệt!");
                    return;
            }
        }
    }

    //  ROOM MENU
    private void roomMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ PHÒNG HỌP =====");
            System.out.println("1. Xem danh sách phòng");
            System.out.println("2. Thêm phòng mới");
            System.out.println("3. Cập nhật thông tin phòng");
            System.out.println("4. Cập nhật sức chứa phòng");
            System.out.println("5. Cập nhật trạng thái phòng");
            System.out.println("6. Xóa phòng");
            System.out.println("0. Quay lại");

            int choice = InputUtil.inputChoice("\nChọn: ", 0, 6);

            switch (choice) {
                case 1:
                    viewRooms();
                    break;
                case 2:
                    addRoom();
                    break;
                case 3:
                    updateRoom();
                    break;
                case 4:
                    updateRoomCapacity();
                    break;
                case 5:
                    updateRoomStatus();
                    break;
                case 6:
                    deleteRoom();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewRooms() {
        List<Room> list = roomService.getAllRooms();
        
        if (list.isEmpty()) {
            System.out.println("\nChưa có phòng nào trong hệ thống");
            return;
        }

        System.out.println("\n========== DANH SÁCH PHÒNG ==========");
        for (Room r : list) {
            System.out.println("\nID: " + r.getId());
            System.out.println("   Tên: " + r.getName());
            System.out.println("   Sức chứa: " + r.getCapacity() + " người");
            System.out.println("   Vị trí: " + r.getLocation());
            System.out.println("   Thiết bị cố định: " + r.getFixedEquipment());
            System.out.println("   Trạng thái: " + r.getStatus());
        }
    }

    private void addRoom() {
        System.out.println("\n===== THÊM PHÒNG MỚI =====");
        String name = InputUtil.inputNonEmptyString("Tên phòng: ");
        int capacity = InputUtil.inputPositiveInt("Sức chứa (số người): ");
        String location = InputUtil.inputNonEmptyString("Vị trí: ");
        String equipment = InputUtil.inputNonEmptyString("Thiết bị cố định (ngăn cách bằng dấu phẩy): ");

        if (roomService.addRoom(name, capacity, location, equipment)) {
            System.out.println("Thành công: Thêm phòng mới!");
        } else {
            System.out.println("Lỗi: Thêm phòng thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void updateRoom() {
        System.out.println("\n===== CẬP NHẬT THÔNG TIN PHÒNG =====");
        int id = InputUtil.inputPositiveInt("ID phòng cần cập nhật: ");
        
        Room room = roomService.getRoomById(id);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        String name = InputUtil.inputNonEmptyString("Tên mới (hiện tại: " + room.getName() + "): ");
        int capacity = InputUtil.inputPositiveInt("Sức chứa mới (hiện tại: " + room.getCapacity() + "): ");
        String location = InputUtil.inputNonEmptyString("Vị trí mới (hiện tại: " + room.getLocation() + "): ");
        String equipment = InputUtil.inputNonEmptyString("Thiết bị mới (hiện tại: " + room.getFixedEquipment() + "): ");

        if (roomService.updateRoom(id, name, capacity, location, equipment, room.getStatus())) {
            System.out.println("Thành công: Cập nhật phòng!");
        } else {
            System.out.println("Lỗi: Cập nhật phòng thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void updateRoomCapacity() {
        System.out.println("\n===== CẬP NHẬT SỨC CHỨA PHÒNG =====");
        int id = InputUtil.inputPositiveInt("ID phòng: ");
        
        Room room = roomService.getRoomById(id);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("Sức chứa hiện tại: " + room.getCapacity());
        int newCapacity = InputUtil.inputPositiveInt("Sức chứa mới: ");
        
        if (roomService.updateRoomCapacity(id, newCapacity)) {
            System.out.println("Thành công: Cập nhật sức chứa!");
        } else {
            System.out.println("Lỗi: Cập nhật sức chứa thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void updateRoomStatus() {
        System.out.println("\n===== CẬP NHẬT TRẠNG THÁI PHÒNG =====");
        int id = InputUtil.inputPositiveInt("ID phòng: ");
        
        Room room = roomService.getRoomById(id);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("Trạng thái hiện tại: " + room.getStatus());
        System.out.println("1. AVAILABLE (Có sẵn)");
        System.out.println("2. MAINTENANCE (Bảo trì)");
        
        int choice = InputUtil.inputChoice("\nChọn trạng thái: ", 1, 2);
        String status = choice == 1 ? "AVAILABLE" : "MAINTENANCE";
        
        if (roomService.updateRoomStatus(id, status)) {
            System.out.println("Thành công: Cập nhật trạng thái!");
        } else {
            System.out.println("Lỗi: Cập nhật trạng thái thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void deleteRoom() {
        System.out.println("\n===== XÓA PHÒNG =====");
        int id = InputUtil.inputPositiveInt("ID phòng cần xóa: ");
        
        Room room = roomService.getRoomById(id);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        String confirm = InputUtil.inputNonEmptyString("Bạn chắc chắn muốn xóa phòng '" + room.getName() + "'? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            if (roomService.deleteRoom(id)) {
                System.out.println("Thành công: Xóa phòng!");
            } else {
                System.out.println("Lỗi: Xóa phòng thất bại!");
            }
        } else {
            System.out.println("Đã hủy bỏ thao tác");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    //  EQUIPMENT MENU
    private void equipmentMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ THIẾT BỊ =====");
            System.out.println("1. Xem danh sách thiết bị");
            System.out.println("2. Thêm thiết bị mới");
            System.out.println("3. Cập nhật số lượng khả dụng");
            System.out.println("4. Cập nhật trạng thái thiết bị");
            System.out.println("0. Quay lại");

            int choice = InputUtil.inputChoice("\nChọn: ", 0, 4);

            switch (choice) {
                case 1:
                    viewEquipment();
                    break;
                case 2:
                    addEquipment();
                    break;
                case 3:
                    updateAvailableQuantity();
                    break;
                case 4:
                    updateEquipmentStatus();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewEquipment() {
        List<Equipment> list = equipmentService.getAllEquipment();
        
        if (list.isEmpty()) {
            System.out.println("\nChưa có thiết bị nào trong hệ thống");
            return;
        }

        System.out.println("\n========== DANH SÁCH THIẾT BỊ ==========");
        for (Equipment e : list) {
            System.out.println("\nID: " + e.getId());
            System.out.println("   Tên: " + e.getName());
            System.out.println("   Tổng số lượng: " + e.getTotalQuantity());
            System.out.println("   Số lượng khả dụng: " + e.getAvailableQuantity());
            System.out.println("   Đang sử dụng: " + (e.getTotalQuantity() - e.getAvailableQuantity()));
            System.out.println("   Trạng thái: " + e.getStatus());
        }
    }

    private void addEquipment() {
        System.out.println("\n===== THÊM THIẾT BỊ MỚI =====");
        String name = InputUtil.inputNonEmptyString("Tên thiết bị: ");
        int quantity = InputUtil.inputPositiveInt("Số lượng ban đầu: ");

        if (equipmentService.addEquipment(name, quantity, "ACTIVE")) {
            System.out.println("Thành công: Thêm thiết bị mới!");
        } else {
            System.out.println("Lỗi: Thêm thiết bị thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void updateAvailableQuantity() {
        System.out.println("\n===== CẬP NHẬT SỐ LƯỢNG KHẢ DỤng =====");
        int id = InputUtil.inputPositiveInt("ID thiết bị: ");
        
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) {
            System.out.println("Lỗi: Thiết bị không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("Tên thiết bị: " + equipment.getName());
        System.out.println("Tổng số lượng: " + equipment.getTotalQuantity());
        System.out.println("Số lượng hiện tại: " + equipment.getAvailableQuantity());
        
        int newQuantity = InputUtil.inputPositiveInt("Số lượng mới: ");
        
        if (equipmentService.updateAvailableQuantity(id, newQuantity)) {
            System.out.println("Thành công: Cập nhật số lượng!");
        } else {
            System.out.println("Lỗi: Cập nhật số lượng thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void updateEquipmentStatus() {
        System.out.println("\n===== CẬP NHẬT TRẠNG THÁI THIẾT BỊ =====");
        int id = InputUtil.inputPositiveInt("ID thiết bị: ");
        
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) {
            System.out.println("Lỗi: Thiết bị không tồn tại!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        System.out.println("Tên thiết bị: " + equipment.getName());
        System.out.println("Trạng thái hiện tại: " + equipment.getStatus());
        System.out.println("1. ACTIVE (Hoạt động)");
        System.out.println("2. INACTIVE (Không hoạt động)");
        
        int choice = InputUtil.inputChoice("\nChọn trạng thái: ", 1, 2);
        String status = choice == 1 ? "ACTIVE" : "INACTIVE";
        
        if (equipmentService.updateEquipmentStatus(id, status)) {
            System.out.println("Thành công: Cập nhật trạng thái!");
        } else {
            System.out.println("Lỗi: Cập nhật trạng thái thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    //  USER MENU
    private void userMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ NHÂN VIÊN HỖ TRỢ =====");
            System.out.println("1. Xem danh sách nhân viên hỗ trợ");
            System.out.println("2. Tạo tài khoản nhân viên hỗ trợ mới");
            System.out.println("0. Quay lại");

            int choice = InputUtil.inputChoice("\nChọn: ", 0, 2);

            switch (choice) {
                case 1:
                    viewSupportStaff();
                    break;
                case 2:
                    createSupportStaffAccount();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewSupportStaff() {
        List<User> staffList = userService.getAllSupportStaff();
        
        if (staffList.isEmpty()) {
            System.out.println("\nChưa có nhân viên hỗ trợ nào trong hệ thống");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }

        System.out.println("\n========== DANH SÁCH NHÂN VIÊN HỖ TRỢ ==========");
        for (User staff : staffList) {
            System.out.println("\nID: " + staff.getId());
            System.out.println("   Tài khoản: " + staff.getUsername());
            System.out.println("   Họ tên: " + staff.getFullname());
            System.out.println("   Điện thoại: " + staff.getPhone());
            System.out.println("   Phòng ban: " + staff.getDepartment());
            System.out.println("   Trạng thái: " + staff.getStatus());
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }

    private void createSupportStaffAccount() {
        System.out.println("\n===== TẠO TÀI KHOẢN NHÂN VIÊN HỖ TRỢ =====");
        String username = InputUtil.inputUsername("Tên đăng nhập (3-20 ký tự): ");
        String password = InputUtil.inputPassword("Mật khẩu (tối thiểu 6 ký tự): ");
        String passwordConfirm = InputUtil.inputPassword("Xác nhận mật khẩu: ");
        
        if (!password.equals(passwordConfirm)) {
            System.out.println("Lỗi: Mật khẩu xác nhận không khớp!");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
            return;
        }
        
        String fullname = InputUtil.inputFullName("Họ và tên: ");
        String phone = InputUtil.inputPhone("Số điện thoại (10 chữ số): ");
        String department = InputUtil.inputNonEmptyString("Phòng ban: ");

        if (userService.createSupportStaffAccount(username, password, fullname, phone, department)) {
            System.out.println("Thành công: Tạo tài khoản nhân viên hỗ trợ!");
        } else {
            System.out.println("Lỗi: Tạo tài khoản thất bại!");
        }
        InputUtil.inputString("\nNhấn Enter để tiếp tục...");
    }
}