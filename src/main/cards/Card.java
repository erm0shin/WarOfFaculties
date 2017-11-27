package main.cards;

import java.io.IOException;

public class Card extends AbstractCard{
    private int power;
    private int initPower;
    private Skill skill;

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getInitPower() {
        return initPower;
    }

    @Override
    public Skill getSkill() {
        return skill;
    }

    @SuppressWarnings("unused")
    public Card() throws IOException {
        super();
        this.power = 0;
        this.initPower = this.power;
        this.skill = Skill.nothing;
    }

    public void setPowerToInitial() {
        this.power = this.initPower;
    }

    public Card(String name, String src, CardType cardType, int power, Skill skill) throws IOException {
        super(name, src, cardType);
        this.power = power;
        this.initPower = this.power;
        this.skill = skill;
    }

    public Card(Card card) {
        super(card);
        this.power = card.power;
        this.initPower = this.power;
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
