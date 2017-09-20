package cards;

import java.io.IOException;

public class MoralCard extends AbstractCard {
    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public int getInitPower() {
        return 0;
    }

    @Override
    public Skill getSkill() {
        return Skill.nothing;
    }

    @SuppressWarnings("unused")
    public MoralCard() throws IOException {
        super();
    }

    public MoralCard(String name, String src, CardType cardType) throws IOException {
        super(name, src, cardType);
    }

    public MoralCard(MoralCard card) {
        super(card);
    }
}
