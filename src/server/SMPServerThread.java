/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package server;

import java.io.*;
import java.util.Arrays;

/*
 * This module contains the application logic of an SMP server
 * which uses a stream socket for interprocess  communication.
 * A command-line argument is required to specify the server port.
 * original author: M. L. Liu
 */
public class SMPServerThread implements Runnable {
    static final String endMessage = ".";
    static final String endMessages = ",";
    SMPStreamSocket myDataSocket;

    SMPServerThread(SMPStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    public void run( ) {
        boolean done = false;
        boolean loggedIn;
        String message, userStr, passStr;
        String[] messages;
        try {
            System.out.println("Client is not logged in. Waiting for authentication...");
            while (!done) {
                loggedIn = false;
                if(loggedIn==false){
                    // Retrieve username and password strings from client and set
                    // loggedIn boolean to true
                    userStr = myDataSocket.receiveUser();
                    passStr = myDataSocket.receivePass();
                    if (userStr != null && !userStr.isEmpty() && passStr != null && !passStr.isEmpty()) {
                        loggedIn = true;

                        while (loggedIn) {
                            // Username and password strings passed through the message
                            // variable. Hide login strings in console using a blank
                            // message string. Sample passwords are only used.
                            message = myDataSocket.receiveMessage( );
                            if ((message.trim()).equals(userStr)) {
                                System.out.println("Client is logged in as: " + message);
                                message = "";
                            }   // end if message.equals(userStr)
                            if ((message.trim()).equals(passStr)) {
                                String stars = message.replaceAll(message,"********");
                                System.out.println(stars);
                                message = "";
                            }   // end if message.equals(passStr)

                            /**/           System.out.println("message received: "+ message);
                            if ((message.trim()).equals (endMessage)){
                                //Session over; close the data socket.
                                /**/              System.out.println("Session over.");
                                myDataSocket.close( );
                                done = true;
                            }   // end if
                            else if ((message.trim()).equals(endMessages)) {
                                // Send messages from server to the client.
                                messages = myDataSocket.receiveMessages();
                                System.out.println("messages sent: " + "\n" + Arrays.toString(messages));
                            }   // end else-if
                            else {
                                // Now send the echo to the requestor
                                myDataSocket.sendMessage(message);
                            } //end else

                        }   // end while(loggedIn)
                    }   // end if
                    else {
                        System.out.println("Client is not logged in. Waiting for authentication...");
                    }   // end else
                }   // end if(loggedIn)
            }   //end while !done

        }   //end try
        catch (Exception ex) {
            System.out.println("Exception caught in thread: " + ex);
        }   // end catch
    }   //end run


}   // end class
