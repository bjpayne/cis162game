package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;

/*****************************************************************
Handle the events for the GUI.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Controller {
    /** Direction Up Button */
    @FXML private Button buttonUp;

    /** Direction Down Button */
    @FXML private Button buttonDown;

    /** Direction Left Button */
    @FXML private Button buttonLeft;

    /** Direction Right Button */
    @FXML private Button buttonRight;

    /** Results text area */
    @FXML private TextArea results;

    /** Health Progress bar */
    @FXML private ProgressBar progressBar;

    /*****************************************************************
    Move the player up
    @param event The action event data
    *****************************************************************/
    @FXML protected void directionUp(ActionEvent event) {
        this.results.setText("Direction Up");
    }

    /*****************************************************************
    Move the player down
    @param event The action event data
    ******************************************************************/
    @FXML protected void directionDown(ActionEvent event) {
        this.results.setText("Direction Down");
    }

    /*****************************************************************
    Move the player left
    @param event The action event data
    ******************************************************************/
    @FXML protected void directionLeft(ActionEvent event) {
        this.results.setText("Direction Left");
    }

    /*****************************************************************
    Move the player right
    @param event The action event data
    ******************************************************************/
    @FXML protected void directionRight(ActionEvent event) {
        this.results.setText("Direction Right");
    }

    /*****************************************************************
    Get the buttons
    ******************************************************************/
    public ArrayList<Button> getButtons() {
        return new ArrayList<>(Arrays.asList(
            buttonUp, buttonDown, buttonLeft, buttonRight
        ));
    }

    /*****************************************************************
    Get the progress bar
    ******************************************************************/
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
