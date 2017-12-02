package com.github.chen0040.jrl.ttt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {
    private int x;
    private int y;
    public int BOUND;

    public Position() {

    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int toInteger(Board board) {
        return x * board.size() + y;
    }

    public static Position fromInteger(Board board, int intValue) {
        int x = (int)Math.floor((double)intValue / board.size());
        int y = intValue - x * board.size();
        return new Position(x, y);
    }
}
