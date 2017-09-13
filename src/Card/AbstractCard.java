package Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//enum CardType { moral, king, student, teacher };
//enum MoralCardType { vygovor, depremirovanie, stipuha, premia };
//enum SpecialCardType { nothing, inspire, spy, killer };

public abstract class AbstractCard {
//    protected int type;
    protected ImageIcon icon;
    protected String name;
    protected int id;
    protected CardType cardType;

    protected static int count_id = 0;

    public abstract int getPower();

    public abstract Skill getSkill();

    @Override
    public String toString() {
        return "AbstractCard{" +
                "name='" + name + '\'' +
                '}';
    }

    public CardType getCardType() {
        return cardType;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public AbstractCard() throws IOException {
        BufferedImage image = ImageIO.read(new File("src/images/no_image.png"));
        this.icon = new ImageIcon(image);

        this.id = AbstractCard.count_id;
        AbstractCard.count_id++;

        this.name = "anonymous";

        this.cardType = CardType.student;
    }

    public AbstractCard(String _name, String _src, CardType _cardType) throws IOException {
        BufferedImage image = ImageIO.read(new File(_src));
        this.icon = new ImageIcon(image);

        this.id = AbstractCard.count_id;
        AbstractCard.count_id++;

        this.name = _name;

        this.cardType = _cardType;
    }

    public AbstractCard(AbstractCard card) {
        this.icon = card.icon;
        this.id = AbstractCard.count_id;
        AbstractCard.count_id++;
        this.name = new String(card.name);
        this.cardType = card.cardType;
    }
}
