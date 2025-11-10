package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import java.awt.Color;             // Import Color để vẽ màu
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp trừu tượng ParticularObject (Đối tượng Đặc biệt/Động) kế thừa từ GameObject
public abstract class ParticularObject extends GameObject {

    // --- HẰNG SỐ TEAM (Đội) ---
    public static final int LEAGUE_TEAM = 1; // Team của người chơi (đồng minh)
    public static final int ENEMY_TEAM = 2;  // Team của kẻ thù
    
    // --- HẰNG SỐ HƯỚNG ---
    public static final int LEFT_DIR = 0;  // Hướng Trái
    public static final int RIGHT_DIR = 1; // Hướng Phải

    // --- HẰNG SỐ TRẠNG THÁI ---
    public static final int ALIVE = 0;    // Đang sống, hoạt động bình thường
    public static final int BEHURT = 1;   // Bị trúng đòn (đang chịu sát thương)
    public static final int FEY = 2;      // Đang trong quá trình chết (chuẩn bị chuyển sang DEATH)
    public static final int DEATH = 3;    // Đã chết (sẽ bị xóa khỏi game)
    public static final int NOBEHURT = 4; // Bất tử tạm thời sau khi bị trúng đòn hoặc hồi sinh
    
    private int state = ALIVE; // Trạng thái hiện tại của đối tượng, mặc định là ALIVE
    
    // --- THUỘC TÍNH VẬT LÝ VÀ CHỈ SỐ ---
    private float width;  // Chiều rộng
    private float height; // Chiều cao
    private float mass;   // Khối lượng (ảnh hưởng đến trọng lực)
    private float speedX; // Tốc độ di chuyển theo trục X
    private float speedY; // Tốc độ di chuyển theo trục Y
    private int blood;    // Máu hiện tại
    
    private int damage; // Sát thương gây ra khi va chạm/tấn công
    
    private int direction; // Hướng nhìn hiện tại (LEFT_DIR hoặc RIGHT_DIR)
    
    // Animation khi đối tượng bị trúng đòn
    protected Animation behurtForwardAnim, behurtBackAnim; 
    
    private int teamType; // Loại team (LEAGUE_TEAM hoặc ENEMY_TEAM)
    
    private long startTimeNoBeHurt; // Thời điểm bắt đầu trạng thái bất tử (NOBEHURT)
    private long timeForNoBeHurt;   // Thời gian bất tử được thiết lập
    
    // Hàm khởi tạo
    public ParticularObject(float x, float y, float width, float height, float mass, int blood, GameWorldState gameWorld){

        // Vị trí (x, y) là tọa độ trung tâm của đối tượng
        super(x, y, gameWorld); // Gọi hàm khởi tạo lớp cha (GameObject)
        
        // Thiết lập các thuộc tính ban đầu
        setWidth(width);
        setHeight(height);
        setMass(mass);
        setBlood(blood);
        
        direction = RIGHT_DIR; // Mặc định hướng ban đầu là phải

    }
    
    // --- GETTER & SETTER ---
    
    public void setTimeForNoBehurt(long time){
        timeForNoBeHurt = time;
    }
    
    public long getTimeForNoBeHurt(){
        return timeForNoBeHurt;
    }
    
    public void setState(int state){
        this.state = state;
    }
    
    public int getState(){
        return state;
    }
    
    public void setDamage(int damage){
            this.damage = damage;
    }

    public int getDamage(){
            return damage;
    }

    
    public void setTeamType(int team){
        teamType = team;
    }
    
    public int getTeamType(){
        return teamType;
    }
    
    public void setMass(float mass){
        this.mass = mass;
    }

    public float getMass(){
            return mass;
    }

    public void setSpeedX(float speedX){
        this.speedX = speedX;
    }

    public float getSpeedX(){
        return speedX;
    }

    public void setSpeedY(float speedY){
        this.speedY = speedY;
    }

    public float getSpeedY(){
        return speedY;
    }

    public void setBlood(int blood){
        // Đảm bảo máu không bao giờ âm
        if(blood>=0)
                this.blood = blood;
        else this.blood = 0;
    }

    public int getBlood(){
        return blood;
    }

    public void setWidth(float width){
        this.width = width;
    }

    public float getWidth(){
        return width;
    }

    public void setHeight(float height){
        this.height = height;
    }

    public float getHeight(){
        return height;
    }
    
    public void setDirection(int dir){
        direction = dir;
    }
    
    public int getDirection(){
        return direction;
    }
    
    // Phương thức trừu tượng: Mọi đối tượng động phải định nghĩa hành động tấn công
    public abstract void attack(); 
    
    
    // Phương thức kiểm tra xem đối tượng có nằm ngoài tầm nhìn của camera không (tối ưu hóa)
    public boolean isObjectOutOfCameraView(){
        // Kiểm tra nếu tọa độ X hoặc Y của đối tượng (trừ đi vị trí camera) vượt quá kích thước màn hình
        if(getPosX() - getGameWorld().camera.getPosX() > getGameWorld().camera.getWidthView() ||
                getPosX() - getGameWorld().camera.getPosX() < -50 // Thêm khoảng đệm -50px
            ||getPosY() - getGameWorld().camera.getPosY() > getGameWorld().camera.getHeightView()
                // Thêm khoảng đệm -50px
                    ||getPosY() - getGameWorld().camera.getPosY() < -50) 
            return true; // Nằm ngoài
        else return false; // Nằm trong
    }
    
    // Phương thức trả về vùng va chạm với bản đồ (dùng cho vật lý/di chuyển)
    public Rectangle getBoundForCollisionWithMap(){
        Rectangle bound = new Rectangle();
        // Tính toán vị trí góc trên trái từ vị trí trung tâm (posX, posY)
        bound.x = (int) (getPosX() - (getWidth()/2)); 
        bound.y = (int) (getPosY() - (getHeight()/2));
        bound.width = (int) getWidth();
        bound.height = (int) getHeight();
        return bound;
    }

    // Phương thức xử lý khi đối tượng bị trúng đòn
    public void beHurt(int damgeEat){
        setBlood(getBlood() - damgeEat); // Giảm máu
        state = BEHURT; // Chuyển trạng thái sang Bị trúng đòn
        hurtingCallback(); // Gọi hàm callback sau khi bị trúng đòn (để các lớp con xử lý thêm)
    }

    @Override
    public void Update(){ // Cập nhật logic của đối tượng
        
        // Xử lý logic dựa trên trạng thái hiện tại
        switch(state){
            case ALIVE: // Trạng thái sống bình thường
                
                // Kiểm tra va chạm với các đối tượng thuộc team đối địch (kẻ thù/đạn)
                ParticularObject object = getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
                if(object!=null){ // Nếu có va chạm với đối tượng đối địch
                    
                    if(object.getDamage() > 0){ // Nếu đối tượng đối địch gây sát thương
                        
                        System.out.println("eat damage.... from collision with enemy........ "+object.getDamage()); // Debug
                        beHurt(object.getDamage()); // Bị trúng đòn và giảm máu
                    }
                    
                }
                
                break;
                
            case BEHURT: // Trạng thái bị trúng đòn
                
                if(behurtBackAnim == null){ // Nếu không có animation khi bị trúng đòn
                    state = NOBEHURT; // Chuyển ngay sang trạng thái bất tử
                    startTimeNoBeHurt = System.nanoTime(); // Bắt đầu tính thời gian bất tử
                    if(getBlood() == 0) // Nếu hết máu
                        state = FEY; // Chuyển sang trạng thái chết
                    
                } else { // Nếu có animation khi bị trúng đòn
                    behurtForwardAnim.Update(System.nanoTime()); // Cập nhật animation
                    if(behurtForwardAnim.isLastFrame()){ // Nếu animation đã chạy hết
                        behurtForwardAnim.reset(); // Reset animation
                        state = NOBEHURT; // Chuyển sang trạng thái bất tử
                        if(getBlood() == 0) // Nếu hết máu
                            state = FEY; // Chuyển sang trạng thái chết
                        startTimeNoBeHurt = System.nanoTime(); // Bắt đầu tính thời gian bất tử
                    }
                }
                
                break;
                
            case FEY: // Trạng thái chết (quá trình)
                
                state = DEATH; // Chuyển ngay sang trạng thái đã chết
                
                break;
            
            case DEATH: // Trạng thái đã chết
                
                // (Sẽ được ParticularObjectManager xóa khỏi danh sách)
                
                break;
                
            case NOBEHURT: // Trạng thái bất tử tạm thời
                System.out.println("state = nobehurt"); // Debug
                // Kiểm tra xem đã hết thời gian bất tử chưa
                if(System.nanoTime() - startTimeNoBeHurt > timeForNoBeHurt)
                    state = ALIVE; // Quay lại trạng thái sống bình thường
                break;
        }
        
    }

    // Phương thức vẽ khung va chạm với bản đồ (dùng để debug)
    public void drawBoundForCollisionWithMap(Graphics2D g2){
        Rectangle rect = getBoundForCollisionWithMap();
        g2.setColor(Color.BLUE);
        // Vẽ khung, trừ đi vị trí camera để cuộn map
        g2.drawRect(rect.x - (int) getGameWorld().camera.getPosX(), rect.y - (int) getGameWorld().camera.getPosY(), rect.width, rect.height);
    }

    // Phương thức vẽ khung va chạm với kẻ thù (dùng để debug)
    public void drawBoundForCollisionWithEnemy(Graphics2D g2){
        Rectangle rect = getBoundForCollisionWithEnemy();
        g2.setColor(Color.RED);
        // Vẽ khung, trừ đi vị trí camera để cuộn map
        g2.drawRect(rect.x - (int) getGameWorld().camera.getPosX(), rect.y - (int) getGameWorld().camera.getPosY(), rect.width, rect.height);
    }

    // Phương thức trừu tượng: Mọi đối tượng động phải định nghĩa vùng va chạm với kẻ thù
    public abstract Rectangle getBoundForCollisionWithEnemy();

    // Phương thức trừu tượng: Mọi đối tượng động phải định nghĩa cách vẽ
    public abstract void draw(Graphics2D g2);
    
    // Phương thức callback (trống), cho phép các lớp con ghi đè để thêm logic khi bị trúng đòn
    public void hurtingCallback(){}; 
    
}