/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouse.server.network.event;

/**
 *
 * @author Kevin Streicher <e1025890@student.tuwien.ac.at>
 */
public class GameOverEvent {

    private int winner;

    public GameOverEvent(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }
}
