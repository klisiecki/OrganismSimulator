package com.example.pw.objects;
import java.awt.Graphics2D;
import java.awt.Point;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


public class Leukocyte extends Cell {
	private boolean killed;
	
	/**
	 * Tworzy leukocyt o podanym id i pozycji
	 */
	public Leukocyte(int id, Point pos) {
		super(id, pos);
		this.size = Settings.MAX_CELL_SIZE;
		this.bounds = new Circle(pos, size);
	}
	
	/**
	 * Sprawdza czy w pobliżu znajduje się jakiś wirus, i jeśli tak to go zabija
	 */
	private void killViruses() {
		synchronized(OrganismComponent.getViruses()) {
			for(Virus v: OrganismComponent.getViruses()) {
				if(distanceTo(v.getPos()) < Settings.KILL_DISTANCE) {
					v.kill();
					this.kill();
					break;
				}
			}
			if(OrganismComponent.getViruses().size() == 0) {
				OrganismComponent.removeLeukocytes();
			}
		}
	}
	
	public void kill() {
		killed = true;
	}

	@Override
	protected void step() {
		super.step();
		killViruses();
	}
	
	@Override
	public void run() {
		super.run();
		try {
			prevIntersection.lock();
			isLocking = true;
			intersectionLocked = prevIntersection;
			while(!OrganismComponent.OVER) {
				if(OrganismComponent.getViruses().size() > 0) goToOrgan(OrganismComponent.findVirus());
				Thread.sleep(Settings.CELL_SLEEP);
			}
			
		} catch (InterruptedException e) {}

	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Settings.LEUKOCYTE_COLOR);
		g.fill(bounds);
		if (killed) {
			bounds = new Circle(pos, size++);
		}
		
		if (size == 30) {
			delete();
		}
		super.draw(g);
	}

}
