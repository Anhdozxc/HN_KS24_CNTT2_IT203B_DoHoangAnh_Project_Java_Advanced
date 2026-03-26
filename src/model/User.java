package model;

import java.time.LocalDateTime;

/**
 * Lớp đại diện cho người dùng trong hệ thống
 * Vai trò: EMPLOYEE (Nhân viên), SUPPORT_STAFF (Nhân viên hỗ trợ), ADMIN (Quản trị viên)
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String fullname;
    private String phone;
    private String department;
    private String status;
    private LocalDateTime createdDate;

    // ========== CONSTRUCTOR ==========
    /**
     * Constructor không có tham số
     */
    public User() {
    }

    /**
     * Constructor có tham số (dùng khi thêm user mới)
     */
    public User(String username, String password, String role, String fullname, 
                String phone, String department) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.phone = phone;
        this.department = department;
        this.status = "ACTIVE";
    }

    /**
     * Constructor đầy đủ (dùng khi lấy từ database)
     */
    public User(int id, String username, String password, String role, String fullname,
                String phone, String department, String status, LocalDateTime createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.phone = phone;
        this.department = department;
        this.status = status;
        this.createdDate = createdDate;
    }

    // ========== GETTERS ==========
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // ========== PHƯƠNG THỨC TIỆN ÍCH ==========
    /**
     * In thông tin người dùng
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullname='" + fullname + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

