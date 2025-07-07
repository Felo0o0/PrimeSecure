/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.thread;

import com.primesecure.model.PrimesList;

/**
 * A thread implementation for finding prime numbers within a specified range.
 * <p>
 * This class extends Thread to enable concurrent prime number checking.
 * It searches for prime numbers within a given range and adds them to
 * a shared PrimesList.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeCheckerThread extends Thread {
    
    /** The starting number of the range to check */
    private int startRange;
    
    /** The ending number of the range to check */
    private int endRange;
    
    /** The shared list to store found prime numbers */
    private PrimesList primesList;
    
    /** Counter for prime numbers found by this thread */
    private int foundCount;
    
    /**
     * Creates a new prime checker thread.
     * 
     * @param startRange The starting number of the range (inclusive)
     * @param endRange The ending number of the range (inclusive)
     * @param primesList The shared list to store found prime numbers
     */
    public PrimeCheckerThread(int startRange, int endRange, PrimesList primesList) {
        this.startRange = startRange;
        this.endRange = endRange;
        this.primesList = primesList;
        this.foundCount = 0;
    }
    
    /**
     * Executes the prime number search when the thread is started.
     * <p>
     * This method checks each number in the specified range and adds
     * prime numbers to the shared list.
     * </p>
     */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + 
                           " buscando primos entre " + startRange + " y " + endRange);
        
        for (int num = startRange; num <= endRange; num++) {
            if (PrimesList.isPrime(num)) {
                synchronized(primesList) {
                    primesList.add(num);
                }
                foundCount++;
                
                // Optional: Add a small delay to demonstrate thread interleaving
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        
        System.out.println(Thread.currentThread().getName() + 
                           " encontro " + foundCount + " numeros primos");
    }
    
    /**
     * Gets the number of prime numbers found by this thread.
     * 
     * @return The count of prime numbers found
     */
    public int getFoundCount() {
        return foundCount;
    }
}