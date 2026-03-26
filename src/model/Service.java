package model;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho dịch vụ (nước, trà, bánh, vệ sinh...)
 */
public class Service {
    private int id;
    private String name;
    private double price;
    private String description;
    private String status;
    private LocalDateTime createdDate;

    // ========== CONSTRUCTOR ==========
    public Service() {
    }

    public Service(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = "ACTIVE";
    }

    public Service(int id, String name, double price, String description,
                   String status, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // ========== SETTERS ==========
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "☕ " + name + " | Giá: " + price + " | " + description + " | Trạng thái: " + status;
    }
}

