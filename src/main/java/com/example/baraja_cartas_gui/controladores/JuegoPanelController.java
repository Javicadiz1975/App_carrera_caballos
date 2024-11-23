package com.example.baraja_cartas_gui.controladores;

import com.example.baraja_cartas_gui.modelo.Jugadores.Jugador;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.example.baraja_cartas_gui.modelo.Croupier.Croupier;
import com.example.baraja_cartas_gui.modelo.baraja.Card;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;

import java.io.File;
import java.io.FileWriter;
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

    private static final String RUTA_ARCHIVO_CARTAS = "cartas_sacadas.txt"; // Archivo donde se almacenarán las cartas
    private static final String RUTA_ARCHIVO_PALOS_GANADORES = "palos_ganadores.txt"; // Archivo para guardar los palos ganadores

    private List<Card> cartasSacadas = new ArrayList<>();



    private final Map<CardSuit, ImageView> paloImageViewMap = new EnumMap<>(CardSuit.class); // Mapa para las imágenes de cada palo

    public JuegoPanelController() {
        this.croupier = new Croupier(jugadores);
        this.posiciones = new HashMap<>();
        this.meta = 8; // Longitud de la pista
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
            posiciones.put(palo, 1);
        }

        // Iniciar las rondas automáticas solo después de configurar los jugadores
        iniciarAutoRonda();
    }
    private void inicializarPista() {
        raceTrack.getChildren().clear(); // Limpia el contenido del GridPane

        // Añadir etiquetas "Salida" y "Llegada"
        Label salidaLabel = new Label("Salida");
        salidaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
        salidaLabel.setAlignment(Pos.CENTER);
        raceTrack.add(salidaLabel, 0, 0, 1, CardSuit.values().length); // Columna 0, ocupa todas las filas

        Label llegadaLabel = new Label("Llegada");
        llegadaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
        llegadaLabel.setAlignment(Pos.CENTER);
        raceTrack.add(llegadaLabel, 8, 0, 1, CardSuit.values().length); // Columna 8, ocupa todas las filas

        // Añadir carriles y caballos
        for (int row = 0; row < CardSuit.values().length; row++) {
            // Crear carriles
            Rectangle lane = new Rectangle(500, 70); // Ancho y alto del carril
            lane.setFill(row % 2 == 0 ? Color.LIGHTGRAY : Color.WHITESMOKE); // Alternar colores
            lane.setStroke(Color.BLACK); // Borde negro
            lane.setStrokeWidth(1); // Grosor del borde

            // Añadir el carril al GridPane
            raceTrack.add(lane, 1, row, 7, 1); // Carril abarca columnas de 1 a 7

            // Añadir imagen del caballo
            String imageName = "/images/" + CardSuit.values()[row].name().toUpperCase() + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            imageView.setPreserveRatio(true);

            paloImageViewMap.put(CardSuit.values()[row], imageView);
            raceTrack.add(imageView, 1, row); // Inicialmente en la columna 1

            // Guardar posición inicial del caballo
            posiciones.put(CardSuit.values()[row], 1);
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
            // Guardar la carta sacada en la lista
            cartasSacadas.add(cartaSacada);

            // Registrar la carta en el archivo
            registrarCartaEnArchivo(cartaSacada);

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
        if (posicionActual < meta) { // Asegura no sobrepasar la meta (8)
            posiciones.put(palo, posicionActual + 1);
            actualizarCaballo(palo);
        }
    }

    private void retrocederCaballo(CardSuit palo) {
        int posicionActual = posiciones.get(palo);
        if (posicionActual > 1) { // Asegura no retroceder antes de la columna 1
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
            if (posicion == meta) { // Verifica si la posición es igual a la meta (8)
                return palo; // Retorna el palo del caballo que ganó
            }
        }
        return null; // Retorna null si no hay ganador aún
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

        registrarPaloGanadorEnArchivo(ganadorPalo);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ganador de la carrera");
        alert.setHeaderText(null); // Sin encabezado para un diseño más limpio

        // Crear un contenedor principal
        VBox contenedorPrincipal = new VBox();
        contenedorPrincipal.setSpacing(15);
        contenedorPrincipal.setPadding(new Insets(20));

        // Título destacado
        Label titulo = new Label("¡Tenemos un ganador!");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setStyle("-fx-text-fill: #2a9d8f;"); // Verde azulado moderno
        contenedorPrincipal.getChildren().add(titulo);

        // Crear un contenedor para la información personalizada
        GridPane contenido = new GridPane();
        contenido.setHgap(10);
        contenido.setVgap(10);

        // Agregar los detalles del ganador
        contenido.add(new Label("Nombre del ganador:"), 0, 0);
        Label lblNombreGanador = new Label(nombreGanador);
        lblNombreGanador.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblNombreGanador.setStyle("-fx-text-fill: #264653;"); // Azul oscuro
        contenido.add(lblNombreGanador, 1, 0);

        contenido.add(new Label("Palo ganador:"), 0, 1);
        Label lblPaloGanador = new Label(ganadorPalo.name());
        lblPaloGanador.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblPaloGanador.setStyle("-fx-text-fill: #e76f51;"); // Rojo tierra
        contenido.add(lblPaloGanador, 1, 1);

        contenido.add(new Label("Bote acumulado:"), 0, 2);
        Label lblBote = new Label(boteAcumulado + " €");
        lblBote.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblBote.setStyle("-fx-text-fill: #f4a261;"); // Naranja moderno
        contenido.add(lblBote, 1, 2);

        // Agregar el contenido al contenedor principal
        contenedorPrincipal.getChildren().add(contenido);

        // Mostrar la imagen del caballo ganador
        String imagePath = "src/main/resources/images/" + ganadorPalo.name() + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            ImageView cartaImagen = new ImageView(new Image(imageFile.toURI().toString()));
            cartaImagen.setFitHeight(150);
            cartaImagen.setFitWidth(100);
            cartaImagen.setStyle("-fx-border-color: #264653; -fx-border-width: 2;"); // Borde decorativo

            HBox imagenContenedor = new HBox();
            imagenContenedor.setSpacing(10);
            imagenContenedor.getChildren().addAll(new Label("El caballo ganador:"), cartaImagen);
            contenedorPrincipal.getChildren().add(imagenContenedor);
        }

        // Agregar el contenido personalizado al diálogo
        alert.getDialogPane().setContent(contenedorPrincipal);
        alert.setOnHidden(dialogEvent -> volverABienvenida());

        // Mostrar la alerta
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

    private void registrarCartaEnArchivo(Card carta) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO_CARTAS, true)) { // `true` habilita el modo append
            writer.write(carta.getDescription() + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error al registrar la carta en el archivo: " + e.getMessage());
        }
    }

    private void registrarPaloGanadorEnArchivo(CardSuit paloGanador) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO_PALOS_GANADORES, true)) { // `true` habilita el modo append
            writer.write(paloGanador.name() + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error al registrar el palo ganador en el archivo: " + e.getMessage());
        }
    }
}




