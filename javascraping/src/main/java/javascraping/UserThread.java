package javascraping;

// Custom thread class. Used to run a custom funciton in the thread
public class UserThread extends Thread {
	ThreadInterface threadInterface;
	String[] inputs;

	// Pass the function and and inputs to that function in the constructor 
	UserThread(ThreadInterface threadInterface, String...inputs) {
		this.threadInterface = threadInterface;
		this.inputs = inputs;
	}

	// Override the run function. Thread class uses this function to perform 
	// user operations in thread.
	@Override
	public void run() {
		// Run the function along with the passed inputs
		this.threadInterface.func(this.inputs);
	}
}
