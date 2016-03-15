package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


/**
 *	Klasa organów produkujących białe krwinki
 */
public class LeukocyteProducerOrgan extends Organ {

	/**
	 * Tworzy nowy organ produkujący leukocyty
	 * @param id id obiektu
	 * @param name Nazwa organu
	 * @param x Współrzędna x
	 * @param y Współrzędna y
	 * @param type Typ organu
	 */
	public LeukocyteProducerOrgan(int id, String name, int x, int y, int type) {
		super(id, name, x, y, type);
	}
	
	/**
	 * Tworzy nowy leukocyt w organie
	 */
	public void makeLeukocyte() {
		synchronized(OrganismComponent.getLeukocytes()) {
			if(!this.getLeukocyteIntersection().isLocked() && !dead) {
				Leukocyte l = new Leukocyte(-1, slotPos2);
				l.setPrevIntersection(this.getLeukocyteIntersection());
				OrganismComponent.addLeukocyte(l);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setColor(Settings.LEUKOCYTE_COLOR);
		Circle icon = new Circle(this.pos.x+WIDTH-4,this.pos.y+HEIGHT-4,6);
		g.fill(icon);
		g.setColor(Color.BLACK);
		g.draw(icon);
	}
}
