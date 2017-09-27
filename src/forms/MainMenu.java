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
    private Fraction fraction;
    private String enemyIP;


    @SuppressWarnings("unused")
    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    @SuppressWarnings("unused")
    public String getEnemyIP() {
        return enemyIP;
    }

    public void setEnemyIP(String enemyIP) {
        this.enemyIP = enemyIP;
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
        final ChoiceFraction dialog = new ChoiceFraction(this);
        dialog.pack();
        dialog.setVisible(true);
        if (fraction == Fraction.IU) {
            fractionLabel.setText("IU");
        }
    }

    private void choiceEnemy() {
        final ChoiceEnemy dialog = new ChoiceEnemy(this);
        dialog.pack();
        dialog.setVisible(true);
        enemyIPLabel.setText(enemyIP);
    }

    private void play() throws IOException {
        if ((fractionLabel.getText().equals("Nothing")) || (enemyIPLabel.getText().equals("Nothing"))) {
            JOptionPane.showMessageDialog(this, "Enter the required data");
            return;
        }
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
