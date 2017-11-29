package com.github.chen0040.jrl.ttt.bots;

import com.github.chen0040.jrl.ttt.Board;
import com.github.chen0040.jrl.ttt.Move;
import com.github.chen0040.jrl.ttt.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Bot {
    protected final int color;
    protected final Board board;

    protected List<Move> moves = new ArrayList<>();
    protected static Random random = new Random(42);

    protected double[] REWARD = new double[3];

    public Bot(int color, Board board) {
        this.color = color;
        this.board = board;

        REWARD[0] = -3;
        REWARD[1] = 100;
        REWARD[2] = -100;
    }

    public abstract void act();
    public abstract void updateStrategy();




    protected int getStrategyColor(int boardColor){
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

    public void clearHistory() {
        moves.clear();
    }


}
