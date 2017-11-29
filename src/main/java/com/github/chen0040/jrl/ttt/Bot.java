package com.github.chen0040.jrl.ttt;

import com.github.chen0040.rl.actionselection.SoftMaxActionSelectionStrategy;
import com.github.chen0040.rl.learning.qlearn.QAgent;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Bot {
    private final int color;
    private final Board board;
    private final QLearner agent;
    private int stateCount;
    private int actionCount;
    private List<Move> moves = new ArrayList<>();
    private static Random random = new Random(42);

    private double[] REWARD = new double[3];

    public Bot(int color, Board board, QLearner learner) {
        this.agent = learner;

        this.color = color;
        this.board = board;

        REWARD[0] = -3;
        REWARD[1] = 100;
        REWARD[2] = -100;
    }



    private int getStrategyColor(int boardColor){
        if(boardColor == color) return 1;
        else if(boardColor == 0) return 0;
        else return 2;
    }

    public int getState() {
        int state = 0;
        for(int i=0; i < board.size(); ++i) {
            for(int j=0; j < board.size(); ++j) {
                int strategyColor = getStrategyColor(board.getCell(i, j));
                state = state * 3 + strategyColor;
            }
        }
        return state;
    }

    public Set<Integer> getPossibleActions() {
        List<Position> actions = board.getPossibleActions();
        return actions.stream().map(pos -> pos.toInteger(board)).collect(Collectors.toSet());
    }

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
