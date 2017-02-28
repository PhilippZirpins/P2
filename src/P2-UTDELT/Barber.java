/*
Produsent og konsument synkroniseres ved hjelp av metodene wait() og notify(),
og gjensidig utelukkelse implementeres via Javas monitorkonsept; dvs. synchronized funksjoner.

Wait, notify, and notifyAll can only be called by the thread that owns the
monitor of the object. Ownership of an object's monitor is given by the
synchronized keyword.

Wait releases the monitor and puts the thread that performed the call in a
blocked state.

To awaken a thread that is in a blocked state, another thread must obtain
ownership of the same monitor and call notify or notifyAll on it.

A thread being awakened by notify/notifyAll continues from where it
was blocked.

notify evokes a random thread waiting for the monitor
notifyAll wakes all threads waiting for the monitor

Producer
• The producer adds items to a buffer as long as it is not full. If it is, the
producer should block.
• One or more producers should be awakened (notify / notifyAll) after a
consumer has removed an item from the buffer. Thus, the producer can
continue.

Consumer
• If the buffer is empty, the consumer should be blocked.
• When producer has added additional elements to the buffer, the consumer should
be awakened (notify / notifyAll).

*/

/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 * One barber instance corresponds to one consumer in
 * the producer/consumer problem.
 */
public class Barber implements Runnable {
	/**
	 * Creates a new barber. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		The GUI.
	 * @param pos		The position of this barber's chair
	 */

	private CustomerQueue queue;
	private Gui gui;
	private int pos;

	private Thread thread;

	public Barber(CustomerQueue queue, Gui gui, int pos) { 
		// Incomplete
		this.queue = queue;
		this.gui = gui;
		this.pos = pos;
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public synchronized void run(){
		while (true){

			//Let barber wait for new customers
			try {
				//Now wait until notified
				wait();

				//Update gui first
				gui.barberIsAwake(pos);
				gui.println("Barber         " + pos + " notified of a new customer.");

			} catch (InterruptedException e) {
				//If something crashes shut down appropriately
				e.printStackTrace();
				stopThread();
			}

			//Check if there is at least one customer
			if (queue.isThereAtLeastOneCustomer()){
				//Get the getNextCustomer customer
				Customer currentCustomer = queue.getNextCustomer();

				//Update the gui
				gui.fillBarberChair(pos, currentCustomer);
				gui.println("Customer    " + currentCustomer + " takes barber seat: " + pos);

				//Notify everyone (only the doorman matters here) that one seat was emptied
				notifyAll();

				//Now work in some random amount of time.
				try {
					Thread.sleep((int)(Math.random()*Globals.barberWork));
				} catch (InterruptedException e) {
					//If something crashes shut down appropriately
					e.printStackTrace();
					stopThread();
				}

				//Work done, empty chair in gui
				gui.emptyBarberChair(pos);

				//Now daydream for som random amount of time, thus we have to update the gui
				try {
					gui.barberIsSleeping(pos);
					Thread.sleep((int)(Math.random()*Globals.barberSleep));
				} catch (InterruptedException e) {
					//If something crashes shut down appropriately
					e.printStackTrace();
					stopThread();
				}

				//Barber is done daydreaming, update gui and wait for next customer
				gui.barberIsAwake(pos);
				gui.println("Barber        " + pos + " waiting for new customer.");
			}
			// If no customers there, wait until the doorman notifies one of the barbers
			else{
				try {
					wait();
				} catch (InterruptedException e) {
					//If something crashes shut down appropriately
					e.printStackTrace();
					stopThread();
				}
			}
		}
	}

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() {
		//Create and start thread
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread() {
		//Update gui

		gui.emptyBarberChair(pos);
		gui.println("Stopping barber thread.");

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// Add more methods as needed
}

