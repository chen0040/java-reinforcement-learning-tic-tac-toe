package com.github.chen0040.jrl.ttt;

import java.util.HashSet;
import java.util.Set;

public class Move {
    public int oldState;
    public int newState;
    public int action;
    public double reward;
    public Set<Integer> possibleActions = new HashSet<>();

    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
    }

    public Move(int oldState, int action, int newState, double reward, Set<Integer> possibleActions) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
        this.possibleActions = possibleActions;
    }
}
