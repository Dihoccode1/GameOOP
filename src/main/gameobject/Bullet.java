package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import java.awt.Graphics2D;        // Import Graphics2D để vẽ

// Lớp trừu tượng Bullet (Đạn) kế thừa từ ParticularObject
// Lớp này định nghĩa hành vi cơ bản của một viên đạn, chủ yếu là di chuyển và va chạm/gây sát thương.
public abstract class Bullet extends ParticularObject {

    // Hàm khởi tạo
    public Bullet(float x, float y, float width, float height, float mass, int damage, GameWorldState gameWorld) {
            // Gọi hàm khởi tạo lớp cha (ParticularObject)
            // Đạn thường có 1 máu (blood = 1) vì sẽ biến mất sau khi va chạm
            super(x, y, width, height, mass, 1, gameWorld); 
            setDamage(damage); // Thiết lập sát thương của đạn
    }
    
    // Phương thức trừu tượng: Mọi loại đạn phải định nghĩa cách vẽ riêng
    public abstract void draw(Graphics2D g2d);

    @Override
    public void Update(){ // Cập nhật logic của đạn
        super.Update(); // Gọi Update của lớp cha (xử lý trạng thái, máu,...)
        
        // Cập nhật vị trí dựa trên tốc độ hiện tại
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        
        // Kiểm tra va chạm với đối tượng đối địch (kẻ thù/MegaMan)
        ParticularObject object = getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
        
        // Nếu có va chạm VÀ đối tượng đó đang ở trạng thái ALIVE
        if(object!=null && object.getState() == ALIVE){
            setBlood(0); // Đặt máu của đạn về 0 (đạn sẽ chết/biến mất ở frame tiếp theo)
            object.beHurt(getDamage()); // Gây sát thương lên đối tượng bị va chạm
            System.out.println("Bullet set behurt for enemy"); // Debug
        }
    }
    
}