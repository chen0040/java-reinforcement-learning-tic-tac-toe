package com.github.chen0040.jrl.ttt.dojos;

import com.github.chen0040.jrl.ttt.Board;
import com.github.chen0040.jrl.ttt.bots.NaiveBot;
import com.github.chen0040.jrl.ttt.bots.SarsaBot;
import com.github.chen0040.rl.learning.sarsa.SarsaLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DojoSarsa {

    private static final Logger logger = LoggerFactory.getLogger(DojoSarsa.class);

    public static double test(Board board, SarsaLearner model, int episodes) {

        SarsaBot bot1 = new SarsaBot(1, board, model);
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

    public static SarsaLearner trainAgainstSelf(Board board, int episodes) {


        int stateCount = (int)Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        SarsaLearner learner = new SarsaLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        SarsaBot bot1 = new SarsaBot(1, board, learner);
        SarsaBot bot2 =new SarsaBot(2, board, learner);

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

    public static SarsaLearner trainAgainstNaiveBot(Board board, int episodes) {


        int stateCount = (int)Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        SarsaLearner learner = new SarsaLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        SarsaBot bot1 = new SarsaBot(1, board, learner);
        NaiveBot bot2 =new NaiveBot(2, board);

        int wins = 0;

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
            //bot2.updateStrategy();
            logger.info("board: \n{}", board);
            wins += board.getWinner() == 1 ? 1 : 0;
            logger.info("success rate: {} %", (wins * 100) / (i+1));
        }

        return learner;
    }

    public static void main(String[] args) {

        Board board = new Board();
        SarsaLearner model = trainAgainstNaiveBot(board, 30000);


        double cap = test(board, model, 100);
        logger.info("SARSA Bot beats Random Bot in {} % of the games", cap * 100);
    }

}
