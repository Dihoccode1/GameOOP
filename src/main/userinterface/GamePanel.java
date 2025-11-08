package main.userinterface;

import main.state.GameWorldState;
// XÓA: import main.state.MenuState; 
import main.state.State;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener{

    State gameState;

    InputManager inputManager;
    
    Thread gameThread;

    public boolean isRunning = true;

    public GamePanel(){

        // Chỉ giữ lại khởi tạo GameWorldState.
        // Dòng này đã đúng theo yêu cầu tối giản (bỏ qua MenuState):
        gameState = new GameWorldState(this); 
        
        inputManager = new InputManager(gameState);

    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    // XÓA: int a = 0; (Biến debug không cần thiết)
    
    @Override
    public void run() {

        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;

        long period = 1000000000/80;

        while(isRunning){

            gameState.Update();
            // Tối ưu hóa: Gọi Render() chỉ khi cần thiết, nhưng giữ lại ở đây để đảm bảo vẽ liên tục
            gameState.Render(); 

            repaint();

            currentTime = System.nanoTime();
            sleepTime = period - (currentTime - previousTime);
            try{

                    if(sleepTime > 0)
                            Thread.sleep(sleepTime/1000000);
                    // Giữ lại logic này để tránh treo CPU khi game chạy quá nhanh
                    else Thread.sleep(period/2000000); 

            }catch(Exception e){}

            previousTime = System.nanoTime();
        }

    }

    public void paint(Graphics g){

        g.drawImage(gameState.getBufferedImage(), 0, 0, this);

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        inputManager.setPressedButton(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputManager.setReleasedButton(e.getKeyCode());
    }

    public void setState(State state) {
        gameState = state;
        inputManager.setState(state);
    }
    
}