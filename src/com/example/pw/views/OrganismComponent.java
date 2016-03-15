package com.example.pw.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.example.pw.objects.Cell;
import com.example.pw.objects.Erythrocyte;
import com.example.pw.objects.ErythrocyteProducerOrgan;
import com.example.pw.objects.Intersection;
import com.example.pw.objects.Leukocyte;
import com.example.pw.objects.LeukocyteProducerOrgan;
import com.example.pw.objects.Organ;
import com.example.pw.objects.Section;
import com.example.pw.objects.Virus;
import com.example.pw.utils.Generator;
import com.example.pw.utils.Result;
import com.example.pw.utils.Settings;

/**
 * Klasa definiująca komponent wyświetlający symulację
 */
public class OrganismComponent extends JComponent implements MouseListener,
		MouseMotionListener, MouseWheelListener, Runnable {
	/**
	 * Liczba organów
	 */
	public static final int ORGANS_COUNT = 12;
	/**
	 * Liczba skrzyżowań układu krwionośnego
	 */
	public static final int INTERSECTIONS_COUNT = 5;
	/**
	 */
	public static final int INTERSECTIONS_COUNT2 = 5;
	/**
	 * Liczba odcinków układu krwionośnego
	 */
	public static final int SECTIONS_COUNT = 22;
	/**
	 * Liczba odcinków układu odpornościowego
	 */
	public static final int SECTIONS_COUNT2 = 20;

	private static InformationPanelFrame informationPanel;
	private static ControlPanelFrame controlPanel;
	private static Organ[] organs;
	private static ArrayList<Erythrocyte> erythrocytes;
	private static ArrayList<Leukocyte> leukocytes;
	private static ArrayList<Virus> viruses;

	/**
	 * Skrzyżowania w układzie krwionośnym
	 */
	private static Intersection[] intersections;

	/**
	 * Odcinki w układzie odpornościowym
	 */
	private static Intersection[] intersections2;

	/**
	 * Skrzyżowania w układzie krwionośnym
	 */

	private static Section[] sections;
	/**
	 * Odcinki w układzie odpornościowym
	 */

	private static Section[] sections2;

	private static Cell selectedCell;
	private static Organ selectedOrgan;

	/**
	 * Globalne id dla wszystkich obiektów
	 */
	public static int ID = 0;

	/**
	 * Zmienna określająca czy symulacja została rozpoczęta
	 */
	public static boolean RUNNING;
	/**
	 * Zmienna określająca czy symulacja została zakończona
	 */
	public static boolean OVER;

	private static int organsAlive;
	private static long startTime;
	private static long simulationTime = 0;
	private static long lastLeukocyteTime;
	private static int virusDelay;
	private static int damageDelay;

	// zmienne do obsługi skalowania
	private boolean isDragging = false;
	private double scale = 1.0;
	private int lastX, lastY;
	private int componentX = 0, componentY = 0;

	private final String fileName = "results";
	private String playerName;
	private ArrayList<Result> results;

	private static final String[] organNames = { "Nos", // 0
			"Mózg", // 1
			"Oczy", // 2
			"Płuca", // 3
			"Grasica", // 4
			"Tarczyca", // 5
			"Skóra", // 6
			"Śledziona", // 7
			"Mięsień", // 8
			"Wątroba", // 9
			"Szpik Kostny", // 10
			"Nerki" }; // 11

	private static final int[] xOrganPositions = { 225, 400, 575, // up
			25, 25, 25, // left
			775, 775, 775, // right
			225, 400, 575 }; // down

	private static final int[] yOrganPositions = { 25, 25, 25, // up
			150, 300, 450, // left
			150, 300, 450, // right
			575, 575, 575 }; // down

	private static final int[] organTypes = { Organ.UP, Organ.UP, Organ.UP,
			Organ.LEFT, Organ.LEFT, Organ.LEFT, Organ.RIGHT, Organ.RIGHT,
			Organ.RIGHT, Organ.DOWN, Organ.DOWN, Organ.DOWN };

	private static final Point[] intersectionPositions = { new Point(300, 275),
			new Point(600, 275), new Point(450, 325), new Point(300, 425),
			new Point(600, 425) };

	private static final Point[] intersectionPositions2 = {
			new Point(470, 225), new Point(250, 350), new Point(650, 350),
			new Point(450, 475), new Point(470, 375) };

	private static final int erythrocyteProducer = 10;
	private static final int[] leukocyteProducers = { 4, 7 };

	private static final int[] sectionPressure = { 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 14, 14, 14, 14, 12 };

	private static final int[] sectionPressure2 = { 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, };

	private static final int[] sectionBegins = { -1, -2, -2, -3, -7, -8, -8,
			-9, -12, -11, -11, -10, -6, -5, -5, -4, 1, 1, 4, 2, 5, 2 };
	private static final int[] sectionEnds = { 1, 1, 2, 2, 2, 2, 5, 5, 5, 5, 4,
			4, 4, 4, 1, 1, 4, 3, 3, 3, 3, 5 };

	private static final int[] sectionBegins2 = { -1, -2, -3, -4, -5, -6, -7,
			-8, -9, -10, -11, -12, 2, 2, 3, 3, 2, 5, 4, 5 };
	private static final int[] sectionEnds2 = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4,
			4, 4, 1, 4, 1, 4, 5, 3, 5, 1 };

	private static final int twoWays = 15;

	/**
	 * Tworzy organizm
	 */
	public OrganismComponent() {
		initializeOrgans();
		initializeIntersections();
		initializeSections();

		erythrocytes = new ArrayList<Erythrocyte>();
		leukocytes = new ArrayList<Leukocyte>();
		viruses = new ArrayList<Virus>();
		selectedCell = null;
		RUNNING = OVER = false;
		virusDelay = Settings.VIIRUS_DELAY;
		damageDelay = Settings.DAMAGE_DELAY;
		organsAlive = ORGANS_COUNT;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	private void readResults() throws IOException {
		if ((new File(fileName)).exists()) {
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(fileName)));
			results = (ArrayList<Result>) d.readObject();
		} else {
			results = new ArrayList<Result>();
		}
	}

	private void saveResult(Result result) throws IOException {
		results.add(result);
		Collections.sort(results);
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(fileName)));
		e.writeObject(results);
		e.close();
	}

	private void initializeOrgans() {
		// inicjalizacja organów:
		organs = new Organ[ORGANS_COUNT];
		for (int i = 0; i < ORGANS_COUNT; i++) {
			if (!isErythrocyteProducer(i) && !isLeukocyteProducer(i))
				organs[i] = new Organ(ID++, organNames[i], xOrganPositions[i],
						yOrganPositions[i], organTypes[i]);
			else if (!isLeukocyteProducer(i))
				organs[i] = new ErythrocyteProducerOrgan(ID++, organNames[i],
						xOrganPositions[i], yOrganPositions[i], organTypes[i]);
			else
				organs[i] = new LeukocyteProducerOrgan(ID++, organNames[i],
						xOrganPositions[i], yOrganPositions[i], organTypes[i]);
		}
	}

	private void startOrgans() {
		for (Organ o : organs) {
			Thread t = new Thread(o);
			t.start();
		}
	}

	/**
	 * Zwraca czas po którym będzie można stworzyć nowy leukocyt
	 */
	public static long nextLeukocyteTime() {
		if (System.currentTimeMillis() - lastLeukocyteTime < Settings.LEUKOCYTE_MAKE_DELAY)
			return System.currentTimeMillis() - lastLeukocyteTime;
		return 0;
	}

	/**
	 * Zmniejsza liczbę żywych organów
	 */
	public static void organDied() {
		organsAlive--;
	}

	private boolean isErythrocyteProducer(int i) {
		return i == erythrocyteProducer;
	}

	private boolean isLeukocyteProducer(int i) {
		for (int nr : leukocyteProducers)
			if (i == nr)
				return true;
		return false;
	}

	private void initializeIntersections() {
		// inicjalizacja skrzyżowań:
		intersections = new Intersection[INTERSECTIONS_COUNT];
		for (int i = 0; i < INTERSECTIONS_COUNT; i++) {
			intersections[i] = new Intersection(ID++, intersectionPositions[i],
					0);
		}

		intersections2 = new Intersection[INTERSECTIONS_COUNT2];

		for (int i = 0; i < INTERSECTIONS_COUNT2; i++) {
			intersections2[i] = new Intersection(ID++,
					intersectionPositions2[i], 1);
		}
	}

	private void initializeSections() {
		// inicjalizacja odcinków:
		sections = new Section[SECTIONS_COUNT];
		for (int i = 0; i < SECTIONS_COUNT; i++) {
			sections[i] = new Section.TwoWay(ID++,
					getIntersection(sectionBegins[i]),
					getIntersection(sectionEnds[i]), sectionPressure[i], 0);
		}

		for (int i = 0; i < SECTIONS_COUNT; i++) {
			getIntersection(sectionBegins[i]).addSection(sections[i]);
			getIntersection(sectionBegins[i]).addIntersection(
					getIntersection(sectionEnds[i]));

			getIntersection(sectionEnds[i]).addSection(sections[i]);
			getIntersection(sectionEnds[i]).addIntersection(
					getIntersection(sectionBegins[i]));
		}

		sections2 = new Section[SECTIONS_COUNT2];
		for (int i = 0; i < SECTIONS_COUNT2; i++) {
			if (i <= twoWays)
				sections2[i] = new Section.TwoWay(ID++,
						getIntersection2(sectionBegins2[i]),
						getIntersection2(sectionEnds2[i]), sectionPressure[i],
						1);
			else
				sections2[i] = new Section.OneWay(ID++,
						getIntersection2(sectionBegins2[i]),
						getIntersection2(sectionEnds2[i]), sectionPressure2[i],
						1);
		}

		for (int i = 0; i < SECTIONS_COUNT2; i++) {
			getIntersection2(sectionBegins2[i]).addSection(sections2[i]);
			getIntersection2(sectionBegins2[i]).addIntersection(
					getIntersection2(sectionEnds2[i]));

			if (i <= twoWays) {
				getIntersection2(sectionEnds2[i]).addSection(sections2[i]);
				getIntersection2(sectionEnds2[i]).addIntersection(
						getIntersection2(sectionBegins2[i]));
			}
		}
	}

	/**
	 * Zwraca żadane skrzyżowanie układu krwionośnego
	 * 
	 * @param i
	 *            Numer skrzyżowania. Ujemny oznacza organ o numerze -i
	 * @return
	 */
	public Intersection getIntersection(int i) {
		return i > 0 ? intersections[i - 1] : organs[-i - 1];
	}

	/**
	 * Zwraca żadane skrzyżowanie układu odpornościowego
	 * 
	 * @param i
	 *            Numer skrzyżowania. Ujemny oznacza organ o numerze -i
	 * @return
	 */
	public Intersection getIntersection2(int i) {
		return i > 0 ? intersections2[i - 1] : organs[-i - 1]
				.getLeukocyteIntersection();
	}

	/**
	 * Zwraca organ o podanym numerze
	 */
	public static Organ getOrgan(int i) {
		return organs[i];
	}

	/**
	 * Produkuje nowy erytrocyt w odpowiednim organie
	 */
	public static void makeErythrocyte() {
		synchronized (erythrocytes) {
			if (!((ErythrocyteProducerOrgan) organs[10]).isLocked())
				((ErythrocyteProducerOrgan) organs[10]).makeErythrocyte();
		}
	}

	/**
	 * Produkuje nowy leukocyt w losowym organie (spośród tych produkujących
	 * leukocyty)
	 */
	public static void makeLeukocyte() {
		if (System.currentTimeMillis() - lastLeukocyteTime > Settings.LEUKOCYTE_MAKE_DELAY
				&& viruses.size() > 0)
			synchronized (leukocytes) {
				lastLeukocyteTime = System.currentTimeMillis();
				int nr = Generator.getInt(0, leukocyteProducers.length - 1);
				LeukocyteProducerOrgan o = (LeukocyteProducerOrgan) organs[leukocyteProducers[nr]];
				o.makeLeukocyte();
			}
	}

	/**
	 * Tworzy wirusa w organiźmie
	 */
	public static void makeVirus() {
		synchronized (viruses) {
			int nr = Generator.getInt(0, INTERSECTIONS_COUNT2 - 1);
			if (!intersections2[nr].isLocked()) {
				Virus v = new Virus(ID++, intersectionPositions2[nr],
						intersections2[nr]);
				viruses.add(v);
				Thread t = new Thread(v);
				t.start();
				controlPanel.setVirusesCount(viruses.size());
			}
		}
	}

	public static ArrayList<Leukocyte> getLeukocytes() {
		return leukocytes;
	}

	public static ArrayList<Virus> getViruses() {
		return viruses;
	}

	/**
	 * Znajduje organ najbliżej którego jest jakiś wirus
	 */
	public static Organ findVirus() {
		synchronized (organs) {
			Organ e = organs[0];
			for (int i = 1; i < ORGANS_COUNT; i++) {
				Organ e2 = organs[i];
				Virus v = viruses.get(Generator.getInt(0, viruses.size() - 1));
				if (v.distanceTo(e2.getPos2()) < v.distanceTo(e.getPos2())) {
					e = e2;
				}
			}
			return e;
		}
	}

	/**
	 * Dodaje erytrocyt do organizmu
	 */
	public static void addErythrocyte(Erythrocyte e) {
		synchronized (erythrocytes) {
			erythrocytes.add(e);
			controlPanel.setErythrocytesCount(erythrocytes.size());
			e.setId(ID++);
			Thread t = new Thread(e);
			t.start();
			deselectAll();
			e.setSelected(true);
			informationPanel.setErythrocyte(e);
			informationPanel.setMode("erythrocyte");
			informationPanel.updateErythrocyteInfo();
			if (!informationPanel.isVisible())
				informationPanel.setVisible(true);
			selectedCell = e;
		}
	}

	/**
	 * Dodaje leukocyt do organizmu
	 */
	public static void addLeukocyte(Leukocyte l) {
		synchronized (leukocytes) {
			leukocytes.add(l);
			l.setId(ID++);
			Thread t = new Thread(l);
			t.start();
			controlPanel.setLeukocytesCount(leukocytes.size());

		}
	}

	/**
	 * Usuwa podaną komórkę z organizmu
	 */
	public static void removeCell(Cell e) {
		if (e instanceof Erythrocyte)
			synchronized (erythrocytes) {
				erythrocytes.remove(e);
				controlPanel.setErythrocytesCount(erythrocytes.size());
			}
		else if (e instanceof Leukocyte)
			synchronized (leukocytes) {
				leukocytes.remove(e);
				controlPanel.setLeukocytesCount(leukocytes.size());
			}
		else
			synchronized (viruses) {
				viruses.remove(e);
				controlPanel.setVirusesCount(viruses.size());
			}
	}

	/**
	 * Usuwa aktualnie zaznaczoną komórkę
	 */
	public static void removeSelectedCell() {
		if (selectedCell != null)
			selectedCell.delete();
		selectedCell = null;
	}

	/**
	 * Usuwa wszystkie leukocyty z organizmu
	 */
	public static void removeLeukocytes() {
		synchronized (leukocytes) {
			for (Leukocyte l : leukocytes)
				l.delete();
			controlPanel.setLeukocytesCount(leukocytes.size());
		}
	}

	/**
	 * Powoduje uszkodzenie zaznaczonej komórki
	 */
	public static void damageSelectedCell() {
		if (selectedCell != null)
			selectedCell.damage();
	}

	/**
	 * Tworzy wartość odżywczą w zaznaczonym organie
	 */
	public static void makeValueInSelectedOrgan() {
		if (selectedOrgan != null)
			selectedOrgan.makeValue();
	}

	/**
	 * Ustawia referencję do panelu informacyjnego
	 */
	public void setInformationPanelFrame(InformationPanelFrame frame) {
		this.informationPanel = frame;
	}

	/**
	 * Ustawia referencję do panelu kontrolnego
	 */
	public void setControlPanelFrame(ControlPanelFrame frame) {
		this.controlPanel = frame;
	}

	@Override
	protected void paintComponent(Graphics g) {

		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		at.translate(componentX, componentY);

		// g.translate(x, y);
		Graphics2D g2 = (Graphics2D) g;
		g2.setTransform(at);

		g2.setColor(Settings.BACKGROUND_COLOR);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (RUNNING) {
			for (int i = 0; i < SECTIONS_COUNT2; i++)
				sections2[i].draw(g2);
			for (int i = 0; i < INTERSECTIONS_COUNT2; i++)
				intersections2[i].draw(g2);
			for (int i = 0; i < ORGANS_COUNT; i++)
				organs[i].getLeukocyteIntersection().draw(g2);
			synchronized (viruses) {
				for (Virus v : viruses)
					v.draw(g2);
			}
			synchronized (leukocytes) {
				for (Leukocyte l : leukocytes)
					l.draw(g2);
			}
			for (int i = 0; i < SECTIONS_COUNT; i++)
				sections[i].draw(g2);
			for (int i = 0; i < INTERSECTIONS_COUNT; i++)
				intersections[i].draw(g2);
			for (int i = 0; i < ORGANS_COUNT; i++)
				organs[i].draw(g2);
			synchronized (erythrocytes) {
				for (Erythrocyte c : erythrocytes)
					if (!c.isDeleted())
						c.draw(g2);
			}
		} else {
			g2.setColor(Color.WHITE);
			g2.drawString("Kliknij aby rozpocząć", 400, 300);
		}
		if (OVER) {
			g2.setColor(Color.WHITE);
			g.fillRect(400, 300, 100, 20);
			g2.setColor(Color.BLACK);
			g.drawString("<<KONIEC>>", 405, 315);
		}

	}

	/**
	 * Funkcja usuwająca zaznaczenie wszystkich elementów
	 */
	private static void deselectAll() {
		for (Organ o : organs)
			o.setSelected(false);
		for (Erythrocyte o : erythrocytes)
			o.setSelected(false);
		for (Leukocyte o : leukocytes)
			o.setSelected(false);
		for (Virus o : viruses)
			o.setSelected(false);
		selectedCell = null;
		selectedOrgan = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!RUNNING)
			RUNNING = true;
		Point p = e.getPoint();
		p.x = (int) (p.x / scale);
		p.y = (int) (p.y / scale);
		p.x -= componentX;
		p.y -= componentY;

		for (Organ o : organs) {
			if (o.contains(p)) {
				deselectAll();
				o.setSelected(true);
				if (!informationPanel.isVisible())
					informationPanel.setVisible(true);
				informationPanel.setMode("organ");
				informationPanel.setOrgan(o);
				selectedOrgan = o;
			}
			repaint();
		}

		synchronized (erythrocytes) {
			for (Erythrocyte o : erythrocytes) {
				if (o.contains(p)) {
					deselectAll();
					o.setSelected(true);
					informationPanel.setErythrocyte(o);
					informationPanel.setMode("erythrocyte");
					selectedCell = o;
					if (!informationPanel.isVisible())
						informationPanel.setVisible(true);
					informationPanel.updateErythrocyteInfo();
				}
			}
		}

		synchronized (viruses) {
			for (Virus v : viruses) {
				if (v.contains(p)) {
					deselectAll();
					v.setSelected(true);
					informationPanel.setVirus(v);
					informationPanel.setMode("virus");
					selectedCell = v;
					if (!informationPanel.isVisible())
						informationPanel.setVisible(true);
					informationPanel.updateVirusInfo();
				}
			}
		}

		synchronized (leukocytes) {
			for (Leukocyte l : leukocytes) {
				if (l.contains(p)) {
					deselectAll();
					l.setSelected(true);
					informationPanel.setLeukocyte(l);
					informationPanel.setMode("leukocyte");
					selectedCell = l;
					if (!informationPanel.isVisible())
						informationPanel.setVisible(true);
					informationPanel.updateLeukocyteInfo();
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			while (controlPanel == null)
				Thread.sleep(Settings.ORGANISM_SLEEP);
			while (!RUNNING)
				Thread.sleep(Settings.ORGANISM_SLEEP);
			startOrgans();
			int i = 0;
			startTime = lastLeukocyteTime = System.currentTimeMillis();
			while (!OVER) {
				controlPanel.updateButtons();
				Thread.sleep(Settings.ORGANISM_SLEEP);
				if (organsAlive == 0)
					OVER = true;
				simulationTime = System.currentTimeMillis() - startTime;
				controlPanel.updateTime();
				i++;
				repaint();
				if (i % virusDelay == 0) {
					makeVirus();
					if (virusDelay > 10)
						virusDelay *= (double) (Generator.getInt(90, 105) / (double) 100);
				}
				if (informationPanel != null)
					informationPanel.updateOrgan();

				if (erythrocytes.size() > 3 && i % damageDelay == 0) {
					synchronized (erythrocytes) {
						erythrocytes.get(
								Generator.getInt(0, erythrocytes.size() - 1))
								.damage();
					}
					if (damageDelay > 10)
						damageDelay *= (double) (Generator.getInt(90, 105) / (double) 100);
				}
			}
			playerName = JOptionPane.showInputDialog("Podaj imie:", "");
			Result result = new Result(playerName, simulationTime);
			readResults();
			saveResult(result);
			for (Organ o : organs)
				o.setHealth(0);
			repaint();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("zapisanie wyniku nie powiodło się");
			e.printStackTrace();
		}
	}

	/**
	 * Zwraca czas symulacji
	 */
	public static long getSimulationTime() {
		return simulationTime;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			isDragging = true;
			lastX = e.getX();
			lastY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			isDragging = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e) && isDragging) {
			componentX += e.getX() - lastX;
			componentY += e.getY() - lastY;
			lastX = e.getX();
			lastY = e.getY();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotate = e.getWheelRotation();
		scale -= (double) rotate / 10;
		if (scale < Settings.MIN_SCALE) {
			scale = Settings.MIN_SCALE;
			return;
		}
		if (scale > Settings.MAX_SCALE) {
			scale = Settings.MAX_SCALE;
			return;
		}
		componentX += rotate * 10 / scale;
		componentY += rotate * 10 / scale;

	}

}
