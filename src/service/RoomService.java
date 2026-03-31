package service;

import dao.RoomDao;
import model.Room;
import util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Service xử lý logic liên quan đến quản lý phòng họp
 */
public class RoomService {
    private RoomDao roomDao;

    public RoomService() {
        this.roomDao = new RoomDao();
    }

    // THÊM PHÒNG
    /**
     * Thêm phòng họp mới
     */
    public boolean addRoom(String name, int capacity, String location, String fixedEquipment) {
        // Kiểm tra hợp lệ
        if (!ValidationUtil.isNotEmpty(name)) {
            System.out.println("Lỗi: Tên phòng không được để trống");
            return false;
        }
        
        if (!ValidationUtil.isPositive(capacity)) {
            System.out.println("Lỗi: Sức chứa phải lớn hơn 0");
            return false;
        }
        
        if (!ValidationUtil.isNotEmpty(location)) {
            System.out.println("Lỗi: Vị trí phòng không được để trống");
            return false;
        }
        
        // Kiểm tra tên phòng đã tồn tại
        if (roomDao.roomNameExists(name)) {
            System.out.println("Lỗi: Tên phòng đã tồn tại!");
            return false;
        }
        
        Room newRoom = new Room(name, capacity, location, fixedEquipment);
        return roomDao.addRoom(newRoom);
    }

    //  CẬP NHẬT PHÒNG
    public boolean updateRoom(int roomId, String name, int capacity, String location,
                              String fixedEquipment, String status) {
        Room room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại");
            return false;
        }
        
        room.setName(name);
        room.setCapacity(capacity);
        room.setLocation(location);
        room.setFixedEquipment(fixedEquipment);
        room.setStatus(status);
        
        return roomDao.updateRoom(room);
    }

    public boolean updateRoomStatus(int roomId, String status) {
        if (!ValidationUtil.isValidRoomStatus(status)) {
            System.out.println("Lỗi: Trạng thái không hợp lệ (AVAILABLE, MAINTENANCE)");
            return false;
        }
        
        return roomDao.updateRoomStatus(roomId, status);
    }

    /**
     * Cập nhật sức chứa phòng
     */
    public boolean updateRoomCapacity(int roomId, int newCapacity) {
        if (!ValidationUtil.isPositive(newCapacity)) {
            System.out.println("Lỗi: Sức chứa phải lớn hơn 0");
            return false;
        }
        
        Room room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("Lỗi: Phòng không tồn tại");
            return false;
        }
        
        room.setCapacity(newCapacity);
        return roomDao.updateRoom(room);
    }

    // LẤY THÔNG TIN PHÒNG
    /**
     * Lấy danh sách tất cả phòng
     */
    public List<Room> getAllRooms() {
        return roomDao.getAllRooms();
    }

    /**
     * Lấy phòng theo ID
     */
    public Room getRoomById(int roomId) {
        return roomDao.getRoomById(roomId);
    }

    /**
     * Lấy phòng theo tên
     */
    public Room getRoomByName(String name) {
        return roomDao.getRoomByName(name);
    }

    /**
     * Lấy phòng khả dụng
     */
    public List<Room> getAvailableRooms() {
        return roomDao.getAvailableRooms();
    }

    /**
     * Lấy phòng theo sức chứa tối thiểu
     */
    public List<Room> getRoomsByCapacity(int minCapacity) {
        return roomDao.getRoomsByCapacity(minCapacity);
    }

    /**
     * Lấy phòng theo trạng thái
     */
    public List<Room> getRoomsByStatus(String status) {
        return roomDao.getRoomsByStatus(status);
    }

    //  XÓA PHÒNG
    /**
     * Xóa phòng
     */
    public boolean deleteRoom(int roomId) {
        return roomDao.deleteRoom(roomId);
    }

    //  KIỂM TRA
    /**
     * Kiểm tra phòng có đủ sức chứa không
     */
    public boolean isRoomCapacitySufficient(int roomId, int participantCount) {
        Room room = roomDao.getRoomById(roomId);
        if (room == null) return false;
        return participantCount <= room.getCapacity();
    }

    /**
     * Lấy tổng số phòng
     */
    public int getTotalRooms() {
        return roomDao.getTotalRooms();
    }

    /**
     * Tìm kiếm phòng theo tên
     */
    public List<Room> searchRoomByName(String keyword) {
        if (!ValidationUtil.isNotEmpty(keyword)) {
            System.out.println("Lỗi: Từ khóa tìm kiếm không được để trống");
            return new ArrayList<>();
        }
        return roomDao.searchRoomByName(keyword);
    }
}
