package mouse.shared.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 2014-04-12.
 */
public class MessageParser {
    private static final Logger log = LoggerFactory.getLogger(MessageParser.class);

    private final List<MessageListener> listeners;

    public MessageParser(){
        this.listeners = new ArrayList<MessageListener>();
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }
    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    public void parseMessage(InputStream stream){
        Message.fromStream(stream).alertListeners(this.listeners);
    }
}
