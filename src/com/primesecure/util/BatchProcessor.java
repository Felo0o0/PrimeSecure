/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.util;

import com.primesecure.model.Message;
import com.primesecure.thread.MessageProcessorThread;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes batches of messages from files.
 * <p>
 * This utility class handles loading multiple messages from a file
 * and processing them in parallel using threads.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class BatchProcessor {
    
    /**
     * Loads messages from a CSV file and processes them in parallel.
     * <p>
     * Expected CSV format: content,sender,recipient,primeCode
     * </p>
     * 
     * @param filePath Path to the CSV file containing messages
     * @param encrypt Whether to encrypt (true) or decrypt (false) the messages
     * @param threadCount Number of threads to use for processing
     * @return List of processed messages
     * @throws IOException If file operations fail
     */
    public static List<Message> processBatchFromCsv(String filePath, boolean encrypt, int threadCount) 
            throws IOException {
        
        List<Message> messages = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header if present
            if ((line = reader.readLine()) != null && line.startsWith("content")) {
                // This was the header, move to next line
                line = reader.readLine();
            }
            
            // Read messages
            while (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String content = parts[0];
                    String sender = parts[1];
                    String recipient = parts[2];
                    int primeCode = Integer.parseInt(parts[3]);
                    
                    Message message = new Message(content, sender, recipient, primeCode);
                    messages.add(message);
                }
                line = reader.readLine();
            }
        }
        
        // Process messages using threads
        if (!messages.isEmpty()) {
            MessageProcessorThread.processMessagesBatch(messages, encrypt, threadCount);
        }
        
        return messages;
    }
    
    /**
     * Saves a list of messages to a CSV file.
     * 
     * @param messages The list of messages to save
     * @param filePath The path where to save the CSV file
     * @return Number of messages saved
     * @throws IOException If file operations fail
     */
    public static int saveMessagesToCsv(List<Message> messages, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("content,sender,recipient,primeCode,encryptionStatus");
            
            // Write messages
            for (Message message : messages) {
                writer.printf("%s,%s,%s,%d,%s\n",
                    message.getContent().replace(",", "\\,"),
                    message.getSender().replace(",", "\\,"),
                    message.getRecipient().replace(",", "\\,"),
                    message.getPrimeCode(),
                    message.isEncrypted() ? "encrypted" : "plain"
                );
            }
        }
        
        return messages.size();
    }
    
    /**
     * Generates a template CSV file for batch message processing.
     * 
     * @param filePath The path where to save the template
     * @param sampleCount Number of sample rows to include
     * @throws IOException If file operations fail
     */
    public static void generateBatchTemplate(String filePath, int sampleCount) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("content,sender,recipient,primeCode");
            
            // Write sample rows
            for (int i = 1; i <= sampleCount; i++) {
                int randomPrime = PrimeCalculator.generateRandomPrime(100, 997);
                writer.printf("Mensaje de ejemplo %d,Remitente%d,Destinatario%d,%d\n", 
                             i, i, i, randomPrime);
            }
        }
    }
}