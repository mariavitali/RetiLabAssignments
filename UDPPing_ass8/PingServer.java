import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PingServer {
    /*IL PING SERVER rimanda al mittente qualsiasi dato riceve.
      Accetta un argomento da linea di comando: la porta, che è quella su cui è attivo il server
      + un argomento opzionale, il seed, un valore long utilizzato per la generazione di latenze e perdita di pacchetti.
      Se uno qualunque degli argomenti è scorretto, stampa un messaggio di errore del tipo ERR -arg x, dove x è il numero dell'argomento.

      Dopo aver ricevuto un PING, il server determina se ignorare il pacchetto (simulandone la perdita) o effettuarne l'eco.
      La probabilità di perdita di pacchetti è del 25%.
      Se decide di effettuare l'eco del PING, il server attende un intervallo di tempo casuale per simulare la latenza di rete
      Stampa l'indirizzo IP e la porta del client, il messaggio di PING e l'azione intrapresa dal server
      in seguito alla sua ricezione (PING non inviato,oppure PING ritardato di x ms).*/


    /* PingServer port [seed]
    *       port (int) è il numero della porta pubblica del server tramite cui può essere contattato dai client
    *       seed (long) argomento opzionale per la generazione di numeri random che simulano la perdita di pacchetti e la latenza della rete
    */

    static final long DEFAULT_SEED = 132;
    static int BUF_SIZE = 1024;

    public static void main(String[] args) {
        /*controlli iniziali sugli argomenti passati al server*/
        if(args.length < 1 || args.length > 2){
            System.out.println("Usage: java PingServer port [seed]");
            System.exit(1);
        }

        int port = 0;
        long seed = 0;

        /*controllo sulla porta*/
        try {
            port = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            System.out.println("ERR -arg 1");
            System.exit(1);
        }

        if(port < 1024 || port > 65535){
            System.out.println("ERR -arg 1\nPort not valid.");
            System.exit(1);
        }

        /*inizializzazione di seed*/
        if(args.length == 2){
            try {
                seed = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
                System.out.println("ERR -arg 2");
                System.exit(1);
            }

        }else{
            seed = DEFAULT_SEED;
        }


        try {
            /*creazione DatagramSocket e DatagramPacket per ricezione di messaggi*/
            Random rand = new Random(seed);
            DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("Listening on port " + port);
            byte[] received = new byte[BUF_SIZE];
            DatagramPacket receivedPacket = new DatagramPacket(received, received.length);
            while(true){
                try {
                    serverSocket.receive(receivedPacket);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
                try {
                    String byteToString = new String(receivedPacket.getData(), 0, receivedPacket.getLength(), StandardCharsets.US_ASCII);
                    int number = Math.abs(rand.nextInt(100));
                    System.out.println(number);
                    if(number < 25){
                        //ignore message
                        System.out.println(receivedPacket.getAddress().getHostAddress() + ":" + receivedPacket.getPort() + "> " + byteToString + " ACTION : not sent");
                    }
                    else{
                        //simula il ritardo della rete e invia il messaggio al client
                        int delay = Math.abs(rand.nextInt(300));
                        try {
                            Thread.sleep(delay);
                        }catch(InterruptedException ie){
                            ie.printStackTrace();
                        }
                        //creazione packet per invio di messaggi al client
                        byte[] toSend = byteToString.getBytes(StandardCharsets.US_ASCII);
                        DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, receivedPacket.getAddress(), receivedPacket.getPort());

                        try {
                            serverSocket.send(sendPacket);
                        }catch(IOException ioe){
                            ioe.printStackTrace();
                        }

                        System.out.println(receivedPacket.getAddress().getHostAddress() + ":" + receivedPacket.getPort() + "> " + byteToString + " ACTION : delayed " + delay + " ms");


                    }
                }catch(Exception uee){
                    uee.printStackTrace();
                }

            }
        }catch(SocketException e){
            e.printStackTrace();
        }




    }
}
