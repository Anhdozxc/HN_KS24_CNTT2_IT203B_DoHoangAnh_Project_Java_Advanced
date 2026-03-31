package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    // Thong tin ket noi database
    private static final String URL = "jdbc:mysql://localhost:3306/meeting_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Lỗi kết nối database: " + e.getMessage());
            return null;
        }
    }
}

