package presentation;

import model.User;
import service.UserService;
import util.InputUtil;

/**
 * Lớp Menu chính - Giao diện đăng ký, đăng nhập và điều hướng theo vai trò
 */
public class MainMenu {
    private UserService userService;

    public MainMenu() {
        this.userService = new UserService();
    }

    /**
     * Hiển thị menu chính
     */
    public void showMainMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("  HỆ THỐNG QUẢN LÝ ĐẶT PHÒNG HỌP");
            System.out.println("  VÀ DỊCH VỤ VĂN PHÒNG");
            System.out.println("========================================");
            System.out.println("\nMENU CHÍNH:");
            System.out.println("1. Đăng ký tài khoản (nhân viên)");
            System.out.println("2. Đăng nhập");
            System.out.println("3. Thoát chương trình");
            
            int choice = InputUtil.inputChoice("\nNhập lựa chọn: ", 1, 3);
            
            switch (choice) {
                case 1:
                    showRegisterMenu();
                    break;
                case 2:
                    showLoginMenu();
                    break;
                case 3:
                    System.out.println(" Cảm ơn đã sử dụng hệ thống. Tạm biệt!");
                    System.exit(0);
                    break;
            }
        }
    }

    /**
     * Menu đăng ký tài khoản
     */
    private void showRegisterMenu() {
        System.out.println("\n========================================");
        System.out.println("  ĐĂNG KÝ TÀI KHOẢN NHÂN VIÊN");
        System.out.println("========================================");
        
        String username = InputUtil.inputUsername("\nTên đăng nhập (3-20 ký tự): ");
        String password = InputUtil.inputPasswordMasked("Mật khẩu (tối thiểu 6 ký tự): ");
        String passwordConfirm = InputUtil.inputPasswordMasked("Xác nhận mật khẩu: ");
        
        if (!password.equals(passwordConfirm)) {
            System.out.println("Lỗi: Mật khẩu xác nhận không khớp!");
            return;
        }
        
        String fullname = InputUtil.inputFullName("Họ và tên: ");
        String phone = InputUtil.inputPhone("Số điện thoại (10 chữ số): ");
        String department = InputUtil.inputNonEmptyString("Phòng ban: ");
        
        if (userService.registerEmployee(username, password, fullname, phone, department)) {
            System.out.println("Thành công: Đăng ký thành công! Vui lòng đăng nhập.");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
        } else {
            System.out.println("Lỗi: Đăng ký thất bại! Vui lòng kiểm tra lại thông tin.");
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
        }
    }

    /**
     * Menu đăng nhập
     */
    private void showLoginMenu() {
        System.out.println("\n========================================");
        System.out.println("  ĐĂNG NHẬP");
        System.out.println("========================================");
        
        String username = InputUtil.inputNonEmptyString("\nTên đăng nhập: ");
        String password = InputUtil.inputPasswordMasked("Mật khẩu: ");
        
        User user = userService.login(username, password);
        
        if (user != null) {
            switch (user.getRole()) {
                case "EMPLOYEE":
                    EmployeeMenu employeeMenu = new EmployeeMenu(user);
                    employeeMenu.showMenu();
                    break;
                case "SUPPORT_STAFF":
                    SupportStaffMenu supportMenu = new SupportStaffMenu(user);
                    supportMenu.showMenu();
                    break;
                case "ADMIN":
                    AdminMenu adminMenu = new AdminMenu(user);
                    adminMenu.showMenu();
                    break;
                default:
                    System.out.println("Lỗi: Vai trò không xác định!");
            }
        } else {
            InputUtil.inputString("\nNhấn Enter để tiếp tục...");
        }
    }

    /**
     * Điểm vào chương trình
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMainMenu();
    }
}


