package com.github.chen0040.jrl.ttt.bots;

import com.github.chen0040.jrl.ttt.Board;
import com.github.chen0040.jrl.ttt.Move;
import com.github.chen0040.jrl.ttt.Position;
import com.github.chen0040.rl.learning.qlearn.QLearner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NaiveBot extends Bot {
    public NaiveBot(int color, Board board) {
        super(color, board);

    }

    @Override
    public void act() {
        int state = getState();

        Set<Integer> possibleActions = getPossibleActions();
        List<Integer> possibleActionList = new ArrayList<>(possibleActions);

        int action = -1;
        if(!possibleActions.isEmpty()) {
            action = possibleActionList.get(random.nextInt(possibleActionList.size()));
            Position pos = Position.fromInteger(board, action);
            board.move(pos, color);
        }

        if(action != -1) {
            int newState = getState();
            moves.add(new Move(state, action, newState, 0));
        }
    }

    @Override
    public void updateStrategy() {

    }

}
