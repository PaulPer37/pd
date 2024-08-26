/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

import java.util.Map;

/**
 *
 * @author RUCO HOUSE
 */
public class HuffmanEncoder {
    private HuffmanTree arbolHuffman;
    private Map<Byte, String> codigosHuffman;

    public HuffmanEncoder(byte[] data) {
        arbolHuffman = new HuffmanTree(data);
        codigosHuffman = arbolHuffman.getCodigosHuffman();
    }

    public String encode(byte[] data) {
        StringBuilder codificado = new StringBuilder();
        for (byte b : data) {
            codificado.append(codigosHuffman.get(b));
        }
        return codificado.toString();
    }

    public HuffmanTree getArbolHuffman() {
        return arbolHuffman;
    }

    public Map<Byte, String> getCodigosHuffman() {
        return codigosHuffman;
    }
}