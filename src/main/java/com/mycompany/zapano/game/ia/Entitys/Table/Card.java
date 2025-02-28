/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zapano.game.ia.Entitys.Table;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aleja
 */
public class Card {

    public static List<String> getPosibleSuits() {
        return posibleSuits;
    }

    public static List<String> getPosibleRanks() {
        return posibleRanks;
    }

    //This is the "number" of the card, from 1 to 13
    Integer value;
    //And this is what is printed in the card, from A to J,Q,K
    String rank;

    String suit;

    static List<String> posibleSuits = Arrays.asList("Corazones", "Diamantes", "Tréboles", "Picas");
    static List<String> posibleRanks = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");

    public Card(Integer value, String rank, String suit) {

        this.value = value;
        this.rank = rank;
        this.suit = suit;

    }

    public String getRank() {
        return rank;
    }

    public Integer getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }
    
    @Override
    public String toString() {
        return "Carta de " + suit + " con valor " + value;
    }

}
