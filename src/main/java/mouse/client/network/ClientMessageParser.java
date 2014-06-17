package mouse.client.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import mouse.shared.messages.MessageParser;
import mouse.shared.messages.serverToClient.ServerToClientMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;
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

    public void removeListener(ServerToClientMessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void parseMessage(InputStream stream) {
        ServerToClientMessage.fromStream(stream).alertListeners(this.listeners);
    }
}
