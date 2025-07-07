/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.security;

/**
 * Proporciona funcionalidad para encriptar texto usando numeros primos.
 * <p>
 * Esta clase implementa un algoritmo de encriptacion basado en numeros primos
 * para transformar texto plano en texto encriptado.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeEncoder {
    
    /**
    * Encripta un texto usando un codigo primo como clave.
    * <p>
    * El algoritmo aplica una transformacion basada en el valor del numero primo
    * a cada caracter del texto de entrada.
    * </p>
    * 
    * @param plainText El texto plano a encriptar
    * @param primeCode El codigo primo a usar como clave
    * @return El texto encriptado
    */
    public String encode(String plainText, int primeCode) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        
        StringBuilder encoded = new StringBuilder();
        
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            
            // Aplicar transformacion basada en el codigo primo
            // Usar diferentes operaciones para diferentes posiciones
            int shift = (primeCode % 26) + (i % 5);
            char encChar;
            
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encChar = (char) (((c - base + shift) % 26) + base);
            } else if (Character.isDigit(c)) {
                encChar = (char) (((c - '0' + shift) % 10) + '0');
            } else {
                // Para caracteres especiales, aplicar un desplazamiento simple
                encChar = (char) (c + (shift % 5));
            }
            
            encoded.append(encChar);
        }
        
        return encoded.toString();
    }
}