/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RUCO HOUSE
 */
public class HuffmanDecoder {
    private HuffmanTree arbolHuffman;

    public HuffmanDecoder(HuffmanTree arbolHuffman) {
        this.arbolHuffman = arbolHuffman;
    }

    public byte[] decode(String textoCodificado) {
        List<Byte> decodificado = new ArrayList<>();
        HuffmanNode nodoActual = arbolHuffman.getRaiz();
        for (char bit : textoCodificado.toCharArray()) {
            if (bit == '0') {
                nodoActual = nodoActual.izquierda;
            } else {
                nodoActual = nodoActual.derecha;
            }

            if (nodoActual.izquierda == null && nodoActual.derecha == null) {
                decodificado.add(nodoActual.valor);
                nodoActual = arbolHuffman.getRaiz();
            }
        }
        byte[] decodedData = new byte[decodificado.size()];
        for (int i = 0; i < decodedData.length; i++) {
            decodedData[i] = decodificado.get(i);
        }
        return decodedData;
    }
}