package com.github.chen0040.jrl.ttt;

import com.github.chen0040.jrl.ttt.dojos.DojoQ;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class QLearnUnitTest {

    private static final Logger logger = LoggerFactory.getLogger(QLearnUnitTest.class);

    @Test
    public void testQLearn_againstNaiveBot(){
        Board board = new Board();
        QLearner model = DojoQ.trainAgainstNaiveBot(board, 30000);

        double cap = DojoQ.test(board, model, 1000);
        logger.info("Q-Learn Bot beats Random Bot in {} % of the games", cap * 100);
        assertThat(cap).isGreaterThan(0.85);
    }

    @Test
    public void testQLearn_againstSelf(){
        Board board = new Board();
        QLearner model = DojoQ.trainAgainstSelf(board, 30000);


        double cap = DojoQ.test(board, model, 1000);
        logger.info("Q-Learn Bot beats Random Bot in {} % of the games", cap * 100);
    }
}
