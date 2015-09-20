package view;
import static controller.Start.controller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

import controller.Controller;
import controller.Start;

import model.DataHandling;
import model.GameState;

public class GUI implements ActionListener {
	// Create some Objects we need	
	public JLabel gameStatusLabel = new JLabel("Status: Spiel nicht gestartet");
	private JLabel weAreLabel = new JLabel("Wir sind: " + DataHandling.getWeAre());
	private JLabel comPathLabel = new JLabel("Server-Pfad: " + DataHandling.getContactPath());
	private JPanel gridPanel = new JPanel();
	private JFrame mainFrame = new JFrame("Gummibärenbande 4 Gewinnt");
	private JLabel wonRoundsWe = new JLabel();
	private JLabel wonRoundsOpp = new JLabel();
	
	/**
	 * Method to update the label containing who we are
	 */
	public void updateWeAreLabel() {
		weAreLabel.setText("Wir sind: " + DataHandling.getWeAre());
	}
	
	/**
	 * Method to update the label containing the path to the server-files
	 */
	public void updateComPathLabel() {
		comPathLabel.setText("ComPath: " + DataHandling.getContactPath());
	}
	
	/**
	 * Method to build the GUI. 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void buildGUI(Integer[][] spielfeldData) throws IOException, SQLException {
		URL path_bg = GUI.class.getResource("/images/background.jpg");
		JLabel bg_image = new JLabel(new ImageIcon(path_bg));		
		// create frame object and its components
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create panel to hold all components
		JPanel contentPanel = new JPanel();	
		mainFrame.setContentPane(contentPanel);
	    // add grid to Frame
		contentPanel.add(bg_image);
		bg_image.setLayout(null);
		bg_image.add(gridPanel);
		gridPanel.setOpaque(false);
		gridPanel.setPreferredSize(new Dimension(510,430));
		// Create panel to hold all game informations
		JPanel infoPanel = new JPanel();
		bg_image.add(infoPanel);
		infoPanel.setOpaque(false);
		// Create panel to hold game settings
		JPanel settingsPanel = new JPanel();
		Border titleSettings = BorderFactory.createTitledBorder("Spiel Info");
		settingsPanel.setBorder(titleSettings);
		infoPanel.add(settingsPanel);
		
		//add the rounds won labels to bg_image Panel
		bg_image.add(wonRoundsWe);
		bg_image.add(wonRoundsOpp);
		
		// Create Statistics Labels
		settingsPanel.add(gameStatusLabel);
		settingsPanel.add(weAreLabel);
		settingsPanel.add(comPathLabel);
		settingsPanel.setOpaque(false);
		
		// Create MenuBar
		JMenuBar menuBar = new JMenuBar();
		// Create Menu
		JMenu menu = new JMenu("Spiel");
		menuBar.add(menu);
		// Create Menu Item for start game.
		JMenuItem startGame = new JMenuItem("Spiel starten");
		startGame.setActionCommand("start");			
		menu.add(startGame);
		startGame.addActionListener(this);
		
		// Create Menu Item for stop game.
		JMenuItem stopGame = new JMenuItem("Spiel anhalten");
		stopGame.setActionCommand("stop");			
		menu.add(stopGame);
		stopGame.addActionListener(this);
		
		// Create Menu Item to reset the grid.
		JMenuItem resetGame = new JMenuItem("Spielfeld zurück setzen");
		resetGame.setActionCommand("reset");			
		menu.add(resetGame);
		resetGame.addActionListener(this);
		
		menu.addSeparator();
		
		// Create Menu Item to do a new round
		JMenuItem newGame = new JMenuItem("Neuen Satz spielen");
		newGame.setActionCommand("newGame");			
		menu.add(newGame);
		newGame.addActionListener(this);
		
		menu.addSeparator();
		
		JMenuItem configure = new JMenuItem("Einstellungen");
		configure.setActionCommand("configure");
		menu.add(configure);
		configure.addActionListener(this);
		
		menu.addSeparator();
		
		JMenuItem oldGame = new JMenuItem("Zeige alte Spiele");
		oldGame.setActionCommand("oldGame");
		menu.add(oldGame);
		oldGame.addActionListener(this);
		
		JMenuItem statistics = new JMenuItem("Zeige Statistiken");
		statistics.setActionCommand("stats");
		menu.add(statistics);		
		statistics.addActionListener(this);
		
		menu.addSeparator();
		
		JMenuItem quit = new JMenuItem("Beenden");
		quit.setActionCommand("quit");
		menu.add(quit);		
		quit.addActionListener(this);
		
		// add Menu Bar to the Frame
		mainFrame.add(menuBar);
		mainFrame.setJMenuBar(menuBar);
		
		// now add the icons
		for (int row = 0, i = 0; row < spielfeldData.length ; row++ ) {
		   for (int col = 0; col < spielfeldData[row].length; col++, i++) { 
			  
			  // load the local graphics
			  URL path_empty = GUI.class.getResource("/images/empty.png");
			  URL path_user1 = GUI.class.getResource("/images/user1.png");
			  URL path_user2 = GUI.class.getResource("/images/user2.png");
			   
			  // create labels with graphics
			  JLabel label_empty = new JLabel(new ImageIcon(path_empty));
			  label_empty.setVisible(true);
			  JLabel label_user1 = new JLabel(new ImageIcon(path_user1));
			  label_user1.setVisible(true);
			  JLabel label_user2 = new JLabel(new ImageIcon(path_user2));
			  label_user2.setVisible(true);			  
			  
			  // assign matching graphics to labels
			   switch(spielfeldData[row][col]){
				   case 0: gridPanel.add(label_empty); break;
				   case 1: gridPanel.add(label_user1); break;
				   case 2: gridPanel.add(label_user2); break;
			   }			   
		   }
		}
		
		// positioning the components
		gridPanel.setBounds(300, 130, 520, 440);
		infoPanel.setBounds(10, 5, 200, 90);
		wonRoundsWe.setBounds(350, 632, 200, 42);
		wonRoundsOpp.setBounds(620, 630, 200, 42);
		
		// create frame data
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		gridPanel.setLayout(new GridLayout(6,7));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		infoPanel.setLayout(new FlowLayout());
		wonRoundsWe.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
		wonRoundsOpp.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));
		settingsPanel.setPreferredSize(new java.awt.Dimension(200, 80));
		mainFrame.pack();
  }

	/**
	 * Method to build the GUI. 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void updateGUI() throws SQLException, IOException {		
		//remove all old labels
		gridPanel.removeAll();

		for (int row = 0; row <= 5 ; row++ ) {
			   for (int col = 0; col <= 6; col++ ) { 
			  
			  // load the local graphics
			  URL path_empty = GUI.class.getResource("/images/empty.png");
			  URL path_user1 = GUI.class.getResource("/images/user1.png");
			  URL path_user2 = GUI.class.getResource("/images/user2.png");
			   
			  // create labels with graphics
			  JLabel label_empty = new JLabel(new ImageIcon(path_empty));
			  label_empty.setVisible(true);
			  JLabel label_user1 = new JLabel(new ImageIcon(path_user1));
			  label_user1.setVisible(true);
			  JLabel label_user2 = new JLabel(new ImageIcon(path_user2));
			  label_user2.setVisible(true);
			  
			  // assign matching graphics to labels
			   switch(DataHandling.spielfeldData[row][col]){
				   case 0: gridPanel.add(label_empty); break;
				   case 1: gridPanel.add(label_user1); break;
				   case 2: gridPanel.add(label_user2); break;
			   }
			   
		   }
		}
		
		//now add the icons indicating won rounds	
		String gameNr = controller.gameID.substring(0, controller.gameID.length()-1);
		String currentTurn = controller.gameID.substring(controller.gameID.length()-1, controller.gameID.length());		
		int rounds = Integer.valueOf(currentTurn).intValue();
		int wonWe = 0;
		int wonOpp = 0;
		int count = Controller.countTurns;
		
		if (count == 0){
			rounds--;
		}
		
		for (int i=1; i <= rounds; i++){		
			//get DB selections for the round and select last item of the round
			ResultSet result = Start.database.selectDB("SELECT * FROM games WHERE gameID=" + gameNr + "" + i + "");	
			result.last();			
			//get int from winner
			int winner = result.getInt("WINNER");
			
			if (winner==1){
				wonWe++;
			}
			if (winner==2){
				wonOpp++;
			}
		}
			
		// load the local graphics
		URL path_saft = GUI.class.getResource("/images/saft.png");
		URL path_krone = GUI.class.getResource("/images/krone.png");
			
		// create labels with graphics
		JLabel label_saft1 = new JLabel(new ImageIcon(path_saft));
		label_saft1.setVisible(true);
		JLabel label_saft2 = new JLabel(new ImageIcon(path_saft));
		label_saft2.setVisible(true);
		JLabel label_saft3 = new JLabel(new ImageIcon(path_saft));
		label_saft3.setVisible(true);
		JLabel label_krone1 = new JLabel(new ImageIcon(path_krone));
		label_krone1.setVisible(true);
		JLabel label_krone2 = new JLabel(new ImageIcon(path_krone));
		label_krone2.setVisible(true);
		JLabel label_krone3 = new JLabel(new ImageIcon(path_krone));
		label_krone3.setVisible(true);
		
		//remove old labels
		wonRoundsWe.removeAll();
		wonRoundsOpp.removeAll();
			
		//add images to the won labels
		switch (wonWe) {
			case 0:
				break;
				
			case 1:
				wonRoundsWe.add(label_saft1);
				break;
				
			case 2:
				wonRoundsWe.add(label_saft1);
				wonRoundsWe.add(label_saft2);
				break;
				
			case 3:
				wonRoundsWe.add(label_saft1);
				wonRoundsWe.add(label_saft2);
				wonRoundsWe.add(label_saft3);
				break;
		}
		
		switch (wonOpp) {
		case 0:
			break;
			
		case 1:
			wonRoundsOpp.add(label_krone1);
			break;
			
		case 2:
			wonRoundsOpp.add(label_krone1);
			wonRoundsOpp.add(label_krone2);
			break;
			
		case 3:
			wonRoundsOpp.add(label_krone1);
			wonRoundsOpp.add(label_krone2);
			wonRoundsOpp.add(label_krone3);
			break;
	}
		String id = controller.gameID;
		id = id.substring(id.length()-1, id.length());
		if (id.equals("1")) {
			wonRoundsOpp.removeAll();
			wonRoundsWe.removeAll();
		}
		mainFrame.pack();
		System.out.println("INTERFACE UPDATED");
  }

	
	/**
	 * ActionListeners for the GUI
	 */
	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		if ("stats".equals(e.getActionCommand())) {
			System.out.println("Zeige Statistik");
			Statistics stats = new Statistics();
				try {
					stats.buildStatistics();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    }		
		if ("start".equals(e.getActionCommand())) {
			controller.gameState = GameState.WAITFORSERVERFILE;
	        gameStatusLabel.setText("Status: Warte auf Serverfile!");
	    } 
		if ("stop".equals(e.getActionCommand())) {
	        controller.gameState = GameState.GAMEPAUSE;
	    } 
		
		if ("reset".equals(e.getActionCommand())) {
			controller.gameState = GameState.RESETGAME;
		}
		
		if ("configure".equals(e.getActionCommand())) {
			SettingsFrame settings = new SettingsFrame();
			settings.BuildConfigPanel();
	    } 	
		if ("oldGame".equals(e.getActionCommand())) {
			OldGames oldGames = new OldGames();
			try {
				oldGames.buildOldgames();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    } 
		if ("quit".equals(e.getActionCommand())) {
			System.exit(0);
	    } 		
		if ("newGame".equals(e.getActionCommand())) {
			controller.gameState = GameState.NEWGAME;
			try {
				DataHandling.deleteXML();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			DataHandling.gameWon = false;
	    } 
	}	
}
