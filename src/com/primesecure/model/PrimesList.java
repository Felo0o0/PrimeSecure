/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.model;

import java.util.ArrayList;

/**
 * A specialized ArrayList that only stores prime numbers.
 * <p>
 * This class extends ArrayList with functionality to validate that
 * only prime numbers are added to the collection. It also provides
 * utility methods specific to prime number operations.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimesList extends ArrayList<Integer> {
    
    /**
     * Creates a new empty list of prime numbers.
     */
    public PrimesList() {
        super();
    }
    
    /**
     * Adds a number to the list only if it is prime.
     * <p>
     * This method overrides the standard add method to ensure
     * that only prime numbers can be added to the collection.
     * </p>
     * 
     * @param number The number to add
     * @return true if the number was added (is prime), false otherwise
     */
    @Override
    public boolean add(Integer number) {
        if (isPrime(number)) {
            return super.add(number);
        }
        return false;
    }
    
    /**
     * Checks if a number is prime.
     * <p>
     * A prime number is a natural number greater than 1 that cannot be
     * formed by multiplying two smaller natural numbers.
     * </p>
     * 
     * @param number The number to check
     * @return true if the number is prime, false otherwise
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        
        if (number <= 3) {
            return true;
        }
        
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets a random prime number from the list.
     * 
     * @return A randomly selected prime number from the list,
     *         or -1 if the list is empty
     */
    public int getRandomPrime() {
        if (this.isEmpty()) {
            return -1;
        }
        
        int randomIndex = (int) (Math.random() * this.size());
        return this.get(randomIndex);
    }
}