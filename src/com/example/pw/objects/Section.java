package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import com.example.pw.utils.Settings;

/**
 * Klasa Section definiuje odcinek w układzie krwionośnym lub odpornościowym. 
 * Jest to klasa abstrakcyjna przechowująca elementy wspólne dla odcinków jedno- i dwukierunkowych.
 *
 */
public abstract class Section extends Element {
	public final static int PRESSURE_MEDIUM = 10;
	public final static int PRESSURE_HIGH = 12;
	
	/**
	 * Długość odcinka
	 */
	protected int length;
	
	/**
	 * Wierzchołki prostokąta będącego kształtem odcinka
	 */
	protected Point[] vertices;
	
	/**
	 * Ciśnienie odcinka, odpowiada za jego szerokość i prędkość poruszania się komórek
	 */
	protected int pressure;
	
	
	/**
	 * Kształt odcinka
	 */
	protected Polygon bounds;
	
	/**
	 * Jeden z końców odcinka 
	 */
	protected Point p1;
	
	/**
	 * Jeden z końców odcinka 
	 */
	protected Point p2;
	
	/**
	 * Skrzyżowanie początkowe odcinka
	 */
	protected Intersection begin;
	
	/**
	 * Skrzyżowanie końcowe odcinka
	 */
	protected Intersection end;
	
	/**
	 * Typ układu do którego należy odcinek
	 */
	protected int type;
	/**
	 * 
	 * @param id Unikalne id obiektu
	 * @param begin	Pierwsze z sąsiadujących skrzyżowań
	 * @param end Drugie z sąsiadujących skrzyżowań
	 * @param pressure Ciśnienie w naczyniu. Wynika z niego szerokość i szybkość przemieszczania się obiektów.
	 */
	public Section(int id, Intersection begin, Intersection end, int pressure, int type) {
		super(id);
		this.begin = begin;
		this.end = end;
		this.pressure = pressure;
		this.type = type;
		
		if (begin.getPos().getX() < end.getPos().getX()) {
			if(begin.getId() < 12 && type ==1 ) this.p1 = begin.getPos();
			else this.p1 = begin.getPos();
			this.p2 = end.getPos();
			
				
		} else {
			if(begin.getId() < 12 && type ==1 ) this.p2 = begin.getPos();
			else this.p2 = begin.getPos();
			this.p1 = end.getPos();
		}
		
		this.length = (int) p1.distance(p2);
		this.vertices = new Point[4];
		calculateVertices();
	}
	
	/**
	 * Oblicza współrzędne wierzchołków obszaru graficznej reprezentacji obiektu.
	 */
	protected abstract void calculateVertices();
	/**
	 * Zwraca współrzędne początka danego pasa ruchu
	 * @param i numer pasa ruchu
	 */
	protected abstract Point getBegin(int i);
	/**
	 * Zwraca współrzędne końca danego pasa ruchu
	 * @param i numer pasa ruchu
	 */
	protected abstract Point getEnd(int i);
	
	/**
	 * Dodaje komórkę c do pasa ruchu
	 * @param vesselNr numer pasa ruchu
	 */
	protected abstract void addCell(Cell c, int vesselNr);
	/**
	 * Usuwa komórkę c z pasa ruchu
	 * @param vesselNr numer pasa ruchu
	 */
	protected abstract void rmCell(Cell c, int vesselNr);
	/**
	 * Określa czy komórka o może przesunąc się do przodu
	 * @param vesselNr numer pasa ruchu
	 * @return
	 */
	protected abstract boolean free(Cell o, int vesselNr);
	/**
	 * Zwraca liczbę pasów ruchu
	 */
	protected abstract int lanes();
	
	/**
	 * Klasa TwoWay definiuje odcinek dwukierunkowy
	 *
	 */
	public static class TwoWay extends Section {
		private Polygon background;
		private Point b[];
		private Point e[];
		
		private ArrayList<ArrayList<Cell>> vessel;

		
		/**
		 * 
		 * @param id Unikalne id obiektu
		 * @param begin	Pierwsze z sąsiadujących skrzyżowań
		 * @param end Drugie z sąsiadujących skrzyżowań
		 * @param pressure Ciśnienie w naczyniu. Wynika z niego szerokość i szybkość przemieszczania się obiektów.
		 */
		public TwoWay(int id, Intersection begin, Intersection end, int pressure, int type) {
			super(id,begin, end,pressure,type);
			vessel = new ArrayList<ArrayList<Cell>>();
			vessel.add(new ArrayList<Cell>());
			vessel.add(new ArrayList<Cell>());
			
			
			bounds = new Polygon();
			background = new Polygon();

			for(Point p: vertices) bounds.addPoint(p.x, p.y);
			background.addPoint(vertices[0].x, vertices[0].y);
			background.addPoint(vertices[1].x, vertices[1].y);
			background.addPoint(vertices[2].x, vertices[2].y);
			background.addPoint(vertices[3].x, vertices[3].y);

			b = new Point[2];
			e = new Point[2];
			
			b[0] = 	new Point ((vertices[0].x + p1.x) / 2,
								(vertices[0].y + p1.y) / 2);

			e[0] = new Point( (vertices[1].x + p2.x) / 2,
								(vertices[1].y + p2.y) / 2);

			b[1] = new Point ((vertices[2].x + p2.x) / 2,
								(vertices[2].y + p2.y) / 2);
			
			e[1] = new Point ((vertices[3].x + p1.x) / 2,
							(vertices[3].y + p1.y) / 2);
		}		
		
		@Override
		public void addCell(Cell c, int vesselNr) {
			vessel.get(vesselNr).add(c);
		}
		
		@Override
		public void rmCell(Cell c,int vesselNr) {
			vessel.get(vesselNr).remove(c);
		}
		
		@Override
		public boolean free(Cell o, int vesselNr) {
			boolean free = true;
			synchronized(this) {
				if(vessel.get(vesselNr).size() > 1) for(Cell c: vessel.get(vesselNr)) {
					if(	o != c && 
						c.getDistance() - o.getDistance() <= (c.getSize()+o.getSize()) &&
						c.getDistance() - o.getDistance() > 0)
							free = false;
				}
			}
			return free;
		}
		
		@Override
		public Point getBegin(int i) {
			return b[i];
		}
		
		@Override
		public Point getEnd(int i) {
			return e[i];
		}
		
	
		@Override
		public void draw(Graphics2D g) {
			if(this.type == 0 && Settings.TRANSPARENCY)
				g.setColor(Settings.SECTION_TRANSPARENT_COLOR);
			else if(type == 0) g.setColor(Settings.SECION_COLOR);
			else g.setColor(Settings.SECION_COLOR2);
			g.fill(bounds);
		}

		@Override
		protected void calculateVertices() {
			int dy = (int) ((Math.abs(p1.getX()-p2.getX())*pressure)/length);
			int dx = (int) ((Math.abs(p1.getY()-p2.getY())*pressure)/length);	
			
			if(p2.getY() < p1.getY())  {
				vertices[0] = new Point(p1.x+dx,p1.y+dy);
				vertices[1] = new Point(p2.x+dx,p2.y+dy);
				vertices[3] = new Point(p1.x-dx,p1.y-dy);
				vertices[2] = new Point(p2.x-dx,p2.y-dy);
			} else {
				vertices[0] = new Point(p1.x-dx,p1.y+dy);
				vertices[1] = new Point(p2.x-dx,p2.y+dy);
				vertices[3] = new Point(p1.x+dx,p1.y-dy);
				vertices[2] = new Point(p2.x+dx,p2.y-dy);
			}
		}

		@Override
		protected int lanes() {
			return 2;
		}
		
	}
	/**
	 * Klasa OneWay definiuje odcinek jednokierunkowy
	 *
	 */
	public static class OneWay extends Section {
		private Point b;
		private Point e;
		private ArrayList<Cell> vessel; 
		
		/**
		 * Konstruktor odcinka jednokierunkowego, analogiczny jak przy dwukierunkowym
		 */
		public OneWay(int id, Intersection begin, Intersection end ,int pressure, int type) {
			super(id, begin, end, pressure,type);
			calculateVertices();
			bounds = new Polygon();
			vessel = new ArrayList<Cell>();
			bounds.addPoint(vertices[0].x, vertices[0].y);
			bounds.addPoint(vertices[1].x, vertices[1].y);
			bounds.addPoint(vertices[2].x, vertices[2].y);
			bounds.addPoint(vertices[3].x, vertices[3].y);
			
			b = begin.getPos();
			e = end.getPos();
		}

		@Override
		protected void calculateVertices() {
			int dy = (int) ((Math.abs(p1.getX()-p2.getX())*pressure)/length)/2;
			int dx = (int) ((Math.abs(p1.getY()-p2.getY())*pressure)/length)/2;	
			
			if(p2.getY() < p1.getY())  {
				vertices[0] = new Point(p1.x+dx,p1.y+dy);
				vertices[1] = new Point(p2.x+dx,p2.y+dy);
				vertices[3] = new Point(p1.x-dx,p1.y-dy);
				vertices[2] = new Point(p2.x-dx,p2.y-dy);
			} else {
				vertices[0] = new Point(p1.x-dx,p1.y+dy);
				vertices[1] = new Point(p2.x-dx,p2.y+dy);
				vertices[3] = new Point(p1.x+dx,p1.y-dy);
				vertices[2] = new Point(p2.x+dx,p2.y-dy);
			}
		}

		@Override
		public void draw(Graphics2D g) {
			g.setColor(Settings.SECION_COLOR2);
			g.fill(bounds);
			g.setColor(Color.WHITE);
		}

		@Override
		protected Point getBegin(int i) {
			return b;
		}

		@Override
		protected Point getEnd(int i) {
			return e;
		}
		
		@Override
		protected void addCell(Cell c, int vesselNr) {
			vessel.add(c);
		}
		
		@Override
		protected void rmCell(Cell c, int vesselNr) {
			vessel.remove(c);
		}
		
		@Override
		protected boolean free(Cell o, int vesselNr) {
			boolean free = true;
			synchronized(this) {
				if(vessel.size() > 1) for(Cell c: vessel) {
					if(	o != c && 
						c.getDistance() - o.getDistance() <= (c.getSize()+o.getSize()) &&
						c.getDistance() - o.getDistance() > 0)
							free = false;
				}
			}
			return free;
		}
		
		@Override
		protected int lanes() {
			return 1;
		}
	}
}
