package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game

/**
 * Lớp Camera.
 * Quản lý vị trí góc trên bên trái của vùng hiển thị (viewport) 
 * và đảm bảo nhân vật chính (MegaMan) luôn nằm trong tầm nhìn.
 */
public class Camera extends GameObject {

    private float widthView;  // Chiều rộng của khu vực hiển thị (viewport width)
    private float heightView; // Chiều cao của khu vực hiển thị (viewport height)
    
    private boolean isLocked = false; // Cờ (flag) cho biết camera có bị khóa (không di chuyển) hay không
    
    // Hàm khởi tạo
    public Camera(float x, float y, float widthView, float heightView, GameWorldState gameWorld) {
        super(x, y, gameWorld); // Gọi hàm khởi tạo lớp cha (GameObject)
        this.widthView = widthView;
        this.heightView = heightView;
    }

    // Khóa camera (thường dùng cho các cảnh cắt/cutscene)
    public void lock(){
        isLocked = true;
    }
    
    // Mở khóa camera (cho phép camera di chuyển theo nhân vật)
    public void unlock(){
        isLocked = false;
    }
    
    @Override
    public void Update() { // Cập nhật vị trí camera
    
        // Chỉ cập nhật nếu camera không bị khóa
        if(!isLocked){
        
            MegaMan mainCharacter = getGameWorld().megaMan; // Lấy tham chiếu đến nhân vật chính

            // --- LOGIC CẬP NHẬT TRỤC X (Cuộn ngang) ---
            // Nếu MegaMan cách góc trái màn hình > 400px, di chuyển camera sang phải theo nhân vật
            if(mainCharacter.getPosX() - getPosX() > 400) setPosX(mainCharacter.getPosX() - 400);
            // Nếu MegaMan cách góc trái màn hình < 200px, di chuyển camera sang trái theo nhân vật
            if(mainCharacter.getPosX() - getPosX() < 200) setPosX(mainCharacter.getPosX() - 200);

            // --- LOGIC CẬP NHẬT TRỤC Y (Cuộn dọc) ---
            // Nếu MegaMan cách góc trên > 400px (tức là MegaMan ở phía dưới màn hình), di chuyển camera xuống
            if(mainCharacter.getPosY() - getPosY() > 400) setPosY(mainCharacter.getPosY() - 400); 
            // Nếu MegaMan cách góc trên < 250px (tức là MegaMan ở phía trên màn hình), di chuyển camera lên
            else if(mainCharacter.getPosY() - getPosY() < 250) setPosY(mainCharacter.getPosY() - 250); 
        }
    
    }

    // --- GETTER & SETTER cho Kích thước View ---
    public float getWidthView() {
        return widthView;
    }

    public void setWidthView(float widthView) {
        this.widthView = widthView;
    }

    public float getHeightView() {
        return heightView;
    }

    public void setHeightView(float heightView) {
        this.heightView = heightView;
    }
    
}