/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RUCO HOUSE
 */
public class FileCompressor {

    public void compressFile(String inputFilePath, String outputFilePath) throws IOException {
    byte[] data = Files.readAllBytes(Paths.get(inputFilePath));
    
    HuffmanEncoder encoder = new HuffmanEncoder(data);
    String encodedText = encoder.encode(data);
    Map<Byte, String> huffmanCodes = encoder.getCodigosHuffman();
    
    try (FileOutputStream fos = new FileOutputStream(outputFilePath);
         DataOutputStream dos = new DataOutputStream(fos)) {
     
        dos.writeInt(huffmanCodes.size());
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            dos.writeByte(entry.getKey());
            dos.writeUTF(entry.getValue());
        }
        BitSet bitSet = new BitSet(encodedText.length());
        for (int i = 0; i < encodedText.length(); i++) {
            if (encodedText.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        
        dos.writeInt(encodedText.length());
        byte[] encodedBytes = bitSet.toByteArray();
        dos.write(encodedBytes);
    }
}

    public void decompressFile(String compressedFilePath, String outputFilePath, HuffmanTree huffmanTree) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(compressedFilePath);
             DataInputStream dis = new DataInputStream(fis)) {

            int numCodes = dis.readInt();
            Map<Byte, String> huffmanCodes = new HashMap<>();

            for (int i = 0; i < numCodes; i++) {
                byte key = dis.readByte();
                String value = dis.readUTF();
                huffmanCodes.put(key, value);
            }

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

            HuffmanDecoder decoder = new HuffmanDecoder(huffmanTree);
            byte[] decodedData = decoder.decode(encodedText);

            Files.write(Paths.get(outputFilePath), decodedData);
        }
    }



    
    
    public HuffmanTree reconstructHuffmanTree(Map<Byte, String> huffmanCodes) {
        HuffmanNode root = new HuffmanNode(0, null);

        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            HuffmanNode current = root;
            String code = entry.getValue();

            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.izquierda == null) {
                        current.izquierda = new HuffmanNode(0, null);
                    }
                    current = current.izquierda;
                } else if (bit == '1') {
                    if (current.derecha == null) {
                        current.derecha = new HuffmanNode(0, null);
                    }
                    current = current.derecha;
                }
            }

            current.valor = entry.getKey();
        }

        return new HuffmanTree(root);
    }
}