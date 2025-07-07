/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.thread;

import com.primesecure.model.Message;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A thread implementation for processing messages in batch.
 * <p>
 * This class provides functionality to encrypt or decrypt multiple
 * messages concurrently using multiple threads.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class MessageProcessorThread extends Thread {
    
    /** The list of messages to process */
    private List<Message> messages;
    
    /** Flag indicating whether to encrypt (true) or decrypt (false) */
    private boolean encrypt;
    
    /** Synchronization barrier for thread completion */
    private CountDownLatch latch;
    
    /**
     * Creates a new message processor thread.
     * 
     * @param messages The list of messages to process
     * @param encrypt Flag indicating whether to encrypt (true) or decrypt (false)
     * @param latch Synchronization barrier for thread completion
     */
    public MessageProcessorThread(List<Message> messages, boolean encrypt, CountDownLatch latch) {
        this.messages = messages;
        this.encrypt = encrypt;
        this.latch = latch;
    }
    
    /**
     * Executes the message processing when the thread is started.
     * <p>
     * This method iterates through the messages and performs either
     * encryption or decryption based on the encrypt flag.
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
                
                // Optional: Add a small delay to demonstrate thread interleaving
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }
    
    /**
     * Static utility method to process a batch of messages using multiple threads.
     * <p>
     * This method distributes the workload across the specified number of threads
     * and waits for all threads to complete before returning.
     * </p>
     * 
     * @param messages The list of messages to process
     * @param encrypt Flag indicating whether to encrypt (true) or decrypt (false)
     * @param threadCount The number of threads to use for processing
     */
    public static void processMessagesBatch(List<Message> messages, boolean encrypt, int threadCount) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        
        // Use at least one thread, but not more than the message count
        int actualThreadCount = Math.min(Math.max(1, threadCount), messages.size());
        CountDownLatch latch = new CountDownLatch(actualThreadCount);
        
        // Calculate messages per thread
        int messagesPerThread = messages.size() / actualThreadCount;
        int remainingMessages = messages.size() % actualThreadCount;
        
        // Create and start threads
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
        
        // Wait for all threads to complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}