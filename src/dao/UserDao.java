package dao;

import model.User;
import util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Data Access Object (DAO) quản lý các hoạt động với bảng Users
 * Cung cấp các phương thức CRUD: Create, Read, Update, Delete
 */
public class UserDao {

    // ========== CREATE: Thêm người dùng mới ==========
    /**
     * Thêm người dùng mới vào database
     * @param user Đối tượng User cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, role, fullname, phone, department, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getFullname());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getDepartment());
            stmt.setString(7, user.getStatus() != null ? user.getStatus() : "ACTIVE");
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi: Thêm người dùng thất bại: " + e.getMessage());
            return false;
        }
    }

    // ========== READ: Lấy thông tin người dùng ==========
    /**
     * Lấy người dùng theo ID
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy người dùng theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy người dùng theo username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy người dùng theo username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả người dùng
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy danh sách người dùng: " + e.getMessage());
        }
        return users;
    }

    /**
     * Lấy tất cả người dùng theo vai trò
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy người dùng theo vai trò: " + e.getMessage());
        }
        return users;
    }

    /**
     * Lấy tất cả nhân viên hỗ trợ (SUPPORT_STAFF)
     */
    public List<User> getAllSupportStaff() {
        return getUsersByRole("SUPPORT_STAFF");
    }

    // ========== UPDATE: Cập nhật thông tin người dùng ==========
    /**
     * Cập nhật thông tin người dùng
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET fullname = ?, phone = ?, department = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFullname());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getDepartment());
            stmt.setString(4, user.getStatus());
            stmt.setInt(5, user.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật người dùng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Đổi mật khẩu người dùng
     */
    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật trạng thái người dùng
     */
    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật trạng thái: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE: Xóa người dùng ==========
    /**
     * Xóa người dùng theo ID
     */
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa người dùng: " + e.getMessage());
            return false;
        }
    }

    // ========== KIỂM TRA ==========
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean usernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    /**
     * Kiểm tra tổng số người dùng
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) as total FROM users";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi đếm người dùng: " + e.getMessage());
        }
        return 0;
    }

    // ========== PHƯƠNG THỨC HỖ TRỢ ==========
    /**
     * Ánh xạ ResultSet thành đối tượng User
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String role = rs.getString("role");
        String fullname = rs.getString("fullname");
        String phone = rs.getString("phone");
        String department = rs.getString("department");
        String status = rs.getString("status");
        
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = null;
        if (ts != null) {
            createdDate = ts.toLocalDateTime();
        }
        
        return new User(id, username, password, role, fullname, phone, department, status, createdDate);
    }
}


