package main.state; // Gói chứa các lớp trạng thái của game

import main.effect.CacheDataLoader; // Import lớp để tải tài nguyên (hình ảnh)
import main.gameobject.BackgroundMap; // Import lớp quản lý bản đồ nền (ảnh)
import main.gameobject.BulletManager; // Import lớp quản lý tất cả đạn
import main.gameobject.Camera;         // Import lớp Camera, quản lý góc nhìn của màn hình
import main.gameobject.MegaMan;        // Import lớp nhân vật chính MegaMan
import main.gameobject.ParticularObject; // Import lớp cha của các đối tượng động (nhân vật, kẻ thù)
import main.gameobject.ParticularObjectManager; // Import lớp quản lý các đối tượng động
import main.gameobject.PhysicalMap;    // Import lớp quản lý bản đồ vật lý (va chạm)
import main.gameobject.RedEyeDevil;    // Import lớp kẻ thù đơn giản
import main.userinterface.GameFrame;   // Import GameFrame để lấy kích thước màn hình
import main.userinterface.GamePanel;   // Import GamePanel
import java.awt.Color;                 // Import Color để vẽ màu
import java.awt.Graphics2D;            // Import Graphics2D để vẽ đồ họa
import java.awt.event.KeyEvent;        // Import KeyEvent để xử lý phím
import java.awt.image.BufferedImage;   // Import BufferedImage để tạo hình ảnh bộ đệm
// XÓA: import main.userinterface.MenuState; 


public class GameWorldState extends State { // Lớp GameWorldState, kế thừa từ lớp State
    
    private BufferedImage bufferedImage; // Hình ảnh bộ đệm (back buffer) để vẽ game lên đó trước khi hiển thị
    private int lastState; // Lưu trữ trạng thái trước đó (dùng cho Pause/Resume)

    public ParticularObjectManager particularObjectManager; // Đối tượng quản lý nhân vật và kẻ thù
    public BulletManager bulletManager; // Đối tượng quản lý đạn

    public MegaMan megaMan; // Nhân vật chính
    
    public PhysicalMap physicalMap; // Bản đồ vật lý (quy định mặt đất, tường, v.v.)
    public BackgroundMap backgroundMap; // Bản đồ nền (hình ảnh)
    public Camera camera; // Góc nhìn của người chơi

    // Khai báo các hằng số đại diện cho các trạng thái game
    public static final int INIT_GAME = 0; // Khởi tạo game (màn hình Press Enter)
    public static final int GAMEPLAY = 2;  // Đang chơi
    public static final int GAMEOVER = 3;  // Game Over
    public static final int GAMEWIN = 4;   // Thắng game
    public static final int PAUSEGAME = 5; // Tạm dừng game
    
    public int state = INIT_GAME; // Trạng thái hiện tại của game, ban đầu là INIT_GAME
    public int previousState = state; // Trạng thái trước đó
    
    private int numberOfLife = 3; // Số mạng còn lại của MegaMan
    
    
    public GameWorldState(GamePanel gamePanel){
            super(gamePanel); // Gọi hàm khởi tạo của lớp cha (State)
      
        // Khởi tạo BufferedImage với kích thước cửa sổ game
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        // Khởi tạo các thành phần game
        megaMan = new MegaMan(400, 400, this);
        physicalMap = new PhysicalMap(0, 0, this);
        backgroundMap = new BackgroundMap(0, 0, this);
        camera = new Camera(0, 50, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, this);
        bulletManager = new BulletManager(this);
        
        // Khởi tạo và thêm MegaMan vào danh sách các đối tượng động
        particularObjectManager = new ParticularObjectManager(this);
        particularObjectManager.addObject(megaMan);
        
        initEnemies(); // Gọi phương thức khởi tạo kẻ thù
    }
    
    private void initEnemies(){
        // CHỈ GIỮ LẠI CÁC KẺ THÙ ĐƠN GIẢN
        
        // Tạo và thêm kẻ thù RedEyeDevil vào các vị trí khác nhau
        ParticularObject redeye = new RedEyeDevil(1250, 410, this);
        redeye.setDirection(ParticularObject.LEFT_DIR); // Hướng ban đầu
        redeye.setTeamType(ParticularObject.ENEMY_TEAM); // Loại team (Kẻ thù)
        particularObjectManager.addObject(redeye);
        
        ParticularObject redeye2 = new RedEyeDevil(2500, 500, this);
        redeye2.setDirection(ParticularObject.LEFT_DIR);
        redeye2.setTeamType(ParticularObject.ENEMY_TEAM);
        particularObjectManager.addObject(redeye2);
        
        ParticularObject redeye3 = new RedEyeDevil(3450, 500, this);
        redeye3.setDirection(ParticularObject.LEFT_DIR);
        redeye3.setTeamType(ParticularObject.ENEMY_TEAM);
        particularObjectManager.addObject(redeye3);
        
        ParticularObject redeye4 = new RedEyeDevil(500, 1190, this);
        redeye4.setDirection(ParticularObject.RIGHT_DIR);
        redeye4.setTeamType(ParticularObject.ENEMY_TEAM);
        particularObjectManager.addObject(redeye4);
    }

    // Phương thức chuyển đổi trạng thái game
    public void switchState(int state){
        previousState = this.state; // Lưu trạng thái hiện tại làm trạng thái trước đó
        this.state = state; // Cập nhật trạng thái mới
    }
    
    // Phương thức hỗ trợ vẽ chuỗi ký tự xuống dòng
    private void drawString(Graphics2D g2, String text, int x, int y){
        for(String str : text.split("\n"))
            g2.drawString(str, x, y+=g2.getFontMetrics().getHeight());
    }
    
    @Override
    public void Update(){ // Cập nhật logic game, chạy trong vòng lặp game
        
        switch(state){
            case INIT_GAME: // Trạng thái chờ khởi động
                // Không làm gì, chờ người dùng nhấn Enter
                break;
            
            case GAMEPLAY: // Trạng thái đang chơi
                particularObjectManager.UpdateObjects(); // Cập nhật vị trí, hành vi của nhân vật/kẻ thù
                bulletManager.UpdateObjects(); // Cập nhật vị trí, va chạm của đạn
        
                physicalMap.Update(); // Cập nhật bản đồ vật lý (ít thay đổi, có thể xử lý việc cuộn map)
                camera.Update(); // Cập nhật vị trí Camera theo MegaMan
                
                // Xử lý MegaMan chết
                if(megaMan.getState() == ParticularObject.DEATH){
                    numberOfLife --; // Giảm số mạng
                    if(numberOfLife >= 0){ // Nếu còn mạng
                        megaMan.setBlood(100); // Hồi đầy máu
                        megaMan.setPosY(megaMan.getPosY() - 50); // Đặt lại vị trí hồi sinh
                        megaMan.setState(ParticularObject.NOBEHURT); // Chuyển sang trạng thái bất tử tạm thời
                        particularObjectManager.addObject(megaMan); // Thêm MegaMan lại vào danh sách (nếu bị xóa khi chết)
                    }else{ // Hết mạng
                        switchState(GAMEOVER); // Chuyển sang trạng thái Game Over
                    }
                }
                
                break;
            case GAMEOVER:
                // Không làm gì, chờ người dùng nhấn Enter để chơi lại
                break;
            case GAMEWIN:
                // Không làm gì, chờ người dùng nhấn Enter để chơi lại
                break;
        }
    }

    @Override
    public void Render(){ // Vẽ hình ảnh game, chạy trong vòng lặp game

        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics(); // Lấy đối tượng Graphics2D từ bufferedImage

        if(g2!=null){

            // Đặt lại màu nền trong trường hợp lỗi render
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
            
            // Xử lý render theo từng trạng thái
            switch(state){
                case INIT_GAME: // Màn hình chờ
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                    break;
                case PAUSEGAME: // Màn hình tạm dừng
                    // Vẽ lại game ở trạng thái hiện tại (đã được vẽ ở lần Render trước)
                    // Sau đó vẽ một box đen và thông báo tạm dừng lên trên
                    g2.setColor(Color.BLACK);
                    g2.fillRect(300, 260, 500, 70);
                    g2.setColor(Color.WHITE);
                    g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                    break;
                
                case GAMEWIN:
                case GAMEPLAY: // Trạng thái đang chơi
                    backgroundMap.draw(g2); // Vẽ bản đồ nền
                    particularObjectManager.draw(g2); // Vẽ nhân vật và kẻ thù
                    bulletManager.draw(g2); // Vẽ đạn
                    
                    // Vẽ thanh máu MegaMan
                    g2.setColor(Color.GRAY);
                    g2.fillRect(19, 59, 102, 22);
                    g2.setColor(Color.red);
                    g2.fillRect(20, 60, megaMan.getBlood(), 20);
                    
                    // Vẽ biểu tượng mạng sống
                    for(int i = 0; i < numberOfLife; i++){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("hearth").getImage(), 20 + i*40, 18, null);
                    }
                    
                    // Nếu thắng game, vẽ thêm màn hình chiến thắng
                    if(state == GAMEWIN){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("gamewin").getImage(), 300, 300, null);
                    }
                    
                    break;
                case GAMEOVER: // Màn hình Game Over
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("GAME OVER!", 450, 300);
                    break;
            }
            // Xóa Graphics2D sau khi sử dụng để giải phóng tài nguyên
            g2.dispose();
        }
    }

    @Override
    public BufferedImage getBufferedImage(){ // Trả về hình ảnh bộ đệm đã được vẽ
        return bufferedImage;
    }

    @Override
    public void setPressedButton(int code) { // Xử lý sự kiện khi phím được nhấn xuống
       switch(code){
            
            case KeyEvent.VK_DOWN: // Phím xuống
                megaMan.dick(); // Hành động cúi người/trườn
                break;
                
            case KeyEvent.VK_RIGHT: // Phím phải
                megaMan.setDirection(megaMan.RIGHT_DIR); // Đặt hướng là phải
                megaMan.run(); // Bắt đầu chạy
                break;
                
            case KeyEvent.VK_LEFT: // Phím trái
                megaMan.setDirection(megaMan.LEFT_DIR); // Đặt hướng là trái
                megaMan.run(); // Bắt đầu chạy
                break;
                
            case KeyEvent.VK_ENTER: // Phím Enter
                if(state == GameWorldState.INIT_GAME){ // Nếu đang ở màn hình chờ
                    switchState(GameWorldState.GAMEPLAY); // Chuyển sang trạng thái chơi game
                }
                break;
                
            case KeyEvent.VK_SPACE: // Phím Space
                megaMan.jump(); // Hành động nhảy
                break;
                
            case KeyEvent.VK_A: // Phím A
                megaMan.attack(); // Hành động tấn công/bắn
                break;
                
        }}

    @Override
    public void setReleasedButton(int code) { // Xử lý sự kiện khi phím được nhả ra
        switch(code){
            
            case KeyEvent.VK_UP: // Phím lên (không làm gì khi nhả)
                break;
                
            case KeyEvent.VK_DOWN: // Phím xuống
                megaMan.standUp(); // Hành động đứng dậy sau khi cúi
                break;
                
            case KeyEvent.VK_RIGHT: // Phím phải
                if(megaMan.getSpeedX() > 0) // Nếu đang chạy sang phải
                    megaMan.stopRun(); // Dừng chạy
                break;
                
            case KeyEvent.VK_LEFT: // Phím trái
                if(megaMan.getSpeedX() < 0) // Nếu đang chạy sang trái
                    megaMan.stopRun(); // Dừng chạy
                break;
                
            case KeyEvent.VK_ENTER: // Phím Enter
                if(state == GAMEOVER || state == GAMEWIN) { // Nếu đang ở màn hình Game Over/Win
                    // Tạo một GameWorldState mới để chơi lại
                    gamePanel.setState(new GameWorldState(gamePanel)); 
                } else if(state == PAUSEGAME) { // Nếu đang tạm dừng
                    state = lastState; // Trở lại trạng thái trước khi tạm dừng
                }
                break;
                
            case KeyEvent.VK_SPACE: // Phím Space (không làm gì khi nhả)
                break;
                
            case KeyEvent.VK_A: // Phím A (không làm gì khi nhả)
                break;
            case KeyEvent.VK_ESCAPE: // Phím Escape
                lastState = state; // Lưu lại trạng thái hiện tại
                state = PAUSEGAME; // Chuyển sang trạng thái tạm dừng
                break;
        }}
}