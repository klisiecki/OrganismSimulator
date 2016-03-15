package com.example.pw.objects;
import java.awt.Graphics2D;
import java.awt.Point;


/**
 * Abstrakcyjna klasa Element definiuje elementy Organizmu posiadające reprezentację graficzną. 
 *
 */
public abstract class Element {
	/**
	 * Pozycja elementu w organiźmie (współrzędne)
	 */
	protected Point pos;
	/**
	 * Id elementu
	 */
	protected int id;
	
	/**
	 * Inicjalizuje element o podanym id
	 */
	public Element(int id) {
		this.id = id;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public void setPos(Point pos) {
		this.pos = pos;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Każdy obiekt klasy Element rysuje się za pomocą metody draw
	 * @param g obiekt typu Graphics2D, na którym element ma się narysować
	 */
	public abstract void draw(Graphics2D g);
	
}
