package com.example.baraja_cartas_gui.modelo.baraja;

public class NumeredCard extends Card{

    private int num;
    /**
     * Constructor per crear les Cartes amb el seu cardSuit i el seu valor cridant un metode helper
     *
     * @param num --> Numero de carta
     * @param cardSuit --> Pal de la carta
     */
    public NumeredCard(int num, CardSuit cardSuit) {
        this.num = num;
        super.suit = cardSuit;
        super.value = num;
    }

    @Override
    public String getDescription() {

        return num + " of " + suit;
    }



}

