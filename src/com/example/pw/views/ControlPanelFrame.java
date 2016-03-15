package com.example.pw.views;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;

import com.example.pw.utils.Settings;


public class ControlPanelFrame extends JFrame {
	private JPanel cellsPanel;
	private JPanel downPanel;
	private JPanel labelsPanel;
	
	private JButton closeButton;
	private JButton erythrocyteButton;
	private JButton leukocyteButton;
	private JButton endButton;
	private Container pane;
	
	private JCheckBox transparencyCheckbox;
	
	private static JLabel erythrocytesLabel;
	
	private int erythrocytesCount =0;
	private int leukocytesCount = 0;
	private int virusesCount = 0;
	private JLayeredPane layeredPane;
	
	private SimpleDateFormat sdf;
	private Date date;
	private String time;
	
	/**
	 * Tworzy panel kontrolny
	 */
	public ControlPanelFrame() {
		cellsPanel = new JPanel();
		pane = this.getContentPane();
		initializeCloseButton();
		initializeEndButton();
		initializeTransparencyCheckbox();
		initializeCellsButtons();
		initializeLabels();
	}
	
	private void initializeTransparencyCheckbox() {
		JPanel transparencyPanel = new JPanel();
		transparencyCheckbox = new JCheckBox("Włącz przeźroczystość");
		transparencyCheckbox.setSelected(Settings.TRANSPARENCY);
		transparencyCheckbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Settings.TRANSPARENCY = transparencyCheckbox.isSelected();
			}
		});
		transparencyPanel.add(transparencyCheckbox);
		downPanel.add(transparencyPanel,BorderLayout.PAGE_START);
		pane.add(downPanel, BorderLayout.SOUTH);
	}
	
	private void initializeEndButton() {
		JPanel endButtonPanel = new JPanel();
		endButton = new JButton("Zakończ symulację");
		endButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OrganismComponent.OVER = true;
			}
		});
		endButtonPanel.add(endButton);
		downPanel.add(endButtonPanel,BorderLayout.WEST);
	}
	
	private void initializeCloseButton() {
		downPanel = new JPanel();
		closeButton = new JButton("Zamknij Program");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		downPanel.setPreferredSize(new Dimension(350, 100));
		downPanel.add(closeButton,BorderLayout.EAST);
	}
	
	private void initializeCellsButtons() {
		erythrocyteButton = new JButton("Nowy Erytrocyt (E)");
		leukocyteButton = new JButton("Nowy Leukocyt (L)");
		leukocyteButton.setPreferredSize(new Dimension(180,30));
		erythrocyteButton.setPreferredSize(new Dimension(180,30));
		leukocyteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OrganismComponent.makeLeukocyte();
			}
		});
		erythrocyteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OrganismComponent.makeErythrocyte();
			}
		});
		
		layeredPane = new JLayeredPane();
		cellsPanel.add(layeredPane);
		cellsPanel.add(erythrocyteButton);
		cellsPanel.add(leukocyteButton);
		pane.add(cellsPanel,BorderLayout.CENTER);
	}
	
	private void initializeLabels() {
		labelsPanel = new JPanel();
		erythrocytesLabel = new JLabel();
		setErythrocytesCount(0);
		setLeukocytesCount(0);
		updateTime();
		updateInfoLabel();
		labelsPanel.add(erythrocytesLabel);
		pane.add(labelsPanel, BorderLayout.WEST);
	}
	
	/**
	 * Aktualizuje czas symulacji
	 */
	public void updateTime() {
		sdf = new SimpleDateFormat("mm:ss");
		date = new Date(OrganismComponent.getSimulationTime());
		time = sdf.format(date);
		updateInfoLabel();
	}
	
	/**
	 * Aktualizuje ogólne informacje
	 */
	private void updateInfoLabel() {
		erythrocytesLabel.setText("<html>Czas: " + time+
				"<br>Erytrocytów:"+ erythrocytesCount+"" +
				"<br>Leukocytów: " + leukocytesCount +
				"<br>Wirusów: " + virusesCount +
				"	</html>");
	}
	
	/**
	 * Aktualizuje stan przycisków
	 */
	public void updateButtons() {
		if(virusesCount == 0) {
			leukocyteButton.setEnabled(false);
			leukocyteButton.setText("Nowy Leukocyt (L)");
		} else if(OrganismComponent.nextLeukocyteTime() > 0) {
				leukocyteButton.setEnabled(false);
				leukocyteButton.setText(""+((Settings.LEUKOCYTE_MAKE_DELAY-OrganismComponent.nextLeukocyteTime())/1000+1));
		} else {
			leukocyteButton.setEnabled(true);
			leukocyteButton.setText("Nowy Leukocyt (L)");
		}
		
		if(OrganismComponent.getOrgan(10).isLocked()) erythrocyteButton.setEnabled(false);
		else erythrocyteButton.setEnabled(true);
	}
	
	/**
	 * Ustawia liczbę erytrocytów
	 */
	public void setErythrocytesCount(int count) {
		this.erythrocytesCount = count;
		updateInfoLabel();
	}
	
	/**
	 * Ustawia liczbę leukocytów
	 */
	public void setLeukocytesCount(int count) {
		this.leukocytesCount = count;
		updateInfoLabel();
	}
	
	/**
	 * Ustawia liczbę wirusów
	 */
	public void setVirusesCount(int count) {
		this.virusesCount = count;
		updateInfoLabel();
	}
	

}
