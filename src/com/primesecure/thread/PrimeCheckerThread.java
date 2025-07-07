/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.thread;

import com.primesecure.model.PrimesList;

/**
 * Una implementacion de hilo para encontrar numeros primos dentro de un rango especificado.
 * <p>
 * Esta clase extiende Thread para permitir la verificacion concurrente de numeros primos.
 * Busca numeros primos dentro de un rango dado y los agrega a una PrimesList compartida.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeCheckerThread extends Thread {
    
    /** El numero inicial del rango a verificar */
    private int startRange;
    
    /** El numero final del rango a verificar */
    private int endRange;
    
    /** La lista compartida para almacenar los numeros primos encontrados */
    private PrimesList primesList;
    
    /** Contador de numeros primos encontrados por este hilo */
    private int foundCount;
    
    /**
    * Crea un nuevo hilo verificador de primos.
    * 
    * @param startRange El numero inicial del rango (inclusive)
    * @param endRange El numero final del rango (inclusive)
    * @param primesList La lista compartida para almacenar los numeros primos encontrados
    */
    public PrimeCheckerThread(int startRange, int endRange, PrimesList primesList) {
        this.startRange = startRange;
        this.endRange = endRange;
        this.primesList = primesList;
        this.foundCount = 0;
    }
    
    /**
    * Ejecuta la busqueda de numeros primos cuando se inicia el hilo.
    * <p>
    * Este metodo verifica cada numero en el rango especificado y agrega
    * los numeros primos a la lista compartida.
    * </p>
    */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + 
        " buscando primos entre " + startRange + " y " + endRange);
        
        for (int num = startRange; num <= endRange; num++) {
            if (PrimesList.isPrime(num)) {
                try {
                    synchronized(primesList) {
                        primesList.add(num);
                    }
                    foundCount++;
                } catch (IllegalArgumentException e) {
                    // Este caso no debería ocurrir ya que verificamos isPrime antes
                    System.out.println("Error inesperado: " + e.getMessage());
                }
                
                // Opcional: Agregar un pequeño retraso para demostrar entrelazado de hilos
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
    * Obtiene el numero de numeros primos encontrados por este hilo.
    * 
    * @return El conteo de numeros primos encontrados
    */
    public int getFoundCount() {
        return foundCount;
    }
}