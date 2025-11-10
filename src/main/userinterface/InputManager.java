package main.userinterface; // Gói chứa các lớp giao diện người dùng

import main.state.State; // Import lớp State, đại diện cho các trạng thái của game (Menu, Playing,...)

/**
 * Lớp InputManager (Quản lý Đầu vào).
 * Nhiệm vụ của nó là nhận sự kiện nhấn/nhả phím từ GamePanel 
 * và chuyển tiếp sự kiện đó đến trạng thái game (State) hiện tại.
 */
public class InputManager {
    
    private State gameState; // Biến lưu trữ trạng thái game hiện tại (Menu, GameWorldState,...)
    
    // Hàm khởi tạo, nhận và lưu trữ trạng thái game ban đầu
    public InputManager(State state){
        this.gameState = state;
    }
    
    // Phương thức dùng để thay đổi trạng thái game mà InputManager đang quản lý
    // Cần dùng khi game chuyển từ Menu sang Playing, hoặc Playing sang GameOver
    public void setState(State state) {
        gameState = state;
    }
    
    // Phương thức xử lý sự kiện khi một phím được nhấn xuống
    // Nhận mã phím (code) và chuyển tiếp nó đến phương thức xử lý của trạng thái game hiện tại
    public void setPressedButton(int code){
        gameState.setPressedButton(code);
    }
    
    // Phương thức xử lý sự kiện khi một phím được nhả ra
    // Nhận mã phím (code) và chuyển tiếp nó đến phương thức xử lý của trạng thái game hiện tại
    public void setReleasedButton(int code){
        gameState.setReleasedButton(code);
    }
    
}