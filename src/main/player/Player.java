package main.player;

import main.cards.AbstractCard;
import main.cards.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class Player {
    private ArrayList<AbstractCard> reserve = new ArrayList<>();
    private ArrayList<Card> students = new ArrayList<>();
    private ArrayList<Card> teachers = new ArrayList<>();
    private ArrayList<Card> removedCards = new ArrayList<>();
    private Card king;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Fraction fraction;
    private int score;
    private int winCount;
    private int defeatCount;
    private Status status;
    private Generator generator = new Generator();

    public int getWinCount() {
        return winCount;
    }

    public int getDefeatCount() {
        return defeatCount;
    }

    public void incWinCount() {
        winCount++;
    }

    public void incDefeatCount() {
        defeatCount++;
    }

    public ArrayList<AbstractCard> getReserve() {
        return reserve;
    }

    public ArrayList<Card> getStudents() {
        return students;
    }

    public ArrayList<Card> getTeachers() {
        return teachers;
    }

    public ArrayList<Card> getRemovedCards() {
        return removedCards;
    }

    public Card getKing() {
        return king;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addStudent(Card student) {
        students.add(student);
    }

    public void addCards(int count) throws IOException {
        final Random random = new Random();
        for (int i = 0; i < count; i++) {
            final int choice = random.nextInt(3);
            switch (choice) {
                case 0:
                    reserve.addAll(generator.iuStudents(1));
                    break;
                case 1:
                    reserve.addAll(generator.iuTeachers(1));
                    break;
                case 2:
                    reserve.addAll(generator.moralCards(1));
                    break;
                default:
                    break;
            }
        }
    }

    public void addTeacher(Card teacher) {
        teachers.add(teacher);
    }

    public void removeCard(int index) {
        reserve.remove(index);
    }

    public void removeRemovedCard(Card card) {
        removedCards.remove(card);
    }

    public void removeCardsFromField() {
        for (Card card: students) {
            card.setPowerToInitial();
            final Card clone = new Card(card);
            removedCards.add(clone);
        }
        students.clear();
        for (Card card: teachers) {
            card.setPowerToInitial();
            final Card clone = new Card(card);
            removedCards.add(clone);
        }
        teachers.clear();
    }

    public int getStudScore() {
        int result = 0;
        for (Card card: students) {
            result += card.getPower();
        }
        return result;
    }

    public int getTeachScore() {
        int result = 0;
        for (Card card: teachers) {
            result += card.getPower();
        }
        return result;
    }

    public int getResScore() {
        score = getStudScore() + getTeachScore();
        return score;
    }

    public void multStudents(double coefficient) {
        for (Card card: students) {
            card.multPower(coefficient);
        }
    }

    public void multTeachers(double coefficient) {
        for (Card card: teachers) {
            card.multPower(coefficient);
        }
    }

    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    public void inspire(Card card/*CardType cardType*/) {
        switch (card.getCardType()/*cardType*/) {
            case student:
                for (Card student : students) {
                    if (!Objects.equals(student, card)) {
                        student.incPower();
                    }
                }
                break;
            case teacher:
                for (Card teacher : teachers) {
                    if (!Objects.equals(teacher, card)) {
                        teacher.incPower();
                    }
                }
                break;
            default:
                break;
        }
    }

    public int getMaxPower() {
        int maxPower = 0;
        for (Card card: students) {
            if (card.getPower() > maxPower) {
                maxPower = card.getPower();
            }
        }
        for (Card card: teachers) {
            if (card.getPower() > maxPower) {
                maxPower = card.getPower();
            }
        }
        return maxPower;
    }

    @SuppressWarnings("Duplicates")
    public void killSelfStrongestCards(int maxPower) {
        final Iterator<Card> iterStud = students.iterator();
        while (iterStud.hasNext()) {
            final Card card = iterStud.next();
            if (card.getPower() == maxPower) {
                card.setPowerToInitial();
                final Card clone = new Card(card);
                removedCards.add(clone);
                iterStud.remove();
            }
        }

        final Iterator<Card> iterTeach = teachers.iterator();
        while (iterTeach.hasNext()) {
            final Card card = iterTeach.next();
            if (card.getPower() == maxPower) {
                card.setPowerToInitial();
                final Card clone = new Card(card);
                removedCards.add(clone);
                iterTeach.remove();
            }
        }
    }

    public Player() throws IOException {
        fraction = Fraction.IU;
        winCount = 0;
        defeatCount = 0;
        king = generator.iuKing();
        reserve.addAll(generator.iuStudents(5));
        reserve.addAll(generator.iuTeachers(4));
        reserve.addAll(generator.moralCards(2));
        score = 0;
        status = Status.playing;
    }
}