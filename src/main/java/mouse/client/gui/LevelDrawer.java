package mouse.client.gui;

import mouse.client.data.ClientLevel;
import mouse.client.gui.helper.ImageBlender;
import mouse.shared.Mouse;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Date 06.04.2014
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class LevelDrawer {

    static final Color COLOR_BORDER = Color.blue;
    static final Color COLOR_EMPTY = Color.white;
    static final Color COLOR_DOOR_CLOSED = Color.red;
    static final Color COLOR_DOOR_OPEN = Color.green;
    static final Color COLOR_WALL = Color.black;
    static final int BORDER_WIDTH = 0;
    static final int MOUSE_MARGIN = 2;
    static final int BAIT_MARGIN = 3;
    static final int START_MARGIN = 1;

    private static final Logger log = LoggerFactory.getLogger(LevelDrawer.class);
    //TODO fix visibility
    ClientLevel level;
    BufferedImage imgBait;
    Image[] imgPlayerMouse;
    Image[] imgPlayerStart;

    public LevelDrawer(ClientLevel level) {
        this.level = level;

        BufferedImage imgMouse = null;
        BufferedImage imgStart = null;

        try {
            //Introduced alternative mouse image sizes for a higher resolution level which needs smaller graphics (low quality)
            //imgMouse = ImageIO.read(ClassLoader.getSystemResource("images/mouse.gif"));
            //imgStart = ImageIO.read(ClassLoader.getSystemResource("images/start.gif"));
            //this.imgBait = ImageIO.read(ClassLoader.getSystemResource("images/bait.gif"));

            imgMouse = ImageIO.read(ClassLoader.getSystemResource("images/mouse_small.gif"));
            imgStart = ImageIO.read(ClassLoader.getSystemResource("images/start_small.gif"));
            this.imgBait = ImageIO.read(ClassLoader.getSystemResource("images/bait_small.gif"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Color[] playerColors = new Color[]{
            Color.red,
            Color.green,
            Color.blue,
            Color.yellow
        };

        this.imgPlayerMouse = new Image[playerColors.length];
        this.imgPlayerStart = new Image[playerColors.length];
        for (int i = 0; i < playerColors.length; i++) {
            this.imgPlayerMouse[i] = ImageBlender.blend(imgMouse, playerColors[i], 0.5);
            this.imgPlayerStart[i] = ImageBlender.blend(imgStart, playerColors[i], 0.5);
        }
    }

    public void draw(DrawArgs args) {
        if (level == null) {
            args.g2d.drawString("Waiting for Game", args.wndWidth / 2, args.wndHeight / 2);
            return;
        }
        synchronized (level) {
            int tileWidth = args.wndWidth / level.getWidth();
            int tileHeight = args.wndHeight / level.getHeight();

            int tileContentWidth = tileWidth - 2 * BORDER_WIDTH;
            int tileContentHeight = tileHeight - 2 * BORDER_WIDTH;

            double mouseWidth = tileContentWidth - 2 * MOUSE_MARGIN;
            double mouseHeight = tileContentHeight - 2 * MOUSE_MARGIN;

            int mouseImgWidth = imgPlayerMouse[0].getWidth(null);
            int mouseImgHeight = imgPlayerMouse[0].getHeight(null);

            double baitWidth = tileContentWidth - 2 * BAIT_MARGIN;
            double baitHeight = tileContentHeight - 2 * BAIT_MARGIN;

            int baitImgWidth = imgBait.getWidth(null);
            int baitImgHeight = imgBait.getHeight(null);

            double startWidth = tileContentWidth - 2 * START_MARGIN;
            double startHeight = tileContentHeight - 2 * START_MARGIN;

            int startImgWidth = imgPlayerStart[0].getWidth(null);
            int startImgHeight = imgPlayerStart[0].getHeight(null);

            // draw border-color to background. tiles will leave border-areas out to show background.
            args.g2d.setColor(COLOR_BORDER);
            args.g2d.fillRect(0, 0, tileWidth * level.getWidth(), tileHeight * level.getHeight());

            // draw tiles
            for (int y = 0; y < level.getHeight(); y++) {
                for (int x = 0; x < level.getWidth(); x++) {
                    Tile t = level.tileAt(x, y);
                    if (t == null) {
                        t = Tile.EMPTY;
                    }

                    switch (t) {
                        case EMPTY:
                            args.g2d.setColor(COLOR_EMPTY);
                            break;
                        case DOOR_CLOSED:
                            args.g2d.setColor(COLOR_DOOR_CLOSED);
                            break;
                        case DOOR_OPEN:
                            args.g2d.setColor(COLOR_DOOR_OPEN);
                            break;
                        case WALL:
                            args.g2d.setColor(COLOR_WALL);
                            break;
                    }

                    args.g2d.fillRect(x * tileWidth + BORDER_WIDTH,
                            y * tileHeight + BORDER_WIDTH,
                            tileContentWidth,
                            tileContentHeight);
                }
            }
            int i = 0;
            if (level.getStartPositions() != null) {
                // draw starts
                i = 0;
                for (Point pos : level.getStartPositions()) {

                    AffineTransform tform = new AffineTransform();

                    tform.translate(pos.getX() * tileWidth + START_MARGIN,
                            pos.getY() * tileHeight + START_MARGIN);

                    tform.scale((startWidth) / startImgWidth,
                            (startHeight) / startImgHeight);

                    args.g2d.drawImage(imgPlayerStart[i++], tform, null);
                }
            }

            // draw bait
            {
                AffineTransform tform = new AffineTransform();

                tform.translate(level.getBaitPosition().getX() * tileWidth + BAIT_MARGIN,
                        level.getBaitPosition().getY() * tileHeight + BAIT_MARGIN);

                tform.scale((baitWidth) / baitImgWidth,
                        (baitHeight) / baitImgHeight);

                args.g2d.drawImage(imgBait, tform, null);
            }

            // draw mice
            if (level.getMice() != null) {
                log.debug("Draw mice:" + level.getMice().size());
                //No mice -> dont draw mice
                i = 0;
                for (Mouse mouse : level.getMice()) {

                    AffineTransform tform = new AffineTransform();

                    tform.translate(mouse.getPosition().getX() * tileWidth + MOUSE_MARGIN,
                            mouse.getPosition().getY() * tileHeight + MOUSE_MARGIN);

                    tform.scale((mouseWidth) / mouseImgWidth,
                            (mouseHeight) / mouseImgHeight);

                    tform.translate(0.5 * mouseImgWidth,
                            0.5 * mouseImgHeight);

                    switch (mouse.getOrientation()) {
                        case NORTH:
                            tform.scale(-1, 1);
                            tform.rotate(Math.toRadians(90));
                            break;
                        case EAST:
                            tform.scale(-1, 1);
                            break;
                        case SOUTH:
                            tform.rotate(Math.toRadians(270));
                            break;
                        case WEST:
                            tform.rotate(0);
                            break;
                    }

                    tform.translate(-0.5 * mouseImgWidth,
                            -0.5 * mouseImgHeight);

                    args.g2d.drawImage(imgPlayerMouse[i++], tform, null);
                }
            }

        }

    }

    public Point mousePositionToTilePosition(int wndWidth, int wndHeight, Point mousePos) {
        if (level == null) {
            return null;
        }
        int tileWidth = wndWidth / level.getWidth();
        int tileHeight = wndHeight / level.getHeight();

        int x = (int) Math.floor(mousePos.getX() / tileWidth);
        int y = (int) Math.floor(mousePos.getY() / tileHeight);

        if (x < 0 || x >= level.getWidth()) {
            return null;
        }
        if (y < 0 || y >= level.getHeight()) {
            return null;
        }

        return new Point(x, y);
    }

    public void mouseClicked(MouseEvent e) {
        //ignored
    }

    public void mousePressed(MouseEvent e) {
        //ignored
    }
}
