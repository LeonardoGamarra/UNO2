import java.util.ArrayList;

public class Player
{
    private int id;
    private ArrayList<Card> deck = new ArrayList<>();
    private boolean playable;
    public Player(int id, boolean playable)
    {
        this.id = id;
        this.playable = playable;
    }
    public Card getCard(int n)
    {
        Card card = deck.get(n);
        deck.remove(n);
        return card;
    }
    public void setCard(Card card)
    {
        deck.add(card);
    }
    public Card peekCard(int n)
    {
        return deck.get(n);
    }
    public int deckSize()
    {
        return deck.size();
    }
    public int getID()
    {
        return id;
    }
}
