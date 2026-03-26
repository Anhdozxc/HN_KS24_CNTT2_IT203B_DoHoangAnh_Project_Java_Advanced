package model;

/**
 * Lớp đại diện cho chi tiết dịch vụ của một lần đặt phòng
 */
public class BookingServiceDetail {
    private int id;
    private int bookingId;
    private int serviceId;
    private int quantity;

    // ========== CONSTRUCTOR ==========
    public BookingServiceDetail() {
    }

    public BookingServiceDetail(int bookingId, int serviceId, int quantity) {
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
    }

    public BookingServiceDetail(int id, int bookingId, int serviceId, int quantity) {
        this.id = id;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    // ========== SETTERS ==========
    public void setId(int id) {
        this.id = id;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

