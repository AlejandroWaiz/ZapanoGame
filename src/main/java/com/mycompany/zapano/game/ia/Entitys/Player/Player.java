/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zapano.game.ia.Entitys.Player;

import java.util.ArrayList;
import com.mycompany.zapano.game.ia.Entitys.Table.Card;

/**
 *
 * @author Aleja
 */
public abstract class Player {

    public String name;
    public Integer handicap;
    public Integer onFavorMeter;
    public Integer totalValueAddition;
    public ArrayList<Card> deck;

    // Constructor para jugador estandar (Usuario y oponente en nivel normal)
    public Player(String name) {
        this.name = name;
        this.handicap = 0;
        this.onFavorMeter = 0;
        this.totalValueAddition = 0;
        this.deck = new ArrayList<>(); // Inicialización del deck
    }

    public Player(String name, Integer handicap, Integer onFavorMeter) {
        this.name = name;
        this.handicap = 0;
        this.onFavorMeter = 0;
        this.deck = new ArrayList<>(); // Inicialización del deck
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHandicap() {
        return handicap;
    }

    public void setHandicap(Integer handicap) {
        this.handicap = handicap;
    }

    public Integer getOnFavorMeter() {
        return onFavorMeter;
    }

    public void setOnFavorMeter(Integer onFavorMeter) {
        this.onFavorMeter = onFavorMeter;
    }

}
