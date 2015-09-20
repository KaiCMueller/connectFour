package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

public class PlayerGUI implements ActionListener {
	
	private JTextField col = new JTextField();
	private JRadioButton offen = new JRadioButton("offen");
	private JRadioButton playerO = new JRadioButton("Spieler O");
	private JRadioButton playerX = new JRadioButton("Spieler X");
	
	public void buildPlayerGui(){
		col.setPreferredSize(new java.awt.Dimension(50, 20));
		JFrame frame = new JFrame("Player GUI");
		JPanel panel = new JPanel();
		frame.setContentPane(panel);	
		
		panel.add(col);
		
		JPanel winnerPanel = new JPanel();
		Border titleChoose = BorderFactory.createTitledBorder("Winner:");
		winnerPanel.setBorder(titleChoose);
		panel.add(winnerPanel);
		

		offen.setActionCommand("offen");
		playerX.setActionCommand("playerX");
		playerO.setActionCommand("playerO");
		ButtonGroup winnerOptions = new ButtonGroup();
		winnerOptions.add(offen);
		winnerOptions.add(playerX);
		winnerOptions.add(playerO);
		offen.addActionListener(this);
		playerX.addActionListener(this);
		playerO.addActionListener(this);
		winnerPanel.add(offen);
		winnerPanel.add(playerX);
		winnerPanel.add(playerO);
		
		JButton save = new JButton("Speichern");
		save.setActionCommand("save");
		panel.add(save);
		save.addActionListener(this);
		
		// create frame data
		frame.pack();
		frame.setSize(250,180);
		frame.setResizable(false);
		frame.setVisible(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
	}
	public void actionPerformed(ActionEvent e) {
		try {
			DataHandling.deleteXML();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String sieger = "";
		if (playerO.isSelected()){
			sieger = "Spieler O";
		}
		if (playerX.isSelected()){
			sieger = "Spieler X";
		}
		if (offen.isSelected()){
			sieger = "offen";
		}
		
		if ("save".equals(e.getActionCommand())) {
			try {
				BufferedWriter writer = new BufferedWriter (new FileWriter(DataHandling.getContactPath() + "/server2" + DataHandling.getWeAre() + ".xml"));
				writer.write("<?xml version='1.0' encoding='utf-8'?>");
				writer.write("<content>");
				writer.write("<freigabe>true</freigabe>");
				writer.write("<satzstatus>Satz spielen</satzstatus>");
				writer.write("<gegnerzug>" + col.getText() + "</gegnerzug>");
				writer.write("<sieger>" + sieger + "</sieger>");
				writer.write("</content>");
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
