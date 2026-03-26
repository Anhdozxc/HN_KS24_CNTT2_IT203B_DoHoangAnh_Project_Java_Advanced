import presentation.MainMenu;

/**
 * Lớp Main - Điểm vào của chương trình
 * Chạy hệ thống quản lý đặt phòng họp & dịch vụ văn phòng
 */
public class Main {
    public static void main(String[] args) {
        // Khởi động giao diện menu chính
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMainMenu();
    }
}

