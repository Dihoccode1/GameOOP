package main.Interface;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
public class GameFrame extends JFrame {
    public static final int SCREEN_WIDTH=1000;
    public static final int SCREEN_HEIGHT=600; 
    private GamePanel gamePanel= new GamePanel();
    public GameFrame() {
            Toolkit toolkit = this.getToolkit();
            Dimension dimension = toolkit.getScreenSize();
            this.setBounds((dimension.width-SCREEN_WIDTH)/2,(dimension.height-SCREEN_HEIGHT)/2,SCREEN_WIDTH,SCREEN_HEIGHT);
            //  tính toán kích thước frame 
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //  dùng để đóng chương trình 
            add(gamePanel);
    }
    public void startGame() {
        gamePanel.startGame();
    }
    public static void main(String[] args) {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
            // hiển thị frame 
            frame.startGame();
}
}