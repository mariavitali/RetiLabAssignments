import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainClass {
    /*HTTP server listening on port 8080
    * Accept requests from any Web browser at URL localhost:8080.
    *
    * Start connection by querying localhost:8080, that will show a list of all possible file GET requests and instructions to proceed
    *
    * GITHUB UPDATE TEST
    */


    public final static int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Listening for connection on port 8080 ....");
            while(true){
                Socket clientSocket = serverSocket.accept();
                /*thread - Request Handler che poi richiama anche Response Handler
                * intanto il server continua con l'accettazione delle richieste*/
                InputStreamReader isr =  new InputStreamReader(clientSocket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                while (!line.isEmpty()) {
                    System.out.println(line);
                    line = reader.readLine();
                }
                ResponseGenerator handler = new ResponseGenerator();
                String httpResponse = handler.printMenu();
                clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                System.out.println("Response sent!");

                clientSocket.close();
            }
        }
        catch(Exception e){
            e.getStackTrace();
        }
    }
}
