package mouse.client.cfg;

import java.util.*;

/**
 * Created by Florian on 2014-04-11.
 */
public class ClientConfiguration {
    private boolean isFullscreen;
    private short multicastPort;

    public ClientConfiguration() {
        this.isFullscreen = false;
        this.multicastPort = 30332;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }
    public short getMulticastPort() {
        return multicastPort;
    }

    public void parseCommandLine(String[] args){
        Dictionary<String, List<String>> cmds = new Hashtable<String, List<String>>();

        // convert command line args to more efficient data-structure
        {
            List<String> cmdArgs = null;

            for (String arg : args) {
                if (arg.startsWith("-")) {
                    String cmd = arg.substring(1).toLowerCase();
                    cmdArgs = cmds.get(cmd);
                    if (cmdArgs == null) {
                        cmdArgs = new ArrayList<String>();
                        cmds.put(cmd, cmdArgs);
                    }
                } else if (cmdArgs != null) {
                    cmdArgs.add(arg);
                }
            }
        }

        // parse commands
        {
            if (cmds.get("fullscreen") != null){
                isFullscreen = true;
            }
            if (cmds.get("windowed") != null){
                isFullscreen = false;
            }
            if (cmds.get("multicast-port") != null){
                multicastPort = Short.parseShort(cmds.get("multicast-port").get(0));
            }
        }
    }



    // if nothing else specified load default config
    public static final ClientConfiguration INSTANCE = new ClientConfiguration();
}
