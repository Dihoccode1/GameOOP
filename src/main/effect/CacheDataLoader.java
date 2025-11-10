package main.effect; // Gói chứa các lớp liên quan đến hiệu ứng và tải dữ liệu

import java.applet.Applet;             // Import lớp Applet (ít dùng trong ứng dụng hiện đại, nhưng có trong code gốc)
import java.awt.image.BufferedImage;    // Import lớp BufferedImage để lưu trữ hình ảnh
import java.io.BufferedReader;          // Import BufferedReader để đọc dữ liệu từ file
import java.io.File;                    // Import File để làm việc với file hệ thống
import java.io.FileReader;              // Import FileReader để đọc file
import java.io.IOException;             // Import IOException để xử lý lỗi đầu vào/đầu ra
import java.util.Hashtable;             // Import Hashtable để lưu trữ dữ liệu dưới dạng key-value
import javax.imageio.ImageIO;           // Import ImageIO để đọc file hình ảnh

/**
 * Lớp CacheDataLoader.
 * Áp dụng mẫu thiết kế Singleton (chỉ có một instance duy nhất).
 * Chức năng: Tải toàn bộ tài nguyên game (ảnh, animation, bản đồ) vào bộ nhớ (cache) khi khởi động.
 */
public class CacheDataLoader {
    
    // Biến static lưu trữ instance duy nhất của lớp (Singleton)
    private static CacheDataLoader instance = null; 
    
    // Đường dẫn đến các file dữ liệu
    private String framefile = "data/frame.txt";       // Định nghĩa các khung hình ảnh đơn lẻ
    private String animationfile = "data/animation.txt"; // Định nghĩa các chuỗi animation
    private String physmapfile = "data/phys_map.txt";    // Định nghĩa bản đồ vật lý (va chạm)
    private String backgroundmapfile = "data/background_map.txt"; // Định nghĩa bản đồ nền (hình ảnh)
    
    
    // Hashtable lưu trữ các FrameImage, dùng tên làm key
    private Hashtable<String, FrameImage> frameImages; 
    // Hashtable lưu trữ các Animation, dùng tên làm key
    private Hashtable<String, Animation> animations;
    
    
    private int[][] phys_map;        // Mảng 2D lưu dữ liệu bản đồ vật lý
    private int[][] background_map;  // Mảng 2D lưu dữ liệu bản đồ nền
    
    // Hàm khởi tạo private: Ngăn không cho tạo đối tượng bằng từ khóa new (đặc trưng của Singleton)
    private CacheDataLoader() {}

    // Phương thức static để lấy instance duy nhất của lớp
    public static CacheDataLoader getInstance(){
        if(instance == null) // Nếu chưa có instance nào
            instance = new CacheDataLoader(); // Tạo instance mới
        return instance; // Trả về instance đã có
    }
    
    
    
    // Phương thức lấy một Animation theo tên
    public Animation getAnimation(String name){
        
        // Tạo một bản sao mới (copy) từ Animation gốc để tránh việc thay đổi trạng thái animation gốc
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
        
    }
    
    // Phương thức lấy một FrameImage theo tên
    public FrameImage getFrameImage(String name){

        // Tạo một bản sao mới (copy) từ FrameImage gốc
        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;

    }
    
    // Phương thức trả về dữ liệu bản đồ vật lý
    public int[][] getPhysicalMap(){
        return instance.phys_map;
    }
    
    // Phương thức trả về dữ liệu bản đồ nền
    public int[][] getBackgroundMap(){
        return instance.background_map;
    }
    
    // Phương thức chính để tải toàn bộ dữ liệu
    public void LoadData()throws IOException{
        
        LoadFrame();         // Tải các khung hình ảnh
        LoadAnimation();     // Tải các animation
        LoadPhysMap();       // Tải bản đồ vật lý
        LoadBackgroundMap(); // Tải bản đồ nền
      
        
    }
    
    
    
    // Phương thức tải dữ liệu Bản đồ Nền (Background Map)
    public void LoadBackgroundMap() throws IOException{
        
        FileReader fr = new FileReader(backgroundmapfile); // Mở file đọc
        BufferedReader br = new BufferedReader(fr);         // Tạo bộ đệm đọc
        
        String line = null;
        
        line = br.readLine();
        int numberOfRows = Integer.parseInt(line); // Đọc số hàng (Rows)
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line); // Đọc số cột (Columns)
            
        
        instance.background_map = new int[numberOfRows][numberOfColumns]; // Khởi tạo mảng 2D
        
        for(int i = 0;i < numberOfRows;i++){
            line = br.readLine();
            String [] str = line.split(" |  "); // Tách chuỗi theo dấu cách hoặc tab
            for(int j = 0;j<numberOfColumns;j++)
                instance.background_map[i][j] = Integer.parseInt(str[j]); // Chuyển đổi và lưu vào mảng
        }
        
        // --- Phần debug: In dữ liệu bản đồ ra console ---
        for(int i = 0;i < numberOfRows;i++){
            
            for(int j = 0;j<numberOfColumns;j++)
                System.out.print(" "+instance.background_map[i][j]);
            
            System.out.println();
        }
        
        br.close(); // Đóng bộ đọc
        
    }
    
    // Phương thức tải dữ liệu Bản đồ Vật lý (Physical Map)
    public void LoadPhysMap() throws IOException{
        
        FileReader fr = new FileReader(physmapfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        line = br.readLine();
        int numberOfRows = Integer.parseInt(line); // Đọc số hàng
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line); // Đọc số cột
            
        
        instance.phys_map = new int[numberOfRows][numberOfColumns]; // Khởi tạo mảng 2D
        
        for(int i = 0;i < numberOfRows;i++){
            line = br.readLine();
            String [] str = line.split(" "); // Tách chuỗi theo dấu cách
            for(int j = 0;j<numberOfColumns;j++)
                instance.phys_map[i][j] = Integer.parseInt(str[j]); // Chuyển đổi và lưu vào mảng
        }
        
        // --- Phần debug: In dữ liệu bản đồ ra console ---
        for(int i = 0;i < numberOfRows;i++){
            
            for(int j = 0;j<numberOfColumns;j++)
                System.out.print(" "+instance.phys_map[i][j]);
            
            System.out.println();
        }
        
        br.close();
        
    }
    
    // Phương thức tải dữ liệu Animation
    public void LoadAnimation() throws IOException {
        
        animations = new Hashtable<String, Animation>(); // Khởi tạo Hashtable lưu trữ Animation
        
        FileReader fr = new FileReader(animationfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        // Kiểm tra xem file có dữ liệu không
        if(br.readLine()==null) { 
            System.out.println("No data");
            throw new IOException(); // Ném ngoại lệ nếu file rỗng
        }
        else {
            
            // Đóng và mở lại file để đọc lại từ đầu
            fr = new FileReader(animationfile);
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals("")); // Bỏ qua các dòng trống
            int n = Integer.parseInt(line); // Đọc số lượng animation
            
            for(int i = 0;i < n; i ++){ // Lặp qua từng animation
                
                Animation animation = new Animation(); // Tạo đối tượng Animation mới
                
                while((line = br.readLine()).equals(""));
                animation.setName(line); // Đọc và đặt tên cho animation
                
                while((line = br.readLine()).equals(""));
                String[] str = line.split(" "); // Đọc chuỗi chứa tên Frame và thời gian delay
                
                // Lặp qua chuỗi, mỗi lần lấy 2 phần tử (tên Frame và delay)
                for(int j = 0;j<str.length;j+=2)
                    // Thêm FrameImage vào Animation cùng với thời gian delay
                    animation.add(getFrameImage(str[j]), Double.parseDouble(str[j+1]));
                
                instance.animations.put(animation.getName(), animation); // Lưu Animation vào Hashtable
                
            }
            
        }
        
        br.close();
    }
    
    // Phương thức tải dữ liệu Frame (Khung hình ảnh đơn lẻ)
    public void LoadFrame() throws IOException{
        
        frameImages = new Hashtable<String, FrameImage>(); // Khởi tạo Hashtable lưu trữ FrameImage
        
        FileReader fr = new FileReader(framefile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        // Kiểm tra file có dữ liệu không
        if(br.readLine()==null) {
            System.out.println("No data");
            throw new IOException();
        }
        else {
            
            // Đóng và mở lại file để đọc lại từ đầu
            fr = new FileReader(framefile);
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals(""));
            
            int n = Integer.parseInt(line); // Đọc số lượng FrameImage
            String path = null; // Lưu trữ đường dẫn file ảnh hiện tại
            BufferedImage imageData = null; // Lưu trữ hình ảnh lớn chứa tất cả các frame (sprite sheet)
            
            for(int i = 0;i < n; i ++){ // Lặp qua từng FrameImage
                
                FrameImage frame = new FrameImage();
                
                // Đọc tên frame
                while((line = br.readLine()).equals(""));
                frame.setName(line);
                
                // Đọc đường dẫn file ảnh
                while((line = br.readLine()).equals(""));
                String[] str = line.split(" ");
                
                // Cờ kiểm tra xem có cần tải file ảnh mới hay không (tránh tải lại file ảnh đã tải)
                boolean refreshImage = (path == null || !path.equals(str[1]));
                path = str[1]; // Cập nhật đường dẫn file ảnh
                
                // Đọc tọa độ x trên sprite sheet
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int x = Integer.parseInt(str[1]);
                
                // Đọc tọa độ y trên sprite sheet
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int y = Integer.parseInt(str[1]);
                
                // Đọc chiều rộng (w) của frame
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int w = Integer.parseInt(str[1]);
                
                // Đọc chiều cao (h) của frame
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int h = Integer.parseInt(str[1]);
                
                // Nếu cần tải file ảnh mới
                if(refreshImage) {
                    refreshImage = false; // Đặt lại cờ
                    imageData = ImageIO.read(new File(path)); // Tải hình ảnh lớn (sprite sheet)
                }
                
                // Cắt phần ảnh (subimage) cho frame hiện tại
                if(imageData != null) {
                    BufferedImage image = imageData.getSubimage(x, y, w, h);
                    frame.setImage(image); // Đặt hình ảnh đã cắt cho FrameImage
                }
                
                instance.frameImages.put(frame.getName(), frame); // Lưu FrameImage vào Hashtable
            }
            
        }
        
        br.close();
        
    }
    
}