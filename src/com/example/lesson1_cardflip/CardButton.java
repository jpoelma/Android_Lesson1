package com.example.lesson1_cardflip;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardButton implements View.OnClickListener {
	private int myId;
	private Button myButton;
	private Card card;
	private CardGame myCardGame;
	private boolean revealed;
	public CardButton(int id, Button button) {
		myButton = button;
		myId = id;
		myButton.setOnClickListener(this);
		revealed = false;
		updateView();
	}
	public void setCard(Card c) {
		card = c;
		updateView();
	}
	public Card getCard() {
		return card;
	}
	public void setCardGame(CardGame cg) {
		myCardGame = cg;
	}
	public void updateView() {
		if(card != null) {
			if(revealed) {
				myButton.setText(Html.fromHtml(card.getDisplayText()), TextView.BufferType.SPANNABLE);
				myButton.setBackgroundResource(0);
			} else {
				myButton.setText("");
				myButton.setText(Html.fromHtml("[" + card.getDisplayText() + "]"),TextView.BufferType.SPANNABLE);
				
				myButton.setBackgroundResource(R.drawable.ic_launcher);
			}
			
		} else {
			myButton.setText("ID: " + myId);
		}
		
	}
	@Override
	public void onClick(View v) {
		if(!revealed) {
			revealed = true;
			updateView();
		
			myCardGame.cardFlipped(myId);
		}
	}
	
	public void resetState() {
		revealed = false;
		updateView();
	}
	public boolean isRevealed() {
		return revealed;
	}

}
