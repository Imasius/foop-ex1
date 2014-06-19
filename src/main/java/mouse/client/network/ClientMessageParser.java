package mouse.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import mouse.shared.messages.MessageParser;
import mouse.shared.messages.serverToClient.ServerToClientMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kevin_000
 */
public class ClientMessageParser extends MessageParser {

    private static final Logger log = LoggerFactory.getLogger(ClientMessageParser.class);
    private final List<ServerToClientMessageListener> listeners;

    public ClientMessageParser() {
        this.listeners = new ArrayList<ServerToClientMessageListener>();
    }

    public void addListener(ServerToClientMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void parseMessage(ObjectInputStream stream) throws IOException {
        if (stream == null) {
            log.error("Unable to parseMessage, stream was null");
            throw new IOException("Unable to parseMessage, stream was null");
        }
        ServerToClientMessage.fromStream(stream).alertListeners(this.listeners);
        //log.debug("Received Message and alerted:" + listeners.size());
    }

}