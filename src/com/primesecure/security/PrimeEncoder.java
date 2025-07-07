/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.security;

/**
 * Provides functionality to encode messages using prime numbers.
 * <p>
 * This class implements a simple encryption algorithm based on
 * prime numbers to secure message content.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeEncoder {
    
    /**
     * Encodes a message using a prime number as the encryption key.
     * <p>
     * The algorithm performs a character-by-character transformation
     * based on the prime number code.
     * </p>
     * 
     * @param message The plain text message to encode
     * @param primeCode The prime number to use as encryption key
     * @return The encoded message
     */
    public String encode(String message, int primeCode) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        
        StringBuilder encoded = new StringBuilder();
        
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            int shifted = c + (primeCode % 20);
            encoded.append((char) shifted);
        }
        
        return encoded.toString();
    }
    
    /**
     * Checks if a message can be encoded with the given prime code.
     * <p>
     * This method validates that both the message and prime code are
     * suitable for the encoding process.
     * </p>
     * 
     * @param message The message to validate
     * @param primeCode The prime code to validate
     * @return true if the message can be encoded, false otherwise
     */
    public boolean canEncode(String message, int primeCode) {
        return message != null && !message.isEmpty() && 
               com.primesecure.model.PrimesList.isPrime(primeCode);
    }
}