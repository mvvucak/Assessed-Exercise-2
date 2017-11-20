/**
 * Programming AE2
 * Contains monoalphabetic cipher and methods to encode and decode a character.
 */
public class MonoCipher
{
	/** The size of the alphabet. */
	private final int SIZE = 26;

	/** The alphabet. */
	private char [] alphabet;
	
	/** The cipher array. */
	private char [] cipher;

	/**
	 * Instantiates a new mono cipher.
	 * @param keyword the cipher keyword
	 */
	public MonoCipher(String keyword)
	{
		//create alphabet
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++)
			alphabet[i] = (char)('A' + i);
		
		this.cipher = createCipher(keyword);
		
		System.out.println(cipher);
		for (int i = 0; i < SIZE; i++)
			System.out.print(cipher[i] +" ");
			
		
		
		// print cipher array for testing and tutors
	}
	
	public char [] createCipher(String keyword)
	{
		//The cipher will always be the same size as the alphabet array, since only letters not in the keyword are added to the cipher.
		char [] newCipher = new char [SIZE];
		int cipherPointer=0; //Pointer telling us what the next empty index in the cipher array is.
		
		//For each character in keyword, add character to corresponding index in cipher array and increment array pointer.
		for(int i=cipherPointer; i < keyword.length(); i++)
		{
			newCipher[i]= keyword.charAt(i);
			cipherPointer++;
		}
		
		//Loop through entire alphabet, starting from the end. If character is not in the keyword, add it. Else, don't.
		for(int i=SIZE-1; i>=0;i--)
		{
			if(!keyword.contains(""+alphabet[i]))
			{
				newCipher[cipherPointer]=alphabet[i];
				cipherPointer++;
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
		if(isALetter(ch))
		{
			//Find position in alphabet and use it as index in cipher array.
			int index = ch-'A';
			return cipher[index];
		}
		else
		{
			return ch;
		}
	}

	/**
	 * Decode a character
	 * @param ch the character to be encoded
	 * @return the decoded character
	 */
	public char decode(char ch)
	{
		
		//If character is a letter, use cipher to decode character. 
		if(isALetter(ch))
		{
			char decodedChar=' ';
			//Linear search through cipher array to find character and return alphabetical character at same index.
			for(int i=0; i<SIZE; i++)
			{
				if(ch == cipher[i])
				{
					decodedChar = alphabet[i];
					i = 27; //Exits loop to avoid unnecessary repetition when character is found.
				}
			}
			return decodedChar;
		}
		//Otherwise, just return it without changes.
		else
		{
			return ch;
			
		}
		
		
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
