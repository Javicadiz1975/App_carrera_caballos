package com.example.baraja_cartas_gui.controladores;

import com.example.baraja_cartas_gui.modelo.Jugadores.BotJugador;
import com.example.baraja_cartas_gui.modelo.Jugadores.Jugador;
import com.example.baraja_cartas_gui.modelo.Jugadores.JugadorHumano;
import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    public void initialize() {
        paloChoiceBox.getItems().addAll("GOLD", "CUPS", "SWORDS", "CLUBS");
        preguntarNumeroJugadores(); // Pregunta cuántos jugadores humanos habrá.
    }

    private void preguntarNumeroJugadores() {
        TextInputDialog dialog = new TextInputDialog(); // Valor por defecto.
        dialog.setTitle("Configuración de Jugadores");
        dialog.setHeaderText("Número de Jugadores Humanos");
        dialog.setContentText("Introduce un número entre 1 y 4:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int seleccionados = Integer.parseInt(result.get().trim());
                if (seleccionados >= 1 && seleccionados <= 4) {
                    numeroJugadoresHumanos = seleccionados;
                    actualizarTitulo();
                    actualizarBoton(); // Cambiar el texto del botón dinámicamente
                } else {
                    tituloLabel.setText("Error: Número fuera de rango (1-4).");
                    preguntarNumeroJugadores();
                }
            } catch (NumberFormatException e) {
                tituloLabel.setText("Error: Entrada no válida.");
                preguntarNumeroJugadores();
            }
        } else {
            tituloLabel.setText("Error: No seleccionaste el número de jugadores.");
            preguntarNumeroJugadores();
        }
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

