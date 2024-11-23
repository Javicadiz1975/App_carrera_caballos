package com.example.baraja_cartas_gui.controladores;


import com.example.baraja_cartas_gui.modelo.baraja.CardSuit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class EstadisticasController {

    @FXML
    private GridPane estadisticasGrid;

    private static final String RUTA_ARCHIVO_PALOS_GANADORES = "palos_ganadores.txt";

    public void initialize() {
        Map<CardSuit, Integer> estadisticas = contarGanadoresPorPalo();
        cargarEstadisticasEnGrid(estadisticas);
    }

    @FXML
    public void volver(ActionEvent event) {
        // Cerrar la ventana actual
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Lee el archivo de palos ganadores y cuenta cuántas veces aparece cada palo.
     *
     * @return Un mapa con los palos como claves y el número de apariciones como valores.
     */
    private Map<CardSuit, Integer> contarGanadoresPorPalo() {
        Map<CardSuit, Integer> contadorPalos = new EnumMap<>(CardSuit.class);

        // Inicializar el contador para todos los palos
        for (CardSuit palo : CardSuit.values()) {
            contadorPalos.put(palo, 0);
        }

        // Leer el archivo y contar los palos
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_PALOS_GANADORES))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    CardSuit palo = CardSuit.valueOf(linea.trim().toUpperCase());
                    contadorPalos.put(palo, contadorPalos.get(palo) + 1);
                } catch (IllegalArgumentException e) {
                    System.err.println("Palo inválido en el archivo: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de estadísticas: " + e.getMessage());
        }

        return contadorPalos;
    }

    /**
     * Carga las estadísticas en el GridPane con imágenes y texto.
     *
     * @param estadisticas Mapa de palos y el número de veces que han ganado.
     */
    private void cargarEstadisticasEnGrid(Map<CardSuit, Integer> estadisticas) {
        estadisticasGrid.getChildren().clear(); // Limpiar cualquier contenido previo
        int row = 0;

        for (Map.Entry<CardSuit, Integer> entry : estadisticas.entrySet()) {
            CardSuit palo = entry.getKey();
            int victorias = entry.getValue();

            // Cargar la imagen del palo
            String imagePath = "/images/" + palo.name().toLowerCase() + ".png";
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            imageView.setPreserveRatio(true);

            // Crear una etiqueta con el número de victorias
            Label victoriasLabel = new Label(palo.name() + ": " + victorias + " victorias");

            // Agregar la imagen y el texto al GridPane
            estadisticasGrid.add(imageView, 0, row); // Columna 0: Imagen
            estadisticasGrid.add(victoriasLabel, 1, row); // Columna 1: Texto
            row++;
        }
    }
}

