package mouse.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;

/**
 * Created by Florian on 2014-04-12.
 */
public class ServerConnection implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ServerConnection.class);

    private final ServerInfo serverInfo;
    private final ClientMessageParser msgParser;
    private Socket socket;

    public ServerConnection(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
        this.msgParser = new ClientMessageParser();
    }

    public void addServerToClientMessageListener(ServerToClientMessageListener listener) {
        msgParser.addListener(listener);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverInfo.getAddress(), 30330);
        } catch (IOException ex) {
            log.error("Unable to create connection socket.", ex);
            System.exit(1);
        }

        try {
            while (true) {
                msgParser.parseMessage(socket.getInputStream());
            }
        } catch (IOException e) {
            log.debug("Shutting down connection.");
            try {
                socket.close();
            } catch (IOException ex) {
            }
            socket = null;
        }
    }

    public void sendMessage(ClientToServerMessage m) {
        try {
            m.writeToStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Unable to send message.", ex);
            System.exit(1);
        }
    }
}
