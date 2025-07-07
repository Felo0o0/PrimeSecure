/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.util;

import com.primesecure.model.Message;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles export and import of messages for backup and transfer.
 * <p>
 * This utility class provides functionality to save and load messages
 * from files, allowing for message backup and transfer between systems.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class MessageExporter {
    
    /**
     * Exports messages to a binary file.
     * 
     * @param messages List of messages to export
     * @param filePath Path to save the exported file
     * @return Number of messages exported
     * @throws IOException If file operations fail
     */
    public static int exportMessages(List<Message> messages, String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(messages);
            return messages.size();
        }
    }
    
    /**
     * Imports messages from a binary file.
     * 
     * @param filePath Path to the file containing exported messages
     * @return List of imported messages
     * @throws IOException If file operations fail
     * @throws ClassNotFoundException If the file format is invalid
     */
    @SuppressWarnings("unchecked")
    public static List<Message> importMessages(String filePath) 
            throws IOException, ClassNotFoundException {
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Message>) in.readObject();
        }
    }
    
    /**
     * Exports messages to a human-readable text file.
     * 
     * @param messages List of messages to export
     * @param filePath Path to save the text file
     * @param includeContent Whether to include message content in export
     * @return Number of messages exported
     * @throws IOException If file operations fail
     */
    public static int exportMessagesToText(List<Message> messages, String filePath, boolean includeContent) 
            throws IOException {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== PrimeSecure Messages Export ===");
            writer.println("Total messages: " + messages.size());
            writer.println();
            
            for (int i = 0; i < messages.size(); i++) {
                Message msg = messages.get(i);
                writer.println("Message #" + (i + 1));
                writer.println("From: " + msg.getSender());
                writer.println("To: " + msg.getRecipient());
                writer.println("Prime Code: " + msg.getPrimeCode());
                writer.println("Status: " + (msg.isEncrypted() ? "Encrypted" : "Plain text"));
                
                if (includeContent) {
                    writer.println("Content: " + msg.getContent());
                }
                
                writer.println("------------------------------");
            }
        }
        
        return messages.size();
    }
}