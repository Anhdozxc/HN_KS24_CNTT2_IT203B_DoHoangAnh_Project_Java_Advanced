package dao;

import model.Room;
import util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Data Access Object (DAO) quản lý các hoạt động với bảng Rooms
 */
public class RoomDao {

    //  CREATE: Thêm phòng
    /**
     * Thêm phòng mới vào database
     */
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (name, capacity, location, fixed_equipment, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getLocation());
            stmt.setString(4, room.getFixedEquipment());
            stmt.setString(5, room.getStatus() != null ? room.getStatus() : "AVAILABLE");
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi thêm phòng: " + e.getMessage());
            return false;
        }
    }

    // READ: Lấy thông tin phòng
    /**
     * Lấy phòng theo ID
     */
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy phòng theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy phòng theo tên
     */
    public Room getRoomByName(String name) {
        String sql = "SELECT * FROM rooms WHERE name = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy phòng theo tên: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả phòng
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy danh sách phòng: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Lấy phòng theo trạng thái
     */
    public List<Room> getRoomsByStatus(String status) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = ? ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy phòng theo trạng thái: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Lấy phòng khả dụng
     */
    public List<Room> getAvailableRooms() {
        return getRoomsByStatus("AVAILABLE");
    }

    /**
     * Lấy phòng theo sức chứa
     */
    public List<Room> getRoomsByCapacity(int minCapacity) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE capacity >= ? AND status = 'AVAILABLE' ORDER BY capacity";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, minCapacity);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi lấy phòng theo sức chứa: " + e.getMessage());
        }
        return rooms;
    }

    //  UPDATE: Cập nhật thông tin phòng
    /**
     * Cập nhật thông tin phòng
     */
    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET name = ?, capacity = ?, location = ?, fixed_equipment = ?, status = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getLocation());
            stmt.setString(4, room.getFixedEquipment());
            stmt.setString(5, room.getStatus());
            stmt.setInt(6, room.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi cập nhật phòng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật trạng thái phòng
     */
    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật trạng thái phòng: " + e.getMessage());
            return false;
        }
    }

    //  DELETE: Xóa phòng
    /**
     * Xóa phòng theo ID
     */
    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println(" Lỗi xóa phòng: " + e.getMessage());
            return false;
        }
    }

    //  KIỂM TRA
    /**
     * Kiểm tra tên phòng đã tồn tại chưa
     */
    public boolean roomNameExists(String name) {
        return getRoomByName(name) != null;
    }

    /**
     * Lấy tổng số phòng
     */
    public int getTotalRooms() {
        String sql = "SELECT COUNT(*) as total FROM rooms";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi đếm phòng: " + e.getMessage());
        }
        return 0;
    }

    //  PHƯƠNG THỨC HỖ TRỢ
    /**
     * Ánh xạ ResultSet thành đối tượng Room
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int capacity = rs.getInt("capacity");
        String location = rs.getString("location");
        String fixedEquipment = rs.getString("fixed_equipment");
        String status = rs.getString("status");
        
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = null;
        if (ts != null) {
            createdDate = ts.toLocalDateTime();
        }
        
        return new Room(id, name, capacity, location, fixedEquipment, status, createdDate);
    }

    /**
     * Tìm kiếm phòng theo tên (sử dụng LIKE)
     */
    public List<Room> searchRoomByName(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi tìm kiếm phòng: " + e.getMessage());
        }
        return rooms;
    }
}
