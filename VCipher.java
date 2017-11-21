/**
 * Programming AE2
 * Class contains Vigenere cipher and methods to encode and decode a character
 */
public class VCipher
{
	private char [] alphabet;   //the letters of the alphabet
	private final int SIZE = 26;
	private char [][] cipher;
	private int cipherRow;
        // more instance variables
	
	/** 
	 * The constructor generates the cipher
	 * @param keyword the cipher keyword
	 */
	public VCipher(String keyword)
	{
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = (char)('A' + i);
		cipher = createCipher(keyword);
		cipherRow = 0;
		
		//Print out cipher in legible format.
		for(int o = 0; o < cipher.length; o++)
		{
			for (int i = 0; i < SIZE; i++)
				System.out.print(cipher[o][i] + " ");
			System.out.println(" Row Length: " + cipher[o].length);
		}
		
	}
	
	public char [][] createCipher(String keyword)
	{
		//Number of columns will always be equal to size of alphabet
		//Number of rows will depend on length of keyword.
		char [][] newCipher = new char [keyword.length()][SIZE];
		
		for(int i = 0; i < keyword.length(); i++)
		{
			//Pointer telling us what the next empty index in the row is.
			int cipherPointer = 0;
			//Find ith keyword character and place in first index of corresponding row.
			char currentChar = keyword.charAt(i);
			newCipher[i][0] = currentChar;
			cipherPointer++; 
			
			//Populate rest of row
			for(int j = 0; j < SIZE; j++)
			{
				//Find index of next character to be entered into the array.
				int index = currentChar-'A' + j;
				//If end of alphabet reached, start from beginning of alphabet.
				if(index > SIZE-1)
				{
					index = index - SIZE;
				}
				//Add character to array if it's not the starting character. Skip otherwise.
				if(currentChar != alphabet [index])
				{
					newCipher[i][cipherPointer] = alphabet[index];
					cipherPointer++; //Increment pointer.
				}
			}
		}
		return newCipher;
	}
	
	/**
	 * Encode a character
	 * @param ch the character to be encoded
	 * @return the encoded character
	 */	
	public char encode(char ch)
	{
		//Return encoded character if input is a letter, character itself if not (e.g. punctuation).
		if(isALetter(ch))
		{
			//Find position in alphabet and use it as index in cipher array.
			int indexAlphabet = ch-'A';
			//Get current row of cipher being used.
			int indexCipher = getCipherRow();
			//Return encoded character.
			return cipher[indexCipher][indexAlphabet];
		}
		else
		{
			return ch;
		}
	}
	
	/**
	 * Decode a character
	 * @param ch the character to be decoded
	 * @return the decoded character
	 */  
	public char decode(char ch)
	{
		if(isALetter(ch))
		{
			char decodedChar = ' ';
			//Get current row of cipher being used.
			int indexCipher = getCipherRow();
			//Linear search through cipher row to find character and return character at same index in alphabet.
			for(int i = 0; i < SIZE; i++)
			{
				if(ch == cipher[indexCipher][i])
				{
					decodedChar = alphabet[i];
				}
			}
			return decodedChar;
		}
		else
		{
			return ch;
		}
	}
	
	/**
	 * Retrieve row of cipher that should be used for next encode/decode operation and update it for the next operation.
	 * @return Row of cipher (cipher[return][]) to be used.
	 */  
	private int getCipherRow()
	{
		int indexCipher = cipherRow;
		//Update cipher row indicator so that it is correct for next character in file.
		//If it's already last row of cipher, set it back to 0.
		if(cipherRow == cipher.length-1)
		{
			cipherRow = 0;
		}
		//Otherwise, increment to next row.
		else
		{
			cipherRow++;
		}
		return indexCipher;
	}
	
	/**
	 * Determine whether a character is alphabetic (i.e. not a number, space or punctuation). Assumes letter will be capitalized.
	 * @param input the character to be tested
	 * @return whether character is alphabetic (true) or not (false)
	 */
	public boolean isALetter(char input)
	{
		//Use char unicode values to determine whether character is a letter (i.e. between A and Z, inclusive)
		int difference = input-'A';
		if(difference>=0 && difference<=25)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
