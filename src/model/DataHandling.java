package model;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.Start;


public class DataHandling {
	/**
	 * Boolean to verify whether there is a new xml to read. 
	 */
	public static Boolean newXML;
	
	/**
	 * String to hold winning Player 
	 */
	public static String winningTeam;
	
	/**
	 * Boolean to verify whether the game is open. 
	 */
	public static Boolean gameWon = false;
	
	/**
	 * String to hold the server contact path provided by config panel
	 */
	private static String contactPathServer= new String();	
	/**
	 * Method to set contactPathServer
	 */
	public static void setContactPath (String path) {
		contactPathServer = path;
	}
	/**
	 * Method to get contactPathServer
	 */
	public static String getContactPath () {
		return contactPathServer;
	}
	
	/**
	 * String to hold player type: [spielerx | spielero]
	 */
	private static String weAre = new String();
	/**
	 * Method to set player
	 */
	public static void setWeAre (String name) {
		weAre = name;
	}
	/** 
	 * Method to get weAre
	 */
	public static String getWeAre () {
		return weAre;
	}
	
	/**
	 * A clean two-dimensional Integer-Array, representing the Grid.
	 */
	public static Integer[][] spielfeldData = {
	      { 0, 0, 0, 0, 0, 0, 0 },
	      { 0, 0, 0, 0, 0, 0, 0 },
	      { 0, 0, 0, 0, 0, 0, 0 },
	      { 0, 0, 0, 0, 0, 0, 0 },
	      { 0, 0, 0, 0, 0, 0, 0 },
	      { 0, 0, 0, 0, 0, 0, 0 }
	    };
	
	/**
	 * Method to clean up the used Grid.
	 */
	public static void resetSpielfeldData() {
		for (int i = 0; i <= 5 ; i++ ) {
			   for (int j = 0; j <= 6; j++ ) { 
				   spielfeldData[i][j] = 0;
			   }
		}
	}
	
	/** 
	 *  Reads and parses the Server-File into an
	 *  array to save XML data by patter: Key => Value
	 * 
	 *  @return xmlArray - A two dimensional Array holding the XML content by pattern Keyword -> Value
	 *  @throw IO Exception
	 */
	public static String[][] readXML() throws IOException{
		BufferedReader reader = null;
		// read XML file into a String
		while (reader == null) {
			try {
			reader = new BufferedReader (new FileReader(contactPathServer + "/server2" + weAre + ".xml"));
			}
			catch (IOException e){
				System.err.println("file in use, try again ;)");
			}
		}
		String fullFile = "";
		String input = "";
		while((input = reader.readLine()) != null) {
			fullFile += input;
		}
		reader.close();	
		// initiate the Array to keep the values
		String[][] xmlArray = {
				{"freigabe",""},
				{"satzstatus",""},
				{"gegnerzug",""},
				{"sieger",""},
		};		
		// Parse Freigabe
		int startFreigabe = fullFile.indexOf("<freigabe>") + 10;
		int endFreigabe = fullFile.indexOf("</freigabe>");
		xmlArray[0][1] = fullFile.substring(startFreigabe, endFreigabe);
		
		// Parse Satzstatus
		int startSatzstatus = fullFile.indexOf("<satzstatus>") + 12;
		int endSatzstatus = fullFile.indexOf("</satzstatus>");
		xmlArray[1][1] = fullFile.substring(startSatzstatus, endSatzstatus);
		
		// Parse Gegnerzug
		int startGegnerzug = fullFile.indexOf("<gegnerzug>") + 11;
		int endGegnerzug = fullFile.indexOf("</gegnerzug>");
		xmlArray[2][1] = fullFile.substring(startGegnerzug, endGegnerzug);
		
		// Parse Sieger
		int startSieger = fullFile.indexOf("<sieger>") + 8;
		int endSieger = fullFile.indexOf("</sieger>");
		xmlArray[3][1] = fullFile.substring(startSieger, endSieger);
		
		// return Array
		return xmlArray;
	}
	
	/**
	 * Delete the XML from the communication path.
	 * @throws IOException
	 */
	public static void deleteXML() throws IOException{
		File file = new File(contactPathServer + "/server2" + weAre + ".xml");
		file.delete();
	}
	
	/**
	 * Writes the given turn to the communication file
	 * 
	 * @param column - our actual turn
	 */
	public static void writeAgentFile(String column) throws IOException {
		BufferedWriter writer = new BufferedWriter (new FileWriter(contactPathServer + "/" + weAre + "2server.txt"));
		writer.write(column);
		writer.close();
	}
	
	/**
	 * Checks whether the XML is new or not and sets the boolean newXML.
	 * 
	 */
	public static void checkXML() throws IOException {
		File file = new File(contactPathServer + "/server2" + weAre + ".xml");
		if(file.exists() == true){
			newXML = true;
		}
		else{
			newXML = false;
		}
		
	}
	
	/** 
	 * Writes the turn into HSQLDB.
	 * 
	 * @param turn - the turn
	 * @param gameID - the current gameID
	 * @param date - the current date
	 * @param time - the current time
	 * @param player - the player
	 * @param winner - who won
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void writeTurnToDB(String gameID, String date, String time, int player, int turn, int winner) throws IOException, SQLException {
		Start.database.sqlDB("INSERT INTO games VALUES(NULL, " + gameID + ", '" + date + " " + time + "', " + player + ", " + turn + ", " + winner + ");");
	}	
	
	/**
	 * Returns all games.
	 * 
	 * @return qresult - a resultSet
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static ResultSet readGames() throws IOException, SQLException {
		ResultSet qresult = Start.database.selectDB("SELECT * FROM games");		
		return qresult;
	}
	
	/**
	 * Returns a specific game, by it's gameID.
	 * 
	 * @param gameID - Number of the wanted game
	 * @return gameData[gameID][player][turn][winner]
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static ResultSet readGame(int gameID) throws IOException, SQLException {
		ResultSet qresult = Start.database.selectDB("SELECT * FROM games WHERE gameID=" + gameID + "");		
		return qresult;
	}
	
	/**
	 * Returns the highest gameID from the database, to genereate the next.
	 * 
	 * @return highestGameID
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String getHighestID() throws IOException, SQLException {
		String ID = "";
		Start.database.connectDB();
		ResultSet result = readGames();
		result.last();
		if(result.getRow() == 0){
			ID = "10";
		}
		else {
			ID = result.getString("gameID");	
		}
		return ID;
	}

}
