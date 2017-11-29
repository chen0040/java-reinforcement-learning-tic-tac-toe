package com.github.chen0040.jrl.ttt;

public class Move {
    public int oldState;
    public int newState;
    public int action;
    public double reward;

    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
    }
}
