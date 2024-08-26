/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 * @author RUCO HOUSE
 */
public class FileCompressor {

    public void compressFile(String inputFilePath, String outputFilePath) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        HuffmanEncoder encoder = new HuffmanEncoder(text);
        String encodedText = encoder.encode(text);
        Map<Character, String> huffmanCodes = encoder.getCodigosHuffman();

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(outputFilePath))) {
            outputStream.writeObject(huffmanCodes);
            outputStream.writeObject(encodedText);
        }
    }

    public void decompressFile(String compressedFilePath, String outputFilePath, HuffmanTree huffmanTree) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(compressedFilePath))) {
            Map<Character, String> huffmanCodes = (Map<Character, String>) inputStream.readObject();
            String encodedText = (String) inputStream.readObject();

            // Usa el árbol de Huffman proporcionado
            HuffmanDecoder decoder = new HuffmanDecoder(huffmanTree);
            String decodedText = decoder.decode(encodedText);

            Files.write(Paths.get(outputFilePath), decodedText.getBytes());
        }
    }
    
    public HuffmanTree reconstructHuffmanTree(Map<Character, String> huffmanCodes) {
        HuffmanNode root = new HuffmanNode(0, '\0');

        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            HuffmanNode current = root;
            String code = entry.getValue();

            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.izquierda == null) {
                        current.izquierda = new HuffmanNode(0, '\0');
                    }
                    current = current.izquierda;
                } else if (bit == '1') {
                    if (current.derecha == null) {
                        current.derecha = new HuffmanNode(0, '\0');
                    }
                    current = current.derecha;
                }
            }

            current.caracter = entry.getKey();
        }

        return new HuffmanTree(root);
    }
}