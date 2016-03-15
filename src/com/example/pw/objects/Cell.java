package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Clickable;
import com.example.pw.utils.Generator;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


/**
 * Klasa nadrzędna dla wszystkich elementów poruszających się po odcinkach układu krwionośnego i odpornościowego
 */
public abstract class Cell extends Element implements Clickable, Runnable{
	
	//stałe określajace kierunek poruszania się komórki
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;	
	
	/**
	 * Rozmiar będący promieniem danej komórki
	 */
	protected int size;
	
	/**
	 * Aktualna prędkość poruszania się komórki
	 */
	protected int speed;
	
	/**
	 * Kolor komórki
	 */
	protected Color color;
	
	/**
	 * Kształt komórki
	 */
	protected Circle bounds;
	
	/**
	 * Określa czy komórka jest zaznaczona
	 */
	protected boolean selected;
	
	/**
	 * Określa czy komórka jest uszkodzona (nie porusza się)
	 */
	protected boolean damaged;
	
	/**
	 * Określa czy komórka ma status usuniętej 
	 * (aby mogła zwolnić zajmowane zasoby przed faktycznym usunięciem, np. semafor)
	 */
	protected boolean deleted;
	
	/**
	 * Określa czy komórka blokuje jakieś skrzyżowanie
	 */
	protected volatile boolean isLocking;
	
	/**
	 * Skrzyżowanie które jest blokowane
	 */
	protected volatile Intersection intersectionLocked;
	
	/**
	 * Poprzednie skrzyżowanie na drodze komórki
	 */
	protected Intersection prevIntersection;
	
	/**
	 * Skrzyżowanie docelowe komórki
	 */
	protected Intersection targetIntersection;
	
	/**
	 * Odcinek po którym komórka się porusza
	 */
	protected Section currentWay;
	
	/**
	 * Pas odcinka po którym komórka się porusza
	 */
	protected int lane;
	


	/**
	 * Poprzedni punkt na drodze komórki, razem z nextPoint wyznacza prostą
	 * po której komórka się porusza
	 */
	protected Point prevPoint;
	
	/**
	 * Docelowy punkt na drodze komórki, razem z prevPoint wyznacza prostą
	 * po której komórka się porusza
	 */
	protected Point nextPoint;
	
	/**
	 * Odległość komórki od poprzedniego punktu
	 */
	protected int distance;
	
	private int direction;

	/**
	 * Tworzy komórkę o podanym id i pozycji
	 */
	public Cell(int id, Point pos) {
		super(id);
		this.nextPoint = new Point(pos);
		this.prevPoint = new Point(pos);
		this.pos = new Point(pos);
		this.size = Generator.getInt(Settings.MIN_CELL_SIZE,Settings.MAX_CELL_SIZE);
		this.bounds = new Circle(pos, size);
		this.distance = 0;
		calcDirection();
	}
	
	/**
	 * Tworzy komórkę o podanym id, pozycji i rozmiarze
	 */
	public Cell(int id, Point pos, int size) {
		this(id,pos);
		this.size = size;
	}
	
	/**
	 * Funkcja służąca do uszkodzenia (spowodowania awarii) krwinki
	 */
	public void damage() {
		this.damaged = true;
	}
	
	/**
	 * Funkcja służaca do usunięcia krwinki
	 */
	public void delete() {
		this.deleted = true;
	}
	
	/**
	 * Funkcja służąca do sprawdzenia czy komórka ma status usuniętej
	 */
	public boolean isDeleted() {
		return deleted;
	}
	
	/**
	 * Funkcja wyznaczająca kierunek poruszania się komórki
	 */
	protected void calcDirection() {
		int dx = nextPoint.x-pos.x;
		int dy = nextPoint.y-pos.y;
		if(Math.abs(dx) > Math.abs(dy)) {
			if(dx > 0) this.direction = RIGHT;
			else this.direction = LEFT;
		} else {
			if(dy > 0) this.direction= DOWN;
			else this.direction = UP;
		}
	}
	
	/**
	 * Funkcja przemieszczająca krwinkę o jeden piksel
	 */
	protected void step() {
		while (distanceTo(prevPoint) < distance)
		switch (direction) {
		case RIGHT:
			pos.x++;
			pos.y = fx(pos.x);
			break;
		case LEFT:
			pos.x--;
			pos.y = fx(pos.x);
			break;
		case DOWN:
			pos.y++;
			pos.x = fy(pos.y);
			break;
		case UP:
			pos.y--;
			pos.x = fy(pos.y);
			break;
		default:
			System.out.println("Nieznany typ!");
		}
		bounds.setPos(pos);
	}
	
	private int fx(int x) {
		return fx(x,prevPoint.x,prevPoint.y,nextPoint.x,nextPoint.y);
	}
	private int fy(int y) {
		return fy(y,prevPoint.x,prevPoint.y,nextPoint.x,nextPoint.y);
	}
	
	private int fx(final int x, final int x1,final int y1,final int x2,final int y2) {
		return (x*y1-x*y2+x1*y2-x2*y1) / (x1-x2);
	}
	private int fy(final int y, final int x1,final int y1,final int x2,final int y2) {
	    return (((x2-x1)*(y-y1)) / (y2-y1))+x1 ;
	}
	
	/**
	 * Funkcja przesuwająca komórkę do podanego punktu
	 * @param p punkt docelowy
	 * @param d odległość od punktu w której komórka się zatrzyma
	 * @param unlock przyjmuje wartość true jeśli trzeb po drodze zwolnić semafor
	 * @param when kiedy (po jakiej przebytej odległości) zwolnić semafor
	 * @param check true jeśli trzeba sprawdzać odległość od innych komórek na tym samym pasie
	 * @throws InterruptedException
	 */
	protected void moveTo(Point p, double d, boolean unlock, int when,boolean check) throws InterruptedException{
		prevPoint = new Point(pos);
		nextPoint = new Point(p);
		distance = 0;
		calcDirection();
		while(distanceTo(nextPoint) > d) {
			if(OrganismComponent.OVER) return;
			if(check) while(!currentWay.free(this,lane)){
				Thread.sleep(Settings.CELL_SLEEP);
				if(deleted) break;
			}
			
			if(deleted) {
				synchronized(currentWay) {currentWay.rmCell(this, lane);}
				if(isLocking && intersectionLocked != null) intersectionLocked.unlock();
				OrganismComponent.removeCell(this);
				Thread.currentThread().interrupt();
				while(true) Thread.sleep(Settings.CELL_SLEEP);
			}
			while(damaged) {
				if(deleted) break;
				Thread.sleep(Settings.CELL_SLEEP);
			}
			distance++;
			if(unlock && distance == when && prevIntersection.isLocked()) {
				prevIntersection.unlock();
				isLocking = false;
			}
			step();
			Thread.sleep(Settings.CELL_SLEEP-speed);
			
		}
	}
	
	/**
	 * Funkcja przemieszczająca komórkę do podanego skrzyżowania
	 * @param nr numer skrzyżowania
	 * @throws InterruptedException
	 */
	protected void goToIntersection(int nr) throws InterruptedException{
		if(OrganismComponent.OVER) return;
		//docelowe skrzyżowanie:
		targetIntersection = prevIntersection.getIntersections().get(nr);
		//droga po której będziemy się poruszać:
		currentWay = prevIntersection.getSections().get(nr);
		//pas po którym będziemy się poruszać:
		if(currentWay.lanes() == 2) lane = distanceTo(currentWay.getBegin(0)) < distanceTo(currentWay.getBegin(1)) ? 0 : 1;
		else lane = 0;
		synchronized(currentWay) { currentWay.addCell(this, lane); }
		this.speed = (currentWay.pressure-9)*3;
		//do początku aktualnej drogi
		moveTo(currentWay.getBegin(lane),1,false,0,false);
		//do końca drogi
		moveTo(currentWay.getEnd(lane),25,true,20,true);
		//wejście na skrzyżowanie
		targetIntersection.lock();
		isLocking = true;
		intersectionLocked = targetIntersection;
		synchronized(currentWay) { currentWay.rmCell(this, lane); }
		//do środka skrzyżowania
		moveTo(targetIntersection.getPos(),1,false,0,false);
		prevIntersection = targetIntersection;
	}
	
	/**
	 * Funkcja przemieszczająca komórkę do organu
	 * @param o docelowy organ
	 * @throws InterruptedException
	 */
	protected void goToOrgan(Organ o) throws InterruptedException {
		if(OrganismComponent.OVER) return;
		ArrayList<Intersection> intersections;
		do {
			intersections = new ArrayList<Intersection>(prevIntersection.getIntersections());
			int wayNumber = 0;
			for(int i = 1; i < intersections.size(); i++) if(intersections.get(i).getId() > 11) wayNumber = i;
			double d1,d2;
			for(int i = 0; i < intersections.size(); i++) {
				d1 = intersections.get(i).getPos().distance(o.getPos());
				d2 = intersections.get(wayNumber).getPos().distance(o.getPos());
				if(intersections.get(i).getId() == o.getId()) {
					wayNumber = i;
					break;
				} else 	if((d1 < d2) && (intersections.get(i).getId() > 11)) 
					wayNumber = i;	
			}
			goToIntersection(wayNumber);
		} while (prevIntersection.getId() != o.getId()); //dopóki nie dotarł
	}
	
	/**
	 * Zwraca odległość komórki do danego punktu
	 * @param p współrzędne punktu
	 * @return
	 */
	public int distanceTo(Point p) {
		return (int) pos.distance(p);
	}
	
	/**
	 * Funkcja ustawia kolejny docelowy punkt komórki
	 */
	public void setNextPoint(Point nextPoint) {
		this.nextPoint = nextPoint;
		calcDirection();
	}
	
	public Point getNextPoint() {
		return nextPoint;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Intersection getPrevIntersection() {
		return prevIntersection;
	}

	public void setPrevIntersection(Intersection prevIntersection) {
		this.prevIntersection = prevIntersection;
		
	}

	@Override
	public void run() {
	}

	@Override
	public void draw(Graphics2D g) {
		if(selected) {
			g.setColor(Settings.SELECTED_CELL_COLOR);
			g.draw(bounds);
		}
	}

	@Override
	public boolean contains(Point p) {
		return bounds.contains(p);
	}

	@Override
	public void setSelected(boolean isSelected) {
		// TODO Auto-generated method stub
		selected = isSelected;
	}
}
