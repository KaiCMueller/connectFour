package model;

public class Spielfeld {

	public static Integer[][] Create() {
		// This creates an empty game area
		Integer[][] SpielfeldData = {
			      { 0, 0, 0, 0, 0, 0, 0 },
			      { 0, 0, 0, 0, 0, 0, 0 },
			      { 0, 0, 0, 0, 0, 0, 0 },
			      { 0, 0, 0, 0, 0, 0, 0 },
			      { 0, 0, 0, 0, 0, 0, 0 },
			      { 0, 0, 0, 0, 0, 0, 0 }
			    };
		return SpielfeldData;
	}
	
	/**
	 * Method to add a turn to the Grid.
	 * @param spielfeldData - object to use
	 * @param column - which column
	 * @param val - whose coin? 1 <= val <= 2
	 * @return
	 */
	public static Integer[][] Add(Integer[][] spielfeldData, int column, int val) {
		int maxrow = 5;
		for(int row=0; row<= 5; row++){
			if (spielfeldData[row][column] == 0){
				maxrow = row;
			}
		}
		spielfeldData[maxrow][column] = val;
		return spielfeldData;
	}
	
	
	
	
	
}
