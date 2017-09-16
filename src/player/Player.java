package player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

import Card.*;

public class Player {
    private ArrayList<AbstractCard> reserve = new ArrayList<AbstractCard>();
    private ArrayList<Card> students = new ArrayList<Card>();
    private ArrayList<Card> teachers = new ArrayList<Card>();
    private Card king;
    private Fraction fraction;
    private int score;
    private Generator generator = new Generator();

    public ArrayList<AbstractCard> getReserve() {
        return reserve;
    }

    public ArrayList<Card> getStudents() {
        return students;
    }

    public ArrayList<Card> getTeachers() {
        return teachers;
    }

    public Card getKing() {
        return king;
    }

    public void addStudent(Card student) {
        students.add(student);
    }

    public void addCards(int count) throws IOException {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int choice = random.nextInt(3);
            switch (choice) {
                case 0:
                    reserve.addAll(generator.IU_students(1));
                    break;
                case 1:
                    reserve.addAll(generator.IU_teachers(1));
                    break;
                case 2:
                    reserve.addAll(generator.MoralCards(1));
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

    public void inspire(Card card/*CardType cardType*/) {
        switch (card.getCardType()/*cardType*/) {
            case student:
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i) != card) {
                        students.get(i).incPower();
                    }
                }
//                for (Card card: students) {
//                    card.incPower();
//                }
                break;
            case teacher:
                for (int i = 0; i < teachers.size(); i++) {
                    if (teachers.get(i) != card) {
                        teachers.get(i).incPower();
                    }
                }
//                for (Card card: teachers) {
//                    card.incPower();
//                }
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

    public void killSelfStrongestCards(int maxPower) {
//        Iterator<Card> iter = students.iterator();
//        while (iter.hasNext()) {
//            Card card = iter.next();
//            if (card.getPower() == maxPower) {
//                iter.remove();
//            }
//        }
        students.removeIf(card -> card.getPower() == maxPower);
        teachers.removeIf(card -> card.getPower() == maxPower);
    }

    public Player() throws IOException {
//        Generator generator = new Generator();

        fraction = Fraction.IU;
        king = generator.IU_king();
        reserve.addAll(generator.IU_students(5));
        reserve.addAll(generator.IU_teachers(4));
        reserve.addAll(generator.MoralCards(2));
//        reserve.setSize(11);
//        for (int i = 0; i < 5; i++) {
//            reserve[i] = new Card(generator.IU_students(1));
//        }
        score = 0;
    }
}
