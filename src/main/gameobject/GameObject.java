package main.gameobject; // Gói (package) chứa các lớp đại diện cho các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game

/**
 * Lớp trừu tượng GameObject (Đối tượng Game).
 * Đây là lớp cơ sở cho tất cả các vật thể trong trò chơi, 
 * cung cấp các thuộc tính và chức năng cơ bản như vị trí và tham chiếu đến thế giới game.
 */
public abstract class GameObject {

    private float posX; // Vị trí theo trục X (tọa độ ngang)
    private float posY; // Vị trí theo trục Y (tọa độ dọc)
    
    private GameWorldState gameWorld; // Tham chiếu đến trạng thái thế giới game mà đối tượng thuộc về
    
    // Hàm khởi tạo
    public GameObject(float x, float y, GameWorldState gameWorld){
        posX = x; // Thiết lập vị trí X ban đầu
        posY = y; // Thiết lập vị trí Y ban đầu
        this.gameWorld = gameWorld; // Lưu trữ tham chiếu đến thế giới game
    }
    
    // --- GETTER & SETTER cho Vị trí X ---
    public void setPosX(float x){
        posX = x;
    }
    
    public float getPosX(){
        return posX;
    }
    
    // --- GETTER & SETTER cho Vị trí Y ---
    public void setPosY(float y){
        posY = y;
    }
    
    public float getPosY(){
        return posY;
    }
    
    // --- GETTER cho Thế giới Game ---
    public GameWorldState getGameWorld(){
        return gameWorld;
    }
    
    // Phương thức trừu tượng: Mọi đối tượng game đều phải có hàm Update()
    // Hàm này chứa logic cập nhật trạng thái của đối tượng trong mỗi khung hình (frame)
    public abstract void Update(); 
    
}