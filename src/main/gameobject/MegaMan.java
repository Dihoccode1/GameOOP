package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import main.effect.Animation;      // Import lớp Animation để xử lý hiệu ứng hình ảnh
import main.effect.CacheDataLoader; // Import lớp để tải các tài nguyên (animation)
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.awt.Rectangle;         // Import Rectangle để xử lý va chạm

// Lớp MegaMan kế thừa từ lớp Human (Lớp cha của các nhân vật có hình dạng người)
public class MegaMan extends Human {

    public static final int RUNSPEED = 3; // Tốc độ chạy cố định của MegaMan (pixel/frame)
    
    // Khai báo các Animation cho các hành động và trạng thái khác nhau
    private Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim; // Chạy
    private Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim; // Đứng yên
    private Animation dickForwardAnim, dickBackAnim; // Cúi/Trườn
    private Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim; // Nhảy/Bay
    private Animation landingForwardAnim, landingBackAnim; // Tiếp đất
    
    private Animation climWallForward, climWallBack; // Leo tường (mặc dù logic leo tường không được sử dụng hết)
    
    private long lastShootingTime; // Thời điểm cuối cùng MegaMan bắn đạn
    private boolean isShooting = false; // Cờ (flag) cho biết MegaMan có đang trong hành động bắn hay không
    

    // Hàm khởi tạo
    public MegaMan(float x, float y, GameWorldState gameWorld) {
        // Gọi hàm khởi tạo lớp cha: x, y, rộng=70, cao=90, khối lượng=0.1f, máu=100, thế giới game
        super(x, y, 70, 90, 0.1f, 100, gameWorld);
          
        setTeamType(LEAGUE_TEAM); // Thiết lập team là đồng minh (LEAGUE_TEAM)

        // Thiết lập thời gian bất tử sau khi trúng đòn (2000ms = 2 giây)
        setTimeForNoBehurt(2000*1000000); 
        
        // --- Tải và thiết lập các Animation ---
        
        // Chạy
        runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackAnim.flipAllImage(); // Lật hình ảnh cho animation chạy ngược (chạy trái)  
        
        // Đứng yên
        idleForwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackAnim.flipAllImage();
        
        // Cúi
        dickForwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackAnim.flipAllImage();
        
        // Nhảy/Bay
        flyForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyForwardAnim.setIsRepeated(false); // Animation này chỉ chạy 1 lần
        flyBackAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyBackAnim.setIsRepeated(false);
        flyBackAnim.flipAllImage();
        
        // Tiếp đất
        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackAnim.flipAllImage();
        
        // Leo tường
        climWallBack = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climWallForward = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climWallForward.flipAllImage();
        
        // Bị trúng đòn
        behurtForwardAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim.flipAllImage();
        
        // Đứng yên và bắn
        idleShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim.flipAllImage();
        
        // Chạy và bắn
        runShootingForwarAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim.flipAllImage();
        
        // Bay và bắn
        flyShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackAnim.flipAllImage();
        
    }

    @Override
    public void Update() { // Cập nhật logic của MegaMan

        super.Update(); // Gọi Update của lớp cha (ParticularObject & Human)
        
        // Xử lý cờ (flag) isShooting:
        if(isShooting){
            // Nếu đã qua 500ms kể từ lần bắn cuối cùng, tắt trạng thái bắn
            if(System.nanoTime() - lastShootingTime > 500*1000000){
                isShooting = false;
            }
        }
        
        // Xử lý animation tiếp đất:
        if(getIsLanding()){
            landingBackAnim.Update(System.nanoTime()); // Cập nhật animation tiếp đất
            if(landingBackAnim.isLastFrame()) { // Nếu animation chạy hết
                setIsLanding(false); // Tắt cờ tiếp đất
                landingBackAnim.reset(); // Reset animation tiếp đất
                // Reset các animation chạy để đảm bảo chúng bắt đầu lại từ đầu
                runForwardAnim.reset(); 
                runBackAnim.reset();
            }
        }
        
    }

    @Override
    // Phương thức trả về vùng va chạm với kẻ thù
    public Rectangle getBoundForCollisionWithEnemy() {
        
        Rectangle rect = getBoundForCollisionWithMap(); // Lấy vùng va chạm cơ bản
        
        if(getIsDicking()){ // Nếu đang cúi
            // Điều chỉnh vùng va chạm nhỏ lại (MegaMan cúi)
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 20;
            rect.width = 44;
            rect.height = 65;
        }else{ // Đứng hoặc chạy/nhảy
            // Vùng va chạm lớn hơn
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 44;
            rect.height = 80;
        }
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) { // Vẽ MegaMan lên màn hình
        
        // Xử lý vẽ dựa trên trạng thái
        switch(getState()){
            
            case ALIVE:
            case NOBEHURT: // Trạng thái sống hoặc bất tử tạm thời
                // Hiệu ứng nhấp nháy khi bất tử (NOBEHURT)
                if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1)
                {
                    // Tạo khoảng trống (plash) để nhấp nháy
                }else{ 
                    
                    if(getIsLanding()){ // Nếu đang tiếp đất

                        if(getDirection() == RIGHT_DIR){ // Hướng phải
                            landingForwardAnim.setCurrentFrame(landingBackAnim.getCurrentFrame()); // Đồng bộ frame
                            landingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{ // Hướng trái
                            landingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else if(getIsJumping()){ // Nếu đang nhảy/bay

                        if(getDirection() == RIGHT_DIR){ // Hướng phải
                            flyForwardAnim.Update(System.nanoTime()); // Cập nhật animation nhảy/bay
                            if(isShooting){ // Nếu đang bắn
                                flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame()); // Đồng bộ frame
                                flyShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) + 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                flyForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }else{ // Hướng trái
                            flyBackAnim.Update(System.nanoTime());
                            if(isShooting){
                                flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
                                flyShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) - 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            flyBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }

                    }else if(getIsDicking()){ // Nếu đang cúi

                        if(getDirection() == RIGHT_DIR){ // Hướng phải
                            dickForwardAnim.Update(System.nanoTime());
                            dickForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{ // Hướng trái
                            dickBackAnim.Update(System.nanoTime());
                            dickBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else{ // Trạng thái Đứng hoặc Chạy
                        if(getSpeedX() > 0){ // Đang chạy phải
                            runForwardAnim.Update(System.nanoTime());
                            if(isShooting){ // Chạy và bắn
                                runShootingForwarAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
                                runShootingForwarAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                runForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0); // Bỏ qua frame đầu tiên khi chạy
                        }else if(getSpeedX() < 0){ // Đang chạy trái
                            runBackAnim.Update(System.nanoTime());
                            if(isShooting){ // Chạy và bắn
                                runShootingBackAnim.setCurrentFrame(runBackAnim.getCurrentFrame() - 1);
                                runShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                runBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(runBackAnim.getCurrentFrame() == 1) runBackAnim.setIgnoreFrame(0);
                        }else{ // Đứng yên
                            if(getDirection() == RIGHT_DIR){ // Đứng yên, hướng phải
                                if(isShooting){ // Đứng yên và bắn
                                    idleShootingForwardAnim.Update(System.nanoTime());
                                    idleShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{ // Đứng yên
                                    idleForwardAnim.Update(System.nanoTime());
                                    idleForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }else{ // Đứng yên, hướng trái
                                if(isShooting){
                                    idleShootingBackAnim.Update(System.nanoTime());
                                    idleShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{
                                    idleBackAnim.Update(System.nanoTime());
                                    idleBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }
                        } 
                    }
                }
                
                break;
            
            case BEHURT: // Trạng thái bị trúng đòn
                if(getDirection() == RIGHT_DIR){ // Hướng phải
                    behurtForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }else{ // Hướng trái
                    behurtBackAnim.setCurrentFrame(behurtForwardAnim.getCurrentFrame()); // Đồng bộ frame
                    behurtBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }
                break;
                
            case FEY: // Trạng thái sắp chết (không vẽ gì ở đây, để dành cho hiệu ứng)
                
                break;

        }
        
        //drawBoundForCollisionWithMap(g2); // Dùng để debug
        //drawBoundForCollisionWithEnemy(g2); // Dùng để debug
    }

    @Override
    public void run() { // Hành động chạy
        if(getDirection() == LEFT_DIR)
            setSpeedX(-RUNSPEED); // Chạy trái
        else setSpeedX(RUNSPEED); // Chạy phải
    }

    @Override
    public void jump() { // Hành động nhảy

        if(!getIsJumping()){ // Nếu chưa nhảy (đang đứng trên mặt đất)
            setIsJumping(true); // Đặt cờ nhảy
            setSpeedY(-5.0f); // Thiết lập tốc độ Y âm (bay lên)           
            flyBackAnim.reset(); // Reset animation bay/nhảy
            flyForwardAnim.reset();
        }
        // Xử lý logic bám/leo tường
        else{
            // Tạo vùng va chạm giả để kiểm tra tường bên phải
            Rectangle rectRightWall = getBoundForCollisionWithMap();
            rectRightWall.x += 1;
            // Tạo vùng va chạm giả để kiểm tra tường bên trái
            Rectangle rectLeftWall = getBoundForCollisionWithMap();
            rectLeftWall.x -= 1;
            
            // Nếu va chạm với tường phải VÀ đang di chuyển sang phải
            if(getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall)!=null && getSpeedX() > 0){
                setSpeedY(-5.0f); // Bật nhảy khỏi tường
                flyBackAnim.reset();
                flyForwardAnim.reset();
            }
            // Nếu va chạm với tường trái VÀ đang di chuyển sang trái
            else if(getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall)!=null && getSpeedX() < 0){
                setSpeedY(-5.0f); // Bật nhảy khỏi tường
                flyBackAnim.reset();
                flyForwardAnim.reset();
            }
                
        }
    }

    @Override
    public void dick() { // Hành động cúi/trườn
        if(!getIsJumping()) // Chỉ cúi khi không nhảy
            setIsDicking(true);
    }

    @Override
    public void standUp() { // Hành động đứng dậy
        setIsDicking(false); // Tắt cờ cúi
        // Reset các animation để chuyển về trạng thái đứng yên
        idleForwardAnim.reset();
        idleBackAnim.reset();
        dickForwardAnim.reset();
        dickBackAnim.reset();
    }

    @Override
    public void stopRun() { // Hành động dừng chạy
        setSpeedX(0); // Đặt tốc độ X về 0
        // Reset animation chạy
        runForwardAnim.reset();
        runBackAnim.reset();
        // Bỏ qua việc bỏ qua frame 0 (để chuẩn bị cho lần chạy tiếp theo)
        runForwardAnim.unIgnoreFrame(0); 
        runBackAnim.unIgnoreFrame(0);
    }

    @Override
    public void attack() { // Hành động tấn công (bắn)
    
        // Chỉ cho phép bắn nếu không đang bắn và không đang cúi
        if(!isShooting && !getIsDicking()){
            
            // Tạo đối tượng đạn mới (BlueFire)
            Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
            
            if(getDirection() == LEFT_DIR) { // Bắn sang trái
                bullet.setSpeedX(-10); // Tốc độ đạn
                bullet.setPosX(bullet.getPosX() - 40); // Điều chỉnh vị trí xuất phát (trước MegaMan)
                if(getSpeedX() != 0 && getSpeedY() == 0){ // Nếu đang chạy trên mặt đất
                    bullet.setPosX(bullet.getPosX() - 10); // Điều chỉnh thêm
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }else { // Bắn sang phải
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if(getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            
            if(getIsJumping()) // Nếu đang nhảy, hạ thấp vị trí đạn một chút
                bullet.setPosY(bullet.getPosY() - 20);
            
            bullet.setTeamType(getTeamType()); // Đặt team cho đạn (đồng minh)
            getGameWorld().bulletManager.addObject(bullet); // Thêm đạn vào BulletManager
            
            lastShootingTime = System.nanoTime(); // Cập nhật thời điểm bắn
            isShooting = true; // Đặt cờ bắn
            
        }
    
    }
    
    @Override
    public void hurtingCallback(){ // Xử lý logic phụ sau khi bị trúng đòn
        // Ví dụ: thêm âm thanh bị thương, tạo hiệu ứng giật lùi, v.v.
        System.out.println("Call back hurting");
        
    }

}