# java-reinforcement-learning-tic-tac-toe

Demo of reinforcement learning using tic-tac-toe with the [java-reinforcement-learning](https://github.com/chen0040/java-reinforcement-learning) package.

# Usage

### Q-Learn

The following create two Q-Learning bots that plays against each other on the tic-tac-toe game to a train a Q-Learn model:

```java
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
```

The following code uses the trained Q-Learn model to create a QBot (bot1) which plays against bot2 (which is a naive bot that randomly takes a possible action each step):

```java
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
```

The following code shows how to train and then test a Q-Learn model:

```java
Board board = new Board();
QLearner model = train(board, 30000);

double successRate = test(board, model, 1000);
logger.info("Q-Learn Bot beats Random Bot in {} % of the games being played", successRate * 100);
```

This sample code can be found in the DojoQ.java file in the project.

### SARSA (State-Action-Reward-State-Action)

The following create two SARSA bots that plays against each other on the tic-tac-toe game to a train a SARSA model:

```java
public static QLearner train(Board board, int episodes) {

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
```

The following code uses the trained SARSA model to create a SarsaBot (bot1) which plays against bot2 (which is a naive bot that randomly takes a possible action each step):

```java
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
```

The following code shows how to train and then test a SARSA model:

```java
Board board = new Board();
SarsaLearner model = train(board, 30000);

double successRate = test(board, model, 1000);
logger.info("SARSA Bot beats Random Bot in {} % of the games being played", successRate * 100);
```

This sample code can be found in the DojoSarsa.java file in the project.

