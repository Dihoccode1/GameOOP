package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import main.effect.CacheDataLoader; // Import lớp để tải các tài nguyên (animation)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp RocketBullet kế thừa từ lớp Bullet
public class RocketBullet extends Bullet{
    
    // Khai báo các đối tượng Animation cho đạn bay về phía trước (forward)
    private Animation forwardBulletAnimUp, forwardBulletAnimDown, forwardBulletAnim;
    // Khai báo các đối tượng Animation cho đạn bay về phía sau (back)
    private Animation backBulletAnimUp, backBulletAnimDown, backBulletAnim;

    private long startTimeForChangeSpeedY; // Biến lưu trữ thời điểm bắt đầu tính thay đổi tốc độ Y
    
    public RocketBullet(float x, float y, GameWorldState gameWorld) {
        
            // Gọi hàm khởi tạo của lớp cha (Bullet/ParticularObject)
            // x, y, rộng=30, cao=30, khối lượng=1.0f, sát thương=10, thế giới game
            super(x, y, 30, 30, 1.0f, 10, gameWorld);
            
            // 1. Lấy Animation cho đạn bay ngược (ví dụ: đạn bay sang trái)
            backBulletAnimUp = CacheDataLoader.getInstance().getAnimation("rocketUp");     // Bay ngược, hướng lên
            backBulletAnimDown = CacheDataLoader.getInstance().getAnimation("rocketDown"); // Bay ngược, hướng xuống
            backBulletAnim = CacheDataLoader.getInstance().getAnimation("rocket");         // Bay ngược, ngang
            
            // 2. Lấy Animation cho đạn bay tiến (ví dụ: đạn bay sang phải)
            forwardBulletAnimUp = CacheDataLoader.getInstance().getAnimation("rocketUp");
            forwardBulletAnimUp.flipAllImage(); // Lật tất cả hình ảnh để chuyển từ trái sang phải
            forwardBulletAnimDown = CacheDataLoader.getInstance().getAnimation("rocketDown");
            forwardBulletAnimDown.flipAllImage();
            forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("rocket");
            forwardBulletAnim.flipAllImage();
            
            // Khởi tạo thời gian bắt đầu
            startTimeForChangeSpeedY = System.nanoTime();

    }
 
    @Override
    // Trả về vùng giới hạn va chạm với kẻ thù
    public Rectangle getBoundForCollisionWithEnemy() {
            // Vùng va chạm với kẻ thù lấy bằng vùng va chạm với map (đơn giản hóa)
            return getBoundForCollisionWithMap(); 
    }

    @Override
    public void draw(Graphics2D g2) { // Phương thức vẽ đạn
        
        // Kiểm tra hướng bay X (lớn hơn 0 là bay sang phải)
        if(getSpeedX() > 0){  
            // Kiểm tra hướng bay Y (lớn hơn 0 là bay xuống)
            if(getSpeedY() > 0){ 
                // Vẽ animation bay xuống (hướng phải)
                forwardBulletAnimDown.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
            // Kiểm tra hướng bay Y (nhỏ hơn 0 là bay lên)
            }else if(getSpeedY() < 0){ 
                // Vẽ animation bay lên (hướng phải)
                forwardBulletAnimUp.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
            }else
                // Vẽ animation bay ngang (hướng phải)
                forwardBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }else{ // Đạn bay sang trái (hoặc đứng yên X)
            // Kiểm tra hướng bay Y (lớn hơn 0 là bay xuống)
            if(getSpeedY() > 0){
                // Vẽ animation bay xuống (hướng trái)
                backBulletAnimDown.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
            // Kiểm tra hướng bay Y (nhỏ hơn 0 là bay lên)
            }else if(getSpeedY() < 0){
                // Vẽ animation bay lên (hướng trái)
                backBulletAnimUp.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
            }else
                // Vẽ animation bay ngang (hướng trái)
                backBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }
        //drawBoundForCollisionWithEnemy(g2); // Dòng dùng để debug, vẽ khung va chạm
    }

    // Phương thức thay đổi ngẫu nhiên tốc độ Y (tạo quỹ đạo ngoằn ngoèo)
    private void changeSpeedY(){
        // Sử dụng thời gian hệ thống % 3 để tạo ra 3 trạng thái ngẫu nhiên (lên, xuống, ngang)
        if(System.currentTimeMillis() % 3 == 0){
            // Đặt tốc độ Y bằng tốc độ X (bay chéo lên/xuống)
            setSpeedY(getSpeedX());
        }else if(System.currentTimeMillis() % 3 == 1){
            // Đặt tốc độ Y bằng âm tốc độ X (bay chéo ngược lại)
            setSpeedY(-getSpeedX());
        }else {
            // Đặt tốc độ Y bằng 0 (bay ngang)
            setSpeedY(0);
            
        }
    }
    
    @Override
    public void Update() { // Cập nhật logic của đạn
            
        super.Update(); // Gọi phương thức Update của lớp cha (xử lý di chuyển, va chạm map cơ bản)
        
        // Kiểm tra xem đã đủ thời gian để thay đổi tốc độ Y chưa (500ms)
        if(System.nanoTime() - startTimeForChangeSpeedY > 500*1000000){
            startTimeForChangeSpeedY = System.nanoTime(); // Cập nhật thời điểm bắt đầu mới
            changeSpeedY(); // Thay đổi tốc độ Y
        }
    }

    @Override
    public void attack() {} // Đạn không có hành động tấn công riêng biệt
}