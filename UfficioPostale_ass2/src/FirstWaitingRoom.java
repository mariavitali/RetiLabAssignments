
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles the queue that simulates the first unbounded waiting room at the PostOffice.
 *
 * @author Maria Vitali, matricola 548154
 */

public class FirstWaitingRoom {
    //queue first waiting room
    private static final int MAX_CAPACITY = 500;
    private BlockingQueue<Task> firstRoom;

    /**
     * creates a new waiting room with max capacity MAX_CAPACITY people (to avoid overloading memory)
     */

    public FirstWaitingRoom(){
        firstRoom = new LinkedBlockingQueue<>(MAX_CAPACITY);
    }

    /**
     * a new client enters the first waiting room
     * @param newTask identifies the new client
     */

    public void addClient(Task newTask){
        try{
            firstRoom.put(newTask);
        }
        catch (InterruptedException e){
            e.getStackTrace();
        }
    }

    /**
     * next client's turn to enter the next room with limited capacity
     *
     * @return the client (seen as a task to solve)
     */

    public Task nextClient(){
        return firstRoom.poll();
    }

    public boolean isFull(){
        return (firstRoom.size() >= MAX_CAPACITY);
    }

    public boolean empty(){
        return firstRoom.isEmpty();
    }


}
