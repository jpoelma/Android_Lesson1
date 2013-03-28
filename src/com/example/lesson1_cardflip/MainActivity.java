package com.example.lesson1_cardflip;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements CardGame {
	private int cardTileIds[] = {
			R.id.button2,R.id.button3, R.id.button4, R.id.button5, 
			R.id.button6,R.id.button7,R.id.button8,R.id.button9,
			R.id.button10, R.id.button11,R.id.button12, R.id.button13,
			R.id.button14,R.id.button15,R.id.button16,R.id.button17
			}; 
	private CardButton cardTiles[];
	private List<Integer> chosenTiles;
	
	private Deck deck;
	private RadioButton match2;
	private RadioButton match3;
	private int points;
	private Handler myHandler = new Handler();
	private Runnable clearSelectionRunnable;
	private int flips;
	private int numberOfCards;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		clearSelectionRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				resetSelection();
				Log.println(Log.INFO, "Some Tag", "Delayed Reset, chosenTiles=" + chosenTiles.size() + ", pts=" + points);
			}
			
		};
		GridLayout ll = (GridLayout)findViewById(R.id.cardTable);
		ll.removeAllViews();

		initGame();
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void initGame() {
		numberOfCards = 24;
		Button myButton;
		GridLayout cardTable = (GridLayout)findViewById(R.id.cardTable);
		Log.println( Log.INFO, " A tag", "Init Table");
		cardTable.setColumnCount(4);
		cardTable.setRowCount(numberOfCards / 4 + ((numberOfCards % 4) !=0 ? 1: 0));
		Log.println( Log.INFO, " A tag", "Init Tiles");
		//cardTable.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		LayoutParams lp;
		cardTiles = new CardButton[numberOfCards];
		
		for(int i=0; i<numberOfCards; i++) {
			myButton = new Button(this);
			myButton.setText("");
			myButton.setGravity(Gravity.FILL_HORIZONTAL);
			cardTiles[i] =new CardButton(i, myButton); //(Button)findViewById(cardTileIds[i]));
			cardTiles[i].setCardGame(this);
			lp= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			cardTable.addView(myButton, i, lp);
		}

		
		deck = new Deck();
		
		chosenTiles = new LinkedList< Integer >();
		match2 = ((RadioButton)findViewById(R.id.match2));
		match3 = ((RadioButton)findViewById(R.id.match3));
		
		newGame();
	}
	public void nextLevel() {
		chosenTiles.clear();
		for(CardButton c: cardTiles) {
			c.resetState();
		}
		deck.shuffleAll();
		Card[] drawnCards= deck.draw(numberOfCards);
		for(int i=0; i<numberOfCards; i++) {
			cardTiles[i].setCard(drawnCards[i]);			
		}
	}
	@Override
	public void newGame() {
		match2.setEnabled(true);
		match3.setEnabled(true);
		points =0;
		flips = 0;
		((TextView)findViewById(R.id.actionText)).setText("");
		refreshDisplay();
		nextLevel();
		//Note: needs Score Reset to
	}
	private int getNumberToMatch() {
		//Grey Out Game mode to prevent play from "re-choosing" their  mode mid-game
		match2.setEnabled(false);
		match3.setEnabled(false);
		//Figure out How many to match
		if(match2.isChecked()) {
			return 2;
		}
		if(match3.isChecked()) {
			return 3;
		}
		
		return 0;
	}
	
	//Refresh the Score & points TextBoxes
	private void refreshDisplay() {
		((TextView)findViewById(R.id.scoreText)).setText("Score: " + points);
		((TextView)findViewById(R.id.flipCountText)).setText("Flips: " + flips);
	}
	
	@Override
	public void cardFlipped(int tileId) {
		int scoreMul=1;
		
		Card card1;
		Card card2;
		flips++;
		
		//Add Picked Card To pile
		chosenTiles.add(tileId);
		//Do I need to check the cards selected?
		if(getNumberToMatch() > chosenTiles.size()) {
			return;
		}
		int matches=0;
		//Compare to every Card Selected at least once (allows for > 2 card matches to be scored)
		for(int i=0; i<chosenTiles.size(); i++) {
			card1 = cardTiles[chosenTiles.get(i)].getCard();
			for(int j=i+1; j<chosenTiles.size(); j++) {
				card2 = cardTiles[chosenTiles.get(j)].getCard();
				
				if(card1.match(card2)) {
					scoreMul *= card1.pointsForMatch(card2);
					matches++;
				}
			}
		}
		//Has a sufficient number of matched been made? (Ex: for 3 card match => (A Matches B && B Matches C)  "A" does not need to match "C" )
		//   Ex: A-Clubs + 10-Clubs + 10-Diamonds
		if(matches < getNumberToMatch()-1) {
			myHandler.postDelayed(clearSelectionRunnable,500);
			((TextView)findViewById(R.id.actionText)).setText("Cards Did not match, You lose 1 point");
			points--;
			refreshDisplay();
			Log.println(Log.INFO, "Some Tag", "Not A Match, chosenTiles=" + chosenTiles.size() + ", pts=" + points);
			return ;
		}
		
		points += scoreMul;

		Log.println(Log.INFO, "Some Tag", "Match!, chosenTiles=" + chosenTiles.size() + ", pts=" + points);
		//Matched Message
		String actionText = "Matched: ";
		
		actionText += cardTiles[chosenTiles.get(0)].getCard().getDisplayText();
		for(int i=1; i< chosenTiles.size(); i++) {
			actionText += ", " + cardTiles[chosenTiles.get(i)].getCard().getDisplayText();
		}
		actionText += " for " + scoreMul + (scoreMul == 1 ? " Point!" :" Points! ");
		((TextView)findViewById(R.id.actionText)).setText(Html.fromHtml(actionText), TextView.BufferType.SPANNABLE);
		
		//Clear Tiles and check to see if you can progress to next level/set of cards
		chosenTiles.clear();
		if(!atLeastOneMatch()) {
			nextLevel();
			Log.println(Log.INFO, "Some Tag", "Next Level, chosenTiles=" + chosenTiles.size() + ", pts=" + points);
		}
		Log.println(Log.INFO, "Some Tag", "Still More Matches.., chosenTiles=" + chosenTiles.size() + ", pts=" + points);
		
		refreshDisplay();
	}
	private void resetSelection() {
		for(int i: chosenTiles) {
			cardTiles[i].resetState();
		}
		chosenTiles.clear();				
	}
	private boolean atLeastOneMatch() {
		CardButton card1,card2;

		
		for(int i=0; i<cardTiles.length; i++) {
			card1 = cardTiles[i];
			if(!card1.isRevealed()) {
				for(int j=i+1; j<cardTiles.length; j++) {
					card2 = cardTiles[j];
					if(card1.getCard().match(card2.getCard()) && !card2.isRevealed()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public void dealButtonOnClick(View v) {
		newGame();
	}

}
