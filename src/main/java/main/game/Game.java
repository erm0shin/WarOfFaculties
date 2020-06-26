package main.game;

import main.cards.*;
import main.forms.Field;
import main.player.Player;
import main.player.Status;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    private static final double MIN_MULT_COEF = 0.5;
    private static final double MAX_MULT_COEF = 2;

    private Player player;
    private Player enemy;
    private Field field;

    private Card cardToResurrection;
    private Card cardToRemoveFromRemoved;

    @SuppressWarnings("MagicNumber")
    public Game() throws IOException {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        player = new Player();
        enemy = new Player();
        field = new Field(player, enemy, this);
        field.setPreferredSize(new Dimension(screenSize.width, (int)(screenSize.height * 0.70)));
        field.pack();
        field.setVisible(true);
    }

    public Player getPlayer() {
        return player;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setCardToRemoveFromRemoved(Card cardToRemoveFromRemoved) {
        this.cardToRemoveFromRemoved = cardToRemoveFromRemoved;
    }

    public void setCardToResurrection(Card cardToResurrection) {
        this.cardToResurrection = cardToResurrection;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void heal(Player player) throws IOException {
        if (Objects.equals(player, this.player)) {
            field.showRemovedCards(true);
            this.player.removeRemovedCard(cardToRemoveFromRemoved);
            move(this.player, enemy, cardToResurrection, -1);
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

    public void myMove(int id) throws IOException {
        int i = 0;
        final List<AbstractCard> cards = player.getReserve();
        for (; i < player.getReserve().size(); i++) {
            final AbstractCard card = cards.get(i);
            if (id == card.getId()) {
                move(player, enemy, card, i);
                break;
            }
        }
        player.removeCard(i);
        field.rePaint(player, enemy);
    }

    public void enemyMove() throws IOException {
        if (enemy.getReserve().isEmpty() || (player.getStatus() == Status.finished)) {
            enemy.setStatus(Status.finished);
        }

        if ((enemy.getStatus() == Status.finished) && (player.getStatus() == Status.finished)) {
            this.summarazingRound();
            return;
        }

        if (enemy.getStatus() == Status.finished) {
            return;
        }

        final Random random = new Random();
        final int index = random.nextInt(enemy.getReserve().size());
        final AbstractCard card = enemy.getReserve().get(index);

        field.drawEnemyMove(card);
        move(enemy, player, card, index);
        enemy.removeCard(index);
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

    public void finishRound() throws IOException {
        player.setStatus(Status.finished);

        if (enemy.getStatus() == Status.finished) {
            summarazingRound();
        }

        enemyMove();
    }

    public void summarazingRound () {
        final int playerScore = player.getResScore();
        final int enemyScore = enemy.getResScore();
        String roundResult = null;
        if (playerScore > enemyScore) {
            player.incWinCount();
            enemy.incDefeatCount();
            roundResult = "Вы выиграли этот раунд";
        }
        if (playerScore < enemyScore) {
            player.incDefeatCount();
            enemy.incWinCount();
            roundResult = "Вы проиграли этот раунд";
        }
        if (playerScore == enemyScore) {
            player.incWinCount();
            player.incDefeatCount();
            enemy.incWinCount();
            enemy.incDefeatCount();
            roundResult = "Ничья в этом раунде";
        }

        final int playerWinCount = player.getWinCount();
        final int playerDefeatCount = player.getDefeatCount();
        final int enemyDefeatCount = enemy.getDefeatCount();

        player.removeCardsFromField();
        enemy.removeCardsFromField();
        player.setStatus(Status.playing);
        enemy.setStatus(Status.playing);

        field.finishRound(roundResult, playerWinCount, playerDefeatCount, enemyDefeatCount);
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
    }
}
