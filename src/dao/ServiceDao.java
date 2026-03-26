package dao;

import model.Service;
import util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Data Access Object (DAO) quản lý các hoạt động với bảng Services
 */
public class ServiceDao {

    // ========== CREATE: Thêm dịch vụ ==========
    /**
     * Thêm dịch vụ mới vào database
     */
    public boolean addService(Service service) {
        String sql = "INSERT INTO services (name, price, description, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, service.getName());
            stmt.setDouble(2, service.getPrice());
            stmt.setString(3, service.getDescription());
            stmt.setString(4, service.getStatus() != null ? service.getStatus() : "ACTIVE");
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm dịch vụ: " + e.getMessage());
            return false;
        }
    }

    // ========== READ: Lấy thông tin dịch vụ ==========
    /**
     * Lấy dịch vụ theo ID
     */
    public Service getServiceById(int id) {
        String sql = "SELECT * FROM services WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy dịch vụ theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy dịch vụ theo tên
     */
    public Service getServiceByName(String name) {
        String sql = "SELECT * FROM services WHERE name = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy dịch vụ theo tên: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả dịch vụ
     */
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy danh sách dịch vụ: " + e.getMessage());
        }
        return services;
    }

    /**
     * Lấy dịch vụ theo trạng thái
     */
    public List<Service> getServicesByStatus(String status) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE status = ? ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy dịch vụ theo trạng thái: " + e.getMessage());
        }
        return services;
    }

    /**
     * Lấy dịch vụ hoạt động (ACTIVE)
     */
    public List<Service> getActiveServices() {
        return getServicesByStatus("ACTIVE");
    }

    // ========== UPDATE: Cập nhật dịch vụ ==========
    /**
     * Cập nhật thông tin dịch vụ
     */
    public boolean updateService(Service service) {
        String sql = "UPDATE services SET name = ?, price = ?, description = ?, status = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, service.getName());
            stmt.setDouble(2, service.getPrice());
            stmt.setString(3, service.getDescription());
            stmt.setString(4, service.getStatus());
            stmt.setInt(5, service.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật dịch vụ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật giá dịch vụ
     */
    public boolean updatePrice(int serviceId, double newPrice) {
        String sql = "UPDATE services SET price = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, serviceId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật giá: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật trạng thái dịch vụ
     */
    public boolean updateStatus(int serviceId, String status) {
        String sql = "UPDATE services SET status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, serviceId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật trạng thái: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE: Xóa dịch vụ ==========
    /**
     * Xóa dịch vụ theo ID
     */
    public boolean deleteService(int id) {
        String sql = "DELETE FROM services WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa dịch vụ: " + e.getMessage());
            return false;
        }
    }

    // ========== KIỂM TRA ==========
    /**
     * Kiểm tra tên dịch vụ đã tồn tại chưa
     */
    public boolean serviceNameExists(String name) {
        return getServiceByName(name) != null;
    }

    /**
     * Lấy tổng số dịch vụ
     */
    public int getTotalServices() {
        String sql = "SELECT COUNT(*) as total FROM services";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi đếm dịch vụ: " + e.getMessage());
        }
        return 0;
    }

    // ========== PHƯƠNG THỨC HỖ TRỢ ==========
    /**
     * Ánh xạ ResultSet thành đối tượng Service
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String description = rs.getString("description");
        String status = rs.getString("status");
        
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = null;
        if (ts != null) {
            createdDate = ts.toLocalDateTime();
        }
        
        return new Service(id, name, price, description, status, createdDate);
    }
}


