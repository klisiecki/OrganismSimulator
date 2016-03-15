package com.example.pw.utils;
import java.io.Serializable;


/**
 * Przechowuje wynik symulacji
 */
public class Result implements Serializable, Comparable<Result>{
	private static final long serialVersionUID = 1L;
	private String playerName;
	private long time;
	
	/**
	 * Konstruktor bezparametrowy
	 */
	public Result() {
	}
	
	/**
	 * Tworzy nowy wynik 
	 */
	public Result(String name, long time) {
		this.playerName = name;
		this.time = time;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(Result o) {
		if(o.time < this.time) return -1;
		else if (o.time > this.time) return 1;
		return 0;
	}
}
