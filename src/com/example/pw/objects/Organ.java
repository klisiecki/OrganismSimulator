package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Clickable;
import com.example.pw.utils.Generator;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


public class Organ extends Intersection implements Clickable, Runnable {
	/**
	 * Liczba typów wartości odżywczych
	 */
	public static final int VALUE_TYPES = 14;
	private static final String[] valueTypes = {	"Tlen","Dwutlenek Węgla",
													"Cukier","Tłuszcz",
													"Białko","Hormon",
													"Witamina A","Witamina B1",
													"Witamina C","Witamina D",
													"Witamina B2","Witamina B3",
													"Witamina K","Witamina B12",
													};

	private static final int[] valueStarts = {		0,3,
													9,6,
													5,7,
													7,8,
													1,9,
													4,10,
													2,11
													};

	private static final int[] valueTargets = {		3,0,
													2,1,
													8,4,
													6,5,
													7,8,
													10,9,
													11,4
													};
	
	private static final Color[] valueColors = { 	Color.YELLOW, Color.BLACK,
													Color.WHITE,Color.YELLOW.darker(),
													Color.CYAN,Color.RED,
													Color.LIGHT_GRAY,Color.MAGENTA,
													Color.RED.darker(),Color.BLUE,
													Color.DARK_GRAY,Color.ORANGE,
													Color.BLUE.darker(),Color.GREEN};
	
	private static final Point[] slotShift = {		new Point(15,0),
													new Point(20,0),
													new Point(-15,0),
													new Point(0,15),
													new Point(0,20),
													new Point(0,-15),
													new Point(0,15),
													new Point(0,-20),
													new Point(0,-15),
													new Point(15,0),
													new Point(-20,0),
													new Point(-15,0)
														};
	
	/**
	 * Szerokość organu
	 */
	protected static final int WIDTH = 100;
	/**
	 * Wysokość organu
	 */
	protected static final int HEIGHT = 100;
	
	//typy organu w zależności od położenia 
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;		
	
	private int type;
	private String name;
	
	/**
	 * Typy produkowanych wartości
	 */
	protected ArrayList<Integer> valuesProduced;
	/**
	 * Typy otrzymywanych wartośći
	 */
	protected ArrayList<Integer> valuesReceived;
	
	/**
	 * Wartości odżywcze wyprodukowane w organie
	 */
	protected ArrayList<Value> valuesProducedInside;
	
	/**
	 * Wartości odżywcze dostarczone do organu
	 */
	protected ArrayList<Value> valuesReceivedInside;
	
	/**
	 * Punkt od którego rysowane są wartości odżywcze
	 */
	protected Point valuesPoint;
	
	/**
	 * Punkt w którym rysowana jest nazwa organu
	 */
	protected Point titlePoint;

	/**
	 * Współrzędne punktu połączenia z układem krwionośnym
	 */
	protected Point slotPos;
	
	/**
	 * Współrzędne punktu połączenia z układem krwionośnym
	 */
	protected Point slotPos2;
	

	private Intersection leukocyteIntersection;
	private int health;

	private Rectangle bounds;
	private Circle slot;
	
	/**
	 * Określa czy organ jest żywy (ma dodatni stan zdrowia)
	 */
	protected boolean dead;
	private boolean selected;
	private int capacity;
	
	
	/**
	 * @param name Nazwa organu
	 * @param x	Współrzędna x lewego górnego rogu
	 * @param y	Współrzędna y lewego górnego rogu
	 */
	public Organ(int id, String name, int x, int y, int type) {
		super(id, new Point(x,y),0);
		this.name = name;
		this.pos = new Point(x,y);
		this.type = type;
		this.health = 100;
		this.bounds = new Rectangle(x,y,WIDTH,HEIGHT);
		this.selected = false;
		this.capacity = Generator.getInt(Settings.MIN_ORGAN_CAPACITY, Settings.MAX_ORGAN_CAPACITY);
		if(capacity%2 != 0) capacity++;
		
		//ustalenie położenia slotu w zależności od pozycji organu
		switch (type) {
		case UP:
			this.slotPos = new Point(x+(WIDTH/2),y+HEIGHT);
			break;
		
		case LEFT:
			this.slotPos = new Point(x+(WIDTH),y+(HEIGHT/2));
			break;
		
		case RIGHT:
			this.slotPos = new Point(x,y+(HEIGHT/2));
			break;
		
		case DOWN:
			this.slotPos = new Point(x+(WIDTH/2),y);
			break;

		default:
			break;
		}
		
		if(type == DOWN) {
			this.titlePoint = new Point(pos.x,pos.y+HEIGHT-20);
			this.valuesPoint = new Point(pos);
		} else {
			this.titlePoint = new Point(pos);
			this.valuesPoint = new Point(pos.x,pos.y+20);
		}
		
		slotPos2 = new Point(slotPos);
		slotPos.move(slotPos.x-slotShift[id].x, slotPos.y-slotShift[id].y);
		slotPos2.move(slotPos2.x+slotShift[id].x, slotPos2.y+slotShift[id].y);

		leukocyteIntersection = new Intersection(this.id, slotPos2, 1);

		initializeValues();
		slot = new Circle(slotPos,12);
	}
	

	

	
	/**
	 * Zwraca nazwy produkowanych wartości odżywczych
	 */
	public String getProducedNames() {
		String s = "";
		for (int v: valuesProduced) s += valueTypes[v] +",";
		return s;
	}
	
	/**
	 * Zwraca nazwy przyjmowanych wartości odżywczych
	 */
	public String getReceivedNames() {
		String s = "";
		for (int v: valuesReceived) s += valueTypes[v] +",";
		return s;
	}
	
	
	private void initializeValues() {
		valuesProduced = new ArrayList<Integer>();
		valuesReceived = new ArrayList<Integer>();
		
		valuesProducedInside = new ArrayList<Value>();
		valuesReceivedInside = new ArrayList<Value>();
		
		for(int i = 0; i < VALUE_TYPES; i++) if(valueStarts[i] == this.id) valuesProduced.add(i); 
		for(int i = 0; i < VALUE_TYPES; i++) if(valueTargets[i] == this.id) valuesReceived.add(i); 
	}
	
	/**
	 * Zwraca typy produkowanych wartości odżywczych
	 */
	public ArrayList<Integer> getValuesProduced() {
		return this.valuesProduced;
	}
	
	/**
	 * Zwraca typy przyjmowanych wartości odżywczych
	 */
	public ArrayList<Integer> getValuesReceived() {
		return this.valuesReceived;
	}

	public void draw(Graphics2D g) {
		//podświetlenie organu gdy kliknięty
		if(selected) { 
			g.setColor(Settings.ORGAN_SELECTED);
			g.fillRect(pos.x-1, pos.y-1, WIDTH+3, HEIGHT+3);
		}
		
		//rysowanie slotu
		g.setColor(Settings.ORGAN_SLOT);
		g.fill(slot);
		
		//rysowanie tła
		if(!this.isLocked()) g.setColor(Settings.ORGAN_BACKGROUND);
		else g.setColor(Settings.ORGAN_BACKGROUND_BUSY);
		g.fillRect(pos.x, pos.y, WIDTH, HEIGHT);
		
		
		//rysowanie paska życia
		if(health < 75 && health >= 50) g.setColor(Color.YELLOW);
		else if (health < 50 && health >= 25) g.setColor(Color.ORANGE);
		else if (health < 25) g.setColor(Color.RED);
		else g.setColor(Color.GREEN);
		g.fillRect(titlePoint.x, titlePoint.y, health, 20);
		//rysowanie nazwy
		g.setColor(Color.BLACK);
		g.drawString(name, titlePoint.x+5, titlePoint.y+15);

		//ramka tytułowa
		g.drawRect(titlePoint.x, titlePoint.y, WIDTH, 20);
		//główny zarys (prostokąt) organu
		g.draw(bounds);	
		g.drawLine(valuesPoint.x+(WIDTH/2), valuesPoint.y, valuesPoint.x+(WIDTH/2), valuesPoint.y+HEIGHT-20);
		
		//rysowanie wartości odżywczych

		synchronized(this) {		
			int x,y;
			x = y = 5;
			for(Value v: valuesProducedInside) {
				Circle c = new Circle(valuesPoint.x+x,valuesPoint.y+y,2);
				g.setColor(v.getColor());
				g.fill(c);
				x += 5;
				if (x >= (WIDTH / 2)-5) {
					x = 5;
					y += 5;
				}
			}
			x = WIDTH/2 + 5;
			y = 5;
			for(Value v: valuesReceivedInside) {
				Circle c = new Circle(valuesPoint.x+x,valuesPoint.y+y,2);
				g.setColor(v.getColor());
				g.fill(c);
				x += 5;
				if (x >= WIDTH -5) {
					x = WIDTH/2 + 5;
					y += 5;
				}
			}
		}
		
		if(dead) {
			g.setColor(Settings.ORGAN_DEAD);
			if(Settings.TRANSPARENCY) g.setColor(Settings.ORGAN_DEAD_TRANSPARENT);
			g.fill(bounds);
		}
	}
	
	/**
	 * Pobiera wartości odżywcze z organu
	 * @param types typy wartości do pobrania
	 * @param count liczba wartości do pobrania
	 * @return wartości które udało się pobrać
	 */
	public ArrayList<Value> takeValues(ArrayList<Integer> types, int count) {
		ArrayList<Value> taken = new ArrayList<Value>();
		synchronized(this) {
			for(Value v: valuesProducedInside) {
				if(types.contains(v.getTypeId())) {
					taken.add(v);
					if(taken.size() == count) break;
				}
			}
			valuesProducedInside.removeAll(taken);
		}
		return taken;
	}
	
	/**
	 * Zostawia wartości odżywcze w organie
	 * @param values wartości do zostawienia
	 * @return wartości które udało się zostawić
	 */
	public ArrayList<Value> leaveValues(ArrayList<Value> values) {
		ArrayList<Value> leaved = new ArrayList<Value>();
		synchronized(this) {
			for(Value v: values) {
				if(this.valuesReceived.contains(v.getTypeId()) &&
					leaved.size()+valuesReceivedInside.size() < capacity/2) {
						leaved.add(v);
						valuesReceivedInside.add(v);
						this.addHealth(1);
				}
			}
			return leaved;
		}
	}
	
	/**
	 * Usuwa wszystkie wartości odżywcze z organu
	 */
	public void removeAllValues() {
		synchronized(this) {
			valuesProducedInside.clear();
			valuesReceivedInside.clear();
		}
	}
	
	/**
	 * Produkuje nową wartość odżywczą
	 */
	public void makeValue() {
		if(this.valuesProducedInside.size() < capacity/2 && !dead) {
			int i = Generator.getInt(0, valuesProduced.size()-1);
			Value v = new Value(valueTypes[valuesProduced.get(i)], 
					valuesProduced.get(i),
					OrganismComponent.ID++, 
					Generator.getInt(Settings.MIN_VALUE_LIFE, Settings.MAX_VALUE_LIFE), 
					valueColors[valuesProduced.get(i)]);
			valuesProducedInside.add(v);
		}
	}

	/**
	 * Usuwa przestarzałe wartości odżywcze
	 */
	protected void removeOldValues() {
		long time = System.currentTimeMillis();
		ArrayList<Value> old = new ArrayList<Value>();
		for(Value v: valuesProducedInside) {
			if((time - v.getBorntime()) > v.getLifeLenght()) old.add(v);
		}
		valuesProducedInside.removeAll(old);
		old.clear();
		
		for(Value v: valuesReceivedInside) {
			if((time - v.getBorntime()) > v.getLifeLenght()) old.add(v);
		}
		
		valuesReceivedInside.removeAll(old);
		old.clear();
	}
	
	@Override
	public void run() {
		int i = 0;
		while(!OrganismComponent.OVER) {
			if(dead) return;
			synchronized(this) {
				if(valuesProduced.size()>0 && valuesProducedInside.size() < capacity) makeValue();
				i++;
				if(i%30 == 0) removeOldValues();
			}
			try {
				Thread.sleep(Generator.getInt(Settings.MIN_VALUE_MAKE_DELAY,Settings.MAX_VALUE_MAKE_DELAY));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean contains(Point p) {
		return bounds.contains(p);
	}

	public boolean isSelected() {
		return selected;
	}
	
	public boolean isDead() {
		return dead;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public Intersection getLeukocyteIntersection() {
		return leukocyteIntersection;
	}

	public void setLeukocyteIntersection(Intersection leukocyteIntersection) {
		this.leukocyteIntersection = leukocyteIntersection;
	}

	public int getValuesProducedCount() {
		return this.valuesProducedInside.size();
	}
	
	public int getValuesReceivedCount() {
		return this.valuesReceivedInside.size();
	}

	public void setHealth(int health) {
		if(health > 100) this.health = 100; else
		if(health < 0) this.health = 0; else
		this.health = health;
		if(this.health ==0 && !dead) {
			dead = true;
			OrganismComponent.organDied();
		}
	}
	
	/**
	 * Dodaje zdrowie
	 */
	public void addHealth(int i) {
		this.setHealth(this.getHealth()+i);
	}
	
	
	/**
	 * Odejmuje zdrowie
	 */
	public void removeHealth(int i) {
		this.setHealth(this.getHealth()-i);
	}
	
	@Override
	public Point getPos() {
		return this.slotPos;
	}
	
	/**
	 * Zwraca pozycję skrzyżowania układu odpornościowego
	 */
	public Point getPos2() {
		return this.slotPos2;
	}
	@Override
	public String toString() {
		return "Name = "+name+", id = "+id;
	}

}
