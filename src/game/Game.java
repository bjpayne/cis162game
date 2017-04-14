package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
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
A simple game of clue
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Game extends Application {

    /** The gameGUI controller */
    private GameGUI controller;

    /** The startGame items */
    private List<Item> items;

    /** The startGame locations */
    private List<Location> locations;

    /** The startGame locations neighboring locations */
    private HashMap<Location, Location> locationNeighbors;

    /** The Location Dao */
    private Dao<Location, Integer> locationDao;

    /** The Location Neighbors Dao */
    private Dao<LocationNeighbors, Integer> locationNeighborsDao;

    /** The Item Dao */
    private Dao<Item, Integer> itemDao;

    /** The Suspect Dao */
    private Dao<Suspect, Integer> suspectDao;

    /** The current player location */
    private Location currentLocation;

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

            initDataAccessObjects();

            createGameWorld();

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
    Create the game
    *****************************************************************/
    public void createGameWorld() {
        try {
            this.setMovementButtonListeners();

            this.setWelcomeMessage();

            this.setLocation(9);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the movement button action event listener
    *****************************************************************/
    private void setMovementButtonListeners() {
        for (Button button : this.controller.getMovementButtons()) {
            button.setOnAction(event -> move(button.getText()));
        }
    }

    /*****************************************************************
    Move the player if the direction is a neighbor of the
    current location
    *****************************************************************/
    private void move(final String direction) {
        try {
            QueryBuilder<LocationNeighbors, Integer> query =
                locationNeighborsDao.queryBuilder();

            query.where()
                .eq(LocationNeighbors.LOCATION_ID, this.currentLocation.getId())
                .and()
                .eq(LocationNeighbors.DIRECTION, direction.toLowerCase());

            List<LocationNeighbors> locationNeighbors;

            locationNeighbors = locationNeighborsDao.query(query.prepare());

            if (locationNeighbors.size() == 0) {
                this.controller
                    .getResults()
                    .appendText(
                        "You cannot move " +
                            direction +
                            System.lineSeparator()
                    );

                return;
            }

            LocationNeighbors neighbor = locationNeighbors.get(0);

            Location newLocation = locationDao.
                queryForId(neighbor.getNeighborId());

            if (newLocation == null) {
                this.controller
                    .getResults()
                    .appendText("You cannot move " + direction);
            }

            this.currentLocation = newLocation;

            this.controller.getResults().appendText(newLocation.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /*****************************************************************
    Set the welcome message
    *****************************************************************/
    private void setWelcomeMessage() {
        this.controller.setResults(
            "Welcome to the Game. " +
            "Click Help->How to play to learn how to play the startGame."
        );
    }

    /*****************************************************************
    Set the current location
    *****************************************************************/
    private void setLocation(final int locationId) {
        try {
            this.currentLocation = locationDao.queryForId(locationId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    /*****************************************************************
    Setup the data access objects
    *****************************************************************/
    private void initDataAccessObjects() throws SQLException {
        try {
            locationDao = DaoManager.createDao(getDbSource(), Location.class);

            locationNeighborsDao = DaoManager
                .createDao(getDbSource(), LocationNeighbors.class);

            itemDao = DaoManager.createDao(getDbSource(), Item.class);

            suspectDao = DaoManager.createDao(getDbSource(), Suspect.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
