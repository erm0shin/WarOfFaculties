package main.forms;

import main.cards.AbstractCard;
import main.cards.Card;
import main.player.Player;
import main.game.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Field extends JDialog {
    private JPanel contentPane;
    private JLabel prechoiceLabel;
    private JPanel pack;
    private JPanel students;
    private JPanel teachers;
    private JButton submitButton;
    private JLabel studScore;
    private JLabel teachScore;
    private JLabel kingLabel;
    private JLabel resScore;
    private JPanel enemyTeachers;
    private JPanel enemyStudents;
    private JLabel enemyTeachScore;
    private JLabel enemyStudScore;
    private JLabel enemyResScore;
    private JLabel enemyKingLabel;
    private JButton finishButton;
    private JButton showRemovedCardsButton;
    private JLabel enemyLife1Label;
    private JLabel enemyLife2Label;
    private JLabel playerLife1Label;
    private JLabel playerLife2Label;
    private JPanel enemyLifes;
    private JPanel playerLifes;
    private JLabel enemyStatusLabel;

    private Game game;

    public Field(Player player, Player enemy, Game game) throws IOException {
        this.game = game;

        setContentPane(contentPane);
        setModal(true);
        prechoiceLabel.setVerticalTextPosition(JLabel.BOTTOM);
        prechoiceLabel.setHorizontalTextPosition(JLabel.CENTER);
        submitButton.setEnabled(false);
        showRemovedCardsButton.setEnabled(false);

        enemyStatusLabel.setText("Противник ожидает Вас");

        studScore.setText("0");
        teachScore.setText("0");
        resScore.setText("0");

        enemyStudScore.setText("0");
        enemyTeachScore.setText("0");
        enemyResScore.setText("0");

        prechoiceLabel.setIcon(null);

        kingLabel.setIcon(player.getKing().getIcon());

        enemyKingLabel.setIcon(enemy.getKing().getIcon());

        final ImageIcon ruby = new ImageIcon(ImageIO.read(new File("src/static/ruby.jpg")));
        playerLife1Label.setIcon(ruby);
        playerLife2Label.setIcon(ruby);
        enemyLife1Label.setIcon(ruby);
        enemyLife2Label.setIcon(ruby);

        pack.setLayout(new GridLayout());
        students.setLayout(new GridLayout());
        teachers.setLayout(new GridLayout());

        enemyStudents.setLayout(new GridLayout());
        enemyTeachers.setLayout(new GridLayout());

        for (int i = 0; i < player.getReserve().size(); i++) {
            final JButton button = new JButton();
            final AbstractCard card = player.getReserve().get(i);
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            pack.add(button);
            pack.revalidate();
            button.addActionListener(e -> prechoice(card.getIcon(), card.getId(), card.getName(), true));
        }

        submitButton.addActionListener(e -> {
            try {
                game.myMove(Integer.parseInt(prechoiceLabel.getName()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                game.enemyMove();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            submitButton.setEnabled(false);
        });

        finishButton.addActionListener(e -> {
            try {
                game.finishRound();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        showRemovedCardsButton.addActionListener(e -> showRemovedCards(false));
    }

    public void showRemovedCards(boolean buttonRequired) {
        final RemovedCards dialog = new RemovedCards(game, buttonRequired);
        dialog.pack();
        dialog.setVisible(true);
        rePaint(game.getPlayer(), game.getEnemy());
    }

    private void prechoice(Icon icon, int id, String name, boolean key) {
        prechoiceLabel.setIcon(icon);
        prechoiceLabel.setName(String.valueOf(id));
        prechoiceLabel.setText(name);

        if (key) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    public void drawEnemyMove(AbstractCard card) throws IOException {
        final Timer timer0 = new Timer(0, e -> enemyStatusLabel.setText("Противник ходит"));
        timer0.setRepeats(false);
        timer0.start();

        prechoiceLabel.setIcon(card.getIcon());
        prechoiceLabel.setText(card.getName());

        final Timer timer = new Timer(1000, e -> {
            enemyStatusLabel.setText("Противник ожидает Вас");
            prechoiceLabel.setIcon(null);
            prechoiceLabel.setText("");
            rePaint(game.getPlayer(), game.getEnemy());
        });
        timer.setRepeats(false);

        timer.start();
    }

    @SuppressWarnings("deprecation")
    public void finishRound(String roundResult, int playerWinCount,
                            int playerDefeatCount, int enemyDefeatCount) {

        JOptionPane.showMessageDialog(this, roundResult);

        for (int i = 0; i < playerDefeatCount; i++) {
            this.playerLifes.getComponent(i).hide();
        }
        for (int i = 0; i < enemyDefeatCount; i++) {
            this.enemyLifes.getComponent(i).hide();
        }

        if ((playerWinCount == playerDefeatCount) && (playerDefeatCount == 2)) {
            JOptionPane.showMessageDialog(this, "Ничья в этой партии");
            dispose();
            return;
        }
        if (playerWinCount == 2) {
            JOptionPane.showMessageDialog(this, "Вы выиграли эту партию");
            dispose();
            return;
        }
        if (playerDefeatCount == 2) {
            JOptionPane.showMessageDialog(this, "Вы проиграли эту партию");
            dispose();
            return;
        }

        rePaint(game.getPlayer(), game.getEnemy());
    }

    public void rePaint(Player player, Player enemy) {
        rePaintStudents(students, player);
        rePaintTeachers(teachers, player);
        rePaintStudents(enemyStudents, enemy);
        rePaintTeachers(enemyTeachers, enemy);

        studScore.setText(String.valueOf(player.getStudScore()));
        teachScore.setText(String.valueOf(player.getTeachScore()));
        resScore.setText(String.valueOf(player.getResScore()));

        enemyStudScore.setText(String.valueOf(enemy.getStudScore()));
        enemyTeachScore.setText(String.valueOf(enemy.getTeachScore()));
        enemyResScore.setText(String.valueOf(enemy.getResScore()));

        prechoiceLabel.setIcon(null);
        prechoiceLabel.setText("");

        pack.removeAll();
        for (int i = 0; i < player.getReserve().size(); i++) {
            final AbstractCard card = player.getReserve().get(i);
            final JButton button = new JButton();
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), card.getName(), true));
            pack.add(button);
            pack.revalidate();
        }

        if (player.getRemovedCards().isEmpty()) {
            showRemovedCardsButton.setEnabled(false);
        } else {
            showRemovedCardsButton.setEnabled(true);
        }
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void rePaintStudents(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getStudents().size(); i++) {
            final Card card = player.getStudents().get(i);
            final JButton button = new JButton();
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), card.getName(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            final JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getStudents().get(i).getPower()));
        }
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void rePaintTeachers(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getTeachers().size(); i++) {
            final Card card = player.getTeachers().get(i);
            final JButton button = new JButton();
            button.setVerticalTextPosition(AbstractButton.BOTTOM);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), card.getName(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            final JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getTeachers().get(i).getPower()));
        }
    }
}