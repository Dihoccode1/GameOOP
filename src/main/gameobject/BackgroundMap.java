package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.CacheDataLoader; // Import lớp để tải dữ liệu (bản đồ, hình ảnh tile)
import main.userinterface.GameFrame; // Import GameFrame để lấy kích thước màn hình
import java.awt.Color;             // Import Color (được sử dụng cho debug hoặc mặc định)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ đồ họa

// Lớp BackgroundMap (Bản đồ Nền) kế thừa từ GameObject
// Lớp này quản lý việc vẽ các ô (tile) hình ảnh tạo nên bối cảnh game.
public class BackgroundMap extends GameObject {

    public int[][] map; // Mảng 2 chiều lưu trữ dữ liệu bản đồ nền (mỗi số đại diện cho một loại tile)
    private int tileSize;    // Kích thước của một ô (tile) trên bản đồ (pixel)
    
    // Hàm khởi tạo
    public BackgroundMap(float x, float y, GameWorldState gameWorld) {
        super(x, y, gameWorld); // Gọi hàm khởi tạo lớp cha (GameObject)
        map = CacheDataLoader.getInstance().getBackgroundMap(); // Tải dữ liệu bản đồ nền từ file
        tileSize = 30; // Kích thước mỗi ô là 30x30 pixel
    }

    @Override
    public void Update() {} // Bản đồ nền thường không cần cập nhật logic trong vòng lặp game
    
    // Phương thức vẽ bản đồ nền
    public void draw(Graphics2D g2){
        
        Camera camera = getGameWorld().camera; // Lấy camera để tính toán vị trí vẽ (cuộn map)
        
        g2.setColor(Color.RED); // Thiết lập màu (chủ yếu cho debug)
        
        // Duyệt qua tất cả các hàng (i) và cột (j) trong dữ liệu bản đồ
        for(int i = 0;i< map.length;i++)
            for(int j = 0;j<map[0].length;j++)
                // Kiểm tra 1: map[i][j]!=0: Đảm bảo ô đó có hình ảnh để vẽ (không phải ô trống)
                // Kiểm tra 2, 3, 4, 5: Tối ưu hóa render (chỉ vẽ các ô nằm trong tầm nhìn của camera)
                if(map[i][j]!=0 && j*tileSize - camera.getPosX() > -30 && j*tileSize - camera.getPosX() < GameFrame.SCREEN_WIDTH
                        && i*tileSize - camera.getPosY() > -30 && i*tileSize - camera.getPosY() < GameFrame.SCREEN_HEIGHT){ 
                    
                    // Lấy hình ảnh tile tương ứng với số map[i][j]
                    // Vẽ hình ảnh tại vị trí: (j * tileSize) - vị trí camera X
                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("tiled"+map[i][j]).getImage(), (int) getPosX() + j*tileSize - (int) camera.getPosX(), 
                        // Vẽ tại vị trí: (i * tileSize) - vị trí camera Y
                        (int) getPosY() + i*tileSize - (int) camera.getPosY(), null);
                }
        
    }
    
}