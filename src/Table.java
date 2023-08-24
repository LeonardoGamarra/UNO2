import java.util.ArrayList;

public class Table
{
    private ArrayList<Card> table = new ArrayList<Card>();
    public void setCard(Card card)
    {
        table.add(card);
    }
    public Card getCard()
    {
        Card card = table.get(0);
        table.remove(0);
        return card;
    }
    public int getSize()
    {
        return table.size();
    }
    public Card peekCard()
    {
        return table.get(0);
    }
}
