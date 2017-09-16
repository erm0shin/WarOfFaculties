package Card;

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

    public MoralCard() throws IOException {
        super();
    }

    public MoralCard(String _name, String _src, CardType _cardType) throws IOException {
        super(_name, _src, _cardType);
    }

    public MoralCard(MoralCard card) {
        super(card);
    }
}
