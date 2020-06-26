package main.forms;

import main.game.Game;
import main.player.Fraction;

import javax.swing.*;
import java.io.IOException;

public class MainMenu extends JDialog {
    private JPanel contentPane;
    private JButton playButton;
    private JButton choiceFractionButton;
    private JLabel fractionLabel;
    private JButton infoButton;
    private Fraction fraction;


    @SuppressWarnings("unused")
    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
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

        infoButton.addActionListener(e -> showInfo());
    }

    private void choiceFraction() throws IOException {
        final ChoiceFraction dialog = new ChoiceFraction(this);
        dialog.pack();
        dialog.setVisible(true);
        if (fraction == Fraction.IU) {
            fractionLabel.setText("ИУ");
        }
    }

    private void showInfo() {
        JOptionPane.showMessageDialog(this, "Для победы в игре необходимо выиграть 2 раунда.\n" +
                "Для победы в раунде необходимо набрать очков больше, чем у соперника.\n" +
                "Как только захотите закончить раунд, нажмите кнопку 'Спасовать'.\n" +
                "Для того чтобы пустить карту в бой, выберете ее из своей колоды и нажмите 'Подтвердить'.\n" +
                "Для того чтобы увидеть свое бито, нажмите кнопку 'Показать бито'\n\n" +
                "В игре присутствуют карты с особыми возможностями:\n" +
                "Гуренко: воскрешает карту из бито\n" +
                "Кузовлев: карта выкладывается на стороне врага, но в вашу колоду добавляются 2 случайных карты\n" +
                "Иванов: убивает сильнейшую карту на поле\n" +
                "Иванова: прибавляет всем воюющим преподавателям 1 к силе\n" +
                "Губарь: прибавляет всем воюющим преподавателям 1 к силе\n" +
                "Тихомирова: прибавляет всем воюющим преподавателям 1 к силе\n" +
                "Технопарковец: прибавляет всем воюющим студентам 1 к силе\n" +
                "Двоечник: карта выкладывается на стороне врага, но в вашу колоду добавляются 2 случайных карты\n" +
                "Премия: удваивает силу воюющих преподавателей\n" +
                "Депремирование: уменьшает силу воюющих преподавателей вдвое\n" +
                "Стипендия: удваивает силу воюющих студентов\n" +
                "Выговор: уменьшает силу воюющих студентов вдвое");
    }

    @SuppressWarnings("unused")
    private void play() throws IOException {
        if ((fractionLabel.getText().equals("Не выбрано"))) {
            JOptionPane.showMessageDialog(this, "Выберете фракцию");
            return;
        }
        final Game game = new Game();
    }

    public static void main(String[] args) {
        final MainMenu dialog = new MainMenu();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}