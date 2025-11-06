package main.Interface;

import main.input.KeyboardInput;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel implements Runnable {
    private KeyboardInput keyboard;
    private Thread thread;
    private boolean isRunning = false;


    public GamePanel() {
        keyboard = new KeyboardInput();
        this.addKeyListener(keyboard);
        this.setFocusable(true);
}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
     g.fillRect(0,0,GameFrame.SCREEN_WIDTH,GameFrame.SCREEN_HEIGHT);
       
    }

    @Override
    public void run() {
        final long FPS = 120;
        final long period = 1000000000 / FPS; // nano giây
        long beginTime;
        long sleepTime;
        int count = 1;

        beginTime = System.nanoTime();

        while (isRunning) {
            // System.out.println("Frame " + count++);

            // Cập nhật game logic ở đây
            repaint();

            // Tính thời gian thực tế chạy frame
            long deltaTime = System.nanoTime() - beginTime;
            sleepTime = period - deltaTime;

            if (sleepTime < 0) sleepTime = 0;

            try {
                Thread.sleep(sleepTime / 1000000); // chuyển ns -> ms
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            beginTime = System.nanoTime();
        }
    }

    public void startGame() {
        if (thread == null) {
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }
    }
}
