package forms;

import javax.swing.*;

public class ChoiceEnemy extends JDialog {
    private JPanel contentPane;
    private JTextField enemyIPField;
    private JButton submitButton;
    private MainMenu menu;

    public ChoiceEnemy(MainMenu menu) {
        setContentPane(contentPane);
        setModal(true);

        this.menu = menu;

        submitButton.addActionListener(e -> submit());
    }

    private void submit() {
        menu.setEnemyIP(enemyIPField.getText());
        dispose();
    }
}