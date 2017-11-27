package main.player;

import main.cards.Card;
import main.cards.CardType;
import main.cards.MoralCard;
import main.cards.Skill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class Generator {
    @SuppressWarnings("UnnecessaryLocalVariable")
    Card iuKing() throws IOException {
        final Card king = new Card("Proletarskiy", "src/static/proletarskiy.png", CardType.king, 0, Skill.nothing);
        return king;
    }

    ArrayList<Card> iuStudents(int number) throws IOException {
        final ArrayList<Card> pack = new ArrayList<>();

        pack.add(new Card("Первокурсник", "src/static/pervokursnik.png", CardType.student, 1, Skill.nothing));
        pack.add(new Card("Первокурсник", "src/static/pervokursnik.png", CardType.student, 1, Skill.nothing));
        pack.add(new Card("Первокурсник", "src/static/pervokursnik.png", CardType.student, 1, Skill.nothing));

        pack.add(new Card("Двоечник", "src/static/dvoechnik.jpg", CardType.student, 1, Skill.spy));
        pack.add(new Card("Двоечник", "src/static/dvoechnik.jpg", CardType.student, 1, Skill.spy));

        pack.add(new Card("Выпускник", "src/static/vypusknik.png", CardType.student, 4, Skill.nothing));
        pack.add(new Card("Выпускник", "src/static/vypusknik.png", CardType.student, 4, Skill.nothing));

        pack.add(new Card("Технопарковец", "src/static/technopark.png", CardType.student, 5, Skill.inspire));
        pack.add(new Card("Технопарковец", "src/static/technopark.png", CardType.student, 5, Skill.inspire));
        pack.add(new Card("Технопарковец", "src/static/technopark.png", CardType.student, 5, Skill.inspire));

        final ArrayList<Card> result = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < number; i++) {
            final Card card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                final Card clone = new Card(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }

    ArrayList<Card> iuTeachers(int number) throws IOException {
        final ArrayList<Card> pack = new ArrayList<>();

        pack.add(new Card("Гуренко", "src/static/gurenko.png", CardType.teacher, 4, Skill.doctor));
        pack.add(new Card("Кузовлев", "src/static/kuzovlev.png", CardType.teacher, 4, Skill.spy));
        pack.add(new Card("Тихомирова", "src/static/tichomirova.png", CardType.teacher, 3, Skill.inspire));
        pack.add(new Card("Иванов", "src/static/ivanov.png", CardType.teacher, 5, Skill.killer));
        pack.add(new Card("Фомин", "src/static/fomin.png", CardType.teacher, 4, Skill.nothing));
        pack.add(new Card("Губарь", "src/static/gubar.png", CardType.teacher, 4, Skill.inspire));
        pack.add(new Card("Пугачев", "src/static/pugachev.png", CardType.teacher, 4, Skill.nothing));
        pack.add(new Card("Иванова", "src/static/ivanova.jpg", CardType.teacher, 6, Skill.inspire));

        final ArrayList<Card> result = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < number; i++) {
            final Card card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                final Card clone = new Card(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }

    ArrayList<MoralCard> moralCards(int number) throws IOException {
        final ArrayList<MoralCard> pack = new ArrayList<>();

        pack.add(new MoralCard("Депремирование", "src/static/reprimand.jpg", CardType.reprimand));
        pack.add(new MoralCard("Выговор", "src/static/depreciation.jpg", CardType.depreciation));
        pack.add(new MoralCard("Премия", "src/static/premium.jpg", CardType.premium));
        pack.add(new MoralCard("Стипендия", "src/static/grant.jpg", CardType.grant));

        final ArrayList<MoralCard> result = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < number; i++) {
            final MoralCard card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                final MoralCard clone = new MoralCard(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }
}
