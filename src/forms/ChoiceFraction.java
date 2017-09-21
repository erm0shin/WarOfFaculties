package forms;

import player.Fraction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ChoiceFraction extends JDialog {
    private JPanel contentPane;
    private JRadioButton iuButton;
    private JLabel iuLabel;
    private JButton submitButton;

    public ChoiceFraction() throws IOException {
        setContentPane(contentPane);
        setModal(true);

        iuLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/images/proletarskiy.png"))));
        iuButton.setSelected(true);

        submitButton.addActionListener(e -> submit());
    }

    private void submit() {
        if (iuButton.isSelected()) {
            MainMenu.setFraction(Fraction.IU);
            dispose();
        }
    }
}
