package com.example.baraja_cartas_gui.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ConfigPartidaController {
    @FXML
    public void iniciarJuego(ActionEvent event) {
        // Solicitar el número de jugadores

    }
    private void mostrarError(String mensaje) {
        // Mostrar una alerta de error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Entrada inválida");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }



    @FXML
    public void verEstadisticas(ActionEvent event) {
        try {
            // Cargar la pantalla de estadísticas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/baraja_cartas_gui/estadisticas.fxml"));
            Parent root = loader.load();

            // Crear un nuevo escenario para mostrar las estadísticas
            Stage estadisticasStage = new Stage();
            estadisticasStage.setTitle("Estadísticas");
            estadisticasStage.initModality(Modality.WINDOW_MODAL); // Bloquear la pantalla principal mientras se visualizan las estadísticas
            estadisticasStage.setScene(new Scene(root));
            estadisticasStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar la pantalla de estadísticas.");
        }
    }

    @FXML
    public void salir(ActionEvent event) {
        // Cerrar la aplicación
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}


