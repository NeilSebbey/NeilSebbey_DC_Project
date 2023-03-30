/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package server;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*****************************************************
 *    Starting point code base
 *    Title:    Client Server code
 *    Author: Peter Given & M. L. Liu
 *    Site owner/sponsor:  mtukerry.instructure.com (MTU Kerry Canvas)
 *    Date: 15/02/2023
 *    Code version:  edited 15 February 2023
 *    Availability:  https://mtukerry.instructure.com/courses/1347/files/270562?module_item_id=84001
 *                   (Accessed 15/02/2023)
 *    Modified:  Code refactored (Features added, e.g. login and server messages)
 *****************************************************/

/*
 * A wrapper class of Socket which contains
 * methods for sending and receiving messages
 * original author: M. L. Liu
 */
public class SMPStreamSocket extends Socket {
   private Socket  socket;
   private BufferedReader input;
   private PrintWriter output;

   public SMPStreamSocket(InetAddress acceptorHost,
                          int acceptorPort) throws SocketException,
                                   IOException{
      socket = new Socket(acceptorHost, acceptorPort );
      setStreams( );

   }

   SMPStreamSocket(Socket socket)  throws IOException {
      this.socket = socket;
      setStreams( );
   }

   private void setStreams( ) throws IOException{
      // get an input stream for reading from the data socket
      InputStream inStream = socket.getInputStream();
      input = 
         new BufferedReader(new InputStreamReader(inStream));
      OutputStream outStream = socket.getOutputStream();
      // create a PrinterWriter object for character-mode output
      output = 
         new PrintWriter(new OutputStreamWriter(outStream));
   }

   public void sendLogin(String user, String pass)
           throws IOException {
      output.print(user + "\n");
      output.print(pass + "\n");

      //The ensuing flush method call is necessary for the data to
      // be written to the socket data stream before the
      // socket is closed.
      output.flush();
   } // end sendMessage

   // Username and Password to be sent to and received from Server
   public void sendUser(String user)
           throws IOException {
      output.print(user + "\n");
      //The ensuing flush method call is necessary for the data to
      // be written to the socket data stream before the
      // socket is closed.
      output.flush();
   } // end sendMessage

   public String receiveUser()
           throws IOException {
      // read a line from the data stream
      String username = input.readLine();
      return username;
   } //end receiveMessage

   public void sendPass(String pass)
           throws IOException {
      output.print(pass + "\n");
      //The ensuing flush method call is necessary for the data to
      // be written to the socket data stream before the
      // socket is closed.
      output.flush();
   } // end sendMessage

   public String receivePass()
           throws IOException {
      // read a line from the data stream
      String password = input.readLine();
      return password;
   } //end receiveMessage

   public boolean validateLogin(String userNameInput, String passwordInput)
           throws IOException {
      /*****************************************************
       *    Username / Password Validation from a Text File
       *    Title:    Lab1Frame.java, lines 59-44
       *    Author: 0wlbear
       *    Site owner/sponsor:  github.com
       *    Date: 25/01/2016
       *    Code version:  Jan 25, 2016, 5:40 PM GMT
       *    Availability:  https://gist.github.com/0wlbear/8e0d24abe8fdd7133c96 (Accessed 22/02/2023)
       *    Modified:  Code refactored (assigned a boolean, identifiers renamed)
       *****************************************************/
      // read a line from the users file
      File file = new File("src/server/users.txt");
      boolean vLogin = false;

      try {
         Scanner in = new Scanner(new File("src/server/users.txt"));
         while (in.hasNextLine())
         {
            String s = in.nextLine();
            String[] sArray = s.split(",");

            System.out.println(sArray[0]); //Just to verify that file is being read
            System.out.println(sArray[1]);

            // Assign boolean if login is valid or not
            if (userNameInput.equals(sArray[0]) && passwordInput.equals(sArray[1]))
            {
               vLogin = true;
               return vLogin;
            }
            else
            {
               vLogin = false;
            }
         }

         in.close();

      } catch (FileNotFoundException e) {
         JOptionPane.showMessageDialog(null,
                 "User Database Not Found", "Error",
                 JOptionPane.ERROR_MESSAGE);
      }

      return vLogin;
   } //end validateLogin
   // end of refactored code

   // Send and Receive message using echo
   public void sendMessage(String message)
   		          throws IOException {	
      output.print(message + "\n");   
      //The ensuing flush method call is necessary for the data to
      // be written to the socket data stream before the
      // socket is closed.
      output.flush();               
   } // end sendMessage

   public String receiveMessage( )
		throws IOException {	
      // read a line from the data stream
      String message = input.readLine( );  
      return message;
   } //end receiveMessage

   // Client receives messages stored by the server
   public String[] receiveMessages()
                  throws IOException {
      String[] messages = new String[]{"Hello there!", "Welcome to the Secure Messaging Protocol", "How are you?"};
      return messages;
   }  // end receiveMessages

   public void close( )
		throws IOException {	
      socket.close( );
   }
} //end class
