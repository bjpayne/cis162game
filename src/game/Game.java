package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/*****************************************************************
 A startGame of clue
 @author Ben Payne
 @version 4/4/2017.
 ******************************************************************/
public class Game extends Application {

    /**
     * The gameGUI controller
     */
    private GameGUI controller;

    /**
     * The startGame items
     */
    private List<Item> items;

    /**
     * The startGame locations
     */
    private List<Location> locations;

    /**
     * The startGame locations neighboring locations
     */
    private HashMap<Location, Location> locationsNeighbors;

    /**
     * The Location Dao
     */
    private Dao<Location, Integer> locationDao;

    /*****************************************************************
     Start the GUI
     @param primaryStage The gui stage
     @throws Exception throws a base exception
     *****************************************************************/
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameGui.fxml"));

            Parent root = loader.load();

            controller = loader.getController();

            locationDao = DaoManager.createDao(getDbSource(), Location.class);

            setWelcomeMessage();

            setMovementButtonListener();

            primaryStage.setTitle("Game");
            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
     Launch the GUI
     @param args Main arguments passed to application
     *****************************************************************/
    public static void main(final String[] args) {
        launch(args);
    }

    /*****************************************************************
     Set the movement button action event listener
     *****************************************************************/
    private void setMovementButtonListener() {
        for (Button button : this.controller.getMovementButtons()) {
            button.setOnAction(event -> move(button.getText()));
        }
    }

    /*****************************************************************
     Move the player if the direction is a neighbor of the
     current location
     *****************************************************************/
    private void move(final String direction) {
        System.out.println(direction);
    }

    /*****************************************************************
     Set the welcome message
     *****************************************************************/
    private void setWelcomeMessage() {
        this.controller.setResults(
            "Welcome to the startGame." +
            "Click Help->How to play to learn how to play the startGame."
        );
    }

    /*****************************************************************
     Get the ORM connection source
     *****************************************************************/
    private JdbcConnectionSource getDbSource() throws SQLException {
        String connectionSource = "";

        connectionSource += "jdbc:sqlite:";

        connectionSource += this
                .getClass()
                .getResource("game.db")
                .getPath();

        return new JdbcConnectionSource(connectionSource);
    }
}
