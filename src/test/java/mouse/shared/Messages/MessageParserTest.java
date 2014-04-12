package mouse.shared.Messages;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import junit.framework.TestCase;
import mouse.server.simulation.Orientation;
import mouse.shared.Level;
import mouse.shared.Tile;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Florian on 2014-04-12.
 */
public class MessageParserTest {
    abstract class MessageObserverTest implements MessageObserver {
        boolean worked;

        protected MessageObserverTest() {
            this.worked = false;
        }

        public void setWorked(boolean worked) {
            this.worked = true;
        }

        public void evaluate(){
            assertTrue("Message not detected", this.worked);
        }
    }

    void messageTest(MessageObserverTest observerTest, Message msg){
        MessageParser parser = new MessageParser();

        parser.addObserver(observerTest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeToStream(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        parser.parseMessage(in);

        observerTest.evaluate();
    }

    @Test
    public void gameOverMessageIntegrity() {
        MessageObserverTest observerTest = new MessageObserverTest() {
            @Override public void onMouseMoved(int mouseIdx, Point newPosition) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { setWorked(true); }
            @Override public void onGameStart(Level level) { }
        };
        messageTest(observerTest, new GameOverMessage());
    }
    @Test
    public void mouseMoveMessageIntegrity() {
        MessageObserverTest observerTest = new MessageObserverTest() {
            @Override public void onMouseMoved(int mouseIdx, Point newPosition) {
                setWorked(true);
                assertEquals(1, mouseIdx);
                assertEquals(new Point(2,3), newPosition);
            }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed) { }
            @Override public void onGameOver() { }
            @Override public void onGameStart(Level level) { }
        };
        messageTest(observerTest, new MouseMovedMessage(1, new Point(2,3)));
    }
    @Test
    public void doorStateChangedMessageIntegrity() {
        MessageObserverTest observerTest = new MessageObserverTest() {
            @Override public void onMouseMoved(int mouseIdx, Point newPosition) { }
            @Override public void onDoorStateChanged(Point doorPosition, boolean isClosed){
                setWorked(true);
                assertEquals(new Point(1,2), doorPosition);
                assertEquals(true, isClosed);
            }
            @Override public void onGameOver() { }
            @Override public void onGameStart(Level level) { }
        };
        messageTest(observerTest, new DoorStateChangedMessage(new Point(1,2),true));
    }
}
