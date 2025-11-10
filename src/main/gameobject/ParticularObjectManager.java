package main.gameobject; // Gói chứa các đối tượng trong game

import main.state.GameWorldState; // Import trạng thái thế giới game
import java.awt.Graphics2D;        // Import Graphics2D để vẽ
import java.util.Collections;      // Import Collections, dùng để tạo danh sách đồng bộ hóa
import java.util.LinkedList;       // Import LinkedList, để lưu trữ danh sách đối tượng
import java.util.List;             // Import List interface

// Lớp quản lý các đối tượng động (nhân vật chính, kẻ thù)
public class ParticularObjectManager {

    // Danh sách các đối tượng động (particularObjects), được bảo vệ khỏi truy cập đồng thời (synchronized)
    protected List<ParticularObject> particularObjects;

    private GameWorldState gameWorld; // Tham chiếu đến trạng thái thế giới game
    
    // Hàm khởi tạo
    public ParticularObjectManager(GameWorldState gameWorld){
        
        // Khởi tạo danh sách particularObjects là một LinkedList và bọc nó bằng Collections.synchronizedList
        // Điều này đảm bảo danh sách an toàn khi được truy cập bởi nhiều luồng (ví dụ: luồng game và luồng input)
        particularObjects = Collections.synchronizedList(new LinkedList<ParticularObject>());
        this.gameWorld = gameWorld; // Lưu trữ tham chiếu đến thế giới game
        
    }
    
    public GameWorldState getGameWorld(){
        return gameWorld; // Trả về trạng thái thế giới game
    }
    
    // Phương thức thêm một đối tượng động vào danh sách
    public void addObject(ParticularObject particularObject){
        
        
        synchronized(particularObjects){ // Khóa (lock) danh sách để đảm bảo tính đồng bộ (thread safety)
            particularObjects.add(particularObject);
        }
        
    }
    
    // Phương thức xóa một đối tượng động khỏi danh sách
    public void RemoveObject(ParticularObject particularObject){
        synchronized(particularObjects){ // Khóa danh sách
        
            // Duyệt qua danh sách để tìm đối tượng cần xóa
            for(int id = 0; id < particularObjects.size(); id++){
                
                ParticularObject object = particularObjects.get(id);
                // So sánh tham chiếu (kiểm tra xem có phải cùng một đối tượng không)
                if(object == particularObject)
                    particularObjects.remove(id); // Xóa đối tượng khỏi danh sách

            }
        }
    }
    
    // Phương thức kiểm tra va chạm của một đối tượng (object) với bất kỳ KẺ THÙ nào trong danh sách
    public ParticularObject getCollisionWidthEnemyObject(ParticularObject object){
        synchronized(particularObjects){ // Khóa danh sách
            for(int id = 0; id < particularObjects.size(); id++){
                
                ParticularObject objectInList = particularObjects.get(id);

                // 1. Kiểm tra hai đối tượng thuộc hai team khác nhau (ví dụ: Đạn của người chơi va chạm với Kẻ thù)
                // 2. Kiểm tra xem vùng va chạm của chúng có giao nhau (intersects) không
                if(object.getTeamType() != objectInList.getTeamType() && 
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())){
                    return objectInList; // Trả về đối tượng bị va chạm (kẻ thù)
                }
            }
        }
        return null; // Không tìm thấy va chạm
    }
    
    // Phương thức cập nhật logic cho TẤT CẢ các đối tượng trong danh sách
    public void UpdateObjects(){
        
        synchronized(particularObjects){ // Khóa danh sách
            for(int id = 0; id < particularObjects.size(); id++){
                
                ParticularObject object = particularObjects.get(id);
                
                // Chỉ cập nhật nếu đối tượng nằm trong tầm nhìn của Camera (tối ưu hóa hiệu suất)
                if(!object.isObjectOutOfCameraView()) object.Update();
                
                // Nếu đối tượng ở trạng thái chết
                if(object.getState() == ParticularObject.DEATH){
                    particularObjects.remove(id); // Xóa đối tượng khỏi danh sách
                }
            }
        }
    }
    
    // Phương thức vẽ TẤT CẢ các đối tượng trong danh sách
    public void draw(Graphics2D g2){
        synchronized(particularObjects){ // Khóa danh sách
            for(ParticularObject object: particularObjects)
                // Chỉ vẽ nếu đối tượng nằm trong tầm nhìn của Camera
                if(!object.isObjectOutOfCameraView()) object.draw(g2); 
        }
    }
    
}