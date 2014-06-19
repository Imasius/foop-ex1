package mouse.shared.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The Message class is only responsible for serialization and deserialization
 * Created by Florian on 2014-04-12. Adapted by Kevein 2014-06-17
 */
public abstract class Message implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Message.class);

    public void writeToStream(ObjectOutputStream stream) throws IOException {
        if (stream == null) {
            log.error("ObjectOutputStream was null. No message send:" + this.getClass().getName());
            throw new IOException("ObjectOutputStream was null. No message send:" + this.getClass().getName());
        }
        ObjectOutput out = null;
        byte[] data = null;
        try {
            out = stream;
            out.writeObject(this);
            out.flush();
            stream.reset();
        } catch (IOException ex) {
            log.error("Unable to serialize message:" + this.getClass().getName() + " skip write", ex);
            throw new IOException("Unable to serialize message:" + this.getClass().getName() + " skip write");
        }
    }
}
