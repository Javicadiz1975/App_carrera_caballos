package com.example.baraja_cartas_gui.modelo.Croupier;


import com.example.baraja_cartas_gui.modelo.baraja.Card;
import com.example.baraja_cartas_gui.modelo.baraja.CardsDeck;

/**
 * Clase Croupier que maneja el mazo de cartas y distribuye cartas a los jugadores durante el juego.
 */
public class Croupier {
    private CardsDeck cardsDeck; // El mazo de cartas utilizado por el croupier

    /**
     * Constructor que inicializa el croupier con un mazo de cartas nuevo.
     */
    public Croupier() {
        cardsDeck = new CardsDeck(); // Crea un nuevo mazo de cartas
    }

    /**
     * Reparte una carta del mazo. Si el mazo se ha agotado, reinicia la baraja antes de repartir.
     *
     * @return La carta repartida del mazo. Si no quedan cartas, primero se reinicia el mazo y luego se reparte.
     */
    public Card repartirCarta() {
        if (!cardsDeck.quedanCartas()) {
            reiniciarBaraja(); // Reinicia el mazo si no quedan cartas
        }
        return cardsDeck.getCardFromDeck(); // Reparte una carta aleatoria
    }

    /**
     * Reinicia el mazo de cartas creando un nuevo mazo, listo para una nueva partida.
     * Este método asegura que siempre haya cartas disponibles para repartir.
     */
    public void reiniciarBaraja() {
        cardsDeck = new CardsDeck(); // Vuelve a crear el mazo
        System.out.println("La baraja ha sido reiniciada.");
    }
}
