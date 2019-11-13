
import java.net.ServerSocket;
import java.net.Socket;

public class MainClass {
    /*HTTP server listening on port 8080
     * Accept requests from any Web browser at URL localhost:8080.
     *
     * Start connection by querying localhost:8080, that will show a list of all retrievable files and instructions to proceed
     *
     */


    public final static int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Listening for connection on port 8080 ....");
            while(true){
                try(Socket clientSocket = serverSocket.accept()) {

                    RequestHandler handler = new RequestHandler(clientSocket);
                    handler.readRequest();

                    //clientSocket.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }
    }
}