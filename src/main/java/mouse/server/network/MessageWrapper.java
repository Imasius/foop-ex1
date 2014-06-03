package mouse.server.network;

import mouse.shared.messages.Message;

/**
 * Created by Simon on 03.06.2014.
 */
public class MessageWrapper {

    private final Message message;
    private final ClientConnectionHandler client;

    public MessageWrapper(Message message, ClientConnectionHandler client) {
        this.message = message;
        this.client = client;
    }

    public Message getMessage() {
        return message;
    }

    public ClientConnectionHandler getClient() {
        return client;
    }
}
