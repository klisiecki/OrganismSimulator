package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Settings;

/**
 * Klasa Intersection definiuje skrzyżowanie między odcinkami.
 *
 */
public class Intersection extends Element{
	private static final int RADIUS = 14;
	
	//sąsiadujące odcinki
	protected ArrayList<Section> sections;
	//skrzyżowania osiągalne z odpowiednich sąsiadujących odcinków:
	protected ArrayList<Intersection> intersections;

	
	/**
	 * Typ układu do którego należy skrzyżowanie
	 */
	protected int type;
	
	/**
	 * Graficzna postać skrzyżowania
	 */
	private Circle shape;
	
	/**
	 * Semafor zabezpieczający skrzyżowanie przed kolizjami
	 */
	protected Semaphore sem;
	
	/**
	 * Funkcja blokująca możliwość wjechania skrzyżowanie
	 */
	public void lock() throws InterruptedException{
		sem.acquire();
	}
	
	/**
	 * Funkcja odblokowująca możliwość wjechania skrzyżowanie
	 */
	public void unlock() throws InterruptedException{
		sem.release();
	}
	
	/**
	 * Funkcja sprawdzająca czy można wjechać na skrzyżowanie
	 */
	public boolean isLocked() {
		return sem.availablePermits() == 0;
	}
	
	/**
	 * Tworzy nowe skrzyżowanie
	 * @param id Unikalny id elementu
	 * @param p Współrzędne środka skrzyżowania
	 */
	public Intersection(int id, Point p,int type) {
		super(id);
		this.type = type;
		this.pos = p;
		shape = new Circle(p,RADIUS);
		
		sections = new ArrayList<Section>();
		intersections = new ArrayList<Intersection>();
		
		sem = new Semaphore(1);
	}
	
	/**
	 * Dodaje sąsiadujący odcinek
	 */
	public void addSection(Section section) {
		sections.add(section);
	}
	
	/**
	 * Dodaje sąsiadujące skrzyżowanie
	 */
	public void addIntersection(Intersection intersection) {
		intersections.add(intersection);
	}
	
	/**
	 * Zwraca sąsiadujące odcinki
	 */
	public ArrayList<Section> getSections() {
		return this.sections;
	}
	
	/**
	 * Zwraca sąsiadujące skrzyżowania
	 */
	public ArrayList<Intersection> getIntersections() {
		return this.intersections;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(this.type == 0) g.setColor(Settings.INTERSECION_COLOR);
		else g.setColor(Settings.INTERSECION_COLOR2);
		g.fill(shape);
		g.setColor(Color.BLACK);
		//g.drawString(""+sem.availablePermits(), pos.x, pos.y);
	}
	
	@Override
	public Point getPos() {
		return this.pos;
	}
}
