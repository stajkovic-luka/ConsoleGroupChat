package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author lukas
 */
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter; // moze i PrintWriter...
    private String clientUsername;
    
    
    
    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this); // dodaje client-e u grupu
            broadcastMessage("SERVER: " + clientUsername + "has joined the chat!");
            
            
        }catch(IOException ex){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }
    
    

    @Override
    public void run() {
        String msgFromClient;
        
        while(socket.isConnected()){
            try{
                msgFromClient = bufferedReader.readLine();
                broadcastMessage(msgFromClient);
            }catch(IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
            
        }
        
        
    }

    // Prikaz poruke svima u grupnom razgovoru
    // BITNO: Prikaz se vrsi svim klijentima, osim onom koji je poslao poruku!
    private void broadcastMessage(String messageToBroadcast) {
        for (ClientHandler clientHandler : clientHandlers) {
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToBroadcast);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler. bufferedWriter.flush();
                    
                }
            }catch(IOException ex){
                closeAll(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    
    // Zatvara se aktivne tokove, konekcije
    private void closeAll(Socket socket1, BufferedReader bufferedReader1, BufferedWriter bufferedWriter1) {

    }
    
}
