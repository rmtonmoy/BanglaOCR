import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GrayscaleMean{
    
    
    public static void main(String args[])throws IOException{
        BufferedImage img = null;
        File f = null;    
        int arr[][][];
        
        try{
            f = new File("C:\\Users\\User\\Desktop\\BlackAndWhite.jpg");      
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }

        int width = img.getWidth();
        int height = img.getHeight();
        
        arr = new int[width+5][height+5][5];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                int avg = (r+g+b)/3;           
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                
                arr[x][y][0] = a;
                arr[x][y][1] = r;
                arr[x][y][2] = g;
                arr[x][y][3] = b;
            }
        }
        
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                
                int currval[] = new int[5], cnt = 0;
                for(int k = 0; k < 4; k++) currval[k] = 0;
          
                for(int dx = -5; dx <= 5; dx++)
                    for(int dy = -5; dy <= 5; dy++)
                    {
                        int nx = x + dx;
                        int ny = y + dy;
                        
                        if(0 <= nx && nx < width && 0 <= ny && ny < height){
                            for(int k = 0; k < 4; k++) currval[k] += arr[nx][ny][k]; 
                            cnt++;
                        }
                    }
                
                int avg = (currval[1]+currval[2]+currval[3])/(cnt * 3);   
                int p =  (avg<<16) | (avg<<8) | avg;
                img.setRGB(x, y, p);
            }
        }

        try{
            f = new File("C:\\Users\\User\\Desktop\\meanFilter.jpg");
            ImageIO.write(img, "jpg", f);
        }catch(IOException e){
            System.out.println(e);
        }
    }
}