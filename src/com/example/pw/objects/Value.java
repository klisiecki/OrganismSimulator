package com.example.pw.objects;

import java.awt.Color;


/**
 * Klasa definiująca wartość odżywczą
 */
public class Value {
	private String type;
	private int typeId;
	private int id;
	private long lifeLenght;
	private long borntime;
	private Color color;
	
	/**
	 * Inicjalizuje wartość odżywczą o podanych parametrach
	 * @param type typ wartości
	 * @param typeId id typu 
	 * @param id id elementu
	 * @param lifeLenght czas życia
	 * @param color kolor 
	 */
	public Value(String type, int typeId, int id, int lifeLenght, Color color) {
		this.type = type;
		this.typeId = typeId;
		this.id = id;
		this.lifeLenght = lifeLenght;
		this.color = color;
		this.borntime = System.currentTimeMillis();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLifeLenght() {
		return lifeLenght;
	}

	public void setLifeLenght(long lifeLenght) {
		this.lifeLenght = lifeLenght;
	}

	public long getBorntime() {
		return borntime;
	}

	public void setBorntime(long borntime) {
		this.borntime = borntime;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
