package forms;

import cards.*;
import player.Player;
import player.Status;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
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
    private JLabel enemyLife1Label;
    private JLabel enemyLife2Label;
    private JLabel playerLife1Label;
    private JLabel playerLife2Label;
    private JPanel enemyLifes;
    private JPanel playerLifes;
    private JLabel enemyStatusLabel;

    private static final double MIN_MULT_COEF = 0.5;
    private static final double MAX_MULT_COEF = 2;

    private Player player = new Player();
    private Player enemy = new Player();

    public Field() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        submitButton.setEnabled(false);
        showRemovedCardsButton.setEnabled(false);

        enemyStatusLabel.setText("Enemy are waiting");

        studScore.setText("0");
        teachScore.setText("0");
        resScore.setText("0");

        enemyStudScore.setText("0");
        enemyTeachScore.setText("0");
        enemyResScore.setText("0");

        prechoiceLabel.setIcon(null);

        kingLabel.setIcon(player.getKing().getIcon());

        enemyKingLabel.setIcon(enemy.getKing().getIcon());

        final ImageIcon ruby = new ImageIcon(ImageIO.read(new File("src/images/ruby.jpg")));
        playerLife1Label.setIcon(ruby);
        playerLife2Label.setIcon(ruby);
        enemyLife1Label.setIcon(ruby);
        enemyLife2Label.setIcon(ruby);

        pack.setLayout(new GridLayout());
        students.setLayout(new GridLayout());
        teachers.setLayout(new GridLayout());

        enemyStudents.setLayout(new GridLayout());
        enemyTeachers.setLayout(new GridLayout());

        for (int i = 0; i < player.getReserve().size(); i++) {
            final JButton button = new JButton();
            final AbstractCard card = player.getReserve().get(i);
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            pack.add(button);
            pack.revalidate();
            button.addActionListener(e -> prechoice(card.getIcon(), card.getId(), true));
        }

        submitButton.addActionListener(e -> {
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
        });

        finishButton.addActionListener(e -> {
            try {
                finishRound();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        showRemovedCardsButton.addActionListener(e -> showRemovedCards(false));
    }

    public Player getPlayer() {
        return player;
    }

    public Player getEnemy() {
        return enemy;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void heal(Player player) throws IOException {

        //Не баг, а фича!
        //Предусмотреть воскрешение лекаря
        //Бесконечное добавление карт!!!

        if (Objects.equals(player, this.player)) {
            showRemovedCards(true);
            return;
        }
        final int size = enemy.getRemovedCards().size();
        if (size != 0) {
            final Random random = new Random();
            final Card card = enemy.getRemovedCards().get(random.nextInt(size));
            final Card clone = new Card(card);
            enemy.removeRemovedCard(card);
            move(this.enemy, this.player, clone, -1);
        }
    }

    private void showRemovedCards(boolean buttonRequired) {
        final RemovedCards dialog = new RemovedCards(this, buttonRequired);
        dialog.pack();
        dialog.setVisible(true);
        rePaint();
    }

    private void submit() throws IOException {
        int i = 0;
        final int id = Integer.parseInt(prechoiceLabel.getName());
        final ArrayList<AbstractCard> cards = player.getReserve();
        for (; i < pack.getComponentCount(); i++) {
            final AbstractCard card = cards.get(i);
            if (id == card.getId()) {
                move(player, enemy, card, i);
                System.out.println(card);
                break; // Иначе выкладывает несколько одинаковых карт!!!
            }
        }
        player.removeCard(i);
        rePaint();
    }

    private void prechoice(Icon icon, int id, boolean key) {
        prechoiceLabel.setIcon(icon);
        prechoiceLabel.setName(String.valueOf(id));

        if (key) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void setMood(int index, Player player, Player enemy) {
        final MoralCard card = (MoralCard)player.getReserve().get(index);
        switch (card.getCardType()) {
            case grant:
                player.multStudents(MAX_MULT_COEF);
                break;
            case premium:
                player.multTeachers(MAX_MULT_COEF);
                break;
            case depreciation:
                enemy.multStudents(MIN_MULT_COEF);
                break;
            case reprimand:
                enemy.multTeachers(MIN_MULT_COEF);
                break;
            default:
                break;
        }
    }

    private void enemyMove() throws IOException {
        if (enemy.getReserve().isEmpty()) {
            enemy.setStatus(Status.finished);
        }

        if (player.getStatus() == Status.finished) {
            enemy.setStatus(Status.finished);
        }

        if (enemy.getStatus() == Status.finished) {
            summarazingRound();
            return;
        }

        final Timer timer0 = new Timer(0, e -> enemyStatusLabel.setText("Enemy are mooving"));
        timer0.setRepeats(false);
        timer0.start();

        final Random random = new Random();
        final int index = random.nextInt(enemy.getReserve().size());
        final AbstractCard card = enemy.getReserve().get(index);

        System.out.println(card);
        System.out.println();

        prechoiceLabel.setIcon(card.getIcon());

        move(enemy, player, card, index);

        enemy.removeCard(index);

        final Timer timer = new Timer(1000, e -> {
            enemyStatusLabel.setText("Enemy are waiting");
            prechoiceLabel.setIcon(null);
            rePaint();
        });
        timer.setRepeats(false);

        timer.start();
    }

    @SuppressWarnings({"OverlyComplexMethod", "ParameterHidesMemberVariable", "EnumSwitchStatementWhichMissesCases"})
    public void move(Player player, Player enemy, AbstractCard card, int index) throws IOException {

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
            case nothing:
                break;
            case killer:
                kill();
                break;
            case inspire:
                player.inspire((Card)card);
                break;
            case spy:
                spy(player, enemy, (Card)card);
                break;
            case doctor:
                if (!player.getRemovedCards().isEmpty()) {
                    heal(player);
                }
                break;
        }
    }

    private void finishRound() throws IOException {

        //отправить информацию о завершении раунда соперника
        //получить информацию от соперника о завершении им раунда

        player.setStatus(Status.finished);

        if (enemy.getStatus() == Status.finished) {
            summarazingRound();
        }

        enemyMove();
    }

    @SuppressWarnings("deprecation")
    private void summarazingRound () {
        final int playerScore = player.getResScore();
        final int enemyScore = enemy.getResScore();
        if (playerScore > enemyScore) {
            player.incWinCount();
            enemy.incDefeatCount();
            JOptionPane.showMessageDialog(this, "You won this round");
        }
        if (playerScore < enemyScore) {
            player.incDefeatCount();
            enemy.incWinCount();
            JOptionPane.showMessageDialog(this, "You lose this round");
        }
        if (playerScore == enemyScore) {
            player.incWinCount();
            player.incDefeatCount();
            enemy.incWinCount();
            enemy.incDefeatCount();
            JOptionPane.showMessageDialog(this, "Dead heat in this round");
        }

        final int playerWinCount = player.getWinCount();
        final int playerDefeatCount = player.getDefeatCount();
        final int enemyDefeatCount = enemy.getDefeatCount();
        for (int i = 0; i < playerDefeatCount; i++) {
            this.playerLifes.getComponent(i).hide();
        }
        for (int i = 0; i < enemyDefeatCount; i++) {
            this.enemyLifes.getComponent(i).hide();
        }

        if ((playerWinCount == playerDefeatCount) && (playerDefeatCount == 2)) {
            JOptionPane.showMessageDialog(this, "Dead heat in this game");
            dispose();
        }
        if (playerWinCount == 2) {
            JOptionPane.showMessageDialog(this, "You won in this game");
            dispose();
        }
        if (playerDefeatCount == 2) {
            JOptionPane.showMessageDialog(this, "You lose in this game");
            dispose();
        }

        player.removeCardsFromField();
        enemy.removeCardsFromField();
        player.setStatus(Status.playing);
        enemy.setStatus(Status.playing);

        rePaint();
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void spy(Player player, Player enemy, Card card) throws IOException {
        if (card.getCardType() == CardType.student) {
            enemy.addStudent(card);
        } else {
            enemy.addTeacher(card);
        }
        player.addCards(2);
    }

    private void kill() {
        final int maxPlayerPower = player.getMaxPower();
        final int maxEnemyPower = enemy.getMaxPower();

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
        resScore.setText(String.valueOf(player.getResScore()));

        enemyStudScore.setText(String.valueOf(enemy.getStudScore()));
        enemyTeachScore.setText(String.valueOf(enemy.getTeachScore()));
        enemyResScore.setText(String.valueOf(enemy.getResScore()));

        prechoiceLabel.setIcon(null);

        System.out.println(enemy.getReserve().size());

        pack.removeAll();
        for (int i = 0; i < player.getReserve().size(); i++) {
            final AbstractCard card = player.getReserve().get(i);
            final JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), true));
            pack.add(button);
            pack.revalidate();
        }

        if (player.getRemovedCards().isEmpty()) {
            showRemovedCardsButton.setEnabled(false);
        } else {
            showRemovedCardsButton.setEnabled(true);
        }
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void rePaintStudents(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getStudents().size(); i++) {
            final Card card = player.getStudents().get(i);
            final JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            final JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getStudents().get(i).getPower()));
        }
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void rePaintTeachers(JPanel panel, Player player) {
        panel.removeAll();
        for (int i = 0; i < player.getTeachers().size(); i++) {
            final Card card = player.getTeachers().get(i);
            final JButton button = new JButton();
            button.setIcon(card.getIcon());
            button.setText(String.valueOf(card.getPower()));
            button.addActionListener(e -> prechoice(button.getIcon(), card.getId(), false));
            panel.add(button);
            panel.revalidate();
        }

        for (int i = 0; i < panel.getComponentCount(); i++) {
            final JButton button = (JButton)panel.getComponent(i);
            button.setText(String.valueOf(player.getTeachers().get(i).getPower()));
        }
    }
}
