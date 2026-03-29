package model;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho thiết bị di động
 */
public class Equipment {
    private int id;
    private String name;
    private int totalQuantity;
    private int availableQuantity;
    private String status;
    private LocalDateTime createdDate;

    //  CONSTRUCTOR
    public Equipment() {
    }

    public Equipment(String name, int totalQuantity, int availableQuantity, String status) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
    }

    public Equipment(int id, String name, int totalQuantity, int availableQuantity,
                     String status, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
        this.createdDate = createdDate;
    }

    //  GETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    //  SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Thiết bị: " + name + " | Tổng: " + totalQuantity + " | Khả dụng: " + availableQuantity +
                " | Trạng thái: " + status;
    }
}

