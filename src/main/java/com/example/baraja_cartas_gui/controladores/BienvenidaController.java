package com.example.baraja_cartas_gui.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BienvenidaController {

    @FXML
    private ImageView fondoImageView;

    @FXML
    public void initialize() {
        // Cargar la imagen de fondo
        String fondoImagePath = "/images/bienvenida.PNG"; // Ruta de la imagen de fondo
        URL fondoImageUrl = getClass().getResource(fondoImagePath);
        if (fondoImageUrl != null) {
            fondoImageView.setImage(new Image(fondoImageUrl.toExternalForm()));
        } else {
            System.err.println("Error: No se encontró la imagen en la ruta: " + fondoImagePath);
        }
    }



    @FXML
    public void iniciarPartida(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/configJugador.fxml"));
            Parent root = loader.load();

            // Crear la ventana modal para la configuración de jugadores
            Stage configStage = new Stage();
            configStage.initModality(Modality.APPLICATION_MODAL);
            configStage.setScene(new Scene(root));
            configStage.setTitle("Configuración de Jugadores");

            // Cerrar la ventana de bienvenida
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Mostrar la pantalla de configuración
            configStage.showAndWait(); // La configuración decidirá cuándo iniciar el juego
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarPartidaAutomatica() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/juegoPanel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Carrera de Caballos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void verEstadisticas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/estadisticas.fxml"));
            Parent root = loader.load();

            Stage estadisticasStage = new Stage();
            estadisticasStage.initModality(Modality.APPLICATION_MODAL);
            estadisticasStage.setScene(new Scene(root));
            estadisticasStage.setTitle("Estadísticas");
            estadisticasStage.showAndWait(); // Bloquea hasta que se cierre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void salir(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

