package com.example.baraja_cartas_gui.modelo.Jugadores;
import  com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

/**
 * Clase abstracta que define las características y funcionalidades comunes de un jugador en el juego de carreras de caballos con cartas.
 * Esta clase es extendida por tipos específicos de jugadores, como jugadores humanos y bots.
 */
public abstract class Jugador {
    protected String nombre;       // El nombre del jugador
    protected int apuesta;         // La cantidad apostada por el jugador
    protected boolean esHumano;    // Indicador de si el jugador es humano
    protected CardSuit paloElegido; // El palo de cartas elegido por el jugador

    /**
     * Constructor que inicializa un jugador con su nombre y si es humano.
     *
     * @param nombre El nombre del jugador.
     * @param esHumano Verdadero si el jugador es humano, falso en caso contrario.
     */
    public Jugador(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.esHumano = esHumano;
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return El nombre del jugador como una cadena de texto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la apuesta realizada por el jugador.
     *
     * @return La cantidad de la apuesta.
     */
    public int getApuesta() {
        return apuesta;
    }

    /**
     * Devuelve el palo de cartas elegido por el jugador.
     *
     * @return El palo de cartas como un elemento de la enumeración {@link CardSuit}.
     */
    public CardSuit getPaloElegido() {
        return paloElegido;
    }

    /**
     * Representa al jugador como una cadena de texto que incluye su nombre, apuesta y palo elegido.
     * Este método sobrescribe el método {@code toString} de la clase {@code Object}.
     *
     * @return Una representación textual del jugador.
     */
    @Override
    public String toString() {
        return "Jugador: " + nombre + ", Apuesta: " + apuesta + ", Palo elegido: " + paloElegido;
    }
}
