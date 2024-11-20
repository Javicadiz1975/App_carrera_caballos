package com.example.baraja_cartas_gui.controladores;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.example.baraja_cartas_gui.modelo.Croupier.Croupier;
import com.example.baraja_cartas_gui.modelo.baraja.Card;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JuegoPanelController {
    @FXML
    private GridPane raceTrack; // Representa la pista de carrera
    @FXML
    private ImageView ImagenCarta; // Imagen de la carta sacada

    @FXML
    private Label descripcionCarta;

    private final Croupier croupier;
    private final Map<CardSuit, Integer> posiciones;
    private final int meta;

    private Timeline timeline; // Control de la secuencia de juego automática
    int rondaActual = 0;



    private final Map<CardSuit, ImageView> paloImageViewMap = new EnumMap<>(CardSuit.class); // Mapa para las imágenes de cada palo

    public JuegoPanelController() {
        this.croupier = new Croupier();
        this.posiciones = new HashMap<>();
        this.meta = 9; // Longitud de la pista
    }

    @FXML
    private void initialize() {
        inicializarPista();
        iniciarAutoRonda();
    }

    private void inicializarPista() {
        raceTrack.getChildren().clear();
        for (CardSuit palo : CardSuit.values()) {
            String imageName = "/images/" + palo.name().toUpperCase() + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setPreserveRatio(true);

            paloImageViewMap.put(palo, imageView);
            raceTrack.add(imageView, 0, palo.ordinal());
            posiciones.put(palo, 0);
        }
    }

    private void iniciarAutoRonda() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> iniciarRonda()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void iniciarRonda() {
        Card cartaSacada = croupier.repartirCarta();
        if (cartaSacada != null) {
            descripcionCarta.setText(cartaSacada.getDescription());
            String archivoCarta = cartaSacada.getDescription().replace(" of ", "_");
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

            // Verificar ganador después de mover el caballo
            CardSuit ganador = verificarGanador();
            if (ganador != null) {
                Platform.runLater(() -> mostrarAlertaGanador(ganador));
                timeline.stop();  // Detener el juego automáticamente después de un ganador
            }
        }
    }

    private void avanzarCaballo(CardSuit palo) {
        int posicionActual = posiciones.get(palo);
        if (posicionActual < raceTrack.getColumnConstraints().size() - 1) { // Asegura no sobrepasar el número de columnas
            posiciones.put(palo, posicionActual + 1);
            actualizarCaballo(palo);
        }
    }

    private void retrocederCaballo(CardSuit palo) {
        int posicionActual = posiciones.get(palo);
        if (posicionActual > 0) {
            posiciones.put(palo, posicionActual - 1);
            actualizarCaballo(palo);
        }
    }

    private void actualizarCaballo(CardSuit palo) {
        ImageView imageView = paloImageViewMap.get(palo);
        if (imageView != null) {
            // Asegúrate de remover el imageView solo si es necesario
            raceTrack.getChildren().remove(imageView);
            int nuevaPosicion = posiciones.get(palo);
            raceTrack.add(imageView, nuevaPosicion % raceTrack.getColumnConstraints().size(), palo.ordinal()); // Añadir en la nueva posición, asegurando que la columna exista
        }
    }

    public CardSuit verificarGanador() {
        for (CardSuit palo : CardSuit.values()) {
            int posicion = posiciones.get(palo);
            if (posicion >= meta) {
                return palo;  // Retorna el palo del caballo que ganó
            }
        }
        return null;  // Retorna null si no hay ganador aún
    }

    private void mostrarAlertaGanador(CardSuit ganador) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ganador de la carrera");
        alert.setHeaderText("¡Tenemos un ganador!");
        alert.setContentText("El caballo del palo " + ganador.name() + " ha ganado la carrera.");
        alert.showAndWait();
    }
}




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
