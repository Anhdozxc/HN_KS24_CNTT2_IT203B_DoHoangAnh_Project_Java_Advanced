package model;

/**
 * Lớp đại diện cho chi tiết thiết bị của một lần đặt phòng
 */
public class BookingEquipmentDetail {
    private int id;
    private int bookingId;
    private int equipmentId;
    private int quantity;

    // ========== CONSTRUCTOR ==========
    public BookingEquipmentDetail() {
    }

    public BookingEquipmentDetail(int bookingId, int equipmentId, int quantity) {
        this.bookingId = bookingId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
    }

    public BookingEquipmentDetail(int id, int bookingId, int equipmentId, int quantity) {
        this.id = id;
        this.bookingId = bookingId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getEquipmentId() {
        return equipmentId;
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

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

