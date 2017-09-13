package Card;

import java.io.IOException;

public class MoralCard extends AbstractCard {
    @Override
    public int getPower() {
        return 0;
    }

    @Override
    public Skill getSkill() {
        return null;
    }

    public MoralCard() throws IOException {
        super();
    }

    public MoralCard(String _name, String _src, CardType _cardType) throws IOException {
        super(_name, _src, _cardType);
    }
}
