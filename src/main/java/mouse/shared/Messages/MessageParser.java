package mouse.shared.Messages;

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

    private final List<MessageObserver> observers;

    public MessageParser(){
        this.observers = new ArrayList<MessageObserver>();
    }

    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    public void parseMessage(InputStream stream){
        Message.fromStream(stream).alertObservers(this.observers);
    }
}
