/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package com.primesecure;

import com.primesecure.model.Message;
import com.primesecure.model.PrimesList;
import com.primesecure.security.FileEncryptor;
import com.primesecure.util.BatchProcessor;
import com.primesecure.util.MessageExporter;
import com.primesecure.util.PrimeCalculator;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicacion principal del sistema PrimeSecure.
 * <p>
 * Esta clase proporciona una interfaz de consola para interactuar con
 * las diferentes funcionalidades del sistema de mensajeria segura.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeSecureApp {

    /** Scanner para entrada de usuario */
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
    * Punto de entrada principal de la aplicacion.
    * 
    * @param args Los argumentos de linea de comandos
    */
    public static void main(String[] args) {
        System.out.println("=== PrimeSecure App ===");
        System.out.println("Sistema de mensajeria segura basado en numeros primos");
        
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nSeleccione una opcion:");
            System.out.println("1. Buscar numeros primos en un rango");
            System.out.println("2. Enviar un mensaje");
            System.out.println("3. Encriptar/Desencriptar un archivo");
            System.out.println("4. Generar mensajes de prueba");
            System.out.println("5. Salir");
            
            System.out.print("\nOpcion: ");
            int option = readInt();
            
            try {
                switch (option) {
                    case 1:
                        findPrimesOption();
                        break;
                    case 2:
                        sendMessageOption();
                        break;
                    case 3:
                        fileEncryptionOption();
                        break;
                    case 4:
                        generateTestMessagesOption();
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Gracias por usar PrimeSecure App!");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
    * Maneja la opcion de busqueda de numeros primos.
    */
    private static void findPrimesOption() {
        System.out.println("\n=== Busqueda de Numeros Primos ===");
        
        System.out.print("Ingrese el inicio del rango: ");
        int start = readInt();
        
        System.out.print("Ingrese el fin del rango: ");
        int end = readInt();
        
        System.out.print("Ingrese el numero de hilos a utilizar: ");
        int threads = readInt();
        
        System.out.println("\nBuscando numeros primos...");
        
        PrimesList primes = BatchProcessor.findPrimesInRange(start, end, threads);
        
        System.out.println("\nSe encontraron " + primes.getPrimesCount() + " numeros primos.");
        
        if (primes.size() <= 100) {
            System.out.println("Numeros primos encontrados: " + primes);
        } else {
            System.out.println("Primeros 10 primos: " + primes.subList(0, 10));
            System.out.println("Ultimos 10 primos: " + primes.subList(primes.size() - 10, primes.size()));
        }
    }
    
    /**
    * Maneja la opcion de envio de mensajes.
    */
    private static void sendMessageOption() {
        System.out.println("\n=== Envio de Mensajes ===");
        
        scanner.nextLine(); // Limpiar buffer
        
        System.out.print("Ingrese el contenido del mensaje: ");
        String content = scanner.nextLine();
        
        System.out.print("Ingrese el remitente: ");
        String sender = scanner.nextLine();
        
        System.out.print("Ingrese el destinatario: ");
        String recipient = scanner.nextLine();
        
        // Generar un codigo primo aleatorio
        int primeCode = PrimeCalculator.generateRandomPrime(100, 997);
        System.out.println("Codigo primo generado: " + primeCode);
        
        // Crear el mensaje
        Message message = new Message(content, sender, recipient, primeCode);
        
        // Mostrar opciones
        System.out.println("\n1. Enviar en texto plano");
        System.out.println("2. Enviar encriptado");
        System.out.print("Opcion: ");
        int option = readInt();
        
        if (option == 2) {
            message.encrypt();
            System.out.println("\nMensaje encriptado: " + message.getContent());
        }
        
        // Simular envio
        System.out.println("\nMensaje enviado exitosamente!");
        System.out.println("Detalles del mensaje:");
        System.out.println("- Remitente: " + message.getSender());
        System.out.println("- Destinatario: " + message.getRecipient());
        System.out.println("- Estado: " + (message.isEncrypted() ? "Encriptado" : "Texto plano"));
        System.out.println("- Codigo primo: " + message.getPrimeCode());
        System.out.println("- Contenido: " + message.getContent());
    }
    
    /**
    * Maneja la opcion de encriptacion/desencriptacion de archivos.
    */
    private static void fileEncryptionOption() {
        System.out.println("\n=== Encriptacion/Desencriptacion de Archivos ===");
        
        scanner.nextLine(); // Limpiar buffer
        
        System.out.print("Ingrese la ruta del archivo de entrada: ");
        String inputFile = scanner.nextLine();
        
        System.out.print("Ingrese la ruta del archivo de salida: ");
        String outputFile = scanner.nextLine();
        
        System.out.print("Ingrese el codigo primo a utilizar: ");
        int primeCode = readInt();
        
        System.out.println("\n1. Encriptar archivo");
        System.out.println("2. Desencriptar archivo");
        System.out.print("Opcion: ");
        int option = readInt();
        
        boolean encrypt = (option == 1);
        
        System.out.print("Ingrese el numero de hilos a utilizar: ");
        int threads = readInt();
        
        try {
            String result = FileEncryptor.processFile(
                inputFile, outputFile, primeCode, encrypt, threads);
            
            System.out.println("\n" + result);
        } catch (IOException e) {
            System.out.println("Error procesando el archivo: " + e.getMessage());
        }
    }
    
    /**
    * Maneja la opcion de generacion de mensajes de prueba.
    */
    private static void generateTestMessagesOption() {
        System.out.println("\n=== Generacion de Mensajes de Prueba ===");
        
        System.out.print("Ingrese el numero de mensajes a generar: ");
        int count = readInt();
        
        scanner.nextLine(); // Limpiar buffer
        
        System.out.print("Ingrese la ruta del archivo donde guardar los mensajes: ");
        String filePath = scanner.nextLine();
        
        try {
            MessageExporter.generateSampleMessages(filePath, count);
            System.out.println("\nSe generaron " + count + " mensajes de prueba en " + filePath);
        } catch (IOException e) {
            System.out.println("Error generando mensajes: " + e.getMessage());
        }
    }
    
    /**
    * Metodo auxiliar para leer un entero desde la entrada estandar.
    * 
    * @return El entero leido
    */
    private static int readInt() {
        try {
            return Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un numero valido.");
            return readInt();
        }
    }
}