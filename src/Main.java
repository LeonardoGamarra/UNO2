public class Main
{
    public static void main(String[] args)
    {
        Table table = new Table();
        Deck deck = new Deck();
        for (int i = 0; i < 108; i++)
        {
            System.out.println(cardTranslator(deck.getCard()));
        }

    }
    public static String cardTranslator(Card card)
    {
        String[] color = {"Red", "Green", "Blue", "Yellow"};
        String[] special = {"+2", "Reverse", "Block", "Multicolor", "+4"};
        String result = "";
        switch (card.getSpecial())
        {
            case 0:
                result = special[card.getSpecial()] + " " + color[card.getColor()];
                break;
            case 1: case 2:
                result = color[card.getColor()] + " " + special[card.getSpecial()];
                break;
            case 3: case 4:
                result = special[card.getSpecial()];
                break;
            case 5:
                result = card.getNumber() + " " + color[card.getColor()];
                break;
            default:
                return "cardString";
        }
        return cardColorTranslator(card.getColor()) + result + "\u001B[0m";
    }
    public static String cardColorTranslator(int color)
    {
        switch (color)
        {
            case 0:
                return "\u001B[31m";
            case 1:
                return "\u001B[32m";
            case 2:
                return "\u001B[34m";
            case 3:
                return "\u001B[33m";
            default:
                return "f";
        }
    }
}