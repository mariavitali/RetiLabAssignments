import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CongressInterface extends Remote {
    String SERVICE_NAME = "CongressService";

    boolean registerSpeaker(String name, Integer day, Integer session) throws RemoteException, FullSessionException;

    Integer getnDays() throws RemoteException;

    Integer getnSessions() throws RemoteException;

    ArrayList<ArrayList<ArrayList<String>>> getCongressInfo() throws RemoteException;

}
