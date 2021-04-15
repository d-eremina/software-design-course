package com.homework.monopoly;

import java.util.Random;

public class Shop extends Cell {
    private Player owner;

    private int N;
    private int K;
    public final double compensationCoefficient;
    public final double improvementCoefficient;

    public Shop(int N) {
        this.N = N;
        Random rnd = new Random();
        K = rnd.nextInt((int) Math.round(0.4 * N) + 1) + (int) Math.round(0.5 * N);
        compensationCoefficient = rnd.nextDouble() * (0.9 - 0.1) + 0.1;
        improvementCoefficient = rnd.nextDouble() * (2 - 0.1) + 0.1;
    }

    /**
     * Allows players to buy this shop
     *
     * @param player Player who want to buy this shop
     * @return False if player can not afford this shop, else true
     */
    public boolean Buy(Player player) {
        if (player.MoneyLeft() < N) {
            return false;
        }
        owner = player;
        owner.payForUpgrade(N);
        player.ChangeBalance(-N);
        return true;
    }

    /**
     * Allows player who owns this shop to upgrade this shop
     *
     * @return False if player can not allow upgrade, else true
     */
    public boolean Upgrade() {
        int sum = (int) Math.round(N * improvementCoefficient);
        if (owner.MoneyLeft() < sum) {
            return false;
        }
        owner.ChangeBalance(-sum);
        owner.payForUpgrade(sum);
        N += sum;
        K += Math.round(compensationCoefficient * K);
        return true;
    }

    /**
     * Shows who is the owner of this shop
     *
     * @return Name of the owner of this shop
     */
    public String Owner() {
        // If nobody bought this shop yet
        if (owner == null) {
            return "None";
        }
        return owner.toString();
    }

    /**
     * Get-accessor for N
     *
     * @return Value of N
     */
    public int getN() {
        return N;
    }

    /**
     * Get-accessor for K
     *
     * @return Value of K
     */
    public int getK() {
        return K;
    }

    /**
     * String representation of class object
     *
     * @return String representation of this shop
     */
    @Override
    public String toString() {
        // If shop has an owner, returns his short name
        if (owner != null)
            return owner.getShortName();
        return "S";
    }
}
