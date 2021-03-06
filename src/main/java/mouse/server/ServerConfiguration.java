package mouse.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: Simon
 * Date: 28.03.14
 */
public class ServerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServerConfiguration.class);

    public static final short DEFAULT_SERVER_PORT = 30330;
    public static final boolean DEFAULT_BROADCAST_ENABLED = true;
    public static final short DEFAULT_MULTICAST_LISTEN_PORT = 30331;
    public static final short DEFAULT_MULTICAST_PORT = 30332;
    public static final String DEFAULT_MULTICAST_ADDRESS = "224.0.100.100";
    public static final int DEFAULT_MULTICAST_INTERVAL = 2000;
    public static final int DEFAULT_PLAYER_COUNT = 2;
    public static final int DEFAULT_TICK_INTERVAL = 1000;
    public static final String DEFAULT_LEVEL = "level1.txt";

    private static final String configurationFile = "mouse.properties";
    private static final String serverPortKey = "server.port";
    private static final String broadcastEnabledKey = "server.lobbyBroadcast";
    private static final String multicastListenPortKey = "server.lobbyBroadcast.listenPort";
    private static final String multicastPortKey = "server.lobbyBroadcast.port";
    private static final String multicastAddressKey = "server.lobbyBroadcast.address";
    private static final String multicastIntervalKey = "server.lobbyBroadcast.interval";
    private static final String playerCountKey = "server.game.playerCount";
    private static final String tickIntervalKey = "server.game.tickInterval";    
    private static final String levelKey = "server.game.level";

    private short serverPort = DEFAULT_SERVER_PORT;
    private boolean broadcastEnabled = DEFAULT_BROADCAST_ENABLED;
    private short multicastListenPort = DEFAULT_MULTICAST_LISTEN_PORT;
    private short multicastPort = DEFAULT_MULTICAST_PORT;
    private String multicastAddress = DEFAULT_MULTICAST_ADDRESS;
    private int multicastInterval = DEFAULT_MULTICAST_INTERVAL;
    private int playerCount = DEFAULT_PLAYER_COUNT;
    private int tickInterval = DEFAULT_TICK_INTERVAL;
    private String level = DEFAULT_LEVEL;

    public static ServerConfiguration load() {
        ServerConfiguration serverConfiguration = new ServerConfiguration();
        Properties serverProperties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = ServerConfiguration.class.getClassLoader().getResourceAsStream(configurationFile);
            if (inputStream == null) {
                log.warn("No {} found. Using default configuration.", configurationFile);
                return serverConfiguration;
            }

            serverProperties.load(inputStream);

            serverConfiguration.initializeFrom(serverProperties);
        } catch (IOException ex) {
            log.error("Invalid {}. Using default configuration.", configurationFile);
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch(IOException ex) { /* ignored */ }
            }
        }

        return serverConfiguration;
    }

    private void initializeFrom(Properties serverProperties) {
        log.info("Found {}. Initializing ServerConfiguration now.", configurationFile);

        serverPort = Short.valueOf(serverProperties.getProperty(serverPortKey));
        log.debug("'{}' has value '{}'.", serverPortKey, serverPort);

        broadcastEnabled = Boolean.valueOf(serverProperties.getProperty(broadcastEnabledKey));
        log.debug("'{}' has value '{}'.", broadcastEnabledKey, broadcastEnabled);

        multicastListenPort = Short.valueOf(serverProperties.getProperty(multicastListenPortKey));
        log.debug("'{}' has value '{}'.", multicastListenPortKey, multicastListenPort);

        multicastPort = Short.valueOf(serverProperties.getProperty(multicastPortKey));
        log.debug("'{}' has value '{}'.", multicastPortKey, multicastPort);

        multicastAddress = serverProperties.getProperty(multicastAddressKey);
        log.debug("'{}' has value '{}'.", multicastAddressKey, multicastAddress);

        multicastInterval = Integer.valueOf(serverProperties.getProperty(multicastIntervalKey));
        log.debug("'{}' has value '{}'.", multicastIntervalKey, multicastInterval);

        playerCount = Integer.valueOf(serverProperties.getProperty(playerCountKey));
        log.debug("'{}' has value '{}'.", playerCountKey, playerCount);

        tickInterval = Integer.valueOf(serverProperties.getProperty(tickIntervalKey));
        log.debug("'{}' has value '{}'.", tickIntervalKey, tickInterval);
        
        level = serverProperties.getProperty(levelKey);
        log.debug("'{}' has value '{}'.", levelKey, level);
    }

    public short getServerPort() {
        return serverPort;
    }

    public boolean isBroadcastEnabled() {
        return broadcastEnabled;
    }

    public int getMulticastInterval() {
        return multicastInterval;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    public short getMulticastPort() {
        return multicastPort;
    }

    public short getMulticastListenPort() {
        return multicastListenPort;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getTickInterval() {
        return tickInterval;
    }
    public String getLevel() {
        return level;
    }
}
