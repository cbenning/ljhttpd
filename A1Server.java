import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author chris
 * 
 * This object represents a server, at this point it is only one thread and can hold only one 
 * conncurrent connection.
 */
public class A1Server {
		
	private int port;
	//True is listening, false if not
	private boolean listening = false;
	//True if connected, false if not
	private ServerSocket server;
	private Socket client;

	
	//This is the default constructor, nothing much here
	public A1Server(){	
		
	}
	
	/**
	 * @param none
	 * 
	 * This is the main looping method in this object, 
	 * it keeps the listener running and ready for a new connection.
	 */
	public void run(){
		//Attempt to bind to port
		try {
			server = new ServerSocket(port);
			System.out.println("Server now listening on port "+port+".");
			System.out.println("------------------------------------------------------------------");
			listening = true;
			
			//This is the main daemon loop, it resets the listener when a connection is terminated and
			//Gets it ready for the next one.
			while(listening){
			listen();			
			
			
			}	
		}	 
		catch (IOException e) {
			System.err.println("Could not bind to port "+port+".");
			e.printStackTrace();
		}
		System.out.println("Server shutting down...");
	}
		
	/**
	 * @param none
	 * 
	 * This method basically just listens for a connection. Once a client connects, it enters a 
	 * loop which recieves and processes commands then sends a response. When a client disconnects
	 * it jumps out of the loop and gets all the streams ready for their next use.
	 */
	  public void listen() throws IOException
	  {
	      System.out.println("\nServer - waiting for a connection");
	    	  try{
	    		  //Wait for a connection
	    		  client = server.accept();
	  	    	//Log that the client has connected
	  	    	System.out.print("\nConnecting to: "+client.getInetAddress()+"...");
	  	    	System.out.println("Connected");
	  	    	System.out.print("Spawning thread...");
	  		    A1ServerThread tInstance = new A1ServerThread(client);
			    tInstance.start();
			    System.out.println("Thread spawned");
	    	  }
	    	  catch(IOException e){
	    		  System.out.println("Failed to connect to "+client.getInetAddress()+"\n"+e.getMessage());
	    	  }
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}

