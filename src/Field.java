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
            if (id == /*cards.get(i).getId()*/card.getId()) {
//                JButton button = new JButton();
//                button.setIcon(cards.get(i).getIcon());
//                button.setText(String.valueOf(cards.get(i).getPower()));
//                button.addActionListener(e -> prechoice(button.getIcon(), id, false));
                switch (/*cards.get(i).getCardType()*/card.getCardType()) {
                    case student:
//                        students.add(button);
//                        students.revalidate();

//                        player.addStudent((Card)cards.get(i));
                        player.addStudent((Card)card);
                        player.removeCard(i);

                        break;
//                    case teacher:
//                        teachers.add(button);
//                        teachers.revalidate();
//                        player.addTeacher((Card)cards.get(i));
//                        break;
//                    default:
//                        setMood(i);
//                        return;
//                        break;
                }

//                if (cards.get(i).getSkill() == Skill.killer) {
//                    kill();
//                }

                pack.remove(pack.getComponent(i));
                pack.revalidate();
//                player.removeCard(i);   //cards.remove(i);

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

    private void setMood(int index) {
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

        rePaint();

        pack.remove(pack.getComponent(index));
        pack.revalidate();
        player.removeCard(index);
    }

    private void enemyMove() {
        Random random = new Random();
        Vector<AbstractCard> cards = enemy.getReserve();
        int index = random.nextInt(cards.size());

//        System.out.println(cards.size());
//        System.out.println(index);
//        System.out.println();

        JButton button = new JButton();
        button.setIcon(cards.get(index).getIcon());
        button.setText(String.valueOf(cards.get(index).getPower()));
        button.addActionListener(e -> prechoice(button.getIcon(), /*cards.get(index).getId()*/-1, false));

        switch (cards.get(index).getCardType()) {
            case student:
                enemyStudents.add(button);
                enemyStudents.revalidate();
                enemy.addStudent((Card) cards.get(index));
                break;
            case teacher:
                enemyTeachers.add(button);
                enemyTeachers.revalidate();
                enemy.addTeacher((Card) cards.get(index));
                break;
            default:
                setEnemyMood(index);
                return;
//                break;
        }
        enemy.removeCard(index);   //cards.remove(i);
        rePaint();
    }

    private void setEnemyMood(int index) {
        MoralCard card = (MoralCard)enemy.getReserve().get(index);
        switch (card.getCardType()) {
            case grant:
                enemy.multStudents(2);
                break;
            case premium:
                enemy.multTeachers(2);
                break;
            case depreciation:
                player.multStudents(0.5);
                break;
            case reprimand:
                player.multTeachers(0.5);
                break;
        }
        rePaint();
        enemy.removeCard(index);
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
        students.removeAll();
        for (int i = 0; i < player.getStudents().size(); i++) {
            Card card = player.getStudents().get(i);
            JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), false));
            students.add(button);
            students.revalidate();
//            player.addStudent((Card)cards.get(i));
        }
        for (int i = 0; i < player.getStudents().size(); i++) {
            System.out.println(player.getStudents().get(i));
        }
        System.out.println();


        for (int i = 0; i < students.getComponentCount(); i++) {
            JButton button = (JButton)students.getComponent(i);
            button.setText(String.valueOf(player.getStudents().get(i).getPower()));
        }
        for (int i = 0; i < teachers.getComponentCount(); i++) {
            JButton button = (JButton)teachers.getComponent(i);
            button.setText(String.valueOf(player.getTeachers().get(i).getPower()));
        }

        for (int i = 0; i < enemyStudents.getComponentCount(); i++) {
            JButton button = (JButton)enemyStudents.getComponent(i);
            button.setText(String.valueOf(enemy.getStudents().get(i).getPower()));
        }
        for (int i = 0; i < enemyTeachers.getComponentCount(); i++) {
            JButton button = (JButton)enemyTeachers.getComponent(i);
            button.setText(String.valueOf(enemy.getTeachers().get(i).getPower()));
        }

        studScore.setText(String.valueOf(player.getStudScore()));
        teachScore.setText(String.valueOf(player.getTeachScore()));
        resScore.setText(String.valueOf(player.getResScore()));                //можно заменить на сумму двух предыдущих

        enemyStudScore.setText(String.valueOf(enemy.getStudScore()));
        enemyTeachScore.setText(String.valueOf(enemy.getTeachScore()));
        enemyResScore.setText(String.valueOf(enemy.getResScore()));

        prechoiceLabel.setIcon(null);
    }

//    public static void main(String[] args) throws IOException {
//        Field dialog = new Field();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
