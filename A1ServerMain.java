

/**
 * @author chris
 * 
 * This is the main class that instantiates a new Server object and starts it's daemon running. It also processes commandline
 * parameters and sets the Server object accordingly before starting it.
 */
public class A1ServerMain {
	private final static int DEFAULT_PORT = 7000;
	
	/**
	 * @param args
	 * This is a static method that just fires up the server and initializes variable.
	 */
	
	public static void main(String[] args) {
		
		
		//Initialize the server
		A1Server runningServer = new A1Server();
		System.out.println("Starting server...\n");
		
		//Here check if there is any commandline argument.
		//If there is set the port, if not default to 7000
		if ((args.length>0)&&(args[0]!=null)){
			
			//Set the port
			runningServer.setPort(Integer.valueOf( args[0] ).intValue());
		}
		
		else {
			//Set the port to 7000
			runningServer.setPort(DEFAULT_PORT);
			System.out.println("No parameter was given or it was given in the wrong format. Defaulting to port "+DEFAULT_PORT+".\n");
		}
				
			runningServer.run();

		
	}
	


	
}
