package com.github.chen0040.jrl.ttt;

import com.github.chen0040.rl.actionselection.SoftMaxActionSelectionStrategy;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dojo {

    private static final Logger logger = LoggerFactory.getLogger(Dojo.class);
    private QLearner learner;
    public void train(Board board, int episodes) {


        int stateCount = (int)Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        learner = new QLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        Bot bot1 = new Bot(1, board, learner);
        Bot bot2 =new Bot(2, board, learner);

        for(int i=0; i < episodes; ++i) {
            logger.info("Iteration: {} / {}", (i+1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            logger.info("winner: {}", board.getWinner());
            bot1.updateStrategy();
            bot2.updateStrategy();
        }
    }

    public static void main(String[] args) {
        Dojo dojo = new Dojo();
        Board board = new Board();
        dojo.train(board, 1000);
    }

}
