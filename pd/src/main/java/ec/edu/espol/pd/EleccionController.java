/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.edu.espol.pd;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RUCO HOUSE
 */
public class EleccionController implements Initializable {

    private File file1;
    private File file2;
    public Button comprimir; 
    public Button descomprimir; 
    public FileCompressor fileCompressor = new FileCompressor();
    private HuffmanTree arbolHuffman; // Definición de arbolHuffman
    @FXML
    private TextArea mensaje;
    @FXML
    private Text titulo;
    @FXML
    private AnchorPane Anchorpane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mensaje.setVisible(false);
        titulo.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (newScene != null) {
                    newScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                }
            }
        });

        Scene scene = titulo.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            System.out.println("CSS loaded");
        } else {
            System.out.println("Scene is null");
        }
    } 

    @FXML
    private void uploadFile1(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Subir archivo a comprimir");
        file1 = fileChooser.showOpenDialog(new Stage());

        if (file1 != null) {
            if (validateFile1(file1)) {
                System.out.println("Archivo 1 es válido.");
                this.handleCompressFile(); // Asegúrate de que el método esté definido
            } else {
                System.out.println("Archivo 1 no es válido.");
                file1 = null;
            }
        }
    }

    private boolean validateFile1(File file1) {
        List<String> validExtensions = Arrays.asList(".txt", ".png", ".jpg", ".jpeg", ".gif", ".bmp", ".pdf", ".wav");
        String fileName = file1.getName().toLowerCase();
        boolean isValidExtension = validExtensions.stream().anyMatch(fileName::endsWith);

        if (!isValidExtension) {
            return false;
        }

        try {
            String mimeType = Files.probeContentType(file1.toPath());
            return mimeType != null && (
                mimeType.startsWith("text") ||
                mimeType.startsWith("image") ||
                mimeType.equals("application/pdf") ||
                mimeType.equals("audio/wav")
            );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleCompressFile() {
        mensaje.setVisible(true);
        if (file1 != null) {
            try {
                String compressedFilePath = file1.getAbsolutePath() + ".huff";
                fileCompressor.compressFile(file1.getAbsolutePath(), compressedFilePath);
                File originalFile = file1;
                File compressedFile = new File(compressedFilePath);
                if (compressedFile.length() >= originalFile.length()) {
                    mensaje.setText("La compresión no es eficiente; el archivo original es más pequeño.");
                    compressedFile.delete(); 
                } else {
                    mensaje.setText("Archivo comprimido exitosamente:\n" + compressedFilePath);
                }
            } catch (IOException e) {
                mensaje.setText("Error durante la compresión:\n" + e.getMessage());
            }
        } else {
            mensaje.setText("Por favor seleccione un archivo primero.");
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

    private void handleDecompressFile() {
       mensaje.setVisible(true);
       if (file2 != null) {
           try {
               String decompressedFilePath = this.getDecompressedFilePath(file2);

               try (DataInputStream dis = new DataInputStream(new FileInputStream(file2))) {
                   int numCodes = dis.readInt();
                   Map<Byte, String> huffmanCodes = new HashMap<>();

                   for (int i = 0; i < numCodes; i++) {
                       byte key = dis.readByte();
                       String value = dis.readUTF();
                       huffmanCodes.put(key, value);
                   }
                   arbolHuffman = fileCompressor.reconstructHuffmanTree(huffmanCodes);
                   int encodedTextLength = dis.readInt();    
                   int numBytes = (encodedTextLength + 7) / 8;
                   byte[] encodedBytes = new byte[numBytes];
                   dis.readFully(encodedBytes);
                   BitSet bitSet = BitSet.valueOf(encodedBytes);
                   StringBuilder encodedTextBuilder = new StringBuilder(encodedTextLength);
                   for (int i = 0; i < encodedTextLength; i++) {
                       encodedTextBuilder.append(bitSet.get(i) ? '1' : '0');
                   }
                   String encodedText = encodedTextBuilder.toString();
                   HuffmanDecoder decoder = new HuffmanDecoder(arbolHuffman);
                   byte[] decodedData = decoder.decode(encodedText);
                   Files.write(Paths.get(decompressedFilePath), decodedData);

                   mensaje.setText("File decompressed successfully:\n" + decompressedFilePath);
               }

           } catch (IOException e) {
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
