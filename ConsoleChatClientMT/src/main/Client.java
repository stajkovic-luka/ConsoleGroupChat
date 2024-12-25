package main;

import java.net.Socket;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author lukas
 */
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    
    public Client(Socket socket, String username) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch(IOException ex){
            closeAll(socket, bufferedWriter, bufferedReader);
        }
    }
    
    // Salje poruku serverskom soketu.
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine(); // napomena: BufferedWriter ne ukljucuje sam \n karakter, mora rucno
            bufferedWriter.flush(); // napomena2: Isto vazi i za flush
            
            
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                
            }
            
        }catch(IOException ex){
            closeAll(socket, bufferedWriter, bufferedReader);
        }
        
    }
    
    // Gasi sve aktivne konekcije i tokove
    public void closeAll(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try{
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    // Kreiramo nit da se izbegne cekanje prilikom primanja poruke.
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                
                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch(IOException ex){
                        closeAll(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
    }
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name for your group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }

}


