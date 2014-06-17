package mouse.shared.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The Message class is only responsible for serialization and deserialization
 * Created by Florian on 2014-04-12. Adapted by Kevein 2014-06-17
 */
public abstract class Message {

    private static final Logger log = LoggerFactory.getLogger(Message.class);

    public void writeToStream(OutputStream stream) {
        ObjectOutput out = null;
        byte[] data = null;
        try {
            out = new ObjectOutputStream(stream);
            out.writeObject(this);
            out.flush();
        } catch (IOException ex) {
            log.error("Unable to serialize message.", ex);
            System.exit(1);
        }
    }
}
