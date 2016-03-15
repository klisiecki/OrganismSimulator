package com.example.pw.utils;
import java.util.Random;


/**
 *	Klasa pomocnicza do generowania liczb losowych
 */
public class Generator {
	private static Random gen = new Random();
	
	/**
	 * Generuje liczbę naturalną liczbę losową z podanego przedziału
	 * @param a początek przedziału
	 * @param b koniec przedziału
	 * @return liczba losowa z przedziału <a,b>
	 */
	public static int getInt(int a, int b) {
		if(a >=0 && b >=a) 
			return Math.abs(gen.nextInt()%(b-a+1))+a;
		return 0;
	}
}
