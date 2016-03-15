package com.example.pw.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Generator;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;

/**
 * Klasa definiująca wirusa
 *
 */
public class Virus extends Cell {
	private Shape shape;
	private Organ target;
	private boolean killed = false;
	private int damaged = 0;

	/**
	 * Tworzy wirusa o podanych parametrach
	 * 
	 * @param id
	 *            id wirusa
	 * @param pos
	 *            pozycja w której tworzony jest wirus
	 * @param intersection
	 *            skrzyżowanie w którym tworzony jest wirus
	 */
	public Virus(int id, Point pos, Intersection intersection) {
		super(id, pos);
		this.bounds = new Circle(pos, Settings.VIRUS_SIZE);
		this.shape = createStar(6, pos, Settings.VIRUS_SIZE,
				Settings.VIRUS_SIZE - 2);
		this.prevIntersection = intersection;
		this.size = Settings.MAX_CELL_SIZE - 1;
	}

	private void newTarget() {
		Organ o;
		do {
			int nr = Generator.getInt(0, OrganismComponent.ORGANS_COUNT - 1);
			o = OrganismComponent.getOrgan(nr);

		} while (o.isDead());
		target = o;
	}

	@Override
	protected void goToOrgan(Organ o) throws InterruptedException {
		super.goToOrgan(o);
		o.removeAllValues();
		int time = Generator.getInt(1, 30);
		while (time-- >= 0) {
			synchronized (o) {
				o.removeHealth(1);
			}
			Thread.sleep(5 * Settings.CELL_SLEEP);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		this.shape = createStar(6, pos, Settings.VIRUS_SIZE - damaged,
				Settings.VIRUS_SIZE - 2 - damaged);
		g.setColor(Settings.VIRUS_COLOR);
		g.fill(shape);
		super.draw(g);
		if (killed)
			damaged++;
		if (damaged == Settings.VIRUS_SIZE)
			deleted = true;
	}

	@Override
	public void run() {
		super.run();
		try {
			prevIntersection.lock();
			isLocking = true;
			intersectionLocked = prevIntersection;
			while (!OrganismComponent.OVER) {

				newTarget();

				goToOrgan(target);
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Funkcja tworząca gwiazdę, kod skopiowany z:
	 * http://stackoverflow.com/questions
	 * /2710065/drawing-star-shapes-with-variable-parameters
	 */
	public static Shape createStar(int arms, Point center, double rOuter,
			double rInner) {
		double angle = Math.PI / arms;
		GeneralPath path = new GeneralPath();

		for (int i = 0; i < 2 * arms; i++) {
			double r = (i & 1) == 0 ? rOuter : rInner;
			Point2D.Double p = new Point2D.Double(center.x
					+ Math.cos(i * angle) * r, center.y + Math.sin(i * angle)
					* r);
			if (i == 0)
				path.moveTo(p.getX(), p.getY());
			else
				path.lineTo(p.getX(), p.getY());
		}
		path.closePath();
		return path;
	}

	public void kill() {
		killed = true;
	}

	public Organ getTarget() {
		return target;
	}

	public void setTarget(Organ target) {
		this.target = target;
	}
}
