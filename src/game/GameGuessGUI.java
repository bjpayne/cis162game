package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/*****************************************************************
Handle the events for the Guess Modal.
@author Ben Payne
@version 4/4/2017.
*****************************************************************/
public class GameGuessGUI {
    /** The suspect choice box */
    @FXML private ChoiceBox<String> suspectChoiceBox;

    /** The location choice box */
    @FXML private Text locationChoice;

    /** The item choice box */
    @FXML private Text itemChoice;

    /** The Window stage */
    private Stage dialogStage;

    /** Whether the user clicked guess */
    private boolean userClickedGuess = false;

    /*****************************************************************
    Set the choice boxes
    @param suspects the game suspects
    *****************************************************************/
    void setSuspectChoiceBox(List<Suspect> suspects) {
        ObservableList<String> choiceBoxList =
            FXCollections.observableArrayList();

        int index = 0;

        for (GameObjectInterface object : suspects) {
            choiceBoxList.add(index, object.getName());

            index++;
        }

        this.suspectChoiceBox.setItems(choiceBoxList);
    }

    /*****************************************************************
    Set the choice boxes
    @param locationName the current location
    *****************************************************************/
    void setLocationChoice(String locationName) {
        this.locationChoice.setText(locationName);
    }

    /*****************************************************************
    Set the choice boxes
    @param itemName the currently held item name
    *****************************************************************/
    void setItemChoice(String itemName) {
        this.itemChoice.setText(itemName);
    }

    /*****************************************************************
    Set the window owner
    @param dialogStage the game suspects
    *****************************************************************/
    void setDialogStage(final Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /*****************************************************************
    Cancel the guess window
    *****************************************************************/
    @FXML private void cancelGuess() {
        this.userClickedGuess = false;

        dialogStage.close();
    }

    /*****************************************************************
    Submit a guess
    *****************************************************************/
    @FXML private void submitGuess() {
        this.userClickedGuess = true;

        dialogStage.close();
    }

    /*****************************************************************
    Get the suspect choice box
    @return ChoiceBox
    *****************************************************************/
    ChoiceBox<String> getSuspectChoiceBox() {
        return suspectChoiceBox;
    }

    /*****************************************************************
    Get whether the user guessed or not
    @return boolean
    *****************************************************************/
    boolean userClickedGuess() {
        return this.userClickedGuess;
    }
}
