package model;
import java.net.*;      
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;


import javax.swing.table.AbstractTableModel;
/*
 * thread safety: 
 * The SwingWorker separates tasks that need to be done in the
 * background from tasks that actually modify shared data (where there might be concurrency issues). 
 * The model is updated through the done() method, which runs on 
 * the event dispatch thread, thus eliminating any concurrency problems. 
 * All concurrent functions are contained within the swingWorker 
*/
/**
 * This class handles the connection to the server, 
 * the guesses and responses of the server. 
 * It also extends the AbstractTableModel (and implements its methods) 
 * which facilitates the display of the data and its updates.
 */

public class JottoModel extends AbstractTableModel{
	 
		private int id;
		private ArrayList<String[]> pastGuesses;
		private static final String MAIN_SERVER_URL = "http://courses.csail.mit.edu/6.005/jotto.py";
	
	// Definition of Methods required by AbstractTableModel:
	public Object getValueAt(int r, int c) 
		{
		    if (r >= getRowCount() || c>=getColumnCount()) 
		    	 return null;
		    else 
		    	return pastGuesses.get(r)[c];
		       
		 	    
		    }
	public int getRowCount() 
		{ 
		return pastGuesses.size(); 
		}
	public int getColumnCount() 
		{ 
		return 3; 
		}

	/** Constructs a JottoModel object with specific
	 * ID or a random ID, if the input is invalid (negative or non-integer)
	 * @param String idText : String holding the puzzle ID
	 */
	public JottoModel (String idText) {
	   
	    try {
	    	int idInt= Integer.parseInt(idText);
            if(idInt <= 0)
                throw new RuntimeException();
            this.id = idInt;
        } catch (NumberFormatException ex) {
            this.id = 5 + (int) (Math.random() * 30000);
        }
       
        pastGuesses = new ArrayList<String[]>();
	}
	
	public int getID() {
		return id;
	}
	public ArrayList<String[]> getPastGuesses() {
	    return pastGuesses;
	}
	
	/** Sends a guess to the server and returns its response
	 * @param String attempt : String representing the guess made by the user
	 * @return String representing the response or null of any connection issue arises
	*/
	
     public String makeGuess(String attempt) {
    	
    	try 
    	{ 
        URL serverRequest = new URL(MAIN_SERVER_URL+ "?puzzle=" + id + "&guess=" + attempt);
        URLConnection yc = serverRequest.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
        String inputLine = in.readLine();
        in.close();
        return inputLine;
        }
    	catch (IOException e) 
    	{
    		return null;
    	}
    }
 	/**
 	 * Validates the response of the server and 
 	 * stores it in an abstract table entry 
 	 * @param yourGuess : String representing the guess made by the client 
 	 * @param serverResponse : The server's response to the guess
 	 * @return String[] : An array holding the server response to the guess
 	 */
    
    private String[] verifyServerResponse(String yourGuess, String serverResponse) {
	    if(serverResponse.equals("guess 5 5")) 
	    {
	        System.out.println("You win! The secret word was " + yourGuess + "!");
	        return new String[] {yourGuess, "You win!", ""};            
        }
	    else {    
	    String[] attemptInfo = new String[3];
        attemptInfo[0] = yourGuess;
        if(serverResponse.startsWith("guess")) 
        {
            attemptInfo[1] = serverResponse.substring(6,7); 
            attemptInfo[2] = serverResponse.substring(8,9);  
        } 
        else if(serverResponse.startsWith("error 0")) 
        {
            attemptInfo[1] = "Ill-formatted request.";
            attemptInfo[2] = "";
        } 
        else if(serverResponse.startsWith("error 1")) 
        {
            attemptInfo[1] = "Non-number puzzle ID.";
            attemptInfo[2] = "";
        } 
        else if(serverResponse.startsWith("error 2")) 
        {
            attemptInfo[1] = "Invalid guess. Length of guess != 5 or guess is not a dictionary word";
            attemptInfo[2] = "";
        }
	    
        return attemptInfo;
	    }
	}
	/**
	 * Handles multiple server requests by sending each request 
	 * in a a new thread through the SwingWorker class (thread safe)
	 * Updates the JottoModel once the response was received. 
	 * Updates the list of pastGuesses
	 * @param theGuess  : String representing the guess by the client

	 */
	public void sendGuess(final String theGuess) {
	    final int currRow = pastGuesses.size();
	    pastGuesses.add(new String[]{theGuess, "", ""});
	    
	    // update the table 
	    fireTableDataChanged();
	    
	    SwingWorker<String, Void> swingW = new SwingWorker<String, Void>() {
	        @Override
	        public String doInBackground() {
	            return makeGuess(theGuess);
	        }
	      
	        /*
	Thread-safe method required by swingWorker
	*/
	        
	        @Override
	        public void done() {
	            try {
	                String serverResponse = get();
	                if (serverResponse == null) {
	                    pastGuesses.set(currRow, new String[]{theGuess, "Issue with connection", ""});
	                    fireTableDataChanged(); 
	                    return;
	                }
	                

	                String[] verifiedResponse = verifyServerResponse(theGuess, serverResponse);
	                pastGuesses.set(currRow, verifiedResponse);
	                //System.out.println(serverResponse);
	                fireTableDataChanged(); 
                } 
	            catch (InterruptedException e) {} 
	            catch (ExecutionException e) {}
	        }
	    };
	    
	    swingW.execute();
	  
	}
}