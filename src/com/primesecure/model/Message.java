/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.primesecure.model;

import java.io.Serializable;

/**
 * Represents a secure message in the PrimeSecure messaging system.
 * <p>
 * This class encapsulates all information related to a message, including
 * its content, sender, recipient, and the prime number used for encryption.
 * </p>
 * 
 * @author PrimeSecure Team
 * @version 1.0
 * @since 2023-07-01
 */
public class Message implements Serializable {
    
    /** Serialization version UID */
    private static final long serialVersionUID = 1L;
    
    /** The content of the message (can be plain or encrypted) */
    private String content;
    
    /** The sender of the message */
    private String sender;
    
    /** The recipient of the message */
    private String recipient;
    
    /** The prime code used for encryption/decryption */
    private int primeCode;
    
    /** Flag indicating whether the message is currently encrypted */
    private boolean encrypted;
    
    /**
     * Creates a new message with specified sender and recipient.
     * <p>
     * The message is created in unencrypted state by default.
     * </p>
     * 
     * @param content The content of the message
     * @param sender The sender of the message
     * @param recipient The recipient of the message
     * @param primeCode The prime code to be used for encryption/decryption
     */
    public Message(String content, String sender, String recipient, int primeCode) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.primeCode = primeCode;
        this.encrypted = false;
    }
    
    /**
     * Creates a simplified message with default sender and recipient.
     * <p>
     * This constructor sets the sender to "Sistema" and recipient to "Usuario".
     * </p>
     * 
     * @param content The content of the message
     * @param primeCode The prime code to be used for encryption/decryption
     */
    public Message(String content, int primeCode) {
        this(content, "Sistema", "Usuario", primeCode);
    }
    
    /**
     * Encrypts the message content using the prime code.
     * <p>
     * This method transforms the original content using the prime number
     * as an encryption key. The implementation uses the PrimeEncoder.
     * </p>
     * 
     * @see com.primesecure.security.PrimeEncoder
     */
    public void encrypt() {
        if (!encrypted) {
            this.content = new com.primesecure.security.PrimeEncoder().encode(content, primeCode);
            this.encrypted = true;
        }
    }
    
    /**
     * Decrypts the message content using the prime code.
     * <p>
     * This method restores the original content using the prime number
     * as a decryption key. The implementation uses the PrimeDecoder.
     * </p>
     * 
     * @see com.primesecure.security.PrimeDecoder
     */
    public void decrypt() {
        if (encrypted) {
            this.content = new com.primesecure.security.PrimeDecoder().decode(content, primeCode);
            this.encrypted = false;
        }
    }
    
    /**
     * Gets the content of the message.
     * 
     * @return The content (may be encrypted or plain)
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Gets the sender of the message.
     * 
     * @return The sender's identifier
     */
    public String getSender() {
        return sender;
    }
    
    /**
     * Gets the recipient of the message.
     * 
     * @return The recipient's identifier
     */
    public String getRecipient() {
        return recipient;
    }
    
    /**
     * Gets the prime code used for encryption/decryption.
     * 
     * @return The prime number code
     */
    public int getPrimeCode() {
        return primeCode;
    }
    
    /**
     * Checks if the message is currently encrypted.
     * 
     * @return true if the message is encrypted, false otherwise
     */
    public boolean isEncrypted() {
        return encrypted;
    }
}