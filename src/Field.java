import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import Card.*;
import player.*;


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

    private Player player = new Player();
    private Player enemy = new Player();


    public Field() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        submitButton.setEnabled(false);

        studScore.setText("0");
        teachScore.setText("0");
        resScore.setText("0");

        enemyStudScore.setText("0");
        enemyTeachScore.setText("0");
        enemyResScore.setText("0");

        BufferedImage no_image = ImageIO.read(new File("src/images/no_image.png"));
        ImageIcon no_avatar = new ImageIcon(no_image);
        prechoiceLabel.setIcon(no_avatar);

//        BufferedImage dean = ImageIO.read(new File("src/images/proletarskiy.png"));
//        ImageIcon king = new ImageIcon(dean);
        kingLabel.setIcon(player.getKing().getIcon());

        enemyKingLabel.setIcon(enemy.getKing().getIcon());

        pack.setLayout(new GridLayout());
        students.setLayout(new GridLayout());
        teachers.setLayout(new GridLayout());

        enemyStudents.setLayout(new GridLayout());
        enemyTeachers.setLayout(new GridLayout());

        for (int i = 0; i < player.getReserve().size(); i++) {
            JButton button = new JButton();
//            button.setIcon(card.getIcon());
            AbstractCard card = player.getReserve().get(i);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            pack.add(button);
            pack.revalidate();
            button.addActionListener(e -> prechoice(card.getIcon(), card.getId(), true));
        }

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
                enemyMove();
            }
        });

    }

    private void submit() {
        int id = Integer.parseInt(prechoiceLabel.getName());
        Vector<AbstractCard> cards = player.getReserve();
        for (int i = 0; i < pack.getComponentCount(); i++) {
            AbstractCard card = cards.get(i);
            if (id == card.getId()) {
                switch (card.getCardType()) {
                    case student:
                        player.addStudent((Card)card);
                        break;
                    case teacher:
                        player.addTeacher((Card)card);
                        break;
                    default:
                        setMood(i, player, enemy);
//                        return;
                        break;
                }

//                if (cards.get(i).getSkill() == Skill.killer) {
//                    kill();
//                }

                player.removeCard(i);
                pack.remove(pack.getComponent(i));
                pack.revalidate();

                break; // Иначе выкладывает несколько одинаковых карт!!!
            }
        }
        rePaint();
    }

    private void prechoice(Icon/*ImageIcon*/ icon, int id, boolean key) {
        prechoiceLabel.setIcon(icon);
        prechoiceLabel.setName(String.valueOf(id));

        if (key) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    private void setMood(int index, Player player, Player enemy) {
        MoralCard card = (MoralCard)player.getReserve().get(index);
        switch (card.getCardType()) {
            case grant:
                player.multStudents(2);
                break;
            case premium:
                player.multTeachers(2);
                break;
            case depreciation:
//                player.multStudents(0.5);
                enemy.multStudents(0.5);
                break;
            case reprimand:
//                player.multTeachers(0.5);
                enemy.multTeachers(0.5);
                break;
        }
    }

    private void enemyMove() {
        Random random = new Random();
//        Vector<AbstractCard> cards = enemy.getReserve();
        int index = random.nextInt(enemy.getReserve().size());
        AbstractCard card = enemy.getReserve().get(index);

        switch (card.getCardType()) {
            case student:
                enemy.addStudent((Card)card);
                break;
            case teacher:
                enemy.addTeacher((Card)card);
                break;
            default:
//                setEnemyMood(index);
                setMood(index, enemy, player);
                break;
        }
        enemy.removeCard(index);
        rePaint();
    }

    private void kill() {
        int maxPlayerPower = player.getMaxPower();
        int maxEnemyPower = enemy.getMaxPower();

        if (maxEnemyPower > maxPlayerPower) {
            enemy.killSelfStrongestCards(maxEnemyPower);
        }
        if (maxEnemyPower < maxPlayerPower) {
            player.killSelfStrongestCards(maxPlayerPower);
        }
        if (maxEnemyPower == maxPlayerPower) {
            enemy.killSelfStrongestCards(maxEnemyPower);
            player.killSelfStrongestCards(maxPlayerPower);
        }
    }

    private void rePaint() {
        rePaintStudents(students, player);
        rePaintTeachers(teachers, player);
        rePaintStudents(enemyStudents, enemy);
        rePaintTeachers(enemyTeachers, enemy);

        studScore.setText(String.valueOf(player.getStudScore()));
        teachScore.setText(String.valueOf(player.getTeachScore()));
        resScore.setText(String.valueOf(player.getResScore()));                //можно заменить на сумму двух предыдущих

        enemyStudScore.setText(String.valueOf(enemy.getStudScore()));
        enemyTeachScore.setText(String.valueOf(enemy.getTeachScore()));
        enemyResScore.setText(String.valueOf(enemy.getResScore()));

        prechoiceLabel.setIcon(null);
    }

    private void rePaintStudents(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getStudents().size(); i++) {
            Card card = player.getStudents().get(i);
            JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getStudents().get(i).getPower()));
        }
    }

    private void rePaintTeachers(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getTeachers().size(); i++) {
            Card card = player.getTeachers().get(i);
            JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getTeachers().get(i).getPower()));
        }
    }

//    public static void main(String[] args) throws IOException {
//        Field dialog = new Field();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
