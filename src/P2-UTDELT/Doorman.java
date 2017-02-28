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
		while(running){
			// If any seat is empty, then we can add a customer
			if (queue.isAnySeatEmpty()){
				gui.println("Doorman notified about free seats.");
				//Create new customer

				synchronized (queue){
					Customer newCustomer = new Customer();

					//Say that new customer arrived
					gui.println("Customer    " + newCustomer + " arrived.");

					//Add customer to queue (gui updated inside that method)
					queue.addCustomer(newCustomer);

					//Notify all consumers about the new customer
					queue.notifyAll();
				}

				//The customer was added to the queue and a consumer was notified about it, now daydream
				try {
					gui.println("Doorman daydreaming");
					Thread.sleep((int)(Math.random()*Globals.doormanSleep));
				} catch (InterruptedException e) {
					//If something crashes shut down appropriately
					e.printStackTrace();
					stopThread();
				}
				gui.println("Done dreaming");
			}
			// Else, no seats are empty, go into blocked state and wait until consumers free a seat/are done with a customer
			else{
				gui.println("Full house!");
				try {
					synchronized (queue){
						queue.wait();
					}
				} catch (InterruptedException e) {
					//If something crashes shut down appropriately
					e.printStackTrace();
					stopThread();
				}
			}
		}
	}

	/**
	 * Starts the doorman running as a separate thread. Make
	 * sure to create the thread and start it.
	 */
	public void startThread() {
		//Create and start new thread
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	/**
	 * Stops the doorman thread. Use Thread.join() for stopping
	 * a thread.
	 */
	public void stopThread(){

		running = false;
		thread.interrupt();

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// Add more methods as needed
}
