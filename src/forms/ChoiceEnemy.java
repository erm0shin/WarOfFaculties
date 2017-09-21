package forms;

import javax.swing.*;

public class ChoiceEnemy extends JDialog {
    private JPanel contentPane;
    private JTextField enemyIPField;
    private JButton submitButton;

    public ChoiceEnemy() {
        setContentPane(contentPane);
        setModal(true);

        submitButton.addActionListener(e -> submit());
    }

    private void submit() {
        MainMenu.setEnemyIP(enemyIPField.getText());
        dispose();
    }

    public static void main(String[] args) {
        final ChoiceEnemy dialog = new ChoiceEnemy();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
