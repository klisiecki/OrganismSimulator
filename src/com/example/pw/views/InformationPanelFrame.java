package com.example.pw.views;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.example.pw.objects.Erythrocyte;
import com.example.pw.objects.Leukocyte;
import com.example.pw.objects.Organ;
import com.example.pw.objects.Virus;

public class InformationPanelFrame extends JFrame {
	private CardLayout cardLayout;
	
	private JPanel cardPanel;
	private JPanel erythrocytePanel;
	private JPanel organPanel;
	private JPanel virusPanel;
	private JPanel leukocytePanel;
	
	private JTextArea organList;
	private JScrollPane organListScroll;
	
	private JPanel erythrocyteButtonPanel;
	
	private Erythrocyte erythrocyte;
	private Organ organ;
	private Virus virus;
	private Leukocyte leukocyte;
	
	private JLabel virusInfoLabel;
	private JLabel leukocyteInfoLabel;
	private JLabel erythrocyteInfoLabel;
	
	private JLabel rightLabel;
	
	/**
	 * Tworzy panel informacyjny
	 */
	public InformationPanelFrame() {
		cardLayout = new CardLayout();
		cardPanel = new JPanel();
		cardPanel.setLayout(cardLayout);
		initializeErythrocytePanel();
		initializeOrganPanel();
		initializeVirusPanel();
		initializeLeukocytePanel();
		cardPanel.add(organPanel,"organ");
		cardPanel.add(erythrocytePanel,"erythrocyte");
		cardPanel.add(leukocytePanel,"leukocyte");
		cardPanel.add(virusPanel,"virus");
		add(cardPanel,BorderLayout.CENTER);
	}
	
	private void initializeVirusPanel() {
		virusPanel = new JPanel();
		virusInfoLabel = new JLabel();
		virusPanel.add(virusInfoLabel);
	}
	
	/**
	 * Aktualizuje iformacje o wirusie
	 */
	public void updateVirusInfo() {
		virusInfoLabel.setText("<html>Virus" +
				"<br>Id: " + virus.getId()+
				"<br>Cel: " + virus.getTarget().getName()+
				" </html>");
	}

	private void initializeLeukocytePanel() {
		leukocytePanel = new JPanel();
		leukocyteInfoLabel = new JLabel();
		leukocytePanel.add(leukocyteInfoLabel);
	}
	
	/**
	 * Aktualizuje informacje o leukocycie
	 */
	public void updateLeukocyteInfo() {
		leukocyteInfoLabel.setText("<html>Leukocyt" +
				"<br>Id: " + leukocyte.getId()+
				" </html>");
	}
	private void initializeErythrocytePanel() {
		erythrocytePanel = new JPanel();
		JPanel deletePanel = new JPanel();
		JButton deleteButton = new JButton("Usuń Erytrocyt (D)");
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				erythrocyte.delete();
			}
		});
		deletePanel.add(deleteButton);
		
		JPanel damagePanel = new JPanel();
		JButton damageButton = new JButton("Uszkodź Erytrocyt (U)");
		damageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				erythrocyte.damage();
			}
		});
		damagePanel.add(damageButton);
		
		JPanel newListPanel = new JPanel();
		JButton newListButton = new JButton("Losuj nową trasę");
		newListButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				erythrocyte.generateOrganList();
				updateOrganList();
			}
		});
		newListPanel.add(newListButton);
		
		erythrocyteButtonPanel = new JPanel();
		erythrocyteButtonPanel.setPreferredSize(new Dimension(350, 200));
		erythrocyteButtonPanel.add(newListPanel,BorderLayout.NORTH);
		erythrocyteButtonPanel.add(deletePanel,BorderLayout.CENTER);
		erythrocyteButtonPanel.add(damagePanel,BorderLayout.SOUTH);
		
		organList = new JTextArea();
		organList.setEditable(false);
		organList.setLineWrap(true);
		organList.setWrapStyleWord(true);
		organListScroll = new JScrollPane(organList);
		
		organListScroll.setPreferredSize(new Dimension(150,200));
		organListScroll.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder("Trasa erytrocytu:"),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                                    organListScroll.getBorder()));
		
		
		erythrocyteInfoLabel = new JLabel();
		JPanel erythrocyteInfoPanel = new JPanel();
		erythrocyteInfoPanel.add(erythrocyteInfoLabel);
		
		erythrocytePanel.add(erythrocyteInfoPanel,BorderLayout.WEST);
		erythrocytePanel.add(organListScroll,BorderLayout.EAST);
		erythrocytePanel.add(erythrocyteButtonPanel,BorderLayout.SOUTH);
	}
	
	/**
	 * Aktualizuje informacje o erytrocycie
	 */
	public void updateErythrocyteInfo() {
		erythrocyteInfoLabel.setText("<html>Erytrocyt" +
				"<br>Id: " + erythrocyte.getId()+
				" </html>");
	}
	
	private void updateOrganList() {
		organList.setText(erythrocyte.getOrganList());
	}
	
	private void initializeOrganPanel() {
		organPanel = new JPanel();
		JPanel labelPanel = new JPanel();
		JLabel leftLabel = new JLabel("<html>" +
				"Nazwa:<br>" +
				"Id:<br>" +
				"Stan zdrowia:<br>" +
				"Pojemność:<br>" +
				"Liczba wartości:<br>" +
				"Produkuje:<br>" +
				"Przyjmuje:<br>" +
				"</html");
		leftLabel.setHorizontalAlignment(SwingConstants.LEFT);
		leftLabel.setPreferredSize(new Dimension(120, 130));
		rightLabel = new JLabel("<html></html");
		rightLabel.setPreferredSize(new Dimension(200, 130));
		labelPanel.add(leftLabel,BorderLayout.LINE_START);
		labelPanel.add(rightLabel,BorderLayout.LINE_END);
		labelPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Podstawowe informacje:"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
		labelPanel.setPreferredSize(new Dimension(350, 180));
		organPanel.add(labelPanel);
		
		JButton makeValueButton = new JButton("Produkuj wartość odżywczą (N)");
		makeValueButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				organ.makeValue();
			}
		});
		JPanel valuePanel = new JPanel();
		valuePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Wartości odżywcze:"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
		valuePanel.add(makeValueButton);
		valuePanel.setPreferredSize(new Dimension(350, 200));
		
		organPanel.add(valuePanel);
	}
	
	/**
	 * Aktualizuje informacje o organie
	 */
	public void updateOrgan() {
		if(organ != null)
		rightLabel.setText("<html>"+
					organ.getName() + "<br>" +
					organ.getId() + "<br>" +
					organ.getHealth() + "%<br>" +
					organ.getCapacity() + "<br>" +
					organ.getValuesProducedCount() + "/"+organ.getValuesReceivedCount()+ "<br>" +
					organ.getProducedNames() + "<br>" +
					organ.getReceivedNames() + "<br>" +
							"");
	}
	
	/**
	 * Zmienia tryb wyświetlanych informacji
	 */
	public void setMode(String mode) {
		cardLayout.show(cardPanel,mode);
	}
	
	/**
	 * Ustawia referencję do zaznaczonego erytrocytu
	 */
	public void setErythrocyte(Erythrocyte e) {
		this.erythrocyte = e;
		organList.setText(e.getOrganList());
	}
	
	/**
	 * Ustawia referencję do zaznaczonego wirusa
	 */
	public void setVirus(Virus virus) {
		this.virus = virus;
	}
	
	/**
	 * Ustawia referencję do zaznaczonego organu
	 */
	public void setOrgan(Organ o) {
		this.organ = o;
		updateOrgan();
	}
	/**
	 * Ustawia referencję do zaznaczonego leukocytu
	 */
	public void setLeukocyte(Leukocyte leukocyte) {
		this.leukocyte = leukocyte;
	}
}