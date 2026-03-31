package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.Console;

/**
 * Lớp tiện ích để nhận input từ người dùng
 * Cung cấp các phương thức đơn giản để đọc dữ liệu từ console
 */
public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    //  NHẬP SỐ NGUYÊN
    /**
     * Nhập số nguyên từ người dùng
     * @param prompt Thông điệp nhắc nhở
     * @return Số nguyên đã nhập
     */
    public static int inputInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên hợp lệ!");
            }
        }
    }

    public static int inputPositiveInt(String prompt) {
        while (true) {
            int num = inputInt(prompt);
            if (num > 0) {
                return num;
            }
            System.out.println("Lỗi: Vui lòng nhập một số dương!");
        }
    }

    public static int inputNonNegativeInt(String prompt) {
        while (true) {
            int num = inputInt(prompt);
            if (num >= 0) {
                return num;
            }
            System.out.println("Lỗi: Vui lòng nhập số từ 0 trở lên!");
        }
    }

    //  NHẬP SỐ THẬP PHÂN
    public static double inputDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số hợp lệ!");
            }
        }
    }

    // Nhập số thập phân dương
    public static double inputPositiveDouble(String prompt) {
        while (true) {
            double num = inputDouble(prompt);
            if (num > 0) {
                return num;
            }
            System.out.println("Lỗi: Vui lòng nhập một số dương!");
        }
    }


    //  NHẬP CHUỖI
    public static String inputString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input;
    }

    public static String inputOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String inputNonEmptyString(String prompt) {
        while (true) {
            String input = inputString(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Lỗi: Vui lòng nhập dữ liệu, không được để trống!");
        }
    }

    // NHẬP NGÀY GIỜ
    public static LocalDateTime inputDateTime(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return LocalDateTime.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Lỗi: Định dạng ngày giờ không đúng! Vui lòng nhập theo: yyyy-MM-dd HH:mm");
            }
        }
    }

    //  NHẬP LỰA CHỌN
    public static int inputChoice(String prompt, int minChoice, int maxChoice) {
        while (true) {
            int choice = inputInt(prompt);
            if (choice >= minChoice && choice <= maxChoice) {
                return choice;
            }
            System.out.println("Lỗi: Vui lòng chọn từ " + minChoice + " đến " + maxChoice + "!");
        }
    }

    // NHẬP ĐIỆN THOẠI
    public static String inputPhone(String prompt) {
        while (true) {
            String phone = inputNonEmptyString(prompt);
            if (phone.matches("\\d{10}")) {
                return phone;
            }
            System.out.println("Lỗi: Số điện thoại phải có 10 chữ số!");
        }
    }

    //  NHẬP EMAIL
    public static String inputEmail(String prompt) {
        while (true) {
            String email = inputNonEmptyString(prompt);
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            }
            System.out.println("Lỗi: Email không hợp lệ!");
        }
    }

    //  NHẬP USERNAME
    public static String inputUsername(String prompt) {
        while (true) {
            String username = inputNonEmptyString(prompt);
            if (username.matches("^[a-zA-Z0-9_]{3,20}$")) {
                return username;
            }
            System.out.println("Lỗi: Username phải 3-20 ký tự (chữ cái, số, gạch dưới)!");
        }
    }

    //  NHẬP MẬT KHẨU
    public static String inputPassword(String prompt) {
        while (true) {
            String password = inputNonEmptyString(prompt);
            if (password.length() >= 6) {
                return password;
            }
            System.out.println("Lỗi: Mật khẩu phải có ít nhất 6 ký tự!");
        }
    }

    // Nhập mật khẩu với hiển thị dấu sao (*)
    public static String inputPasswordMasked(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String password = "";

                // Thử sử dụng Console.readPassword() nếu có sẵn
                Console console = System.console();
                if (console != null) {
                    // Nếu chương trình chạy từ terminal, sử dụng readPassword
                    char[] passwordChars = console.readPassword();
                    password = new String(passwordChars);
                } else {
                    // Nếu chạy từ IDE, fallback về Scanner thường
                    password = scanner.nextLine().trim();
                }

                if (password.length() >= 6) {
                    return password;
                }
                System.out.println("Lỗi: Mật khẩu phải có ít nhất 6 ký tự!");
            } catch (Exception e) {
                System.out.println("Lỗi: Không thể đọc mật khẩu!");
            }
        }
    }


    //  NHẬP TÊN NGƯỜI DÙNG
    public static String inputFullName(String prompt) {
        while (true) {
            String fullname = inputNonEmptyString(prompt);
            if (fullname.matches("^[a-zA-ZÀ-ỿ\\s]+$")) {
                return fullname;
            }
            System.out.println("Lỗi: Tên chỉ được chứa chữ cái và khoảng trắng!");
        }
    }
}
