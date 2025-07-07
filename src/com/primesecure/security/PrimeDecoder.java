/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.security;

/**
 * Proporciona funcionalidad para desencriptar texto usando numeros primos.
 * <p>
 * Esta clase implementa un algoritmo de desencriptacion basado en numeros primos
 * para transformar texto encriptado en texto plano.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeDecoder {
    
    /**
    * Desencripta un texto usando un codigo primo como clave.
    * <p>
    * El algoritmo revierte la transformacion aplicada por el PrimeEncoder
    * usando el mismo valor de numero primo.
    * </p>
    * 
    * @param encodedText El texto encriptado a desencriptar
    * @param primeCode El codigo primo usado como clave
    * @return El texto plano original
    */
    public String decode(String encodedText, int primeCode) {
        if (encodedText == null || encodedText.isEmpty()) {
            return encodedText;
        }
        
        StringBuilder decoded = new StringBuilder();
        
        for (int i = 0; i < encodedText.length(); i++) {
            char c = encodedText.charAt(i);
            
            // Revertir la transformacion basada en el codigo primo
            // Usar las mismas operaciones que en encode pero en reversa
            int shift = (primeCode % 26) + (i % 5);
            char decChar;
            
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                // Agregar 26 para manejar valores negativos en el modulo
                decChar = (char) (((c - base - shift + 26) % 26) + base);
            } else if (Character.isDigit(c)) {
                // Agregar 10 para manejar valores negativos en el modulo
                decChar = (char) (((c - '0' - shift + 10) % 10) + '0');
            } else {
                // Para caracteres especiales, revertir el desplazamiento simple
                decChar = (char) (c - (shift % 5));
            }
            
            decoded.append(decChar);
        }
        
        return decoded.toString();
    }
}