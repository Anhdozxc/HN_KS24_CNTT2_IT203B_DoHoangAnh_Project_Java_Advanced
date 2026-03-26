package service;

import dao.ServiceDao;
import model.Service;
import util.ValidationUtil;
import java.util.List;

/**
 * Lớp Service xử lý logic liên quan đến quản lý dịch vụ
 */
public class ServiceService {
    private ServiceDao serviceDao;

    public ServiceService() {
        this.serviceDao = new ServiceDao();
    }

    // ========== THÊM DỊCH VỤ ==========
    /**
     * Thêm dịch vụ mới
     */
    public boolean addService(String name, double price, String description) {
        // Kiểm tra hợp lệ
        if (!ValidationUtil.isNotEmpty(name)) {
            System.out.println("Lỗi: Tên dịch vụ không được để trống");
            return false;
        }
        
        if (!ValidationUtil.isPositiveDouble(price)) {
            System.out.println("Lỗi: Giá dịch vụ phải lớn hơn 0");
            return false;
        }
        
        // Kiểm tra tên dịch vụ đã tồn tại
        if (serviceDao.serviceNameExists(name)) {
            System.out.println("Lỗi: Tên dịch vụ đã tồn tại!");
            return false;
        }
        
        Service newService = new Service(name, price, description);
        return serviceDao.addService(newService);
    }

    // ========== CẬP NHẬT DỊCH VỤ ==========
    /**
     * Cập nhật thông tin dịch vụ
     */
    public boolean updateService(int serviceId, String name, double price,
                                 String description, String status) {
        Service service = serviceDao.getServiceById(serviceId);
        if (service == null) {
            System.out.println("❌ Dịch vụ không tồn tại");
            return false;
        }
        
        service.setName(name);
        service.setPrice(price);
        service.setDescription(description);
        service.setStatus(status);
        
        return serviceDao.updateService(service);
    }

    /**
     * Cập nhật giá dịch vụ
     */
    public boolean updatePrice(int serviceId, double newPrice) {
        if (!ValidationUtil.isPositiveDouble(newPrice)) {
            System.out.println("❌ Giá phải lớn hơn 0");
            return false;
        }
        
        return serviceDao.updatePrice(serviceId, newPrice);
    }

    /**
     * Cập nhật trạng thái dịch vụ
     */
    public boolean updateServiceStatus(int serviceId, String status) {
        return serviceDao.updateStatus(serviceId, status);
    }

    // ========== LẤY THÔNG TIN DỊCH VỤ ==========
    /**
     * Lấy danh sách tất cả dịch vụ
     */
    public List<Service> getAllServices() {
        return serviceDao.getAllServices();
    }

    /**
     * Lấy dịch vụ theo ID
     */
    public Service getServiceById(int serviceId) {
        return serviceDao.getServiceById(serviceId);
    }

    /**
     * Lấy dịch vụ hoạt động (ACTIVE)
     */
    public List<Service> getActiveServices() {
        return serviceDao.getActiveServices();
    }

    // ========== XÓA DỊCH VỤ ==========
    /**
     * Xóa dịch vụ
     */
    public boolean deleteService(int serviceId) {
        return serviceDao.deleteService(serviceId);
    }

    /**
     * Lấy tổng số dịch vụ
     */
    public int getTotalServices() {
        return serviceDao.getTotalServices();
    }
}


