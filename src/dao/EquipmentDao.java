package dao;

import model.Equipment;
import util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Data Access Object (DAO) quản lý các hoạt động với bảng Equipment
 */
public class EquipmentDao {

    //  CREATE: Thêm thiết bị
    /**
     * Thêm thiết bị mới vào database
     */
    public boolean addEquipment(Equipment equipment) {
        String sql = "INSERT INTO equipment (name, total_quantity, available_quantity, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getTotalQuantity());
            stmt.setInt(3, equipment.getAvailableQuantity());
            stmt.setString(4, equipment.getStatus() != null ? equipment.getStatus() : "ACTIVE");
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi thêm thiết bị: " + e.getMessage());
            return false;
        }
    }

    //  READ: Lấy thông tin thiết bị
    /**
     * Lấy thiết bị theo ID
     */
    public Equipment getEquipmentById(int id) {
        String sql = "SELECT * FROM equipment WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEquipment(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy thiết bị theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy thiết bị theo tên
     */
    public Equipment getEquipmentByName(String name) {
        String sql = "SELECT * FROM equipment WHERE name = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEquipment(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy thiết bị theo tên: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả thiết bị
     */
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipment ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                equipmentList.add(mapResultSetToEquipment(rs));
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy danh sách thiết bị: " + e.getMessage());
        }
        return equipmentList;
    }

    /**
     * Lấy thiết bị theo trạng thái
     */
    public List<Equipment> getEquipmentByStatus(String status) {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipment WHERE status = ? ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapResultSetToEquipment(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy thiết bị theo trạng thái: " + e.getMessage());
        }
        return equipmentList;
    }

    /**
     * Lấy thiết bị khả dụng
     */
    public List<Equipment> getAvailableEquipment() {
        return getEquipmentByStatus("ACTIVE");
    }

    //  UPDATE: Cập nhật thông tin thiết bị
    /**
     * Cập nhật thông tin thiết bị
     */
    public boolean updateEquipment(Equipment equipment) {
        String sql = "UPDATE equipment SET name = ?, total_quantity = ?, available_quantity = ?, status = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getTotalQuantity());
            stmt.setInt(3, equipment.getAvailableQuantity());
            stmt.setString(4, equipment.getStatus());
            stmt.setInt(5, equipment.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi cập nhật thiết bị: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật số lượng khả dụng
     */
    public boolean updateAvailableQuantity(int equipmentId, int quantity) {
        String sql = "UPDATE equipment SET available_quantity = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, equipmentId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi cập nhật số lượng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Giảm số lượng khả dụng
     */
    public boolean decreaseAvailableQuantity(int equipmentId, int quantity) {
        String sql = "UPDATE equipment SET available_quantity = available_quantity - ? " +
                     "WHERE id = ? AND available_quantity >= ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, equipmentId);
            stmt.setInt(3, quantity);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi giảm số lượng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tăng số lượng khả dụng
     */
    public boolean increaseAvailableQuantity(int equipmentId, int quantity) {
        String sql = "UPDATE equipment SET available_quantity = available_quantity + ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, equipmentId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi tăng số lượng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật trạng thái thiết bị
     */
    public boolean updateStatus(int equipmentId, String status) {
        String sql = "UPDATE equipment SET status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, equipmentId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi cập nhật trạng thái: " + e.getMessage());
            return false;
        }
    }

    // =DELETE: Xóa thiết bị
    /**
     * Xóa thiết bị theo ID
     */
    public boolean deleteEquipment(int id) {
        String sql = "DELETE FROM equipment WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi xóa thiết bị: " + e.getMessage());
            return false;
        }
    }

    //  KIỂM TRA
    /**
     * Kiểm tra tên thiết bị đã tồn tại chưa
     */
    public boolean equipmentNameExists(String name) {
        return getEquipmentByName(name) != null;
    }

    /**
     * Lấy tổng số thiết bị
     */
    public int getTotalEquipment() {
        String sql = "SELECT COUNT(*) as total FROM equipment";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi đếm thiết bị: " + e.getMessage());
        }
        return 0;
    }

    //  PHƯƠNG THỨC HỖ TRỢ
    /**
     * Ánh xạ ResultSet thành đối tượng Equipment
     */
    private Equipment mapResultSetToEquipment(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int totalQuantity = rs.getInt("total_quantity");
        int availableQuantity = rs.getInt("available_quantity");
        String status = rs.getString("status");
        
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = null;
        if (ts != null) {
            createdDate = ts.toLocalDateTime();
        }
        
        return new Equipment(id, name, totalQuantity, availableQuantity, status, createdDate);
    }
}


