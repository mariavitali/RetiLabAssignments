import java.util.Scanner;

/**
 * Post Office simulator.
 * During opening hours (inserted by the user), clients enter the office through a first waiting room (max capacity 500).
 * To access the front desks, clients move to a second limited waiting room (10 people).
 * Desks help clients with their tasks.
 *
 * Clients keep arriving at the Office at random times (not everyone at the same time).
 *
 * When opening time is over, no one can enter the office, but everyone who's already in the queue will be helped.
 *
 * @author Maria Vitali, matricola 548154
 */

public class PostOffice {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        /*Initialization of Post Office*/

        System.out.println("Welcome to the Post Office!\n");
        System.out.print("Insert today's opening time (seconds): ");

        long todayTime;
        todayTime = keyboard.nextLong();

        if(todayTime == 0){
            System.out.println("\nTODAY THE OFFICE IS CLOSED!\n");
            System.exit(0);
        }

        int counter = 0;    //number of clients that have visited the office so far

        FirstWaitingRoom firstRoom = new FirstWaitingRoom();
        Desks desks = new Desks();

        OpeningTime time = new OpeningTime(todayTime);
        Thread t = new Thread(time);

        /*open*/
        t.start();
        while(t.isAlive()){
            if(!firstRoom.isFull()){
                Task newClient = new Task("Client" + (++counter));
                firstRoom.addClient(newClient);
            }

            /*check the waiting rooms' sizes and adjust*/
            if(!desks.secondWaitingRoomFull() && !firstRoom.empty()){
                Task client = firstRoom.nextClient();
                System.out.println("-------> " + client.getName() + ": enters second waiting room");
                desks.helpClient(client);
                System.out.println("~~~ Second waiting room: " + desks.peopleInTheSecondRoom() + " clients.");
            }

            /*waits a random time for a new client to arrive*/
            try{
                Thread.sleep((long)Math.floor(Math.random()*1000));
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        /*closed*/
        System.out.println("\n\n********No more people can come in! Opening time is over!********\n\n");

        /*complete all tasks left, move people from first to second waiting room*/
        while(firstRoom.empty() == false){
            if(!desks.secondWaitingRoomFull()){
                Task client = firstRoom.nextClient();
                System.out.println("-------> " + client.getName() + ": enters second waiting room");
                desks.helpClient(client);
                System.out.println("~~~ Second waiting room: " + desks.peopleInTheSecondRoom() + " clients.");
            }
        }

        /*close desks and wait for them to complete the queued tasks*/
        desks.closeDesks();
        System.out.println("\n\nOFFICE CLOSED\n");
    }
}
