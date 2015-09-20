package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static controller.Start.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.DataHandling;


public class SettingsFrame implements ActionListener { 
	// some Objects we need
	private static JTextField comPath = new JTextField("Server-Pfad hier eingeben");
	private static JRadioButton playerX = new JRadioButton("Spieler X");
	private static JRadioButton playerO = new JRadioButton("Spieler O");
	private static JFrame configFrame = new JFrame("Einstellungen");
	
	/**
	 * The to build the configuration panel. 
	 */
	public void BuildConfigPanel() {
		
		// Create the Componentspanel
		JPanel configPanel = new JPanel();
		configFrame.setContentPane(configPanel);
		configFrame.setLayout(null);
		// Create the Panel to hold the Path Textfield
		JPanel pathPanel = new JPanel();
		Border titlePath = BorderFactory.createTitledBorder("Server-Pfad");
		pathPanel.setBorder(titlePath);
		configPanel.add(pathPanel);
		// Create the Panel to hold player chooser radio buttons
		JPanel choosePanel = new JPanel();
		Border titleChoose = BorderFactory.createTitledBorder("Spieler auswählen");
		choosePanel.setBorder(titleChoose);
		configPanel.add(choosePanel);
		// Create the Panel to hold the buttons
		JPanel buttonPanel = new JPanel();
		configPanel.add(buttonPanel);
		
				
		//create comPath Textfield
		comPath.setSize(180, 20);
		comPath.setPreferredSize(new java.awt.Dimension(170, 20));
		pathPanel.add(comPath);
//	    comPathButton.addActionListener(acl_comPath);
		
		//create Player Chooser
		playerX.setActionCommand("playerX");
		playerO.setActionCommand("playerO");
		ButtonGroup playerChooseOptions = new ButtonGroup();
		playerChooseOptions.add(playerX);
		playerChooseOptions.add(playerO);
		playerX.addActionListener(this);
		playerO.addActionListener(this);
		choosePanel.add(playerX);
		choosePanel.add(playerO);
		
		// create OK Button
		JButton ok = new JButton("OK");
		ok.setSize(50,20);
		ok.setLocation(10, 50);
		ok.setActionCommand("settings_ok");
		buttonPanel.add(ok);
		ok.addActionListener(this);
		
		// create CANCEL Button
		JButton cancel = new JButton("Abbrechen");
		cancel.setSize(50,20);
		cancel.setLocation(10, 50);
		cancel.setActionCommand("settings_cancel");
		buttonPanel.add(cancel);
		cancel.addActionListener(this);
		
		// create frame data
		configFrame.pack();
		configFrame.setSize(200,180);
		configFrame.setResizable(false);
		configFrame.setVisible(true);
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setLayout(new FlowLayout());
	}

/**
 * ActionListeners for the configuration panel.
 */
public void actionPerformed(ActionEvent e) {
	if ("settings_ok".equals(e.getActionCommand())) {
		// write comPath
		DataHandling.setContactPath(comPath.getText());
		gui.updateComPathLabel();
		if (playerX.isSelected() == true) {
			DataHandling.setWeAre("spielerx");
			gui.updateWeAreLabel();
		}
		if (playerO.isSelected() == true) {
			DataHandling.setWeAre("spielero");
			gui.updateWeAreLabel();
		}
		configFrame.dispose();
	}
	if ("settings_cancel".equals(e.getActionCommand())) {
		configFrame.dispose();
	}
}
}