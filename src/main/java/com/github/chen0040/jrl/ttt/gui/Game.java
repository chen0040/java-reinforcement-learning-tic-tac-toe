package com.github.chen0040.jrl.ttt.gui;

import com.github.chen0040.jrl.ttt.Board;
import com.github.chen0040.jrl.ttt.Position;
import com.github.chen0040.jrl.ttt.bots.QBot;
import com.github.chen0040.jrl.ttt.bots.SarsaBot;
import com.github.chen0040.jrl.ttt.dojos.DojoQ;
import com.github.chen0040.jrl.ttt.dojos.DojoSarsa;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.learning.sarsa.SarsaLearner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.function.Consumer;

public class Game extends JPanel {

    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 400;

    private final Board board;

    private QBot bot_q;
    private SarsaBot bot_sarsa;

    private QLearner model_q;
    private SarsaLearner model_sarsa;

    private static final Random random = new Random();
    private String message;

    public Game(Board board) {
        this.board = board;
        setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerAct(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void trainQ(Consumer<QLearner> callback) {
        new Thread(() -> {
            message = "Q Learn Started!";
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.bot_sarsa = null;
            this.model_q = DojoQ.trainAgainstNaiveBot(board, 30000);
            this.bot_q = new QBot(1, board, model_q);
            message = null;
            board.reset();
            repaint();
            callback.accept(this.model_q);
        }).start();
    }

    public void trainSarsa(Consumer<SarsaLearner> callback) {
        new Thread(() -> {
            message = "Q Learn Started!";
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.bot_q = null;
            this.model_sarsa = DojoSarsa.trainAgainstNaiveBot(board, 30000);
            this.bot_sarsa = new SarsaBot(1, board, model_sarsa);
            message = null;
            board.reset();
            repaint();
            callback.accept(this.model_sarsa);
        }).start();
    }

    public void newGame() {
        message = null;
        board.reset();
        if(bot_q != null) {
            bot_q.clearHistory();
        }
        else if(bot_sarsa != null){
            bot_sarsa.clearHistory();
        }
        if(bot_q != null || bot_sarsa != null) {
            if(random.nextBoolean()) {
                if(bot_q != null) {
                    bot_q.act();
                }
                else if(bot_sarsa != null){
                    bot_sarsa.act();
                }
            }
        }
        repaint();
    }

    private void playerAct(MouseEvent e) {

        if(bot_sarsa == null && bot_q == null){
            int stateCount = (int)Math.pow(3, board.size() * board.size());
            int actionCount = board.size() * board.size();

            model_sarsa = new SarsaLearner(stateCount, actionCount);
            bot_sarsa = new SarsaBot(1, board, model_sarsa);
        }


        int x = (int)(3.0 * e.getX() / SCREEN_WIDTH);
        int y = (int)(3.0 * e.getY() / SCREEN_HEIGHT);
        if(board.getCell(x, y) == 0) {
            board.move(new Position(x, y), 2);
        }

        if(bot_q != null) {
            bot_q.act();
        }
        else if(bot_sarsa != null){
            bot_sarsa.act();
        }
        if(!board.canBePlayed()){
            int winner = board.getWinner();
            if(winner == 1){
                message = "Bot Win!";
            } else if(winner == 2) {
                message = "You Win!";
            } else {
                message = "Game Ended!";
            }

            if(bot_q != null) {
                bot_q.updateStrategy();
                bot_q.clearHistory();
            }
            else if(bot_sarsa != null){
                bot_sarsa.updateStrategy();
                bot_sarsa.clearHistory();
            }
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.drawLine(0, 0, 0, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH / 3, 0, SCREEN_WIDTH / 3, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH * 2 / 3, 0, SCREEN_WIDTH * 2 / 3, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.drawLine(0, 0, SCREEN_WIDTH, 0);
        g.drawLine(0, SCREEN_HEIGHT/ 3, SCREEN_WIDTH, SCREEN_HEIGHT/ 3);
        g.drawLine(0, SCREEN_HEIGHT * 2 / 3, SCREEN_WIDTH, SCREEN_HEIGHT * 2 / 3);
        g.drawLine(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);



        int size = SCREEN_WIDTH / 6;

        for(int i=0; i < 3; ++i) {
            for(int j=0; j < 3; ++j) {
                int cell = board.getCell(i, j);
                int x = i * SCREEN_WIDTH / 3 + size / 2;
                int y = j * SCREEN_HEIGHT / 3 + size / 2;
                if(cell==0) {
                    continue;
                }
                if(cell == 1) {
                    g.setColor(Color.yellow);
                } else {
                    g.setColor(Color.black);
                }
                g.fillOval(x, y, size, size);
            }
        }

        if(message != null) {
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics fm = getFontMetrics(small);

            g.setColor(Color.red);
            g.setFont(small);

            g.drawString(message, (SCREEN_WIDTH - fm.stringWidth(message)) / 2,
                    60);
        }
    }
}
