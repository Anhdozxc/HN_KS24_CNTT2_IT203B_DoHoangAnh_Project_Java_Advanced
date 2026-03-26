package service;

import dao.UserDao;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;
import java.util.List;

/**
 * Lớp Service xử lý logic nghiệp vụ liên quan đến người dùng
 * Bao gồm: đăng ký, đăng nhập, quản lý người dùng
 */
public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    // ========== ĐĂNG KÝ TÀI KHOẢN ==========
    /**
     * Đăng ký tài khoản mới cho nhân viên
     * @return true nếu đăng ký thành công
     */
    public boolean registerEmployee(String username, String password, String fullname, 
                                    String phone, String department) {
        // Kiểm tra hợp lệ
        if (!ValidationUtil.isValidUsername(username)) {
            System.out.println("Lỗi: Username không hợp lệ (3-20 ký tự, chữ/số/gạch dưới)");
            return false;
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Lỗi: Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        
        if (!ValidationUtil.isNotEmpty(fullname)) {
            System.out.println("Lỗi: Tên đầy đủ không được để trống");
            return false;
        }
        
        if (!ValidationUtil.isValidPhone(phone)) {
            System.out.println("Lỗi: Số điện thoại phải 10 chữ số");
            return false;
        }
        
        // Kiểm tra username đã tồn tại
        if (userDao.usernameExists(username)) {
            System.out.println("Lỗi: Username đã tồn tại!");
            return false;
        }
        
        // Mã hóa mật khẩu
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        // Tạo user mới
        User newUser = new User(username, hashedPassword, "EMPLOYEE", fullname, phone, department);
        
        return userDao.addUser(newUser);
    }

    // ========== ĐĂNG NHẬP ==========
    public User login(String username, String password) {
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            System.out.println("Lỗi: Username hoặc mật khẩu không được để trống");
            return null;
        }
        
        // Tìm user theo username
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            System.out.println("Lỗi: Tên đăng nhập không tồn tại");
            return null;
        }
        
        // Kiểm tra trạng thái tài khoản
        if (!"ACTIVE".equals(user.getStatus())) {
            System.out.println("Lỗi: Tài khoản đã bị vô hiệu hóa");
            return null;
        }
        
        // Mã hóa mật khẩu nhập và so sánh
        String hashedPassword = PasswordUtil.hashPassword(password);
        if (!hashedPassword.equals(user.getPassword())) {
            System.out.println("Lỗi: Mật khẩu không chính xác");
            return null;
        }
        
        System.out.println("Thành công: Đăng nhập thành công!");
        return user;
    }

    // ========== CẬP NHẬT HỒNG SƠ ==========
    /**
     * Cập nhật thông tin cá nhân
     */
    public boolean updateProfile(int userId, String fullname, String phone, String department) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            System.out.println("Lỗi: Người dùng không tồn tại");
            return false;
        }
        
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setDepartment(department);
        
        return userDao.updateUser(user);
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            System.out.println("Lỗi: Người dùng không tồn tại");
            return false;
        }
        
        // Kiểm tra mật khẩu cũ
        String hashedOldPassword = PasswordUtil.hashPassword(oldPassword);
        if (!hashedOldPassword.equals(user.getPassword())) {
            System.out.println("Lỗi: Mật khẩu cũ không chính xác");
            return false;
        }
        
        // Kiểm tra mật khẩu mới
        if (!ValidationUtil.isValidPassword(newPassword)) {
            System.out.println("Lỗi: Mật khẩu mới phải có ít nhất 6 ký tự");
            return false;
        }
        
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword);
        return userDao.changePassword(userId, hashedNewPassword);
    }

    // ========== QUẢN LÝ NGƯỜI DÙNG (ADMIN ONLY) ==========
    public boolean createSupportStaffAccount(String username, String password, String fullname,
                                             String phone, String department) {
        if (!ValidationUtil.isValidUsername(username)) {
            System.out.println("Lỗi: Username không hợp lệ");
            return false;
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            System.out.println("Lỗi: Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        
        if (userDao.usernameExists(username)) {
            System.out.println("Lỗi: Username đã tồn tại!");
            return false;
        }
        
        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(username, hashedPassword, "SUPPORT_STAFF", fullname, phone, department);
        
        return userDao.addUser(newUser);
    }

    /**
     * Lấy danh sách nhân viên hỗ trợ
     */
    public List<User> getAllSupportStaff() {
        return userDao.getAllSupportStaff();
    }

    /**
     * Lấy tất cả người dùng
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Lấy thông tin người dùng theo ID
     */
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    /**
     * Vô hiệu hóa tài khoản
     */
    public boolean deactivateAccount(int userId) {
        return userDao.updateStatus(userId, "INACTIVE");
    }

    /**
     * Kích hoạt tài khoản
     */
    public boolean activateAccount(int userId) {
        return userDao.updateStatus(userId, "ACTIVE");
    }

    /**
     * Xóa tài khoản
     */
    public boolean deleteAccount(int userId) {
        return userDao.deleteUser(userId);
    }

    /**
     * Đếm tổng số người dùng
     */
    public int getTotalUsers() {
        return userDao.getTotalUsers();
    }
}


