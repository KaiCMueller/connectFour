package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.DataHandling;
import model.GameState;
import model.Spielfeld;

import static controller.Start.gui;

public class Controller {
	/**
	 * Controller.java is the MVC Controller.
	 * The whole user interaction and data flow is managed here.
	 * 
	 * @param args
	 * 
	 */
	
	// init some variables we need
	public static GameState gameState;
	public boolean play = true;
	public String gameID ="";
	public static int countTurns = 0;	
	private static int oppTurn;
	private static int ownTurn;
	
	
	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	private static void writeOppTurnToDB(String gameID, int won) throws IOException, SQLException {
		// write turn to DB
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		
		DataHandling.writeTurnToDB(gameID, date.format(new Date()), time.format(new Date()), 2, oppTurn, won);
	}
	
	/**
	 * 
	 */
	private static void writeOwnTurnToDB(String gameID, int won) throws IOException, SQLException {
		// write turn to DB
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		
		DataHandling.writeTurnToDB(gameID, date.format(new Date()), time.format(new Date()), 1, ownTurn, won);
	}
	
	/**
	 * Method to Generate a new gameID. Syntax: <game Number><turn Number>
	 * @return String
	 * @throws IOException
	 * @throws JDOMException 
	 */
	private static String generateGameID() throws IOException, SQLException {
		
		String gameID = "";		
		String highestID = DataHandling.getHighestID();
		String oldSatzNr = highestID.substring(highestID.length()-1);
		String oldGameNr = highestID.substring(0, highestID.length()-1);
		if(oldSatzNr.equals("0") || oldSatzNr.equals("1") || oldSatzNr.equals("2")){
			int newSatzNr = Integer.valueOf(oldSatzNr).intValue()+1;			
			gameID = highestID.substring(0, highestID.length()-1) + newSatzNr;
		}
		else {
			int newGameNr = Integer.valueOf(oldGameNr).intValue()+1; 
			gameID = newGameNr + "1";
		}	
		System.out.println("GameID: " + gameID);
		return gameID;
	}
	
	/**
	 * Method to do own move. 
	 * 
	 * Parameters are the current gameID and an int for won. 
	 * won = 0 -> nobody won yet
	 * won = 1 -> we won with that turn
	 * 
	 * @param String gameID, int won
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private static void doOwnMove() throws IOException, SQLException{
		//count turns up
		countTurns++;
		
		// get our turn from logic
		ownTurn =  Logic.doDecision(DataHandling.spielfeldData);
		System.out.println("Wurf in Spalte: " + ownTurn);
		//convert our turn to string
		String ownTurnString = ""+ownTurn;
		//write our turn to contactPathServer
		DataHandling.writeAgentFile(ownTurnString);
		// add our turn to spielfeld
		DataHandling.spielfeldData = Spielfeld.Add(DataHandling.spielfeldData,ownTurn,1);
	}
	
	/**
	 * Method to do enemy move.
	 * 
	 * Parameters are the opponents colum, the current gameID and an int for won. 
	 * won = 0 -> nobody won yet
	 * won = 1 -> we won with that turn
	 *  0 <= enemyCol <= 6
	 * 
	 * @param int enemyCol, String gameID, int won
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private static void doEnemyMove(int enemyCol) throws IOException, SQLException{
		//count turn up
		countTurns++;
		
		// add enemy turn to spielfeld
		DataHandling.spielfeldData = Spielfeld.Add(DataHandling.spielfeldData,enemyCol,2);
		
		//save turn to variable
		oppTurn = enemyCol;
	}
	
	/**
	 * Method to get enemy move from the XML provided by the server.
	 * @return int enemyCol
	 */
	private static int getEnemyMove() throws IOException{
		// write XML to String-Array
		String[][] xmlData = DataHandling.readXML();
		// get enemyMove from String-Array
		String enemyMove = xmlData[2][1];
		// check enemyMove
		if (enemyMove.equals("")) {
			int enemyCol = -1;
			//return enemyMove
			return enemyCol;
		}
		else {
			// convert enemyMove to int
			int enemyCol = Integer.valueOf(enemyMove).intValue();
			//return enemyMove
			return enemyCol;
		}
	}
	
	/**
	 * Method to get the winner from the XML provided by the server.
	 * If there is a winner the boolean gameWon in DataHandling class is set to true
	 * and the name of the winning team is saved to the String winner in DataHandling.
	 * @throws SQLException 
	 */
	private static void getWon() throws IOException, SQLException{
		String offen = new String("offen");
		// write XML to String-Array
		String[][] xmlData = DataHandling.readXML();
		// get winner from String-Array
		String winner = xmlData[3][1];
		// verify whether the game is won or not
		System.out.println("Gewinner: " + winner);
	    if(winner.equals(offen)){
//			System.out.println("gameWon: " + DataHandling.gameWon);		
	    }
	    else{
	    	// if the game is won, set boolean gameWon true to stop the game and write the winner to winningTeam
	    	DataHandling.gameWon = true;
	    	gui.updateGUI();
	    	DataHandling.winningTeam = winner;
	    	System.out.println("gameWon: " + DataHandling.gameWon);	
	    }	    
	}
	
	
	/**
	 * Start method of the Controller. There is a while loop running and a case statement.
	 * Cases are switched via the GUI to trigger other methods.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void start() throws IOException, InterruptedException, SQLException {	
		gameState = GameState.NEWGAME;
		while ( play = true) {
			while (DataHandling.gameWon == false) {
				switch (gameState){	
					case NEWGAME:
						countTurns = 0;
						DataHandling.resetSpielfeldData();
						gameID = generateGameID();
						gui.updateGUI();
						gameState = GameState.WAITFORSERVERFILE;
						break;
				
					case GAMESTARTED:
						getWon();
						if (DataHandling.gameWon == false){
							gui.gameStatusLabel.setText("Status: Satz spielen!");
							if (getEnemyMove() == -1){
								System.out.println("Wir starten");
								doOwnMove();
								writeOwnTurnToDB(gameID, 0);
								System.out.println("Zug erledigt");
							}
							else {
								System.out.println("Gegner startet");
								doEnemyMove(getEnemyMove());	
								doOwnMove();
								writeOppTurnToDB(gameID, 0);
								writeOwnTurnToDB(gameID, 0);
								System.out.println("Zug erledigt");
							}
							DataHandling.deleteXML();
							gameState = GameState.WAITFORSERVERFILE;
							gui.updateGUI();
						}
						else {
							if (getEnemyMove() != -1) {
								doEnemyMove(getEnemyMove());
								writeOppTurnToDB(gameID, 2);
								gui.updateGUI();
							}
							else {
								Start.database.connectDB();
								ResultSet result = DataHandling.readGames();
								result.last();
								String ID = result.getString("ID");
								Start.database.sqlDB("UPDATE games SET winner='1' WHERE ID=" + ID + ";");
								gui.updateGUI();
							}
							DataHandling.deleteXML();
							gui.gameStatusLabel.setText("Status: Satz beendet!");
						}
						break;
	
						
					case WAITFORSERVERFILE:
						Thread.sleep(300);
						DataHandling.checkXML();	
						if(DataHandling.newXML == true){
							gameState = GameState.GAMESTARTED;
						}
						break;
						
					case GAMEPAUSE:
						gui.gameStatusLabel.setText("Status: Satz vom Spieler angehalten!");
						break;
						
					case RESETGAME:
						DataHandling.resetSpielfeldData();
						gameState = GameState.WAITFORSERVERFILE;
						gui.gameStatusLabel.setText("Status: Spielfeld zurück gesetzt!");
						gui.updateGUI();
						break;
				}
			}
		}
		
	}
}
