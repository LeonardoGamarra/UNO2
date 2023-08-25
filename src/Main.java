import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to UNO!");
        System.out.println("Select the number of players:");
        Player[] players;
        players = new Player[in.nextInt()];

        Deck deck = new Deck();
        System.out.println("Deck initialized!");

        Table table = new Table();
        System.out.println("Table initialized!");

        for (int i = 0; i < players.length; i++) {
            if (i == 0)
                players[i] = new Player(i, true);
            else
                players[i] = new Player(i, false);
        }
        System.out.println("Players loaded!");

        System.out.println("The deck is being shuffled!");
        deck.shuffle();

        System.out.println("Cards are being drawn!");
        System.out.println();
        drawCards(players, deck);

        // Start of gameplay loop

        // Draws the first table card
        table.setCard(deck.getCard());
        if (table.peekCard().getSpecial() == 3 || table.peekCard().getSpecial() == 4)
            table.peekCard().setColor((int) (Math.random() * 4));
        System.out.println();
        System.out.println("The first table card is " + cardTranslator(table.peekCard()));

        // A player is randomized to start
        int turn = (int) (Math.random() * players.length);
        System.out.println();
        System.out.println("The first to play is Player " + players[turn].getID());

        // Stores if the game is ended
        boolean ended = false;

        int round = 1;
        int reverse = 1;

        // Main loop
        do {
            // Declares variables used to receive and analyze inputs
            boolean bought;
            boolean validCard;
            int n = -1;

            System.out.println();
            System.out.println("PLAY " + round);
            // Player turn
            if (turn == 0) {
                Thread.sleep(1000);
                printScoreBoard(players, reverse);
                System.out.println("Its your turn!");
                System.out.println("Last card played: " + cardTranslator(table.peekCard()));
                do {
                    System.out.println();
                    players[0].setDeck(organizeDeck(players[0].getDeck()));
                    printPlayerDeck(players[0]);
                    System.out.println("Enter a number to choose a card or (0) to buy:");

                    // Receives player input
                    n = in.nextInt() - 1;
                    validCard = false;
                    bought = false;

                    // If player decides to buy, this code runs
                    if (n == -1) {
                        bought = true;
                        do {
                            buyCard(players[0], deck, table);
                            sayCard(players[0].peekCard(players[0].deckSize() - 1));
                            validCard = isCardPlayable(players[0].peekCard(players[0].deckSize() - 1), table.peekCard());
                        }
                        while (!validCard);
                        n = players[0].deckSize() - 1;
                    }

                    // Else, checks if player has the card
                    else if (n >= players[0].deckSize() || n < 0)
                        System.out.println("Invalid number!");
                        // If it has the card, checks if it can be played
                    else
                        validCard = isCardPlayable(players[0].peekCard(n), table.peekCard());
                    if (!validCard)
                        System.out.println("This card can`t be played!");

                }
                while (!bought && (n >= players[0].deckSize() && n < 0) || !validCard);
            }

            // CPU turn
            else {
                // Searches for a playable card
                for (int i = 0; i < players[turn].deckSize(); i++) {
                    if (isCardPlayable(players[turn].peekCard(i), table.peekCard())) {
                        n = i;
                        break;
                    }
                }

                // Starts buying if nothing works
                if (n == -1) {
                    do {

                        buyCard(players[turn], deck, table);
                        validCard = isCardPlayable(players[turn].peekCard(players[turn].deckSize() - 1), table.peekCard());
                    }
                    while (!validCard);
                    System.out.println();
                    n = players[turn].deckSize() - 1;
                }

            }

            // Plays card

            if (players[turn].peekCard(n).getSpecial() == 3 || players[turn].peekCard(n).getSpecial() == 4) {
                int color;
                if (turn == 0) {
                    System.out.println("Choose a color: \u001B[31m(1)\u001B[0m \u001B[32m(2)\u001B[0m \u001B[34m(3)\u001B[0m \u001B[33m(4)\u001B[0m");
                    color = in.nextInt();
                } else {
                    color = chooseColor(players[turn]);
                }
                players[turn].peekCard(n).setColor(color - 1);
            }

            playCard(n, players[turn], table);
            ended = isGameOver(players);

            if (table.peekCard().getSpecial() != 5) {
                if (table.peekCard().getSpecial() == 4) {
                    for (int i = 0; i < 4; i++) {
                        buyCard(players[correctTurn(players.length, turn + reverse)], deck, table);
                        if (correctTurn(players.length, turn + reverse) == 0) {
                            sayCard(players[0].peekCard(players[0].deckSize() - 1));
                        }
                    }
                    System.out.println();
                    turn += reverse;
                } else if (table.peekCard().getSpecial() == 0) {
                    for (int i = 0; i < 2; i++) {
                        buyCard(players[correctTurn(players.length, turn + reverse)], deck, table);
                        if (correctTurn(players.length, turn + reverse) == 0) {
                            sayCard(players[0].peekCard(players[0].deckSize() - 1));
                        }
                    }
                    System.out.println();
                    turn += reverse;
                } else if (table.peekCard().getSpecial() == 2) {
                    turn += reverse;
                } else if (table.peekCard().getSpecial() == 1) {
                    reverse *= -1;
                }
            }
            round++;
            turn += reverse;
            turn = correctTurn(players.length, turn);
        }
        while (!ended);
        for (int i = 0; i < players.length; i++)
        {
            if (players[i].deckSize() == 0)
            {
                if (players[i].getID() == 0)
                    System.out.println("You won!");
                else
                    System.out.println("CPU" + players[i].getID() + " won!");
            }
        }
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
        return switch (card.getColor()) {
            case 0 -> "\u001B[31m";
            case 1 -> "\u001B[32m";
            case 2 -> "\u001B[34m";
            case 3 -> "\u001B[33m";
            case 4 -> "\u001B[35m";
            case 5 -> "\u001B[36m";
            default -> "f";
        };
    }
    public static void resetDeck(Deck deck, Table table) throws InterruptedException
    {
        System.out.println("There are no cards in the deck!");
        Thread.sleep(2000);
        System.out.println("Used cards from the table are being retrieved!");
        Thread.sleep(2000);
        while (table.getSize() != 1)
        {
            deck.setCard(table.getCard());
        }
        System.out.println("Cards are being shuffled!");
        deck.shuffle();
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
    public static void buyCard(Player player, Deck deck, Table table) throws InterruptedException
    {
        Thread.sleep(1000);
        if (player.getID() != 0)
            System.out.println("CPU" + player.getID() + " bought a card!");
        if (deck.getSize() == 0)
            resetDeck(deck, table);
        player.setCard(deck.getCard());
    }
    public static void sayCard(Card card) throws InterruptedException
    {
        Thread.sleep(1000);
        System.out.println("You received " + cardTranslator(card));
    }
    public static boolean isCardPlayable(Card player, Card table)
    {
        if (player.getSpecial() == 3 || player.getSpecial() == 4)
            return true;
        else if (player.getColor() == table.getColor())
            return true;
        else if (player.getNumber() == table.getNumber() && player.getNumber() != 10)
            return true;
        else return player.getSpecial() == table.getSpecial() && player.getSpecial() != 5;
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
    public static void playCard(int n, Player player, Table table) throws InterruptedException
    {
        Thread.sleep(1000);
        if (player.getID() == 0)
            System.out.println("You played " + cardTranslator(player.peekCard(n)));
        else
            System.out.println("CPU" + player.getID() + " played " + cardTranslator(player.peekCard(n)));
        table.setCard(player.getCard(n));
    }
    public static int chooseColor(Player player)
    {
        int result = (int) Math.ceil(Math.random() * 4);
        for (int i = 0; i < player.deckSize() - 1; i++)
        {
            if (player.peekCard(i).getSpecial() != 3 && player.peekCard(i).getSpecial() != 4)
            {
                result = player.peekCard(i).getColor() + 1;
                break;
            }
        }
        return result;
    }
    public static void printPlayerDeck(Player player)
    {
        System.out.println("This is your hand:");
        for (int i = 0; i < player.deckSize(); i++)
        {
            System.out.print("| " + cardTranslator(player.peekCard(i)) + " (" + (i + 1) + ")" +  " |");
        }
        System.out.println();
    }
    public static int correctTurn(int totalPlayer, int turn)
    {
        if (turn < 0)
            return turn + totalPlayer;
        else if (turn >= totalPlayer)
            return turn - totalPlayer;
        else
            return turn;
    }
    public static void printScoreBoard(Player[] players, int reverse)
    {
        System.out.println("-------------- SCOREBOARD --------------");
        System.out.println("      Player       ||  Cards Remaining  ");
        String uno = "";
        for (int i = 0; i < players.length; i++) {
            if (players[i].deckSize() == 1)
                uno = " UNO!";
            else
                uno = "";
            if (i == 0)
                System.out.print(spaceFormatter("You", 19) + "||" + spaceFormatter(String.valueOf(players[i].deckSize() + uno), 19));
            else
                System.out.print(spaceFormatter("CPU" + players[i].getID(), 19) + "||" + spaceFormatter(String.valueOf(players[i].deckSize() + uno), 19));
            if (reverse == 1)
                System.out.println("↓");
            else
                System.out.println("↑");
        }
        System.out.println("----------------------------------------");
    }
    public static String spaceFormatter(String s, int n)
    {
        int spaces = n - s.length();
        StringBuilder result = new StringBuilder();
        result.append(s);
        for (int i = 0; i < spaces; i++)
        {
            result.append(" ");
        }
        return result.toString();
    }
    public static ArrayList<Card> organizeDeck(ArrayList<Card> card)
    {
        ArrayList<Card> result = new ArrayList<>();
        ArrayList<Card> red = new ArrayList<>();
        ArrayList<Card> green = new ArrayList<>();
        ArrayList<Card> blue = new ArrayList<>();
        ArrayList<Card> yellow = new ArrayList<>();
        ArrayList<Card> plus = new ArrayList<>();
        ArrayList<Card> multicolor = new ArrayList<>();
        for (int i = 0; i < card.size(); i++)
        {
            switch (card.get(i).getColor())
            {
                case 0:
                    red.add(card.get(i));
                    break;
                case 1:
                    green.add(card.get(i));
                    break;
                case 2:
                    blue.add(card.get(i));
                    break;
                case 3:
                    yellow.add(card.get(i));
                    break;
                case 4:
                    multicolor.add(card.get(i));
                    break;
                case 5:
                    plus.add(card.get(i));
                    break;
            }
        }
        // Red
        for (int i = 0; i <= 9; i++)
        {
            for (int j = 0; j < red.size(); j++)
            {
                if (red.get(j).getNumber() == i)
                    result.add(red.get(j));
            }
        }
        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j < red.size(); j++)
            {
                if (red.get(j).getSpecial() == i)
                    result.add(red.get(j));
            }
        }

        // Green
        for (int i = 0; i <= 9; i++)
        {
            for (int j = 0; j < green.size(); j++)
            {
                if (green.get(j).getNumber() == i)
                    result.add(green.get(j));
            }
        }
        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j < green.size(); j++)
            {
                if (green.get(j).getSpecial() == i)
                    result.add(green.get(j));
            }
        }

        // Blue
        for (int i = 0; i <= 9; i++)
        {
            for (int j = 0; j < blue.size(); j++)
            {
                if (blue.get(j).getNumber() == i)
                    result.add(blue.get(j));
            }
        }
        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j < blue.size(); j++)
            {
                if (blue.get(j).getSpecial() == i)
                    result.add(blue.get(j));
            }
        }

        // Yellow
        for (int i = 0; i <= 9; i++)
        {
            for (int j = 0; j < yellow.size(); j++)
            {
                if (yellow.get(j).getNumber() == i)
                    result.add(yellow.get(j));
            }
        }
        for (int i = 0; i <= 2; i++)
        {
            for (int j = 0; j < yellow.size(); j++)
            {
                if (yellow.get(j).getSpecial() == i)
                    result.add(yellow.get(j));
            }
        }

        // Multicolor and +4
        result.addAll(multicolor);
        result.addAll(plus);

        return result;
    }
    public static void printReverseCard()
    {
        System.out.println("⠐⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠂\n" +
                "⠄⠄⣰⣾⣿⣿⣿⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣆⠄⠄\n" +
                "⠄⠄⣿⣿⣿⡿⠋⠄⡀⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⠋⣉⣉⣉⡉⠙⠻⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣇⠔⠈⣿⣿⣿⣿⣿⡿⠛⢉⣤⣶⣾⣿⣿⣿⣿⣿⣿⣦⡀⠹⠄⠄\n" +
                "⠄⠄⣿⣿⠃⠄⢠⣾⣿⣿⣿⠟⢁⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣿⣿⣿⣿⠟⢁⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣿⣿⡟⠁⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣿⠋⢠⣾⣿⣿⣿⣿⣿⣿⡿⠿⠿⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⡿⠁⣰⣿⣿⣿⣿⣿⣿⣿⣿⠗⠄⠄⠄⠄⣿⣿⣿⣿⣿⣿⣿⡟⠄⠄\n" +
                "⠄⠄⣿⡿⠁⣼⣿⣿⣿⣿⣿⣿⡿⠋⠄⠄⠄⣠⣄⢰⣿⣿⣿⣿⣿⣿⣿⠃⠄⠄\n" +
                "⠄⠄⡿⠁⣼⣿⣿⣿⣿⣿⣿⣿⡇⠄⢀⡴⠚⢿⣿⣿⣿⣿⣿⣿⣿⣿⡏⢠⠄⠄\n" +
                "⠄⠄⠃⢰⣿⣿⣿⣿⣿⣿⡿⣿⣿⠴⠋⠄⠄⢸⣿⣿⣿⣿⣿⣿⣿⡟⢀⣾⠄⠄\n" +
                "⠄⠄⢀⣿⣿⣿⣿⣿⣿⣿⠃⠈⠁⠄⠄⢀⣴⣿⣿⣿⣿⣿⣿⣿⡟⢀⣾⣿⠄⠄\n" +
                "⠄⠄⢸⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⢶⣿⣿⣿⣿⣿⣿⣿⣿⠏⢀⣾⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣿⣿⣿⣿⣷⣶⣶⣶⣶⣶⣿⣿⣿⣿⣿⣿⣿⠋⣠⣿⣿⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⢁⣼⣿⣿⣿⣿⣿⠄⠄\n" +
                "⠄⠄⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⢁⣴⣿⣿⣿⣿⣿⣿⣿⠄⠄\n" +
                "⠄⠄⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⢁⣴⣿⣿⣿⣿⠗⠄⠄⣿⣿⠄⠄\n" +
                "⠄⠄⣆⠈⠻⢿⣿⣿⣿⣿⣿⣿⠿⠛⣉⣤⣾⣿⣿⣿⣿⣿⣇⠠⠺⣷⣿⣿⠄⠄\n" +
                "⠄⠄⣿⣿⣦⣄⣈⣉⣉⣉⣡⣤⣶⣿⣿⣿⣿⣿⣿⣿⣿⠉⠁⣀⣼⣿⣿⣿⠄⠄\n" +
                "⠄⠄⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣾⣿⣿⡿⠟⠄⠄\n" +
                "⠠⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄");

    }
}