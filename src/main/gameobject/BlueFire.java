package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import main.effect.CacheDataLoader; // Import lớp để tải các tài nguyên (animation)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp BlueFire (Đạn Lửa Xanh) kế thừa từ lớp Bullet
// Đây là đạn cơ bản của Mega Man, có animation nạp/phóng.
public class BlueFire extends Bullet{
    
    private Animation forwardBulletAnim, backBulletAnim; // Animation cho đạn bay tới và bay lùi
    
    // Hàm khởi tạo
    public BlueFire(float x, float y, GameWorldState gameWorld) {
        // Gọi hàm khởi tạo lớp cha: x, y, rộng=60, cao=30, khối lượng=1.0f, sát thương=10, thế giới game
        super(x, y, 60, 30, 1.0f, 10, gameWorld);
        
        // Tải animation "bluefire"
        forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
        backBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
        backBulletAnim.flipAllImage(); // Lật hình ảnh cho animation bay lùi
    }

    
    
    // Phương thức trả về vùng va chạm với kẻ thù
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        // Vùng va chạm được lấy bằng vùng va chạm với map (đơn giản hóa)
        return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) { // Phương thức vẽ đạn
            
        // Kiểm tra hướng di chuyển (getSpeedX() > 0 là bay sang phải)
        if(getSpeedX() > 0){
            // --- Logic nạp/phóng animation (chỉ chạy 3 frame đầu tiên một lần) ---
            // Nếu animation chưa bị bỏ qua frame 0 VÀ frame hiện tại là frame 3 (frame cuối của quá trình nạp)
            if(!forwardBulletAnim.isIgnoreFrame(0) && forwardBulletAnim.getCurrentFrame() == 3){
                // Bắt đầu bỏ qua 3 frame đầu tiên (0, 1, 2) để chỉ hiển thị frame cuối cùng (bay)
                forwardBulletAnim.setIgnoreFrame(0);
                forwardBulletAnim.setIgnoreFrame(1);
                forwardBulletAnim.setIgnoreFrame(2);
            }
                
            forwardBulletAnim.Update(System.nanoTime()); // Cập nhật trạng thái animation
            // Vẽ animation
            forwardBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }else{ // Đạn bay sang trái
            // --- Logic nạp/phóng animation (tương tự hướng phải) ---
            if(!backBulletAnim.isIgnoreFrame(0) && backBulletAnim.getCurrentFrame() == 3){
                backBulletAnim.setIgnoreFrame(0);
                backBulletAnim.setIgnoreFrame(1);
                backBulletAnim.setIgnoreFrame(2);
            }
            backBulletAnim.Update(System.nanoTime());
            backBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }
    }

    @Override
    public void Update() { // Cập nhật logic của đạn
          
        // Chỉ cập nhật vị trí đạn khi animation nạp/phóng đã hoàn tất (frame 0 bị bỏ qua)
        if(forwardBulletAnim.isIgnoreFrame(0) || backBulletAnim.isIgnoreFrame(0))
            setPosX(getPosX() + getSpeedX()); // Cập nhật vị trí X
            
        // Kiểm tra va chạm với đối tượng đối địch
        ParticularObject object = getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
        
        // Nếu có va chạm VÀ đối tượng đó đang ở trạng thái ALIVE
        if(object!=null && object.getState() == ALIVE){
            setBlood(0); // Đạn tự hủy
            object.setBlood(object.getBlood() - getDamage()); // Gây sát thương lên kẻ thù
            object.setState(BEHURT); // Đặt kẻ thù vào trạng thái Bị trúng đòn
            System.out.println("Bullet set behurt for enemy"); // Debug
        }
    }

    @Override
    public void attack() {} // Đạn không có hành động tấn công riêng biệt
}