package com.example.pw;
import javax.swing.JFrame;

import com.example.pw.views.ControlPanelFrame;
import com.example.pw.views.InformationPanelFrame;
import com.example.pw.views.MainFrame;

/**
 *	Główna klasa programu
 */
public class OrganismSimulator {
	private static final int MAIN_FRAME_WIDTH = 900;
	private static final int MAIN_FRAME_HEIGHT = 700;
	
	private static final int CONTROL_PANEL_FRAME_WIDTH = 350;
	private static final int CONTROL_PANEL_FRAME_HEIGHT = 300;
	
	private static final int INFORMATION_FRAME_WIDTH = 350;
	private static final int INFORMATION_FRAME_HEIGHT = 380;
	
	private static final int X_PROGRAM_POSITION = 100;
	private static final int Y_PROGRAM_POSITION = 25;
	
	private static final String TITLE = "Symulator Organizmu";
	private static final String CONTROL_PANEL_TITLE = "Panel Kontrolny";
	private static final String INFORMATION_PANEL_TITLE = "Panel Informacyjny";

	private static MainFrame mainFrame;
	private static ControlPanelFrame controlPanelFrame;
	private static InformationPanelFrame informationPanelFrame;
	
	/**
	 * Główna funkcja programu, tworzy wszystkie okna
	 */
	public static void main(String[] args) {
		initializeMainFrame();
		initializeControlPanelFrame();
		initializeInformationPanelFrame();
		connectFrames();		
	}

	/**
	 * Funkcja inicjalizująca główne okno programu
	 */
	private static void initializeMainFrame() {
		mainFrame = new MainFrame();
		mainFrame.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
		//mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setLocation(X_PROGRAM_POSITION, Y_PROGRAM_POSITION);
		mainFrame.setVisible(true);
	}
	
	/**
	 * Funkcja inicjalizująca onko panelu kontrolnego
	 */
	private static void initializeControlPanelFrame() {
		controlPanelFrame = new ControlPanelFrame();
		controlPanelFrame.setSize(CONTROL_PANEL_FRAME_WIDTH,CONTROL_PANEL_FRAME_HEIGHT);
		controlPanelFrame.setResizable(false);
		controlPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlPanelFrame.setTitle(CONTROL_PANEL_TITLE);
		controlPanelFrame.setLocation(X_PROGRAM_POSITION + mainFrame.getWidth() + 5, Y_PROGRAM_POSITION);
		controlPanelFrame.setVisible(true);
	}
	
	/**
	 * Funkcja inicjalizująca onko panelu informacyjnego
	 */
	private static void initializeInformationPanelFrame() {
		informationPanelFrame = new InformationPanelFrame();
		informationPanelFrame.setSize(INFORMATION_FRAME_WIDTH, INFORMATION_FRAME_HEIGHT);
		informationPanelFrame.setResizable(false);
		informationPanelFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		informationPanelFrame.setTitle(INFORMATION_PANEL_TITLE);
		informationPanelFrame.setLocation(	X_PROGRAM_POSITION + MAIN_FRAME_WIDTH + 5, 
											Y_PROGRAM_POSITION + CONTROL_PANEL_FRAME_HEIGHT + 20);
	}
	
	private static void connectFrames() {
		mainFrame.setInformationPanelFrame(informationPanelFrame);
		mainFrame.setControlPanelFrame(controlPanelFrame);
	}
	
}