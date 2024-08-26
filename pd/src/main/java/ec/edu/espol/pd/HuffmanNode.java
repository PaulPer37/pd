/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.espol.pd;

/**
 *
 * @author RUCO HOUSE
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    int frecuencia;
    char caracter;
    HuffmanNode izquierda;
    HuffmanNode derecha;

    public HuffmanNode(int frecuencia, char caracter) {
        this.frecuencia = frecuencia;
        this.caracter = caracter;
        this.izquierda = null;
        this.derecha = null;
    }

    public HuffmanNode(int frecuencia, HuffmanNode izquierda, HuffmanNode derecha) {
        this.frecuencia = frecuencia;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.frecuencia - o.frecuencia;
    }
}
