package ui;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;

import model.JottoModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import java.awt.Dimension; 
/**
 * Creates the GUI, and listens to the view. 
 */

public class JottoGUI extends JFrame {

    // remember to use these objects in your GUI:
    private  JButton newPuzzleButton;
    private  JTextField newPuzzleNumber;
    private  JLabel puzzleNumber;
    private  JTextField guess;
    private JTable guessTable;
    private JottoModel model;
    private JLabel guessLabel;
  
  
    private static final int DEFAULT_NUMBER_OF_ROWS = 400;
  

    public JottoGUI() { 
    	
    	
    	model = new JottoModel("16952");
    	
        guessLabel = new JLabel("Your guess is :");

        guessTable = new JTable();
        guessTable.setName("guessTable");
        
        guess = new JTextField();
        guess.setName("guess");
        
		/**
		 *Sends the client's guess to the model
		 */
    	guess.addActionListener(new ActionListener() {
 		   public void actionPerformed(ActionEvent e) {
 		      
 		       model.sendGuess(guess.getText());
 		       guess.setText("");
 		   }
 		});
    	
        newPuzzleButton = new JButton("Start a new Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleButton.addActionListener(new NewPuzzleListener());
        
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        newPuzzleNumber.addActionListener(new NewPuzzleListener());
        
        puzzleNumber = new JLabel("Puzzle Number : " + model.getID());
        puzzleNumber.setName("puzzleNumber");
        

        
    	
  
        

 	

 		JPanel placeHolder = new JPanel();		
		GroupLayout guiInterface = new GroupLayout(placeHolder);
		guiInterface.setAutoCreateGaps(true);
		guiInterface.setAutoCreateContainerGaps(true);
		placeHolder.setLayout(guiInterface);
		setTitle("Jotto client GUI");
		setContentPane(placeHolder);
		

		
		guiInterface.setHorizontalGroup(
				guiInterface.createParallelGroup()
		            .addGroup(guiInterface.createSequentialGroup()
		                    .addComponent(puzzleNumber)
		                    .addComponent(newPuzzleButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
		                    .addComponent(newPuzzleNumber))
		            .addGroup(guiInterface.createSequentialGroup()
		                    .addComponent(guessLabel)
		                    .addComponent(guess))
		            .addComponent(guessTable));		   
		                          
		guiInterface.setVerticalGroup(
				guiInterface.createSequentialGroup()
		            .addGroup(guiInterface.createParallelGroup(GroupLayout.Alignment.CENTER)
		                    .addComponent(puzzleNumber)
		                    .addComponent(newPuzzleButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
		                    .addComponent(newPuzzleNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
		            .addGroup(guiInterface.createParallelGroup(GroupLayout.Alignment.CENTER)
		                    .addComponent(guessLabel)
		                    .addComponent(guess, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
		            .addComponent(guessTable, 0, DEFAULT_NUMBER_OF_ROWS, Short.MAX_VALUE));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
 	}

 	public static void main(final String[] args) {
 		SwingUtilities.invokeLater(new Runnable() {
 			public void run() {
 				JottoGUI initial = new JottoGUI();

 				initial.setVisible(true);
 			}
 		});
 	}
 	
 	/**
 	 * A listener for starting a new puzzle
 	 */
 	class NewPuzzleListener implements ActionListener {
 
         public void actionPerformed(ActionEvent e) {
            model = new JottoModel(newPuzzleNumber.getText());
            newPuzzleNumber.setText("");
            puzzleNumber.setText("Puzzle #" + model.getID());
            guessTable.setModel(model);
            
        }
 	}
 }
