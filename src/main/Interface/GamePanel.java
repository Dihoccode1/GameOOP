package main.Interface;

import main.input.KeyboardInput;

import javax.swing.JPanel;

import effect.Animation;
import effect.FrameImage;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IO;
import java.io.IOException;
import javax.imageio.ImageIO; 
import java.awt.Graphics2D;
public class GamePanel extends JPanel implements Runnable {
    private KeyboardInput keyboard;
    private Thread thread;
    private boolean isRunning = false;
    private Animation animation;
    FrameImage frame1,frame2,frame3;

    public GamePanel() {
        keyboard = new KeyboardInput();
        this.addKeyListener(keyboard);
        try{
            BufferedImage image = ImageIO.read(getClass().getResource("/res/img/megasprite.png"));
            BufferedImage  subImage1=image.getSubimage(529, 38, 70, 100);
            frame1= new FrameImage("frame1",subImage1);
            BufferedImage subImage2=image.getSubimage(616, 38, 70, 100);
            frame2= new FrameImage("frame2",subImage2);
            BufferedImage subImage3=image.getSubimage(704, 38, 70, 100);
            frame3= new FrameImage("frame3",subImage3);
                 animation = new Animation();
        animation.add(frame1, 500*1000000);
        animation.add(frame2, 500*1000000);
        animation.add(frame3, 500*1000000);
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
        this.setFocusable(true);
}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
     g.fillRect(0,0,GameFrame.SCREEN_WIDTH,GameFrame.SCREEN_HEIGHT);
     Graphics2D  g2D= (Graphics2D) g;
     animation.update(System.nanoTime());
      animation.draw(g2D,100 , 130) ;
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
