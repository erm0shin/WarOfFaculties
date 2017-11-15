package forms;

import cards.Card;
import game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RemovedCards extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JPanel removedCards;
    private int cardId;
    private Color defaultColor;
    private ArrayList<Card> cards;

    @SuppressWarnings("deprecation")
    public RemovedCards(Game game, boolean buttonRequired) {
        setContentPane(contentPane);
        setModal(true);
        submitButton.setFocusable(false);

        removedCards.setLayout(new GridLayout());

        this.cards = game.getPlayer().getRemovedCards();

        for (Card card : cards) {
            final JButton button = new JButton();
            button.setIcon(card.getIcon());
            removedCards.add(button);
            removedCards.revalidate();
            button.addActionListener(e -> prechoice(card.getId(), button));
        }

        defaultColor = removedCards.getComponent(0).getBackground();

        if (!buttonRequired) {
            submitButton.hide();
        }

        submitButton.addActionListener(e -> submit(game));
    }

    private void prechoice(int id, JButton button) {
        cardId = id;
        for (int i = 0; i < removedCards.getComponentCount(); i++) {
            removedCards.getComponent(i).setBackground(defaultColor);
        }
        button.setBackground(Color.ORANGE);
    }

    private void submit(Game game) {
        for (Card card : cards) {
            if (cardId == card.getId()) {
                final Card clone = new Card(card);
//                field.setCardToResurrection(clone);
//                field.setCardToRemoveFromRemoved(card);
                game.setCardToResurrection(clone);
                game.setCardToRemoveFromRemoved(card);
                break;
            }
        }
        dispose();
    }
}
