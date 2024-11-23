package com.example.baraja_cartas_gui.controladores;

import com.example.baraja_cartas_gui.modelo.Jugadores.BotJugador;
import com.example.baraja_cartas_gui.modelo.Jugadores.Jugador;
import com.example.baraja_cartas_gui.modelo.Jugadores.JugadorHumano;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ConfigJugadoresController {

    @FXML
    private Text tituloLabel;

    @FXML
    private TextField nombreField;

    @FXML
    private ChoiceBox<String> paloChoiceBox;

    @FXML
    private TextField apuestaField;

    @FXML
    private Button botonAccion; // Botón dinámico

    private int numeroJugadoresHumanos; // Número de jugadores humanos
    private int jugadorActual = 1;

    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<CardSuit> palosElegidos = new ArrayList<>();
    @FXML
    private ImageView ImagenCarta; // Imagen del croupier

    @FXML
    public void initialize() {
        // Cargar la imagen del croupier para la pantalla de configuración
        String croupierImagePath = "/images/croupier_circular.png";
        URL croupierImageUrl = getClass().getResource(croupierImagePath);
        if (croupierImageUrl != null) {
            ImagenCarta.setImage(new Image(croupierImageUrl.toExternalForm()));
        } else {
            System.err.println("Error: No se encontró la imagen en la ruta: " + croupierImagePath);
        }

        // Inicializar el choice box
        paloChoiceBox.getItems().addAll("GOLD", "CUPS", "SWORDS", "CLUBS");

        // Preguntar número de jugadores
        preguntarNumeroJugadores();
    }

    public void preguntarNumeroJugadores() {
        // Crear un nuevo diálogo personalizado
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Configuración de Jugadores");

        // Contenedor principal
        VBox contenido = new VBox();
        contenido.setSpacing(15);
        contenido.setPadding(new Insets(20));

        // Cargar la imagen específica para este diálogo
        String imagePath = "/images/bienvenida.PNG"; // Imagen para "Número de Jugadores"
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            System.err.println("Error: No se encontró la imagen en la ruta: " + imagePath);
            return; // Salir si no se encuentra la imagen
        }
        ImageView imageViewNumeroJugadores = new ImageView(new Image(imageUrl.toExternalForm()));
        imageViewNumeroJugadores.setFitHeight(200);
        imageViewNumeroJugadores.setFitWidth(400);
        imageViewNumeroJugadores.setPreserveRatio(true);
        contenido.getChildren().add(imageViewNumeroJugadores);

        // Título del diálogo
        Label titulo = new Label("Número de Jugadores Humanos");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.web("#2a9d8f")); // Verde moderno
        contenido.getChildren().add(titulo);

        // Subtítulo
        Label subtitulo = new Label("Introduce un número entre 1 y 4:");
        subtitulo.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitulo.setTextFill(Color.web("#264653")); // Azul oscuro
        contenido.getChildren().add(subtitulo);

        // Campo de entrada
        TextField input = new TextField();
        input.setPromptText("1 - 4");
        input.setStyle("-fx-border-color: #e76f51; -fx-border-radius: 5; -fx-padding: 5;");
        contenido.getChildren().add(input);

        // Contenedor para los botones
        HBox botones = new HBox();
        botones.setSpacing(10);
        botones.setPadding(new Insets(10, 0, 0, 0));
        botones.setStyle("-fx-alignment: center-right;");

        // Botón de Aceptar
        Button aceptar = new Button("Aceptar");
        aceptar.setStyle("-fx-background-color: #2a9d8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10;");
        aceptar.setOnAction(e -> {
            try {
                int seleccionados = Integer.parseInt(input.getText().trim());
                if (seleccionados >= 1 && seleccionados <= 4) {
                    numeroJugadoresHumanos = seleccionados;
                    actualizarTitulo();
                    actualizarBoton();
                    dialog.close(); // Cerrar el diálogo
                } else {
                    subtitulo.setText("Error: Número fuera de rango (1-4).");
                    subtitulo.setTextFill(Color.web("#e76f51")); // Rojo para errores
                }
            } catch (NumberFormatException ex) {
                subtitulo.setText("Error: Entrada no válida.");
                subtitulo.setTextFill(Color.web("#e76f51")); // Rojo para errores
            }
        });

        // Botón de Cancelar
        Button cancelar = new Button("Cancelar");
        cancelar.setStyle("-fx-background-color: #e63946; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10;");
        cancelar.setOnAction(e -> {
            subtitulo.setText("Error: No seleccionaste el número de jugadores.");
            subtitulo.setTextFill(Color.web("#e76f51")); // Rojo para errores
            dialog.close(); // Cerrar el diálogo
        });

        botones.getChildren().addAll(cancelar, aceptar);
        contenido.getChildren().add(botones);

        // Configurar el contenedor principal en una escena
        Scene scene = new Scene(contenido);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    private void actualizarTitulo() {
        tituloLabel.setText("Jugador " + jugadorActual + " de " + numeroJugadoresHumanos);
    }

    private void actualizarBoton() {
        // Si ya hemos configurado el último jugador o si solo hay un jugador humano
        if (jugadorActual > numeroJugadoresHumanos) {
            botonAccion.setText("Iniciar Partida");
        } else {
            botonAccion.setText("Configurar Próximo Jugador");
        }
    }

    @FXML
    public void manejarBotonAccion(ActionEvent event) {
        if (jugadorActual > numeroJugadoresHumanos) {
            // Inicia la partida si se ha configurado el último jugador
            iniciarPartida(event);
        } else {
            // Configura el próximo jugador
            configurarJugador(event);
        }
    }

    private void configurarJugador(ActionEvent event) {
        String nombre = nombreField.getText();
        String palo = paloChoiceBox.getValue();
        String apuesta = apuestaField.getText();

        if (nombre.isEmpty() || palo == null || apuesta.isEmpty()) {
            tituloLabel.setText("Error: Completa todos los campos.");
            return;
        }

        try {
            int apuestaInt = Integer.parseInt(apuesta);
            CardSuit paloEnum = CardSuit.valueOf(palo.toUpperCase());

            if (apuestaInt <= 0 || palosElegidos.contains(paloEnum)) {
                tituloLabel.setText("Error: Verifica la apuesta o el palo.");
                return;
            }

            JugadorHumano jugador = new JugadorHumano(nombre);
            jugador.elegirPalo(paloEnum);
            jugador.realizarApuesta(apuestaInt);
            jugadores.add(jugador);
            palosElegidos.add(paloEnum);

            jugadorActual++;
            if (jugadorActual > numeroJugadoresHumanos) {
                actualizarBoton();
                return; // Salimos, ya no necesitamos configurar más jugadores humanos
            }

            actualizarTitulo();
            limpiarCampos();
            actualizarBoton();
        } catch (NumberFormatException e) {
            tituloLabel.setText("Error: Apuesta no válida.");
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        paloChoiceBox.setValue(null);
        apuestaField.clear();
    }

    private void completarJugadores() {
        Random random = new Random();
        while (jugadores.size() < 4) { // Completar hasta un máximo de 4 jugadores.
            BotJugador bot = new BotJugador("Bot " + (jugadores.size() + 1));
            CardSuit palo;
            do {
                palo = CardSuit.values()[random.nextInt(CardSuit.values().length)];
            } while (palosElegidos.contains(palo));
            bot.elegirPalo(palo);
            bot.realizarApuesta(random.nextInt(100) + 1); // Apuesta aleatoria para el bot
            jugadores.add(bot);
            palosElegidos.add(palo);
        }
    }

    private void iniciarPartida(ActionEvent event) {
        completarJugadores(); // Completar con bots si es necesario

        try {
            // Cargar la pantalla de juego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/juegoPanel.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del juego
            JuegoPanelController controlador = loader.getController();

            // Pasar la lista de jugadores configurados
            controlador.iniciarJuegoConJugadores(jugadores);

            // Cerrar la ventana actual de configuración
            Stage currentStage = (Stage) tituloLabel.getScene().getWindow();
            currentStage.close();

            // Abrir la nueva pantalla de la partida
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Carrera de Caballos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            tituloLabel.setText("Error al cargar la pantalla del juego.");
        }
    }
}

