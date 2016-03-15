package com.example.pw.utils;
import java.awt.Color;

/**
 * Klasa przechowująca globalne ustawienia programu.
 */
public class Settings {

	public static boolean TRANSPARENCY = true;

	// Kolory:
	public static Color BACKGROUND_COLOR = Color.BLACK;
	public static Color INTERSECION_COLOR = Color.CYAN;
	public static Color INTERSECION_COLOR2 = new Color(95, 159, 159);
	public static Color SECION_COLOR = Color.CYAN;
	public static Color SECTION_TRANSPARENT_COLOR = new Color(
			Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(),
			170);
	public static Color SECION_COLOR2 = new Color(95, 159, 159);
	public static Color ORGAN_BACKGROUND = new Color(240, 240, 240);
	public static Color ORGAN_BACKGROUND_BUSY = new Color(220, 220, 220);
	public static Color ORGAN_SLOT = Color.CYAN;
	public static Color ORGAN_SLOT2 = new Color(255, 140, 0);
	public static Color ORGAN_SELECTED = Color.RED;
	public static Color ORGAN_DEAD = Color.RED;
	public static Color ORGAN_DEAD_TRANSPARENT = new Color(Color.RED.getRed(),
			Color.RED.getGreen(), Color.RED.getBlue(), 150);
	public static Color SELECTED_CELL_COLOR = Color.YELLOW;
	public static Color ERYTHROCYTE_COLOR = Color.RED;
	public static Color ERYTHROCYTE_DAMAGED_COLOR = Color.RED.darker();
	public static Color LEUKOCYTE_COLOR = Color.WHITE;
	public static Color VIRUS_COLOR = Color.BLACK;

	// Sleep:
	public static int ORGANISM_SLEEP = 15;
	public static int CELL_SLEEP = 30;
	public static int ORGAN_SLEEP = 30;
	public static int LEUKOCYTE_MAKE_DELAY = 3000;

	public static int MIN_VALUE_MAKE_DELAY = 1000;
	public static int MAX_VALUE_MAKE_DELAY = 4000;

	public static int VIIRUS_DELAY = 1000;
	public static int DAMAGE_DELAY = 2000;

	// Inne:
	public static int MIN_VALUE_LIFE = 50000;
	public static int MAX_VALUE_LIFE = 200000;

	public static int MIN_VALUE_CAPACITY = 10;
	public static int MAX_VALUE_CAPACITY = 30;

	public static int MIN_ORGAN_CAPACITY = 30;
	public static int MAX_ORGAN_CAPACITY = 150;

	public static int MIN_ORGAN_LIST_SIZE = 3;
	public static int MAX_ORGAN_LIST_SIZE = 10;

	public static int MIN_CELL_SIZE = 3;
	public static int MAX_CELL_SIZE = 5;

	public static int VIRUS_SIZE = 5;

	public static int KILL_DISTANCE = 30;
	
	public static double MIN_SCALE = 0.2;
	public static double MAX_SCALE = 5;
}
