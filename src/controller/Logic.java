package controller;

import java.util.Arrays;
import java.util.Random;

public class Logic {
	/**
	 * Logic.java contains the artificial intelligence of the software
	 * Give it the current game area data and it will return the best move
	 * @param spielfeldData 
	 * @author Kai C. Mueller <kai.christian.mueller@gmail.com>
	 * 
	 */
	
	public static int getColPoints(int column, int item, Integer[][] SpielfeldData, int offset){
		/**
		 * this method will return the max possible points for a column
		 * beside the column id, it needs the the next empty row in this column (item),
		 * the game-area (SpielfeldData) and the offset int, which will decide
		 * if we do a real move (0) or simulate a move for the next round (1)
		 * 
		 */
		
		Integer[][] sf = SpielfeldData;
		// i will be our column counter
		int i = column;
		int col_points = 0;

		// way_points will contain the max points for each direction from the start point (firstEmptyPosArray)
		// way_points1 for player 1, way_points2 for player 2
		int way_points1[] = {0,0,0,0,0,0,0,0};
		int way_points2[] = {0,0,0,0,0,0,0,0};
		
		// way_possible will contain the max reachable points in each direction from the start point (firstEmptyPosArray)
		// if it is not possible the get 4 coins in either one direction, the column will not be choosed
		// way_possible1 for player 1, way_possible2 for player 2 
		int way_possible1[] = {0,0,0,0,0,0,0,0};
		int way_possible2[] = {0,0,0,0,0,0,0,0};
		
		// go through all 8 directions from the start point (firstEmptyPosArray)
		for(int way = 0; way <= 7; way++){
		// way 2 can not be more than 0, override
		
			// possible1/2 will count up while player 1/2 can make points in this way
			int possible1 = 0;
			int possible2 = 0;
			
			// final_possible1/2 will contain the max possible points for player 1/2 in this way
			int final_possible1 = 99;
			int final_possible2 = 99;
			
			// points1/2 will count up while player 1/2 is making points in this way
			int points1 = 0;
			int points2 = 0;
			
			// final_ponts1/2 will contain the max points for player 1/2 in this way
			int final_points1 = 99;
			int final_points2 = 99;

			// set start column and row
			int col = i;
			int row = item;
			
			// position will contain the counter for each way
			int position = 0;
			
			// while we are moving in the game area, follow each way 
			while(row <= 5 && col <= 6 && row >= 0 && col >= 0){
				
				// the first position will be the start position
				// it is not interesting, because it is 0 everytime
				if(position > 0){
					
					// if coin in position is 0 or 1
					if(sf[row][col] == 0 || sf[row][col] == 1){ 
						// count up possible1 and set the final value for points2
						// 0 or 1 will stop a line of 2
						possible1++; 
						if(final_points2 == 99){ final_points2 = points2; }
						}
					// if coin in position is 0 or 2
					if(sf[row][col] == 0 || sf[row][col] == 2){ 
						// count up possible2 and set the final value for points1
						// 0 or 2 will stop a line of 1
						possible2++; 
						if(final_points1 == 99){ final_points1 = points1; }
						}
				}
				
				// if coin in position is 1
				if(sf[row][col] == 1){ 
					// count up points1 and set the final value for possible2
					// 1 will stop a possible line of 2
					points1++; 
					if(position > 0){ if(final_possible2 == 99){ final_possible2 = possible2; } }
				}
				if(sf[row][col] == 2){ 
					// count up points2 and set the final value for possible1
					// 2 will stop a possible line of 1
					points2++;
					if(position > 0){ if(final_possible1 == 99){ final_possible1 = possible1; } }
				}

				// uncomment this and the AI will unveil it's evil thinking!
				
				/*
					System.out.print(" col: ");
					System.out.print(col);
					System.out.print(" way: ");
					System.out.print(way);
					System.out.print(" row: ");
					System.out.print(row);
					System.out.print(" points1: ");
					System.out.print(points1);
					System.out.print(" points2: ");
					System.out.println(points2);
				*/
				
				// update the final points values
				if(final_points1 != 99){ way_points1[way] = final_points1; }
				else { way_points1[way] = points1; }
				if(final_points2 != 99){ way_points2[way] = final_points2; }
				else { way_points2[way] = points2; }
				
				// update the final possible values
				if(final_possible1 != 99){ way_possible1[way] = final_possible1; }
				else { way_possible1[way] = possible1; }
				if(final_possible2 != 99){ way_possible2[way] = final_possible2; }
				else { way_possible2[way] = possible2; }
				
				// get the next position to go to
				switch(way){
					case 0: col--; break; // go left
					case 1: col++; break; // go right
					case 2: row--; break; // go up
					case 3: row++; break; // go down
					case 4: col--; row--; break; // go upper left
					case 5: col++; row++; break; // go lower right
					case 6: col--; row++; break; // go lower left
					case 7: col++; row--; break; // go upper right
				}
				
				// increment the position for this way
				position++;
				
			}
		}
		
		// at this point we have 8 ways we go from the start point
		// we will now summarize the points of the ways which belong together
		// we will have 4 ways at the end:
		
		// 0: from left to right (and backwards)
		// 1: from top to bottom (and backwards)
		// 2: from upper left to lower right (and backwards)
		// 3: from lower left to upper right (and backwards)
		
		// calculate points
		int final_points1[] = new int[] {
			way_points1[0] + way_points1[1], 
			way_points1[2] + way_points1[3],				
			way_points1[4] + way_points1[5],
			way_points1[6] + way_points1[7]
		};
		
        // calculate possible points
		int final_possible1[] = new int[] {
			way_possible1[0] + way_possible1[1], 
			way_possible1[2] + way_possible1[3],				
			way_possible1[4] + way_possible1[5],
			way_possible1[6] + way_possible1[7]
		};
		
		// If it is not possible to get 4 coins in line
		// set points of this way to 0, we won't waste time with it!
		for(int dir = 0; dir <= 3; dir++){
			if(final_possible1[dir] < 3){ final_points1[dir] = 0; }
		}
		
		// sort array, so we can get the maximum value which will be at position [3]
		Arrays.sort(final_points1);
		
		//adjust our final points
		for (int y = 0; y<final_points1.length; y++) {
			// if we can win, set points to 9
			if(final_points1[y] >= 3){ 
				// offset 0 means, that it's now our turn,
				// so if we can win with the current turn,
				// all other options are less relevant.
				// offset 1 means, that it's the opponents turn in two turns,
				// so if we can win, it's nice, but it's more important
				// if we can loose with this turn, so just 8 points
				// which will be later set to 7 points for "preparing winning situation"
				if(offset == 0){ final_points1[y] = 9; }
				if(offset == 1){ final_points1[y] = 8; }
			}
		}
		
		// do all the same for player 2
		int final_points2[] = new int[] {
			way_points2[0] + way_points2[1], 
			way_points2[2] + way_points2[3],				
			way_points2[4] + way_points2[5],
			way_points2[6] + way_points2[7]
		};
		
		int final_possible2[] = new int[] {
			way_possible2[0] + way_possible2[1], 
			way_possible2[2] + way_possible2[3],				
			way_possible2[4] + way_possible2[5],
			way_possible2[6] + way_possible2[7]
		};
		
		// If it is not possible to get 4 coins in line
		// set points to 0, we won't waste time with it!
		for(int dir = 0; dir <= 3; dir++){
			if(final_possible2[dir] < 3){ final_points2[dir] = 0; }
		}
		
		// sort array, so we can get the maximum value which will be at position [3]
		Arrays.sort(final_points2);
		
		// adjust the final points for the opponent
		for (int y = 0; y<final_points2.length; y++) {
			// if we can loose, set points
			if(final_points2[y] >= 3){ 
				// offset 0 means, that it's now our turn,
				// so if we can loose with the current turn,
				// it's not so relevant, because it's OUR turn,
				// we just have to WIN (9 points above) or to avoid the loose
				// by throwing our coin in this column (8 points)
				// offset 1 means, that it will be the opponents turn in two turns
				// so if we can loose, we have to AVOID this situation (9 points)
				// 9 points will later be set to -1 points, so it won't be used!
				if(offset == 0){ final_points2[y] = 8; }
				if(offset == 1){ final_points2[y] = 9; }
			}
			// if opponent can get three coins in a row, try to avoid this with priority 4
			if(final_points2[y] == 2){
				final_points2[y] = 4;
			}
		}
		
		// at this point we have all points for player 1 and player 2
		// now we look which player can make the most points in this column
		// the max points will be saved to col_points array
		
		// save max points to col_points array
		if(final_points1[3] > final_points2[3]){ col_points = final_points1[3];}
		else { col_points = final_points2[3];}
	
		return col_points;
	}
	
	public static int doDecision(Integer[][] SpielfeldData) {
		/**
		 * this method will decide in which column a coin should be added
		 * 
		 */
		
		Integer[][] sf = SpielfeldData;
		
		// let's find out, in which row our coin will be added
		// so if there are already three coins in a column
		// our coin will be added in row four
		// firstEmptyPosArray will contain the row with first empty space (0)
		// first fill it with the deepest row which is possible (5)
		int [] firstEmptyPosArray = {5,5,5,5,5,5,5};
		
		// go through 7 columns (0-6)
		for(int col = 0; col <= 6; col++){
			int maxrow = 5;
			// count up rows while row is not empty (1,2)
			for(int row = 5; row >= 0; row--){
				// while in for-loop: fill with max row value
				if (sf[row][col] == 0){
					firstEmptyPosArray[col] = maxrow;
				}
				else {
					maxrow--;
				}
			}
		}
	
		// get the maximum points for each column
		int col_points[] = {0,0,0,0,0,0,0};
		for(int i = 0; i <= 6; i++){
			col_points[i] = getColPoints(i,firstEmptyPosArray[i], sf,0);
		}
		
		// at this points the max reachable points for each column is finally calculated
		// now we will look one step further, what will happen the next round if we do a move?

		// now create an array for the next round points
		int col_next_points[] = {0,0,0,0,0,0,0};
		
		// simulate the max points for each column
		for(int i = 0; i <= 6; i++){
			// simulate the game area for the next round
			Integer[][] futureSpielfeldData = {
				      { 0, 0, 0, 0, 0, 0, 0 },
				      { 0, 0, 0, 0, 0, 0, 0 },
				      { 0, 0, 0, 0, 0, 0, 0 },
				      { 0, 0, 0, 0, 0, 0, 0 },
				      { 0, 0, 0, 0, 0, 0, 0 },
				      { 0, 0, 0, 0, 0, 0, 0 }
				    };
			// fill it with the current game area data
			for(int row = 0; row<=5; row++){
				for(int col = 0; col<=6; col++){
					futureSpielfeldData[row][col] = sf[row][col];
				}
			}

			// set the columns net empty position as our coin
			int row = firstEmptyPosArray[i];
			futureSpielfeldData[row][i] = 1;

			// set the row one coin higher
			if(firstEmptyPosArray[i] >= 1){firstEmptyPosArray[i]--;}

			// get the possible points for this scenario in this column
			col_next_points[i] = getColPoints(i,firstEmptyPosArray[i], futureSpielfeldData,1);

		}

		// now let's look at the next round points for each column
		// if we would make the opponent win, we won't do that, right?
		// but if we can win the next round with this column, let's do it!

		for(int i = 0; i<=6; i++){
			// change priority only if there is no actual win or loose situation
			if(col_points[i] != 8 && col_points[i] != 9){
				// we can loose? oh no! don't use this column!
				// remember: 9 points means loose when offset is 1
				if(col_next_points[i] == 9){ col_points[i] = -1; }
				// we can win! but it's the opponents turn 
				// remember: 8 points means win when offset is 1
				//if(col_next_points[i] == 8){ col_points[i] = 7; }
				if(col_next_points[i] == 8){ col_points[i] = -7; }
			}
		}

		// now we have to decide in which column we will throw our coin
		
		// first decision is to get the maximum points
		// 9 points will mean that we will win if we throw our coin in this column
		// 8 points will mean, that the other player will win if he will throw his coin in this column the next move
		
		// if column is full, set points to 0
		for (int colc = 0; colc <= 6; colc++) {
			if(sf[0][colc] != 0){
				col_points[colc] = -9;
			}
		}
		
		// TODO DEBUG
		// shows the final calculations (points for each column)
		
		int iu = 0;
		for (int testitem: col_points) {
			System.out.print(iu + ": ");
			System.out.println(testitem);
			iu++;
		}
		
		// get the column with the max points
		int count = 0;
		int maxcol = 0;
		int maxpoint = 0;	
	
		// we need a random int, 0 or 1
		Random zufallsgenerator;
        zufallsgenerator = new Random();
        int index = zufallsgenerator.nextInt(2);
		
        // define the order in which the columns will be chosen
		// entries at the end of pref_cols will be preferred
		// we prefer columns in the middle
        // make some combination which will be choosed randomly
        // so the coins will be more spread over the field
        // to get more chances
        int[][] pref_cols = {{0,6,1,5,2,4,3},{6,0,5,1,4,2,3}};
     
		for (int col: pref_cols[index]) {
			if(col_points[col] >= maxpoint){
				maxcol = col;
				maxpoint = col_points[col];
			}
			count++;
		}
		
		// return the column with the max points
		return maxcol;
	}
}
