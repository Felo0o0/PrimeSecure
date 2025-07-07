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
 * Utilidad para exportar e importar mensajes.
 * <p>
 * Esta clase proporciona funcionalidad para guardar mensajes en archivos
 * y cargarlos nuevamente, tanto en formato binario como de texto.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class MessageExporter {
    
    /**
    * Exporta una lista de mensajes a un archivo binario.
    * 
    * @param messages La lista de mensajes a exportar
    * @param filePath La ruta del archivo donde guardar los mensajes
    * @return El numero de mensajes exportados
    * @throws IOException Si ocurre un error durante la escritura del archivo
    */
    public static int exportMessages(List<Message> messages, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messages);
            return messages.size();
        }
    }
    
    /**
    * Importa una lista de mensajes desde un archivo binario.
    * 
    * @param filePath La ruta del archivo desde donde cargar los mensajes
    * @return La lista de mensajes importados
    * @throws IOException Si ocurre un error durante la lectura del archivo
    * @throws ClassNotFoundException Si la clase de los objetos serializados no se encuentra
    */
    @SuppressWarnings("unchecked")
    public static List<Message> importMessages(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Message>) ois.readObject();
        }
    }
    
    /**
    * Exporta una lista de mensajes a un archivo de texto.
    * 
    * @param messages La lista de mensajes a exportar
    * @param filePath La ruta del archivo donde guardar los mensajes
    * @param includeContent Si se debe incluir el contenido de los mensajes
    * @return El numero de mensajes exportados
    * @throws IOException Si ocurre un error durante la escritura del archivo
    */
    public static int exportMessagesToText(List<Message> messages, String filePath, boolean includeContent) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("# Mensajes exportados desde PrimeSecure");
            writer.println("# Formato: Remitente | Destinatario | Estado | Codigo Primo | Contenido (opcional)");
            writer.println();
            
            for (Message message : messages) {
                writer.print(message.getSender() + " | ");
                writer.print(message.getRecipient() + " | ");
                writer.print((message.isEncrypted() ? "Encriptado" : "Desencriptado") + " | ");
                writer.print(message.getPrimeCode());
                
                if (includeContent) {
                    writer.print(" | " + message.getContent());
                }
                
                writer.println();
            }
            
            return messages.size();
        }
    }
    
    /**
    * Genera un archivo de ejemplo con mensajes aleatorios.
    * 
    * @param filePath La ruta del archivo donde guardar los mensajes
    * @param count El numero de mensajes a generar
    * @throws IOException Si ocurre un error durante la escritura del archivo
    */
    public static void generateSampleMessages(String filePath, int count) throws IOException {
        List<Message> sampleMessages = new ArrayList<>();
        
        String[] senders = {"Juan", "Maria", "Carlos", "Ana", "Pedro"};
        String[] recipients = {"Departamento IT", "Recursos Humanos", "Gerencia", "Ventas", "Soporte"};
        String[] contents = {
            "Hola, necesito ayuda con un problema",
            "Por favor revisa el informe adjunto",
            "La reunion se ha reprogramado para mañana",
            "Felicitaciones por tu ascenso",
            "Necesitamos discutir el nuevo proyecto"
        };
        
        for (int i = 0; i < count; i++) {
            int senderIdx = i % senders.length;
            int recipientIdx = (i + 1) % recipients.length;
            int contentIdx = (i + 2) % contents.length;
            int primeCode = PrimeCalculator.generateRandomPrime(100, 997);
            
            Message message = new Message(
                contents[contentIdx], 
                senders[senderIdx], 
                recipients[recipientIdx], 
                primeCode
            );
            
            // Encriptar algunos mensajes aleatoriamente
            if (i % 2 == 0) {
                message.encrypt();
            }
            
            sampleMessages.add(message);
        }
        
        exportMessages(sampleMessages, filePath);
    }
}