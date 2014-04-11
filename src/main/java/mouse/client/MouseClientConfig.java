package mouse.client;

import java.util.*;

/**
 * Created by Florian on 2014-04-11.
 */
public class MouseClientConfig {
    private boolean isFullscreen;

    public MouseClientConfig() {
        this.isFullscreen = false;
    }

    public boolean isFullscreen() {
        return isFullscreen;
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
        }
    }



    // if nothing else specified load default config
    public static final MouseClientConfig INSTANCE = new MouseClientConfig();
}
