/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.util;

import com.primesecure.model.Message;
import com.primesecure.model.PrimesList;
import com.primesecure.thread.MessageProcessorThread;
import com.primesecure.thread.PrimeCheckerThread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Utilidad para procesar lotes de operaciones usando multihilos.
 * <p>
 * Esta clase proporciona metodos para realizar operaciones en lote
 * como busqueda de numeros primos y procesamiento de mensajes utilizando
 * multiples hilos para mejorar el rendimiento.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class BatchProcessor {
    
    /**
    * Busca numeros primos en un rango usando multiples hilos.
    * <p>
    * Este metodo divide el rango en segmentos y asigna cada segmento
    * a un hilo diferente para buscar numeros primos en paralelo.
    * </p>
    * 
    * @param startRange El inicio del rango (inclusive)
    * @param endRange El fin del rango (inclusive)
    * @param threadCount El numero de hilos a utilizar
    * @return Una lista de los numeros primos encontrados
    */
    public static PrimesList findPrimesInRange(int startRange, int endRange, int threadCount) {
        if (startRange < 2) startRange = 2; // El primer numero primo es 2
        
        // Validar parametros
        if (endRange < startRange) {
            throw new IllegalArgumentException("El rango final debe ser mayor o igual al rango inicial");
        }
        
        // Usar al menos un hilo
        int actualThreadCount = Math.max(1, threadCount);
        
        // Calcular el tamaño del rango para cada hilo
        int rangeSize = (endRange - startRange + 1) / actualThreadCount;
        
        // Crear la lista compartida para almacenar los primos
        PrimesList primesList = new PrimesList();
        
        // Crear e iniciar los hilos
        List<PrimeCheckerThread> threads = new ArrayList<>();
        
        for (int i = 0; i < actualThreadCount; i++) {
            int threadStartRange = startRange + (i * rangeSize);
            int threadEndRange = (i == actualThreadCount - 1) 
                ? endRange 
                : threadStartRange + rangeSize - 1;
            
            PrimeCheckerThread thread = new PrimeCheckerThread(threadStartRange, threadEndRange, primesList);
            thread.setName("PrimeChecker-" + (i + 1));
            threads.add(thread);
            thread.start();
        }
        
        // Esperar a que todos los hilos terminen
        for (PrimeCheckerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Imprimir estadisticas
        System.out.println("Busqueda de primos completada:");
        int totalFound = 0;
        for (PrimeCheckerThread thread : threads) {
            totalFound += thread.getFoundCount();
            System.out.println(thread.getName() + " encontro " + thread.getFoundCount() + " primos");
        }
        System.out.println("Total de primos encontrados: " + totalFound);
        
        return primesList;
    }
    
    /**
    * Procesa un lote de mensajes usando multiples hilos.
    * <p>
    * Este metodo encripta o desencripta una lista de mensajes en paralelo
    * utilizando multiples hilos.
    * </p>
    * 
    * @param messages La lista de mensajes a procesar
    * @param encrypt Indicador de si se debe encriptar (true) o desencriptar (false)
    * @param threadCount El numero de hilos a utilizar
    */
    public static void processMessages(List<Message> messages, boolean encrypt, int threadCount) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        // Usar el metodo estatico de MessageProcessorThread
        MessageProcessorThread.processMessagesBatch(messages, encrypt, threadCount);
    }
    
    /**
    * Genera una lista de mensajes de prueba.
    * 
    * @param count El numero de mensajes a generar
    * @return Una lista de mensajes de prueba
    */
    public static List<Message> generateTestMessages(int count) {
        List<Message> messages = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            int primeCode = PrimeCalculator.generateRandomPrime(100, 997);
            Message message = new Message(
                "Mensaje de prueba #" + (i + 1), 
                "Remitente" + i, 
                "Destinatario" + i, 
                primeCode
            );
            messages.add(message);
        }
        
        return messages;
    }
}