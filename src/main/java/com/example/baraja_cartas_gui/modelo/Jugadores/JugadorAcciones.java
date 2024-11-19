package com.example.baraja_cartas_gui.modelo.Jugadores;

import  com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

/**
 * Interfaz que define las acciones esenciales que todos los jugadores deben poder realizar en el juego de carreras de caballos con cartas.
 */
public interface JugadorAcciones {

        /**
         * Realiza una apuesta con el monto especificado.
         * Este método debe ser implementado para manejar la lógica de apuestas del jugador.
         *
         * @param apuesta El monto de la apuesta a realizar.
         */
        void realizarApuesta(int apuesta);

        /**
         * Elige un palo de cartas como representación en el juego.
         * Este método debe ser implementado para permitir al jugador seleccionar un palo de la baraja,
         * que será utilizado en la dinámica del juego.
         *
         * @param paloElegido El palo de cartas a elegir.
         */
        void elegirPalo(CardSuit paloElegido);
}
