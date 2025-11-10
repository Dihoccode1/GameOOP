package main.effect; // Gói chứa các lớp liên quan đến hiệu ứng (effects)


import java.awt.Graphics;          // Import lớp Graphics cơ bản
import java.awt.Graphics2D;        // Import lớp Graphics2D (dùng để vẽ)
import java.awt.image.BufferedImage; // Import lớp BufferedImage (dùng để lưu trữ hình ảnh)

/**
 * Lớp FrameImage.
 * Đại diện cho một khung hình ảnh đơn lẻ (single frame) được sử dụng trong Animation.
 * Lớp này giúp quản lý tên và dữ liệu hình ảnh (BufferedImage) của khung hình.
 */
public class FrameImage{
    
    private String name;           // Tên của khung hình (ví dụ: "run1", "jump")
    private BufferedImage image;   // Đối tượng hình ảnh thực tế của khung hình
    
    // Hàm khởi tạo 1: Khởi tạo với tên và hình ảnh đã có
    public FrameImage(String name, BufferedImage image){
        this.name = name;
        this.image = image;
    }
    
    // Hàm khởi tạo 2: Hàm khởi tạo sao chép (Copy Constructor)
    // Tạo một bản sao sâu (deep copy) của FrameImage khác
    public FrameImage(FrameImage frameImage){
        // Tạo một BufferedImage mới với cùng kích thước và kiểu dữ liệu
        image = new BufferedImage(frameImage.getWidthImage(), 
                frameImage.getHeightImage(), frameImage.image.getType());
        
        // Lấy đối tượng Graphics từ BufferedImage mới
        Graphics g = image.getGraphics();
        // Sao chép nội dung hình ảnh từ frameImage gốc sang image mới
        g.drawImage(frameImage.image, 0, 0, null);
        
        name = frameImage.name; // Sao chép tên
    }
    
    // Phương thức vẽ khung hình lên màn hình
    public void draw(int x, int y, Graphics2D g2){
        
        // Vẽ hình ảnh tại vị trí (x, y)
        // Lưu ý: Hình ảnh được căn giữa tại tọa độ (x, y)
        g2.drawImage(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
        
    }
    
    // Hàm khởi tạo 3: Khởi tạo mặc định (trống)
    public FrameImage(){
        this.name = null;
        image = null;
    }
    
    // --- GETTER & SETTER ---
    
    // Trả về chiều rộng của hình ảnh
    public int getWidthImage(){
        return image.getWidth();
    }

    // Trả về chiều cao của hình ảnh
    public int getHeightImage(){
        return image.getHeight();
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    
    public BufferedImage getImage(){
        return image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }

}