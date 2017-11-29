package com.github.chen0040.jrl.ttt;

import com.github.chen0040.jrl.ttt.bots.NaiveBot;
import com.github.chen0040.jrl.ttt.bots.QBot;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dojo {

    private static final Logger logger = LoggerFactory.getLogger(Dojo.class);

    public static double test(Board board, QLearner model, int episodes) {

        QBot bot1 = new QBot(1, board, model);
        NaiveBot bot2 =new NaiveBot(2, board);

        int wins = 0;
        int loses = 0;
        for(int i=0; i < episodes; ++i) {
            bot1.clearHistory();
            bot2.clearHistory();

            logger.info("Iteration: {} / {}", (i+1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            int winner = board.getWinner();
            logger.info("Winner: {}", winner);
            wins += winner == 1 ? 1 : 0;
            loses += winner == 2 ? 1 : 0;
        }

        return wins * 1.0 / episodes;

    }

    public static QLearner train(Board board, int episodes) {


        int stateCount = (int)Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        QLearner learner = new QLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        QBot bot1 = new QBot(1, board, learner);
        QBot bot2 =new QBot(2, board, learner);

        for(int i=0; i < episodes; ++i) {
            bot1.clearHistory();
            bot2.clearHistory();

            logger.info("Iteration: {} / {}", (i+1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            logger.info("winner: {}", board.getWinner());
            bot1.updateStrategy();
            bot2.updateStrategy();
            logger.info("board: \n{}", board);
        }

        return learner;
    }

    public static void main(String[] args) {

        Board board = new Board();
        QLearner model = train(board, 30000);


        double cap = test(board, model, 1000);
        logger.info("Q-Learn Bot beats Random Bot in {} % of the games that have a winner", cap * 100);
    }

}
