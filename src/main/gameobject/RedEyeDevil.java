package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import main.effect.CacheDataLoader; // Import lớp để tải các tài nguyên (animation)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp RedEyeDevil (Kẻ thù Quỷ Mắt Đỏ) kế thừa từ ParticularObject (Đối tượng đặc biệt/động)
public class RedEyeDevil extends ParticularObject {

    private Animation forwardAnim, backAnim; // Animation khi quay mặt về phía trước và phía sau
    
    private long startTimeToShoot; // Biến lưu trữ thời điểm bắt đầu bắn viên đạn gần nhất
    
    public RedEyeDevil(float x, float y, GameWorldState gameWorld) {
        // Gọi hàm khởi tạo lớp cha: x, y, rộng=127, cao=89, khối lượng=0 (không chịu trọng lực), máu=100, thế giới game
        super(x, y, 127, 89, 0, 100, gameWorld);
        
        // Lấy animation "redeye"
        backAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim.flipAllImage(); // Lật hình ảnh cho animation hướng về phía trước (thường là phải)
        
        startTimeToShoot = 0; // Khởi tạo thời điểm bắn
        setDamage(10); // Đặt sát thương khi va chạm là 10
        // Đặt thời gian bất tử sau khi bị tấn công (300 triệu nano giây = 0.3 giây)
        setTimeForNoBehurt(300000000); 
    }

    @Override
    public void attack() { // Phương thức tấn công (bắn đạn)
        
        // Tạo một viên đạn mới loại RedEyeBullet tại vị trí của kẻ thù
        Bullet bullet = new RedEyeBullet(getPosX(), getPosY(), getGameWorld());
        
        // Thiết lập tốc độ X của đạn dựa trên hướng của kẻ thù
        if(getDirection() == LEFT_DIR) bullet.setSpeedX(-8); // Nếu hướng trái, đạn bay sang trái
        else bullet.setSpeedX(8); // Nếu hướng phải, đạn bay sang phải
        
        bullet.setTeamType(getTeamType()); // Đặt loại team của đạn (TEAM_ENEMY)
        // Thêm đạn vào danh sách quản lý của BulletManager
        getGameWorld().bulletManager.addObject(bullet); 
    
    }

    
    public void Update(){ // Cập nhật logic của kẻ thù
        super.Update(); // Gọi Update của lớp cha (xử lý di chuyển, va chạm map, bất tử,...)
        
        // Cập nhật Animation dựa trên hướng hiện tại của kẻ thù
        if(getDirection() == LEFT_DIR){
            backAnim.Update(System.nanoTime());
        }else{
            forwardAnim.Update(System.nanoTime());
        }
        
        // Kiểm tra thời gian bắn (10 * 10 triệu nano giây = 1 giây)
        if(System.nanoTime() - startTimeToShoot > 1000*10000000){
            attack(); // Thực hiện tấn công
            System.out.println("Red Eye attack"); // In thông báo debug
            startTimeToShoot = System.nanoTime(); // Cập nhật thời điểm bắn mới
        }
    }
    
    @Override
    public Rectangle getBoundForCollisionWithEnemy() { // Lấy vùng va chạm với các đối tượng khác (trừ map)
        // Lấy vùng va chạm với map làm cơ sở
        Rectangle rect = getBoundForCollisionWithMap(); 
        
        // Điều chỉnh vùng va chạm cho nhỏ hơn: dời sang phải 20 pixel
        rect.x += 20; 
        // Giảm chiều rộng đi 40 pixel (20 bên trái + 20 bên phải)
        rect.width -= 40; 
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) { // Phương thức vẽ kẻ thù
        // Chỉ vẽ nếu đối tượng nằm trong tầm nhìn của camera
        if(!isObjectOutOfCameraView()){ 
            // Kiểm tra trạng thái bất tử (NOBEHURT) và tạo hiệu ứng nhấp nháy
            if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1){
                // Nếu đang bất tử, cứ 10ms thì bỏ qua vẽ (tạo hiệu ứng nhấp nháy)
            }else{
                // Nếu hướng trái, vẽ animation hướng về phía sau
                if(getDirection() == LEFT_DIR){
                    backAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }else{
                    // Nếu hướng phải, vẽ animation hướng về phía trước
                    forwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }
            }
        }
      
    }
    
}