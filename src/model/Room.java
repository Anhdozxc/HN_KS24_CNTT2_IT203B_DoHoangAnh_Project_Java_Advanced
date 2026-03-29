package model;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho phòng họp
 */
public class Room {
    private int id;
    private String name;
    private int capacity;
    private String location;
    private String fixedEquipment;
    private String status;
    private LocalDateTime createdDate;

    //  CONSTRUCTOR
    public Room() {
    }

    public Room(String name, int capacity, String location, String fixedEquipment) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.fixedEquipment = fixedEquipment;
        this.status = "AVAILABLE";
    }

    public Room(int id, String name, int capacity, String location, String fixedEquipment,
                String status, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.fixedEquipment = fixedEquipment;
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

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public String getFixedEquipment() {
        return fixedEquipment;
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

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFixedEquipment(String fixedEquipment) {
        this.fixedEquipment = fixedEquipment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Phòng: " + name + " | Sức chứa: " + capacity + " người | Vị trí: " + location +
                " | Thiết bị: " + fixedEquipment + " | Trạng thái: " + status;
    }
}

