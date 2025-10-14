package app.GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MenuBar;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class MainGUI extends JFrame{
	public MainGUI() {
		JFrame frame = new JFrame("App");
		frame.setLayout(new BorderLayout());
		MenuBarPanel menuBar = new MenuBarPanel(this);
		frame.add(menuBar.get(), BorderLayout.NORTH);
		
		
	    frame.setSize(1028, 720);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    setLocationRelativeTo(null);
	}
}
