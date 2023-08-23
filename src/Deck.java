import java.util.Arrays;

public class Deck
{
    private Card[] deck = new Card[108];

    public Deck()
    {
        // Initializes deck, with blank cards
        for (int i = 0; i < deck.length; i++)
        {
            deck[i] = new Card();
        }

        // Initializes separate deck colors and merges them into deck

        int deckNumber = 0;

        // Red cards
        Card[] cDeck = deckGenerator(0);
        for (int i = 0; i < 25; i++)
        {
            deck[deckNumber] = cDeck[i];
            deckNumber++;
        }

        // Green cards
        cDeck = deckGenerator(1);
        for (int i = 0; i < 25; i++)
        {
            deck[deckNumber] = cDeck[i];
            deckNumber++;
        }

        // Blue cards
        cDeck = deckGenerator(2);
        for (int i = 0; i < 25; i++)
        {
            deck[deckNumber] = cDeck[i];
            deckNumber++;
        }

        // Yellow cards
        cDeck = deckGenerator(3);
        for (int i = 0; i < 25; i++)
        {
            deck[deckNumber] = cDeck[i];
            deckNumber++;
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
}
