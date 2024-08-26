/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.edu.espol.pd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RUCO HOUSE
 */
public class EleccionController implements Initializable {

    /**
     * Initializes the controller class.
     */
        private File file1;
    private File file2;
    public Button comprimir; 
    public Button descomprimir; 
    public FileCompressor fileCompressor = new FileCompressor();
    @FXML
    private TextArea mensaje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización del controlador
    }    
    
    @FXML
    private void uploadFile1(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Subir archivo a comprimir");
        file1 = fileChooser.showOpenDialog(new Stage());

        if (file1 != null) {
            if (validateFile1(file1)) {
                System.out.println("Archivo 1 es válido.");
                this.handleCompressFile();
            } else {
                System.out.println("Archivo 1 no es válido.");
                file1 = null;
            }
        }
    }

    private boolean validateFile1(File file1) {
        List<String> validExtensions = Arrays.asList(".txt", ".png", ".jpg", ".jpeg", ".gif", ".bmp");
        String fileName = file1.getName().toLowerCase();
        boolean isValidExtension = validExtensions.stream().anyMatch(fileName::endsWith);

        if (!isValidExtension) {
            return false;
        }

        try {
            String mimeType = Files.probeContentType(file1.toPath());
            return mimeType != null && (mimeType.startsWith("text") || mimeType.startsWith("image"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void handleCompressFile() {
        if (file1 != null) {
            try {
                String compressedFilePath = file1.getAbsolutePath() + ".huff";
                fileCompressor.compressFile(file1.getAbsolutePath(), compressedFilePath);
                mensaje.setText("File compressed successfully:\n" + compressedFilePath);
            } catch (IOException e) {
                mensaje.setText("Error during compression:\n" + e.getMessage());
            }
        } else {
            mensaje.setText("Please select a file first.");
        }
    }

    @FXML
    private void uploadFile2(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Subir archivo a descomprimir");
        file2 = fileChooser.showOpenDialog(new Stage());

        if (file2 != null) {
            if (validateFile2(file2)) {
                System.out.println("Archivo 2 es válido.");
                this.handleDecompressFile();
            } else {
                System.out.println("Archivo 2 no es válido.");
                file2 = null;
            }
        }
    }

    private boolean validateFile2(File file2) {
        String fileName = file2.getName().toLowerCase();
        return fileName.endsWith(".huff");
    }
    
    private HuffmanTree arbolHuffman;

    @FXML
    private void handleDecompressFile() {
        if (file2 != null) {
            try {
                String decompressedFilePath = this.getDecompressedFilePath(file2);

                // Leer los códigos de Huffman del archivo comprimido
                Map<Byte, String> huffmanCodes;
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file2))) {
                    huffmanCodes = (Map<Byte, String>) inputStream.readObject();
                }

                // Reconstruir el árbol de Huffman
                arbolHuffman = fileCompressor.reconstructHuffmanTree(huffmanCodes);

                // Descomprimir el archivo utilizando el árbol de Huffman
                fileCompressor.decompressFile(file2.getAbsolutePath(), decompressedFilePath, arbolHuffman);
                mensaje.setText("File decompressed successfully:\n" + decompressedFilePath);
            } catch (IOException | ClassNotFoundException e) {
                mensaje.setText("Error during decompression:\n" + e.getMessage());
            }
        } else {
            mensaje.setText("Please select a file first.");
        }
    }

    private String getDecompressedFilePath(File file) {
        String filePath = file.getAbsolutePath();
        return filePath.replace(".huff", "");
    }
}