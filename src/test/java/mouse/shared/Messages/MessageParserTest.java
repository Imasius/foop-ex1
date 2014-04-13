package mouse.shared.Messages;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import mouse.server.simulation.Orientation;
import mouse.shared.MouseState;
import mouse.shared.Tile;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Florian on 2014-04-12.
 */
public class MessageParserTest {
    abstract class TestMessageListener implements MessageListener {
        boolean worked;

        protected TestMessageListener() {
            this.worked = false;
        }

        public void setWorked(boolean worked) {
            this.worked = true;
        }

        public void evaluate(){
            assertTrue("Message not detected", this.worked);
        }
    }

    void messageTest(TestMessageListener listenerTest, Message msg){
        MessageParser parser = new MessageParser();

        parser.addListener(listenerTest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeToStream(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        parser.parseMessage(in);

        listenerTest.evaluate();
    }

    @Test
    public void gameOverMessageIntegrity() {
        TestMessageListener listenerTest = new TestMessageListener() {
            @Override public void onMouseMoved(int mouseIdx, MouseState newState) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { setWorked(true); }
            @Override public void onTryChangeDoorState(Point doorPosition, boolean tryClose) { }
            @Override public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) { }
        };
        messageTest(listenerTest, new GameOverMessage());
    }
    @Test
    public void mouseMoveMessageIntegrity() {
        TestMessageListener listenerTest = new TestMessageListener() {
            @Override public void onMouseMoved(int mouseIdx, MouseState newState) {
                setWorked(true);
                assertEquals(1, mouseIdx);
                assertEquals(new Point(2,3), newState.getPosition());
                assertEquals(Orientation.EAST, newState.getOrientation());
            }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { }
            @Override public void onTryChangeDoorState(Point doorPosition, boolean tryClose) { }
            @Override public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) { }
        };
        messageTest(listenerTest, new MouseMovedMessage(1, new MouseState(new Point(2,3),Orientation.EAST)));
    }
    @Test
    public void doorStateChangedMessageIntegrity() {
        TestMessageListener listenerTest = new TestMessageListener() {
            @Override public void onMouseMoved(int mouseIdx, MouseState newState) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) {
                setWorked(true);
                assertEquals(new Point(1,2), doorPosition);
                assertEquals(true, isClosed);
            }
            @Override public void onGameOver() { }
            @Override public void onTryChangeDoorState(Point doorPosition, boolean tryClose) { }
            @Override public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) { }
        };
        messageTest(listenerTest, new DoorStateChangedMessage(new Point(1,2),true));
    }
    @Test
    public void tryChangeDoorStateMessageIntegrity() {
        TestMessageListener listenerTest = new TestMessageListener() {
            @Override public void onMouseMoved(int mouseIdx, MouseState newState) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { }
            @Override public void onTryChangeDoorState(Point doorPosition, boolean tryClose) {
                setWorked(true);
                assertEquals(new Point(1,2), doorPosition);
                assertEquals(true, tryClose);
            }
            @Override public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) { }
        };
        messageTest(listenerTest, new TryChangeDoorStateMessage(new Point(1,2),true));
    }
    @Test
    public void gameStartMessageIntegrity() {
        TestMessageListener listenerTest = new TestMessageListener() {
            @Override public void onMouseMoved(int mouseIdx, MouseState newState) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { }
            @Override public void onTryChangeDoorState(Point doorPosition, boolean tryClose) { }
            @Override public void onGameStart(Tile[][] tiles, Point baitPosition, Collection<Point> startPositions, Collection<MouseState> mice) {
                setWorked(true);
                assertEquals(tiles.length, 1);
                assertEquals(tiles[0].length, 2);
                assertEquals(tiles[0][0], Tile.DOOR_OPEN);
                assertEquals(tiles[0][1], Tile.EMPTY);
                assertEquals(new Point(1,2), baitPosition);
                assertEquals(startPositions.size(), 1);
                assertEquals(startPositions.toArray()[0], new Point(3, 4));
                assertEquals(mice.size(), 1);
                assertEquals(mice.toArray()[0], new MouseState(new Point(5,6), Orientation.EAST));
            }
        };
        messageTest(listenerTest, new GameStartMessage(new Tile[][] { new Tile[] { Tile.DOOR_OPEN, Tile.EMPTY } }
                                                      , new Point(1,2)
                                                      , Arrays.asList(new Point(3, 4))
                                                      , Arrays.asList(new MouseState(new Point(5,6), Orientation.EAST)) ));
    }
}
