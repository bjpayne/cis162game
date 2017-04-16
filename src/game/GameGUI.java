package game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URI;
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

    /** Pickup Button */
    @FXML private Button buttonPickup;

    /** Drop Button */
    @FXML private Button buttonDrop;

    /** Eat Button */
    @FXML private Button buttonEat;

    /** Look Button */
    @FXML private Button buttonLook;

    /** Guess Button */
    @FXML private Button buttonGuess;

    /** Solve Button */
    @FXML private Button buttonSolve;

    /** Results text area */
    @FXML private WebView results;

    /** Health Progress bar */
    @FXML private ProgressBar healthBar;

    /** Suspect Text */
    @FXML private Text suspectGuess;

    /** Location Text */
    @FXML private Text locationGuess;

    /** Item Text */
    @FXML private Text itemGuess;

    /*****************************************************************
    Move the player up
    *****************************************************************/
    @FXML protected void directionUp() {}

    /*****************************************************************
    Move the player down
    ******************************************************************/
    @FXML protected void directionDown() {}

    /*****************************************************************
    Move the player left
    ******************************************************************/
    @FXML protected void directionLeft() {}

    /*****************************************************************
    Move the player right
    ******************************************************************/
    @FXML protected void directionRight() {}

    /*****************************************************************
    Pick up an item
    ******************************************************************/
    @FXML protected void pickup() {}

    /*****************************************************************
    Drop an item
    ******************************************************************/
    @FXML protected void drop() {}

    /*****************************************************************
    Eat an item
    ******************************************************************/
    @FXML protected void eat() {}

    /*****************************************************************
    Look around
    ******************************************************************/
    @FXML protected void look() {}

    /*****************************************************************
    Make a guess about the murder
    ******************************************************************/
    @FXML protected void guess() {}

    /*****************************************************************
    Solve the murder
    ******************************************************************/
    @FXML protected void solve() {}

    /*****************************************************************
    Start a new game
    ******************************************************************/
    @FXML protected void newGame() {}

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
        try {
            URI path = getClass().getResource("assets/help.md").toURI();

            String help = MarkdownParser.parse(path);

            this.setResults(help, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    About the game
    ******************************************************************/
    @FXML protected void about() {
        String results = "A Game modelled after the Board game of Clue." +
            System.lineSeparator() + "Written by: Ben Payne";

        this.setResults(results, false);
    }

    /*****************************************************************
    Set the results
    @param results the results to set
    ******************************************************************/
    void setResults(final String results) {
        String parsedResults = MarkdownParser.parse(results);

        this.results.getEngine().loadContent(parsedResults);
    }

    /*****************************************************************
    Set the results, optionally parsing markdown
    @param results the results to parse or set
    @param parse whether to parse the results first
    ******************************************************************/
    private void setResults(final String results, final boolean parse) {
        String parsedResults;

        if (parse) {
            parsedResults = MarkdownParser.parse(results);
        } else {
            parsedResults = results;
        }

        this.results.getEngine().loadContent(parsedResults);
    }

    /*****************************************************************
    Get the buttons
    @return ArrayList the gui buttons
    ******************************************************************/
    ArrayList<Button> getMovementButtons() {
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
    WebEngine getResults() {
        return this.results.getEngine();
    }

    /*****************************************************************
    Get the progress bar
    ******************************************************************/
    public ProgressBar getHealthBar() {
        return this.healthBar;
    }

    /*****************************************************************
    Get the guess button
    ******************************************************************/
    public Button getButtonGuess() {
        return buttonGuess;
    }

    /*****************************************************************
    Get the suspect guess text field
    ******************************************************************/
    public Text getSuspectGuess() {
        return suspectGuess;
    }

    /*****************************************************************
    Get the location guess text field
    ******************************************************************/
    public Text getLocationGuess() {
        return locationGuess;
    }

    /*****************************************************************
    Get the item guess text field
    ******************************************************************/
    public Text getItemGuess() {
        return itemGuess;
    }
}
