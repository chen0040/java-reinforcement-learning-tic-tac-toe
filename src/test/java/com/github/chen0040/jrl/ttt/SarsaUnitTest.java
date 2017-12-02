package com.github.chen0040.jrl.ttt;

import com.github.chen0040.jrl.ttt.dojos.DojoQ;
import com.github.chen0040.jrl.ttt.dojos.DojoSarsa;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.learning.sarsa.SarsaLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SarsaUnitTest {

    private static final Logger logger = LoggerFactory.getLogger(SarsaUnitTest.class);

    @Test
    public void testSarsa_againstNaiveBot(){
        Board board = new Board();
        SarsaLearner model = DojoSarsa.trainAgainstNaiveBot(board, 30000);

        double cap = DojoSarsa.test(board, model, 1000);
        logger.info("SARSA Bot beats Random Bot in {} % of the games", cap * 100);
        assertThat(cap).isGreaterThan(0.85);
    }

    @Test
    public void testSarsa_againstSelf(){
        Board board = new Board();
        SarsaLearner model = DojoSarsa.trainAgainstSelf(board, 30000);


        double cap = DojoSarsa.test(board, model, 1000);
        logger.info("SARSA Bot beats Random Bot in {} % of the games", cap * 100);
    }
}
