package com.homework.monopoly;

import java.util.Random;

public class PenaltyCell extends Cell {
    public final double penaltyCoefficient;

    public PenaltyCell(){
        Random rnd = new Random();
        penaltyCoefficient = rnd.nextDouble() * (0.1 - 0.01) + 0.01;
    }

    /**
     * Represents this type of cell on field
     * @return Symbol "%" to represent penalty cell
     */
    @Override
    public String toString() {
        return "%";
    }
}
