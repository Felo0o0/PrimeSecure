/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.util;

import com.primesecure.model.PrimesList;
import java.util.Random;

/**
 * Utility class for prime number operations.
 * <p>
 * This class provides static methods for performing various calculations
 * related to prime numbers, including generation and validation.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeCalculator {
    
    /** Random number generator for prime selection */
    private static final Random random = new Random();
    
    /**
     * Generates a random prime number within the specified range.
     * <p>
     * This method searches for a prime number within the given range
     * and returns one at random.
     * </p>
     * 
     * @param min The minimum value of the range (inclusive)
     * @param max The maximum value of the range (inclusive)
     * @return A random prime number within the range, or -1 if none found
     */
    public static int generateRandomPrime(int min, int max) {
        if (min > max || min < 2) {
            return -1;
        }
        
        // Find all primes in the range
        PrimesList primes = new PrimesList();
        for (int i = min; i <= max; i++) {
            if (PrimesList.isPrime(i)) {
                primes.add(i);
            }
        }
        
        // If no primes found, return -1
        if (primes.isEmpty()) {
            return -1;
        }
        
        // Return a random prime from the list
        return primes.get(random.nextInt(primes.size()));
    }
    
    /**
     * Finds the next prime number after the specified value.
     * 
     * @param start The starting value
     * @return The next prime number greater than start
     */
    public static int findNextPrime(int start) {
        int candidate = start + 1;
        
        while (!PrimesList.isPrime(candidate)) {
            candidate++;
        }
        
        return candidate;
    }
    
    /**
     * Calculates the greatest common divisor of two numbers.
     * <p>
     * This method uses Euclid's algorithm to find the GCD.
     * </p>
     * 
     * @param a The first number
     * @param b The second number
     * @return The greatest common divisor of a and b
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * Checks if two numbers are coprime (their GCD is 1).
     * 
     * @param a The first number
     * @param b The second number
     * @return true if the numbers are coprime, false otherwise
     */
    public static boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;
    }
}