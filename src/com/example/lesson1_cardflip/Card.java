package com.example.lesson1_cardflip;

public class Card {
	public static String[] suit = {"\u2660", "<font color='red'>\u2665</font>","<font color='red'>\u2666</font>","\u2663"}; 
	public static String[] face = {"A", "K", "Q", "J", "10", "9", "8", "7","6","5","4", "3", "2"};
	private int suitId;
	private int faceId;
	
	
	public Card(int theSuitId, int theFaceId) {
		suitId = theSuitId;
		faceId = theFaceId;
	}
	
	public boolean match(Card c) {
		return suitId == c.suitId || faceId == c.faceId; 
	}
	
	public int pointsForMatch(Card c) {
		int pts=0;
		if(!match(c)) {
			return 0;
		}
		
		if(c.suitId == suitId){
			pts += 1;
		}
		if(c.faceId == faceId) {
			pts += 2;
		}
		return pts;
	}
	public String getDisplayText(){
		String ret="";
		if(faceId < face.length) {
			ret += face[faceId];
		}
		if(suitId < suit.length){
			ret += suit[suitId];
		}
		return ret;
	}
	
}
