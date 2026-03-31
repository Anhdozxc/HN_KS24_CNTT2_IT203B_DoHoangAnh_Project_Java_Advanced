package util;

import java.time.LocalDateTime;

/**
 * Lớp tiện ích để validate (kiểm tra) dữ liệu
 * Giúp đảm bảo tính hợp lệ của dữ liệu trước khi lưu vào database
 */
public class ValidationUtil {

    //  KIỂM TRA CHUỖI
    /**
     * Kiểm tra chuỗi không trống
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Kiểm tra chuỗi có độ dài trong khoảng
     */
    public static boolean isLengthBetween(String str, int minLength, int maxLength) {
        return str != null && str.length() >= minLength && str.length() <= maxLength;
    }

    // KIỂM TRA ĐIỆN THOẠI
    /**
     * Kiểm tra số điện thoại hợp lệ (10 chữ số)
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    //  KIỂM TRA EMAIL
    /**
     * Kiểm tra email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    //  KIỂM TRA USERNAME
    /**
     * Kiểm tra username hợp lệ (3-20 ký tự)
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    //  KIỂM TRA MẬT KHẨU
    /**
     * Kiểm tra mật khẩu hợp lệ (tối thiểu 6 ký tự)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    //  KIỂM TRA SỐ
    /**
     * Kiểm tra số nguyên dương
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }

    /**
     * Kiểm tra số dương (có thể là 0)
     */
    public static boolean isNonNegative(int number) {
        return number >= 0;
    }

    /**
     * Kiểm tra số dương (double)
     */
    public static boolean isPositiveDouble(double number) {
        return number > 0;
    }

    //  KIỂM TRA NGÀY GIỜ
    /**
     * Kiểm tra thời gian bắt đầu < thời gian kết thúc
     */
    public static boolean isTimeRangeValid(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }

    /**
     * Kiểm tra thời gian không trong quá khứ
     */
    public static boolean isTimeNotInPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Kiểm tra thời gian bắt đầu không trong quá khứ
     */
    public static boolean isStartTimeValid(LocalDateTime startTime) {
        return isTimeNotInPast(startTime);
    }

    /**
     * Kiểm tra số lượng người tham dự có hợp lệ không
     */
    public static boolean isParticipantCountValid(int participantCount, int roomCapacity) {
        return participantCount > 0 && participantCount <= roomCapacity;
    }

    //  KIỂM TRA VAI TRÒ
    /**
     * Kiểm tra vai trò hợp lệ
     */
    public static boolean isValidRole(String role) {
        if (role == null) return false;
        role = role.trim().toUpperCase();
        return role.equals("EMPLOYEE") || role.equals("SUPPORT_STAFF") || role.equals("ADMIN");
    }

    //  KIỂM TRA TRẠNG THÁI
    /**
     * Kiểm tra trạng thái booking hợp lệ
     */
    public static boolean isValidBookingStatus(String status) {
        if (status == null) return false;
        status = status.trim().toUpperCase();
        return status.equals("PENDING") || status.equals("APPROVED") || 
               status.equals("REJECTED") || status.equals("ASSIGNED");
    }

    /**
     * Kiểm tra trạng thái chuẩn bị hợp lệ
     */
    public static boolean isValidPreparationStatus(String preparationStatus) {
        if (preparationStatus == null) return false;
        return preparationStatus.equals("Preparing") ||
               preparationStatus.equals("Ready") ||
               preparationStatus.equals("Missing Equipment");
    }

    /**
     * Kiểm tra trạng thái phòng hợp lệ
     */
    public static boolean isValidRoomStatus(String status) {
        if (status == null) return false;
        status = status.trim().toUpperCase();
        return status.equals("AVAILABLE") || status.equals("MAINTENANCE");
    }

    // KIỂM TRA TỰ DỌN DẸP
    /**
     * Kiểm tra tất cả các trường yêu cầu không trống
     */
    public static boolean validateBookingData(String roomName, LocalDateTime startTime, 
                                              LocalDateTime endTime, int participantCount, 
                                              int roomCapacity) {
        return isNotEmpty(roomName) && 
               isTimeRangeValid(startTime, endTime) && 
               isStartTimeValid(startTime) &&
               isParticipantCountValid(participantCount, roomCapacity);
    }
}


