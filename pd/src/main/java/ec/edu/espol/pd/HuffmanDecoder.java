/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

/**
 *
 * @author RUCO HOUSE
 */
public class HuffmanDecoder {
    private HuffmanTree arbolHuffman;

    public HuffmanDecoder(HuffmanTree arbolHuffman) {
        this.arbolHuffman = arbolHuffman;
    }

    public String decode(String textoCodificado) {
        StringBuilder decodificado = new StringBuilder();
        HuffmanNode nodoActual = arbolHuffman.getRaiz();
        for (char bit : textoCodificado.toCharArray()) {
            if (bit == '0') {
                nodoActual = nodoActual.izquierda;
            } else {
                nodoActual = nodoActual.derecha;
            }

            if (nodoActual.izquierda == null && nodoActual.derecha == null) {
                decodificado.append(nodoActual.caracter);
                nodoActual = arbolHuffman.getRaiz();
            }
        }
        return decodificado.toString();
    }
}

