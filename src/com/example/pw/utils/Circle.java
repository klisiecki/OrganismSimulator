package com.example.pw.utils;
import java.awt.Point;
import java.awt.geom.Ellipse2D;


/**
 * Klasa Circle definiuje koło o podanych współrzędnych środka i promieniu.
 */
public class Circle extends Ellipse2D.Double {
	
	/**
	 * Promień koła
	 */
	protected int r;
	
	/**
	 * Tworzy koło o podanych parametrach
	 * @param p Współrzędne środka koła
	 * @param r Promień koła
	 */
	
	public Circle(Point p, int r) {
		super(p.x-r,p.y-r,r*2,r*2);
		this.r = r;
	}
	
	/**
	 *  Tworzy koło o podanych parametrach
	 * @param x Współrzędna x środka koła
	 * @param y Współrzędna y środka koła
	 * @param r Promień koła
	 */
	public Circle(int x, int y, int r) {
		this(new Point(x,y),r);
	}
	
	/**
	 * Funkcja ustawia nowe współrzędne dla koła
	 * @param p nowe współrzędne obiektu
	 */
	public void setPos(Point p) {
		this.x = p.x-r;
		this.y = p.y-r;
	}
}
