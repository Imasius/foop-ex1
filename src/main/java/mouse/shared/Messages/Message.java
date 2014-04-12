package mouse.shared.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Florian on 2014-04-12.
 */
public abstract class Message {
    private static final Logger log = LoggerFactory.getLogger(Message.class);

    protected abstract void alertObservers(Iterable<MessageObserver> observers);

    public void writeToStream(OutputStream stream){
        ObjectOutput out = null;
        byte[] data = null;
        try {
            out = new ObjectOutputStream(stream);
            out.writeObject(this);
            out.flush();
        }
        catch (IOException ex) {
            log.error("Unable to serialize message.", ex);
            System.exit(1);
        }
        finally {
            if (out != null)
                try { out.close(); } catch(IOException ex) {}
        }
    }
    public static Message fromStream(InputStream stream){
        ObjectInput in = null;
        Message msg = null;
        try {
            in = new ObjectInputStream(stream);
            msg = (Message) in.readObject();
        }
        catch (IOException ex) {
            log.error("Unable to deserialize message.", ex);
            System.exit(1);
        }
        catch (ClassNotFoundException ex) {
            log.error("Unable to deserialize message.", ex);
            System.exit(1);
        }
        finally {
            if (in != null)
                try { in.close(); } catch(IOException ex) {}
        }

        return msg;
    }
}
