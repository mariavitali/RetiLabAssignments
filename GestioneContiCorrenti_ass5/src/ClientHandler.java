import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * examines and counts clientAccount's different transactions
 *
 * @author Maria Vitali, 548154
 */

public class ClientHandler extends Thread {
    private JSONObject clientAccount;
    private Counters count;

    /**
     * creates a new thread.
     * @param clientAccount (JSONObject) bank account of the client (with unique ClientID values)
     * @param count (Counters)
     */
    public ClientHandler(JSONObject clientAccount, Counters count){
        this.clientAccount = clientAccount;
        this.count = count;
    }

    public void run(){
        try {
            System.out.println("[" + Thread.currentThread().getName() + "]" + " takes " + clientAccount.get("ClientID"));
            JSONArray clientTransactions = (JSONArray)clientAccount.get("transactions");
            for(Object t:clientTransactions) {
                JSONObject transaction = (JSONObject)t;
                switch ((String)transaction.get("causale")) {
                    case "Bonifico":
                        count.incrementBonifico();
                        break;
                    case "Accredito":
                        count.incrementAccredito();
                        break;
                    case "Bollettino":
                        count.incrementBollettino();
                        break;
                    case "F24":
                        count.incrementF24();
                        break;
                    case "PagoBancomat":
                        count.incrementBancomat();
                        break;
                }
            }
        }catch(Exception e){
            e.getStackTrace();
        }
    }

}
