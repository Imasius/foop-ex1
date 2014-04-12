package mouse.client.gui.helper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Florian on 2014-04-11.
 */
public class ImageBlender {
    public static BufferedImage blend(BufferedImage src, Color color, double t){
        BufferedImage dst = createCopy(src);
        double[] pixel = new double[4];
        double[] blendColor = new double[4];
        loadColor(color,blendColor);

        for (int y = 0; y < dst.getHeight(); y++)
            for (int x = 0; x < dst.getWidth(); x++){
                loadColor(dst.getRGB(x,y),pixel);
                blendColor(pixel, blendColor, t);
                dst.setRGB(x,y,storeColor(pixel));
            }

        return dst;
    }

    private static void loadColor(int rgb, double[] colors){
        loadColor(new Color(rgb, true), colors);
    }
    private static void loadColor(Color color, double[] colors){
        colors[0] = ((double)color.getRed()) / 255;
        colors[1] = ((double)color.getGreen()) / 255;
        colors[2] = ((double)color.getBlue()) / 255;
        colors[3] = ((double)color.getAlpha()) / 255;
    }
    private static int storeColor(double[] colors){
        return new Color((int)(colors[0]*255),
                         (int)(colors[1]*255),
                         (int)(colors[2]*255),
                         (int)(colors[3]*255)).getRGB();
    }
    private static void blendColor(double[] color, double[] blend, double t){
        for (int i = 0; i < 3 ; i ++)
            color[i] = color[i] * (1.0-t) + blend[i]*t;
    }
    private static BufferedImage createCopy(BufferedImage img){
        BufferedImage copy = new BufferedImage(img.getWidth(),
                                               img.getHeight(),
                                               BufferedImage.TYPE_4BYTE_ABGR);

        Graphics g = copy.getGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();

        return copy;
    }
}