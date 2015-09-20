package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import controller.Start;
import model.DataHandling;


public class Statistics implements ActionListener {
	
		// some Objects we need
		private JFrame statisticsFrame = new JFrame("Statistiken");
		
		public void buildStatistics() throws IOException, SQLException, NumberFormatException, ParseException {
			JPanel contentPanel = new JPanel();	
			statisticsFrame.setContentPane(contentPanel);
			statisticsFrame.setLayout(null);
			
			JPanel statsPanel = new JPanel();
			contentPanel.add(statsPanel);
			Border titleStats = BorderFactory.createTitledBorder("Statistiken");
			statsPanel.setBorder(titleStats);
			
			JPanel buttonPanel = new JPanel();
			contentPanel.add(buttonPanel);
			
			JButton ok = new JButton("ok");
			ok.setActionCommand("ok");
			ok.addActionListener(this);
			buttonPanel.add(ok);
			
			JLabel gameCountLabel = new JLabel();
			gameCountLabel.setText("Anzahl Spiele: " + getGameCount());
			statsPanel.add(gameCountLabel);
			
			JLabel roundsLabel = new JLabel();
			roundsLabel.setText("Anzahl Sätze: " + getTurnCount());
			statsPanel.add(roundsLabel);
			
			JLabel wonLabel = new JLabel();
			wonLabel.setText("Sätze gewonnen: " + getWon());
			statsPanel.add(wonLabel);
			
			JLabel lostLabel = new JLabel();
			lostLabel.setText("Sätze verloren: " + getLost());
			statsPanel.add(lostLabel);
			
			
			statisticsFrame.pack();
			statisticsFrame.setSize(150, 235);
			statisticsFrame.setResizable(false);
			statisticsFrame.setVisible(true);
			statisticsFrame.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		}
		
		private int getTurnCount() throws IOException, SQLException{
			int rounds = 0;
			Start.database.connectDB();
			ResultSet result = DataHandling.readGames();
			result.last();
			String lastID = result.getString("gameID");
			String countGames = lastID.substring(0, lastID.length()-1);
			rounds = (Integer.valueOf(countGames).intValue()-1)*3+Integer.valueOf(lastID.substring(lastID.length()-1, lastID.length())).intValue();
			
			return rounds;
		}
		
		private int getGameCount() throws IOException, SQLException{
			Start.database.connectDB();
			ResultSet result = DataHandling.readGames();
			result.last();
			String lastID = result.getString("gameID");
			String countGames = lastID.substring(0, lastID.length()-1);
			int games = Integer.valueOf(countGames).intValue();
			
			return games;
		}
		
		private int getWon() throws IOException, SQLException {
			ResultSet result = Start.database.selectDB("SELECT * FROM games WHERE winner=" + 1 + "");
			result.last();
			int won = result.getRow();
			return won;
		}
		
		private int getLost() throws IOException, SQLException {
			ResultSet result = Start.database.selectDB("SELECT * FROM games WHERE winner=" + 2 + "");
			result.last();
			int lost = result.getRow();
			return lost;
		}

	/**
	 * ActionListeners for the configuration panel.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("ok".equals(e.getActionCommand())) {
			statisticsFrame.dispose();
		}
	}
}
