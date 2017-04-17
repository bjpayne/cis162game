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
import java.util.*;

/*****************************************************************
A simple game of clue
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Game extends Application {

    /** The gameGUI controller */
    private GameGUI gameGUIController;

    /** The current location */
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
    private ArrayList<GameObjectInterface> solution;

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

            this.gameGUIController.setGame(this);

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
            e.printStackTrace();
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
    void createGameWorld() {
        try {
            // Cache the game objects
            this.suspects  = this.suspectDao.queryForAll();
            this.locations = this.locationDao.queryForAll();
            this.items     = this.itemDao.queryForAll();

            // Get a random random game objects to solve the mystery
            this.setSolution();

            // Get a random location based on the table size
            this.setLocation((new Random()).nextInt(this.locations.size()));

            // Set the random objects throughout the map
            setLocationsItems();

            // Set the locations neighbors
            setLocationsNeighbors();

            // Set the game welcome message
            this.setWelcomeMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*****************************************************************
    Set the guess button handler
    *****************************************************************/
    void guess() {
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
            GameGuessGUI gameGuessGUIController = loader.getController();

            gameGuessGUIController.setDialogStage(dialogStage);

            gameGuessGUIController.setChoiceBoxes(
                this.suspects,
                this.locations,
                this.items
            );

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (gameGuessGUIController.userClickedGuess()) {
                handleGuess(
                    gameGuessGUIController.getSuspectChoiceBox(),
                    gameGuessGUIController.getLocationChoiceBox(),
                    gameGuessGUIController.getItemChoiceBox()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Handle the users Guess
    *****************************************************************/
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

            for (GameObjectInterface gameObject : this.solution) {
                if (gameObject.belongsTo(suspectChoiceBox) &&
                    gameObject.getId() == suspect_id
                ) {
                    this.gameGUIController.getSuspectGuess().setText(
                        gameObject.getName()
                    );

                    matches++;
                }

                if (gameObject.belongsTo(locationChoiceBox) &&
                    gameObject.getId() == location_id
                ) {
                    this.gameGUIController.getLocationGuess().setText(
                        gameObject.getName()
                    );

                    matches++;
                }

                if (gameObject.belongsTo(itemChoiceBox) &&
                    gameObject.getId() == item_id
                ) {
                    this.gameGUIController.getItemGuess().setText(
                        gameObject.getName()
                    );

                    matches++;
                }
            }

            if (matches == 3) {
                gameGUIController.setResults("You won!");

                this.endGame();
            } else {
                gameGUIController.setResults(
                    "Close. Keep playing to gather more clues"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Move the player if the direction is a neighbor of the
    current location
    @param direction the movement direction
    *****************************************************************/
    void move(final String direction) {
        try {
            Location newLocation =
                this.currentLocation.getNeighbors().get(direction);

            if (newLocation == null) {
                this.gameGUIController
                    .setResults("You cannot move " + direction);

                return;
            }

            this.currentLocation = newLocation;

            if (! this.currentLocation.isSolvable()) {
                gameGUIController.getButtonGuess().setDisable(true);
            } else {
                gameGUIController.getButtonGuess().setDisable(false);
            }

            this.look();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Look around and describe the current game world
    *****************************************************************/
    void look() {
        String lookResult = "You are ";
        lookResult += this.currentLocation.getDescription().toLowerCase();
        lookResult += ".<br>";

        for (Map.Entry<String, Location> entry :
            this.currentLocation.getNeighbors().entrySet()
        ) {
            String neighborDescription = this.currentLocation
                .getNeighborDescription(entry.getValue());

            lookResult += neighborDescription;
            lookResult += " is the ";
            lookResult += entry.getValue().getName();
            lookResult += ".<br>";
        }

        lookResult += "In the room there is a ";

        if (currentLocation.isSolvable()) {
            lookResult += currentLocation
                .getItem()
                .getDescription()
                .toLowerCase();
        }

        this.gameGUIController.setResults(lookResult);
    }

    /*****************************************************************
    End the game
    *****************************************************************/
    private void endGame() {
        List<Button> buttons = this.gameGUIController.getButtons();

        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    /*****************************************************************
    Set the welcome message
    *****************************************************************/
    private void setWelcomeMessage() {
        try {
            URI file = getClass().getResource("assets/welcome.md").toURI();

            this.gameGUIController.setResults(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Query for a  then set the current
    @param index the current locations ID
    *****************************************************************/
    private void setLocation(final int index) {
        try {
            this.currentLocation = this.locations.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Set the game world locations neighbors
    *****************************************************************/
    private void setLocationsNeighbors() {
        // Loop through all the locations
        this.locations.forEach((Location location) -> {
            try {
                // Initialize the neighbors Map
                location.initNeighbors();

                // Get all the neighbors for the current iteration
                QueryBuilder<LocationNeighbors, Integer> query =
                    locationNeighborsDao.queryBuilder();

                query.where()
                    .eq(LocationNeighbors.LOCATION_ID, location.getId());

                List<LocationNeighbors> neighbors =
                    locationNeighborsDao.query(query.prepare());

                // Loop through all the neighbors for the current iteration
                neighbors.forEach((LocationNeighbors neighbor) -> {
                    try {
                        // Add each neighbor using the direction as the key
                        location.addNeighbor(
                            neighbor.getDirection(),
                            this.locationDao.queryForId(
                                neighbor.getNeighborId()
                            ),
                            neighbor.getDescription()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /*****************************************************************
    Set the game world locations items
    *****************************************************************/
    private void setLocationsItems() {
        try {
            List<Location> solvableLocations = new ArrayList<>();

            List<Item> solvableItems = new ArrayList<>();

            this.locations.forEach((Location location) -> {
                if (location.isSolvable()) {
                    solvableLocations.add(location);
                }
            });

            this.items.forEach((Item item) -> {
                if (item.isSolvable()) {
                    solvableItems.add(item);
                }
            });

            // Shuffle the items so they are random
            Collections.shuffle(solvableItems, new Random(System.nanoTime()));

            int index = 0;

            for (Location location : solvableLocations) {
                location.addItem(solvableItems.get(index));

                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Set the correct game objects to solve the mystery
    *****************************************************************/
    private void setSolution() {
        try {
            solution = new ArrayList<>();

            solution.add(Suspect.getSolvableObject(suspectDao));
            solution.add(Location.getSolvableObject(locationDao));
            solution.add(Item.getSolvableObject(itemDao));
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
