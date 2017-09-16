import Card.*;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


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
    private JButton finishButton;
    private JButton showRemovedCardsButton;

    private Player player = new Player();
    private Player enemy = new Player();

    public Player getPlayer() {
        return player;
    }

    public Player getEnemy() {
        return enemy;
    }

    public Field() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        submitButton.setEnabled(false);
        showRemovedCardsButton.setEnabled(false);

        studScore.setText("0");
        teachScore.setText("0");
        resScore.setText("0");

        enemyStudScore.setText("0");
        enemyTeachScore.setText("0");
        enemyResScore.setText("0");

        prechoiceLabel.setIcon(null);

        kingLabel.setIcon(player.getKing().getIcon());

        enemyKingLabel.setIcon(enemy.getKing().getIcon());

        pack.setLayout(new GridLayout());
        students.setLayout(new GridLayout());
        teachers.setLayout(new GridLayout());

        enemyStudents.setLayout(new GridLayout());
        enemyTeachers.setLayout(new GridLayout());

        for (int i = 0; i < player.getReserve().size(); i++) {
            JButton button = new JButton();
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
                try {
                    submit();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    enemyMove();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                submitButton.setEnabled(false);
            }
        });

        finishButton.addActionListener(e -> finishRound());

        showRemovedCardsButton.addActionListener(e -> {
            try {
                showRemovedCards();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void showRemovedCards() throws IOException {
        RemovedCards dialog = new RemovedCards(/*player*/this);
        dialog.pack();
        dialog.setVisible(true);
//        System.out.println(cureCard);
        rePaint();
    }

    private void submit() throws IOException {
        int i = 0;
        int id = Integer.parseInt(prechoiceLabel.getName());
        ArrayList<AbstractCard> cards = player.getReserve();
        for (; i < pack.getComponentCount(); i++) {
            AbstractCard card = cards.get(i);
            if (id == card.getId()) {
                Move(player, enemy, card, i);
                System.out.println(card);
                break; // Иначе выкладывает несколько одинаковых карт!!!
            }
        }
        player.removeCard(i);
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
                enemy.multStudents(0.5);
                break;
            case reprimand:
                enemy.multTeachers(0.5);
                break;
        }
    }

    private void enemyMove() throws IOException {

        //Надо учесть ограничение на количество ходов!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        Random random = new Random();
        int index = random.nextInt(enemy.getReserve().size());
        AbstractCard card = enemy.getReserve().get(index);

        System.out.println(card);
        System.out.println();

        Move(enemy, player, card, index);

//        prechoiceLabel.setIcon(card.getIcon());
//        Thread.sleep(500);
//        prechoiceLabel.setIcon(null);

        enemy.removeCard(index);
        rePaint();
    }

    public void Move(Player player, Player enemy, AbstractCard card, int index) throws IOException {
        switch (card.getCardType()) {
            case student:
                if (card.getSkill() != Skill.spy) player.addStudent((Card)card);
                break;
            case teacher:
                if (card.getSkill() != Skill.spy) player.addTeacher((Card)card);
                break;
            default:
                setMood(index, player, enemy);
                break;
        }

        switch (card.getSkill()) {
            case killer:
                kill();
                break;
            case inspire:
                player.inspire((Card)card);
                break;
            case spy:
                spy(player, enemy, (Card)card);
        }
    }

    private void finishRound() {
        player.removeCardsFromField();
        enemy.removeCardsFromField();
        rePaint();
    }

    private void spy(Player player, Player enemy, Card card) throws IOException {
        if (card.getCardType() == CardType.student) {
            enemy.addStudent(card);
        } else {
            enemy.addTeacher(card);
        }
        player.addCards(2);
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

        rePaint();
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

        System.out.println(enemy.getReserve().size());

        pack.removeAll();
        for (int i = 0; i < player.getReserve().size(); i++) {
            AbstractCard card = player.getReserve().get(i);
            JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), true));
            pack.add(button);
            pack.revalidate();
        }

        if (player.getRemovedCards().size() == 0) {
            showRemovedCardsButton.setEnabled(false);
        } else {
            showRemovedCardsButton.setEnabled(true);
        }

//        System.out.println("Size of players removed cards is " + player.getRemovedCards().size());
//        for (int j = 0; j < player.getRemovedCards().size(); j++) {
//            System.out.println(player.getRemovedCards().get(j));
//        }
//        System.out.println("Size of players removed cards is " + enemy.getRemovedCards().size());
//        for (int j = 0; j < enemy.getRemovedCards().size(); j++) {
//            System.out.println(enemy.getRemovedCards().get(j));
//        }
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
