package mouse.shared.messages.clientToServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import mouse.shared.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientToServer Messages have nothing in common with ServerToClient Message
 * but the serialization
 *
 * @author kevin_000
 */
public abstract class ClientToServerMessage extends Message {

    private static final Logger log = LoggerFactory.getLogger(ClientToServerMessage.class);

    public abstract void alertListeners(Iterable<? extends ClientToServerMessageListener> observers);

    public static ClientToServerMessage fromStream(InputStream stream) {
        ObjectInput in = null;
        ClientToServerMessage msg = null;
        try {
            in = new ObjectInputStream(stream);
            msg = (ClientToServerMessage) in.readObject();
        } catch (IOException ex) {
            log.error("Unable to deserialize message:", ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            log.error("Unable to deserialize message:", ex);
            System.exit(1);
        }
        return msg;
    }
}
