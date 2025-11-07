package effect;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class FrameImage {
    private String name;
    private BufferedImage image;
    
    //  setter -getter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public FrameImage(String name , BufferedImage image){
            setImage(image);
            setName(name);
    }
// hàm sao chép khởi tạo
    public FrameImage(FrameImage frameImages){
        image= new BufferedImage(frameImages.getImageWidth(),frameImages.getImageHeight(),frameImages.getImage().getType());

        Graphics g = image.getGraphics();
        g.drawImage(frameImages.getImage(), getImageWidth()/2, getImageHeight(), null);
    }

    public int getImageWidth(){
        return image.getWidth();
    }
    public int getImageHeight(){
        return image.getHeight();
    }

    public void draw(Graphics2D g, int x , int y){
        g.drawImage(image,x -image.getWidth()/2,y-image.getHeight()/2,null);
    }


}