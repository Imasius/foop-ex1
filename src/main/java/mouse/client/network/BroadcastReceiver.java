package mouse.client.network;

import mouse.client.cfg.ClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 2014-04-12.
 */
public class BroadcastReceiver implements Runnable  {
    private static final Logger log = LoggerFactory.getLogger(BroadcastReceiver.class);

    private final ClientConfiguration clientConfiguration;
    private final List<BroadcastReceiverListener> listeners;

    public BroadcastReceiver(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.listeners = new ArrayList<BroadcastReceiverListener>();
    }

    public void addListener(BroadcastReceiverListener listener) {
        listeners.add(listener);
    }
    public void removeListener(BroadcastReceiverListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void run() {
        log.debug("Initializing multicast task for broadcasting the server address.");

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(clientConfiguration.getMulticastPort());
        } catch (SocketException ex) {
            log.error("Unable to create multicast socket.", ex);
            System.exit(1);
        }

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            log.error("Unable to receive multicast-packet.", e);
            System.exit(1);
        }

        InetAddress address = null;
        try {
            address = Inet4Address.getByAddress(packet.getData());
        } catch (UnknownHostException e) {
            log.error("Received server-address is a unknown host.", e);
            System.exit(1);
        }

        ServerInfo serverInfo = new ServerInfo(address);

        for (BroadcastReceiverListener listener : listeners)
            listener.onServerFound(serverInfo);
    }
}
