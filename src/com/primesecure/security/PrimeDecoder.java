/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.security;

/**
 * Provides functionality to decode messages encrypted with prime numbers.
 * <p>
 * This class implements the decryption algorithm that reverses the
 * encryption performed by PrimeEncoder.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 * @see com.primesecure.security.PrimeEncoder
 */
public class PrimeDecoder {
    
    /**
     * Decodes a message using a prime number as the decryption key.
     * <p>
     * The algorithm reverses the character transformation performed
     * during encoding.
     * </p>
     * 
     * @param encodedMessage The encoded message to decode
     * @param primeCode The prime number used as the encryption key
     * @return The decoded (plain text) message
     */
    public String decode(String encodedMessage, int primeCode) {
        if (encodedMessage == null || encodedMessage.isEmpty()) {
            return encodedMessage;
        }
        
        StringBuilder decoded = new StringBuilder();
        
        for (int i = 0; i < encodedMessage.length(); i++) {
            char c = encodedMessage.charAt(i);
            int shifted = c - (primeCode % 20);
            decoded.append((char) shifted);
        }
        
        return decoded.toString();
    }
    
    /**
     * Checks if a message can be decoded with the given prime code.
     * <p>
     * This method validates that both the encoded message and prime code are
     * suitable for the decoding process.
     * </p>
     * 
     * @param encodedMessage The encoded message to validate
     * @param primeCode The prime code to validate
     * @return true if the message can be decoded, false otherwise
     */
    public boolean canDecode(String encodedMessage, int primeCode) {
        return encodedMessage != null && !encodedMessage.isEmpty() && 
               com.primesecure.model.PrimesList.isPrime(primeCode);
    }
}