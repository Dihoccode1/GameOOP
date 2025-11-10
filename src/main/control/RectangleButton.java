package main.control; // Gói chứa các lớp điều khiển

import main.control.Button; // Import lớp cha trừu tượng Button
import java.awt.Color;   // Import Color để quản lý màu sắc
import java.awt.Font;    // Import Font để quản lý phông chữ
import java.awt.Graphics; // Import Graphics để vẽ

// Lớp RectangleButton (Nút bấm Hình chữ nhật) kế thừa từ lớp Button
public class RectangleButton extends Button {
    
    // Hàm khởi tạo, gọi hàm khởi tạo của lớp cha (Button)
    public RectangleButton(String text, int posX, int posY, int width, int height, int paddingTextX, int paddingTextY,
            Color bgColor) {
        super(text, posX, posY, width, height, paddingTextX, paddingTextY, bgColor);
    }

    @Override
    // Phương thức kiểm tra xem tọa độ chuột (x, y) có nằm trong vùng nút không
    public boolean isInButton(int x, int y) {
        // Chỉ kiểm tra khi nút được kích hoạt (enabled)
        return (enabled && x >= posX && x <= posX + width && y >= posY && y <= posY + height);
    }
    
    @Override
    public void draw(Graphics g) { // Phương thức vẽ nút
        
        // 1. Thiết lập màu nền dựa trên trạng thái
        if(enabled) {
            switch (state) {
                case NONE: g.setColor(bgColor); break;      // Trạng thái bình thường
                case PRESSED: g.setColor(pressedBgColor); break; // Trạng thái nhấn
                case HOVER: g.setColor(hoverBgColor); break;     // Trạng thái di chuột qua
            }
        } else {
            g.setColor(Color.GRAY); // Nếu bị vô hiệu hóa, dùng màu xám
        }
        
        // Vẽ hình chữ nhật nền (tô đầy)
        g.fillRect(posX, posY, width, height);
        
        // 2. Vẽ viền nút
        g.setColor(Color.PINK); // Thiết lập màu viền (hồng)
        g.drawRect(posX, posY, width, height);       // Vẽ viền ngoài
        g.drawRect(posX + 1, posY + 1, width - 2, height - 2); // Vẽ viền trong (tạo độ dày/hiệu ứng 3D nhẹ)
        
        // 3. Vẽ văn bản (text)
        g.setColor(Color.WHITE); // Thiết lập màu chữ (trắng)
        g.setFont(new Font("TimesRoman", Font.PLAIN, 14)); // Thiết lập phông chữ
        
        // Vẽ văn bản tại vị trí đã cộng thêm khoảng đệm (padding)
        g.drawString(text, posX + paddingTextX, posY + paddingTextY);
    }
}