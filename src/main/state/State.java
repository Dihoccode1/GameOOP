package main.state; // Gói (package) chứa các lớp đại diện cho trạng thái của trò chơi

import main.userinterface.GamePanel; // Import lớp GamePanel, để các trạng thái có thể tương tác với bảng vẽ game
import java.awt.image.BufferedImage; // Import lớp BufferedImage, dùng để lưu trữ hình ảnh của trạng thái

/**
 * Lớp trừu tượng (abstract class) State.
 * Đây là lớp cơ sở cho tất cả các trạng thái trong game (ví dụ: Menu, Playing, GameOver).
 * Nó định nghĩa khuôn mẫu (các phương thức bắt buộc) mà mọi trạng thái phải thực hiện.
 */
public abstract class State {
    
    // Biến protected để lưu trữ tham chiếu đến GamePanel, cho phép các trạng thái con truy cập
    protected GamePanel gamePanel; 
    
    // Hàm khởi tạo, bắt buộc mọi trạng thái phải nhận GamePanel khi được tạo
    public State(GamePanel gamePanel) {
       this.gamePanel = gamePanel; 
    }
    
    // Phương thức trừu tượng: Bắt buộc các trạng thái con phải định nghĩa logic cập nhật
    // Ví dụ: cập nhật vị trí nhân vật, tính toán va chạm, logic AI, v.v.
    public abstract void Update(); 
    
    // Phương thức trừu tượng: Bắt buộc các trạng thái con phải định nghĩa logic vẽ/render
    // Ví dụ: vẽ bối cảnh, nhân vật, điểm số, v.v. (thường vẽ vào BufferedImage)
    public abstract void Render(); 
    
    // Phương thức trừu tượng: Bắt buộc các trạng thái con phải trả về hình ảnh đã được vẽ
    // GamePanel sẽ lấy hình ảnh này để hiển thị lên màn hình
    public abstract BufferedImage getBufferedImage(); 
    
    // Phương thức trừu tượng: Xử lý sự kiện khi một phím được nhấn xuống
    public abstract void setPressedButton(int code); 
    
    // Phương thức trừu tượng: Xử lý sự kiện khi một phím được nhả ra
    public abstract void setReleasedButton(int code); 
}