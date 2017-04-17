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
    private GameGUI gameGUIController = null;

    /** The current location */
    private Location currentLocation = null;

    /** The current item being held */
    private Item currentItem = null;

    /** The Location Dao */
    private Dao<Location, Integer> locationDao;

    /** The Location Neighbors Dao */
    private Dao<LocationNeighbors, Integer> locationNeighborsDao;

    /** The Item Dao */
    private Dao<Item, Integer> itemDao;

    /** The Suspect Dao */
    private Dao<Suspect, Integer> suspectDao;

    /** The Correct Game Objects to solve the mystery */
    private Map<GameObjectInterface, String> solution;

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

            primaryStage.setTitle("You have no clue!");
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

            // Drop the current item
            drop();

            // Fill the players health bar
            this.gameGUIController.getHealthBar().setProgress(1.0);

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

            // Set the choices
            gameGuessGUIController.setSuspectChoiceBox(this.suspects);
            gameGuessGUIController.setLocationChoice(
                    this.currentLocation.getName()
            );
            gameGuessGUIController.setItemChoice(this.currentItem.getName());

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (gameGuessGUIController.userClickedGuess()) {
                handleGuess(gameGuessGUIController.getSuspectChoiceBox());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Handle the users Guess
    *****************************************************************/
    private void handleGuess(final ChoiceBox<String> suspectChoiceBox) {
        try {
            final String suspect_name = suspectChoiceBox
                .getSelectionModel()
                .getSelectedItem();

            int matches = 0;

            if (this.solution.containsKey(Suspect.class)) {
                String correctSuspect = this.solution.get(Suspect.class);

                if(correctSuspect.equals(suspect_name)) {
                    this.gameGUIController
                        .getSuspectGuess()
                        .setText(correctSuspect);

                    matches++;
                }
            }

            if (this.solution.containsKey(Suspect.class)) {
                String correctLocation = this.solution.get(Suspect.class);

                if(correctLocation.equals(this.currentItem.getName())) {
                    matches++;
                }
            }

            if (this.solution.containsKey(Suspect.class)) {
                String corecctSuspect = this.solution.get(Suspect.class);
            }

            if (matches == 3) {
                gameGUIController.setResults(
                    "<p>You have all three clues!</p>" +
                    "<p>Click 'Solve' to end the game</p>"
                );
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
            Location neighbor = this.currentLocation
                .getNeighbors()
                .get(direction.toLowerCase());

            if (neighbor == null) {
                look(getErrorMessage("You cannot move " + direction + "."));

                return;
            }

            Location newLocation = null;

            for (Location location : locations) {
                if (location.getId() == neighbor.getId()) {
                    newLocation = location;
                }
            }

            if (newLocation == null) {
                this.gameGUIController.setResults(
                    "Location not found. Please try again."
                );

                throw new Exception("Could not find location");
            }

            this.currentLocation = newLocation;

            if (! this.currentLocation.isSolvable()) {
                gameGUIController.getButtonGuess().setDisable(true);
            } else {
                gameGUIController.getButtonGuess().setDisable(false);
            }

            this.look("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Pickup items found around the game world
    *****************************************************************/
    void pickup() {
        try {
            if (! this.currentLocation.isSolvable()) {
                this.look(getErrorMessage("You cannot pickup an item here."));

                return;
            }

            if (this.currentItem != null) {
                this.gameGUIController.setResults(
                    "<p>You are holding " +
                    this.currentItem.getDescription() +
                    "</p>"
                );

                return;
            }

            Button[] buttons = this.gameGUIController.getMovementButtons();

            for (Button button : buttons) {
                button.setDisable(true);
            }

            this.currentItem = this.currentLocation.getItem();

            this.gameGUIController.setResults(
                "You picked up " +
                this.currentItem.getDescription().toLowerCase() +
                "."
            );

            this.gameGUIController.getButtonGuess().setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Eat the current item
    *****************************************************************/
    void drop() {
        this.currentItem = null;

        // Re-enable all buttons
        for (Button button : this.gameGUIController.getButtons()) {
            button.setDisable(false);
        }

        this.gameGUIController.setResults("<p>You dropped the item.</p>");

        this.gameGUIController.getButtonGuess().setDisable(true);
    }

    /*****************************************************************
    Eat the current item
    *****************************************************************/
    void eat() {
        if (this.currentItem == null) {
            this.gameGUIController.setResults(
                "<p>You are not holding anything.<p>"
            );

            return;
        }

        if (! this.currentItem.isConsumable()) {
            this.gameGUIController.setResults(
                "<p>No! You can't eat that.</p>"
            );

            return;
        }

        if (this.currentItem.isConsumable() && this.currentItem.isSolvable()) {
            this.gameGUIController.setResults(
                "<p>You...you ate a clue. You're dead now.</p>"
            );

            this.gameGUIController.getHealthBar().setProgress(0.0);

            this.endGame();

            return;
        }

        this.gameGUIController
            .getHealthBar()
            .setProgress(this.currentItem.getHealthValue() / 10);
    }

    /*****************************************************************
    Look around and describe the current game world
    *****************************************************************/
    void look(String prefix) {
        StringBuilder lookResult = new StringBuilder(prefix);

        lookResult.append("<p>You are ");
        lookResult.append(
                this.currentLocation.getDescription().toLowerCase()
        );

        lookResult.append(".</p>");


        for (Map.Entry<String, Location> entry :
            this.currentLocation.getNeighbors().entrySet()
        ) {
            String neighborDescription = this.currentLocation
                .getNeighborDescription(entry.getValue());

            lookResult.append("<p>");
            lookResult.append(neighborDescription);
            lookResult.append(" is the ");
            lookResult.append(entry.getValue().getName().toLowerCase());
            lookResult.append(".</p>");
        }

        if (this.currentLocation.isSolvable()) {
            lookResult.append("<p>In the room there is a ");

            lookResult.append(
                this.currentLocation
                    .getItem()
                    .getDescription()
                    .toLowerCase()
            );

            lookResult.append(".</p>");
        }

        if (this.currentItem != null) {
            lookResult.append("<p>You are holding ");
            lookResult.append(this.currentItem.getDescription());
            lookResult.append("</p>");
        }

        this.gameGUIController.setResults(lookResult.toString());
    }

    /*****************************************************************
    End the game
    *****************************************************************/
    private void endGame() {
        for (Button button : this.gameGUIController.getButtons()) {
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
            this.solution = new HashMap<>();

            GameObjectInterface suspect =
                    Suspect.getSolvableObject(this.suspectDao);

            GameObjectInterface location =
                    Location.getSolvableObject(this.locationDao);

            GameObjectInterface item =
                    Item.getSolvableObject(this.itemDao);

            if (suspect == null || location == null || item == null) {
                throw new Exception("Could not find valid game object");
            }

            this.solution.put(suspect, suspect.getName());
            this.solution.put(location, location.getName());
            this.solution.put(item, item.getName());
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

    /*****************************************************************
    Get the standard error message output
    *****************************************************************/
    private String getErrorMessage(String message) {
        return "<p class='error'><strong>" + message + "</strong></p>";
    }
}
