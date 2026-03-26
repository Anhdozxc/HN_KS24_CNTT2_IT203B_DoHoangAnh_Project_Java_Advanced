package dao;

import model.BookingServiceDetail;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO quản lý chi tiết dịch vụ trong phiếu đặt phòng
 */
public class BookingServiceDao {

    // ========== CREATE: Thêm dịch vụ vào phiếu đặt ==========
    /**
     * Thêm chi tiết dịch vụ vào phiếu đặt
     */
    public boolean addBookingService(BookingServiceDetail detail) {
        String sql = "INSERT INTO booking_services (booking_id, service_id, quantity) " +
                     "VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detail.getBookingId());
            stmt.setInt(2, detail.getServiceId());
            stmt.setInt(3, detail.getQuantity());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm dịch vụ vào phiếu đặt: " + e.getMessage());
            return false;
        }
    }

    // ========== READ: Lấy chi tiết dịch vụ ==========
    /**
     * Lấy chi tiết dịch vụ theo ID
     */
    public BookingServiceDetail getServiceDetail(int id) {
        String sql = "SELECT * FROM booking_services WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDetail(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy chi tiết dịch vụ: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả dịch vụ của một phiếu đặt
     */
    public List<BookingServiceDetail> getServicesByBooking(int bookingId) {
        List<BookingServiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM booking_services WHERE booking_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToDetail(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy dịch vụ của phiếu đặt: " + e.getMessage());
        }
        return details;
    }

    // ========== UPDATE: Cập nhật chi tiết ==========
    /**
     * Cập nhật số lượng dịch vụ
     */
    public boolean updateQuantity(int id, int quantity) {
        String sql = "UPDATE booking_services SET quantity = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, id);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật số lượng dịch vụ: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE: Xóa chi tiết ==========
    /**
     * Xóa một dịch vụ khỏi phiếu đặt
     */
    public boolean deleteDetail(int id) {
        String sql = "DELETE FROM booking_services WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa chi tiết dịch vụ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa tất cả dịch vụ của một phiếu đặt
     */
    public boolean deleteByBooking(int bookingId) {
        String sql = "DELETE FROM booking_services WHERE booking_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa dịch vụ của phiếu đặt: " + e.getMessage());
            return false;
        }
    }

    // ========== PHƯƠNG THỨC HỖ TRỢ ==========
    /**
     * Ánh xạ ResultSet thành BookingServiceDetail
     */
    private BookingServiceDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int bookingId = rs.getInt("booking_id");
        int serviceId = rs.getInt("service_id");
        int quantity = rs.getInt("quantity");
        
        return new BookingServiceDetail(id, bookingId, serviceId, quantity);
    }
}


