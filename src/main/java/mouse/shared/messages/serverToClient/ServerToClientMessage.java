package mouse.shared.messages.serverToClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import mouse.shared.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServerToClient Messages have nothing in common with ClientToServer Message
 * but the serialization. Therefore each of them requires different types of
 * listeners to handle the Events. ServerToClient Listeners are handled by
 * ServerToClientMessageListeners
 *
 * @author kevin_000
 */
public abstract class ServerToClientMessage extends Message {

    private static final Logger log = LoggerFactory.getLogger(ServerToClientMessage.class);

    public abstract void alertListeners(Iterable<? extends ServerToClientMessageListener> observers);

    public static ServerToClientMessage fromStream(InputStream stream) {
        ObjectInput in = null;
        ServerToClientMessage msg = null;
        try {
            in = new ObjectInputStream(stream);
            msg = (ServerToClientMessage) in.readObject();
        } catch (IOException ex) {
            log.error("Unable to deserialize message.", ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            log.error("Unable to deserialize message.", ex);
            System.exit(1);
        }
        return msg;
    }
}
