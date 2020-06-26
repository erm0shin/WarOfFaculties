package main.forms;

import main.player.Fraction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChoiceFraction extends JDialog {
    private JPanel contentPane;
    private JRadioButton iuButton;
    private JLabel iuLabel;
    private JButton submitButton;
    private MainMenu menu;

    public ChoiceFraction(MainMenu menu) throws IOException {
        setContentPane(contentPane);
        setModal(true);

        this.menu = menu;

        iuLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/static/proletarskiy.png"))));
        iuButton.setSelected(true);

        submitButton.addActionListener(e -> submit());
    }

    private void submit() {
        if (iuButton.isSelected()) {
            menu.setFraction(Fraction.IU);
            dispose();
        }
    }
}