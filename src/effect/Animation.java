package effect;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;

public class Animation {
    private String name;
    private boolean isRepeated;
    private ArrayList<FrameImage> frameImages ;
    private ArrayList<Boolean>ignoreFrames ;
        // mảng dùng để kiểm soát các khung hình thừa
    private ArrayList<Double> delayFrames  ;
        //  mảng dùng chứa thời gian delay các frame 
    private boolean drawRectFrame ;
    //  tạo box kiểm soát độ rộng của khung hình 
    private int currentFrameIndex ;
    private long beginTime;
    public Animation(){
        delayFrames= new ArrayList<Double>();
        beginTime=0;
        currentFrameIndex=0;
        ignoreFrames= new ArrayList<Boolean>();
        frameImages= new ArrayList<FrameImage>();
        drawRectFrame=false;
        isRepeated  = true;
    }

    // setter and getter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    public ArrayList<FrameImage> getFrameImages() {
        return frameImages;
    }

    public void setFrameImages(ArrayList<FrameImage> frameImages) {
        this.frameImages = frameImages;
    }

    public ArrayList<Boolean> getIgnoreFrames() {
        return ignoreFrames;
    }

    

    public void setIgnoreFrames(ArrayList<Boolean> ignoreFrames) {
        this.ignoreFrames = ignoreFrames;
    }

    public ArrayList<Double> getDelayFrames() {
        return delayFrames;
    }

    public void setDelayFrames(ArrayList<Double> delayFrames) {
        this.delayFrames = delayFrames;
    }

    public boolean getDrawRectFrame() {
        return drawRectFrame;
    }

    public void setDrawRectFrame(boolean drawRectFrame) {
        this.drawRectFrame = drawRectFrame;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        if(currentFrameIndex>=0 && currentFrameIndex< frameImages.size()){
            this.currentFrameIndex=currentFrameIndex;
        }
        else{
            this.currentFrameIndex=0;
        }
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    //  
    
// Hàm sao chép khởi tạo
    public Animation(Animation animation){
        beginTime=animation.getBeginTime();
        currentFrameIndex=animation.getCurrentFrameIndex();
        drawRectFrame=animation.getDrawRectFrame();
        isRepeated=animation.getIsRepeated();
        delayFrames= new ArrayList<Double>();
            for(Double d: animation.delayFrames){
                delayFrames.add(d);
            }
        ignoreFrames=new ArrayList<Boolean>();
        for(boolean d:animation.ignoreFrames){
            ignoreFrames.add(d);
        }
        frameImages=new ArrayList<FrameImage>();
        for(FrameImage f:animation.frameImages){
            frameImages.add(new FrameImage(f));
        }
    }

        public  boolean isIgnoreFrame(int index){
            return ignoreFrames.get(index);
        }
        // kiểm tra xem có phải frame bị thừa không 

        public void setIgnoreFrame(int index,boolean value){
            if( index>=0 && index<ignoreFrames.size()){
                ignoreFrames.set(index, true);
            }
        }
        // tránh các ngoại lệ phát sinh trong quá trình runningTime

        public void isUnIgnoreFrame(int index){
            if(index >= 0 && index<ignoreFrames.size()){
                ignoreFrames.set(index, false);
            }
        }
        public void resert(){
            beginTime=0;
            currentFrameIndex=0;
           for(int i=0;i<ignoreFrames.size();i++){
               ignoreFrames.set(i, false);
           }
        }

        public void add(FrameImage frameImage, double timeToNextFrame){
             ignoreFrames.add(false);
            frameImages.add(frameImage);
            delayFrames.add(timeToNextFrame);
             
        }

        public BufferedImage getCurrentImage(){
            return frameImages.get(currentFrameIndex).getImage();
       }


/**
 * Chuyển sang frame kế tiếp trong animation.
 * - Nếu chưa tới frame cuối thì tăng chỉ số lên 1.
 * - Nếu tới frame cuối và hiệu ứng được lặp lại, quay về frame đầu.
 * - Nếu không lặp, giữ nguyên frame cuối.
 */


       public void update(long currentTime){
            if(beginTime==0) beginTime=currentTime;
            else{
                if(currentTime-beginTime> delayFrames.get(currentFrameIndex)){
                    nextFrame();
                    beginTime=currentTime;
                }
            }
       }

    
/**
 * Chuyển sang khung hình (frame) tiếp theo trong chuỗi hoạt ảnh (animation).
 * <p>
 * Nếu đã đạt đến khung hình cuối cùng và cờ lặp (isRepeated) là true,
 * nó sẽ quay lại khung hình đầu tiên. Nếu không, nó sẽ dừng lại ở khung hình cuối.
 * <p>
 * **Lưu ý:** Hàm này gọi đệ quy (recursively) nếu khung hình tiếp theo
 * được đánh dấu là phải bỏ qua (ignoreFrames).
 */


       public void nextFrame(){
            if(currentFrameIndex>=frameImages.size()-1){
                if(isRepeated) currentFrameIndex=0;
            }
            else{
                currentFrameIndex++;
            }
            if(ignoreFrames.get(currentFrameIndex)){
                nextFrame();
            }
       }
       
       
       public boolean isLastFrame(){
        if(currentFrameIndex==frameImages.size()-1){
            return true;
        }
        else return false;
       }
       

       /**
 * Lật (mirror) tất cả các khung hình (frames) theo chiều ngang. 
 * <p>
 * Hàm này duyệt qua danh sách 'frameImages', áp dụng phép biến đổi 
 * AffineTransform để lật từng ảnh (từ trái sang phải) và sau đó cập nhật lại
 * ảnh đã lật vào danh sách.
 */

       public void flipAllImages(){
            for(int i=0;i<frameImages.size();i++){
                    BufferedImage image= frameImages.get(i).getImage();

                   /**
 * Lật ảnh theo chiều ngang (mirror horizontally).
 * - Dùng phép biến đổi AffineTransform để nhân trục X với -1 (lật qua trục dọc).
 * - Sau đó dịch ảnh sang phải để nó nằm đúng vị trí hiển thị.
 * - Sử dụng AffineTransformOp với nội suy bilinear để làm mượt ảnh.
 */
AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // scale X = -1 để lật ảnh ngang, Y = 1 giữ nguyên
tx.translate(-image.getWidth(), 0); // dịch ảnh sang phải bằng chiều rộng ảnh để không bị lệch ra ngoài
AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR); // tạo phép biến đổi với nội suy mượt
image = op.filter(image, null); // áp phép biến đổi lên ảnh, trả về ảnh đã lật

                    frameImages.get(i).setImage(image);
                    
            }
       }

    /**
 * Vẽ frame hiện tại của animation lên màn hình.
 * - Lấy ảnh hiện tại thông qua getCurrentImage().
 * - Vẽ ảnh sao cho tâm ảnh trùng với vị trí (x, y) được truyền vào.
 * - Nếu chế độ debug (drawRectFrame = true) thì vẽ thêm khung viền quanh ảnh để kiểm tra vùng va chạm / giới hạn.
 */
public void draw(Graphics2D g, int x , int y) {
    BufferedImage image = getCurrentImage(); // lấy frame (ảnh) hiện tại của animation

    // vẽ ảnh tại vị trí (x, y), canh giữa theo tâm ảnh
    g.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);

    if (drawRectFrame) { // nếu bật chế độ vẽ khung bao (debug)
        // vẽ hình chữ nhật bao quanh ảnh để dễ kiểm tra vị trí và kích thước
        g.drawRect(x - image.getWidth() / 2, y - image.getHeight() / 2, image.getWidth(), image.getHeight());
    }
}
     
}
