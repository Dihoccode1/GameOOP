package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.CacheDataLoader; // Import lớp để tải dữ liệu (bản đồ vật lý)
import java.awt.Color;             // Import Color để vẽ màu
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

/**
 * Lớp PhysicalMap (Bản đồ Vật lý).
 * Dùng để lưu trữ và xử lý logic va chạm giữa các GameObject và môi trường.
 */
public class PhysicalMap extends GameObject{

    public int[][] phys_map; // Mảng 2 chiều lưu trữ dữ liệu bản đồ vật lý (0: trống, 1: vật thể rắn)
    private int tileSize;    // Kích thước của một ô (tile) trên bản đồ (pixel)
    
    // Hàm khởi tạo
    public PhysicalMap(float x, float y, GameWorldState gameWorld) {
        super(x, y, gameWorld); // Gọi hàm khởi tạo lớp cha (GameObject)
        this.tileSize = 30; // Kích thước mỗi ô là 30x30 pixel
        // Tải dữ liệu bản đồ vật lý từ CacheDataLoader
        phys_map = CacheDataLoader.getInstance().getPhysicalMap(); 
    }
    
    public int getTileSize(){
        return tileSize; // Trả về kích thước của một ô
    }
    
    @Override
    public void Update() {} // Bản đồ vật lý thường không cần Update trong vòng lặp game
    
    // Phương thức kiểm tra va chạm với TƯỜNG TRÊN (trần nhà)
    public Rectangle haveCollisionWithTop(Rectangle rect){

        // Tính toán phạm vi ô (tile) theo X cần kiểm tra
        int posX1 = rect.x/tileSize;
        posX1 -= 2; // Giảm bớt để kiểm tra thêm ô bên trái
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2; // Tăng thêm để kiểm tra thêm ô bên phải

        // Tính toán vị trí ô theo Y (chỉ kiểm tra khu vực ngay trên đỉnh vật thể)
        int posY = rect.y/tileSize;

        // Giới hạn chỉ số X trong phạm vi bản đồ
        if(posX1 < 0) posX1 = 0;
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        
        // Duyệt từ vị trí Y hiện tại trở lên (tìm trần nhà)
        for(int y = posY; y >= 0; y--){
            // Duyệt trong phạm vi X đã tính
            for(int x = posX1; x <= posX2; x++){
                
                if(phys_map[y][x] == 1){ // Nếu là ô vật lý (solid tile)
                    // Tạo một Rectangle đại diện cho ô vật lý đó
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    // Nếu vùng va chạm của đối tượng (rect) cắt ngang qua ô vật lý (r)
                    if(rect.intersects(r))
                        return r; // Trả về ô vật lý gây ra va chạm
                }
            }
        }
        return null; // Không có va chạm
    }
    
    
    // Phương thức kiểm tra va chạm với MẶT ĐẤT
    public Rectangle haveCollisionWithLand(Rectangle rect){

        // Tính toán phạm vi ô (tile) theo X cần kiểm tra (tương tự haveCollisionWithTop)
        int posX1 = rect.x/tileSize;
        posX1 -= 2; 
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2;

        // Tính toán vị trí ô theo Y (chỉ kiểm tra khu vực ngay dưới đáy vật thể)
        int posY = (rect.y + rect.height)/tileSize;

        // Giới hạn chỉ số X
        if(posX1 < 0) posX1 = 0;
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        
        // Duyệt từ vị trí Y hiện tại trở xuống (tìm mặt đất)
        for(int y = posY; y < phys_map.length;y++){
            for(int x = posX1; x <= posX2; x++){
                
                if(phys_map[y][x] == 1){ // Nếu là ô vật lý
                    // Tạo một Rectangle đại diện cho ô vật lý đó
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    // Nếu rect cắt ngang qua ô vật lý
                    if(rect.intersects(r))
                        return r; // Trả về ô vật lý gây ra va chạm
                }
            }
        }
        return null; // Không có va chạm
    }
    
    // Phương thức kiểm tra va chạm với TƯỜNG PHẢI
    public Rectangle haveCollisionWithRightWall(Rectangle rect){
   
        // Tính toán phạm vi ô (tile) theo Y cần kiểm tra
        int posY1 = rect.y/tileSize;
        posY1-=2; // Kiểm tra thêm ô phía trên
        int posY2 = (rect.y + rect.height)/tileSize;
        posY2+=2; // Kiểm tra thêm ô phía dưới
        
        // Tính toán phạm vi ô theo X (chỉ kiểm tra khu vực ngay bên phải vật thể)
        int posX1 = (rect.x + rect.width)/tileSize;
        int posX2 = posX1 + 3; // Kiểm tra 3 ô tiếp theo
        
        // Giới hạn chỉ số X và Y
        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        if(posY1 < 0) posY1 = 0;
        if(posY2 >= phys_map.length) posY2 = phys_map.length - 1;
        
        
        // Duyệt trong phạm vi X (từ phải sát vật thể ra xa)
        for(int x = posX1; x <= posX2; x++){
            // Duyệt trong phạm vi Y
            for(int y = posY1; y <= posY2;y++){
                if(phys_map[y][x] == 1){ // Nếu là ô vật lý
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    // Kiểm tra va chạm: chỉ xét va chạm nếu ô vật lý không quá cao so với đáy vật thể
                    if(r.y < rect.y + rect.height - 1 && rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
        
    }
    
    // Phương thức kiểm tra va chạm với TƯỜNG TRÁI
    public Rectangle haveCollisionWithLeftWall(Rectangle rect){
        
        // Tính toán phạm vi ô (tile) theo Y cần kiểm tra (tương tự RightWall)
        int posY1 = rect.y/tileSize;
        posY1-=2;
        int posY2 = (rect.y + rect.height)/tileSize;
        posY2+=2;
        
        // Tính toán phạm vi ô theo X (chỉ kiểm tra khu vực ngay bên trái vật thể)
        int posX1 = rect.x/tileSize;
        int posX2 = posX1 - 3; // Kiểm tra 3 ô ngược lại
        
        // Giới hạn chỉ số X và Y
        if(posX2 < 0) posX2 = 0;
        if(posY1 < 0) posY1 = 0;
        if(posY2 >= phys_map.length) posY2 = phys_map.length - 1;
        
        
        // Duyệt trong phạm vi X (từ trái sát vật thể ra xa)
        for(int x = posX1; x >= posX2; x--){
            // Duyệt trong phạm vi Y
            for(int y = posY1; y <= posY2;y++){
                if(phys_map[y][x] == 1){ // Nếu là ô vật lý
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    // Kiểm tra va chạm (tương tự RightWall)
                    if(r.y < rect.y + rect.height - 1 && rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
        
    }
    
    // Phương thức vẽ bản đồ vật lý (chủ yếu dùng để debug)
    public void draw(Graphics2D g2){
        
        Camera camera = getGameWorld().camera; // Lấy camera để tính toán vị trí vẽ
        
        g2.setColor(Color.GRAY); // Đặt màu vẽ (ví dụ: xám)
        // Duyệt qua tất cả các ô trong bản đồ
        for(int i = 0;i< phys_map.length;i++)
            for(int j = 0;j<phys_map[0].length;j++)
                // Nếu giá trị ô là 1 (solid tile)
                if(phys_map[i][j]!=0) 
                    // Vẽ hình chữ nhật đại diện cho ô vật lý, trừ đi vị trí camera để cuộn map
                    g2.fillRect((int) getPosX() + j*tileSize - (int) camera.getPosX(), 
                            (int) getPosY() + i*tileSize - (int) camera.getPosY(), tileSize, tileSize);
        
    }
    
}