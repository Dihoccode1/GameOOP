package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp trừu tượng Human (Con người) kế thừa từ ParticularObject
// Lớp này định nghĩa các thuộc tính và hành động vật lý cơ bản của một nhân vật có hình dạng người.
public abstract class Human extends ParticularObject{

    private boolean isJumping; // Cờ (flag) cho biết nhân vật có đang nhảy không
    private boolean isDicking; // Cờ (flag) cho biết nhân vật có đang cúi không
    
    private boolean isLanding; // Cờ (flag) cho biết nhân vật có đang trong quá trình tiếp đất không

    // Hàm khởi tạo
    public Human(float x, float y, float width, float height, float mass, int blood, GameWorldState gameWorld) {
        // Gọi hàm khởi tạo lớp cha (ParticularObject)
        super(x, y, width, height, mass, blood, gameWorld);
        setState(ALIVE); // Đặt trạng thái ban đầu là ALIVE (sống)
    }

    // --- CÁC PHƯƠNG THỨC HÀNH ĐỘNG (ABSTRACT) ---
    // Bắt buộc các lớp con (ví dụ: MegaMan) phải định nghĩa chi tiết
    public abstract void run();
    
    public abstract void jump();
    
    public abstract void dick(); // Cúi/trườn
    
    public abstract void standUp(); // Đứng dậy
    
    public abstract void stopRun(); // Dừng chạy

    // --- GETTER & SETTER CHO CÁC CỜ TRẠNG THÁI ---
    public boolean getIsJumping() {
        return isJumping;
    }
    
    public void setIsLanding(boolean b){
        isLanding = b;
    }
    
    public boolean getIsLanding(){
        return isLanding;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean getIsDicking() {
        return isDicking;
    }

    public void setIsDicking(boolean isDicking) {
        this.isDicking = isDicking;
    }
    
    // --- PHƯƠNG THỨC CẬP NHẬT LOGIC (VẬT LÝ VÀ VA CHẠM) ---
    @Override
    public void Update(){
        
        super.Update(); // Gọi Update của lớp cha (ParticularObject) để xử lý máu, sát thương, trạng thái
        
        // Chỉ xử lý vật lý và va chạm khi nhân vật còn sống hoặc đang bất tử tạm thời
        if(getState() == ALIVE || getState() == NOBEHURT){
        
            if(!isLanding){ // Không xử lý va chạm nếu đang trong animation tiếp đất

                // 1. Xử lý di chuyển theo trục X
                setPosX(getPosX() + getSpeedX());

                // Kiểm tra va chạm với TƯỜNG TRÁI
                if(getDirection() == LEFT_DIR && 
                        getGameWorld().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap())!=null){

                    // Nếu va chạm, điều chỉnh vị trí X để nhân vật chạm sát vào tường
                    Rectangle rectLeftWall = getGameWorld().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap());
                    // Đặt vị trí X mới = (Tường phải + 1/2 Chiều rộng nhân vật)
                    setPosX(rectLeftWall.x + rectLeftWall.width + getWidth()/2);

                }
                // Kiểm tra va chạm với TƯỜNG PHẢI
                if(getDirection() == RIGHT_DIR && 
                        getGameWorld().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap())!=null){

                    // Nếu va chạm, điều chỉnh vị trí X để nhân vật chạm sát vào tường
                    Rectangle rectRightWall = getGameWorld().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap());
                    // Đặt vị trí X mới = (Tường trái - 1/2 Chiều rộng nhân vật)
                    setPosX(rectRightWall.x - getWidth()/2);

                }

                // 2. Xử lý di chuyển theo trục Y (Trọng lực và Nhảy)
                
                // Tạo vùng va chạm giả định trong frame tiếp theo
                Rectangle boundForCollisionWithMapFuture = getBoundForCollisionWithMap();
                // Tăng vị trí Y của vùng va chạm giả định (kiểm tra va chạm ở vị trí mới)
                // Nếu speedY=0 (đứng yên), kiểm tra ở vị trí y + 2 pixel để đảm bảo luôn kiểm tra mặt đất
                boundForCollisionWithMapFuture.y += (getSpeedY()!=0?getSpeedY(): 2); 
                
                // Kiểm tra va chạm với MẶT ĐẤT
                Rectangle rectLand = getGameWorld().physicalMap.haveCollisionWithLand(boundForCollisionWithMapFuture);
                
                // Kiểm tra va chạm với TRẦN NHÀ
                Rectangle rectTop = getGameWorld().physicalMap.haveCollisionWithTop(boundForCollisionWithMapFuture);
                
                // Xử lý va chạm TRẦN NHÀ
                if(rectTop !=null){
                    
                    setSpeedY(0); // Dừng di chuyển Y
                    // Đẩy nhân vật xuống dưới trần nhà (rectTop.y + kích thước tile + 1/2 chiều cao nhân vật)
                    setPosY(rectTop.y + getGameWorld().physicalMap.getTileSize() + getHeight()/2);
                    
                // Xử lý va chạm MẶT ĐẤT
                }else if(rectLand != null){ 
                    setIsJumping(false); // Dừng trạng thái nhảy
                    if(getSpeedY() > 0) setIsLanding(true); // Nếu đang rơi xuống (speedY > 0), bắt đầu trạng thái tiếp đất
                    setSpeedY(0); // Dừng rơi
                    // Đặt vị trí Y mới = (Mặt đất - 1/2 chiều cao nhân vật - 1 pixel)
                    setPosY(rectLand.y - getHeight()/2 - 1); 
                }else{ 
                    // Nếu không va chạm với đất, nhân vật đang ở trên không
                    setIsJumping(true); // Bật cờ nhảy (đang trên không)
                    // Áp dụng trọng lực: Tốc độ Y = Tốc độ Y cũ + Khối lượng (Mass)
                    setSpeedY(getSpeedY() + getMass()); 
                    // Cập nhật vị trí Y dựa trên tốc độ Y mới
                    setPosY(getPosY() + getSpeedY()); 
                }
            }
        }
    }
    
}