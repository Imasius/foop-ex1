package mouse.client;

import static com.oracle.nio.BufferSecrets.instance;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import mouse.gui.MouseJFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Simon Date: 21.03.14
 */
public class MouseClient {
    
    private static final Logger log = LoggerFactory.getLogger(MouseClient.class);
    
    public static void main(String[] args) {
        log.debug("Starting up client.");
        //TODO check for appropiate threading/jframe init/responsibilities
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //TODO: Make fullscreen/window switch
                boolean fullscreen = false;
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] gs = ge.getScreenDevices();
                MouseJFrame mjfGameFrame = new MouseJFrame();
                if (fullscreen) {
                    gs[0].setFullScreenWindow(mjfGameFrame);
                } else {
                    mjfGameFrame.setVisible(true);
                }
                
            }
        });
    }
}
