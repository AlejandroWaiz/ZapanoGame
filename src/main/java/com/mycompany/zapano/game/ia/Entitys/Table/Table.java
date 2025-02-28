/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zapano.game.ia.Entitys.Table;

import java.util.ArrayList;

/**
 *
 * @author Aleja
 */
public class Table {
    
    ArrayList<Card> allCards;
    ArrayList<Card> UserdDeck;

    String winCondition;
    
    
    public Table(Integer decksQuantity){
        
     allCards = createNewDecks(decksQuantity);
         
    }
        
    public ArrayList<Card> createNewDecks(Integer decksQuantity ){
        
      ArrayList<Card> cards = new ArrayList<>();
      for(int a = 0; a < decksQuantity; a++){
          for(int i = 0; i < Card.posibleSuits.size(); i++){
          for(int j = 0; j < Card.posibleRanks.size(); j++){
              Card card = new Card(j + 1,Card.posibleRanks.get(j),Card.posibleSuits.get(i));
              cards.add(card);
              System.out.println(card.getSuit() + "" + card.getValue());
          }
        }
      }
   
      return cards;
      
    }
    
    
    public ArrayList<Card> getAllCards() {
        return allCards;
    }
 
    public ArrayList<Card> getUsersDeck() {
        return UserdDeck;
    }

    
    public String getWinCondition(){
        return this.winCondition;
    }
    
}
