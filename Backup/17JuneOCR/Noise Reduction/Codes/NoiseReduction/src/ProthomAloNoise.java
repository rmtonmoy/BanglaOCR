import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ProthomAloNoise {
    static int vis[][];
    static BufferedImage img = null;
    
    void dfs(int x, int y)
    {
        
    }
    
    public static void main(String args[])
    {        
        File f = null;        
        try{
            f = new File("C:\\Users\\User\\Desktop\\in.jpg");
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

        int width = img.getWidth();
        int height = img.getHeight();
        
        vis = new int[width + 5][height + 5];
        int x, y, mn = 300, mx = 0;
        
        
        for(x = 0; x < width; x++)
            for(y = 0; y < height; y++)
            {
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                int avg = (r+g+b)/3;           
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                
                mn = Math.min(mn, avg);
                mx = Math.max(mx, avg);
            }
        
        System.out.println(mn + " " + mx);
    }
}
