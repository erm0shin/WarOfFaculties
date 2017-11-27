package main.cards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class AbstractCard {
    protected ImageIcon icon;
    protected String name;
    protected int id;
    protected CardType cardType;

    protected static int countId = 0;

    public abstract int getPower();

    @SuppressWarnings("unused")
    public abstract int getInitPower();

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

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public AbstractCard() throws IOException {
        final BufferedImage image = ImageIO.read(new File("src/static/no_image.png"));
        this.icon = new ImageIcon(image);

        this.id = AbstractCard.countId;
        AbstractCard.countId++;

        this.name = "anonymous";

        this.cardType = CardType.student;
    }

    public AbstractCard(String name, String src, CardType cardType) throws IOException {
        final BufferedImage image = ImageIO.read(new File(src));
        this.icon = new ImageIcon(image);

        this.id = AbstractCard.countId;
        AbstractCard.countId++;

        this.name = name;

        this.cardType = cardType;
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    public AbstractCard(AbstractCard card) {
        this.icon = card.icon;
        this.id = AbstractCard.countId;
        AbstractCard.countId++;
        this.name = new String(card.name);
        this.cardType = card.cardType;
    }
}
