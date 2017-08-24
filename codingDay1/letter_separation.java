/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package junctiondetector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author rm
 */
public class letter_separation {

    int vis[][];
    int border_up, border_down, border_left, border_right;
    int arr[][][];
    int character[][];
    int hist[];
    int color[][];
    int max_hist = 0;
    int matra_max = 0;
    int matra_min;
    int fx[] = {0, 0, 1, -1, 1, -1, 1, -1};
    int fy[] = {1, -1, 0, 0, 1, -1, -1, 1};
    int width,height;

    public void func() throws IOException {
        BufferedImage img = null;
        File f = null;

        int threshold;

        try {
            f = new File("out.jpg");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        width = img.getWidth();
        height = img.getHeight();

        arr = new int[width + 5][height + 5][5];
        color = new int[width + 5][height + 5];
        vis = new int[width + 5][height + 5];
        hist = new int[height + 5];
        threshold = width / 10;

        matra_max = 0;
        matra_min = height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                arr[x][y][0] = a;
                arr[x][y][1] = r;
                arr[x][y][2] = g;
                arr[x][y][3] = b;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int currval[] = new int[5], cnt = 0;
                for (int k = 0; k < 4; k++) {
                    currval[k] = 0;
                }

                for (int dx = 0; dx <= 0; dx++) {
                    for (int dy = 0; dy <= 0; dy++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (0 <= nx && nx < width && 0 <= ny && ny < height) {
                            for (int k = 0; k < 4; k++) {
                                currval[k] += arr[nx][ny][k];
                            }
                            cnt++;
                        }
                    }
                }

                int avg = (currval[1] + currval[2] + currval[3]) / (cnt * 3);
                if (avg > 255 * 2 / 3) {
                    avg = 255;
                } else {
                    avg = 0;
                }

                color[x][y] = avg;

                int p = (avg << 16) | (avg << 8) | avg;
                img.setRGB(x, y, p);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (color[x][y] == 0) {
                    ++hist[y];
                }

            }
            
            max_hist = Math.max(max_hist, hist[y]);
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (Math.abs(hist[y] - max_hist) < threshold) {
                    matra_max = Math.max(matra_max, y);
                    matra_min = Math.min(matra_min, y);
                }

            }
        }
        System.out.println("max " + matra_max);
        System.out.println("min " + matra_min);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int flag = 0;
                int pic_cnt=0;
                String fileName;

                int avg = color[x][y];
                if (avg == 255 && vis[x][y] == 0) {
                    border_up = border_right = 0;
                    border_left = width;
                    border_down = height;
                    ++pic_cnt;
                    dfs(x, y);
                    int curr_x, curr_y, pic_x, pic_y;
                    for (pic_y = 0, curr_y = border_down; curr_y <= border_up; ++curr_y) {
                        for (pic_x = 0, curr_x = border_left; curr_x <= border_right; ++curr_x) {
                            avg = color[curr_x][curr_y];
                            int p = (avg << 16) | (avg << 8) | avg;
                            img.setRGB(pic_x, pic_y, p);
                            try {
                                fileName="new"+Integer.toString(pic_cnt)+".jpg";
                                f = new File(fileName);
                                ImageIO.write(img, "jpg", f);
                            } catch (IOException e) {
                                System.out.println(e);
                            }
                        }
                    }
                }

            }

        }

    }

    void dfs(int x, int y) {
        System.out.println("x ="+x+", y="+y);
        border_up = Math.max(border_up, y);
        border_down = Math.min(border_down, y);
        border_right = Math.max(border_right, x);
        border_left = Math.min(border_left, x);
        vis[x][y] = 1;
        int i, j, nx, ny;
        for (i = 0; i < 8; ++i) {

            nx = x + fx[i];
            ny = y + fy[i];
            if(nx<0 || nx>=width || ny<0 || ny>=height)
                continue;
            if (vis[nx][ny] == 0) {
                dfs(nx, ny);
            }

        }
    }

}
