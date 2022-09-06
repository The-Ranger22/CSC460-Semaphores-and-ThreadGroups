import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * @author Levi Schanding
 * @version 1.0
 */
public class Driver {

    private volatile static Semaphore door;
    private volatile static Semaphore servicing;
    private volatile static Semaphore nap;

    volatile static ThreadGroup rush;
    volatile static ThreadGroup slow;


    /**
     * @param args
     */
    public static void main(String[] args){
        door = new Semaphore(15);
        servicing = new Semaphore(0, true);
        nap = new Semaphore(0, true);

        Random randy = new Random();
        Scanner scanner =  new Scanner(System.in);

        Waiter waiter = new Waiter(nap, servicing);

        rush = new ThreadGroup("Rush");
        slow = new ThreadGroup("Slow");

        Customer[] customers = new Customer[100];
        //Init rush hour threads
        for(int i = 0; i < 50; i++){
            customers[i] = new Customer(rush, nap, door, servicing);
        }
        //Init slow hour threads
        for(int i = 50; i < 100; i++){
            customers[i] = new Customer(slow, nap, door, servicing);
        }

        System.out.print("Press enter to being rush hour simulation.");
        scanner.nextLine();

        waiter.start(); //Start the waiter thread

        try{
            Thread.sleep(1000);
        } catch (InterruptedException ie){

        }
        //Rush hour begins
        for(int i = 0; i < 50; i++){
            customers[i].start();
        }
        while(rush.activeCount() > 0){
            //busy wait
        }
        //Rush hour ends
        System.out.print("Press enter to being slow hour simulation.");
        scanner.nextLine();
        //Slow hour begins
        for(int i = 50; i < 100; i++){
            customers[i].start();
            try{
                Thread.sleep(randy.nextInt(451)+50); //Lower bound 50, upper bound 500 [nextInt's boundary is exclusive, hence the 451]
            } catch (InterruptedException e){

            }

        }
        while(slow.activeCount() > 0){
            //busy wait
        }
        //Slow hour ends
        waiter.interrupt();
    }






}
