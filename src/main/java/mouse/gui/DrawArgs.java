package mouse.gui;

import java.awt.*;

/**
 * Created by Florian on 2014-04-11.
 */
public class DrawArgs {
    public final int wndWidth;
    public final int wndHeight;
    public final Graphics2D g2d;

    public DrawArgs(int wndWidth, int wndHeight, Graphics2D g2d) {
        this.wndWidth = wndWidth;
        this.wndHeight = wndHeight;
        this.g2d = g2d;
    }
}