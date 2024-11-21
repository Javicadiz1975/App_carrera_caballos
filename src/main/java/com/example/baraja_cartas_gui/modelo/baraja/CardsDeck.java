package com.example.baraja_cartas_gui.modelo.baraja;

import java.util.ArrayList;

public class CardsDeck {
    private ArrayList<Card> cardsDeck = new ArrayList<>();
    private int[] num = {1, 2, 3, 4, 5, 6, 7};
    private CardSuit[] cardSuits = {CardSuit.GOLD, CardSuit.CLUBS, CardSuit.CUPS, CardSuit.SWORDS};

    private CardFace[] cardFaces = {CardFace.JACK, CardFace.KNIGHT, CardFace.KING};
    private ArrayList<Integer> numCartes;

    /**
     * Constructor que crea un nuevo mazo de cartas con 40 cartas.
     */
    public CardsDeck() {
        initializeDeck(); // Llama a la inicialización al crear la instancia
    }

    /**
     * Inicializa la baraja con las 40 cartas y resetea las listas de control.
     */
    private void initializeDeck() {
        cardsDeck.clear(); // Limpia el mazo previo
        numCartes = new ArrayList<>(); // Reinicia el registro de cartas repartidas

        // Generar cartas numeradas
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < cardSuits.length; j++) {
                cardsDeck.add(new NumeredCard(num[i], cardSuits[j]));
            }
        }

        // Generar cartas con caras
        for (CardFace face : cardFaces) {
            for (CardSuit suit : cardSuits) {
                cardsDeck.add(new FacedCard(face, suit));
            }
        }

        // Validar que el tamaño sea exactamente 40
        if (cardsDeck.size() != 40) {
            throw new IllegalStateException("El mazo debe contener exactamente 40 cartas. Tamaño actual: " + cardsDeck.size());
        }
    }

    /**
     * Obtiene una carta aleatoria del mazo.
     *
     * @return La carta repartida.
     */
    public Card getCardFromDeck() {
        if (!quedanCartas()) {
            throw new IllegalStateException("No quedan cartas en el mazo para repartir.");
        }

        int numcarta;
        do {
            numcarta = (int) (Math.random() * cardsDeck.size());
        } while (numCartes.contains(numcarta));

        numCartes.add(numcarta); // Marca la carta como repartida
        return cardsDeck.get(numcarta);
    }

    /**
     * Verifica si quedan cartas disponibles en el mazo.
     *
     * @return true si quedan cartas, false en caso contrario.
     */
    public boolean quedanCartas() {
        return numCartes.size() < cardsDeck.size();
    }
}
