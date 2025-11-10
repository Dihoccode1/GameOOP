package main.userinterface; // Khai báo gói (package) chứa lớp này, thường là nơi chứa các giao diện người dùng

import main.effect.CacheDataLoader; // Import lớp CacheDataLoader, dùng để tải tài nguyên game (ảnh, âm thanh,...)
import java.awt.Dimension;        // Import lớp Dimension, dùng để lưu trữ kích thước (chiều rộng, chiều cao)
import java.awt.Toolkit;          // Import lớp Toolkit, cung cấp các công cụ tương tác với hệ thống cửa sổ
import java.io.IOException;       // Import IOException, để xử lý lỗi khi đọc/ghi file (ví dụ: khi tải tài nguyên)

import javax.swing.JFrame;        // Import lớp JFrame, dùng để tạo cửa sổ chính cho ứng dụng đồ họa

public class GameFrame extends JFrame{ // Khai báo lớp GameFrame, kế thừa (extends) từ JFrame để tạo cửa sổ

    public static final int SCREEN_WIDTH = 1000; // Chiều rộng cố định của cửa sổ game (hằng số)
    public static final int SCREEN_HEIGHT = 600; // Chiều cao cố định của cửa sổ game (hằng số)

    GamePanel gamePanel; // Khai báo biến đại diện cho "bảng vẽ" game, nơi mọi thứ sẽ được vẽ

    public GameFrame(){ // Hàm khởi tạo (Constructor) của lớp GameFrame, chạy khi tạo đối tượng GameFrame

        super("Mega Man java game"); // Gọi hàm khởi tạo của lớp cha (JFrame), đặt tiêu đề cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Thiết lập: Khi nhấn nút 'X', chương trình sẽ thoát

        // Lấy thông tin về màn hình máy tính để căn giữa cửa sổ
        Toolkit toolkit = this.getToolkit(); 
        Dimension solution = toolkit.getScreenSize(); // Lấy kích thước thực tế của màn hình

        // Khối lệnh try-catch để tải dữ liệu game, đề phòng lỗi I/O (Input/Output)
        try {
            CacheDataLoader.getInstance().LoadData(); // Gọi phương thức tải tất cả tài nguyên game vào bộ nhớ
        } catch (IOException ex) {
            ex.printStackTrace(); // Nếu có lỗi (ví dụ: không tìm thấy file), in thông báo lỗi ra console
        }

        // Đặt vị trí và kích thước cửa sổ game
        // Tính toán để cửa sổ xuất hiện ở giữa màn hình: (width màn hình - width cửa sổ) / 2
        this.setBounds((solution.width - SCREEN_WIDTH)/2, (solution.height - SCREEN_HEIGHT)/2, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Khởi tạo và thêm GamePanel vào cửa sổ
        gamePanel = new GamePanel(); // Tạo đối tượng GamePanel (bảng vẽ game)
        addKeyListener(gamePanel);   // Đăng ký lắng nghe sự kiện nhấn phím trên GamePanel (để điều khiển)
        add(gamePanel);              // Thêm GamePanel vào cửa sổ GameFrame

    }

    public void startGame(){ // Phương thức để bắt đầu vòng lặp game

        gamePanel.startGame(); // Bắt đầu vòng lặp game (game loop) bên trong GamePanel
        this.setVisible(true); // Hiển thị cửa sổ GameFrame lên màn hình

    }

    public static void main(String arg[]){ // Hàm main, điểm khởi đầu của chương trình

        GameFrame gameFrame = new GameFrame(); // Tạo một đối tượng GameFrame (tạo cửa sổ và tải dữ liệu)
        gameFrame.startGame(); // Bắt đầu trò chơi (khởi động game loop và hiển thị cửa sổ)

    }
        
}