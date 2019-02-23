/*
Juliana Sonan
Memory Card Game
 */
package memory;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Memory extends Application {
    //Class variables
    private int count = 0;
    private ArrayList<Integer> cardValueList = new ArrayList<>();
    private final ArrayList<Tile> list = new ArrayList<>();
    private final MemoryPane board = new MemoryPane();
    private final Button btEasy = new Button("EASY\n(Half deck)");
    private final Button btHard = new Button("HARD\n(Full deck)");
    
    @Override
    public void start(Stage primaryStage) {
        //Create a new Pane
        Pane pane = new Pane();
        //Create an InstructionPane
        InstructionPane instructionPane = new InstructionPane();
        //Add the InstructionPane to the Pane
        pane.getChildren().add(instructionPane);
        //Create an EventHandler
        EventHandler<MouseEvent> handler = e -> {
            //Clear the Pane of all children
            pane.getChildren().clear();
            //Create a new DeckOfCards
            DeckOfCards deck = new DeckOfCards();
            //Initialize variable
            int numberOfCards = 52;
            //If 'Easy' button is pressed, change numberOfCards to 26
            if (btEasy.isFocused())
                numberOfCards = 26;
            //Call the function doubleDeck to create an ArrayList to hold the values of the deck up to the index twice
            ArrayList<Integer> gameDeck = doubleDeck(deck, numberOfCards);
            //Shuffle the values in the gameDeck
            Collections.shuffle(gameDeck);
            //Call the function fillBoard to populate the cards in the MemoryPane
            fillBoard(gameDeck);
            //Add the board to the Pane
            pane.getChildren().add(board);
            //Automatically adjust the stage to fit the scene
            primaryStage.sizeToScene(); 
        };
        //Perform activities when the 'Easy' button is clicked
        btEasy.setOnMouseClicked(handler);
        //Perform activities when the 'Hard' button is clicked
        btHard.setOnMouseClicked(handler);
        //Create a new Scene with the Pane as its child
        Scene scene = new Scene(pane);
        //Set the title of the Stage
        primaryStage.setTitle("Memory Card Game");
        //Set the Scene as a child of the Stage
        primaryStage.setScene(scene);
        //Display the Stage
        primaryStage.show();
    }
    //Method to create a new ArrayList that hold the values of the specified deck up to the specified numberOfCards, twice
    private ArrayList<Integer> doubleDeck(DeckOfCards deck, int numberOfCards) {
        //Create a new ArrayList
        ArrayList<Integer> result = new ArrayList<>();
        //Outer loop to repeat process twice
        for (int i = 0; i < 2; i++) {
            //Inner loop to cycle through the deck up to index, numberOfCards
                for (int j = 0; j < numberOfCards; j++) {
                    //Add the value of the card within the deck into the ArrayList
                    result.add(deck.get(j));
                }
            }
        return result;
    }
    //Method to fill the board with the appropriate cards from the deck
    private void fillBoard(ArrayList<Integer> deck) {
        //Initialize variable
        int index = 0;
        //Outer loop to cycle through the columns of the board
        for (int i = 0; i < 13; i++) {
            //Inner loop to cycle through the rows of the board
                for (int j = 0; j < deck.size() / 13; j++) {
                    //Create a new Tile object
                    Tile tile = new Tile();
                    //Set the card value of the tile to the corresponding value in the deck
                    tile.setCardValue(deck.get(index));
                    //Increment index variable
                    index++;
                    //Add the Tile object to an ArrayList
                    list.add(tile);
                    //Add the Tile object to the board at the specified coordinates
                    board.add(tile, i, j);
                }
            }
    }
    //Class to create the board
    class MemoryPane extends GridPane {
        //Constructor
        MemoryPane() {
            setPadding(new Insets(10));
            setVgap(10);
            setHgap(10);
        }
    }
    //Class to create a tile that goes into each cell of the board
    class Tile extends Pane {
        //Class variables
        private boolean isRevealed = false;
        private int cardValue = -100;
        private boolean isMatched = false;
        //Constructor
        Tile() {
            //Create a new ImageView with the image of the back of the card
            ImageView imageView = new ImageView("images/card.png");
            //Add the image into the Tile object
            getChildren().add(imageView);
            //Perform activities if a Tile object is clicked
            setOnMouseClicked(e -> {
                //Call flip() method
                flip();
            });
        }
        //Mutator method to set the Tile's card value
        public void setCardValue(int cardValue) {
            this.cardValue = cardValue;
        }
        //Accessor method to return the card value 
        public int getCardValue() {
            return cardValue;
        }
        //Mutator method to set whether the Tile's card value is revealed on the board or not
        public void setRevealed(boolean isRevealed) {
            this.isRevealed = isRevealed;
            //Clear the tile of any children
            getChildren().clear();
            //Reveal the card value if true
            if (isRevealed == true) {
                getChildren().add(new ImageView("images/" + getCardValue() + ".png"));
            //Hide the card value if false
            } else {
                getChildren().add(new ImageView("images/card.png"));
            }
        }
        //Accessor method to return whether the card value is revealed or not
        public boolean getRevealed() {
            return isRevealed;
        }
        //Mutator method to set if the Tile's card value has been matched with another Tile's card value
        public void setMatched(boolean isMatched) {
            this.isMatched = isMatched;
        }
        //Accessor method to return whether the Tile has been matched with another Tile
        public boolean getMatched() {
            return isMatched;
        }
        //Method to flip the Tile over (reveal/hide card value)
        public void flip() {
            //If the card value of two cards are revealed at one time, determine if they're equal.
            //If so, set the isMatched to true. Otherwise, set to false.
            if (count == 2) {
                if (cardValueList.get(0) == cardValueList.get(1)) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getRevealed() == true && list.get(i).getMatched() == false) 
                            list.get(i).setMatched(true);        
                    }
                    //Remove the card values from the ArrayList
                    cardValueList.remove(0);
                    cardValueList.remove(1);
                }
                //Set all the tiles that are not matched face down
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getMatched() == false) {
                       list.get(i).setRevealed(false);
                        count = 0; 
                    }
                    
                }
            }
            //If at one time, the card value of two cards are not revealed, and the selected card has not been matched, determine if the card is face down.
            //If so, reveal the card value
            //If not, hide the card value
            if (getMatched() == false) {
                if (isRevealed == true) {
                    setRevealed(false);
                } else {
                    setRevealed(true);   
                }
                //Add the card value to the ArrayList
                cardValueList.add(count, getCardValue());
                //Increment count
                count++;
            }
            
        }
    }
    //Class to display the instructions of the game
    class InstructionPane extends VBox {
        InstructionPane() {
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        //Display the instructions of the game
        getChildren().add(new Text("\nThis is a game to test your memory. "
                    + "\nThe object of the game is to clear the board of all the cards. "
                    + "\nClick on a card to reveal its rank and suit. "
                    + "\nTo clear the card from the board, you must find another card with the same rank and suit. "
                    + "\nOnly a maximum of two cards will be revealed at one time. "
                    + "\nHappy Hunting!"));
        getChildren().add(new Text("\nChoose your difficulty: "));
        btEasy.setTextAlignment(TextAlignment.CENTER);
        btHard.setTextAlignment(TextAlignment.CENTER);
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btEasy, btHard);
        getChildren().add(buttons);
        }
    }
    //Main method
    public static void main(String[] args) {
        Application.launch(args);
    }
}
//Class to create the DeckOfCards object
class DeckOfCards {
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