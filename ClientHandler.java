import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ClientHandler implements Runnable {
    // Loops through this list to send the message to all clients
    // Static to bind this to the class and not the instance.
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static Random rnd = new Random();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int id;
    public String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;

            // Character streams end with 'Writer'
            // Byte streams end with 'Stream'
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.id = rnd.nextInt();
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        }
        catch (IOException e){
            closeEverything(bufferedReader, bufferedWriter, socket);
        }
    }

    public void closeEverything(BufferedReader bufferedReader, BufferedWriter bufferedWriter, Socket socket) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if(clientHandler.id != id){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch (IOException e){
                closeEverything(bufferedReader, bufferedWriter, socket);
            }
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }
            catch (IOException e) {
                closeEverything(bufferedReader, bufferedWriter, socket);
                break;
            }
        }
    }
}
