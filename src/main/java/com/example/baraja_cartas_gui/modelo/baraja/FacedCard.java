package com.example.baraja_cartas_gui.modelo.baraja;

public class FacedCard extends Card{

    private CardFace face;

    public FacedCard(CardFace cardFace, CardSuit pal) {
        super.value = 0.5f;
        super.suit = pal;
        this.face = cardFace;
    }

    @Override
    public String getDescription() {
        return face + " of " + suit;
    }

}