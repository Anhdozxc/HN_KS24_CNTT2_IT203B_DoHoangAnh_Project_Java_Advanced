package model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 * Lớp đại diện cho phiếu đặt phòng
 * Trạng thái: PENDING (chờ duyệt), APPROVED (đã duyệt), REJECTED (từ chối), DONE (hoàn thành)
 */
public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer supportStaffId;
    private Integer participantCount;
    private String notes;
    private LocalDateTime createdDate;

    // ========== CONSTRUCTOR ==========
    public Booking() {
    }

    public Booking(int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime,
                   int participantCount, String notes) {
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantCount = participantCount;
        this.notes = notes;
        this.status = "PENDING";
    }

    public Booking(int id, int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime,
                   String status, Integer supportStaffId, Integer participantCount,
                   String notes, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.supportStaffId = supportStaffId;
        this.participantCount = participantCount;
        this.notes = notes;
        this.createdDate = createdDate;
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public Integer getSupportStaffId() {
        return supportStaffId;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // ========== SETTERS ==========
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSupportStaffId(Integer supportStaffId) {
        this.supportStaffId = supportStaffId;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "📅 Booking ID: " + id + " | Phòng: " + roomId + " | Từ: " + startTime +
                " | Đến: " + endTime + " | Trạng thái: " + status;
    }
}

