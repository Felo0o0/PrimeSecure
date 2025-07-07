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
 * Maneja la encriptacion y desencriptacion de archivos de texto usando multihilos.
 * <p>
 * Esta clase proporciona funcionalidad para procesar archivos en paralelo, 
 * dividiendo la carga de trabajo entre multiples hilos para mejorar el rendimiento.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class FileEncryptor {
    
    /**
    * Clase interna que representa un fragmento de texto a ser procesado por un hilo.
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
    * Implementacion de hilo para procesar fragmentos de texto.
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
    * Procesa un archivo de texto usando ejecucion paralela con multiples hilos.
    * 
    * @param inputFile Ruta al archivo de entrada
    * @param outputFile Ruta al archivo de salida
    * @param primeCode Numero primo a usar para encriptacion/desencriptacion
    * @param encrypt Indicador de si se debe encriptar (true) o desencriptar (false)
    * @param threadCount Numero de hilos a usar
    * @return Metricas de rendimiento como una cadena
    * @throws IOException Si fallan las operaciones de archivo
    */
    public static String processFile(String inputFile, String outputFile, 
                                    int primeCode, boolean encrypt, int threadCount) throws IOException {
        
        long startTime = System.currentTimeMillis();
        
        // Leer todo el contenido del archivo
        String content = new String(Files.readAllBytes(Paths.get(inputFile)));
        
        // Calcular tamaño de fragmento
        int chunkSize = Math.max(content.length() / threadCount, 1);
        List<TextChunk> chunks = new ArrayList<>();
        
        // Dividir contenido en fragmentos
        for (int i = 0; i < content.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, content.length());
            String chunkContent = content.substring(i, end);
            chunks.add(new TextChunk(chunkContent, primeCode, encrypt));
        }
        
        // Procesar fragmentos en paralelo
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<String>> futures = new ArrayList<>();
        
        for (TextChunk chunk : chunks) {
            Future<String> future = executor.submit(new TextProcessorThread(chunk));
            futures.add(future);
        }
        
        // Recolectar resultados
        StringBuilder result = new StringBuilder();
        try {
            for (Future<String> future : futures) {
                result.append(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error procesando archivo: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
        
        // Escribir resultado al archivo de salida
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(result.toString());
        }
        
        long endTime = System.currentTimeMillis();
        
        // Generar reporte de rendimiento
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