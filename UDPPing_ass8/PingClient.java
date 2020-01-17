import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;


public class PingClient {
    /*Il PING CLIENT accetta due argomenti da linea di comando: nome e porta del server.

      Se uno o più argomenti risultano scorretti, il client termina, dopo aver stampato un messaggio di errore del tipo ERR -arg x, dove x è il numero dell'argomento.
      Utilizza una comunicazione UDP per comunicare con il server ed invia 10 messaggi al server, con il seguente formato:
            PING seqno timestamp
      in cui seqno è il numero di sequenza del PING (tra 0-9) ed il timestamp (in millisecondi) indica quando il messaggio è stato inviato.
      Non invia un nuovo PING fino che non ha ricevuto l'eco del PING precedente, oppure è scaduto un timeout.
      Stampa ogni messaggio spedito al server ed il RTT del ping oppure un * se la risposta non è stata ricevuta entro 2 secondi.
      Dopo che ha ricevuto la decima risposta (o dopo il suo timeout), il client stampa alcuni dati e statistiche sulla connessione appena terminata.
    */


    /* PingClient hostname port
    *       hostname (String) hostname del Server (nel nostro caso sarà per forza "localhost")
    *       port     (int) numero della porta del Server (DEVE ESSERE UGUALE A QUELLA PASSATA COME ARGOMENTO AL NOSTRO PingServer)
    */

    static final int BUF_SIZE = 1024;

    public static void main(String[] args) {
        /*controlli iniziali sugli argomenti passati al server*/
        if (args.length != 2) {
            System.out.println("Usage: java PingClient hostname port");
            System.exit(1);
        }

        String hostname = args[0];

        /*controllo che hostname sia il localhost, dato che per questo esercizio il servizio si trova su localhost*/
        if(!hostname.equals("localhost")){
            System.out.println("ERR -arg 1");
            System.out.println("Cause: in this specific exercise the service is only available on 'localhost'");
            System.exit(1);
        }

        /*controlli sulla porta*/
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("ERR -arg 2");
            System.exit(1);
        }

        try {
            /*creazione DatagramSocket del client e DatagramPacket per ricezione di dati*/
            InetAddress address = InetAddress.getByName(hostname);

            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);

            byte[] received = new byte[BUF_SIZE];
            DatagramPacket receivedPacket = new DatagramPacket(received, received.length);

            long[] rtt = new long[10];

            for (int i = 0; i < 10; i++) {
                /*creazione DatagramPacket per invio di dati*/
                DatagramPacket sendPacket;
                Timestamp sendTime = new Timestamp(System.currentTimeMillis());
                String pingMessage = "PING " + i + " " + sendTime.getTime();

                byte[] toSend = pingMessage.getBytes();

                sendPacket = new DatagramPacket(toSend, toSend.length, address, port);
                socket.send(sendPacket);

                try {
                    socket.receive(receivedPacket);
                    String byteToString = new String(receivedPacket.getData(), 0, receivedPacket.getLength(), StandardCharsets.US_ASCII);
                    Timestamp receiveTime = new Timestamp(System.currentTimeMillis());
                    rtt[i] = (receiveTime.getTime() - sendTime.getTime());
                    System.out.println(byteToString + " RTT: " + rtt[i] + " ms");
                } catch (SocketTimeoutException e) {
                    System.out.println(pingMessage + " RTT: *");
                    rtt[i] = -1;
                }

            }

            printStatistics(rtt);


        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (PortUnreachableException pue) {
            pue.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /*stampa alcuni dati raccolti durante la connessione al PingServer*/
    private static void printStatistics(long[] rtt) {
        int countReceived = 0;
        long sum = 0;
        long min = rtt[0];
        long max = rtt[0];
        for(int i = 0; i < rtt.length; i++){
            if(rtt[i] > 0){
                countReceived++;
                sum = sum + rtt[i];
                if(rtt[i] < min) min = rtt[i];
                if(rtt[i] > max) max = rtt[i];
            }
        }

        System.out.println("-------------------PING Statistics---------------------");
        System.out.println("Packets transmitted: " + rtt.length);
        System.out.println("Packets received: " + countReceived);
        int percentage = (int)Math.floor((double)((rtt.length - countReceived) * 100)/10);
        System.out.println(percentage + "% packet loss");
        double avg = (double)sum/countReceived;
        System.out.printf("round-trip (ms) min/avg/max = %d/%.2f/%d\n\n", min, avg, max);
    }
}
