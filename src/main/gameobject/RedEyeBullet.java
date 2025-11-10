package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import main.effect.CacheDataLoader; // Import lớp để tải các tài nguyên (animation)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp RedEyeBullet (Đạn Quỷ Mắt Đỏ) kế thừa từ lớp Bullet
public class RedEyeBullet extends Bullet{
    
    private Animation forwardBulletAnim, backBulletAnim; // Animation cho đạn bay tới và bay lùi
    
    // Hàm khởi tạo
    public RedEyeBullet(float x, float y, GameWorldState gameWorld) {
            // Gọi hàm khởi tạo lớp cha: x, y, rộng=30, cao=30, khối lượng=1.0f, sát thương=10, thế giới game
            super(x, y, 30, 30, 1.0f, 10, gameWorld);
            
            // Lấy animation "redeyebullet"
            forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
            backBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
            backBulletAnim.flipAllImage(); // Lật hình ảnh cho animation bay lùi (ngược hướng forward)
    }

    
    
    // Phương thức trả về vùng va chạm với kẻ thù
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
            // Vùng va chạm với kẻ thù được lấy bằng vùng va chạm với map (đơn giản hóa)
            return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) { // Phương thức vẽ đạn
            
        // Kiểm tra hướng di chuyển X
        if(getSpeedX() > 0){ // Nếu đạn bay sang phải (forward) 
            forwardBulletAnim.Update(System.nanoTime()); // Cập nhật trạng thái animation
            // Vẽ animation bay tới (đã được cập nhật) tại vị trí tương đối với camera
            forwardBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }else{ // Nếu đạn bay sang trái (back) hoặc đứng yên
            backBulletAnim.Update(System.nanoTime()); // Cập nhật trạng thái animation
            // Vẽ animation bay lùi
            backBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }
        //drawBoundForCollisionWithEnemy(g2); // Dòng dùng để debug (vẽ khung va chạm)
    }

    @Override
    public void Update() { // Cập nhật logic của đạn
            
        super.Update(); // Gọi phương thức Update của lớp cha (xử lý di chuyển và va chạm map)
    }

    @Override
    public void attack() {} // Đạn không có hành động tấn công riêng biệt
}