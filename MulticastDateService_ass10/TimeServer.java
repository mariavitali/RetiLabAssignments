import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;

/* Server che invia su un gruppo di multicast dategroup, ad intervalli regolari, la data e l’ora.
* L'intervallo è un numero di secondi random (tra 0 e 10) calcolato appena il server va in esecuzione.
*
* L’indirizzo IP di dategroup viene introdotto da linea di comando.
* */

public class TimeServer {
    /*
    * Usage: java TimeServer dategroup
    *       dategroup (string) è un indirizzo IP compreso tra 224.0.0.0 e 239.255.255.255
    *
    * */

    public static void main(String[] args) {
        String IP;
        int PORT = 6789;

        long ran = (long) (Math.random() * 10000);

        /*controlla gli argomenti passati al main*/
        if(args.length != 1){
            System.out.println("Usage : java TimeServer dategroup_IP_Address");
            System.exit(1);
        }


        try{
            IP = args[0];
            InetAddress dategroup = InetAddress.getByName(IP);

            /*controlla che l'IP passato sia un valido indirizzo di multicast*/
            if(!dategroup.isMulticastAddress()){
                System.out.println(IP + " is not a multicast address. Try an IP address between 224.0.0.0 and 239.255.255.255.");
                System.exit(1);
            }

            while(true) {
                try {
                    String time = getDateAndTime();
                    byte[] toSend = time.getBytes();

                    /*crea il pacchetto da inviare nel gruppo multicast e il socket su una porta anonima (per invio pacchetti)*/
                    DatagramPacket dp = new DatagramPacket(toSend, toSend.length, dategroup, PORT);
                    DatagramSocket ms = new DatagramSocket();

                    /*invia il pacchetto*/
                    ms.send(dp);
                    System.out.println("Packet sent. (" + time + ")");

                    /*aspetta ran secondi prima di inviare il pacchetto successivo*/
                    try {
                        Thread.sleep(ran);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*calcola la data attuale*/
    private static String getDateAndTime(){
        Timestamp date = new Timestamp(System.currentTimeMillis());
        return date.toString();
    }
}
