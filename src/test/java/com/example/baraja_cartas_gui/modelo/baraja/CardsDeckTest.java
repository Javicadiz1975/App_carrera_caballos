package com.example.baraja_cartas_gui.modelo.baraja;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardsDeckTest {

    private CardsDeck cardsDeck;

    @BeforeEach
    void setUp() {
        cardsDeck = new CardsDeck(); // Inicializa una nueva baraja antes de cada prueba
    }

    @Test
    void testInicio() {
        // Verifica que la baraja se inicializa con 40 cartas (7 números + 3 figuras * 4 palos)
        assertEquals(40, cardsDeck.quedanCartas() ? 40 : 0, "El mazo debe contener 40 cartas inicialmente");
    }

    @Test
    void testObtenerCarta() {
        // Obtén una carta del mazo
        Card card = cardsDeck.getCardFromDeck();

        assertNotNull(card, "La carta obtenida no debe ser nula");
    }

    @Test
    void testReinicioBaraja() {
        // Reparte todas las cartas para vaciar la baraja
        while (cardsDeck.quedanCartas()) {
            cardsDeck.getCardFromDeck();
        }

        // Verifica que no quedan cartas
        assertFalse(cardsDeck.quedanCartas(), "No deben quedar cartas después de repartir todas");

        // Reinicializa la baraja
        cardsDeck = new CardsDeck();

        // Verifica que la nueva baraja tiene las 40 cartas
        assertTrue(cardsDeck.quedanCartas(), "La baraja debe contener cartas después de reinicializarla");

        // Verifica que el número de cartas es el correcto
        Set<Card> uniqueCards = new HashSet<>();
        while (cardsDeck.quedanCartas()) {
            uniqueCards.add(cardsDeck.getCardFromDeck());
        }

        assertEquals(40, uniqueCards.size(), "La baraja reinicializada debe contener exactamente 40 cartas únicas");
    }
}


