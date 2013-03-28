package com.example.lesson1_cardflip;

import java.util.Date;
import java.util.Random;

public class Deck {
	private Card[] cardsAvailable;
	private int topIndex;
	private Random prng;
	public Deck() {
		cardsAvailable = new Card[Card.face.length * Card.suit.length];
		for(int f=0; f<Card.face.length; f++) {
			for(int s=0; s<Card.suit.length; s++) {
				cardsAvailable[f + s * Card.face.length] = new Card(s,f);
			}
		}
		prng = new Random(new Date().getTime());
		topIndex = 0;
	}
	//Shuffle All cards in deck
	public void shuffleAll() {
		shuffle(0, cardsAvailable.length);
		topIndex = 0;
	}
	public void shuffleTop() {
		shuffle(topIndex, cardsAvailable.length);
	}
	
	// Shuffles the cards in indices [start, stop)
	public void shuffle(int start, int stop) {
		Card temp;
		int index;
		if(stop > cardsAvailable.length) {
			stop = cardsAvailable.length;
		}
		if(start >= stop || start < 0 || stop < 0) { 
			return;
		}
		
		for(int i=start; i< stop; i++) {
			index = prng.nextInt(stop-start) + start;
			//Swap Current & random Element
			temp = cardsAvailable[i];
			cardsAvailable[i] = cardsAvailable[index];
			cardsAvailable[index] = temp;
		}
		
	}
	
	public Card[] draw(int numberToDraw) {
		Card[] ret = new Card[numberToDraw];
		if(numberToDraw <= 0) {
			return null;
		}
		for(int i=0; i<numberToDraw; i++) {
			ret[i] = cardsAvailable[i+ topIndex];
		}
		topIndex += numberToDraw;
		return ret;
	}
}
