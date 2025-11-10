package main.userinterface; // Gói chứa các lớp giao diện người dùng

import main.state.GameWorldState; // Import trạng thái thế giới trò chơi
// XÓA: import main.state.MenuState; // (Dòng này bị xóa theo yêu cầu)
import main.state.State;         // Import lớp State (lớp cha cho các trạng thái game: Menu, Playing, GameOver)

import java.awt.Graphics;       // Import lớp Graphics, dùng để vẽ đồ họa
import java.awt.event.KeyEvent; // Import lớp KeyEvent, xử lý sự kiện phím
import java.awt.event.KeyListener; // Import giao diện KeyListener, để lắng nghe sự kiện bàn phím

import javax.swing.JPanel;      // Import lớp JPanel, dùng làm bảng vẽ chính trong cửa sổ game (GameFrame)

// Lớp GamePanel, là bảng vẽ chính, thực hiện giao diện Runnable (cho đa luồng) và KeyListener (cho đầu vào)
public class GamePanel extends JPanel implements Runnable, KeyListener{

    State gameState; // Biến đại diện cho trạng thái hiện tại của game (Menu, đang chơi, Game Over)

    InputManager inputManager; // Đối tượng quản lý đầu vào (bàn phím)
    
    Thread gameThread; // Luồng (Thread) riêng biệt để chạy vòng lặp game, giúp game không bị đơ

    public boolean isRunning = true; // Biến cờ (flag) kiểm soát vòng lặp game: true = game đang chạy

    public GamePanel(){ // Hàm khởi tạo

        // Khởi tạo trạng thái game ban đầu là GameWorldState (trạng thái chơi)
        gameState = new GameWorldState(this); 
        
        // Khởi tạo InputManager, truyền trạng thái game hiện tại vào để nó biết xử lý đầu vào cho trạng thái nào
        inputManager = new InputManager(gameState);

    }

    public void startGame(){ // Phương thức khởi động game loop

        gameThread = new Thread(this); // Tạo luồng mới, đối tượng 'this' (GamePanel) sẽ chạy phương thức run()
        gameThread.start();            // Bắt đầu luồng, gọi phương thức run()

    }
    

    
    @Override
    public void run() { // Phương thức này được gọi khi luồng gameThread bắt đầu (chứa vòng lặp game)

        long previousTime = System.nanoTime(); // Thời điểm bắt đầu của frame trước đó (đơn vị nanosecond)
        long currentTime;                      // Thời điểm hiện tại
        long sleepTime;                        // Thời gian cần ngủ (để đạt FPS mong muốn)

        // Tính toán khoảng thời gian lý tưởng cho mỗi frame (1 giây / 80 FPS = 12.5 triệu ns)
        long period = 1000000000/80; 

        while(isRunning){ // Vòng lặp game chính, lặp lại liên tục khi game đang chạy

            gameState.Update(); // 1. Cập nhật logic game (vị trí nhân vật, va chạm, logic AI,...)
       
            gameState.Render(); // 2. Chuẩn bị hình ảnh cho frame hiện tại (vẽ vào bộ đệm)

            repaint();          // 3. Gọi phương thức paint() của JPanel để vẽ hình ảnh lên màn hình

            currentTime = System.nanoTime(); // Ghi lại thời điểm sau khi cập nhật và vẽ
            // Tính thời gian cần "ngủ" để đảm bảo tốc độ 80 FPS
            sleepTime = period - (currentTime - previousTime); 
            
            try{ // Khối try-catch để xử lý ngoại lệ khi luồng ngủ

                if(sleepTime > 0)
                    // Ngủ một khoảng thời gian để giữ FPS ổn định (chuyển nanosecond sang millisecond)
                    Thread.sleep(sleepTime/1000000); 
                
                else 
                    // Nếu quá tải (sleepTime <= 0), ngủ một khoảng thời gian rất ngắn để tránh luồng game bị chiếm dụng quá nhiều
                    Thread.sleep(period/2000000); 

            }catch(Exception e){}

            previousTime = System.nanoTime(); // Cập nhật lại thời điểm bắt đầu frame mới
        }

    }

    @Override
    // Phương thức paint() tự động được gọi khi gọi repaint()
    public void paint(Graphics g){ 
        // Vẽ hình ảnh đã được Render (chuẩn bị) từ trạng thái game ra màn hình
        g.drawImage(gameState.getBufferedImage(), 0, 0, this); 

    }

    @Override
    public void keyTyped(KeyEvent e) {} // Bắt sự kiện gõ phím (ít dùng trong game)

    @Override
    public void keyPressed(KeyEvent e) { // Bắt sự kiện khi một phím được nhấn xuống
        // Truyền mã phím vừa nhấn vào InputManager để xử lý
        inputManager.setPressedButton(e.getKeyCode()); 
    }

    @Override
    public void keyReleased(KeyEvent e) { // Bắt sự kiện khi một phím được nhả ra
        // Truyền mã phím vừa nhả vào InputManager để xử lý
        inputManager.setReleasedButton(e.getKeyCode()); 
    }

    // Phương thức dùng để chuyển đổi trạng thái game (ví dụ: từ Menu sang Playing)
    public void setState(State state) { 
        gameState = state; // Cập nhật trạng thái game hiện tại
        inputManager.setState(state); // Cập nhật trạng thái cho InputManager để nó biết xử lý đầu vào mới
    }
    
}