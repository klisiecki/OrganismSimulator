package com.example.pw.objects;
import java.awt.Color;
import java.awt.Graphics2D;

import com.example.pw.utils.Circle;
import com.example.pw.utils.Settings;
import com.example.pw.views.OrganismComponent;


public class ErythrocyteProducerOrgan extends Organ {
	
	/**
	 * Tworzy organ produkujący erytrocyty
	 */
	public ErythrocyteProducerOrgan(int id, String name, int x, int y, int type) {
		super(id, name, x, y, type);
	}
	
	/**
	 * Tworzy nowy erytrocyt w organie
	 */
	public void makeErythrocyte() {
		if(!dead) {
			Erythrocyte e = new Erythrocyte(-1, slotPos);
			e.setPrevIntersection(this);
			OrganismComponent.addErythrocyte(e);
		}
	}
	
	@Override
	public void run() {
		super.run();
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setColor(Settings.ERYTHROCYTE_COLOR);
		Circle icon = new Circle(this.pos.x+WIDTH-4,this.pos.y+HEIGHT-4,6);
		g.fill(icon);
		g.setColor(Color.BLACK);
		g.draw(icon);
	}

}
