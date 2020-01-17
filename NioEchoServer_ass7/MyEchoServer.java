import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/* server che accetta connessioni all'indirizzo ("localhost", 7777)
* Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client
* e li rispedisce aggiungendo "echoed by server" al messaggio ricevuto*/

public class MyEchoServer {
    public static int PORT = 7777;

    public static void main(String[] args) {
        ServerSocketChannel serverChannel;
        Selector selector;

        try{
            /*apro un ServerSocketChannel e gli assegno l'indirizzo */
            serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(PORT);        //I could also use InetSocketAddress(127.0.0.1, PORT), localhost
            serverSocket.bind(address);
            serverChannel.configureBlocking(false);
            /*apro un selettore*/
            selector = Selector.open();
            /*registro il canale sul selettore*/
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Listening on port 7777 ...\n");
        }
        catch(IOException e){
            e.printStackTrace();
            return;
        }

        while(true){
            System.out.println("\nSELECT\n");
            try{
                selector.select();
                System.out.println("SELECTED");
            }
            catch(IOException e){
                e.printStackTrace();
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        try {
                            /*accetto la richiesta di connessione del client e setto il SocketChannel in modalità non bloccante*/
                            SocketChannel client = server.accept();
                            System.out.println("Accepted connection from " + client);
                            client.configureBlocking(false);

                            /*registro il client sul selettore*/
                            SelectionKey key2 = client.register(selector, SelectionKey.OP_READ);
                            key2.attach("");
                        } catch (ClosedChannelException ex) {
                            ex.printStackTrace();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                    /*se entra qui significa che la connessione è già stata accettata*/
                    else if (key.isReadable()) {            /*lettura pronta*/
                        try {

                            SocketChannel client = (SocketChannel) key.channel();
                            client.configureBlocking(false);

                            System.out.println("I have something to read from client: " + client);
                            ByteBuffer input = ByteBuffer.allocate(1024);
                            String message = "";


                            int n = client.read(input);

                            input.flip();      //flip to read mode

                            if (input.hasRemaining()) {
                                message = message + StandardCharsets.UTF_8.decode(input).toString();
                            }

                            message = (String) key.attachment() + message;

                            System.out.println("So far from client" + client + " I've read: " + message);
                            key.attach(message);

                            /*ha sicuramente finito di leggere*/
                            if(n < input.capacity()) {
                                SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
                                System.out.println("Current interest Ops: " + key2.interestOps());
                                key2.attach(message);
                            }

                            input.clear();      //flip back to write mode
                        }
                        catch(ClosedChannelException cce) {
                            cce.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (key.isWritable()) {      /*scrittura pronta*/
                        try {
                            SocketChannel client = (SocketChannel) key.channel();
                            client.configureBlocking(false);
                            String echo = (String) key.attachment();

                            if (echo != null) {
                                echo = echo + " <echoed by server>";

                                System.out.println("Sending to the client " + client + " the message: " + echo);

                                //da gestire meglio la scrittura "a pezzi"
                                ByteBuffer output = ByteBuffer.allocate(1024);
                                output.put(echo.getBytes());

                                output.flip();

                                if (output.hasRemaining()) {
                                    try {
                                        client.write(output);
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    }
                                }

                                output.clear();

                                key.cancel();
                            }
                        } catch(IOException e){
                            e.printStackTrace();
                        }

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    key.cancel();
                    try{
                        key.channel().close();
                    }catch(IOException cex){
                        cex.printStackTrace();
                    }
                }

            }
        }
    }
}
