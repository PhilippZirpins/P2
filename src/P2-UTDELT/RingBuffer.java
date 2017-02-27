public class RingBuffer {
    private Customer[] customers = null;

    private int capacity = 0;
    private int writePosition = 0;
    private int readPosition = 0;
    private int freeSeats = 0;

    public RingBuffer(int capacity){
        this.capacity = capacity;
        this. customers = new Customer[capacity];
    }

    public int getCapacity(){ return capacity; }

    public int getFreeSeats() { return freeSeats; }

    public int getReadPosition() { return readPosition; }

    public int getWritePosition() { return writePosition; }

    public boolean isAnyCustomerThere(){ return (capacity - freeSeats) > 0; }

    public synchronized boolean insertCustomer(Customer customer){
        //If false, we can't insert any new customers
        if (isAnyCustomerThere()){
            //If true, check if reading position exceeds the max capacity (index out of bounds)
            if (writePosition >= capacity){
                writePosition = 0;
            }
            //Insert new customer and update variables
            customers[writePosition] = customer;
            writePosition++;
            freeSeats--;
            return true;
        }
        return false;
    }

    public synchronized Customer getCustomer(){
        //Check if reading position exceeds the max capacity (index out of bounds)
        if (readPosition >= capacity){
            readPosition = 0;
        }
        //If false, there are no more customers in the list
        if (customers[readPosition] != null){
            //If true, get customer and free the space the customer took
            Customer customer = customers[readPosition];
            customers[readPosition] = null;

            //Update variables
            readPosition++;
            freeSeats++;

            return customer;
        }
        return null;
    }
}
