/**
 * Represents a person going to the PostOffice for an operation.
 * The actual real tasks might be several, here the only property pointed out is the time it takes to complete an operation.
 *
 */

public class Task implements Runnable {
    private String name;
    private long time;

    /**
     * Constructs the client.
     * Initializes the time the task will take with a random value.
     * Time will be in seconds and values between 0 and 10.
     *
     * @param name client id
     *
     */

    public Task(String name){
        this.name = name;
        this.time = (long)Math.floor(Math.random()*10000);
    }

    /**
     * execute task.
     */

    public void run(){
        try{
            System.out.printf("%s: Helping %s\n", Thread.currentThread().getName(),this.name);
            Thread.sleep(time);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Task completed. " + name + " exits Office.");
    }

    /**
     * @return name of the Task (client)
     */

    public String getName(){
        return name;
    }

}
