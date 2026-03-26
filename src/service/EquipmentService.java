package service;

import dao.EquipmentDao;
import model.Equipment;
import util.ValidationUtil;
import java.util.List;

/**
 * Lớp Service xử lý logic liên quan đến quản lý thiết bị
 */
public class EquipmentService {
    private EquipmentDao equipmentDao;

    public EquipmentService() {
        this.equipmentDao = new EquipmentDao();
    }

    // ========== THÊM THIẾT BỊ ==========
    /**
     * Thêm thiết bị mới
     */
    public boolean addEquipment(String name, int totalQuantity, String status) {
        // Kiểm tra hợp lệ
        if (!ValidationUtil.isNotEmpty(name)) {
            System.out.println("Lỗi: Tên thiết bị không được để trống");
            return false;
        }
        
        if (!ValidationUtil.isPositive(totalQuantity)) {
            System.out.println("Lỗi: Số lượng phải lớn hơn 0");
            return false;
        }
        
        // Kiểm tra tên thiết bị đã tồn tại
        if (equipmentDao.equipmentNameExists(name)) {
            System.out.println("Lỗi: Tên thiết bị đã tồn tại!");
            return false;
        }
        
        Equipment newEquipment = new Equipment(name, totalQuantity, totalQuantity, status);
        return equipmentDao.addEquipment(newEquipment);
    }

    // ========== CẬP NHẬT THIẾT BỊ ==========
    /**
     * Cập nhật thông tin thiết bị
     */
    public boolean updateEquipment(int equipmentId, String name, int totalQuantity,
                                   int availableQuantity, String status) {
        Equipment equipment = equipmentDao.getEquipmentById(equipmentId);
        if (equipment == null) {
            System.out.println("❌ Thiết bị không tồn tại");
            return false;
        }
        
        equipment.setName(name);
        equipment.setTotalQuantity(totalQuantity);
        equipment.setAvailableQuantity(availableQuantity);
        equipment.setStatus(status);
        
        return equipmentDao.updateEquipment(equipment);
    }

    /**
     * Cập nhật số lượng khả dụng
     */
    public boolean updateAvailableQuantity(int equipmentId, int newQuantity) {
        Equipment equipment = equipmentDao.getEquipmentById(equipmentId);
        if (equipment == null) {
            System.out.println("❌ Thiết bị không tồn tại");
            return false;
        }
        
        if (newQuantity < 0 || newQuantity > equipment.getTotalQuantity()) {
            System.out.println("❌ Số lượng không hợp lệ");
            return false;
        }
        
        return equipmentDao.updateAvailableQuantity(equipmentId, newQuantity);
    }

    /**
     * Giảm số lượng khả dụng (khi mượn thiết bị)
     */
    public boolean decreaseAvailableQuantity(int equipmentId, int quantity) {
        Equipment equipment = equipmentDao.getEquipmentById(equipmentId);
        if (equipment == null) {
            System.out.println("❌ Thiết bị không tồn tại");
            return false;
        }
        
        if (equipment.getAvailableQuantity() < quantity) {
            System.out.println("❌ Số lượng khả dụng không đủ (Hiện có: " + 
                             equipment.getAvailableQuantity() + ")");
            return false;
        }
        
        return equipmentDao.decreaseAvailableQuantity(equipmentId, quantity);
    }

    /**
     * Tăng số lượng khả dụng (khi trả thiết bị)
     */
    public boolean increaseAvailableQuantity(int equipmentId, int quantity) {
        Equipment equipment = equipmentDao.getEquipmentById(equipmentId);
        if (equipment == null) {
            System.out.println("❌ Thiết bị không tồn tại");
            return false;
        }
        
        if (equipment.getAvailableQuantity() + quantity > equipment.getTotalQuantity()) {
            System.out.println("❌ Vượt quá tổng số lượng thiết bị");
            return false;
        }
        
        return equipmentDao.increaseAvailableQuantity(equipmentId, quantity);
    }

    /**
     * Cập nhật trạng thái thiết bị
     */
    public boolean updateEquipmentStatus(int equipmentId, String status) {
        return equipmentDao.updateStatus(equipmentId, status);
    }

    // ========== LẤY THÔNG TIN THIẾT BỊ ==========
    /**
     * Lấy danh sách tất cả thiết bị
     */
    public List<Equipment> getAllEquipment() {
        return equipmentDao.getAllEquipment();
    }

    /**
     * Lấy thiết bị theo ID
     */
    public Equipment getEquipmentById(int equipmentId) {
        return equipmentDao.getEquipmentById(equipmentId);
    }

    /**
     * Lấy thiết bị khả dụng (ACTIVE)
     */
    public List<Equipment> getAvailableEquipment() {
        return equipmentDao.getAvailableEquipment();
    }

    /**
     * Kiểm tra thiết bị có sẵn để mượn không
     */
    public boolean isEquipmentAvailable(int equipmentId, int quantityNeeded) {
        Equipment equipment = equipmentDao.getEquipmentById(equipmentId);
        if (equipment == null) return false;
        return equipment.getAvailableQuantity() >= quantityNeeded && "ACTIVE".equals(equipment.getStatus());
    }

    // ========== XÓA THIẾT BỊ ==========
    /**
     * Xóa thiết bị
     */
    public boolean deleteEquipment(int equipmentId) {
        return equipmentDao.deleteEquipment(equipmentId);
    }

    /**
     * Lấy tổng số thiết bị
     */
    public int getTotalEquipment() {
        return equipmentDao.getTotalEquipment();
    }
}


