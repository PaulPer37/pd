/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author RUCO HOUSE
 */
public class HuffmanTree {
    private HuffmanNode raiz;

    public HuffmanTree(String text) {
        Map<Character, Integer> mapaFrecuencia = new HashMap<>();
        for (char c : text.toCharArray()) {
            mapaFrecuencia.put(c, mapaFrecuencia.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> colaPrioridad = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : mapaFrecuencia.entrySet()) {
            colaPrioridad.offer(new HuffmanNode(entry.getValue(), entry.getKey()));
        }

        while (colaPrioridad.size() > 1) {
            HuffmanNode izquierda = colaPrioridad.poll();
            HuffmanNode derecha = colaPrioridad.poll();
            HuffmanNode padre = new HuffmanNode(izquierda.frecuencia + derecha.frecuencia, izquierda, derecha);
            colaPrioridad.offer(padre);
        }

        raiz = colaPrioridad.poll();
    }
    public HuffmanTree(HuffmanNode raiz) {
        this.raiz = raiz;
    }
    public HuffmanNode getRaiz() {
        return raiz;
    }

    public Map<Character, String> getCodigosHuffman() {
        Map<Character, String> codigosHuffman = new HashMap<>();
        generarCodigos(raiz, "", codigosHuffman);
        return codigosHuffman;
    }

    private void generarCodigos(HuffmanNode nodo, String codigo, Map<Character, String> codigosHuffman) {
        if (nodo.izquierda == null && nodo.derecha == null) {
            codigosHuffman.put(nodo.caracter, codigo);
            return;
        }

        if (nodo.izquierda != null) {
            generarCodigos(nodo.izquierda, codigo + "0", codigosHuffman);
        }

        if (nodo.derecha != null) {
            generarCodigos(nodo.derecha, codigo + "1", codigosHuffman);
        }
    }
}
