package com.homework.monopoly;

import java.util.Random;
import java.util.Scanner;

public class Monopoly {
    private final int width;
    private final int height;
    private final Player bot;
    private final Player person;
    private Cell[] field;

    public Monopoly(int width, int height, Player person, Player bot, int order) {
        this.bot = bot;
        this.person = person;
        this.width = width;
        this.height = height;
        // Creating game board
        GenerateField();
        GameInfo(field, order);
        PrintField(this.person, this.bot);
    }

    public Player Bot() {
        return bot;
    }

    public Player Person() {
        return person;
    }

    /**
     * Makes the next step in game
     *
     * @param leadPlayer  Player who goes now
     * @param otherPlayer Opponent of player who goes now
     */
    public void Step(Player leadPlayer, Player otherPlayer) {
        Move(leadPlayer, GenerateShift(leadPlayer));
        while (field[leadPlayer.getPosition()] instanceof Taxi) {
            MoveWithTaxi(leadPlayer);
        }
        if (field[leadPlayer.getPosition()] instanceof EmptyCell) {
            System.out.println("> Just relax there");
        }
        if (field[leadPlayer.getPosition()] instanceof PenaltyCell) {
            PayPenalty(leadPlayer, (PenaltyCell) field[leadPlayer.getPosition()]);
        }
        if (field[leadPlayer.getPosition()] instanceof Shop) {
            ShopCheck((Shop) field[leadPlayer.getPosition()], leadPlayer, otherPlayer);
        }
        if (field[leadPlayer.getPosition()] instanceof Bank) {
            if (leadPlayer.toString().equals("Person")) {
                if (leadPlayer.isAnyDebt()) {
                    System.out.printf("You are in the bank. You owe %d$, which you have to pay immediately\n",
                            leadPlayer.getDebts());
                    leadPlayer.PayDebts();
                    System.out.printf("> Your balance is %d$\n", leadPlayer.MoneyLeft());
                } else {
                    OfferCredit(leadPlayer, (Bank) field[leadPlayer.getPosition()]);
                }
            } else {
                System.out.println("> Bot is on the bank cell");
            }
        }
        System.out.println("    - - - - - GAME BOARD AFTER TURN - - - - -");
        PrintField(leadPlayer, otherPlayer);
    }

    /**
     * Offers player to get a credit when he is in bank
     *
     * @param player Player who will get a credit
     * @param bank   Bank where player is
     */
    private void OfferCredit(Player player, Bank bank) {
        int maxCredit = (int) Math.round(player.getUpgradeMoney() * bank.creditCoefficient);
        System.out.printf("> Your balance: %d$\nYou can get a credit for the amount <= %d$\n", player.MoneyLeft(), maxCredit);
        System.out.println("Enter how much money you want to receive. If you don't, enter 0.");
        Scanner myInput = new Scanner(System.in);
        int a = -1;
        try {
            a = myInput.nextInt();
        } catch (Exception e) {
            myInput.next();
        }
        while (a < 0 || a > maxCredit) {
            System.out.println("Incorrect value. Try again.");
            try {
                a = myInput.nextInt();
            } catch (Exception e) {
                myInput.next();
            }
        }
        if (a > 0) {
            player.GetCredit(a, bank);
            System.out.printf("> You got %d$ on credit. Your balance is %d$.\n" +
                            "Now you owe bank %d$ more. Total debts: %d$\n",
                    a, player.MoneyLeft(), (int) Math.round(bank.debtCoefficient * a), player.getDebts());
        } else {
            System.out.printf("> You decided not to get the credit. Your balance is %d$. You owe bank %d$\n",
                    player.MoneyLeft(), player.getDebts());
        }
    }

    /**
     * Makes interaction with shop possible
     * Depending on position it offers buying or upgrading or makes leading player pay penalty
     *
     * @param shop        Shop which player is in
     * @param leadPlayer  Player who goes at current step
     * @param otherPlayer Other player
     */
    private void ShopCheck(Shop shop, Player leadPlayer, Player otherPlayer) {
        // If shop belongs to no one, it is offered to buy it
        if (shop.Owner().equals("None")) {
            PlayerBuying(leadPlayer, shop);
            return;
        }
        // If one player got to another player's shop
        if (shop.Owner().equals(otherPlayer.toString())) {
            PayForShop(leadPlayer, otherPlayer, shop);
            return;
        }
        if (shop.Owner().equals(leadPlayer.toString())) {
            OfferUpgrade(leadPlayer, shop);
        }
    }

    /**
     * Offers player to upgrade his shop
     *
     * @param player Player who will upgrade
     * @param shop   Shop which will be upgraded
     */
    private void OfferUpgrade(Player player, Shop shop) {
        if (player.toString().equals("Person")) {
            System.out.printf("You are in your shop. Your balance is %d$\n", player.MoneyLeft());
            System.out.printf("Would you like to upgrade it for %d$? Input ‘YES’ if you agree or ‘NO’ otherwise\n",
                    Math.round(shop.getN() * shop.improvementCoefficient));
            // Action depending on choice
            if (GetAnswer()) {
                if (shop.Upgrade()) {
                    System.out.printf("> You upgraded your shop.\n" +
                            "Opponent will pay %d$ on this cell\n", shop.getK());
                    System.out.printf("> Your balance is %d$\n", player.MoneyLeft());
                } else
                    System.out.println("Sorry, you don't have enough money");
            } else {
                System.out.print("> You decided not to do anything\n");
            }
        } else {
            // Randomizing bot's choice
            if (GenerateInt(0, 1) == 1) {
                if (shop.Upgrade()) {
                    System.out.printf("> Bot upgraded his shop. You will pay %d$ on this cell\n", shop.getK());
                    System.out.printf("> Bot's balance is %d$\n", player.MoneyLeft());
                } else {
                    System.out.println("Bot had no money for upgrade");
                }
            } else {
                System.out.print("> Bot decided not to upgrade his shop\n");
            }
        }
    }

    /**
     * Interacts with player if he got on shop's with no owner
     *
     * @param player Player who
     * @param shop   Shop which can be bought
     */
    private void PlayerBuying(Player player, Shop shop) {
        if (player.toString().equals("Person")) {
            System.out.printf("You are in shop with no owner. Your balance is %d$\n", player.MoneyLeft());
            System.out.printf("Characteristics: K = %d, improvementCoefficient = %1.3f, compensationCoefficient = %1.3f\n",
                    shop.getK(), shop.improvementCoefficient, shop.compensationCoefficient);
            System.out.printf("Would you like to buy it for %d$? Input ‘YES’ if you agree or ‘NO’ otherwise\n", shop.getN());
            // Action depending on choice
            if (GetAnswer()) {
                if (shop.Buy(player)) {
                    System.out.printf("> You bought a shop. Your balance is %d$ now\n", player.MoneyLeft());
                } else
                    System.out.println("Sorry, you don't have enough money");
            } else {
                System.out.print("> You decided not to do anything\n");
            }
        } else {
            if (GenerateInt(0, 1) == 1) {
                if (shop.Buy(player)) {
                    System.out.printf("> Bot bought a new shop. His balance is %d$\n", player.MoneyLeft());
                } else {
                    System.out.println("Bot had no money to buy a new shop");
                }
            } else {
                System.out.print("> Bot decided not to buy a new shop\n");
            }
        }

    }

    /**
     * Makes player pay for being on other's shop
     *
     * @param from Player who will pay
     * @param to   Player who will receive money
     * @param shop Shop cell
     */
    private void PayForShop(Player from, Player to, Shop shop) {
        int sum = shop.getK();
        from.ChangeBalance(-sum);
        to.ChangeBalance(sum);
        if (from.toString().equals("Bot")) {
            System.out.printf("> Bot got on cell with your shop. It will pay you %d$\n", sum);
            System.out.printf("Your balance is %d$\n", to.MoneyLeft());
            System.out.printf("Bot's balance is %d$\n", from.MoneyLeft());
        } else {
            System.out.printf("> You got on cell with bot's shop. You have to pay him %d$\n", sum);
            System.out.printf("Your balance is %d$\n", from.MoneyLeft());
            System.out.printf("Bot's balance is %d$\n", to.MoneyLeft());
        }

    }

    /**
     * @param player Player who will pay penalty
     * @param pc     Cell which player got on
     */
    private void PayPenalty(Player player, PenaltyCell pc) {
        // Informs about position
        if (player.toString().equals("Person"))
            System.out.println("> You got to penalty cell");
        else
            System.out.println("> Bot got to penalty cell");
        player.ChangeBalance(-(int) (Math.round(pc.penaltyCoefficient * player.MoneyLeft())));
        // Updated money value
        if (player.toString().equals("Person"))
            System.out.printf("> Your balance is  %d$ now\n", player.MoneyLeft());
        else
            System.out.printf("> Bot's balance is  %d$ now\n", player.MoneyLeft());
    }

    /**
     * Moves player forward with taxi
     *
     * @param player Player who will be moved
     */
    private void MoveWithTaxi(Player player) {
        int taxiDistance = GenerateInt(3, 5);
        if (player.toString().equals("Person"))
            System.out.printf("> You are shifted forward with taxi by %d cells\n", taxiDistance);
        else
            System.out.printf("> Bot is shifted forward with taxi by %d cells\n", taxiDistance);
        player.Move(taxiDistance);
    }

    /**
     * Moves player forward on given distance
     *
     * @param player Player who will move now
     * @param shift  Distance
     */
    private void Move(Player player, int shift) {
        player.Move(shift);
    }

    /**
     * Generates distance of moving by imitating two dices
     *
     * @param player Player who will be moved later
     * @return Sum of dices' values
     */
    private int GenerateShift(Player player) {
        int firstCube = GenerateInt(1, 6);
        int secondCube = GenerateInt(1, 6);
        System.out.printf("> (%d, %d) -> player %s moves forward on %d cells\n", firstCube, secondCube, player, firstCube + secondCube);
        return (firstCube + secondCube);
    }

    /**
     * Prints all the info before game starts
     *
     * @param field Game board
     * @param check Informs who goes first
     */
    public void GameInfo(Cell[] field, int check) {
        System.out.println("- - - - - Welcome to Monopoly - - - - - \nGame will start soon. " +
                "Have a look at generated coefficient for this round:");
        System.out.println("BANK RULES: ");
        // Find bank to show coefficients
        for (Cell cell : field) {
            if (cell instanceof Bank) {
                System.out.printf("- Credit coefficient = %1.3f%n", ((Bank) cell).creditCoefficient);
                System.out.printf("- Debt coefficient = %1.3f%n", ((Bank) cell).debtCoefficient);
                break;
            }
        }
        System.out.println("PENALTY: ");
        // Find penalty cell to show coefficients
        for (Cell cell : field) {
            if (cell instanceof PenaltyCell) {
                System.out.printf("- Penalty coefficient = %1.3f%n", ((PenaltyCell) cell).penaltyCoefficient);
                break;
            }
        }
        System.out.print("NOTE:\nYour position will be indicated with () braces.\n" +
                "Bot will be indicated with [] braces\n");
        if (check == 0) {
            System.out.println("- YOU GO FIRST");
        } else {
            System.out.println("- BOT GOES FIRST");
        }
        System.out.println("- - - - - GAME BOARD FOR THIS ROUND - - - - -");
    }

    /**
     * Generates random game board
     */
    private void GenerateField() {
        field = new Cell[2 * (height + width) - 4];
        AddEmptyCells();
        AddBanks();
        AddPenaltyCells();
        AddTaxis();
        AddShops();
    }

    /**
     * Prints current statement of game board
     *
     * @param p1 First player on board
     * @param p2 Second player on board
     */
    public void PrintField(Player p1, Player p2) {
        // First line
        for (int j = 0; j < width; ++j) {
            System.out.print(GetOutput(j, p1, p2));
        }
        System.out.println();
        System.out.println();
        // Borders and filling center with empty output
        for (int i = 1; i < height - 1; i++) {
            System.out.print(GetOutput(field.length - i, p1, p2));
            for (int j = 1; j < width - 1; ++j) {
                System.out.printf("  %s  ", " ");
            }
            System.out.print(GetOutput(width - 1 + i, p1, p2));
            System.out.println();
            System.out.println();
        }
        // Last line
        for (int j = width - 1; j >= 0; j--) {
            System.out.print(GetOutput(width + height - 2 + j, p1, p2));
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Finds out which symbols should be printed depending on players' position
     *
     * @param index Index on game board
     * @param p1    First player to get position
     * @param p2    Second player to get position
     * @return String with output
     */
    private String GetOutput(int index, Player p1, Player p2) {
        if (p1.getPosition() == index && p2.getPosition() == index)
            return String.format("([%s])", field[index]);
        if (p1.getPosition() == index && p2.getPosition() != index) {
            if (p1.toString().equals("Person"))
                return String.format(" (%s) ", field[index]);
            else
                return String.format(" [%s] ", field[index]);
        }
        if (p1.getPosition() != index && p2.getPosition() == index) {
            if (p2.toString().equals("Person"))
                return String.format(" (%s) ", field[index]);
            else
                return String.format(" [%s] ", field[index]);
        }
        return String.format("  %s  ", field[index]);
    }

    /**
     * Adds Empty cells in corners
     */
    private void AddEmptyCells() {
        field[0] = field[width - 1] = field[width - 1 + height - 1] =
                field[field.length - height + 1] = new EmptyCell();
    }

    /**
     * Adds 0-2 penalty cells to each line of board
     */
    private void AddPenaltyCells() {
        PenaltyCell penaltyCell = new PenaltyCell();
        // Upper line
        int penaltyNumber = GenerateInt(0, 2);
        for (int i = 0; i < penaltyNumber; i++) {
            int pos = GetEmptyPosition(1, width - 2);
            field[pos] = penaltyCell;
        }
        // Down line
        penaltyNumber = GenerateInt(0, 2);
        for (int i = 0; i < penaltyNumber; i++) {
            int pos = GetEmptyPosition(width + height - 1, field.length - height);
            field[pos] = penaltyCell;
        }
        // Right line
        penaltyNumber = GenerateInt(0, 2);
        for (int i = 0; i < penaltyNumber; i++) {
            int pos = GetEmptyPosition(width, width + height - 3);
            field[pos] = penaltyCell;
        }
        // Left line
        penaltyNumber = GenerateInt(0, 2);
        for (int i = 0; i < penaltyNumber; i++) {
            int pos = GetEmptyPosition(field.length - height + 2, field.length - 1);
            field[pos] = penaltyCell;
        }
    }

    /**
     * Adds 0-2 taxis to each line of board
     */
    private void AddTaxis() {
        // Upper line
        int taxiNumber = GenerateInt(0, 2);
        for (int i = 0; i < taxiNumber; i++) {
            int pos = GetEmptyPosition(1, width - 2);
            if (pos == -1) {
                break;
            }
            field[pos] = new Taxi();
        }
        // Down line
        taxiNumber = GenerateInt(0, 2);
        for (int i = 0; i < taxiNumber; i++) {
            int pos = GetEmptyPosition(width + height - 1, field.length - height);
            if (pos == -1) {
                break;
            }
            field[pos] = new Taxi();
        }
        // Right line
        taxiNumber = GenerateInt(0, 2);
        for (int i = 0; i < taxiNumber; i++) {
            int pos = GetEmptyPosition(width, width + height - 3);
            if (pos == -1) {
                break;
            }
            field[pos] = new Taxi();
        }
        // Left line
        taxiNumber = GenerateInt(0, 2);
        for (int i = 0; i < taxiNumber; i++) {
            int pos = GetEmptyPosition(field.length - height + 2, field.length - 1);
            if (pos == -1) {
                break;
            }
            field[pos] = new Taxi();
        }
    }

    /**
     * Adds shops to non-instanced cells of the field
     */
    private void AddShops() {
        for (int i = 0; i < field.length; i++) {
            if (field[i] == null) {
                field[i] = new Shop(GenerateInt(50, 500));
            }
        }
    }

    /**
     * Adds one bank to each line
     */
    private void AddBanks() {
        Bank bank = new Bank();
        field[GenerateInt(1, width - 2)] = field[GenerateInt(width, width + height - 3)] =
                field[GenerateInt(width + height - 1, field.length - height)] =
                        field[GenerateInt(field.length - height + 2, field.length - 1)] = bank;
    }

    /**
     * Generates random integer number in [min, max]
     *
     * @param min Minimal value
     * @param max Maximal value
     * @return Random number
     */
    private int GenerateInt(int min, int max) {
        int diff = max - min;
        Random random = new Random();
        return random.nextInt(diff + 1) + min;
    }

    /**
     * Finds an empty cell on board
     *
     * @param min Minimal value for position
     * @param max Maximal value for position
     * @return Random empty position
     */
    private int GetEmptyPosition(int min, int max) {
        int position = 0;
        int iter = 0;
        while (field[position] != null) {
            position = GenerateInt(min, max);
            ++iter;
            // Maybe the line is already full
            if (iter > 100000) {
                return -1;
            }
        }
        return position;
    }

    /**
     * Gets "YES"/"NO" answer from user
     *
     * @return True if user said "yes" ans false if "no"
     */
    private boolean GetAnswer() {
        Scanner reader =
                new Scanner(System.in);
        String answer = reader.nextLine();
        // Repeat until input will be correct
        while (!answer.toUpperCase().equals("YES") && !answer.toUpperCase().equals("NO")) {
            System.out.println("Incorrect answer. Type Yes or No");
            answer = reader.nextLine();
        }
        return answer.toUpperCase().equals("YES");
    }
}
