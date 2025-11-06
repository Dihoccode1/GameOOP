package main.input;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyboardInput implements KeyListener {
     private InputManager input;
    // hàm gọi ngược 
    public KeyboardInput(){
        input = new InputManager();
    }
    public void keyTyped(KeyEvent e){
        System.out.println("keyTyped: ");
    }

  
    public void keyPressed(KeyEvent e){
        System.out.println("keyPressed: ");
             input.processInputPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e){
            System.out.println("keyReleased: ");
             input.processInputPressed(e.getKeyCode());
    }
 // 
}
