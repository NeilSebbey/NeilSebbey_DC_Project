/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package server;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.*;
import java.security.KeyStore;

/*
 * This module contains the application logic of an SMP server
 * which uses a stream socket for interprocess  communication.
 * A command-line argument is required to specify the server port.
 * original author: M. L. Liu
 */
public class SMPServer {

   public static void main(String[] args) {
       // Sample pass details used.
       String ksName = "src/server/myMTU.jks";
       char ksPass[] = "mtu2021kerry".toCharArray();
       char ctPass[] = "mtu2021kerry".toCharArray();
      int serverPort = 8888;    // default port

       if (args.length == 1 )
         serverPort = Integer.parseInt(args[0]);
      try {
          /*****************************************************
           *    SSL Lab (Week 2)
           *    Title:    Code for Sockets and SSL Labs (1) (2).zip
           *    Author: HerongYang.com
           *    Site owner/sponsor:  mtukerry.instructure.com (MTU Kerry Canvas)
           *    Date: 02/02/2023
           *    Code version:  2014
           *    Availability:  https://mtukerry.instructure.com/courses/1347/files/30987?module_item_id=2988
           *                   (Accessed 08/03/2023)
           *    Modified:  Code refactored (identifiers renamed)
           *****************************************************/
          // KeyManagerFactory
          KeyStore ks = KeyStore.getInstance("JKS");
          ks.load(new FileInputStream(ksName), ksPass);
          KeyManagerFactory kmf =
                  KeyManagerFactory.getInstance("SunX509");
          kmf.init(ks, ctPass);
          SSLContext sc = SSLContext.getInstance("TLS");
          sc.init(kmf.getKeyManagers(), null, null);
          SSLServerSocketFactory ssf = sc.getServerSocketFactory();

         // instantiates a SSL stream socket for accepting
         //   connections
          SSLServerSocket myConnectionSocket =
                  (SSLServerSocket) ssf.createServerSocket(serverPort);
          // print server socket information in the debug console
          printServerSocketInfo(myConnectionSocket);
          // end of refactored code
/**/     System.out.println("OK 100: SMP server ready.");
         while (true) {  // forever loop
            // wait to accept a connection 
            /**/
            System.out.println("OK 100: Waiting for a connection.");

            SMPStreamSocket myDataSocket = new SMPStreamSocket
                    ((SSLSocket) myConnectionSocket.accept());
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

    // Print Socket info for server
    private static void printServerSocketInfo(SSLServerSocket s) {
        System.out.println("Server socket class: "+s.getClass());
        System.out.println("   Socket address = "
                +s.getInetAddress().toString());
        System.out.println("   Socket port = "
                +s.getLocalPort());
        System.out.println("   Need client authentication = "
                +s.getNeedClientAuth());
        System.out.println("   Want client authentication = "
                +s.getWantClientAuth());
        System.out.println("   Use client mode = "
                +s.getUseClientMode());
    }

} // end class
