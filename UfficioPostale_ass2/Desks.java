import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Front desks at the PostOffice.
 *
 * Creates a thread pool simulating desks that handle clients.
 *
 * @author Maria Vitali, matricola 548154
 */

public class Desks {
    //thread pool
    private ThreadPoolExecutor desks;
    private static final int ROOM_SIZE = 10;

    /**
     * Constructor method.
     * Creates a new ThreadPool that simulates 4 help desks in the PostOffice.
     *
     * A desk (thread) closes after 60 seconds of inactivity.
     * An ArrayBlockingQueue simulates a fixed-sized waiting room in the PostOffice.
     */
    public Desks(){
        desks = new ThreadPoolExecutor(4,4,60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(ROOM_SIZE));
    }

    /**
     * Executes the task.
     *
     * @param task client runnable task
     */

    public void helpClient(Task task){
        desks.execute(task);
    }

    /**
     * check the status of the second waiting room (empty/full)
     *
     * @return true if the queue of the thread pool is full, false otherwise
     */
    public boolean secondWaitingRoomFull(){
        if(desks.getQueue().size() < ROOM_SIZE) return false;
        else return true;
    }

    public int peopleInTheSecondRoom(){
        return desks.getQueue().size();
    }

    /**
     * Terminates the ThreadPool and waits for its proper termination
     */

    public void closeDesks(){
        desks.shutdown();
        try{
            desks.awaitTermination(60L, TimeUnit.SECONDS);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
