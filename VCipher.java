/**
 * Programming AE2
 * Class contains Vigenere cipher and methods to encode and decode a character
 */
public class VCipher
{
	private char [] alphabet;   //the letters of the alphabet
	private final int SIZE = 26;
	private char [][] cipher;
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
		
	}
	
	public char [][] createCipher(String keyword)
	{
		//Number of columns will always be equal to size of alphabet
		//Number of rows will depend on length of keyword.
		char [][] newCipher = new char [keyword.length()][SIZE];
		
		for(int i = 0; i < keyword.length(); i++)
		{
			int cipherPointer = 0;
			char currentChar = keyword.charAt(i);
			newCipher[i][0] = currentChar;
			cipherPointer++; //Pointer telling us what the next empty index in the row is.
			
			
			for(int j = 0; j < SIZE; j++)
			{
				int index = currentChar-'A' + j;
				if(index > SIZE-1)
				{
					index = index - SIZE;
				}
				
				if(currentChar != alphabet [index])
				{
					newCipher[i][cipherPointer] = alphabet[index];
					cipherPointer++;
				}
			}
			
			System.out.println(newCipher[i]);
			System.out.println(newCipher[i].length);
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
	    return ' ';  // replace with your code
	}
	
	/**
	 * Decode a character
	 * @param ch the character to be decoded
	 * @return the decoded character
	 */  
	public char decode(char ch)
	{
	    return ' ';  // replace with your code
	}
	
	public static void main (String[] args)
	{
		VCipher vipher = new VCipher("TIGER");
	}
	
	
	
}
