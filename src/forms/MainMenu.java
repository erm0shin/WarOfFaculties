package forms;

import player.Fraction;

import javax.swing.*;
import java.io.IOException;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton playButton;
    private JButton choiceFractionButton;
    private JLabel fractionLabel;
    private JButton choiceEnemyButton;
    private JLabel enemyIPLabel;
    private static Fraction fraction;
    private static String enemyIP;

    @SuppressWarnings("unused")
    public static Fraction getFraction() {
        return fraction;
    }

    public static void setFraction(Fraction fraction) {
        MainMenu.fraction = fraction;
    }

    @SuppressWarnings("unused")
    public static String getEnemyIP() {
        return enemyIP;
    }

    public static void setEnemyIP(String enemyIP) {
        MainMenu.enemyIP = enemyIP;
    }

    public MainMenu() {
        setContentPane(contentPane);
        setModal(true);

        playButton.addActionListener(e -> {
            try {
                play();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        choiceFractionButton.addActionListener(e -> {
            try {
                choiceFraction();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        choiceEnemyButton.addActionListener(e -> choiceEnemy());
    }

    private void choiceFraction() throws IOException {
        final ChoiceFraction dialog = new ChoiceFraction();
        dialog.pack();
        dialog.setVisible(true);
        if (fraction == Fraction.IU) {
            fractionLabel.setText("IU");
        }
    }

    private void choiceEnemy() {
        final ChoiceEnemy dialog = new ChoiceEnemy();
        dialog.pack();
        dialog.setVisible(true);
        enemyIPLabel.setText(enemyIP);
    }

    private void play() throws IOException {
        final Field dialog = new Field();
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        final MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
