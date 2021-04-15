package com.homework.monopoly;

import java.util.Random;

public class Bank extends Cell {
    public final double creditCoefficient;
    public final double debtCoefficient;

    public Bank(){
        Random rnd = new Random();
        creditCoefficient = rnd.nextDouble() * (0.2 - 0.002) + 0.002;
        debtCoefficient = rnd.nextDouble() * (3 - 1) + 1;
    }

    /**
     * Represents Banks on game board
     * @return Symbol "$" to represent Banks on field
     */
    @Override
    public String toString() {
        return "$";
    }
}
