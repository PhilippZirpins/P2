/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
	/**
	 * Creates a new customer queue. Make sure to save these variables in the class.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */

	private int queueLength;
	private Gui gui;

	private RingBuffer fifo;


    public CustomerQueue(int queueLength, Gui gui){
		this.queueLength = queueLength;
		this.fifo = new RingBuffer(queueLength);
		this.gui = gui;
	}

	public void add(Customer customer){
    	//If true returned, the customer was inserted
		if (fifo.insertCustomer(customer)){
			//Update gui
			gui.fillLoungeChair(fifo.getWritePosition() - 1, customer);
			gui.println("Chair filled: " + (fifo.getWritePosition() - 1) + " by customer: " + customer);
		}
		//Else, there is no seat left in the barber shop
		else{
			gui.println("No seats left.");
		}
	}

	public Customer next(){
		Customer nextCustomer = fifo.getCustomer();

		//Check if there are customers left in the barber shop
		if (nextCustomer == null){
			gui.println("No customers in the barber shop.");
			return null;
		}
		//If so, update gui and return the customer object
		gui.println("Customer: " + nextCustomer + " leaves seat: " + (fifo.getReadPosition() - 1));
		gui.emptyLoungeChair(fifo.getReadPosition() - 1);
		return nextCustomer;
	}

	public RingBuffer getFifo(){
		return fifo;
	}
}
