package game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;

/*****************************************************************
Handle the events for the GUI.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class GameGUI {
    /** Direction Up Button */
    @FXML private Button buttonUp;

    /** Direction Down Button */
    @FXML private Button buttonDown;

    /** Direction Left Button */
    @FXML private Button buttonLeft;

    /** Direction Right Button */
    @FXML private Button buttonRight;

    /** Direction Right Button */
    @FXML private Button buttonPickup;

    /** Direction Right Button */
    @FXML private Button buttonDrop;

    /** Direction Right Button */
    @FXML private Button buttonEat;

    /** Direction Right Button */
    @FXML private Button buttonLook;

    /** Direction Right Button */
    @FXML private Button buttonGuess;

    /** Results text area */
    @FXML private TextArea results;

    /** Health Progress bar */
    @FXML private ProgressBar healthBar;

    /** Health Progress bar */
    @FXML private Label labelWeapon;

    /** Health Progress bar */
    @FXML private Label labelSuspect;

    /** Health Progress bar */
    @FXML private Label labelLocation;

    /*****************************************************************
    Move the player up
    *****************************************************************/
    @FXML protected void directionUp() {
        this.results.appendText("");
    }

    /*****************************************************************
    Move the player down
    ******************************************************************/
    @FXML protected void directionDown() {
        // Game.move();
    }

    /*****************************************************************
    Move the player left
    ******************************************************************/
    @FXML protected void directionLeft() {
        // Game.move("left");
    }

    /*****************************************************************
    Move the player right
    ******************************************************************/
    @FXML protected void directionRight() {
        // Game.move("right");
    }

    /*****************************************************************
    Pick up an item
    ******************************************************************/
    @FXML protected void pickup() {
        this.setResults("pickup");
    }

    /*****************************************************************
    Drop an item
    ******************************************************************/
    @FXML protected void drop() {
        this.setResults("drop");
    }

    /*****************************************************************
    Eat an item
    ******************************************************************/
    @FXML protected void eat() {
        this.setResults("eat");
    }

    /*****************************************************************
    Look around
    ******************************************************************/
    @FXML protected void look() {
        this.setResults("look around");
    }

    /*****************************************************************
    Make a guess about the murder
    ******************************************************************/
    @FXML protected void guess() {
        this.setResults("guess");
    }

    /*****************************************************************
    Start a new game
    ******************************************************************/
    @FXML protected void newGame() {
        this.setResults("new game");
    }

    /*****************************************************************
    Close the game
    ******************************************************************/
    @FXML protected void exit() {
        System.exit(0);
    }

    /*****************************************************************
    Display help
    ******************************************************************/
    @FXML protected void help() {
        String results = "Move around the map using the direction keys. " +
            "Pickup items you find and collect the clues. " +
            "Once you have enough clues make a guess.";

        this.setResults(results);
    }

    /*****************************************************************
    About the game
    ******************************************************************/
    @FXML protected void about() {
        String results = "A Game modelled after the Board game of Clue." +
            System.lineSeparator() + "Written by: Ben Payne";

        this.setResults(results);
    }

    /*****************************************************************
    Set the results
    ******************************************************************/
    public void setResults(final String results) {
        this.results.appendText(
            results + System.lineSeparator() + System.lineSeparator()
        );
    }

    /*****************************************************************
    Get the buttons
    @return ArrayList the gui buttons
    ******************************************************************/
    public ArrayList<Button> getMovementButtons() {
        return new ArrayList<>(Arrays.asList(
            buttonUp,
            buttonDown,
            buttonLeft,
            buttonRight
        ));
    }

    /*****************************************************************
    Get the results textarea
    ******************************************************************/
    public TextArea getResults() {
        return this.results;
    }

    /*****************************************************************
    Get the progress bar
    ******************************************************************/
    public ProgressBar getHealthBar() {
        return this.healthBar;
    }

    /*****************************************************************
    Get the weaponLabel
    ******************************************************************/
    public Label getLabelWeapon() {
        return this.labelWeapon;
    }

    /*****************************************************************
    Get the weaponLabel
    ******************************************************************/
    public Label getLabelSuspect() {
        return this.labelSuspect;
    }

    /*****************************************************************
    Get the weaponLabel
    ******************************************************************/
    public Label getLabelLocation() {
        return this.labelLocation;
    }
}
