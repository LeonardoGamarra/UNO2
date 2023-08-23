import java.util.ArrayList;
import java.util.Collections;

public class Deck
{
    private ArrayList<Card> deck = new ArrayList<>();

    public Deck()
    {
        // Initializes deck, with blank cards
        for (int i = 0; i < deck.size(); i++)
        {
            deck.add(new Card());
        }

        // Initializes separate deck colors and merges them into deck

        // Red cards
        Card[] cDeck = deckGenerator(0);
        for (int i = 0; i < 25; i++)
        {
            deck.add(cDeck[i]);
        }

        // Green cards
        cDeck = deckGenerator(1);
        for (int i = 0; i < 25; i++)
        {
            deck.add(cDeck[i]);
        }

        // Blue cards
        cDeck = deckGenerator(2);
        for (int i = 0; i < 25; i++)
        {
            deck.add(cDeck[i]);
        }

        // Yellow cards
        cDeck = deckGenerator(3);
        for (int i = 0; i < 25; i++)
        {
            deck.add(cDeck[i]);
        }

        // Multicolor cards

        for (int i = 0; i < 4; i++)
        {
            Card card = new Card();
            card.setSpecial(3);
            deck.add(card);
        }

        // +4 cards

        for (int i = 0; i < 4; i++)
        {
            Card card = new Card();
            card.setSpecial(4);
            deck.add(card);
        }

    }
    public static Card[] deckGenerator(int color) {
        Card[] colorDeck = new Card[25];
        for (int i = 0; i < 25; i++) {
            colorDeck[i] = new Card();
        }
        for (int i = 0; i < 25; i++) {
            colorDeck[i].setColor(color);
        }
        colorDeck[0].setNumber(0);
        colorDeck[0].setSpecial(5);
        int cardNumber = 1;
        for (int i = 1; i <= 9; i++) {
            for (int k = 0; k < 2; k++) {
                colorDeck[cardNumber].setNumber(i);
                colorDeck[cardNumber].setSpecial(5);
                cardNumber++;
            }
        }
        for (int i = 19; i < 25; i++) {
            if (i < 21)
                colorDeck[i].setSpecial(0);
            else if (i < 23)
                colorDeck[i].setSpecial(1);
            else
                colorDeck[i].setSpecial(2);
        }
        return colorDeck;
    }
    public Card getCard()
    {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }
    public void shuffle()
    {
        Collections.shuffle(deck);
    }
    public void resetDeck(Table table)
    {
        if (deck.size() == 0)
        {
            for (int i = 0; i < table.getSize(); i++)
            {
                deck.add(table.getCard());
            }
        }
    }
}
