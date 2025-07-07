/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.thread;

import com.primesecure.model.Message;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Una implementacion de hilo para procesar mensajes en lote.
 * <p>
 * Esta clase proporciona funcionalidad para encriptar o desencriptar multiples
 * mensajes concurrentemente usando multiples hilos.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class MessageProcessorThread extends Thread {
    
    /** La lista de mensajes a procesar */
    private List<Message> messages;
    
    /** Indicador de si se debe encriptar (true) o desencriptar (false) */
    private boolean encrypt;
    
    /** Barrera de sincronizacion para la finalizacion de hilos */
    private CountDownLatch latch;
    
    /**
    * Crea un nuevo hilo procesador de mensajes.
    * 
    * @param messages La lista de mensajes a procesar
    * @param encrypt Indicador de si se debe encriptar (true) o desencriptar (false)
    * @param latch Barrera de sincronizacion para la finalizacion de hilos
    */
    public MessageProcessorThread(List<Message> messages, boolean encrypt, CountDownLatch latch) {
        this.messages = messages;
        this.encrypt = encrypt;
        this.latch = latch;
    }
    
    /**
    * Ejecuta el procesamiento de mensajes cuando se inicia el hilo.
    * <p>
    * Este metodo itera a traves de los mensajes y realiza
    * encriptacion o desencriptacion basado en el indicador encrypt.
    * </p>
    */
    @Override
    public void run() {
        try {
            for (Message message : messages) {
                if (encrypt) {
                    message.encrypt();
                } else {
                    message.decrypt();
                }
                
                // Opcional: Agregar un pequeño retraso para demostrar entrelazado de hilos
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }
    
    /**
    * Metodo estatico de utilidad para procesar un lote de mensajes usando multiples hilos.
    * <p>
    * Este metodo distribuye la carga de trabajo entre el numero especificado de hilos
    * y espera a que todos los hilos completen antes de retornar.
    * </p>
    * 
    * @param messages La lista de mensajes a procesar
    * @param encrypt Indicador de si se debe encriptar (true) o desencriptar (false)
    * @param threadCount El numero de hilos a usar para el procesamiento
    */
    public static void processMessagesBatch(List<Message> messages, boolean encrypt, int threadCount) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        // Usar al menos un hilo, pero no mas que la cantidad de mensajes
        int actualThreadCount = Math.min(Math.max(1, threadCount), messages.size());
        CountDownLatch latch = new CountDownLatch(actualThreadCount);
        
        // Calcular mensajes por hilo
        int messagesPerThread = messages.size() / actualThreadCount;
        int remainingMessages = messages.size() % actualThreadCount;
        
        // Crear e iniciar hilos
        int startIndex = 0;
        for (int i = 0; i < actualThreadCount; i++) {
            int threadMessageCount = messagesPerThread + (i < remainingMessages ? 1 : 0);
            int endIndex = startIndex + threadMessageCount;
            
            List<Message> threadMessages = messages.subList(startIndex, endIndex);
            MessageProcessorThread thread = new MessageProcessorThread(threadMessages, encrypt, latch);
            thread.setName("MessageProcessor-" + (i + 1));
            thread.start();
            
            startIndex = endIndex;
        }
        
        // Esperar a que todos los hilos completen
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}