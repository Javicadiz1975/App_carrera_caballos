package com.example.baraja_cartas_gui.modelo.controladores;

import com.example.baraja_cartas_gui.modelo.Croupier.Croupier;
import com.example.baraja_cartas_gui.modelo.baraja.Card;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JuegoPanelController {
    @FXML
    private GridPane raceTrack; // Representa la pista de carrera
    @FXML
    private ImageView ImagenCarta; // Imagen de la carta sacada
    @FXML
    private Button iniciarRondaButton; // Botón para iniciar la ronda

    private final Croupier croupier;
    private final Map<CardSuit, Integer> posiciones;
    private final int meta;

    int rondaActual = 0;

    public JuegoPanelController() {
        this.croupier = new Croupier();
        this.posiciones = new HashMap<>();
        this.meta = 9; // Longitud de la pista
    }

    @FXML
    private void initialize() {
        inicializarPista();
    }

    private void inicializarPista() {
        raceTrack.getChildren().clear(); // Limpia la pista
        for (CardSuit palo : CardSuit.values()) {
            Text caballo = new Text(palo.name());
            raceTrack.add(caballo, 0, palo.ordinal()); // Posición inicial en la columna 0
            posiciones.put(palo, 0); // Posición inicial
        }
    }

    @FXML
    private void iniciarRonda() {
        Card cartaSacada = croupier.repartirCarta();
        System.out.println(cartaSacada.getDescription());
        String archivoCarta = cartaSacada.getDescription().replace(" of ","_");
        System.out.println(archivoCarta);




        if (cartaSacada != null) {
            // Mostrar imagen de la carta
            String imagePath = "src/main/resources/images/" + archivoCarta + ".png";
            ImagenCarta.setImage(new Image(new File(imagePath).toURI().toString()));

            // Mover caballo
            CardSuit palo = cartaSacada.getSuit();
            if (rondaActual % 4 == 0) {
                retrocederCaballo(palo);
            } else {
                avanzarCaballo(palo);
            }
            rondaActual++;


        }
    }

    private void avanzarCaballo(CardSuit palo) {
        int posicionActual = posiciones.get(palo);
        posiciones.put(palo, posicionActual + 1);
        actualizarCaballo(palo);
    }
    private void retrocederCaballo(CardSuit palo) {
        int posicionActual = posiciones.get(palo);
        if (posicionActual > 0) {
            posiciones.put(palo, posicionActual - 1);
            actualizarCaballo(palo);
        }
    }



    private void actualizarCaballo(CardSuit palo) {
        raceTrack.getChildren().removeIf(node -> GridPane.getRowIndex(node) == palo.ordinal());

        Text caballo = new Text(palo.name());
        int nuevaPosicion = posiciones.get(palo);
        raceTrack.add(caballo, nuevaPosicion, palo.ordinal());
    }


}









//    /**
//     * Verifica si algún caballo ha alcanzado o superado la meta.
//     *
//     * @return Verdadero si hay un ganador, falso en caso contrario.
//     */
//    public boolean verificarGanador() {
//        for (int posicion : posiciones.values()) {
//            if (posicion >= meta) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Obtiene las posiciones actuales de todos los caballos en la carrera.
//     *
//     * @return Un mapa que relaciona cada palo de cartas con su posición actual en la pista.
//     */
//    public Map<CardSuit, Integer> obtenerPosiciones() {
//        return posiciones;
//    }
//
//    /**
//     * Determina el palo del caballo ganador, si existe.
//     *
//     * @return El palo del caballo que ha alcanzado primero la meta, o null si no hay un ganador todavía.
//     */
//    public CardSuit obtenerPaloGanador() {
//        for (Map.Entry<CardSuit, Integer> entry : posiciones.entrySet()) {
//            if (entry.getValue() >= meta) {
//                return entry.getKey();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Identifica al jugador que ha ganado la carrera basado en el palo ganador.
//     *
//     * @param paloGanador El palo del caballo ganador.
//     * @return El jugador que ha ganado, o null si no se ha determinado un ganador.
//     */

