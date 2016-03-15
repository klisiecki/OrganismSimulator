package com.example.pw.utils;
import java.awt.Point;


/**
 * Interfejs dla obiektów umożliwiających kliknięcie myszką w celu wyświetlenia dodatkowych informacji
 *
 */
public interface Clickable {
	/**
	 * Funkcja sprawdzająca czy dany punkt zawiera się w granicach elementu.
	 * @param p współrzędne punktu 
	 * @return wartorść true gdy punkt zawiera się w obszarze, false w przeciwnym wypadku.
	 */
	public boolean contains(Point p);
	
	/**
	 * Funkcja zaznaczająca dany obiekt
	 */
	public void setSelected(boolean isSelected);
}
