/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package junctiondetector;

/**
 *
 * @author rm
 */
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BlackandWhiteConverter{
        
    public static void main(String args[])throws IOException{
        BufferedImage img = null;
        File f = null;    
        int arr[][][];
        int hist[];
        int color[][];
        int max_hist=0;
        int matra_max=0;
        int matra_min;
        int threshold;
        
        for(int itr = 0; itr <= 27; itr++)
        {
            
            max_hist=0;
            matra_max=0;
            
            String path = "/home/rm/NetBeansProjects/JunctionDetector/lines/line-";
            try{
                path = path + String.valueOf(itr) + ".png"; 
                //path="in2.jpg";
                f = new File(path);      
                img = ImageIO.read(f);
            }catch(IOException e){
                System.out.println(e);
            }
            
            System.out.println("Dealing with line no - " + itr); 
            int width = img.getWidth();
            int height = img.getHeight();
        
            arr = new int[width+5][height+5][5];
            color = new int[width+5][height+5];
            hist = new int[height+5];
            threshold=width/10;
        
            matra_max=0;
            matra_min=height;

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
          
                    for(int dx = 0; dx <= 0; dx++)
                        for(int dy = 0; dy <= 0; dy++)
                        {
                            int nx = x + dx;
                            int ny = y + dy;
                        
                            if(0 <= nx && nx < width && 0 <= ny && ny < height){
                                for(int k = 0; k < 4; k++) currval[k] += arr[nx][ny][k]; 
                                cnt++;
                            }
                        }
                
                    int avg = (currval[1]+currval[2]+currval[3])/(cnt * 3);   
                    if(avg > 255*2 / 3) avg = 255;
                    else avg = 0;
                
                    color[x][y]=avg;
                
                    int p =  (avg<<16) | (avg<<8) | avg;
                    img.setRGB(x, y, p);
                }
            }
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                
                    if(color[x][y]==0)
                        ++hist[y];
                
                }
                System.out.println(hist[y]);
                max_hist=Math.max(max_hist,hist[y]);
            }
            
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                
                    if(Math.abs(hist[y]-max_hist)<threshold)
                    {
                        matra_max=Math.max(matra_max,y);
                        matra_min=Math.min(matra_min,y);
                    }
                
                }
            }
            System.out.println("max "+matra_max);
            System.out.println("min "+matra_min);
            
            int pre_matra[] = new int[width+5];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                
                    
                    int flag=0;
                    int avg=color[x][y];
                    if(y>matra_min && y<matra_max)
                    {
                        avg=pre_matra[x];
                    }
                    if(y==matra_max)
                    {
              
                        if(y<height-1)
                    {
                        if(color[x][y+1]==0) 
                            flag=1;
                    /*  if(x>0 && color[x-1][y+1]==0 )
                            flag=1;
                        if(x<width-1 &&  color[x+1][y+1]==0)
                            flag=1;*/
                    }
                    if(flag==0)
                        avg=255;
                    else
                        avg=0;
                    
                    }
                    flag=0;
                    if(y==matra_min)
                    {
              
                        if(y>0)
                    {
                        if(color[x][y-1]==0) 
                            flag=1;
                        /*  if(x>0 && color[x-1][y-1]==0 )
                            flag=1;
                            if(x<width-1 &&  color[x+1][y-1]==0)
                            flag=1;*/
                    }
                   
                        if(flag==0) avg=255;
                        pre_matra[x] = avg;
                    }
                                                        
                    int p =  (avg<<16) | (avg<<8) | avg;
                    img.setRGB(x, y, p);
                
                }            
            }
              
            path = path = "/home/rm/NetBeansProjects/JunctionDetector/lines/out-";
           
            try{
                path = path + String.valueOf(itr) + ".jpg"; 
                //path="out3.jpg";
                f = new File(path);
                ImageIO.write(img, "jpg", f);
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
}
