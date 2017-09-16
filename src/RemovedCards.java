import Card.Card;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class RemovedCards extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JPanel removedCards;
    private int cardId;
    private Color defaultColor;
    private ArrayList<Card> cards;

    public RemovedCards(Field field) {
        setContentPane(contentPane);
        setModal(true);
        submitButton.setFocusable(false);

        removedCards.setLayout(new GridLayout());

        this.cards = field.getPlayer().getRemovedCards();

        for (int i = 0; i < cards.size(); i++) {
            JButton button = new JButton();
            Card card = cards.get(i);
            button.setIcon(card.getIcon());
            removedCards.add(button);
            removedCards.revalidate();
            button.addActionListener(e -> prechoice(card.getId(), button));
        }

        defaultColor = removedCards.getComponent(0).getBackground();

        submitButton.addActionListener(e -> {
            try {
                submit(field);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void prechoice(int id, JButton button) {
        cardId = id;
        for (int i = 0; i < removedCards.getComponentCount(); i++) {
            removedCards.getComponent(i).setBackground(defaultColor);
        }
        button.setBackground(Color.ORANGE);
        System.out.println(cardId);
    }

    private void submit(Field field) throws IOException {
        int i = 0;
        for (; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (cardId == card.getId()) {
                Card clone = new Card(card);
                field.Move(field.getPlayer(), field.getEnemy(), clone, -1);
                field.getPlayer().removeRemovedCard(card);
                break;
            }
        }
        dispose();
    }

//    public static void main(String[] args) {
//        RemovedCards dialog = new RemovedCards();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
