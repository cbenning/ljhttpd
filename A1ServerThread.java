import java.io.*;
import java.net.Socket;

public class A1ServerThread extends Thread {

	private Socket client = null;
	private BufferedReader netIn = null;
	private PrintWriter netOut = null;
	private Request request;
	private String docroot = "doc_root/";

	//constructor
	public A1ServerThread(Socket client) {
		this.client = client;

		try {

			netIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
			netOut = new PrintWriter(client.getOutputStream(), true);

		} catch (IOException e) {

			System.out.println("Failed to create reader or writer");
			
		}
	}

	
	//main dealio
	public void run() {

      request = new Request(netIn);

       
       //Display the request Data
       System.out.println("New Request:");
       System.out.println("Method: "+request.getMethod() );
       System.out.println("Content-Length: "+request.getContentLength() );
       System.out.println("Requested File: "+request.getRequestedFileName());
       //Send Response
       sendResponse(netOut);
       


       //Clean up a nd close
       netOut.close();
	}


//awe cmonn do i need to comment this?
	public void closeConnection() throws IOException {
		netIn.close();
		netOut.close();
		client.close();
	}

//duuuhhh
	private boolean fileExists(String filename)
	{
	    File file = new File(filename);
	    if(file.exists())
	    {
	    	return true;
	    }
	    return false;
	}
	
	//Grab the file, and send it out through the stream
	private void sendFile(PrintWriter netOut, String filename)
	{
		BufferedReader in;
		
		try {
			
			in = new BufferedReader( new FileReader(new File(filename)) );
			
			StringBuffer result = new StringBuffer("");
			String line;
			
			while (( line = in.readLine()) != null){
				
		        result.append(line);
		        
		      }
			in.close();
			netOut.write("Content-Length: "+result.length()+"\n");
			netOut.write("Content-Type: text/html\r\n\r\n");
			netOut.write(result.toString());
			System.out.println(filename+" sent.");
					
		} catch (IOException e) {		}
	}

	
	// 1 for Invalid Request, 2 for Valid Request, 3 for Valid but file not exist 
	private void sendResponse(PrintWriter netOut)
	{
		
			StringBuffer getDataString = new StringBuffer("");
			StringBuffer postDataString = new StringBuffer("");
			
	       //If therei s get data
	       if(request.getGetData()!=null)
	       {
	    	   System.out.println("Data Passed within URL:");
		       String[][] getData = request.getGetData();
		       
				for(int i = 0; i < getData[0].length; i ++)
				{
					System.out.print("Key: ");
					getDataString.append("Key:");
					System.out.print(getData[0][i]+"   |   ");
					getDataString.append(getData[0][i]+"   |   ");
					System.out.print("Value: ");
					getDataString.append("Value: ");
					System.out.println(getData[1][i]+"");
					getDataString.append(getData[1][i]+"<br />");
				}
	       }
	       
	       //If it is post
	       if(request.getMethod().equals("post")&&request.getPostData()!=null)
	       {
	    	   System.out.println("Post Data...");
		       String[][] postData = request.getPostData();
		      
				for(int i = 0; i < postData[0].length; i ++)
				{
					System.out.print("Key: ");
					postDataString.append("Key:");	
					System.out.print(postData[0][i]+"   |   ");
					postDataString.append(postData[0][i]+"   |   ");
					System.out.print("Value: ");
					postDataString.append("Value: ");
					System.out.println(postData[1][i]+"");
					postDataString.append(postData[1][i]+"<br />");
				}
	       }

		
		//
	    //Deal with the file
	    //
		
		
		//Change this to whatever the doc root is
		String filename = docroot+request.getRequestedFileName();
		
			//If request is not valid
	        if(!request.isValid())
	       {
				netOut.append("HTTP/1.1 400 Invalid Request\n");
				sendFile(netOut, "doc_root/400_error.html");
			}
	        //If its valid and file exists
	        else if(request.isValid()&& fileExists(filename))
		    {
				netOut.append("HTTP/1.1 200 OK\r\n");
				sendFile(netOut,filename);
			}
	        //this is for a special echo request
	        else if(request.isValid()&& filename.trim().equals(docroot+"echo"))
		    {
		    	netOut.append("HTTP/1.1 200 OK\r\n");
				netOut.append("Content-Type: text/html\r\n\r\n");
		    	netOut.append("<html><head><title>ECHO</tite></head>\n");
		    	netOut.append("<body><center><h1>\n");

		    	//Add the form data
		        netOut.append("Get data:<br />");
				netOut.append(getDataString.toString());
			    netOut.append("Post data:<br />");
				netOut.append(postDataString.toString());

		    	netOut.append("</h1></center></body></html>\n");
		    }
	        //If its valid but file doesnt exist
		    else if(request.isValid()&& !fileExists(filename))
		    {
				netOut.append("HTTP/1.1 404 Not Found\r\n");
				sendFile(netOut, "doc_root/404_error.html");
			}
		    

	}

	

}
