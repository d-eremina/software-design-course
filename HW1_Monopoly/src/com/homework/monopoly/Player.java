package com.homework.monopoly;

public class Player {
    private int money;
    private int position;
    private final int fieldLength;
    private final String name;
    private final String shortName;
    private int upgradeMoney;
    private int debts;

    public Player(int money, String name, String shortName, int fieldLength) {
        this.money = money;
        this.fieldLength = fieldLength;
        this.name = name;
        this.shortName = shortName;
        debts = 0;
        upgradeMoney = 0;
        position = 0;
    }

    /**
     * Allows player to get a credit
     *
     * @param sum Amount of money to take on credit
     * @param bank ank where player gets a credit
     */
    public void GetCredit(int sum, Bank bank) {
        debts += (int) Math.round(sum * bank.debtCoefficient);
        ChangeBalance(sum);
    }

    /**
     * Forces player to pay his debts
     */
    public void PayDebts() {
        ChangeBalance(-debts);
        debts = 0;
    }

    /**
     * Moves player on the game board
     *
     * @param shift Distance of moving
     */
    public void Move(int shift) {
        position = (position + shift) % fieldLength;
    }

    /**
     * Changes player's balance
     *
     * @param sum Amount of money which will be added
     */
    public void ChangeBalance(int sum) {
        money += sum;
    }

    /**
     * Tracks how much money player paid for upgrades
     *
     * @param sum Amount of money paid for upgrade
     */
    public void payForUpgrade(int sum) {
        upgradeMoney += sum;
    }

    public int MoneyLeft() {
        return money;
    }

    public int getPosition() {
        return position;
    }

    public String getShortName() {
        return shortName;
    }

    public int getUpgradeMoney() {
        return upgradeMoney;
    }

    public int getDebts() {
        return debts;
    }

    /**
     * Checks if player has any debts
     *
     * @return Yes, if player has debts, else no
     */
    public boolean isAnyDebt() {
        return debts > 0;
    }

    @Override
    public String toString() {
        return name;
    }
}