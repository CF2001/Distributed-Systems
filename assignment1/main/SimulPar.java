package main;

/**
 *    Definition of the simulation parameters.
 */
public final class SimulPar {
    
	/**
   	*   Number of total thieves, including the master.
   	*/
    public static final int M = 7;

	/**
   	*   Number of total ordinary thieves.
	*	Ordinary thief j , j = 1, ... , M-1
   	*/
	public static final int NUM_ORD_THIEVES = M - 1; 

	/** 
	 *	Maximum Displacement an ordinary thief can have.
	*/
	public static final int MAX_MDISPLACEMENT = 6;

	/** 
	 *	Minimum Displacement an ordinary thief can have.
	*/
	public static final int MIN_MDISPLACEMENT = 2;

	/**
	 * 	The number of exhibition rooms having paintings in display.
	 */
	public static final int NUM_ROOMS = 5;

	/**
	 * 	Maximum distance between the outside gathering site and the museum room.
	 */
	public static final int MAX_DistOut = 30;

	/**
	 * 	Minimum distance between the outside gathering site and the museum room.
	 */
	public static final int MIN_DistOut = 15;

	/**
	 * 	Maximum number of paintings hanging in each room.
	 */
	public static final int MAX_NUM_PAITINGS = 16;

	/**
	 * 	Minimum number of paintings hanging in each room.
	 */
	public static final int MIN_NUM_PATTINGS = 8;

	/**
	 * 	Number of ordinary thieves in an Assult Party.
	 */
	public static final int K = 3;

	/**
	 *  Total number of Assault Parties.
	 */
	public static final int NUM_ASSAULT_PARTIES = (M-1)/K;

	/**
	 * Maximum separation limit between thieves crawling in line.
	 */
	public static final int S = 3;


	/**
     *  Generate a random number between two numbers.
     * 
     * @param maxNumber Maximum number 
     * @param minNumber Minimum number 
     * @return
     */
    public static int generateRandom(int maxNumber, int minNumber)
    {
        return (int) Math.floor(Math.random() * (maxNumber - minNumber + 1) + minNumber);
    }

}
