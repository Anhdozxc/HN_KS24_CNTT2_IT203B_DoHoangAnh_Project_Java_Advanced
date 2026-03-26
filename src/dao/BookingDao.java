package dao;

import model.Booking;
import util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Data Access Object (DAO) quản lý các hoạt động với bảng Bookings
 */
public class BookingDao {

    // ========== CREATE: Thêm đặt phòng ==========
    /**
     * Thêm phiếu đặt phòng mới vào database
     */
    public int addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, start_time, end_time, status, participant_count, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getRoomId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            stmt.setString(5, booking.getStatus() != null ? booking.getStatus() : "PENDING");
            stmt.setObject(6, booking.getParticipantCount());
            stmt.setString(7, booking.getNotes());
            
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm phiếu đặt phòng: " + e.getMessage());
        }
        return -1;
    }

    // ========== READ: Lấy thông tin đặt phòng ==========
    /**
     * Lấy phiếu đặt phòng theo ID
     */
    public Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy phiếu đặt phòng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả phiếu đặt phòng
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY start_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy danh sách phiếu đặt: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Lấy phiếu đặt của một nhân viên
     */
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY start_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy phiếu đặt của nhân viên: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Lấy phiếu đặt theo trạng thái
     */
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = ? ORDER BY start_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy phiếu đặt theo trạng thái: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Lấy phiếu đặt chờ duyệt (PENDING)
     */
    public List<Booking> getPendingBookings() {
        return getBookingsByStatus("PENDING");
    }

    /**
     * Lấy phiếu đặt được phân công cho nhân viên hỗ trợ
     */
    public List<Booking> getBookingsBySupportStaff(int supportStaffId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE support_staff_id = ? AND status = 'APPROVED' ORDER BY start_time";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, supportStaffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy phiếu đặt của nhân viên hỗ trợ: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Kiểm tra xung đột lịch trình trong một phòng
     */
    public boolean isRoomBooked(int roomId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(*) as count FROM bookings " +
                     "WHERE room_id = ? AND status IN ('PENDING', 'APPROVED') " +
                     "AND ((start_time < ? AND end_time > ?) OR " +
                     "(start_time < ? AND end_time > ?) OR " +
                     "(start_time >= ? AND end_time <= ?))";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            Timestamp startTs = Timestamp.valueOf(startTime);
            Timestamp endTs = Timestamp.valueOf(endTime);
            
            stmt.setInt(1, roomId);
            stmt.setTimestamp(2, endTs);
            stmt.setTimestamp(3, startTs);
            stmt.setTimestamp(4, endTs);
            stmt.setTimestamp(5, startTs);
            stmt.setTimestamp(6, startTs);
            stmt.setTimestamp(7, endTs);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kiểm tra xung đột lịch: " + e.getMessage());
        }
        return false;
    }

    // ========== UPDATE: Cập nhật phiếu đặt ==========
    /**
     * Cập nhật trạng thái phiếu đặt
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật trạng thái phiếu đặt: " + e.getMessage());
            return false;
        }
    }

    /**
     * Phân công nhân viên hỗ trợ
     */
    public boolean assignSupportStaff(int bookingId, int supportStaffId) {
        String sql = "UPDATE bookings SET support_staff_id = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, supportStaffId);
            stmt.setInt(2, bookingId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi phân công nhân viên hỗ trợ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật ghi chú phiếu đặt
     */
    public boolean updateNotes(int bookingId, String notes) {
        String sql = "UPDATE bookings SET notes = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, notes);
            stmt.setInt(2, bookingId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật ghi chú: " + e.getMessage());
            return false;
        }
    }

    // ========== DELETE: Xóa phiếu đặt ==========
    /**
     * Xóa phiếu đặt (chỉ khi trạng thái PENDING)
     */
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ? AND status = 'PENDING'";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa phiếu đặt: " + e.getMessage());
            return false;
        }
    }

    // ========== KIỂM TRA ==========
    /**
     * Lấy tổng số phiếu đặt
     */
    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) as total FROM bookings";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi đếm phiếu đặt: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Đếm phiếu đặt theo trạng thái
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) as total FROM bookings WHERE status = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi đếm phiếu đặt theo trạng thái: " + e.getMessage());
        }
        return 0;
    }

    // ========== PHƯƠNG THỨC HỖ TRỢ ==========
    /**
     * Ánh xạ ResultSet thành đối tượng Booking
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int roomId = rs.getInt("room_id");
        
        Timestamp startTs = rs.getTimestamp("start_time");
        LocalDateTime startTime = startTs != null ? startTs.toLocalDateTime() : null;
        
        Timestamp endTs = rs.getTimestamp("end_time");
        LocalDateTime endTime = endTs != null ? endTs.toLocalDateTime() : null;
        
        String status = rs.getString("status");
        Object supportStaffObj = rs.getObject("support_staff_id");
        Integer supportStaffId = supportStaffObj != null ? (Integer) supportStaffObj : null;
        
        Object participantObj = rs.getObject("participant_count");
        Integer participantCount = participantObj != null ? (Integer) participantObj : null;
        
        String notes = rs.getString("notes");
        
        Timestamp createdTs = rs.getTimestamp("created_date");
        LocalDateTime createdDate = createdTs != null ? createdTs.toLocalDateTime() : null;
        
        return new Booking(id, userId, roomId, startTime, endTime, status, supportStaffId,
                          participantCount, notes, createdDate);
    }
}


