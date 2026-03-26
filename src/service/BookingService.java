package service;

import dao.BookingDao;
import dao.BookingEquipmentDao;
import dao.BookingServiceDao;
import model.Booking;
import model.BookingEquipmentDetail;
import model.BookingServiceDetail;
import util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lớp Service xử lý logic liên quan đến quản lý đặt phòng
 */
public class BookingService {
    private BookingDao bookingDao;
    private BookingEquipmentDao bookingEquipmentDao;
    private BookingServiceDao bookingServiceDao;
    private RoomService roomService;
    private EquipmentService equipmentService;

    public BookingService() {
        this.bookingDao = new BookingDao();
        this.bookingEquipmentDao = new BookingEquipmentDao();
        this.bookingServiceDao = new BookingServiceDao();
        this.roomService = new RoomService();
        this.equipmentService = new EquipmentService();
    }

    // ========== THÊM ĐẶT PHÒNG ==========
    /**
     * Tạo phiếu đặt phòng mới
     */
    public int createBooking(int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime,
                             int participantCount, String notes) {
        // Kiểm tra hợp lệ
        if (!ValidationUtil.isTimeRangeValid(startTime, endTime)) {
            System.out.println("Lỗi: Thời gian bắt đầu phải trước thời gian kết thúc");
            return -1;
        }
        
        if (!ValidationUtil.isStartTimeValid(startTime)) {
            System.out.println("Lỗi: Thời gian đặt không được trong quá khứ");
            return -1;
        }
        
        // Kiểm tra sức chứa phòng
        if (!roomService.isRoomCapacitySufficient(roomId, participantCount)) {
            System.out.println("Lỗi: Số người vượt quá sức chứa của phòng");
            return -1;
        }
        
        // Kiểm tra xung đột lịch trình
        if (bookingDao.isRoomBooked(roomId, startTime, endTime)) {
            System.out.println("Lỗi: Phòng đã được đặt trong khoảng thời gian này");
            return -1;
        }
        
        Booking booking = new Booking(userId, roomId, startTime, endTime, participantCount, notes);
        return bookingDao.addBooking(booking);
    }

    // ========== THÊM THIẾT BỊ VÀO ĐẶT PHÒNG ==========
    /**
     * Thêm thiết bị vào phiếu đặt
     */
    public boolean addEquipmentToBooking(int bookingId, int equipmentId, int quantity) {
        if (!equipmentService.isEquipmentAvailable(equipmentId, quantity)) {
            System.out.println("Lỗi: Thiết bị không đủ hoặc không hoạt động");
            return false;
        }
        
        BookingEquipmentDetail detail = new BookingEquipmentDetail(bookingId, equipmentId, quantity);
        return bookingEquipmentDao.addBookingEquipment(detail);
    }

    // ========== THÊM DỊCH VỤ VÀO ĐẶT PHÒNG ==========
    /**
     * Thêm dịch vụ vào phiếu đặt
     */
    public boolean addServiceToBooking(int bookingId, int serviceId, int quantity) {
        BookingServiceDetail detail = new BookingServiceDetail(bookingId, serviceId, quantity);
        return bookingServiceDao.addBookingService(detail);
    }

    // ========== LẤY THÔNG TIN ĐẶT PHÒNG ==========
    /**
     * Lấy phiếu đặt theo ID
     */
    public Booking getBookingById(int bookingId) {
        return bookingDao.getBookingById(bookingId);
    }

    /**
     * Lấy tất cả phiếu đặt
     */
    public List<Booking> getAllBookings() {
        return bookingDao.getAllBookings();
    }

    /**
     * Lấy phiếu đặt của một nhân viên
     */
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDao.getBookingsByUserId(userId);
    }

    /**
     * Lấy phiếu đặt chờ duyệt
     */
    public List<Booking> getPendingBookings() {
        return bookingDao.getPendingBookings();
    }

    /**
     * Lấy phiếu đặt theo trạng thái
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingDao.getBookingsByStatus(status);
    }

    /**
     * Lấy phiếu đặt được phân công cho nhân viên hỗ trợ
     */
    public List<Booking> getBookingsBySupportStaff(int supportStaffId) {
        return bookingDao.getBookingsBySupportStaff(supportStaffId);
    }

    /**
     * Lấy thiết bị của phiếu đặt
     */
    public List<BookingEquipmentDetail> getEquipmentByBooking(int bookingId) {
        return bookingEquipmentDao.getEquipmentByBooking(bookingId);
    }

    /**
     * Lấy dịch vụ của phiếu đặt
     */
    public List<BookingServiceDetail> getServicesByBooking(int bookingId) {
        return bookingServiceDao.getServicesByBooking(bookingId);
    }

    // ========== PHÂN CÔNG & DỰA CHỈ ==========
    /**
     * Duyệt phiếu đặt (Admin)
     */
    public boolean approveBooking(int bookingId, int supportStaffId) {
        // Cập nhật trạng thái thành APPROVED
        if (!bookingDao.updateBookingStatus(bookingId, "APPROVED")) {
            System.out.println("Lỗi: Không thể duyệt phiếu đặt");
            return false;
        }
        
        // Phân công nhân viên hỗ trợ
        if (!bookingDao.assignSupportStaff(bookingId, supportStaffId)) {
            System.out.println("Lỗi: Không thể phân công nhân viên hỗ trợ");
            return false;
        }
        
        return true;
    }

    /**
     * Từ chối phiếu đặt (Admin)
     */
    public boolean rejectBooking(int bookingId) {
        return bookingDao.updateBookingStatus(bookingId, "REJECTED");
    }

    /**
     * Hoàn thành đặt phòng
     */
    public boolean completeBooking(int bookingId) {
        return bookingDao.updateBookingStatus(bookingId, "DONE");
    }

    // ========== HỦY ĐẶT PHÒNG ==========
    /**
     * Hủy phiếu đặt (chỉ khi trạng thái PENDING)
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDao.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("Lỗi: Phiếu đặt không tồn tại");
            return false;
        }
        
        if (!"PENDING".equals(booking.getStatus())) {
            System.out.println("Lỗi: Chỉ có thể hủy phiếu đặt ở trạng thái chờ duyệt");
            return false;
        }
        
        // Xóa thiết bị
        bookingEquipmentDao.deleteByBooking(bookingId);
        
        // Xóa dịch vụ
        bookingServiceDao.deleteByBooking(bookingId);
        
        // Xóa phiếu đặt
        return bookingDao.deleteBooking(bookingId);
    }

    // ========== KIỂM TRA ==========
    /**
     * Lấy tổng số phiếu đặt
     */
    public int getTotalBookings() {
        return bookingDao.getTotalBookings();
    }

    /**
     * Đếm phiếu đặt theo trạng thái
     */
    public int countByStatus(String status) {
        return bookingDao.countByStatus(status);
    }

    /**
     * Cập nhật ghi chú phiếu đặt
     */
    public boolean updateNotes(int bookingId, String notes) {
        return bookingDao.updateNotes(bookingId, notes);
    }
}


