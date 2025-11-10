package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game

// Lớp BulletManager (Quản lý Đạn), kế thừa từ ParticularObjectManager
// Lớp này mở rộng chức năng quản lý đối tượng động để xử lý đặc biệt cho đạn.
public class BulletManager extends ParticularObjectManager {

    // Hàm khởi tạo
    public BulletManager(GameWorldState gameWorld) {
        super(gameWorld); // Gọi hàm khởi tạo lớp cha (ParticularObjectManager)
    }

    @Override
    public void UpdateObjects() { // Cập nhật logic cho tất cả đạn
        super.UpdateObjects(); // Gọi UpdateObjects của lớp cha (xử lý logic cập nhật cơ bản)
        
        synchronized(particularObjects){ // Khóa danh sách để đảm bảo đồng bộ
            for(int id = 0; id < particularObjects.size(); id++){
                
                ParticularObject object = particularObjects.get(id);
                
                // --- Logic Xóa Đạn Đặc Trưng ---
                // Kiểm tra nếu đạn nằm ngoài tầm nhìn của Camera HOẶC đạn đã ở trạng thái chết (DEATH)
                if(object.isObjectOutOfCameraView() || object.getState() == ParticularObject.DEATH){
                    particularObjects.remove(id); // Xóa đạn khỏi danh sách
                   
                }
            }
        }
    }
    
    
    
}