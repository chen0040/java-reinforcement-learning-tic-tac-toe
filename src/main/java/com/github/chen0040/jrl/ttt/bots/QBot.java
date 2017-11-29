package com.github.chen0040.jrl.ttt.bots;

import com.github.chen0040.jrl.ttt.Board;
import com.github.chen0040.jrl.ttt.Move;
import com.github.chen0040.jrl.ttt.Position;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QBot extends Bot {
    private final QLearner agent;

    public QBot(int color, Board board, QLearner learner) {
        super(color, board);
        this.agent = learner;
    }

    @Override
    public void act() {
        int state = getState();

        Set<Integer> possibleActions = getPossibleActions();
        List<Integer> possibleActionList = new ArrayList<>(possibleActions);

        int action = -1;
        if(!possibleActions.isEmpty()) {
            IndexValue iv = agent.selectAction(state, possibleActions);
            action = iv.getIndex();
            double value = iv.getValue();

            if(value <= 0){
                action = possibleActionList.get(random.nextInt(possibleActionList.size()));
            }

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

        int winner = board.getWinner();
        int strategyColor = getStrategyColor(winner);

        double reward = REWARD[strategyColor];

        for(int i=moves.size()-1; i >=0; --i){
            Move move = moves.get(i);
            if(i == moves.size()-1) {
                move.reward = reward;
            }
            agent.update(move.oldState, move.action, move.newState, move.reward);
        }

    }
}
