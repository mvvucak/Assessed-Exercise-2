/**
 * Programming AE2
 * Processes report on letter frequencies
 */
public class LetterFrequencies
{
	/** Size of the alphabet */
	private final int SIZE = 26;
	
	/** Count for each letter */
	private int [] alphaCounts;
	
	/** Frequency counts */
	private double [] textFreq;
	
	/** The alphabet */
	private char [] alphabet; 
												 	
	/** Average frequency counts */
	private double [] avgCounts = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0,
							       0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,  
								   6.3, 9.1, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1};

	/** Character that occurs most frequently */
	private char maxCh;

	/** Total number of characters encrypted/decrypted */
	private int totChars;
	
	/**
	 * Instantiates a new letterFrequencies object.
	 */
	public LetterFrequencies()
	{
		alphabet = new char [SIZE];
	    alphaCounts = new int [SIZE];	
	    textFreq = new double [SIZE];
	    for (int i = 0; i < SIZE; i++)
		{
			alphabet[i] = (char)('A' + i);
			alphaCounts[i] = 0;
			textFreq[i] = 0;
		}
	    totChars = 0;
	}
		
	/**
	 * Increases frequency details for given character
	 * @param ch the character just read
	 */
	public void addChar(char ch)
	{
		int index = ch-'A';
		alphaCounts[index]++;
		totChars++;
	    // your code
	}
	
	/**
	 * Gets the maximum frequency
	 * @return the maximum frequency
	 */
	private double getMaxPC()
    {
		int index = maxCh-'A';
		return textFreq[index];
	}
	
	/**
	 * Updates array with letter frequencies.
	 */
	private void calculateFreqPercentage()
	{
		for(int i=0; i<SIZE; i++)
		{
			textFreq[i] = (alphaCounts[i]/(totChars*1.0))*100.0;
		}
		
	}
	
	/**
	 * Updates maxChar with most frequently occurring character
	 */
	private void updateMaxCh()
	{
		int maxCount = 0;
		int maxCountIndex = 0;
		
		for(int i = 0; i<SIZE; i++)
		{
			if(alphaCounts[i] > maxCount || alphaCounts[i] == maxCount)
			{
				maxCount = alphaCounts[i];
				maxCountIndex = i;
			}
		}
		
		maxCh = alphabet [maxCountIndex];
	}
	
	/**
	 * Returns a String consisting of the full frequency report
	 * @return the report
	 */
	public String getReport()
	{
		updateMaxCh();
		calculateFreqPercentage();
		String report = String.format("LETTER ANALYSIS %n %n");
		String headings = String.format("%7s %5s %6s %9s %5s %n", "Letter", "Freq", "Freq%", "AvgFreq%", "Diff");
		report = report + headings;
		
		for(int i=0; i<SIZE; i++)
		{
			String row = String.format("%4s %7d %6.1f %7.1f %7.1f %n", alphabet[i], alphaCounts[i], textFreq[i], avgCounts[i], (textFreq[i] - avgCounts[i]));
			report = report + row;
		}
		
		report = report + String.format("%n The most frequent letter is %c at %.1f", maxCh, getMaxPC()) + "%.";
	    return report; 
	}
	
	public static void main(String[] args)
	{
		LetterFrequencies test = new LetterFrequencies();
		System.out.println(test.getReport());
		
	}
}
