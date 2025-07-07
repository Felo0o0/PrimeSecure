/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.app;

import com.primesecure.model.Message;
import com.primesecure.model.PrimesList;
import com.primesecure.security.FileEncryptor;
import com.primesecure.security.PrimeEncoder;
import com.primesecure.security.PrimeDecoder;
import com.primesecure.thread.MessageProcessorThread;
import com.primesecure.thread.PrimeCheckerThread;
import com.primesecure.util.BatchProcessor;
import com.primesecure.util.MessageExporter;
import com.primesecure.util.PrimeCalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main application class for the PrimeSecure messaging system.
 * <p>
 * This class provides a console interface for users to interact with
 * the secure messaging system. It demonstrates the use of prime numbers
 * for encryption and the implementation of multithreading for performance.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class PrimeSecureApp {
    
    /** Scanner for reading user input */
    private static Scanner scanner = new Scanner(System.in);
    
    /** Collection of messages in the system */
    private static List<Message> messages = new ArrayList<>();
    
    /** Collection of prime numbers for use in encryption */
    private static PrimesList primesList = new PrimesList();
    
    /** Random number generator */
    private static Random random = new Random();
    
    /**
     * Application entry point.
     * <p>
     * Initializes the system and presents the main menu to the user.
     * </p>
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("¡Bienvenido al Sistema de Mensajeria Segura PrimeSecure!");
        System.out.println("Tu comunicacion segura es nuestra prioridad.");
        
        // Initialize prime numbers list with random primes
        initializePrimesList();
        
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Crear nuevo mensaje [Threading]");
            System.out.println("2. Ver mensajes");
            System.out.println("3. Encriptar/Desencriptar archivo [Threading]");
            System.out.println("4. Procesar lotes de mensajes [Threading]");
            System.out.println("5. Desencriptar mensaje [Threading]");
            System.out.println("6. Gestionar codigos primos");
            System.out.println("7. Buscar primos en rango [Threading]");
            System.out.println("8. Exportar/Importar mensajes");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (opcion) {
                case 1:
                    crearMensaje();
                    break;
                case 2:
                    verMensajes();
                    break;
                case 3:
                    procesarArchivo();
                    break;
                case 4:
                    procesarLoteMensajes();
                    break;
                case 5:
                    desencriptarMensaje();
                    break;
                case 6:
                    gestionarPrimos();
                    break;
                case 7:
                    buscarPrimosEnRango();
                    break;
                case 8:
                    exportarImportarMensajes();
                    break;
                case 9:
                    salir = true;
                    System.out.println("¡Gracias por usar PrimeSecure! Tu informacion esta segura con nosotros.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        }
    }
    
    /**
     * Initializes the primes list with random prime numbers.
     */
    private static void initializePrimesList() {
        System.out.println("Generando numeros primos aleatorios para mayor seguridad...");
        
        // Generate 20 random primes between 100 and 1000
        for (int i = 0; i < 20; i++) {
            int randomPrime = PrimeCalculator.generateRandomPrime(100, 1000);
            if (randomPrime > 0 && !primesList.contains(randomPrime)) {
                primesList.add(randomPrime);
            }
        }
        
        System.out.println("Sistema inicializado con " + primesList.size() + " numeros primos aleatorios.");
    }
    
    /**
     * Handles the creation of a new message with automatic encryption.
     */
    private static void crearMensaje() {
        System.out.println("\n=== CREAR NUEVO MENSAJE ===");
        
        // Option for simplified messages
        System.out.println("1. Mensaje completo (con remitente/destinatario)");
        System.out.println("2. Mensaje simple (sin remitente/destinatario)");
        System.out.print("Seleccione opcion: ");
        int tipoMensaje = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        String remitente = "Sistema";
        String destinatario = "Usuario";
        
        if (tipoMensaje == 1) {
            System.out.print("Remitente: ");
            remitente = scanner.nextLine();
            
            System.out.print("Destinatario: ");
            destinatario = scanner.nextLine();
        }
        
        System.out.print("Contenido del mensaje: ");
        String contenido = scanner.nextLine();
        
        // Generate random prime code between 2 and 997
        int codigoPrimo = PrimeCalculator.generateRandomPrime(2, 997);
        
        System.out.println("Generando codigo primo aleatorio para maxima seguridad...");
        
        // Create message based on selected type
        Message mensaje;
        if (tipoMensaje == 1) {
            mensaje = new Message(contenido, remitente, destinatario, codigoPrimo);
        } else {
            mensaje = new Message(contenido, codigoPrimo);
        }
        
        // Encrypt automatically using threads
        System.out.println("Cifrando mensaje con procesamiento multihilo...");
        List<Message> mensajesParaEncriptar = new ArrayList<>();
        mensajesParaEncriptar.add(mensaje);
        MessageProcessorThread.processMessagesBatch(mensajesParaEncriptar, true, 1);
        
        messages.add(mensaje);
        System.out.println("¡Mensaje creado y encriptado con codigo primo: " + codigoPrimo + "!");
        System.out.println("Tu mensaje esta ahora protegido contra accesos no autorizados.");
    }
    
    /**
     * Displays all messages in the system.
     */
    private static void verMensajes() {
        System.out.println("\n=== MENSAJES ===");
        
        if (messages.isEmpty()) {
            System.out.println("No hay mensajes en el sistema. ¡Tu buzon esta vacio!");
            return;
        }
        
        for (int i = 0; i < messages.size(); i++) {
            Message mensaje = messages.get(i);
            System.out.println((i + 1) + ". De: " + mensaje.getSender() + 
                               " | Para: " + mensaje.getRecipient() + 
                               " | Estado: " + (mensaje.isEncrypted() ? "Encriptado" : "Desencriptado") + 
                               " | Codigo Primo: " + mensaje.getPrimeCode());
            System.out.println("   Contenido: " + mensaje.getContent());
            System.out.println("----------------------------");
        }
    }
    
    /**
     * Handles file encryption/decryption operations.
     */
    private static void procesarArchivo() {
        System.out.println("\n=== ENCRIPTAR/DESENCRIPTAR ARCHIVO [Threading] ===");
        System.out.println("1. Encriptar archivo de texto");
        System.out.println("2. Desencriptar archivo de texto");
        System.out.println("3. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (opcion == 3) return;
        
        boolean encriptar = (opcion == 1);
        String operacion = encriptar ? "encriptar" : "desencriptar";
        
        System.out.print("Ruta del archivo a " + operacion + ": ");
        String archivoEntrada = scanner.nextLine();
        
        System.out.print("Ruta del archivo de salida: ");
        String archivoSalida = scanner.nextLine();
        
        System.out.print("Codigo primo a utilizar: ");
        int codigoPrimo = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (!PrimesList.isPrime(codigoPrimo)) {
            System.out.println("¡Advertencia! El numero ingresado no es primo. Esto podria comprometer la seguridad.");
            System.out.print("¿Desea generar un codigo primo aleatorio? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                codigoPrimo = PrimeCalculator.generateRandomPrime(100, 997);
                System.out.println("Codigo primo generado: " + codigoPrimo);
            }
        }
        
        System.out.print("Numero de hilos a utilizar (recomendado: 4): ");
        int numHilos = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (numHilos < 1) numHilos = 4;
        
        try {
            System.out.println("Procesando archivo con " + numHilos + " hilos...");
            String resultado = FileEncryptor.processFile(
                archivoEntrada, archivoSalida, codigoPrimo, encriptar, numHilos);
            
            System.out.println(resultado);
            System.out.println("¡Archivo procesado exitosamente! Disfruta de la seguridad prima.");
            
        } catch (IOException e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Processes batches of messages from a CSV file.
     */
    private static void procesarLoteMensajes() {
        System.out.println("\n=== PROCESAR LOTE DE MENSAJES [Threading] ===");
        System.out.println("1. Cargar y procesar mensajes desde CSV");
        System.out.println("2. Generar plantilla CSV para lotes");
        System.out.println("3. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (opcion) {
            case 1:
                System.out.print("Ruta del archivo CSV: ");
                String rutaArchivo = scanner.nextLine();
                
                System.out.println("1. Encriptar mensajes");
                System.out.println("2. Desencriptar mensajes");
                System.out.print("Seleccione operacion: ");
                boolean encriptar = scanner.nextInt() == 1;
                scanner.nextLine(); // Consume newline
                
                System.out.print("Numero de hilos a utilizar (recomendado: 4): ");
                int numHilos = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (numHilos < 1) numHilos = 4;
                
                try {
                    System.out.println("Procesando lote con " + numHilos + " hilos simultaneos...");
                    List<Message> mensajesProcesados = BatchProcessor.processBatchFromCsv(
                        rutaArchivo, encriptar, numHilos);
                    
                    System.out.println("¡" + mensajesProcesados.size() + " mensajes procesados exitosamente!");
                    
                    System.out.print("¿Desea agregar estos mensajes al sistema? (S/N): ");
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        messages.addAll(mensajesProcesados);
                        System.out.println("Mensajes agregados al sistema. ¡La seguridad en numeros primos!");
                    }
                    
                } catch (IOException e) {
                    System.out.println("Error al procesar el lote: " + e.getMessage());
                }
                break;
                
            case 2:
                System.out.print("Ruta para guardar la plantilla: ");
                String rutaPlantilla = scanner.nextLine();
                
                System.out.print("Numero de ejemplos a incluir: ");
                int numEjemplos = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                try {
                    BatchProcessor.generateBatchTemplate(rutaPlantilla, numEjemplos);
                    System.out.println("¡Plantilla generada exitosamente en " + rutaPlantilla + "!");
                    System.out.println("Edite el archivo y luego cargue los mensajes con la opcion 1.");
                } catch (IOException e) {
                    System.out.println("Error al generar plantilla: " + e.getMessage());
                }
                break;
                
            case 3:
                // Return to main menu
                break;
                
            default:
                System.out.println("Opcion invalida.");
        }
    }
    
    /**
     * Handles message decryption.
     */
    private static void desencriptarMensaje() {
        System.out.println("\n=== DESENCRIPTAR MENSAJE [Threading] ===");
        
        if (messages.isEmpty()) {
            System.out.println("No hay mensajes para desencriptar. ¡Tu buzon esta vacio!");
            return;
        }
        
        // Display encrypted messages
        List<Message> mensajesEncriptados = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            Message mensaje = messages.get(i);
            if (mensaje.isEncrypted()) {
                mensajesEncriptados.add(mensaje);
                System.out.println((mensajesEncriptados.size()) + ". De: " + mensaje.getSender() + 
                                   " | Para: " + mensaje.getRecipient() + 
                                   " | Codigo Primo: " + mensaje.getPrimeCode());
                System.out.println("   Contenido: " + mensaje.getContent());
            }
        }
        
        if (mensajesEncriptados.isEmpty()) {
            System.out.println("No hay mensajes encriptados para desencriptar. ¡Todo esta en texto claro!");
            return;
        }
        
        System.out.print("Seleccione el mensaje a desencriptar (1-" + 
                        mensajesEncriptados.size() + "): ");
        int indice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (indice < 1 || indice > mensajesEncriptados.size()) {
            System.out.println("Indice invalido. ¡Intentalo de nuevo!");
            return;
        }
        
        Message mensajeSeleccionado = mensajesEncriptados.get(indice - 1);
        
        System.out.println("Desencriptando mensaje con codigo primo " + 
                         mensajeSeleccionado.getPrimeCode() + " usando threading...");
        
        // Use thread for decryption
        List<Message> mensajesParaDesencriptar = new ArrayList<>();
        mensajesParaDesencriptar.add(mensajeSeleccionado);
        MessageProcessorThread.processMessagesBatch(mensajesParaDesencriptar, false, 1);
        
        System.out.println("¡Mensaje desencriptado exitosamente!");
        System.out.println("Contenido desencriptado: " + mensajeSeleccionado.getContent());
    }
    
    /**
     * Manages prime numbers used in the system.
     */
    private static void gestionarPrimos() {
        System.out.println("\n=== GESTIONAR CODIGOS PRIMOS ===");
        System.out.println("1. Ver lista de primos");
        System.out.println("2. Agregar primo");
        System.out.println("3. Eliminar primo");
        System.out.println("4. Generar nuevos primos aleatorios");
        System.out.println("5. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (opcion) {
            case 1:
                System.out.println("\nLista de numeros primos: ");
                for (int i = 0; i < primesList.size(); i++) {
                    System.out.print(primesList.get(i) + " ");
                    if ((i + 1) % 10 == 0) {
                        System.out.println();
                    }
                }
                System.out.println("\nTotal: " + primesList.size() + " numeros primos");
                break;
                
            case 2:
                System.out.print("Ingrese el numero primo a agregar: ");
                int nuevoPrimo = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (primesList.add(nuevoPrimo)) {
                    System.out.println("¡Numero primo agregado exitosamente!");
                } else {
                    System.out.println("El numero no es primo o ya existe en la lista. ¡Intenta con otro!");
                }
                break;
                
            case 3:
                System.out.print("Ingrese el numero primo a eliminar: ");
                int primoEliminar = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                if (primesList.remove(Integer.valueOf(primoEliminar))) {
                    System.out.println("¡Numero primo eliminado exitosamente!");
                } else {
                    System.out.println("El numero no existe en la lista. ¿Estas seguro que lo ingresaste bien?");
                }
                break;
                
            case 4:
                primesList.clear();
                initializePrimesList();
                break;
                
            case 5:
                // Return to main menu
                break;
                
            default:
                System.out.println("Opcion invalida. ¿Quizas un numero no primo? ;)");
        }
    }
    
    /**
     * Searches for prime numbers in a range using multiple threads.
     */
    private static void buscarPrimosEnRango() {
        System.out.println("\n=== BUSQUEDA DE PRIMOS EN RANGO [Threading] ===");
        
        System.out.print("Inicio del rango: ");
        int inicio = scanner.nextInt();
        
        System.out.print("Fin del rango: ");
        int fin = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Numero de hilos a utilizar: ");
        int numHilos = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (numHilos < 1) numHilos = 2;
        
        // Shared list to store results
        PrimesList listaResultados = new PrimesList();
        
        // Divide range among threads
        int rangoTotal = fin - inicio + 1;
        int rangoPorHilo = rangoTotal / numHilos;
        
        System.out.println("¡Iniciando busqueda con " + numHilos + " hilos en paralelo!");
        System.out.println("Cada hilo buscara aproximadamente " + rangoPorHilo + " numeros...");
        
        // Create and start threads
        PrimeCheckerThread[] hilos = new PrimeCheckerThread[numHilos];
        
        for (int i = 0; i < numHilos; i++) {
            int inicioHilo = inicio + (i * rangoPorHilo);
            int finHilo = (i == numHilos - 1) ? fin : inicio + ((i + 1) * rangoPorHilo) - 1;
            
            hilos[i] = new PrimeCheckerThread(inicioHilo, finHilo, listaResultados);
            hilos[i].setName("BuscadorPrimos-" + (i + 1));
            hilos[i].start();
        }
        
        // Wait for all threads to complete
        try {
            for (PrimeCheckerThread hilo : hilos) {
                hilo.join();
            }
            
            System.out.println("\n¡Se encontraron " + listaResultados.size() + 
                             " numeros primos entre " + inicio + " y " + fin + "!");
            
            System.out.print("¿Agregar estos primos a la lista principal? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                for (Integer primo : listaResultados) {
                    if (!primesList.contains(primo)) {
                        primesList.add(primo);
                    }
                }
                System.out.println("¡Primos agregados a la lista principal! Tu sistema ahora es mas seguro.");
            }
        } catch (InterruptedException e) {
            System.out.println("La busqueda fue interrumpida. ¿Quiza demasiados primos?");
        }
    }
    
    /**
     * Handles export and import of messages.
     */
    private static void exportarImportarMensajes() {
        System.out.println("\n=== EXPORTAR/IMPORTAR MENSAJES ===");
        System.out.println("1. Exportar mensajes (formato binario)");
        System.out.println("2. Importar mensajes (formato binario)");
        System.out.println("3. Exportar mensajes (formato texto)");
        System.out.println("4. Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (opcion) {
            case 1:
                System.out.print("Ruta para guardar los mensajes: ");
                String rutaExportar = scanner.nextLine();
                
                try {
                    int count = MessageExporter.exportMessages(messages, rutaExportar);
                    System.out.println("¡" + count + " mensajes exportados exitosamente a " + rutaExportar + "!");
                } catch (IOException e) {
                    System.out.println("Error al exportar mensajes: " + e.getMessage());
                }
                break;
                
            case 2:
                System.out.print("Ruta del archivo de mensajes: ");
                String rutaImportar = scanner.nextLine();
                
                try {
                    List<Message> mensajesImportados = MessageExporter.importMessages(rutaImportar);
                    
                    System.out.println("Se encontraron " + mensajesImportados.size() + " mensajes.");
                    System.out.print("¿Desea agregar estos mensajes al sistema? (S/N): ");
                    
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        messages.addAll(mensajesImportados);
                        System.out.println("¡Mensajes importados exitosamente! Tu coleccion de mensajes ha crecido.");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error al importar mensajes: " + e.getMessage());
                }
                break;
                
            case 3:
                System.out.print("Ruta para guardar el archivo de texto: ");
                String rutaTexto = scanner.nextLine();
                
                System.out.print("¿Incluir contenido de los mensajes? (S/N): ");
                boolean incluirContenido = scanner.nextLine().equalsIgnoreCase("S");
                
                try {
                    int count = MessageExporter.exportMessagesToText(messages, rutaTexto, incluirContenido);
                    System.out.println("¡" + count + " mensajes exportados como texto a " + rutaTexto + "!");
                } catch (IOException e) {
                    System.out.println("Error al exportar mensajes como texto: " + e.getMessage());
                }
                break;
                
            case 4:
                // Return to main menu
                break;
                
            default:
                System.out.println("Opcion invalida. ¿Quizas un numero primo seria valido? ;)");
        }
    }
}