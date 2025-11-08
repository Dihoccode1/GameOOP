package main.state;

import main.effect.CacheDataLoader;
import main.gameobject.BackgroundMap;
import main.gameobject.BulletManager;
import main.gameobject.Camera;
import main.gameobject.MegaMan;
import main.gameobject.ParticularObject;
import main.gameobject.ParticularObjectManager;
import main.gameobject.PhysicalMap;
import main.gameobject.RedEyeDevil;
import main.userinterface.GameFrame;
import main.userinterface.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
// XÓA: import main.userinterface.MenuState; 


public class GameWorldState extends State {
	
    private BufferedImage bufferedImage;
    private int lastState;

    public ParticularObjectManager particularObjectManager;
    public BulletManager bulletManager;

    public MegaMan megaMan;
   
    public PhysicalMap physicalMap;
    public BackgroundMap backgroundMap;
    public Camera camera;

    public static final int INIT_GAME = 0;
    public static final int GAMEPLAY = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWIN = 4;
    public static final int PAUSEGAME = 5;
    
    public int state = INIT_GAME;
    public int previousState = state;
    
    private int numberOfLife = 3;
    
    
    public GameWorldState(GamePanel gamePanel){
            super(gamePanel);
      
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        megaMan = new MegaMan(400, 400, this);
        physicalMap = new PhysicalMap(0, 0, this);
        backgroundMap = new BackgroundMap(0, 0, this);
        camera = new Camera(0, 50, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, this);
        bulletManager = new BulletManager(this);
        
        particularObjectManager = new ParticularObjectManager(this);
        particularObjectManager.addObject(megaMan);
        
        initEnemies();
    }
    
    private void initEnemies(){
        // CHỈ GIỮ LẠI CÁC KẺ THÙ ĐƠN GIẢN
        ParticularObject redeye = new RedEyeDevil(1250, 410, this);
        redeye.setDirection(ParticularObject.LEFT_DIR);
        redeye.setTeamType(ParticularObject.ENEMY_TEAM);
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

    public void switchState(int state){
        previousState = this.state;
        this.state = state;
    }
    
    // Giữ lại drawString vì nó vẫn được sử dụng trong Render() cho Game Over/Win
    private void drawString(Graphics2D g2, String text, int x, int y){
        for(String str : text.split("\n"))
            g2.drawString(str, x, y+=g2.getFontMetrics().getHeight());
    }
    
    public void Update(){
        
        switch(state){
            case INIT_GAME:
                break;
            
            case GAMEPLAY:
                particularObjectManager.UpdateObjects();
                bulletManager.UpdateObjects();
        
                physicalMap.Update();
                camera.Update();
                
                if(megaMan.getState() == ParticularObject.DEATH){
                    numberOfLife --;
                    if(numberOfLife >= 0){
                        megaMan.setBlood(100);
                        megaMan.setPosY(megaMan.getPosY() - 50);
                        megaMan.setState(ParticularObject.NOBEHURT);
                        particularObjectManager.addObject(megaMan);
                    }else{
                        switchState(GAMEOVER);
                    }
                }
                
                break;
            case GAMEOVER:
                break;
            case GAMEWIN:
                break;
        }
    }

    public void Render(){

        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

        if(g2!=null){

            // Ghi chú và code debug được giữ lại, có thể xóa sau.
            // ...
            
            switch(state){
                case INIT_GAME:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                    break;
                case PAUSEGAME:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(300, 260, 500, 70);
                    g2.setColor(Color.WHITE);
                    g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                    break;
                
                case GAMEWIN:
                case GAMEPLAY:
                    backgroundMap.draw(g2);
                    particularObjectManager.draw(g2);  
                    bulletManager.draw(g2);
                    
                    g2.setColor(Color.GRAY);
                    g2.fillRect(19, 59, 102, 22);
                    g2.setColor(Color.red);
                    g2.fillRect(20, 60, megaMan.getBlood(), 20);
                    
                    for(int i = 0; i < numberOfLife; i++){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("hearth").getImage(), 20 + i*40, 18, null);
                    }
                    
                    if(state == GAMEWIN){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("gamewin").getImage(), 300, 300, null);
                    }
                    
                    break;
                case GAMEOVER:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("GAME OVER!", 450, 300);
                    break;
            }
        }
    }

    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }

    @Override
    public void setPressedButton(int code) {
       switch(code){
            
            case KeyEvent.VK_DOWN:
                megaMan.dick();
                break;
                
            case KeyEvent.VK_RIGHT:
                megaMan.setDirection(megaMan.RIGHT_DIR);
                megaMan.run();
                break;
                
            case KeyEvent.VK_LEFT:
                megaMan.setDirection(megaMan.LEFT_DIR);
                megaMan.run();
                break;
                
            case KeyEvent.VK_ENTER:
                if(state == GameWorldState.INIT_GAME){
                    switchState(GameWorldState.GAMEPLAY); 
                }
                break;
                
            case KeyEvent.VK_SPACE:
                megaMan.jump();
                break;
                
            case KeyEvent.VK_A:
                megaMan.attack();
                break;
                
        }}

    @Override
    public void setReleasedButton(int code) {
        switch(code){
            
            case KeyEvent.VK_UP:
                break;
                
            case KeyEvent.VK_DOWN:
                megaMan.standUp();
                break;
                
            case KeyEvent.VK_RIGHT:
                if(megaMan.getSpeedX() > 0)
                    megaMan.stopRun();
                break;
                
            case KeyEvent.VK_LEFT:
                if(megaMan.getSpeedX() < 0)
                    megaMan.stopRun();
                break;
                
            case KeyEvent.VK_ENTER:
                if(state == GAMEOVER || state == GAMEWIN) {
                    // THAY THẾ logic Menu: Khởi động lại GameWorldState mới.
                    gamePanel.setState(new GameWorldState(gamePanel));
                } else if(state == PAUSEGAME) {
                    state = lastState;
                }
                break;
                
            case KeyEvent.VK_SPACE:
                break;
                
            case KeyEvent.VK_A:
                break;
            case KeyEvent.VK_ESCAPE:
                lastState = state;
                state = PAUSEGAME;
                break;
        }}
}