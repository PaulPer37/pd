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
    private Map<Character, String> codigosHuffman;

    public HuffmanEncoder(String text) {
        arbolHuffman = new HuffmanTree(text);
        codigosHuffman = arbolHuffman.getCodigosHuffman();
    }

    public String encode(String text) {
        StringBuilder codificado = new StringBuilder();
        for (char c : text.toCharArray()) {
            codificado.append(codigosHuffman.get(c));
        }
        return codificado.toString();
    }

    public HuffmanTree getArbolHuffman() {
        return arbolHuffman;
    }

    public Map<Character, String> getCodigosHuffman() {
        return codigosHuffman;
    }
}

