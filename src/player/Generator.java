package player;

import Card.*;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

//cards.add(new Card("asdf", "src/images/dvoechnik.png", CardType.student, 1, Skill.nothing));

public class Generator {
    Card IU_king() throws IOException {
        Card king = new Card("Proletarskiy", "src/images/proletarskiy.png", CardType.king, 0, Skill.nothing);
        return king;
    }

    Vector<Card> IU_students(int number) throws IOException {
        Vector<Card> pack = new Vector();

        pack.add(new Card("Первокурсник", "src/images/pervokursnik.png", CardType.student, 1, Skill.nothing));
        pack.add(new Card("Первокурсник", "src/images/pervokursnik.png", CardType.student, 1, Skill.nothing));
        pack.add(new Card("Первокурсник", "src/images/pervokursnik.png", CardType.student, 1, Skill.nothing));

        pack.add(new Card("Двоечник", "src/images/dvoechnik.png", CardType.student, 1, Skill.spy));
        pack.add(new Card("Двоечник", "src/images/dvoechnik.png", CardType.student, 1, Skill.spy));

        pack.add(new Card("Выпускник", "src/images/vypusknik.png", CardType.student, 4, Skill.nothing));
        pack.add(new Card("Выпускник", "src/images/vypusknik.png", CardType.student, 4, Skill.nothing));

        pack.add(new Card("Технопарковец", "src/images/technopark.png", CardType.student, 5, Skill.inspire));
        pack.add(new Card("Технопарковец", "src/images/technopark.png", CardType.student, 5, Skill.inspire));
        pack.add(new Card("Технопарковец", "src/images/technopark.png", CardType.student, 5, Skill.inspire));

        Vector<Card> result = new Vector();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
//            result.add(pack.get(random.nextInt(pack.size())));
            Card card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                Card clone = new Card(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }

    Vector<Card> IU_teachers(int number) throws IOException {
        Vector<Card> pack = new Vector();

        pack.add(new Card("Гуренко", "src/images/gurenko.png", CardType.teacher, 4, Skill.nothing));
        pack.add(new Card("Кузовлев", "src/images/kuzovlev.png", CardType.teacher, 4, Skill.spy));
        pack.add(new Card("Тихомирова", "src/images/tichomirova.png", CardType.teacher, 3, Skill.inspire));
        pack.add(new Card("Иванов", "src/images/ivanov.png", CardType.teacher, 5, Skill.killer));
        pack.add(new Card("Фомин", "src/images/fomin.png", CardType.teacher, 4, Skill.nothing));
        pack.add(new Card("Губарь", "src/images/gubar.png", CardType.teacher, 4, Skill.inspire));
        pack.add(new Card("Пугачев", "src/images/pugachev.png", CardType.teacher, 4, Skill.nothing));
        pack.add(new Card("Иванова", "src/images/ivanova.jpg", CardType.teacher, 6, Skill.inspire));

        Vector<Card> result = new Vector();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
//            result.add(pack.get(random.nextInt(pack.size())));
            Card card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                Card clone = new Card(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }

    Vector<MoralCard> MoralCards(int number) throws IOException {
        Vector<MoralCard> pack = new Vector();

        pack.add(new MoralCard("Депремирование", "src/images/reprimand.jpg", CardType.reprimand));
        pack.add(new MoralCard("Выговор", "src/images/depreciation.jpg", CardType.depreciation));
        pack.add(new MoralCard("Премия", "src/images/premium.jpg", CardType.premium));
        pack.add(new MoralCard("Стипендия", "src/images/grant.jpg", CardType.grant));

        Vector<MoralCard> result = new Vector();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
//            result.add(pack.get(random.nextInt(pack.size())));
            MoralCard card = pack.get(random.nextInt(pack.size()));
            if (result.contains(card)) {
                MoralCard clone = new MoralCard(card);
                result.add(clone);
            } else {
                result.add(card);
            }
        }

        return result;
    }
}
