package mouse.client.data;

import mouse.shared.LevelStructure;
import mouse.shared.Mouse;
import mouse.shared.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import mouse.shared.Door;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;

/**
 * A ClientLevel is a level which contains all information AND encapsulate
 * everything needed to draw the canvas
 *
 * Created by Florian on 2014-04-12. Changed to new shared structure 2014-06-18
 * by Kevin
 */
public class ClientLevel extends LevelStructure implements ServerToClientMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ClientLevel.class);
    //On client side
    private ArrayList<Mouse> mice;

    public ClientLevel(Tile[][] tiles, Point baitPosition, ArrayList<Point> startPositions) {
        super(tiles, baitPosition, startPositions);
    }

    @Override
    public int getHeight() {
        return tiles[0].length;
    }

    @Override
    public int getWidth() {
        return tiles.length;
    }

    @Override
    public Tile tileAt(int x, int y) {
        return tiles[x][y];
    }

    public ArrayList<Mouse> getMice() {
        return mice;
    }

    public void handleUpdateMice(ArrayList<Mouse> mice) {
        log.debug("Recieved update for " + mice.size() + " mice");
        //mice can be a subset
        if (this.mice == null) {
            //This is an initial update
            this.mice = mice;
        } else {
            //This is an subsqeuent update
            for (Mouse m : mice) {
                this.mice.set(m.getPlayerIndex(), m);
            }
        }
    }

    public void handleUpdateDoors(ArrayList<Door> doors) {
        log.debug("Recieved update for " + doors.size() + " doors");
        for (Door door : doors) {
            log.debug("Door was:" + tiles[door.getPosition().x][door.getPosition().y] + " and is now:" + (door.isClosed() ? "closed" : "open"));
            tiles[door.getPosition().x][door.getPosition().y] = door.isOpen() ? Tile.DOOR_OPEN : Tile.DOOR_CLOSED;
        }
    }

    @Override
    public void handleGameStart() {
        //At the moment we dont do anything
    }

    @Override
    public void handleGameOver(int winner) {
        JOptionPane.showMessageDialog(null, "Player " + winner + " has won. Starting next game!");
        //ignore, at the moment we dont do anything specific when the game ends, but it could make sense to
        //change the level or something
    }

    @Override
    public void handleUpdateLevel(LevelStructure level) {
        log.debug("Level has changed");
        this.startPositions = level.getStartPositions();
        this.baitPosition = level.getBaitPosition();
        this.tiles = level.getTiles();
    }

}
