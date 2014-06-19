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

    public static ServerToClientMessage fromStream(ObjectInputStream stream) {
        if (stream == null) {
            log.error("ObjectInputStream was null. No message read!");
            return null;
        }
        ServerToClientMessage msg = null;
        try {
            msg = (ServerToClientMessage) stream.readObject();
        } catch (IOException ex) {
            log.error("Unable to deserialize message.", ex);
        } catch (ClassNotFoundException ex) {
            log.error("Unable to deserialize message.", ex);
        }
        return msg;
    }
}
