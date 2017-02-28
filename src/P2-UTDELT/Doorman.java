/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 * One doorman instance corresponds to one producer in
 * the producer/consumer problem.
 */
public class Doorman implements Runnable {
	/**
	 * Creates a new doorman. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		A reference to the GUI interface.
	 */

	private CustomerQueue queue;
	private Gui gui;

	private Thread thread;
	private boolean running;

	public Doorman(CustomerQueue queue, Gui gui) {
		this.queue = queue;
		this.gui = gui;
		this.running = false;
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run(){
		// Incomplete
		while(running){

			if (queue.getFifo().getFreeSeats() > 0){
				queue.add(new Customer());
				notify(); //Notify consumer about the new customer, does this actually notify the consumer?
			}
			// If no seats left go into blocked state
			else{
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}



	}

	/**
	 * Starts the doorman running as a separate thread. Make
	 * sure to create the thread and start it.
	 */
	public void startThread() {
		// Incomplete
		thread = new Thread(this, "Doorman");
		thread.start();
		running = true;
	}

	/**
	 * Stops the doorman thread. Use Thread.join() for stopping
	 * a thread.
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
		//after trying to add new customer, sleep
		gui.println("Doorman sleeping");
	}

	// Add more methods as needed
}
