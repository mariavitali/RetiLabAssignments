import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*attivazione dei servizi remoti, creazione del registry*/

public class ServiceActivation {
    static int registryPort = 7000;
    public static void main(String[] args) {
        Integer ndays = 3;
        Integer nSessions = 12;
        Integer nSlots = 5;
        CongressServer server = new CongressServer(ndays, nSessions, nSlots);
        try {
            try {
                /*esporto l'oggetto di tipo CongressServer e i suoi metodi remoti*/
                CongressInterface congress = (CongressInterface) UnicastRemoteObject.exportObject(server, 0);
                LocateRegistry.createRegistry(registryPort);
                Registry reg = LocateRegistry.getRegistry(registryPort);
                reg.rebind(CongressInterface.SERVICE_NAME, congress);
                System.out.println("Server ready");
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
