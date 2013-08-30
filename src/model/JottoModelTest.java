package model;

import static org.junit.Assert.*; 
import org.junit.Test;

 public class JottoModelTest {
	 
	 /* This is testing the constructor */

	 @Test
	 public void validIDInputTest() {
	        JottoModel model = new JottoModel("4567");
	        int expectedID = 4567;
	        assertEquals(expectedID, model.getID());
	    }
	 
    @Test
    public void emptyIDInputTest() {
        JottoModel m = new JottoModel("");
        assertTrue(m.getID() > 0);
    }

    
    /* This is the testing of the make Guess function in 3 cases
      non-delayed guesses, delayed guesses, invalid guesses
     */
    
    @Test
    public void invalidMakeGuessTest() {
        JottoModel m = new JottoModel("16952");
        
        String response = m.makeGuess("gmfkmdgmdfgd");
        
        assertTrue(response.startsWith("error"));
    }
    
    @Test
    public void validMakeGuessTest() {
        JottoModel m = new JottoModel("16952");
        
        String response = m.makeGuess("crazy");
        
        assertEquals("guess 3 1", response);
    }
    
    @Test
    public void delayedMakeGuessTest() {
        JottoModel m = new JottoModel("16952");
        
        String response = m.makeGuess("*bean");
        
        assertEquals("guess 1 0", response);
    }
    

    
    @Test
    public void emptyMakeTest() {
        JottoModel m = new JottoModel("16952");
        
        String response = m.makeGuess("");
        
        assertTrue(response.startsWith("error"));
    }
    
    /* This is the testing of the sendGuess method in 2 cases:
	immediate guesses, delayed guesses
     */
    @Test
    public void immediateGuessTest() throws InterruptedException {
        JottoModel m = new JottoModel("16952");
        m.sendGuess("crazy");
        m.sendGuess("cargo");
        String[] crazy = new String[] {"crazy", "3", "1"};
        String[] cargo = new String[] {"cargo", "You win!", ""};
        Thread.sleep(500);
        assertArrayEquals(m.getPastGuesses().get(0), crazy);
        assertArrayEquals(m.getPastGuesses().get(1), cargo);
    	
    }
    
    @Test
    public void delayedGuessTest() throws InterruptedException {
        JottoModel m = new JottoModel("16952");
        m.sendGuess("*bean");
        m.sendGuess("cargo");
        String[] beanDelayed = new String[] {"*bean", "1", "0"};
        String[] cargo = new String[] {"cargo", "You win!", ""};
        Thread.sleep(6000);
        assertArrayEquals(m.getPastGuesses().get(0), beanDelayed);
        assertArrayEquals(m.getPastGuesses().get(1), cargo);
    }
    

    


}
