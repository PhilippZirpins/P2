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

	private Customer[] customers = null;

	private int writePosition = 0;
	private int readPosition = 0;
	private int seatsTaken = 0;


    public CustomerQueue(int queueLength, Gui gui){
		this.queueLength = queueLength;
		this. customers = new Customer[queueLength];
		this.gui = gui;
	}

	public synchronized boolean addCustomer(Customer customer){
		//If false, we can't insert any new customers
		if (isAnySeatEmpty()){
			//If true, check if reading position exceeds the max capacity (index out of bounds)
			if (writePosition >= queueLength){
				writePosition = 0;
			}
			//Insert new customer
			customers[writePosition] = customer;

			//Update gui
			gui.fillLoungeChair(writePosition, customer);
			gui.println("Customer    " + customer + " filled chair " + (writePosition + 1));

			//Update  variables
			writePosition++;
			seatsTaken++;

			return true;
		}
		//If we can't insert, then no seats left
		gui.println("No seats left.");
		return false;
	}

	public synchronized Customer getNextCustomer(){
		//Check if reading position exceeds the max capacity (index out of bounds)
		if (readPosition >= queueLength){
			readPosition = 0;
		}
		//If false, there are no more customers in the list
		if (customers[readPosition] != null){
			//If true, get customer, free the seat he/she took, and update gui
			Customer customer = customers[readPosition];
			gui.println("Customer    " + customer + " leaves seat " + (readPosition + 1));

			customers[readPosition] = null;
			gui.emptyLoungeChair(readPosition);

			//Update variables
			readPosition++;
			seatsTaken--;

			return customer;
		}
		gui.println("No customers in the barber shop.");
		return null;
	}

	public synchronized boolean isAnySeatEmpty(){ return (queueLength - seatsTaken) > 0; }

	public synchronized boolean isThereAtLeastOneCustomer(){ return seatsTaken > 0; }

}
