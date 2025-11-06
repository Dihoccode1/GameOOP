package main.input;

import java.awt.event.KeyEvent;

public class InputManager {
    public InputManager() {
        
    }
        public void processInputPressed(int keyCode) {
            switch (keyCode) {
               case   KeyEvent.VK_UP:
                    System.out.println("up");
               break;

               case KeyEvent.VK_DOWN:
                    System.out.println("down");
               break;

               case KeyEvent.VK_LEFT:
                    System.out.println("left");
               break;

               case KeyEvent.VK_RIGHT:
                System.out.println("right");
               break;

               case KeyEvent.VK_SPACE:
                    System.out.println("space");
               break;

               case KeyEvent.VK_ENTER:
                System.out.println("enter");
               break;
            }
        }


         public void processInputReleased(int keyCode) {
            switch (keyCode) {
               case   KeyEvent.VK_UP:
                    System.out.println("up");
               break;

               case KeyEvent.VK_DOWN:
                    System.out.println("down");
               break;

               case KeyEvent.VK_LEFT:
                    System.out.println("left");
               break;

               case KeyEvent.VK_RIGHT:
                System.out.println("right");
               break;

               case KeyEvent.VK_SPACE:
                    System.out.println("space");
               break;

               case KeyEvent.VK_ENTER:
                System.out.println("enter");
               break;
            }
        }
}
