package mouse.shared.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by Florian on 2014-04-12. Split into ClientServer by Kevin 20014-17-06
 */
public abstract class MessageParser {

    private static final Logger log = LoggerFactory.getLogger(MessageParser.class);

    public abstract void parseMessage(InputStream stream);
}
