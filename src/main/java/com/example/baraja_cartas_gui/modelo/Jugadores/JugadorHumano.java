package com.example.baraja_cartas_gui.modelo.Jugadores;
import  com.example.baraja_cartas_gui.modelo.baraja.CardSuit;


/**
 * Representa un jugador humano en el juego de carreras de caballos con cartas.
 * Esta clase extiende a {@link Jugador} e implementa las acciones específicas que un jugador humano puede realizar,
 * como realizar apuestas y elegir un palo de cartas.
 */
public class JugadorHumano extends Jugador implements JugadorAcciones {

    /**
     * Constructor que inicializa un nuevo jugador humano con su nombre.
     *
     * @param nombre El nombre del jugador humano.
     */
    public JugadorHumano(String nombre) {
        super(nombre, true);  // Llama al constructor de la clase base Jugador indicando que es un humano
    }

    /**
     * Realiza una apuesta con el monto especificado y registra la apuesta en la consola.
     * Este método sobrescribe el comportamiento de apuestas de la clase base para adaptarlo a las interacciones humanas.
     *
     * @param apuesta El monto de la apuesta a realizar.
     */
    @Override
    public void realizarApuesta(int apuesta) {
        this.apuesta = apuesta;  // Establece la apuesta en el atributo heredado
        System.out.println(nombre + " ha apostado " + this.apuesta);
    }

    /**
     * Elige un palo de cartas que representará al jugador en el juego.
     * Este método sobrescribe la acción de elegir palo para que el jugador humano pueda seleccionarlo explícitamente.
     *
     * @param paloElegido El palo de cartas elegido por el jugador humano.
     */
    @Override
    public void elegirPalo(CardSuit paloElegido) {
        this.paloElegido = paloElegido;  // Establece el palo elegido en el atributo heredado
        System.out.println(nombre + " ha elegido el palo: " + this.paloElegido);
    }
}








