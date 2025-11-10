package main.control; // Gói (package) chứa các lớp điều khiển (control)

import java.awt.Color;   // Import lớp Color để quản lý màu sắc
import java.awt.Graphics; // Import lớp Graphics để vẽ

// Lớp trừu tượng Button (Nút bấm)
public abstract class Button {

    // --- HẰNG SỐ TRẠNG THÁI CỦA NÚT ---
    public static final int NONE = 0;    // Trạng thái bình thường (không có tương tác)
    public static final int PRESSED = 1; // Trạng thái khi nút đang bị nhấn
    public static final int HOVER = 2;   // Trạng thái khi con trỏ chuột di chuột qua nút
    
    // --- THUỘC TÍNH CƠ BẢN ---
    protected String text;     // Văn bản hiển thị trên nút
    protected int posX;        // Vị trí X của nút (góc trên trái)
    protected int posY;        // Vị trí Y của nút (góc trên trái)
    protected int width;       // Chiều rộng của nút
    protected int height;      // Chiều cao của nút
    protected int paddingTextX; // Khoảng đệm X cho văn bản (dùng để căn chỉnh)
    protected int paddingTextY; // Khoảng đệm Y cho văn bản
    protected boolean enabled; // Cờ (flag) cho biết nút có được kích hoạt (sử dụng) không
    
    // --- THUỘC TÍNH TRẠNG THÁI VÀ MÀU SẮC ---
    protected int state;             // Trạng thái hiện tại của nút (NONE, PRESSED, HOVER)
    protected Color bgColor;         // Màu nền mặc định
    protected Color pressedBgColor;  // Màu nền khi nút bị nhấn
    protected Color hoverBgColor;    // Màu nền khi di chuột qua
    
    // Hàm khởi tạo
    public Button(String text, int posX, int posY, int width, int height, int paddingTextX, int paddingTextY,
            Color bgColor) {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.paddingTextX = paddingTextX;
        this.paddingTextY = paddingTextY;
        this.bgColor = bgColor;
        enabled = true; // Mặc định nút được kích hoạt
    }
    
    // --- GETTER & SETTER ---
    
    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public void setBgColor(Color color) {
        bgColor = color;
    }

    public void setHoverBgColor(Color color) {
        hoverBgColor = color;
    }
    public void setPressedBgColor(Color color) {
        pressedBgColor = color;
    }
    
    // --- PHƯƠNG THỨC TRỪU TƯỢNG ---
    
    // Kiểm tra xem tọa độ (x, y) có nằm trong vùng của nút không
    public abstract boolean isInButton(int x, int y); 
    
    // Vẽ nút
    public abstract void draw(Graphics g); 
}