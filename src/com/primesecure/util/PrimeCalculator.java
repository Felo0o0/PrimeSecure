/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.util;

import com.primesecure.model.PrimesList;
import java.util.Random;

/**
 * Utilidad para calcular y generar numeros primos.
 * <p>
 * Esta clase proporciona metodos para generar numeros primos aleatorios
 * y verificar si un numero es primo.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeCalculator {
    
    /** Generador de numeros aleatorios */
    private static final Random random = new Random();
    
    /**
    * Genera un numero primo aleatorio dentro de un rango especificado.
    * <p>
    * Este metodo busca un numero primo aleatorio entre min y max (inclusive).
    * Si no se encuentra un primo despues de 100 intentos, retorna -1.
    * </p>
    * 
    * @param min El limite inferior del rango (inclusive)
    * @param max El limite superior del rango (inclusive)
    * @return Un numero primo aleatorio, o -1 si no se encuentra ninguno
    */
    public static int generateRandomPrime(int min, int max) {
        if (min < 2) min = 2; // El primer numero primo es 2
        
        // Limitar intentos para evitar bucles infinitos
        int maxAttempts = 100;
        
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int candidate = random.nextInt(max - min + 1) + min;
            
            if (PrimesList.isPrime(candidate)) {
                return candidate;
            }
        }
        
        // Fallback: buscar secuencialmente
        for (int i = min; i <= max; i++) {
            if (PrimesList.isPrime(i)) {
                return i;
            }
        }
        
        return -1; // No se encontro ningun primo en el rango
    }
    
    /**
    * Calcula el siguiente numero primo despues de un valor dado.
    * 
    * @param startValue El valor desde el cual comenzar la busqueda
    * @return El siguiente numero primo
    */
    public static int nextPrime(int startValue) {
        int candidate = startValue + 1;
        
        while (!PrimesList.isPrime(candidate)) {
            candidate++;
        }
        
        return candidate;
    }
    
    /**
    * Calcula el numero primo anterior a un valor dado.
    * 
    * @param startValue El valor desde el cual comenzar la busqueda
    * @return El numero primo anterior, o -1 si no existe ninguno
    */
    public static int previousPrime(int startValue) {
        int candidate = startValue - 1;
        
        while (candidate >= 2) {
            if (PrimesList.isPrime(candidate)) {
                return candidate;
            }
            candidate--;
        }
        
        return -1; // No hay primos menores que 2
    }
}