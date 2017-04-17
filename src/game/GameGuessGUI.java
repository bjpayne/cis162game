package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
    @FXML private ChoiceBox<String> locationChoiceBox;

    /** The item choice box */
    @FXML private ChoiceBox<String> itemChoiceBox;

    /** The Window stage */
    private Stage dialogStage;

    /** Whether the user clicked guess */
    private boolean userClickedGuess = false;

    /*****************************************************************
    Initialize the ChoiceBoxes
    @param suspects the game suspects
    @param locations the game locations
    @param items the game items
    *****************************************************************/
    void setChoiceBoxes(
        final List<? extends GameObjectInterface> suspects,
        final List<? extends GameObjectInterface> locations,
        final List<? extends GameObjectInterface> items
    ) {
        setChoiceBoxList(suspects, this.suspectChoiceBox);

        setChoiceBoxList(locations, this.locationChoiceBox);

        setChoiceBoxList(items, this.itemChoiceBox);
    }

    /*****************************************************************
    Set the choice boxes
    @param suspects the game suspects
    @param choiceBox the choice box to set
    *****************************************************************/
    private void setChoiceBoxList(
        List<? extends GameObjectInterface> suspects,
        ChoiceBox<String> choiceBox
    ) {
        ObservableList<String> choiceBoxList = FXCollections.observableArrayList();

        choiceBoxList.add(0, "-- select one --");

        for (GameObjectInterface suspect : suspects) {
            choiceBoxList.add((int) suspect.getId(), suspect.getName());
        }

        choiceBox.setItems(choiceBoxList);
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
    public ChoiceBox<String> getSuspectChoiceBox() {
        return suspectChoiceBox;
    }

    /*****************************************************************
    Get the location choice box
    @return ChoiceBox
    *****************************************************************/
    public ChoiceBox<String> getLocationChoiceBox() {
        return locationChoiceBox;
    }

    /*****************************************************************
    Get the item choice box
    @return ChoiceBox
    *****************************************************************/
    public ChoiceBox<String> getItemChoiceBox() {
        return itemChoiceBox;
    }

    /*****************************************************************
    Get whether the user guessed or not
    @return boolean
    *****************************************************************/
    public boolean userClickedGuess() {
        return this.userClickedGuess;
    }
}
