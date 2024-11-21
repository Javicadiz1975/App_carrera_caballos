package com.example.baraja_cartas_gui.controladores;

import com.example.baraja_cartas_gui.modelo.Jugadores.Jugador;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.example.baraja_cartas_gui.modelo.Croupier.Croupier;
import com.example.baraja_cartas_gui.modelo.baraja.Card;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    private List<Jugador> jugadores;



    private final Map<CardSuit, ImageView> paloImageViewMap = new EnumMap<>(CardSuit.class); // Mapa para las imágenes de cada palo

    public JuegoPanelController() {
        this.croupier = new Croupier(jugadores);
        this.posiciones = new HashMap<>();
        this.meta = 9; // Longitud de la pista
    }

    @FXML
    private void initialize() {
        inicializarPista();

    }
    public void iniciarJuegoConJugadores(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            throw new IllegalArgumentException("La lista de jugadores no puede ser nula ni vacía.");
        }
        this.jugadores = jugadores;

        // Inicializar posiciones de los caballos
        for (CardSuit palo : CardSuit.values()) {
            posiciones.put(palo, 0);
        }

        // Iniciar las rondas automáticas solo después de configurar los jugadores
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
            CardSuit ganadorPalo = verificarGanador();
            if (ganadorPalo != null) {
                Platform.runLater(() -> mostrarAlertaGanador(ganadorPalo, cartaSacada));
                timeline.stop(); // Detener el juego automáticamente después de un ganador
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

    private void mostrarAlertaGanador(CardSuit ganadorPalo, Card cartaGanadora) {
        // Buscar el nombre del ganador (humano o bot)
        String nombreGanador = jugadores.stream()
                .filter(jugador -> jugador.getPaloElegido() == ganadorPalo) // Comparar el palo ganador
                .map(Jugador::getNombre) // Obtener el nombre del jugador
                .findFirst()
                .orElse("Bot " + ganadorPalo.name()); // Si no encuentra, asumimos que es un bot y usamos su palo como referencia

        // Calcular el bote acumulado
        int boteAcumulado = jugadores.stream()
                .mapToInt(Jugador::getApuesta)
                .sum();

        // Crear el diálogo de alerta
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ganador de la carrera");
        alert.setHeaderText("¡Tenemos un ganador!");
        alert.setContentText("Detalles de la carrera:");

        // Crear un contenedor para la información personalizada
        GridPane contenido = new GridPane();
        contenido.setHgap(10);
        contenido.setVgap(10);

        // Agregar el texto con la información
        contenido.add(new Label("Nombre del ganador:"), 0, 0);
        contenido.add(new Label(nombreGanador), 1, 0);

        contenido.add(new Label("Palo ganador:"), 0, 1);
        contenido.add(new Label(ganadorPalo.name()), 1, 1);

        contenido.add(new Label("Carta ganadora:"), 0, 2);
        contenido.add(new Label(cartaGanadora.getDescription()), 1, 2);

        contenido.add(new Label("Bote acumulado:"), 0, 3);
        contenido.add(new Label(boteAcumulado + " €"), 1, 3);

        // Mostrar la imagen de la carta ganadora
        String imagePath = "src/main/resources/images/" + cartaGanadora.getDescription().replace(" of ", "_") + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageView cartaImagen = new ImageView(new Image(imageFile.toURI().toString()));
            cartaImagen.setFitHeight(100);
            cartaImagen.setFitWidth(70);
            contenido.add(new Label("Imagen de la carta:"), 0, 4);
            contenido.add(cartaImagen, 1, 4);
        }

        // Agregar el contenido personalizado al diálogo
        alert.getDialogPane().setContent(contenido);
        alert.setOnHidden(dialogEvent -> volverABienvenida());

        alert.showAndWait();
    }


    private void volverABienvenida() {
        try {
            // Cargar la pantalla de bienvenida
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/bienvenida.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) raceTrack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bienvenida");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




