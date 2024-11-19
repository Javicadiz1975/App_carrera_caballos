package com.example.baraja_cartas_gui.modelo.baraja;


public abstract class Card {
    protected CardSuit suit;
    protected float value;


    public CardSuit getSuit() {
        return suit;
    }

    public abstract String getDescription();



}
