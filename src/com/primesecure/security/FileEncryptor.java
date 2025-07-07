/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.security;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Handles encryption and decryption of text files using multithreading.
 * <p>
 * This class provides functionality to process files in parallel, 
 * dividing the workload among multiple threads for improved performance.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class FileEncryptor {
    
    /**
     * Inner class representing a chunk of text to be processed by a thread.
     */
    private static class TextChunk {
        private String content;
        private int primeCode;
        private boolean encrypt;
        
        public TextChunk(String content, int primeCode, boolean encrypt) {
            this.content = content;
            this.primeCode = primeCode;
            this.encrypt = encrypt;
        }
    }
    
    /**
     * Thread implementation for processing text chunks.
     */
    private static class TextProcessorThread implements Callable<String> {
        private TextChunk chunk;
        
        public TextProcessorThread(TextChunk chunk) {
            this.chunk = chunk;
        }
        
        @Override
        public String call() {
            if (chunk.encrypt) {
                return new PrimeEncoder().encode(chunk.content, chunk.primeCode);
            } else {
                return new PrimeDecoder().decode(chunk.content, chunk.primeCode);
            }
        }
    }
    
    /**
     * Processes a text file using parallel execution with multiple threads.
     * 
     * @param inputFile Path to the input file
     * @param outputFile Path to the output file
     * @param primeCode Prime number to use for encryption/decryption
     * @param encrypt Flag indicating whether to encrypt (true) or decrypt (false)
     * @param threadCount Number of threads to use
     * @return Performance metrics as a string
     * @throws IOException If file operations fail
     */
    public static String processFile(String inputFile, String outputFile, 
                                   int primeCode, boolean encrypt, int threadCount) throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        // Read entire file content
        String content = new String(Files.readAllBytes(Paths.get(inputFile)));
        
        // Calculate chunk size
        int chunkSize = Math.max(content.length() / threadCount, 1);
        List<TextChunk> chunks = new ArrayList<>();
        
        // Divide content into chunks
        for (int i = 0; i < content.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, content.length());
            String chunkContent = content.substring(i, end);
            chunks.add(new TextChunk(chunkContent, primeCode, encrypt));
        }
        
        // Process chunks in parallel
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<String>> futures = new ArrayList<>();
        
        for (TextChunk chunk : chunks) {
            Future<String> future = executor.submit(new TextProcessorThread(chunk));
            futures.add(future);
        }
        
        // Collect results
        StringBuilder result = new StringBuilder();
        try {
            for (Future<String> future : futures) {
                result.append(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error processing file: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
        
        // Write result to output file
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(result.toString());
        }
        
        long endTime = System.currentTimeMillis();
        
        // Generate performance report
        return String.format(
            "Archivo procesado exitosamente!\n" +
            "Operacion: %s\n" +
            "Tamano del archivo: %d bytes\n" +
            "Hilos utilizados: %d\n" +
            "Tiempo de ejecucion: %d ms",
            encrypt ? "Encriptacion" : "Desencriptacion",
            content.length(),
            threadCount,
            (endTime - startTime)
        );
    }
}