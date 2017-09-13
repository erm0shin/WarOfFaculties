import javax.swing.*;
import java.io.IOException;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton playButton;

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
    }

    private void play() throws IOException {
        Field dialog = new Field();
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
