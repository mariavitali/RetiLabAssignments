import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/*
* bank database organization:
*   - external JSONArray
*   - in the array there are numClients JSONObjects. Each one represents a client account.
*   - each account JSONObject has:
*           > "ClientID" : (String) xxxxxxYY..
*                               where xxxxxx = "Client" and YY.. = is a unique Integer number
*           > "transactions" : (JSONArray)transactions
*   - each transactions JSONArray is an array of (JSONObject)transaction.
*   - each transaction JSONObject has:
*           > "data" : random date within the past 2 years
*           > "importo" : random integer between 1 and 1000
*           > "causale" : random string in the array {Bonifico, Accredito, Bollettino, F24, PagoBancomat}
*
* sample:
*   [
        {
            "ClientID": "Client1",
            "transactions": [
                {
                    "data": "YYYY-MM-DD",
                    "importo": xxxx,
                    "causale": "zzzzzzzzzz"
                },
                ...
            ]
        },
        ...
    ]
* */

public class BankAccountsCreator {
    private int numClients;         //number of clients
    private int numTransactions;        //number of transactions a client have in its account
    private JSONArray bankAccounts;


    public BankAccountsCreator(int numClients, int numTransactions){
        this.numClients = numClients;
        this.numTransactions = numTransactions;
        bankAccounts = new JSONArray();
    }


    public void createBankAccounts(String path){
        /* create external JSONArray, add client accounts */
        for(int i=0; i<numClients; i++){
            bankAccounts.add(createAccount("Client" + (i+1)));
        }
        //System.out.println(bankAccounts.toJSONString());

        /*create new JSON file*/
        File file = new File(path);
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileChannel outChannel = FileChannel.open(Paths.get("bankDB.json"), StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.wrap(bankAccounts.toJSONString().getBytes(StandardCharsets.UTF_8));
            while(buffer.hasRemaining()) {
                outChannel.write(buffer);
            }
            outChannel.close();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    /*create single client account*/
    public JSONObject createAccount(String clientID){
        JSONObject account = new JSONObject();
        JSONArray transactions = clientTransactions();
        account.put("ClientID", clientID);
        account.put("transactions", transactions);
        return account;
    }

    /*create transactions JSONArray, add numTransactions single transactions*/
    public JSONArray clientTransactions(){
        JSONArray transactions = new JSONArray();
        for(int i=0; i<numTransactions; i++){
            transactions.add(transaction());
        }
        return transactions;
    }

    /*create single transaction*/
    public JSONObject transaction(){
        JSONObject transaction = new JSONObject();

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear() - 2, 1, 1);
        long start = startDate.toEpochDay();

        LocalDate endDate = LocalDate.now();
        long end = endDate.toEpochDay();

        long randomDate = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();

        String[] causaliPossibili = new String[]{"Bonifico", "Accredito", "Bollettino", "F24", "PagoBancomat"};

        transaction.put("data", LocalDate.ofEpochDay(randomDate).toString());
        transaction.put("causale", causaliPossibili[(int)(Math.random()*10 % 5)]);
        transaction.put("importo", (int)(Math.random() * 1000 + 1));
        return transaction;
    }

}
