/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package client;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/*
 * This module contains the presentation logic of a
 * Secure Messaging Protocol Client.
 * original author: M. L. Liu
 */
public class SMPClient extends JFrame {
   static final String endMessage = ".";
   static final String endMessages = ",";
   public static void main(String[] args) {
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      try {
         String hostName = JOptionPane.showInputDialog("Welcome to the SMP client.\n" +
                  "What is the name of the server host?");
         // String hostName = br.readLine();
         if (hostName.length() == 0) // if user did not enter a name
            hostName = "localhost";  //   use the default host name
         String portNum = JOptionPane.showInputDialog("What is the port number of the server host?");
         // String portNum = br.readLine();
         if (portNum.length() == 0)
            portNum = "8888";          // default port number
         SMPClientHelper helper =
            new SMPClientHelper(hostName, portNum);

         boolean done = false;
         boolean vLogin;
         boolean loggedIn = false;
         String message, echo, uName, pWord;


         while (!done) {
            /*****************************************************
             *    Username / Password Input using JOptionPane
             *    Title:    JOptionPane with username and password input
             *    Author: 0wlbear
             *    Site owner/sponsor:  stackoverflow.com
             *    Date: 23/08/2013
             *    Code version:  Aug 23, 2013 at 5:46
             *    Availability:  https://stackoverflow.com/questions/18395615/joptionpane-with-username-and-password-input
             *                   (Accessed 27/02/2023)
             *    Modified:  Code refactored (identifiers renamed, String used instead of Hashtable)
             *****************************************************/
            // Login Form creation
            JPanel panel = new JPanel(new BorderLayout(5, 5));

            JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
            label.add(new JLabel("Username", SwingConstants.RIGHT));
            label.add(new JLabel("Password", SwingConstants.RIGHT));
            panel.add(label, BorderLayout.WEST);

            JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
            JTextField username = new JTextField();
            controls.add(username);
            JPasswordField password = new JPasswordField();
            controls.add(password);
            panel.add(controls, BorderLayout.CENTER);

            int loginOpt = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"Login", "Exit"},"default");
            // end of Login Form creation

            if(loginOpt == JOptionPane.OK_OPTION)
            {
               // Convert Login form input to String for the server
               String userStr = username.getText();
               String passStr = new String(password.getPassword());

               // Verify login
               vLogin = helper.getValidLogin(userStr, passStr);
               if (vLogin) {
                  // ** Send login details to the server **
                  // Please note: the users text file ONLY contains password examples
                  // for the purpose of this assignment.
                  helper.getUser(userStr);
                  helper.getPass(passStr);
                  JOptionPane.showMessageDialog(null,"You are now logged in", "OK 200: Logged in", JOptionPane.INFORMATION_MESSAGE);
                  loggedIn = true;
                  while (loggedIn) {
                     message = JOptionPane.showInputDialog("Enter a line to receive an echo "
                             + "from the server, a comma (,) to read all messages from the server, " + "\n" +
                             " or a single period (.) to exit the application.");
                     // message = br.readLine( );
                     if ((message.trim()).equals (endMessage)){
                        loggedIn = false;
                        done = true;
                        helper.done( );
                     }
                     else if ((message.trim()).equals (endMessages)){
                        String[] echoAll = helper.getAllMessages(message);
                        JOptionPane.showMessageDialog(null,echoAll,"OK 202: Messages from the server", JOptionPane.INFORMATION_MESSAGE);
                     }
                     else {
                        echo = helper.getEcho( message);
                        JOptionPane.showMessageDialog(null, echo, "OK 201: Message", JOptionPane.INFORMATION_MESSAGE);
                     }
                  }
               }
               else {
                  vLogin = false;
                  JOptionPane.showMessageDialog(null,"Error: The credentials entered are incorrect. Please try again.",
                          "Error 401: Not logged in", JOptionPane.INFORMATION_MESSAGE);
               }
            }
            else {
               // Exit program when the Exit button is clicked.
               done = true;
               helper.done( );
            }


          } // end while
      } // end try  
      catch (Exception ex) {
         ex.printStackTrace( );
      } //end catch
   } //end main

   private static void printSocketInfo(SSLSocket s) {
      System.out.println("Socket class: "+s.getClass());
      System.out.println("   Remote address = "
              +s.getInetAddress().toString());
      System.out.println("   Remote port = "+s.getPort());
      System.out.println("   Local socket address = "
              +s.getLocalSocketAddress().toString());
      System.out.println("   Local address = "
              +s.getLocalAddress().toString());
      System.out.println("   Local port = "+s.getLocalPort());
      System.out.println("   Need client authentication = "
              +s.getNeedClientAuth());
      SSLSession ss = s.getSession();
      System.out.println("   Cipher suite = "+ss.getCipherSuite());
      System.out.println("   Protocol = "+ss.getProtocol());
   }

} // end class
