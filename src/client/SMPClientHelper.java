/**
 * @author Neil Sebbey
 * Final Year Computing - Software Development
 * Distributed Computing
 * CA: Secure Messaging Protocol
 */

package client;

import server.SMPStreamSocket;

import java.net.*;
import java.io.*;

/*
 * This class is a module which provides the application logic
 * for a Secure Messaging Protocol client using stream-mode socket.
 * original author: M. L. Liu
 */

public class SMPClientHelper {

   static final String endMessage = ".";
   private SMPStreamSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   SMPClientHelper(String hostName,
                   String portNum) throws SocketException,
                     UnknownHostException, IOException {
                                     
  	   this.serverHost = InetAddress.getByName(hostName);
  		this.serverPort = Integer.parseInt(portNum);
      //Instantiates a stream-mode socket and wait for a connection.
   	this.mySocket = new SMPStreamSocket(this.serverHost,
         this.serverPort); 
/**/  System.out.println("Connection request made");
   } // end constructor

   public boolean getValidLogin(String user, String pass) throws SocketException,
           IOException{
      String username = user;
      String password = pass;
      boolean vLogin = false;
      mySocket.sendLogin(username, password);
      // now validate login
      vLogin = mySocket.validateLogin(user, pass);
      return vLogin;
   } // end getValidLogin

   // Get Username and Password from server
   public String getUser(String user) throws SocketException,
           IOException{
      String username = "";
      mySocket.sendUser(user);
      // now receive the echo of username
      username = mySocket.receiveUser();
      return username;
   } // end getUser

   public String getPass(String pass) throws SocketException,
           IOException{
      String password = "";
      mySocket.sendPass(pass);
      // now receive the echo of password
      password = mySocket.receivePass();
      return password;
   } // end getPass
	
   public String getEcho( String message) throws SocketException,
      IOException{     
      String echo = "";    
      mySocket.sendMessage( message);
	   // now receive the echo of message
      echo = mySocket.receiveMessage();
      return echo;
   } // end getEcho

   public String[] getAllMessages(String message) throws SocketException,
            IOException{
      String[] echoAll;
      mySocket.sendMessage( message);
      // now receive the echo of messages stored on the server
      echoAll = mySocket.receiveMessages();
      return echoAll;
   } // end getEcho

   public void done( ) throws SocketException,
                              IOException{
      mySocket.sendMessage(endMessage);
      mySocket.close( );
   } // end done 
} //end class
