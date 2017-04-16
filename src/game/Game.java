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
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*****************************************************************
A simple game of clue
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Game extends Application {

    /** The gameGUI controller */
    private GameGUI gameGUIController;

    /** The gameGUIGuess controller */
    private GameGuessGUI gameGuessGUIController;

    /** The current player  */
    private Location currentLocation;

    /** The Location Dao */
    private Dao<Location, Integer> locationDao;

    /** The Location Neighbors Dao */
    private Dao<LocationNeighbors, Integer> locationNeighborsDao;

    /** The Item Dao */
    private Dao<Item, Integer> itemDao;

    /** The Suspect Dao */
    private Dao<Suspect, Integer> suspectDao;

    /** The Correct Game Objects to solve the mystery */
    private ArrayList<GameObjectInterface> validGameObjects;

    /** The game suspects */
    private List<Suspect> suspects;

    /** The game locations */
    private List<Location> locations;

    /** The game items */
    private List<Item> items;

    /** The Window Owner */
    private Stage primaryStage;

    /*****************************************************************
    Start the GUI
    @param primaryStage The gui window owner
    @throws Exception throws a base exception
    *****************************************************************/
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.primaryStage = primaryStage;

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("view/gameGui.fxml")
            );

            Parent root = loader.load();

            // Get the controller instance
            this.gameGUIController = loader.getController();

            // Set the styles for the view
            this.gameGUIController.getResults().setUserStyleSheetLocation(
                getClass()
                .getResource("assets/styles.css")
                .toExternalForm()
            );

            // Initialize access to the DB
            initDataAccessObjects();

            // Create the game world
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
    private void createGameWorld() {
        try {
            this.handleMoveButtonEvent();

            this.handleGuessButtonEvent();

            this.setWelcomeMessage();

            // Get a random  based on the  table size
            this.setLocation(
                (new Random()).nextInt(locationDao.queryForAll().size()) + 1
            );

            // Cache the game objects
            this.suspects = this.suspectDao.queryForAll();

            this.locations = this.locationDao.queryForAll();

            this.items = this.itemDao.queryForAll();

            // Get a random random game objects to solve the mystery
            this.setValidGameObjects();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the movement button action event listener
    *****************************************************************/
    private void handleMoveButtonEvent() {
        for (Button button : this.gameGUIController.getMovementButtons()) {
            button.setOnAction(event -> move(button.getText()));
        }
    }

    /*****************************************************************
    Set the guess button handler
    *****************************************************************/
    private void handleGuessButtonEvent() {
        this.gameGUIController.getButtonGuess().setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("view/gameGuessGui.fxml")
                );

                AnchorPane page = loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Person");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(this.primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                // Get the controller
                this.gameGuessGUIController = loader.getController();

                this.gameGuessGUIController.setDialogStage(dialogStage);

                this.gameGuessGUIController.setChoiceBoxes(
                    this.suspects,
                    this.locations,
                    this.items
                );

                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();

                if (gameGuessGUIController.userClickedGuess()) {
                    handleGuess(
                        this.gameGuessGUIController.getSuspectChoiceBox(),
                        this.gameGuessGUIController.getLocationChoiceBox(),
                        this.gameGuessGUIController.getItemChoiceBox()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleGuess(
        final ChoiceBox<String> suspectChoiceBox,
        final ChoiceBox<String> locationChoiceBox,
        final ChoiceBox<String> itemChoiceBox
    ) {
        try {
            final int suspect_id = suspectChoiceBox
                .getSelectionModel()
                .getSelectedIndex();

            final int location_id = locationChoiceBox
                .getSelectionModel()
                .getSelectedIndex();

            final int item_id = itemChoiceBox
                .getSelectionModel()
                .getSelectedIndex();

            int matches = 0;

            for (GameObjectInterface validGameObject : this.validGameObjects) {
                if (validGameObject.belongsTo(suspectChoiceBox) &&
                    validGameObject.getId() == suspect_id
                ) {
                    this.gameGUIController.getSuspectGuess().setText(
                        validGameObject.getName()
                    );

                    matches++;
                }

                if (validGameObject.belongsTo(locationChoiceBox) &&
                    validGameObject.getId() == location_id
                ) {
                    this.gameGUIController.getLocationGuess().setText(
                        validGameObject.getName()
                    );

                    matches++;
                }

                if (validGameObject.belongsTo(itemChoiceBox) &&
                    validGameObject.getId() == item_id
                ) {
                    this.gameGUIController.getItemGuess().setText(
                        validGameObject.getName()
                    );

                    matches++;
                }
            }

            gameGUIController.setResults("You got " + matches + " / 3 correct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Move the player if the direction is a neighbor of the
    current location
    @param direction the movement direction
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
                this.gameGUIController
                    .setResults("You cannot move **" + direction + "**");

                return;
            }

            LocationNeighbors neighbor = locationNeighbors.get(0);

            Location newLocation = locationDao.
                queryForId(neighbor.getNeighborId());

            this.setLocation(newLocation);

            this.gameGUIController.setResults(newLocation.getDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the welcome message
    *****************************************************************/
    private void setWelcomeMessage() {
        try {
            URI file = getClass().getResource("assets/welcome.md").toURI();

            String welcome = MarkdownParser.parse(file);

            this.gameGUIController.setResults(welcome);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Query for a  then set the current
    @param locationId the current locations ID
    *****************************************************************/
    private void setLocation(final int locationId) {
        try {
            this.currentLocation = locationDao.queryForId(locationId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the current
    @param location the current locations ID
    *****************************************************************/
    private void setLocation(Location location) {
        try {
            this.currentLocation = location;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*****************************************************************
    Set the correct game objects to solve the mystery
    *****************************************************************/
    private void setValidGameObjects() {
        try {
            // Get the suspect and  ids
            final int suspect_id =
                (new Random()).nextInt(suspectDao.queryForAll().size()) + 1;

            final int location_id =
                (new Random()).nextInt(locationDao.queryForAll().size()) + 1;

            // Get all the items that can solve the mystery
            QueryBuilder<Item, Integer> query = itemDao.queryBuilder();

            query.where().eq(Item.SOLVABLE, true);

            List<Item> items = itemDao.query(query.prepare());

            final int item_id = (new Random()).nextInt(items.size()) + 1;

            // Get the objects
            Suspect suspect = suspectDao.queryForId(suspect_id);

            Location location = locationDao.queryForId(location_id);

            Item item = itemDao.queryForId(item_id);

            validGameObjects = new ArrayList<>();

            validGameObjects.add(suspect);
            validGameObjects.add(location);
            validGameObjects.add(item);
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
                .getResource("db/game.db")
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
