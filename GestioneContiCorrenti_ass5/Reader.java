//PROVARE A FARE LA VERSIONE IN CUI LA LETTURA AVVIENE PIANO PIANO E PARSO UN OGGETTO PER VOLTA E NON L'INTERA STRINGA TUTTA INSIEME
//google library gson -> JsonReader e le sue funzioni
//sottoclasse di Reader, NioFileReader


import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ThreadPoolExecutor;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 * Thread Reader.
 * Reads from the .json file, parses the JSONString, isolate every bank account from the others and passes it to the thread pool.
 *
 * @author Maria Vitali, 548154
 */
public class Reader extends Thread {
    private ThreadPoolExecutor handlers;
    private File file;
    JSONParser parser;
    Counters count;

    public Reader(ThreadPoolExecutor handlers, Counters count, File file){
        this.handlers = handlers;
        this.file = file;
        this.parser = new JSONParser();
        this.count = count;
    }

    @Override
    public void run(){
        System.out.println("Reading from file...");

        //read the file through nio ops
        boolean stop = false;
        FileChannel inChannel;
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024);
        String s = "";
        try {

            inChannel = FileChannel.open(Paths.get(file.getAbsolutePath()), StandardOpenOption.READ);
            while (!stop) {
                int bytesRead = inChannel.read(buffer);

                if (bytesRead == -1) {
                    stop = true;
                }
                buffer.flip();      //flip to read mode

                while(buffer.hasRemaining()){
                    s = StandardCharsets.UTF_8.decode(buffer).toString();
                }

                /*clear buffer*/
                buffer.clear();      //flip back to write mode
            }

            /*parse string into JSONArray*/
            Object obj = parser.parse(s);
            JSONArray bankAccounts = (JSONArray) obj;

            /*every account is passed to the thread pool to perform the counting operations*/
            System.out.println("\nStarting counting operations...\n");
            for (Object account: bankAccounts) {
                ClientHandler h = new ClientHandler((JSONObject)account, count);
                handlers.execute(h);
            }

            inChannel.close();
        }catch (Exception e){
            e.getStackTrace();
        }

        System.out.println("File read!\n");
    }

}