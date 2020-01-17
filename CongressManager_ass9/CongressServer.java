import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;

/*
* La classe implementa i metodi remoti, da esportare poi nel registry, che forniscono i servizi offerti dal software di gestione di un congresso.

* Il server mantiene i programmi delle giornate (ndays giornate) del congresso, ciascuno dei quali è memorizzato in una tabella
* in cui ad ogni riga corrisponde una sessione (nSession al giorno).
* Per ciascuna sessione vengono memorizzati i nomi degli speaker che si sono registrati (max nSlots)
*
*
* */

public class CongressServer extends RemoteServer implements CongressInterface {
    private ArrayList<ArrayList<ArrayList<String>>> congress;
    private Integer ndays;
    private Integer nSessions;
    private Integer nSlots;

    /*costruttore, creo il database*/
    public CongressServer(Integer ndays, Integer nSessions, Integer nSlots) {
        this.ndays = ndays;
        this.nSessions = nSessions;
        this.nSlots = nSlots;
        congress = new ArrayList<>(ndays);
        for(int i = 0; i < ndays; i++) {
            congress.add(i, new ArrayList<>(nSessions));
            for (int j = 0; j < nSessions; j++) {
                congress.get(i).add(j, new ArrayList<>(nSlots));
            }
        }
    }

    /*registrazione dello speaker a una sessione ---> MODIFIED synchronized*/
    @Override
    public synchronized boolean registerSpeaker(String name, Integer day, Integer session) throws RemoteException, FullSessionException {
        /*controllo se i parametri sono validi*/
        if(day >= ndays || day < 0) throw new IndexOutOfBoundsException("Invalid day. The number of days is: " + ndays);
        if(session >= nSessions || session < 0) throw new IndexOutOfBoundsException("Invalid session. The number of sessions per day is: " + nSessions);

        /*se la sessione richiesta nel giorno richiesto è piena, lancio un eccezione*/
        if(congress.get(day).get(session).size() >= nSlots) throw new FullSessionException("Oops! The session you chose is full.");

        /*se l'utente non è già registrato a quella sessione di quel giorno, viene aggiunto*/
        if(!congress.get(day).get(session).contains(name)) {
            congress.get(day).get(session).add(name);
            System.out.println("User " + name + " added to session " + (session+1) + ", day " + (day+1));
        }
        else System.out.println("User " + name + " had already signed up for session " + (session+1) + ", day " + (day+1));
        return true;
    }

    public Integer getnDays()throws RemoteException{
        return ndays;
    }

    public Integer getnSessions() throws RemoteException{
        return nSessions;
    }

    @Override
    public ArrayList<ArrayList<ArrayList<String>>> getCongressInfo() throws RemoteException {
        return congress;
    }
}
