package com.example.baraja_cartas_gui.modelo.Jugadores;

import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

import java.util.Random;

public class BotJugador extends Jugador implements JugadorAcciones {

    public BotJugador(String nombre) {
        super(nombre, false);  // No es humano, es un bot
    }

    // Los bots realizan una apuesta aleatoria
    @Override
    public void realizarApuesta(int apuesta) {
        Random random = new Random();
        this.apuesta = random.nextInt(100) + 1; // Apuesta aleatoria entre 1 y 100
        System.out.println(nombre + " ha apostado " + this.apuesta);
    }

    // Los bots eligen un palo aleatorio
    @Override
    public void elegirPalo(CardSuit paloElegido) {
        Random random = new Random();
        int opcion = random.nextInt(4) + 1;

        switch (opcion) {
            case 1:
                this.paloElegido = CardSuit.GOLD;
                break;
            case 2:
                this.paloElegido = CardSuit.CUPS;
                break;
            case 3:
                this.paloElegido = CardSuit.SWORDS;
                break;
            case 4:
                this.paloElegido = CardSuit.CLUBS;
                break;
        }
        System.out.println(nombre + " ha elegido el palo: " + this.paloElegido);
    }
}

