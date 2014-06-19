package mouse.shared.messages.serverToClient;

import java.util.ArrayList;
import mouse.server.network.MouseServer;
import mouse.shared.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kevin_000
 */
public class UpdateMiceMessage extends ServerToClientMessage {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateMiceMessage.class);
    private final ArrayList<Mouse> mice;
    
    public UpdateMiceMessage(ArrayList<Mouse> mice) {
        this.mice = mice;
    }
    
    public ArrayList<Mouse> getMice() {
        return mice;
    }
    
    @Override
    public void alertListeners(Iterable<? extends ServerToClientMessageListener> observers) {
        log.debug("UpdateMiceMessage -> Alert listeners");
        for (Mouse m : mice) {
            log.debug(m.getPosition().x + "," + m.getPosition().y);
        }
        for (ServerToClientMessageListener observer : observers) {
            observer.handleUpdateMice(mice);
        }
    }
}
