import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler {    //extends Thread (è possibile adattare il codice a una soluzione multithread)
    private Socket client;
    private String directoryPath;

    //gli passo il client socket e lui gestisce quella connessione specifica
    public RequestHandler(Socket client){
        this.client = client;
        directoryPath = "./files";
    }

    //legge la richiesta del client e la gestisce
    public void readRequest(){
        try {
            InputStreamReader in = new InputStreamReader(client.getInputStream());
            BufferedReader reader = new BufferedReader(in);

            //leggo la prima riga della richiesta
            String line = reader.readLine();
            if(line != null) {
                System.out.println(line);

            /*
                se dovessimo aver bisogno di leggere l'intera richiesta

                String request = line;
                while (!line.isEmpty()) {
                    line = reader.readLine();
                    request = request + line;
                }
                System.out.println(request);
            }*/

                //parser della prima riga
                String requestedFile = parserHTTP(line);
                ResponseGenerator rg = new ResponseGenerator();
                String response;
                OutputStream out = client.getOutputStream();

                //se non sto richiedendo nessun file specifico, restituisco il file di testo con le istruzioni di utilizzo del servizio e la lista di file visualizzabili
                if (requestedFile.equals("/")) {
                    requestedFile = "/instructions.html";
                }

                System.out.println("Client requested: " + directoryPath + requestedFile);
                File file = new File(directoryPath + requestedFile);
                if (file.exists()) {
                    String contentType = Files.probeContentType(file.toPath());
                    System.out.println("The content type of the requested file is: " + contentType);
                    response = rg.positiveResponse(contentType);
                    out.write(response.getBytes());
                    Files.copy(file.toPath(), out);

                } else {
                    response = rg.negativeResponse();
                    out.write(response.getBytes());
                    response = "<h1>404 File not found!</h1>\n";
                    out.write(response.getBytes());
                }

                System.out.println("Response sent!");
                in.close();
                out.close();
            }
        }
        catch (IOException e){
            e.getStackTrace();
        }
    }


    //parser di richieste HTTP
    //restituisce una stringa con il nome del file richiesto
    public String parserHTTP(String request){
        String[] s = request.split(" ");
        return s[1];
    }

}
