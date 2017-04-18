package game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;

/*****************************************************************
Handle the events for the GUI.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class GameGUI {
    /** The game instance */
    private Game game;

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
    private Object movementButtons;

    /*****************************************************************
    Set the game instance
    *****************************************************************/
    public void setGame(Game game) {
        this.game = game;
    }

    /*****************************************************************
    Move the player up
    *****************************************************************/
    @FXML protected void directionUp() {
        this.game.move(this.buttonUp.getText());
    }

    /*****************************************************************
    Move the player down
    ******************************************************************/
    @FXML protected void directionDown() {
        this.game.move(this.buttonDown.getText());
    }

    /*****************************************************************
    Move the player left
    ******************************************************************/
    @FXML protected void directionLeft() {
        this.game.move(this.buttonLeft.getText());
    }

    /*****************************************************************
    Move the player right
    ******************************************************************/
    @FXML protected void directionRight() {
        this.game.move(this.buttonRight.getText());
    }

    /*****************************************************************
    Pick up an item
    ******************************************************************/
    @FXML protected void pickup() {
        game.pickup();
    }

    /*****************************************************************
    Drop an item
    ******************************************************************/
    @FXML protected void drop() {
        this.game.drop();
    }

    /*****************************************************************
    Eat an item
    ******************************************************************/
    @FXML protected void eat() {
        this.game.eat();
    }

    /*****************************************************************
    Look around
    ******************************************************************/
    @FXML protected void look() {
        this.game.look("");
    }

    /*****************************************************************
    Make a guess about the murder
    ******************************************************************/
    @FXML protected void guess() {
        game.guess();
    }

    /*****************************************************************
    Solve the murder
    ******************************************************************/
    @FXML protected void solve() {
        this.game.solve();
    }

    /*****************************************************************
    Start a new game
    ******************************************************************/
    @FXML protected void newGame() {
        this.game.createGameWorld();
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
        try {
            URI path = getClass().getResource("assets/help.md").toURI();

            this.setResults(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    About the game
    ******************************************************************/
    @FXML protected void about() {
        try {
            URI path = getClass()
                .getResource("assets/about.md")
                .toURI();

            this.setResults(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @param path the path to parse
    ******************************************************************/
    void setResults(final URI path) {
        String results = MarkdownParser.parse(path);

        this.results.getEngine().loadContent(results);
    }

    /*****************************************************************
    Get the buttons
    @return ArrayList the gui buttons
    ******************************************************************/
    ArrayList<Button> getButtons() {
        ArrayList<Button> buttons = new ArrayList<>();

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (Button.class.isAssignableFrom(field.getType())) {
                    buttons.add((Button) field.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buttons;
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

    /*****************************************************************
    Get the item guess text field
    ******************************************************************/
    public Button[] getMovementButtons() {
        return new Button[] {buttonUp, buttonDown, buttonLeft, buttonRight};
    }
}
