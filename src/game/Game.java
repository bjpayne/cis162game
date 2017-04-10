package game;

import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

/*****************************************************************
A game of clue
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Game extends Application {

    /** The gameGUI controller */
    private GameGUI controller;

    /** The game items */
    private List<Item> items;

    /** The game locations */
    private List<Location> locations;

    /** The game locations neighboring locations */
    private HashMap<Location, Location> locationsNeighbors;

    /** The players current location */
    private static Location currentLocation;

    /*****************************************************************
    Start the GUI
    @param primaryStage The gui stage
    @throws Exception throws a base exception
    *****************************************************************/
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameGui.fxml"));

        Parent root = loader.load();

        this.controller = loader.getController();

        try {
            DataConnectionManager.init("game.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        primaryStage.setTitle("Game");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    /*****************************************************************
    Launch the GUI
    @param args Main arguments passed to application
    *****************************************************************/
    public static void main(final String[] args) {
        launch(args);
    }

    /*****************************************************************
    Create the game world.
    *****************************************************************/
    private void createWorld() {
        try {
            this.items = SqlStatement.select(Item.class).getList();

            this.locations = SqlStatement.select(Location.class).getList();

            for (Location location : this.locations) {
                LocationNeighbors locationNeighbor = SqlStatement
                    .select(LocationNeighbors.class)
                    .where("location_id")
                    .eq(location.getId())
                    .getFirst();

                Location neighbor = SqlStatement
                    .select(Location.class)
                    .where("id")
                    .eq(locationNeighbor.getLocation_id())
                    .getFirst();

                this.locationsNeighbors.put(location, neighbor);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the welcome message
    *****************************************************************/
    private void setWelcomeMessage() {
        this.controller.setResults(
            "Welcome to the game." +
            "Click Help->How to play to learn how to play the game."
        );
    }

    /*****************************************************************
    Move the player
    *****************************************************************/
    public static void move (String direction) {
        //
    }
}
