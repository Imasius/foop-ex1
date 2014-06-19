package mouse.shared.messages.clientToServer;

import java.io.IOException;
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

    public static ClientToServerMessage fromStream(ObjectInputStream stream) throws IOException {
        if (stream == null) {
            log.error("ObjectInputStream was null. No message read!");
            throw new IOException("ObjectInputStream was null. No message read!");
        }
        ClientToServerMessage msg = null;
        try {
            msg = (ClientToServerMessage) stream.readObject();
        } catch (IOException ex) {
            log.error("Unable to deserialize message:", ex);
            throw new IOException("Unable to deserialize message:");
        } catch (ClassNotFoundException ex) {
            log.error("Unable to deserialize message:", ex);
            throw new IOException("Unable to deserialize message:");
        }
        return msg;
    }
}
