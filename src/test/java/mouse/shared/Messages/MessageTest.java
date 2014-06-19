package mouse.shared.Messages;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import mouse.server.simulation.SimulationLevel;
import mouse.shared.Door;
import mouse.shared.LevelStructure;
import mouse.shared.Mouse;
import mouse.shared.Orientation;
import mouse.shared.Tile;
import mouse.shared.messages.clientToServer.ClientToServerMessage;
import mouse.shared.messages.clientToServer.ClientToServerMessageListener;
import mouse.shared.messages.clientToServer.RequestDoorStateMessage;
import mouse.shared.messages.serverToClient.GameOverMessage;
import mouse.shared.messages.serverToClient.GameStartMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessage;
import mouse.shared.messages.serverToClient.ServerToClientMessageListener;
import mouse.shared.messages.serverToClient.UpdateDoorsMessage;
import mouse.shared.messages.serverToClient.UpdateLevelMessage;
import mouse.shared.messages.serverToClient.UpdateMiceMessage;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Florian on 2014-04-12. Reworked for ServerClientStructure by Kevin
 * 1.06.2014
 */
public class MessageTest {

    //ClientToServer
    private RequestDoorStateMessage requestDoorStateMsg;
    //ServerToClient
    private GameStartMessage gameStartMsg;
    private GameOverMessage gameOverMsg;
    private UpdateLevelMessage updateLevelMsg;
    private UpdateMiceMessage updateMiceMsg;
    private UpdateDoorsMessage updateDoorsMsg;

    private Mouse m1;
    private Mouse m2;
    private Mouse m3;
    private Mouse m4;

    private Door door;

    private Point baitPosition;

    private ArrayList<Mouse> mice;
    private ArrayList<Door> doors;
    private ArrayList<Point> startPositions;
    private Tile[][] tiles;

    private ClientToServerMessageListener clientToServerMessageListener;
    private ServerToClientMessageListener serverToClientMessageListener;

    LevelStructure level;

    @Before
    public void setUp() throws Exception {
        //ClientToServerMessages
        door = spy(new Door(new Point(1, 1), true));
        requestDoorStateMsg = new RequestDoorStateMessage(door);

        //ServerToClientMessages
        gameOverMsg = new GameOverMessage(1);
        gameStartMsg = new GameStartMessage();

        tiles = new Tile[2][2];
        tiles[0][0] = Tile.EMPTY;
        tiles[0][1] = Tile.DOOR_OPEN;
        tiles[1][0] = Tile.WALL;
        tiles[1][1] = Tile.DOOR_CLOSED;

        baitPosition = new Point(1, 0);

        startPositions = new ArrayList<Point>();
        startPositions.add(new Point(0, 0));
        startPositions.add(new Point(1, 0));
        startPositions.add(new Point(6, 6));
        startPositions.add(new Point(3, 3));

        m1 = new Mouse(startPositions.get(0), Orientation.NORTH, 0);
        m2 = new Mouse(startPositions.get(1), Orientation.EAST, 1);
        m3 = new Mouse(startPositions.get(2), Orientation.SOUTH, 2);
        m4 = new Mouse(startPositions.get(3), Orientation.WEST, 3);

        baitPosition = new Point(0, 0);

        mice = new ArrayList<Mouse>();
        mice.add(m1);
        mice.add(m2);
        mice.add(m3);
        mice.add(m4);

        updateMiceMsg = new UpdateMiceMessage(mice);

        //The application uses FileLevel, but we just the abstract super class
        level = new SimulationLevel(new LevelStructure(tiles, baitPosition, startPositions) {
        });

        updateLevelMsg = new UpdateLevelMessage(level);

        doors = new ArrayList<Door>();
        doors.add(new Door(startPositions.get(0), true));
        doors.add(new Door(startPositions.get(1), false));
        updateDoorsMsg = new UpdateDoorsMessage(doors);

        serverToClientMessageListener = spy(new ServerToClientMessageListener() {

            public void handleUpdateMice(ArrayList<Mouse> mice) {
                //do nothing
            }

            public void handleUpdateDoors(ArrayList<Door> doors) {
                //do nothing
            }

            public void handleUpdateLevel(LevelStructure level) {
                //do nothing
            }

            public void handleGameStart() {
                //do nothing
            }

            public void handleGameOver() {
                //do nothing
            }

            public void handleGameOver(int winner) {
                //do nothing
            }
        });
        clientToServerMessageListener = spy(new ClientToServerMessageListener() {

            public void handleOpenDoor(Door door) {
                //do nothing
            }

            public void handleCloseDoor(Door door) {
                //do nothing
            }
        });
    }

    //Test ClientToServer Messages
    @Test
    public void testRequestDoorMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            requestDoorStateMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ClientToServerMessage m = null;
        try {
            m = (RequestDoorStateMessage) ClientToServerMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ClientToServerMessageListener> listeners = new ArrayList<ClientToServerMessageListener>();
        listeners.add(clientToServerMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(clientToServerMessageListener, times(1)).handleCloseDoor(((RequestDoorStateMessage) m).getDoor());
    }

    @Test
    public void testRequestDoorMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            requestDoorStateMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        RequestDoorStateMessage m = null;

        try {
            m = (RequestDoorStateMessage) ClientToServerMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //CheckIntegrity
        assertEquals(door.getPosition().x, m.getDoor().getPosition().x);
        assertEquals(door.getPosition().y, m.getDoor().getPosition().y);
        assertEquals(door.isClosed(), m.getDoor().isClosed());
    }

    //Test SeverToClient Messages
    @Test
    public void testGameStartMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            gameStartMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = null;
        try {
            m = (GameStartMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleGameStart();
    }

    @Test
    public void testGameStartMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            gameStartMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        GameStartMessage m = null;
        try {
            m = (GameStartMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
            //CheckIntegrity
            //No Data inside, but should be castable
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGameOverMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            gameOverMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = null;
        try {
            m = (GameOverMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleGameOver(1);
    }

    @Test
    public void testGameOverMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            gameOverMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        GameOverMessage m = null;
        try {
            m = (GameOverMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
            //CheckIntegrity
            //No Data inside, but should be castable
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testUpdateMiceMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateMiceMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = null;
        try {
            m = (UpdateMiceMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        UpdateMiceMessage mm = (UpdateMiceMessage) m;
        ArrayList<Mouse> miceList = mm.getMice();
        verify(serverToClientMessageListener, times(1)).handleUpdateMice(miceList);
    }

    @Test
    public void testUpdateMiceMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateMiceMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        UpdateMiceMessage m = null;

        try {
            m = (UpdateMiceMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //CheckIntegrity
        assertEquals(mice.size(), m.getMice().size());
        assertTrue(mice.containsAll(m.getMice()));
    }

    @Test
    public void testUpdateDoorsMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateDoorsMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = null;
        try {
            m = (UpdateDoorsMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleUpdateDoors(doors);
    }

    @Test
    public void testUpdateDoorsMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateDoorsMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        UpdateDoorsMessage m = null;
        try {
            m = (UpdateDoorsMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //CheckIntegrity
        assertEquals(doors.size(), m.getDoors().size());
        assertTrue(doors.containsAll(m.getDoors()));
    }

    @Test
    public void testUpdateLevelMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateLevelMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = null;
        try {
            m = (UpdateLevelMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleUpdateLevel(((UpdateLevelMessage) m).getLevel());
    }

    @Test
    public void testUpdateLevelIntegrityMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            updateLevelMsg.writeToStream(new ObjectOutputStream(out));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        UpdateLevelMessage m = null;
        try {
            m = (UpdateLevelMessage) ServerToClientMessage.fromStream(new ObjectInputStream(in));
        } catch (IOException ex) {
            Logger.getLogger(MessageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify Integrity
        LevelStructure l = m.getLevel();
        assertEquals(baitPosition.x, l.getBaitPosition().x);
        assertEquals(baitPosition.y, l.getBaitPosition().y);
        assertEquals(tiles[0][0], l.getTiles()[0][0]);
        assertEquals(tiles[0][1], l.getTiles()[0][1]);
        assertEquals(tiles[1][0], l.getTiles()[1][0]);
        assertEquals(tiles[1][1], l.getTiles()[1][1]);
        assertEquals(startPositions.size(), l.getStartPositions().size());
        assertTrue(startPositions.containsAll(l.getStartPositions()));

    }
}
