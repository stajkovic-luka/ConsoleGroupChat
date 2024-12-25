/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lukas
 */
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        while(!serverSocket.isClosed()){
            try{
            Socket socket = serverSocket.accept();
            System.out.println("A new client has connected!");
            
            //Klasa ClientHandler sadrzi logiku rukovanja nitima/klijentima
            ClientHandler clientHandler = new ClientHandler(socket);
            
            Thread thread = new Thread(clientHandler);
            thread.start();
            
            
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
            
            
        }
        
    }
    public void closeServerSocket(){
        try{
            if (serverSocket!=null) {
                serverSocket.close();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
        
    
}
