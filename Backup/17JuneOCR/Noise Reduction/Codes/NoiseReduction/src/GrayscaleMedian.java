import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GrayscaleMedian
{
    public static void main(String args[])throws IOException
    {
        BufferedImage img = null;
        File f = null;

        try{
            f = new File("C:\\Users\\User\\Desktop\\BlackAndWhite.jpg");
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

        int width = img.getWidth();
        int height = img.getHeight();
        
        int arr[][] = new int[width + 5][height + 5]; 

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int p = img.getRGB(x,y);
                
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
          
                int avg = (r+g+b)/3;                                
                arr[x][y] = avg;
            }
        }
        
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int currval[] = new int[111], cnt = 0;
                for(int dx = -1; dx <= 1; dx++)
                    for(int dy = -1; dy <= 1; dy++)
                    {
                        int nx = x + dx;
                        int ny = y + dy;
                        
                        if(0 <= nx && nx < width && 0 <= ny && ny < height)
                        {
                            cnt++;
                            currval[cnt] = arr[nx][ny];
                        }
                    }
                
                for(int i = 1; i <= cnt; i++)
                    for(int j = cnt; j > 1; j--)
                        if(currval[j] < currval[j-1])
                        {
                            int tmp = currval[j];
                            currval[j] = currval[j-1];
                            currval[j-1] = tmp;                            
                        }
                
                int median;
                if(cnt %2 == 1) median = currval[(cnt+1)/2];
                else median = (currval[(cnt+1)/2] + currval[(cnt+1)/2 + 1])/2;
              
                //if(median > 255/2) median = 255;
                //else median = 0;
                int p = (median<<16) | (median<<8) | median;
                
                img.setRGB(x, y, p);
            }
        }

        try{
            f = new File("C:\\Users\\User\\Desktop\\medianFilterd.jpg");
            ImageIO.write(img, "jpg", f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}