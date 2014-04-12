package mouse.client.network;

import java.net.InetAddress;

/**
 * Created by Florian on 2014-04-12.
 */
public class ServerInfo {
    private InetAddress address;

    public ServerInfo(InetAddress address) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return address;
    }
}
