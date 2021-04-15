package com.homework.monopoly;

import java.util.Random;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        int width = 6, height = 6, money = 1000;
        // Trying to read arguments from console
        try {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
            money = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("\n\nArguments had incorrect values. Game will be loaded with default settings (6, 6, 1000)\n\n");
        }
        if (width < 6 || width > 30 || height < 6 || height > 30 || money < 500 || money > 15000) {
            System.out.println("\n\nArguments values are out of range. Game will be loaded with default settings (6, 6, 1000)\n\n");
            width = height = 6;
            money = 1000;
        }

        Player bot = new Player(money, "Bot", "O", 2 * (height + width) - 4);
        Player person = new Player(money, "Person", "M", 2 * (height + width) - 4);
        int order = GenerateInt(0, 1);

        Monopoly currentGame = new Monopoly(width, height, person, bot, order);
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("- - - - - Press ENTER to start - - - - -");
            sc.nextLine();

            // To check who goes at each step
            int step = 0;
            while (currentGame.Bot().MoneyLeft() >= 0 && currentGame.Person().MoneyLeft() >= 0) {
                if (step % 2 == order)
                    currentGame.Step(currentGame.Person(), currentGame.Bot());
                else
                    currentGame.Step(currentGame.Bot(), currentGame.Person());
                step++;
                System.out.println("- - - - - Press ENTER to make another step - - - - -");
                sc.nextLine();
            }
            if (currentGame.Bot().MoneyLeft() <= 0)
                System.out.println("CONGRATULATIONS. YOU WIN");
            else
                System.out.println("SORRY, BUT BOT WINS");
        } catch (Exception e) {
            System.out.println("Game can not be played anymore because of an error: " + e.getMessage());
        }
    }

    /**
     * Generates random integer number in [min, max]
     *
     * @param min Minimal value
     * @param max Maximal value
     * @return Random number
     */
    private static int GenerateInt(int min, int max) {
        int diff = max - min;
        Random random = new Random();
        return random.nextInt(diff + 1) + min;
    }
}
