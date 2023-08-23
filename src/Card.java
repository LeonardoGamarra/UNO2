public class Card
{
    // numbers (0 - 9)
    private int number;

    // colors (red(0), green(1), blue(2), yellow(3))
    private int color;

    // special (+2(0), reverse(1), block(2), multicolor(3), +4(4), default(5))
    private int special;

    public void setNumber(int number)
    {
        this.number = number;
    }
    public void setColor(int color)
    {
        this.color = color;
    }
    public void setSpecial(int special)
    {
        this.special = special;
    }
    public int getNumber()
    {
        return number;
    }
    public int getColor()
    {
        return color;
    }
    public int getSpecial()
    {
        return special;
    }
    public String cardTranslator(Card card)
    {
        String[] color = {"Red", "Green", "Blue", "Yellow"};
        String[] special = {"+2", "Reverse", "Block", "Multicolor", "+4"};
        switch (card.getSpecial())
        {
            case 0:
                return special[card.getSpecial()] + " " + color[card.getColor()];
            case 1: case 2:
                return color[card.getColor()] + " " + special[card.getSpecial()];
            case 3: case 4:
                return special[card.getSpecial()];
            case 5:
                return card.getNumber() + " " + color[card.getColor()];
            default:
                return "cardString";
        }
    }
}
