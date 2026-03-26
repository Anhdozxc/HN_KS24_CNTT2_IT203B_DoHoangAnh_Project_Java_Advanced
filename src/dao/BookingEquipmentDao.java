package dao;

import model.BookingEquipmentDetail;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO quản lý chi tiết thiết bị trong phiếu đặt phòng
 */
public class BookingEquipmentDao {

    // ========== CREATE: Thêm thiết bị vào phiếu đặt ==========
    /**
     * Thêm chi tiết thiết bị vào phiếu đặt
     */
    public boolean addBookingEquipment(BookingEquipmentDetail detail) {
        String sql = "INSERT INTO booking_equipment (booking_id, equipment_id, quantity) " +
                     "VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detail.getBookingId());
            stmt.setInt(2, detail.getEquipmentId());
            stmt.setInt(3, detail.getQuantity());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Loi: The thiet bi vao phieu dat: " + e.getMessage());
            return false;
        }
    }

    // ========== READ: Lấy chi tiết thiết bị ==========
    /**
     * Lấy chi tiết thiết bị theo ID
     */
    public BookingEquipmentDetail getEquipmentDetail(int id) {
        String sql = "SELECT * FROM booking_equipment WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDetail(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Loi: Lay chi tiet thiet bi: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả thiết bị của một phiếu đặt
     */
    public List<BookingEquipmentDetail> getEquipmentByBooking(int bookingId) {
        List<BookingEquipmentDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM booking_equipment WHERE booking_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToDetail(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Loi: Lay thiet bi cua phieu dat: " + e.getMessage());
        }
        return details;
    }

    // ========== UPDATE: Cap nhat chi tiet ==========
    /**
     * Cập nhật số lượng thiết bị
     */
    public boolean updateQuantity(int id, int quantity) {
        String sql = "UPDATE booking_equipment SET quantity = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, id);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Loi: Cap nhat so luong thiet bi: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE: Xoa chi tiet ==========
    /**
     * Xóa một thiết bị khỏi phiếu đặt
     */
    public boolean deleteDetail(int id) {
        String sql = "DELETE FROM booking_equipment WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Loi: Xoa chi tiet thiet bi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa tất cả thiết bị của một phiếu đặt
     */
    public boolean deleteByBooking(int bookingId) {
        String sql = "DELETE FROM booking_equipment WHERE booking_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Loi: Xoa thiet bi cua phieu dat: " + e.getMessage());
            return false;
        }
    }

    // ========== PHƯƠNG THỨC HỖ TRỢ ==========
    /**
     * Ánh xạ ResultSet thành BookingEquipmentDetail
     */
    private BookingEquipmentDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int bookingId = rs.getInt("booking_id");
        int equipmentId = rs.getInt("equipment_id");
        int quantity = rs.getInt("quantity");
        
        return new BookingEquipmentDetail(id, bookingId, equipmentId, quantity);
    }
}


