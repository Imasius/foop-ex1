package mouse.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.TimerTask;

/**
 * User: Simon
 * Date: 26.03.14
 */
public class BroadcastTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(BroadcastTask.class);

    private static final short MULTICAST_LISTEN_PORT = 30329;

    private final String multicastAddress;
    private final short multicastPort;

    private DatagramSocket socket;

    private byte[] message;
    private InetAddress groupAddress;
    private DatagramPacket packet;

    public BroadcastTask(String multicastAddress, short multicastPort) {
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;

        initialize();
    }

    private void initialize() {
        log.debug("Initializing multicast task for broadcasting the server address.");

        try {
            socket = new DatagramSocket(MULTICAST_LISTEN_PORT);
        } catch (SocketException ex) {
            log.error("Unable to create multicast socket.", ex);
        }

        try {
            message = Inet4Address.getLocalHost().getHostAddress().getBytes();
            groupAddress = InetAddress.getByName(multicastAddress);
        } catch (UnknownHostException ex) {
            log.error("Unable to retrieve address.", ex);
        }

        packet = new DatagramPacket(message, message.length, groupAddress, multicastPort);
    }

    @Override
    public void run() {
        try {
            socket.send(packet);;
        } catch (IOException ex) {
            log.error("Unable to send broadcast.", ex);
        }
    }

    public void closeSocket() {
        socket.close();
    }

    public String getSentAddress() {
        return new String(message);
    }
}
