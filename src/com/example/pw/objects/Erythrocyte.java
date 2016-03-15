package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.example.pw.utils.Generator;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


public class Erythrocyte extends Cell {
	private ArrayList<Organ> organList;
	private int listSize;
	
	private ArrayList<Integer> valueTypesTransmitted;
	private ArrayList<Value> valuesTransmitted;
	
	private int capacity;
	private int nextOrgan = 0;
	
	/**
	 * Tworzy erytrocyt o podanym id i pozycji
	 */
	public Erythrocyte(int id, Point pos) {
		super(id, pos);
		damaged = deleted = false;
		isLocking = false;
		this.capacity = Generator.getInt(Settings.MIN_VALUE_CAPACITY,Settings.MAX_VALUE_CAPACITY);
		generateOrganList();
	}
	
	
	/**
	 * Generuje nową listę organów (trasę erytrocytu)
	 */
	public void generateOrganList() {
		organList = new ArrayList<Organ>();
		//wylosowanie długości listy organów
		listSize = Generator.getInt(Settings.MIN_ORGAN_LIST_SIZE, Settings.MAX_ORGAN_LIST_SIZE);
		//zapełnienie listy organami
		int nr;
		Organ o;
		for(int i = 0; i < listSize; i++) {
			do {
				nr = Generator.getInt(0, OrganismComponent.ORGANS_COUNT-1);
				o = OrganismComponent.getOrgan(nr);	
			}
			while(organList.contains(o));
			organList.add(o);
		}
		
		valueTypesTransmitted = new ArrayList<Integer>();
		valuesTransmitted = new ArrayList<Value>();
		for(Organ organ: organList) valueTypesTransmitted.addAll(organ.getValuesReceived());
		nr = 0;
	}
	
	/**
	 * Funkcja przemieszczająca komórkę do podanego organu
	 * @param o organ docelowy
	 * @throws InterruptedException
	 */
	protected void goToOrgan(Organ o) throws InterruptedException {
		super.goToOrgan(o);
		if(!o.isDead()) {
			Thread.sleep(50);
			//zostawienei wartości
			valuesTransmitted.removeAll(o.leaveValues(this.valuesTransmitted));
			Thread.sleep(50);
			//zabranie wartości
			valuesTransmitted.addAll(o.takeValues(valueTypesTransmitted, this.capacity-valuesTransmitted.size()));
			Thread.sleep(50);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(damaged) g.setColor(Settings.ERYTHROCYTE_DAMAGED_COLOR);
		else g.setColor(Settings.ERYTHROCYTE_COLOR);
		
		g.fill(bounds);
		if(selected) {
			g.setColor(Color.YELLOW);
			g.draw(bounds);
		}
	}
	
	/**
	 * Zwraca listę organów jako obiekt typu String
	 */
	public String getOrganList() {
		String list = "";
		for(Organ o: organList) {
			list += o.getName() + "\n";
		}
		return list;
	}

	@Override
	public void run() {
		super.run();
		try {
			prevIntersection.lock();
			isLocking = true;
			intersectionLocked = prevIntersection;
			while(!OrganismComponent.OVER) {
				if(damaged) return;
				goToOrgan(organList.get(nextOrgan++));
				if(nextOrgan == organList.size()) nextOrgan = 0;
			}
		} catch (InterruptedException e) {}
		
	}
}
