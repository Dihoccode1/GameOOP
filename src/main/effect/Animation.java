package main.effect; // Gói chứa các lớp liên quan đến hiệu ứng (effects)


import java.awt.Graphics2D;             // Dùng để vẽ đồ họa
import java.awt.geom.AffineTransform;   // Dùng để biến đổi hình học (ví dụ: lật hình)
import java.awt.image.AffineTransformOp; // Dùng để áp dụng biến đổi lên hình ảnh
import java.awt.image.BufferedImage;    // Dùng để lưu trữ hình ảnh
import java.util.ArrayList;             // Dùng để lưu trữ danh sách các khung hình và delay

/**
 * Lớp Animation.
 * Quản lý một chuỗi các FrameImage và thời gian hiển thị (delay) của chúng 
 * để tạo ra hiệu ứng hoạt hình.
 */
public class Animation {
    
    private String name; // Tên của Animation
    
    private boolean isRepeated; // Cờ (flag) cho biết animation có lặp lại hay không
    
    private ArrayList<FrameImage> frameImages; // Danh sách các khung hình ảnh trong animation
    private int currentFrame; // Chỉ số của khung hình hiện tại đang được hiển thị
    
    private ArrayList<Boolean> ignoreFrames; // Danh sách các cờ cho biết khung hình nào cần bị bỏ qua khi render
    
    private ArrayList<Double> delayFrames; // Thời gian delay (tính bằng nano giây) của mỗi khung hình
    private long beginTime; // Thời điểm bắt đầu hiển thị khung hình hiện tại (nano giây)

    private boolean drawRectFrame; // Cờ (flag) dùng để debug, vẽ khung hình chữ nhật bao quanh frame
    
    // Hàm khởi tạo mặc định
    public Animation(){
        delayFrames = new ArrayList<Double>();
        beginTime = 0;
        currentFrame = 0;

        ignoreFrames = new ArrayList<Boolean>();
        
        frameImages = new ArrayList<FrameImage>();
        
        drawRectFrame = false;
        
        isRepeated = true; // Mặc định animation sẽ lặp lại
    }
    
    // Hàm khởi tạo sao chép (Copy Constructor)
    // Tạo bản sao sâu (deep copy) của một Animation khác
    public Animation(Animation animation){
        
        // Sao chép các thuộc tính cơ bản
        beginTime = animation.beginTime;
        currentFrame = animation.currentFrame;
        drawRectFrame = animation.drawRectFrame;
        isRepeated = animation.isRepeated;
        
        // Sao chép danh sách thời gian delay
        delayFrames = new ArrayList<Double>();
        for(Double d : animation.delayFrames){
            delayFrames.add(d);
        }
        
        // Sao chép danh sách cờ bỏ qua frame
        ignoreFrames = new ArrayList<Boolean>();
        for(boolean b : animation.ignoreFrames){
            ignoreFrames.add(b);
        }
        
        // Sao chép danh sách khung hình (tạo bản sao của từng FrameImage)
        frameImages = new ArrayList<FrameImage>();
        for(FrameImage f : animation.frameImages){
            frameImages.add(new FrameImage(f)); // Tạo bản sao của FrameImage
        }
    }
    
    // --- GETTER & SETTER ---
    
    public void setIsRepeated(boolean isRepeated){
        this.isRepeated = isRepeated;
    }
    
    public boolean getIsRepeated(){
        return isRepeated;
    }
    
    // Kiểm tra xem một frame có bị bỏ qua (ignore) hay không
    public boolean isIgnoreFrame(int id){
        return ignoreFrames.get(id);
    }
    
    // Đặt cờ bỏ qua (true) cho một frame
    public void setIgnoreFrame(int id){
        if(id >= 0 && id < ignoreFrames.size())
            ignoreFrames.set(id, true);
    }
    
    // Bỏ đặt cờ bỏ qua (false) cho một frame
    public void unIgnoreFrame(int id){
        if(id >= 0 && id < ignoreFrames.size())
            ignoreFrames.set(id, false);
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    
    // Đặt khung hình hiện tại
    public void setCurrentFrame(int currentFrame){
        if(currentFrame >= 0 && currentFrame < frameImages.size())
            this.currentFrame = currentFrame;
        else this.currentFrame = 0; // Nếu vượt giới hạn, đặt lại về 0
    }
    
    // Lấy chỉ số khung hình hiện tại
    public int getCurrentFrame(){
        return this.currentFrame;
    }
    
    // Đặt lại animation về trạng thái ban đầu
    public void reset(){
        currentFrame = 0;
        beginTime = 0; // Đặt lại thời điểm bắt đầu
    }
    
    // Thêm một khung hình mới vào animation
    public void add(FrameImage frameImage, double timeToNextFrame){

        ignoreFrames.add(false); // Mặc định không bỏ qua frame mới
        frameImages.add(frameImage);
        delayFrames.add(new Double(timeToNextFrame)); // Thêm thời gian delay cho frame này
        
    }
    
    // Đặt cờ vẽ khung debug
    public void setDrawRectFrame(boolean b){
        drawRectFrame = b;
    }

    
    // Lấy hình ảnh của khung hình hiện tại
    public BufferedImage getCurrentImage(){
        return frameImages.get(currentFrame).getImage();
    }
    
    // Cập nhật animation, xác định xem đã đến lúc chuyển sang khung hình tiếp theo chưa
    public void Update(long deltaTime){
        
        if(beginTime == 0) beginTime = deltaTime; // Nếu là lần update đầu tiên, ghi lại thời điểm
        else{
            
            // Nếu thời gian từ lúc bắt đầu khung hình đến hiện tại lớn hơn thời gian delay của khung hình đó
            if(deltaTime - beginTime > delayFrames.get(currentFrame)){
                nextFrame(); // Chuyển sang khung hình tiếp theo
                beginTime = deltaTime; // Cập nhật thời điểm bắt đầu khung hình mới
            }
        }
        
    }

    
    // Kiểm tra xem khung hình hiện tại có phải là khung hình cuối cùng không
    public boolean isLastFrame(){
        if(currentFrame == frameImages.size() - 1)
            return true;
        else return false;
    }
    
    // Logic chuyển sang khung hình tiếp theo
    private void nextFrame(){
        
        if(currentFrame >= frameImages.size() - 1){ // Nếu đã ở khung hình cuối cùng
            
            if(isRepeated) currentFrame = 0; // Nếu được phép lặp lại, quay về khung 0
            
        }
        else currentFrame++; // Nếu chưa cuối, tăng lên 1
        
        // Nếu khung hình mới bị đánh dấu là bỏ qua, gọi nextFrame() đệ quy để chuyển tiếp
        if(ignoreFrames.get(currentFrame)) nextFrame();
        
    }
    
    
    
    // Phương thức lật tất cả hình ảnh theo chiều ngang (dùng để chuyển hướng animation)
    public void flipAllImage(){
        
        for(int i = 0;i < frameImages.size(); i++){
            
            BufferedImage image = frameImages.get(i).getImage();
            
            // Tạo phép biến đổi lật ngang: Scale X = -1, Scale Y = 1
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            // Dịch chuyển hình ảnh sang trái bằng chiều rộng của nó để đảm bảo hình ảnh nằm đúng vị trí
            tx.translate(-image.getWidth(), 0);

            // Tạo AffineTransformOp để áp dụng phép biến đổi
            AffineTransformOp op = new AffineTransformOp(tx,
            AffineTransformOp.TYPE_BILINEAR);
            // Áp dụng phép biến đổi lên hình ảnh
            image = op.filter(image, null);
            
            frameImages.get(i).setImage(image); // Cập nhật hình ảnh đã lật
            
        }
        
    }
    
    // Phương thức vẽ khung hình hiện tại của animation
    public void draw(int x, int y, Graphics2D g2){
        
        BufferedImage image = getCurrentImage(); // Lấy khung hình hiện tại
        
        // Vẽ hình ảnh, căn giữa tại tọa độ (x, y)
        g2.drawImage(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
        
        // Nếu cờ debug được bật, vẽ khung hình chữ nhật
        if(drawRectFrame)
            g2.drawRect(x - image.getWidth()/2, x - image.getWidth()/2, image.getWidth(), image.getHeight());
        
    }
    
}