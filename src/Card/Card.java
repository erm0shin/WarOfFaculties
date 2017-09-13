package Card;

import java.io.IOException;

public class Card extends AbstractCard{
    private int power;
    private Skill skill;

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    public Card() throws IOException {
        super();
        this.power = 0;
        this.skill = Skill.nothing;
    }

    public Card(String _name, String _src, CardType _cardType, int _power, Skill _skill) throws IOException {
        super(_name, _src, _cardType);
        this.power = _power;
        this.skill = _skill;
    }

    public Card(Card card) {
        super(card);
        this.power = card.power;
        this.skill = card.skill;
    }

    public void incPower() {
        power += 1;
    }

    public void multPower(double coefficient) {
        power = (int)(Math.round(coefficient * power));
        if (power < 1) power = 1;
    }
}
