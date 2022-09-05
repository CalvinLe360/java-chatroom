import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server
 */
public class Server {

    ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while(!serverSocket.isClosed()){
                serverSocket.accept();
                System.out.println("A new client has connected!");

                ClientHandler clientHandler = new ClientHandler();

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch(IOException e){
            System.out.println("error message: " + e.getMessage());
        }
    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}