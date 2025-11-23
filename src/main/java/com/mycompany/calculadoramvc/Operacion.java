/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.calculadoramvc;

/**
 *
 * @author kokoju
 */
public enum Operacion {  // Enum para los distiontos tipos de operaciones
    SUMA("+"),
    RESTA("-"),
    MULTIPLICACION("*"),
    DIVISION("÷");

    private final String simbolo;

    Operacion(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    // Desde la lógica recibimos siempre un símbolo String, por lo que hacemos un recorrido para transformarlo en un elemento del enum
    public static Operacion desdeSimbolo(String simboloElegido) {
        for (Operacion op : values()) {
            if (op.simbolo.equals(simboloElegido)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Operación desconocida: " + simboloElegido);
    }
    
    // Ejecución de la operación, hecho aquí en el enum para evitar modificar varios archivos, si se quisiera escalar
    public double aplicarOperacion(double a, double b) {
        return switch (this) {
            case SUMA -> a + b;
            case RESTA -> a - b;
            case MULTIPLICACION -> a * b;
            case DIVISION -> a / b;
        };
    }   
}
