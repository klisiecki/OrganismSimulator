package com.example.pw.views;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


/**
 * Główne okno programu
 *
 */
public class MainFrame extends JFrame implements KeyListener{
	private static OrganismComponent organism;
	private Thread t;
	public MainFrame() {
		super();
		createOrganism();
		addKeyListener(this);
		getContentPane().setBackground(Color.BLACK);
	}
	
	private void createOrganism() {
		organism = new OrganismComponent();
		add(organism);
		t = new Thread(organism);
		t.start();
	}
	
	/**
	 * Funkcja ustawia referencję do okna panelu informacyjnego
	 */
	public void setInformationPanelFrame(InformationPanelFrame frame) {
		organism.setInformationPanelFrame(frame);
	}
	
	/**
	 * Funkcja ustawia referencje do okna panelu kontrolnego
	 */
	public void setControlPanelFrame(ControlPanelFrame frame) {
		organism.setControlPanelFrame(frame);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
		case 'd':
			OrganismComponent.removeSelectedCell();
			break;
		case 'u':
			OrganismComponent.damageSelectedCell();
			break;
		case 'n':
			OrganismComponent.makeValueInSelectedOrgan();
			break;
		case 'e':
			OrganismComponent.makeErythrocyte();
			break;
		case 'l':
			OrganismComponent.makeLeukocyte();
			break;
		}
	}
	
}
