package controller;

import java.io.IOException;
import java.sql.SQLException;

import model.DataHandling;
import model.Database;

import view.GUI;
//import view.PlayerGUI;

public class Start {
	
	//initialize GUI
	public static Database database = new Database();
	public static GUI gui = new GUI();
	public static Controller controller = new Controller();
	
	/**
	 * The start class is the driver Class of "4gewinnt". Objects of the Class Database,
	 * GUI and Controller get instanced.
	 * Database gets checked for existence.
	 * GUI gets build.
	 * Controller gets started.
	 * 
	 * There is also an instance of PlayerGUI. For debugging purposes uncomment line 45,
	 * to play versus the KI.
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public static void main( String[] args ) throws IOException, InterruptedException, SQLException
	  { 
		// Check if database is available
//		database.sqlDB("DROP TABLE games");
		database.dbCheck("SELECT ID FROM games LIMIT 1","CREATE TABLE games (ID IDENTITY, gameID INTEGER, datetime CHAR(19), player INTEGER, column INTEGER, winner INTEGER);");
		
		// Build GUI
		gui.buildGUI(DataHandling.spielfeldData);
		
		//debuggin Player vs KI GUI
		DataHandling.deleteXML();
//		PlayerGUI playerGUI = new PlayerGUI();
//		playerGUI.buildPlayerGui();
		
		//debuggin Database
		database.printTable(DataHandling.readGames());
		
		// Start Controller
		controller.start();
	  }
}
