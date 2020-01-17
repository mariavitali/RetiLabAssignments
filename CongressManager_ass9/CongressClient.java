import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/*
* Client che utilizza i servizi (metodi remoti) messi a disposizione dal server che gestisce le informazioni relative al congresso
*
* Il client può richiedere operazioni per:
*   - registrare uno speaker ad una sessione;
*   - ottenere il programma del congresso.
*
* Il client è implementato come un processo ciclico che continua a fare richieste sincrone fino ad esaurire tutte le esigenze utente.
*
*
* */

public class CongressClient{
    /*
    * !!! PRIMA DI ATTIVARE IL CLIENT, IL SERVER DEVE ESSERE ATTIVO !!!
    *
    * CongressClient name
    *   name (String) è il nome dell'utente che usufruisce del servizio
    *
    * Seguire poi le istruzioni mostrate del programma per testare il funzionamento.
    * NOTE E CONSIGLI PER I TEST:
    *       - eseguire più accessi consecutivi con username diversi
    *           per verificare che le info relative agli utenti precedenti siano state salvate
    *       - registrare 5 utenti alla stessa sessione dello stesso giorno e provare a registrare un sesto utente alla stessa sessione
    *           per verificare che il sistema notifica che la sessione è piena
    *       - far registrare lo stesso utente alla stessa sessione più volte
    *           per verificare che il sistema riconosce che l'utente si era già registrato e non registra lo stesso utente più volte, riempendo inutilmente la sessione
    *
    * */

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        int cmd;
        String name = null;
        switch(args.length){
            case 1:
                name = args[0];
                break;
            default:
                System.out.println("Usage: java CongressClient name");
                System.exit(1);
        }

        try {
            Registry reg = LocateRegistry.getRegistry(7000);
            CongressInterface congress = (CongressInterface) reg.lookup(CongressInterface.SERVICE_NAME);

            System.out.println("user: " + name);
            System.out.println("\nWELCOME TO THE AWESOME CONGRESS ! :)\n");
            System.out.println("type:");
            System.out.println("0 : exit the service");
            System.out.println("1 : register to a session");
            System.out.println("2 : get congress info\n");


            while((cmd = keyboard.nextInt()) != 0){
                switch(cmd){
                    case 1:
                        System.out.print("Choose congress day: 1, 2, 3\nMy choice: ");
                        int day = keyboard.nextInt();
                        System.out.print("Choose session number: int from 1 to 12\nMy choice: ");
                        int sess = keyboard.nextInt();
                        try{
                            congress.registerSpeaker(name, day-1, sess-1);
                        }catch(FullSessionException e){
                            System.out.println(e.getMessage());
                        }
                        catch(IndexOutOfBoundsException iob){
                            System.out.println(iob.getMessage());
                        }
                        catch(RemoteException e){
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        System.out.println("Here are the info you requested.\n");
                        System.out.println("-------------------AWESOME CONGRESS---------------------");
                        try {
                            ArrayList<ArrayList<ArrayList<String>>> info = congress.getCongressInfo();
                            System.out.println("The Awesome Congress lasts " + congress.getnDays() + " days");
                            System.out.println("There are " + congress.getnSessions() + " sessions per day");
                            System.out.println();
                            for(int i = 0; i < info.size(); i++){
                                System.out.println("DAY " + (i+1));
                                for(int j = 0; j < info.get(i).size(); j++){
                                    System.out.println("Session " + (j+1) + "\t-> " + info.get(i).get(j).toString());
                                }
                                System.out.println();
                            }

                        }catch(RemoteException e){
                            e.printStackTrace();
                        }
                        break;

                    default:
                        System.out.println("Invalid command. Try again.");
                }

                System.out.println("\ntype:");
                System.out.println("0 : exit the service");
                System.out.println("1 : register to a session");
                System.out.println("2 : get congress info\n");
            }
            System.out.println("\nbye!");
            System.exit(0);

        }catch(Exception e){
            e.printStackTrace();
        }

        keyboard.close();

    }
}
