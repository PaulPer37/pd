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
    Byte valor;
    HuffmanNode izquierda;
    HuffmanNode derecha;

    public HuffmanNode(int frecuencia, Byte valor) {
        this.frecuencia = frecuencia;
        this.valor = valor;
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