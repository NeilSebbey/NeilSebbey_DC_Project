/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package server;

import java.net.*;

/*
 * This module contains the application logic of an SMP server
 * which uses a stream socket for interprocess  communication.
 * A command-line argument is required to specify the server port.
 * original author: M. L. Liu
 */
public class SMPServer {

   public static void main(String[] args) {
      int serverPort = 7;    // default port

       if (args.length == 1 )
         serverPort = Integer.parseInt(args[0]);       
      try {
         // instantiates a stream socket for accepting
         //   connections
   	   ServerSocket myConnectionSocket = 
            new ServerSocket(serverPort); 
/**/     System.out.println("OK 100: SMP server ready.");
         while (true) {  // forever loop
            // wait to accept a connection 
            /**/
            System.out.println("OK 100: Waiting for a connection.");
            SMPStreamSocket myDataSocket = new SMPStreamSocket
                    (myConnectionSocket.accept());
            /**/
            System.out.println("OK 202: Connection accepted");

            // Start a thread to handle this client's session
            Thread theThread =
                    new Thread(new SMPServerThread(myDataSocket));
            theThread.start();
            // and go on to the next client

         } //end while forever
       } // end try
     catch (Exception ex) {
       ex.printStackTrace( );
     }   // end catch
   } //end main


} // end class
