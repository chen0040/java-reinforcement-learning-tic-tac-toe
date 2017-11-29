package com.github.chen0040.jrl.ttt;

import com.github.chen0040.rl.utils.TupleTwo;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] board;
    public Board() {
        board = new int[3][];
        for(int i=0; i < board.length; ++i) {
            board[i] = new int[3];
        }
    }

    public Board(int size) {
        assert(size < 10);
        board = new int[size][];
        for(int i=0; i < board.length; ++i) {
            board[i] = new int[size];
        }
    }

    public int size() {
        return board.length;
    }

    public void move(Position pos, int who) {
        assert(who == 1 || who == 2);
        assert(board[pos.getX()][pos.getY()] == 0);
        board[pos.getX()][pos.getY()] = who;
    }


    public int getCell(Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    public int getCell(int x, int y) {
        return board[x][y];
    }

    public List<Position> getPossibleActions() {
        List<Position> result = new ArrayList<>();

        for(int i=0; i < board.length; ++i){
            for(int j=0; j < board[i].length; ++j) {
                if(board[i][j] == 0) {
                    result.add(new Position(i, j));
                }
            }
        }

        return result;
    }

    public int getWinner() {
        int rowCount = board.length;
        int colCount = board[0].length;

        int who;
        // check each column for winner
        for(int i=0; i < rowCount; ++i) {
            who = board[i][0];
            if(who == 0) continue;
            boolean done = true;
            for(int j=0; j < colCount; ++j) {
                if(board[i][j] != who) {
                    done = false;
                    break;
                }
            }
            if(done) return who;
        }

        // check each row for winner
        for(int i=0; i < colCount; ++i) {
            who = board[0][i];
            if(who == 0) continue;
            boolean done = true;
            for(int j=0; j < rowCount; ++j) {
                if(board[j][i] != who) {
                    done = false;
                    break;
                }
            }
            if(done) return who;
        }

        // check diagonal
        who = board[0][0];
        if(who != 0) {
            boolean done = true;
            for(int i=0; i < rowCount; ++i) {
                if(board[i][i] != who) {
                    done = false;
                    break;
                }
            }
            if(done) return who;
        }

        // check inverse diagonal
        who = board[0][colCount-1];
        if(who != 0) {
            boolean done = true;
            for(int i=0; i < rowCount; ++i) {
                if(board[i][rowCount-i-1] != who) {
                    done = false;
                    break;
                }
            }
            if(done) return who;
        }

        return 0;
    }

    public boolean canBePlayed() {
        if(getPossibleActions().isEmpty()) {
            return false;
        }
        if(getWinner() != 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < board.length; ++i) {
            for(int j=0; j < board[i].length; ++j) {
                if(j != 0) sb.append('\t');
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void reset() {
        for(int i=0; i < board.length; ++i){
            for(int j=0; j < board.length; ++j) {
                board[i][j] = 0;
            }
        }
    }
}
