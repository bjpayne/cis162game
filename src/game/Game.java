package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
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

    /** The Location Dao */
    Dao<Location, Integer> locationDao;

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

            this.controller = loader.getController();

            ConnectionSource connectionSource = new JdbcConnectionSource(
                "jdbc:sqlite:" +
                this.getClass().getResource("game.db").getPath()
            );

            locationDao = DaoManager.createDao(
                connectionSource,
                Location.class
            );

            createWorld();

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
    Create the game world.
    *****************************************************************/
    private void createWorld() {
        try {
            // this.items = SqlStatement.select(Item.class).getList();

            this.locations = locationDao.queryForAll();

            for (Location location : this.locations) {
                this.controller
                    .getResults()
                    .appendText(location.getName() + System.lineSeparator());
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
}
