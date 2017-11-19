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
		//Validate keyword and file name.
		boolean inputIsValid = getKeyword() && processFileName();
	    if(inputIsValid)
	    {
	    	if(e.getSource() == monoButton)
	    	{
	    		mcipher = new MonoCipher(keyField.getText().toUpperCase());
	    		keyField.setText("");
	    		processFile(false);
	    	}
	    	else if (e.getSource() == vigenereButton)
	    	{
	    		vcipher = new VCipher (keyField.getText().toUpperCase());
	    		keyField.setText("");
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
		else if (containsNumbers(keyword))
		{
			JOptionPane.showMessageDialog(null, "The keyword cannot contain numbers.", "Invalid Keyword", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else if (containsPunctuation(keyword))
		{
			JOptionPane.showMessageDialog(null, "The keyword cannot contain punctuation.", "Invalid Keyword", JOptionPane.ERROR_MESSAGE);
			keyField.setText("");
			return false;
		}
		else
		{
			return true;  
		}
	}
	
	
	/** 
	 * Obtains filename from GUI
	 * The details of the filename and the type of coding are extracted
	 * If the filename is invalid, a message is produced 
	 * @return whether a valid filename was entered
	 */
	private boolean processFileName()
	{
		String fileName = messageField.getText();
		//Check if file name is empty.
		int l = fileName.length();
		int lNoSpace = fileName.trim().length();
		if(l==0 || lNoSpace==0)
		{
			JOptionPane.showMessageDialog(null, "File name is empty. Please enter a valid file name.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			messageField.setText("");
			return false;
		}
		
		//Check if file name finishes with P or C, error if not.
		char lastChar = fileName.charAt(l-1);
		if(lastChar != 'P'  && lastChar != 'C')
		{
			JOptionPane.showMessageDialog(null, "Please ensure that the filename ends with: \n 'P' for plaintext files OR \n 'C' For encrypted files.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			messageField.setText("");
			return false;
		}
		
		else if(containsPunctuation(fileName))
		{
			JOptionPane.showMessageDialog(null, "The file name cannot contain punctuation.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
			messageField.setText("");
			return false;
		}
		//Store file name details if they are valid.
		else
		{
			this.fileName = fileName;
			this.codeType = lastChar;
			messageField.setText("");
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
		String reportFileName = outputFileName +"F.txt";
		
		//Use code type to determine output file name.
		if(codeType == 'P') //if input file is plain text.
		{
			outputFileName = outputFileName + "C.txt";
		}
		else //If input file is already encrypted.
		{
			outputFileName = outputFileName + "D.txt";
		}
		
		//Create and fill output files.
		try
		{
			FileReader reader = new FileReader(fileNameActual);
			FileWriter writer = new FileWriter(outputFileName);
			FileWriter reportWriter = new FileWriter(reportFileName);
			LetterFrequencies reporter = new LetterFrequencies();
			
			boolean loop = true;
			while(loop)
			{
				//Get next character in file (.read() returns an int). -1 if end of file.
				int next = reader.read();
				char nextChar;
				//Exits loop upon reaching end of file so that reader.close() can be reached.
				if(next == -1)
				{
					loop = false;
				}
				else
				{
					//Cast from int to char to get character.
					nextChar = (char) next;
					//Encode file if it is plain text, decode if not.
					if(codeType == 'P')
					{
						char encoded;
						//Encode using corresponding cipher.
						if(vigenere)
						{
							encoded = vcipher.encode(nextChar);
						}
						else
						{
							encoded = mcipher.encode(nextChar);
						}
						//Write encoded character to file and update letter frequencies.
						writer.write(encoded);
						reporter.addChar(encoded);
						
					}
					else
					{
						char decoded;
						//Decode using corresponding cipher.
						if(vigenere)
						{
							decoded = vcipher.decode(nextChar);
						}
						else
						{
							decoded = mcipher.decode(nextChar);
						}
						//Write decoded character to file and update letter frequencies.
						writer.write(decoded);
						reporter.addChar(decoded);
						
					}
				}
			}
			//Generate letter frequencies report.
			String report = reporter.getReport();
			reportWriter.write(report);
			
			reader.close();
			writer.close();
			reportWriter.close();
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "File not found. Please enter a valid file name.", "File Not Found", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "An Input/Output error has ocurred. Please re-enter details correctly.", "Unknown Exception", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	    return true;
	}
	
	/** 
	 * Is passed cipher keyword in all caps.
	 * Checks if keyword contains duplicate characters.
	 * @return whether keyword contains duplicate characters.
	 */
	public boolean containsDuplicates(String s)
	{
		s = s.toUpperCase();
		int l = s.length();
		//Compare each character in the string to others to test if they are identical.
		for(int i = 0; i<l; i++)
		{
			char base = s.charAt(i);
			
			//No need to compare to prior characters as those tests would have already been made in previous loop run, hence j = i+1;
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
	 * Is passed cipher keyword or file name in all caps.
	 * Checks if they contain punctuation. 
	 * @return whether keyword contains punctuation.
	 */
	public boolean containsPunctuation(String s)
	{
		s = s.toUpperCase();
		//Go through every character in keyword
		for(int i=0; i<s.length(); i++)
		{
			//Use unicode values to determine if character is not in the alphabet AND not a number.
			int differenceInt = s.charAt(i)-'0';
			int differenceLetter = s.charAt(i)-'A';
			boolean notANumber = differenceInt < 0 || differenceInt > 10;
			boolean notALetter = differenceLetter < 0 || differenceLetter > 25;
			
			//If character unicode value is not in range of either numbers or letters
			if(notANumber && notALetter)
			{
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * Is passed cipher keyword or file name in all caps.
	 * Checks if they contain numbers.
	 * @return whether keyword contains numbers.
	 */
	public boolean containsNumbers(String s)
	{
		for(int i=0; i<s.length(); i++)
		{
			int difference = s.charAt(i)-'0';
			if(difference >= 0 && difference <= 10)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void main(String [] args)
	{
		CipherGUI ciph = new CipherGUI();
		
		
	}
	
	
}
