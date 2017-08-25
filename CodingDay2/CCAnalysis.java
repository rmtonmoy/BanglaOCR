
/*
	Extracts Matra
	Java MatraExtractor.class INPUT_FILE_PATH OUTPUT_FILE_PATH  [TH = (max_hist * Integer.valueOf(args[2])) / 100;]

*/

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_BYTE_BINARY;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

public class CCAnalysis{
    static int width;
    static int height; 
    static boolean vis[][];
    static int color[][];    
    static int compno;
    static List imageList; 
    static List finalist; 
    static HashMap< Integer, Integer > HM; 
            
    public static void main(String args[])throws Exception{
        BufferedImage img = null;
        File f = null;    
        imageList = new LinkedList<BufferedImage>();    
        HM = new HashMap<Integer, Integer>();         
        int modeW = 0, mxcnt = 0; 
        
        compno = 0;
        
        String path;
        
        try{           
            path = "in.png";
            f = new File(path);      
            img = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }
        
        
        width = img.getWidth();
        height = img.getHeight();        
    
        color = new int[width+5][height+5];
        vis = new boolean[width+5][height+5];
    
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
            
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;                                                   
            
                int avg = Math.max(Math.max(r, g), b);
                if(avg > 255/2) color[x][y] = 255;
                else color[x][y] = 0;                
            }
        }
        
        
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++){
                if(vis[x][y] == false && color[x][y] == 0){
                    bfs(x, y);
                    compno++;
                }
            }
        
        
        System.out.println("\nTOTAL = " + compno); 
        
        for(HashMap.Entry<Integer, Integer> entry: HM.entrySet()){    
            int b = entry.getValue();  
            if(b >= mxcnt){
                mxcnt = b; 
                modeW = entry.getKey();
            }
        }    
        
        System.out.println("MAX COUNT = "  + mxcnt);                 
         
        
                
        for(int i = 1; i < imageList.size(); i++){
            
            if(isDot((BufferedImage) imageList.get(i))){
                
                
                
                BufferedImage L = (BufferedImage) imageList.get(i - 1); 
                BufferedImage R = (BufferedImage) imageList.get(i); 
                
//                try{
//                    f = new File("out" + i + ".png");
//                    ImageIO.write((RenderedImage) imageList.get(i), "png", f);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }                                              
                imageList.remove(i); 
                imageList.remove(i - 1);                   
                imageList.add(i - 1, join(L, R));
                
//                try{
//                    f = new File("out" + i + ".png");
//                    ImageIO.write(join(L, R), "png", f);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }   
                
            }
            
//            if( ((BufferedImage) imageList.get(i)).getWidth() > modeW ){
//                
//                try{
//                    f = new File("out" + i + ".png");
//                    ImageIO.write((RenderedImage) imageList.get(i), "png", f);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }    
//                
//            }
                
        }
        
        
        
        for(int i = 0; i < imageList.size(); i++){
            try{
               f = new File("out" + i + ".png");
               ImageIO.write((RenderedImage) imageList.get(i), "png", f);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    
    boolean cmp(BufferedImage u, BufferedImage v){
        return true;
    }
    
    static boolean isDot(BufferedImage img){
        int H = img.getHeight();
        int W = img.getWidth();
        
        int ttl = 0, blk = 0;
        for(int x = 0; x < W; x++)
            for(int y = 0; y < H; y++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;                                                   

                int avg = Math.max(Math.max(r, g), b);
                if(avg > 255/2) avg = 255;
                else avg = 0;  
                
                if(avg == 0) blk++;
                ttl++; 
            }
        
        if( ttl * 0.8 < blk ){
            return (Math.abs(H - W) < Math.max(H, W) * 0.2); 
        }
        return false; 
    }
    
    static BufferedImage join(BufferedImage L, BufferedImage R) throws Exception{
        BufferedImage ret;
        int W = L.getWidth();
        int H = L.getHeight() + R.getHeight();
        ret = new BufferedImage(W, H, TYPE_BYTE_BINARY); 
        
        if(L.getWidth() < R.getWidth()) throw new Exception("OH IT SHOULD NOT HAPPEN :("); 
        
        
        for(int x = 0; x < W; x++)
            for(int y = 0; y < H; y++){
                int p =  (255<<16) | (255<<8) | 255;
                ret.setRGB(x, y, p);
            }
        
        for(int x = 0; x < L.getWidth(); x++)
            for(int y = 0; y < L.getHeight(); y++){
                int p = L.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;                                                   

                int avg = Math.max(Math.max(r, g), b);
                if(avg > 255/2) avg = 255;
                else avg = 0;  
                
                if(avg == 0) ret.setRGB(x, y, 0);
            }
        
        int offset = (L.getWidth() - R.getWidth())/2; 
        for(int x = 0; x < R.getWidth(); x++)
            for(int y = 0; y < R.getHeight(); y++)
            {
                int p = R.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;                                                   

                int avg = Math.max(Math.max(r, g), b);
                if(avg > 255/2) avg = 255;
                else avg = 0; 
                
                if(avg == 0) ret.setRGB(x + offset, y + L.getHeight(), 0);
            }
        
        return ret; 
    }
    
    static void bfs(int x, int y)
    {
        vis[x][y] = true; 
        int dx[] = {+1, +1, +1, +0, +0, -1, -1, -1};
        int dy[] = {-1, +0, +1, +1, -1, -1, +0, +1};
        int qx[] = new int[(width+5) * (height+5)]; 
        int qy[] = new int[(width+5) * (height+5)];
        
        int nx, ny;
        File f = null; 
         
        int top = 0, bottom = 0;
        qx[top] = x;
        qy[top++] = y;
        int counter = 0;
        
        while(bottom != top){
            x = qx[bottom];
            y = qy[bottom++];
            
            counter++; 
            for(int i = 0; i < 8; i++){
                nx = x + dx[i];
                ny = y + dy[i];
                
                if(0 <= nx && nx < width && 0 <= ny && ny < height && vis[nx][ny] == false && color[nx][ny] == 0){
                    //System.out.println(top); 
                    
                    qx[top] = nx;
                    qy[top++] = ny;
                    vis[nx][ny] = true; 
                }
            }            
        }
           
        if(counter < 10) return;
        
        
        int min_x = width, max_x = 0, min_y = height, max_y = 0;
        for(int itr = 0; itr < top; itr++)
        {
            min_x = Math.min(min_x, qx[itr]);
            max_x = Math.max(max_x, qx[itr]);
            
            min_y = Math.min(min_y, qy[itr]);
            max_y = Math.max(max_y, qy[itr]); 
        }
        
        int W = max_x - min_x + 1;
        int H = max_y - min_y + 1;
        
        if(HM.containsKey(W)) HM.put(W, HM.get(W) + 1);
        else HM.put(W, 1); 
        
        BufferedImage img = new BufferedImage(W, H, TYPE_BYTE_BINARY);
        
        for( x = 0; x < W; x++)
            for( y = 0; y < H; y++){
                int p =  (255<<16) | (255<<8) | 255;
                img.setRGB(x, y, p);
            }
        
        for(int itr = 0; itr < top; itr++){         
            img.setRGB(qx[itr] - min_x, qy[itr] - min_y, 0);
        }
        
        imageList.add(img);
        
//        try{
//            f = new File("out" + compno + ".jpg");
//            ImageIO.write(img, "jpg", f);
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//        
//        
//        System.out.print(counter + " ");
    }
}
