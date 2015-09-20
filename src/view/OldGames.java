package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.DataHandling;

import controller.Start;

public class OldGames implements ActionListener {
	// some Objects we need
	private JFrame oldgamesFrame = new JFrame("Alte Spiele");
	private JComboBox gamesCombo;
	private JTextArea showRounds = new JTextArea(25, 10);
	private JScrollPane sp = new JScrollPane(showRounds,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public void buildOldgames() throws IOException, SQLException {
		JPanel contentPanel = new JPanel();	
		oldgamesFrame.setContentPane(contentPanel);
		oldgamesFrame.setLayout(null);
		
		showRounds.setVisible(false);
		
		gamesCombo = new JComboBox(getGames());		
		
		gamesCombo.setPreferredSize(new java.awt.Dimension(50, 15));
		contentPanel.add(gamesCombo);
	
		contentPanel.add(sp);
		
		JPanel buttonPanel = new JPanel();
		contentPanel.add(buttonPanel);
		
		JButton show = new JButton("Satz anzeigen");
		show.setActionCommand("show");
		show.addActionListener(this);
		buttonPanel.add(show);
		
		JButton close = new JButton("Schlieﬂen");
		close.setActionCommand("close");
		close.addActionListener(this);
		buttonPanel.add(close);
		
		oldgamesFrame.pack();
		oldgamesFrame.setSize(300, 500);
		oldgamesFrame.setResizable(false);
		oldgamesFrame.setVisible(true);
		oldgamesFrame.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
	}
	
	private String[] getGames() throws IOException, SQLException{		
		//get count rounds
		Start.database.connectDB();
		ResultSet result = DataHandling.readGames();
		result.last();
		String lastID = result.getString("gameID");
		String countGames = lastID.substring(0, lastID.length()-1);
		int rounds = (Integer.valueOf(countGames).intValue()-1)*3+Integer.valueOf(lastID.substring(lastID.length()-1, lastID.length())).intValue();
			
		String[] games1 = new String[rounds];
		//get time and gameID for the first turn of each round
		int round = 1;
		int gameNr = 1;
		
		for (int i = 1; i <= rounds; i++){				
			ResultSet timeAndID = Start.database.selectDB("SELECT * FROM games WHERE gameID=" + gameNr + round + "");
			if (round < 3){
				round++;
			}
			else {
				gameNr++;
				round = 1;
			}
			timeAndID.first();
			games1[i-1] = "gameID: " + timeAndID.getString("GAMEID") + "       Startzeit: " + timeAndID.getString("DATETIME");
		}		
		return games1;
	}
	
	private void showGame(Object ID) throws SQLException, NumberFormatException, IOException {
		//get all turn of game
		String gameID = ""+ID;
		gameID = gameID.substring(8, 10);
		ResultSet round = DataHandling.readGame(Integer.valueOf(gameID).intValue());
		//get number of turns
		round.last();
		int i = round.getRow();
		round.first();
		
		String firstDrop = round.getString("DATETIME");
		firstDrop = firstDrop.substring(11, 19);
		showRounds.append("\n");
		showRounds.append(" Satz " + gameID.substring(gameID.length()-1, gameID.length()) + " von Spiel " + gameID.substring(0, gameID.length()-1) + " beginnt um: " + firstDrop + "\n");
		showRounds.append("-------------------------------------------------------------------------------------\n");
		
		for(int n = 0; n < i; n++ ){
			String player = round.getString("PLAYER");
			String time = round.getString("DATETIME");
			time = time.substring(11, 19);
			if (player.equals("1")) {
				player = " Wir werfen in Spalte ";
			}
			else {
				player = " Der Gegner wirft in Spalte ";
			}
			int column = round.getInt("COLUMN");
			column++;
			showRounds.append(player + column + "  ( um " + time + " Uhr)\n");
			showRounds.append("-------------------------------------------------------------------------------------\n");
			
			String winner = round.getString("WINNER");
			if (winner.equals("1")) {
				showRounds.append(" Wir haben gewonnen!\n");
				showRounds.append("-------------------------------------------------------------------------------------\n");
			}
			if (winner.equals("2")) {
				showRounds.append(" Der Gegner hat gewonnen!\n");
				showRounds.append("-------------------------------------------------------------------------------------\n");
			}
	        round.next();
	    }
		round.last();
		String lastDrop = round.getString("DATETIME");
		lastDrop = lastDrop.substring(11, 19);
		showRounds.append(" Der Satz endet um " + lastDrop + "\n");
	}
	

	/**
	 * ActionListeners for the configuration panel.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("close".equals(e.getActionCommand())) {
			oldgamesFrame.dispose();
		}
		if ("show".equals(e.getActionCommand())) {
			showRounds.setText("");
			showRounds.setVisible(true);
			try {
				showGame(gamesCombo.getSelectedItem());
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
