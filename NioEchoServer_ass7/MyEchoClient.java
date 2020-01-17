import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/* client - si connette al server, legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto dal server */

public class MyEchoClient {
    public static int PORT = 7777;
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        String input;

        System.out.println("\nType in the string you want the server to echo\n");
        System.out.println("------------------------------------------------\n");

        try {
            /*client is in blocking mode*/
            SocketAddress address = new InetSocketAddress("localhost", PORT);
            SocketChannel client = SocketChannel.open(address);



            input = keyboard.nextLine();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(input.getBytes());
            buffer.flip();
            try {
                client.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.clear();

            ByteBuffer echoed = ByteBuffer.allocate(1024);
            try {
                client.read(echoed);
            } catch (IOException e) {
                e.printStackTrace();
            }
            echoed.flip();
            String received = "";
            while (echoed.hasRemaining()) {
                received = received + StandardCharsets.UTF_8.decode(echoed).toString();
            }
            System.out.println("\n" + received);
            System.out.println("\n\n");
            echoed.clear();
            System.out.println("\n\n");

    }catch(Exception e){
            e.printStackTrace();
        }
    }
}
