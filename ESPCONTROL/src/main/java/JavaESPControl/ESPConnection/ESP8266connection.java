/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.ESPConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import lombok.Getter;

/**
 *
 * @author misustefan
 */
@Getter
public class ESP8266connection{
    
    private static Socket socket;
    private InetAddress ESPIpV4;
    // define the port on which you want to connect
    // it can be anything you want as long as it is the same
    // on the ESP board ( go see the .ino code)
    private final int port = 5055; 

    
    public ESP8266connection() throws IOException {
        // the address of your local WI-FI from the .ino file AFTER CONNECTION
        
    }
    
    
    public Socket getConnection() throws IOException{
        if (socket != null && socket.isConnected()) {
            System.out.println("Already connected to the ESP");
            return socket;
        }
        
        int maxTries = 3;
        int count = 0;
        boolean connFlag = true;
        System.out.println("Connecting...Please wait...Thank you!");
        
        while(connFlag)
        {
            try 
            {
                socket = new Socket(ESPIpV4, port);
                if(socket != null && socket.isConnected()){
                    System.out.println("Connected to the ESP");
                    System.out.println("ESP-IPV4-Address -> " + socket.getInetAddress());
                    System.out.println("ESP-IPV4-Port -> " + socket.getPort());
                    return socket;
                }else{
                    throw new IOException("Something wrong happened when connecting..Retrying.."); 
                }
            }catch (IOException e) 
            {
                count++;
                e.printStackTrace();
                if(count == maxTries){
                    System.out.println("Too many retry's.Please refresh and try again!");
                    connFlag = false;
                }
            }
            
            
        }
        return null;
    }
    
    // receiving message from the Controller 
    public String receiveMessage(Socket connSocket) throws IOException{
        // inpputStreamReaDER
        if(socket.isConnected()){
            InputStreamReader inputReader 
                    = new InputStreamReader(connSocket.getInputStream());
            // bufferedReader for processing the input
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            // the message from the buffered reader
            String message = bufferedReader.readLine();
            System.out.println("Received from ESP8266");
            System.out.println(message);
            return message;
        }
        return null;
    }
    
    
    
    
    
}
