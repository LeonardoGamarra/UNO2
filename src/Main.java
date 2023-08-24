import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to UNO!");
        System.out.println("Select the number of players:");
        Player[] players = new Player[in.nextInt()];

        Deck deck = new Deck();
        System.out.println("Deck initialized!");

        Table table = new Table();
        System.out.println("Table initialized!");

        for (int i = 0; i < players.length; i++)
        {
            if (i == 0)
                players[i] = new Player(i, true);
            else
                players[i] = new Player(i, false);
        }
        System.out.println("Players loaded!");

        System.out.println("The deck is being shuffled!");
        deck.shuffle();

        System.out.println("Cards are being drawn!");
        drawCards(players, deck);

        // Start of gameplay loop

        // Draws the first table card
        table.setCard(deck.getCard());
        System.out.println("The first table card is " + cardTranslator(table.peekCard()));

        // A player is randomized to start
        int turn = (int) (Math.random() * players.length);
        System.out.println("The first to play is Player " + players[turn].getID());

        // Stores if the game is ended
        boolean ended = false;

        // Main loop
        do
        {
            // Declares variables used to receive and analyze inputs
            boolean validCard;
            int n;

            // Player turn
            if (turn == 0)
            {
                System.out.println("Its your turn!");
                do
                {
                    System.out.println("Enter a number to choose a card or (0) to buy:");
                    n = in.nextInt() - 1;
                    validCard = false;
                    if (n >= players[0].deckSize() && n < 0)
                        System.out.println("Invalid number!");
                    else
                        validCard = isCardPlayable(players[0].peekCard(n), table.peekCard());
                    if (!validCard)
                        System.out.println("This card can`t be played!");
                    if (n == 0)
                    {
                        buyCard(players[0], deck);
                    }
                }
                while ((n >= players[0].deckSize() && n < 0) || !validCard);

                // Plays chosen card
                table.setCard(players[0].getCard(n));
            }

            // CPU turn
            else
            {

            }
            ended = isGameOver(players);
        }
        while (!ended);

        int b = 0;
    }
    public static String cardTranslator(Card card)
    {
        String[] color = {"Red", "Green", "Blue", "Yellow"};
        String[] special = {"+2", "Reverse", "Block", "Multicolor", "+4"};
        String result = "";
        switch (card.getSpecial())
        {
            case 0 -> result = special[card.getSpecial()] + " " + color[card.getColor()];
            case 1, 2 -> result = color[card.getColor()] + " " + special[card.getSpecial()];
            case 3, 4 -> result = special[card.getSpecial()];
            case 5 -> result = card.getNumber() + " " + color[card.getColor()];
            default ->
            {
                return "cardString";
            }
        }
        return cardColorTranslator(card) + result + "\u001B[0m";
    }
    public static String cardColorTranslator(Card card)
    {
        return switch (card.getSpecial())
        {
            default -> switch (card.getColor())
            {
                case 0 -> "\u001B[31m";
                case 1 -> "\u001B[32m";
                case 2 -> "\u001B[34m";
                case 3 -> "\u001B[33m";
                default -> "f";
            };
            case 3 -> "\u001B[35m";
            case 4 -> "\u001B[36m";
        };

    }
    public static void resetDeck(Deck deck, Table table)
    {
        while (table.getSize() != 1)
        {
            deck.setCard(table.getCard());
        }
    }
    public static void drawCards(Player[] players, Deck deck) throws InterruptedException {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < players.length; j++)
            {
                players[j].setCard(deck.getCard());
                if (j == 0)
                {
                    sayCard(players[0].peekCard(players[0].deckSize() - 1));
                }
            }
        }
    }
    public static void buyCard(Player player, Deck deck)
    {
        player.setCard(deck.getCard());
    }
    public static void sayCard(Card card) throws InterruptedException
    {
        Thread.sleep(500);
        System.out.println("You received " + cardTranslator(card));
    }
    public static boolean isCardPlayable(Card player, Card table)
    {
        if (player.getSpecial() == 5 && table.getSpecial() == 5 && (player.getColor() == table.getColor() || player.getNumber() == table.getNumber()))
            return true;
        else if (player.getSpecial() == table.getSpecial())
            return true;
        else
            return false;
    }
    public static boolean isGameOver(Player[] players)
    {
        for (int i = 0; i < players.length; i++)
        {
            if (players[i].deckSize() == 0)
                return true;
        }
        return false;
    }
}