import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

/** 
 * Programming AE2
 * Class to display cipher GUI and listen for events
 */
public class CipherGUI extends JFrame implements ActionListener
{
	//instance variables which are the components
	private JPanel top, bottom, middle;
	private JButton monoButton, vigenereButton;
	private JTextField keyField, messageField;
	private JLabel keyLabel, messageLabel;

	//application instance variables
	//including the 'core' part of the textfile filename
	//some way of indicating whether encoding or decoding is to be done
	private String fileName;
	private char codeType;
	
	private MonoCipher mcipher;
	private VCipher vcipher;
	
	/**
	 * The constructor adds all the components to the frame
	 */
	public CipherGUI()
	{
		this.setSize(400,150);
		this.setLocation(100,100);
		this.setTitle("Cipher GUI");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.layoutComponents();
		
		this.setVisible(true);
	}
	
	/**
	 * Helper method to add components to the frame
	 */
	public void layoutComponents()
	{
		//top panel is yellow and contains a text field of 10 characters
		top = new JPanel();
		top.setBackground(Color.yellow);
		keyLabel = new JLabel("Keyword : ");
		top.add(keyLabel);
		keyField = new JTextField(10);
		top.add(keyField);
		this.add(top,BorderLayout.NORTH);
		
		//middle panel is yellow and contains a text field of 10 characters
		middle = new JPanel();
		middle.setBackground(Color.yellow);
		messageLabel = new JLabel("Message file : ");
		middle.add(messageLabel);
		messageField = new JTextField(10);
		middle.add(messageField);
		this.add(middle,BorderLayout.CENTER);
		
		//bottom panel is green and contains 2 buttons
		
		bottom = new JPanel();
		bottom.setBackground(Color.green);
		//create mono button and add it to the top panel
		monoButton = new JButton("Process Mono Cipher");
		monoButton.addActionListener(this);
		bottom.add(monoButton);
		//create vigenere button and add it to the top panel
		vigenereButton = new JButton("Process Vigenere Cipher");
		vigenereButton.addActionListener(this);
		bottom.add(vigenereButton);
		//add the top panel
		this.add(bottom,BorderLayout.SOUTH);
	}
	
	/**
	 * Listen for and react to button press events
	 * (use helper methods below)
	 * @param e the event
	 */
	public void actionPerformed(ActionEvent e)
	{
		boolean inputIsValid = getKeyword() && processFileName();
	    if(inputIsValid)
	    {
	    	if(e.getSource() == monoButton)
	    	{
	    		mcipher = new MonoCipher(keyField.getText());
	    		keyField.setText("");
	    		processFile(false);
	    	}
	    	else if (e.getSource() == vigenereButton)
	    	{
	    		processFile(true);
	    	}
	    }
	    
	}
	
	/** 
	 * Obtains cipher keyword
	 * If the keyword is invalid, a message is produced
	 * @return whether a valid keyword was entered
	 */
	private boolean getKeyword()
	{
		String keyword = keyField.getText().toUpperCase();
		
		//Check if keyword is empty string, warn user if so.
		if(keyword.isEmpty() || keyword.trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "The keyword cannot be empty.", "Invalid Keyword", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else if (containsDuplicates(keyword))
		{
			JOptionPane.showMessageDialog(null, "The keyword cannot contain duplicate letters.", "Invalid Keyword", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else if (containsNumbersOrPunctuation(keyword))
		{
			JOptionPane.showMessageDialog(null, "The keyword cannot contain numbers or punctuation.", "Invalid Keyword", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else
		{
			return true;  
		}
	}
	
	/** 
	 * Is passed cipher keyword
	 * Checks if keyword contains duplicate characters.
	 * @return whether keyword contains duplicate characters.
	 */
	public boolean containsDuplicates(String s)
	{
		int l = s.length();
		for(int i = 0; i<l; i++)
		{
			char base = s.charAt(i);
			
			for(int j = i+1; j<l; j++)
			{
				char otherChar = s.charAt(j);
				if(base == otherChar)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/** 
	 * Is passed cipher keyword
	 * Checks if keyword contains numbers or punctuation. Assumes all letters in the keyword are capitalized.
	 * @return whether keyword contains numbers or punctuation.
	 */
	public boolean containsNumbersOrPunctuation(String s)
	{
		//Go through every character in keyword
		for(int i=0; i<s.length(); i++)
		{
			//Use unicode value to determine if character is not in the alphabet. 
			int difference = s.charAt(i)-'A';
			if(difference<0 && difference>25)
			{
				return true;
			}
			
		}
		return false;
	}
	
	
	/** 
	 * Obtains filename from GUI
	 * The details of the filename and the type of coding are extracted
	 * If the filename is invalid, a message is produced 
	 * The details obtained from the filename must be remembered
	 * @return whether a valid filename was entered
	 */
	private boolean processFileName()
	{
		String fileName = messageField.getText();
		int l = fileName.length();
		int lNoSpace = fileName.trim().length();
		if(l==0 || lNoSpace==0)
		{
			JOptionPane.showMessageDialog(null, "File name is empty. Please enter a valid file name.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		char lastChar = fileName.charAt(l-1);
		if(lastChar != 'P'  && lastChar != 'C')
		{
			JOptionPane.showMessageDialog(null, "Please ensure that the filename ends with: \n 'P' for plaintext files OR \n 'C' For encrypted files.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if(containsNumbersOrPunctuation(fileName))
		{
			JOptionPane.showMessageDialog(null, "The file name cannot contain punctuation or numbers", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else
		{
			this.fileName=fileName;
			this.codeType=lastChar;
			return true;
		}
		
	}
	
	/** 
	 * Reads the input text file character by character
	 * Each character is encoded or decoded as appropriate
	 * and written to the output text file
	 * @param vigenere whether the encoding is Vigenere (true) or Mono (false)
	 * @return whether the I/O operations were successful
	 */
	private boolean processFile(boolean vigenere)
	{
		String fileNameActual = fileName + ".txt";
		String outputFileName = fileName.substring(0, fileName.length()-1);
		if(codeType == 'P')
		{
			outputFileName = outputFileName + "C.txt";
		}
		else
		{
			outputFileName = outputFileName + "D.txt";
		}
		
		try
		{
			FileReader reader = new FileReader(fileNameActual);
			FileWriter writer = new FileWriter(outputFileName);
			
			boolean loop = true;
			while(loop)
			{
				//Get next character in file (.read() returns an int). -1 if end of file.
				int next = reader.read();
				char nextChar;
				if(next == -1)
				{
					//Exits loop upon reaching end of file so that reader.close() can be reached. Notifies user end of file has been reached.
					System.err.println("");
					System.err.println("End of File");
					loop = false;
					
				}
				else
				{
					//Test prints to ensure that reader is actually reading the text file.
					//Code will be replaced with calls to encode/decode and writing to a file.
					//Cast from int to char to get character.
					nextChar = (char) next;
					if(codeType == 'P')
					{
						writer.write(mcipher.encode(nextChar));
					}
					else
					{
						writer.write(mcipher.decode(nextChar));
					}
					System.out.print(nextChar);
				}
			}
			
			reader.close();
			writer.close();
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "File not found. Please enter a valid file name.", "File Not Found", JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException e)
		{
			System.err.println("IOException");
		}
		
		
		
		
	    return true;  // replace with your code
	}
	
	public static void main(String [] args)
	{
		CipherGUI ciph = new CipherGUI();
		
		
	}
	
	
}
