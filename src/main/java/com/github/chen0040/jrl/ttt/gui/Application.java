package com.github.chen0040.jrl.ttt.gui;

import com.github.chen0040.jrl.ttt.Board;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    public Application() {

        initUI();
    }

    private void initUI() {

        Game game = new Game(new Board());
        add(game, BorderLayout.CENTER);

        JPanel commands = new JPanel(new BorderLayout());
        add(commands, BorderLayout.SOUTH);

        JButton btnStart = new JButton("Q-Train");
        btnStart.addActionListener(e -> {
            game.trainQ(learner -> {
                JOptionPane.showMessageDialog(Application.this,
                        "Training completed");
            });
        });
        commands.add(btnStart, BorderLayout.WEST);

        JButton btnAccelerateLearning = new JButton("SARSA Train");
        btnAccelerateLearning.addActionListener(e ->{
            game.trainSarsa(learner -> {
                JOptionPane.showMessageDialog(Application.this,
                        "Training completed");
            });
        });
        commands.add(btnAccelerateLearning, BorderLayout.CENTER);

        JButton btnStop = new JButton("New Game");
        btnStop.addActionListener(e -> game.newGame());
        commands.add(btnStop, BorderLayout.EAST);

        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(new Dimension(Game.SCREEN_WIDTH + 20, Game.SCREEN_HEIGHT + 80));


    }


    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });
    }
}
