/*
Juliana Sonan
Memory Card Game
 */
package memory;
import java.util.ArrayList;
import java.util.Collections;
//Class to create the DeckOfCards object
public class DeckOfCards {
    //Class variable
    private ArrayList<Integer> deck = new ArrayList<>();
    //Constructor
    DeckOfCards() {
        //Add 52 cards and their card values in the deck
        for (int i = 0; i < 52; i++) {
                deck.add(i);
            }
            //Shuffle the deck
            Collections.shuffle(deck);
        
    }
    //Method to return the card value
    public int get(int index) {
        return deck.get(index);
    }
    //Method to return the size of the deck
    public int size() {
        return deck.size();
    }
    
}