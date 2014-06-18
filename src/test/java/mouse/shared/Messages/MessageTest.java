package mouse.shared.Messages;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
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
import mouse.shared.messages.serverToClient.UpdateMiceMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Florian on 2014-04-12. Reworked for ServerClientStructure by Kevin
 * 1.06.2014
 */
public class MessageTest extends TestCase {

    //ClientToServer
    private RequestDoorStateMessage requestDoorStateMsg;
    //ServerToClient
    private GameStartMessage gameStartMsg;
    private GameOverMessage gameOverMsg;
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
    private List<Point> startPositions;
    private Tile[][] tiles;

    @Mock
    private ClientToServerMessageListener clientToServerMessageListener;
    @Mock
    private ServerToClientMessageListener serverToClientMessageListener;

    @Before
    @Override
    protected void setUp() throws Exception {
        //ClientToServerMessages
        door = spy(new Door(new Point(1, 1), true));
        requestDoorStateMsg = spy(new RequestDoorStateMessage(door));

        //ServerToClientMessages
        gameOverMsg = spy(new GameOverMessage());

        tiles = new Tile[2][2];
        tiles[0][0] = Tile.EMPTY;
        tiles[0][1] = Tile.DOOR_OPEN;
        tiles[1][0] = Tile.WALL;
        tiles[1][1] = Tile.DOOR_CLOSED;

        startPositions = new ArrayList<Point>();
        startPositions.add(new Point(0, 0));
        startPositions.add(new Point(1, 0));
        startPositions.add(new Point(6, 6));
        startPositions.add(new Point(3, 3));

        m1 = spy(new Mouse(startPositions.get(0), Orientation.NORTH, 0));
        m2 = spy(new Mouse(startPositions.get(1), Orientation.EAST, 1));
        m3 = spy(new Mouse(startPositions.get(2), Orientation.SOUTH, 2));
        m4 = spy(new Mouse(startPositions.get(3), Orientation.WEST, 3));

        baitPosition = new Point(0, 0);

        mice = new ArrayList<Mouse>();
        mice.add(m1);
        mice.add(m2);
        mice.add(m3);
        mice.add(m4);

        gameStartMsg = spy(new GameStartMessage(tiles, baitPosition, startPositions, mice));

        doors = new ArrayList<Door>();
        doors.add(new Door(startPositions.get(0), true));
        doors.add(new Door(startPositions.get(1), false));
        updateDoorsMsg = spy(new UpdateDoorsMessage(doors));

    }

    //Test ClientToServer Messages
    @Test
    private void testRequestDoorMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        requestDoorStateMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ClientToServerMessage m = ClientToServerMessage.fromStream(in);
        //HandleMessage
        List<ClientToServerMessageListener> listeners = new ArrayList<ClientToServerMessageListener>();
        listeners.add(clientToServerMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(clientToServerMessageListener, times(1)).handleCloseDoor(door);
    }

    @Test
    private void testRequestDoorMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        requestDoorStateMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        RequestDoorStateMessage m = (RequestDoorStateMessage) ClientToServerMessage.fromStream(in);
        //CheckIntegrity
        assertEquals(door, m.getDoor());
    }

    //Test SeverToClient Messages
    @Test
    private void testGameStartMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gameStartMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = ServerToClientMessage.fromStream(in);
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleGameStart(tiles, baitPosition, startPositions, mice);
    }

    @Test
    private void testGameStartMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gameStartMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        GameStartMessage m = (GameStartMessage) ServerToClientMessage.fromStream(in);
        //CheckIntegrity
        assertEquals(baitPosition, m.getBaitPosition());
        assertEquals(mice.size(), m.getMice().size());
        assertTrue(mice.containsAll(m.getMice()));
        assertEquals(tiles[0][0], m.getTiles()[0][0]);
        assertEquals(tiles[0][1], m.getTiles()[0][1]);
        assertEquals(tiles[1][0], m.getTiles()[1][0]);
        assertEquals(tiles[1][1], m.getTiles()[1][1]);
        assertEquals(startPositions.size(), m.getStartPositions().size());
        assertTrue(startPositions.containsAll(m.getStartPositions()));
    }

    @Test
    private void testGameOverMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gameOverMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = ServerToClientMessage.fromStream(in);
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleGameOver();
    }

    @Test
    private void testGameOverMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gameOverMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        GameOverMessage m = (GameOverMessage) ServerToClientMessage.fromStream(in);
        //CheckIntegrity
        //No Data inside, but should be castable
    }

    @Test
    private void testUpdateMiceMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        updateMiceMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = ServerToClientMessage.fromStream(in);
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleUpdateMice(mice);
    }

    @Test
    private void testUpdateMiceMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        updateMiceMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        UpdateMiceMessage m = (UpdateMiceMessage) ServerToClientMessage.fromStream(in);
        //CheckIntegrity
        assertEquals(mice.size(), m.getMice().size());
        assertTrue(mice.containsAll(m.getMice()));
    }

    @Test
    private void testUpdateDoorsMessage() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        updateDoorsMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ServerToClientMessage m = ServerToClientMessage.fromStream(in);
        //HandleMessage
        List<ServerToClientMessageListener> listeners = new ArrayList<ServerToClientMessageListener>();
        listeners.add(serverToClientMessageListener);
        m.alertListeners(listeners);
        //Verify
        verify(serverToClientMessageListener, times(1)).handleUpdateDoors(doors);
    }

    @Test
    private void testUpdateDoorsMessageIntegrity() {
        //Write Message
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        updateDoorsMsg.writeToStream(out);
        //Read Message
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        UpdateDoorsMessage m = (UpdateDoorsMessage) ServerToClientMessage.fromStream(in);
        //CheckIntegrity
        assertEquals(doors.size(), m.getDoors().size());
        assertTrue(doors.containsAll(m.getDoors()));
    }

}
