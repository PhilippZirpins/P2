/*
Produsent og konsument synkroniseres ved hjelp av metodene wait() og notify(),
og gjensidig utelukkelse implementeres via Javas monitorkonsept; dvs. synchronized funksjoner.

Produsent = Doorman
Consumer = Barber

Wait, notify, and notifyAll belongs to the class Object. Remember that all
Java objects are a subclass of Object.

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
Note: Always think carefully how to wake up threads.
	Improper use can lock the whole program

Thread.sleep(time)
• Static method
• Puts the thread in a timed wait state
• Sleeps for a given period of time before returning to a runnable state


Producer
• The producer adds items to a buffer as long as it is not full. If it is, the
producer should block.
• One or more producers should be awakened (notify / notifyAll) after a
consumer has removed an item from the buffer. Thus, the producer can
continue.

• Consumer
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

	private boolean running;
	Thread thread;

	public Barber(CustomerQueue queue, Gui gui, int pos) { 
		// Incomplete
		this.queue = queue;
		this.gui = gui;
		this.pos = pos;
		this.running = false;
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run(){

		while(running){
			if (queue.getFifo().isAnyCustomerThere()){
				Customer currentCustomer = queue.getFifo().getCustomer();
				gui.fillBarberChair(pos, currentCustomer);
				gui.println("Customer: " + currentCustomer + " takes barber seat: " + pos);
				notify(); //Notify the doorman thread, does that actually do that?
			}
			// If no customers there, go to sleep
			else{
				try {
					wait();
					gui.barberIsSleeping(pos);
					gui.println("Barber seat " + pos + " emptied, barber fell asleep.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Incomplete
		}
	}

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() {
		// Incomplete
		thread = new Thread(this, "Barber");
		thread.start();
		running = true;
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread() {
		// Incomplete
		running = false;
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.emptyBarberChair(pos);
	}

	// Add more methods as needed
}

