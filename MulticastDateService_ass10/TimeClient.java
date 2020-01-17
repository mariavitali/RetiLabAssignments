import java.io.IOException;
import java.net.*;

/*
* client che si unisce a dategroup (IP del gruppo sul quale TimeServer pubblica data e ora a intervalli regolari)
* e riceve, per dieci volte consecutive, data ed ora, le visualizza, quindi termina.
* */

public class TimeClient {
    /*
    * Usage: java TimeClient dategroup
    *       dategroup (String) indirizzo IP di multicast dove il TimeServer manda data e ora a intervalli regolari
    *
    * */

    public static void main(String[] args) {
        InetAddress group = null;
        int port = 6789;
        int max_timeout = 60000;        //timeout della receive (1 minuto)

        if(args.length != 1){
            System.out.println("Usage: java TimeClient dategroup_IP_Address (same as TimeServer)");
            System.exit(1);
        }

        try {
            group = InetAddress.getByName(args[0]);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }

        if(!group.isMulticastAddress()){
            System.out.println(group.getHostAddress() + " is not a multicast address. Try an IP address between 224.0.0.0 and 239.255.255.255.");
            System.exit(1);
        }

        MulticastSocket multicast = null;
        try{
            /*crea un socket per il multicast sulla porta scelta e setta il timeout per la receive su quel socket*/
            multicast = new MulticastSocket(port);
            multicast.setSoTimeout(max_timeout);

            /*si unisce al gruppo di multicast*/
            multicast.joinGroup(group);

            byte[] received = new byte[256];
            for(int i = 0; i < 10; i++){
                try{
                    /*crea un pacchetto per ricevere i dati*/
                    DatagramPacket dp = new DatagramPacket(received, received.length);
                    multicast.receive(dp);

                    /*stampa ciò che ha ricevuto*/
                    String time = new String(dp.getData());
                    System.out.println(time);

                }
                catch(SocketTimeoutException ste){
                    ste.printStackTrace();
                    multicast.leaveGroup(group);
                    multicast.close();
                    System.exit(1);
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
            /*lascia il gruppo multicast e chiude il socket*/
            multicast.leaveGroup(group);
            multicast.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
