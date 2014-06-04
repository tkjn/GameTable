package uk.co.dezzanet.gametable.charactersheet;

/**
 * Data model detailing the to-hit tables for different weapon skills.
 * 
 * NB, the arrays are zero-based
 * 
 * @author dezza
 */
public class ToHitModel {
	/**
	 * to hit table as per page 29 of rule book. NB this array is zero-based, so toHitTable[3][4] is attack WS4, defend WS 5
	 */
	private int[][] toHitTable = new int[][]{
		   //1  2  3  4  5  6  7  8  9  10
			{4, 4, 5, 6, 6, 6, 6, 6, 6, 6}, // WS 1
			{3, 4, 4, 4, 5, 5, 6, 6, 6, 6}, // WS 2
			{2, 3, 4, 4, 4, 4, 5, 5, 5, 6}, // WS 3
			{2, 3, 3, 4, 4, 4, 4, 4, 5, 5}, // WS 4
			{2, 3, 3, 3, 4, 4, 4, 4, 4, 4}, // WS 5
			{2, 3, 3, 3, 3, 4, 4, 4, 4, 4}, // WS 6
			{2, 3, 3, 3, 3, 3, 4, 4, 4, 4}, // WS 7
			{2, 2, 3, 3, 3, 3, 3, 4, 4, 4}, // WS 8
			{2, 2, 2, 3, 3, 3, 3, 3, 4, 4}, // WS 9
			{2, 2, 2, 2, 3, 3, 3, 3, 3, 4} // WS 10
	};
	
	/**
	 * Gets the to-hit table for a given attack WS
	 * @param attackersWS
	 * @return int[] zero-based array of ints
	 */
	public int[] getToHitForWS(int attackersWS) {
		if (attackersWS < 1 || attackersWS > 10) {
			throw new IllegalArgumentException("Invalid WS");
		}
		return toHitTable[attackersWS - 1];
	}
	
	/**
	 * Gets the to-hit roll needed for a given attack/defend WS
	 * @param attackersWS
	 * @param defendersWS
	 * @return int the to-hit roll needed
	 */
	public int getMinToHitRoll(int attackersWS, int defendersWS) {
		if (attackersWS < 1 || attackersWS > 10 || defendersWS < 1 || defendersWS > 10) {
			throw new IllegalArgumentException("Invalid WS");
		}
		return toHitTable[attackersWS - 1][defendersWS - 1];
	}
	
	/**
	 * Gets the to-hit table for a given defend WS
	 * @param defendersWS
	 * @return int[] zero-based array of ints
	 */
	public int[] getToHitForAttacker(int defendersWS) {
		if (defendersWS < 1 || defendersWS > 10) {
			throw new IllegalArgumentException("Invalid WS");
		}
		int[] data = new int[10];
		
		for (int i = 0; i < 10; ++i) {
			data[i] = toHitTable[i][defendersWS - 1];
		}
		
		return data;
	}
}
