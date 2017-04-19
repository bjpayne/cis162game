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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;
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

    /** The Config Dao */
    private Dao<Config, Integer> configDao;

    /** Whether the mystery has been solved */
    private boolean solved = false;

    /** The game suspects */
    private List<Suspect> suspects;

    /** The game locations */
    private List<Location> locations;

    /** The game items */
    private List<Item> items;

    /** The Correct Game Objects to solve the mystery */
    private Suspect correctGameSuspect;
    private Location correctGameLocation;
    private Item correctGameItem;

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

            primaryStage.setTitle(this.getConfig("title"));
            primaryStage.setScene(new Scene(
                root,
                Integer.parseInt(this.getConfig("window_width")),
                Integer.parseInt(this.getConfig("window_height"))
            ));
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
            cacheTheGameObjects();

            // Get a random location based on the table size
            this.setLocation((new Random()).nextInt(this.locations.size()));

            // Set the random objects throughout the map
            this.setLocationsItems();

            // Set the locations neighbors
            this.setLocationsNeighbors();

            // Drop the current item
            drop();

            // Fill the players health bar
            this.gameGUIController.getHealthBar().setProgress(1.0);

            // Remove any previous guesses
            this.gameGUIController.getSuspectGuess().setText("--");
            this.gameGUIController.getLocationGuess().setText("--");
            this.gameGUIController.getItemGuess().setText("--");

            // Set the game welcome message
            this.setWelcomeMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Cache the game world objects
    *****************************************************************/
    void cacheTheGameObjects() throws SQLException {
        this.suspects  = this.suspectDao.queryForAll();
        this.locations = this.locationDao.queryForAll();
        this.items     = this.itemDao.queryForAll();
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

            // Check if the movement direction is valid
            if (neighbor == null) {
                look(new StringBuilder(
                    this.getErrorMessage("You cannot move " + direction + ".")
                ));

                return;
            }

            Location newLocation = null;

            // Check if the movement direction is valid
            for (Location location : locations) {
                if (location.getId() == neighbor.getId()) {
                    newLocation = location;
                }
            }

            // If a location was not found
            if (newLocation == null) {
                this.gameGUIController.setResults(
                    this.getErrorMessage("Location not found.")
                );

                throw new Exception("Could not find location");
            }

            this.currentLocation = newLocation;

            // Remove 2 health points from player for every move
            double health = this.gameGUIController.getHealthBar().getProgress();

            health = ((health * 10) - 2) / 10;

            // If the player is out of health end the game
            if (health <= 0) {
                this.gameGUIController.setResults(
                    this.getUri("assets/health.md")
                );

                this.endGame();

                return;
            }

            // Set the health bar
            this.gameGUIController.getHealthBar().setProgress(health);

            // Initiate a look
            this.look(new StringBuilder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*****************************************************************
    Pickup items found around the game world
    *****************************************************************/
    void pickup() {
        try {
            HashMap<String, String> placeholderValues = new HashMap<>();

            // If the player is already holding an item
            if (this.currentItem != null) {
                placeholderValues.put(
                    "{ITEM}",
                    this.currentItem.getName().toLowerCase()
                );

                this.gameGUIController.setResults(
                    this.getUri("assets/holding.md"),
                    placeholderValues
                );

                return;
            }

            // Turn off all the buttons
            Button[] buttons = this.gameGUIController.getMovementButtons();

            for (Button button : buttons) {
                button.setDisable(true);
            }

            // Pick up the item
            this.currentItem = this.currentLocation.getItem();

            placeholderValues.put(
                "{ITEM}", this.currentItem.getName().toLowerCase()
            );

            this.gameGUIController.setResults(
                this.getUri("assets/picked_up.md"),
                placeholderValues
            );

            // Turn on the guess button
            this.gameGUIController.getButtonGuess().setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Eat the current item
    *****************************************************************/
    void drop() {
        try {
            // Re-enable all buttons
            for (Button button : this.gameGUIController.getButtons()) {
                button.setDisable(false);
            }

            this.gameGUIController.getButtonGuess().setDisable(true);

            if (this.currentItem == null) {
                return;
            }

            HashMap<String, String> placeholderValues = new HashMap<>();

            placeholderValues.put(
                "{ITEM}",
                this.currentItem.getName().toLowerCase()
            );

            this.gameGUIController.setResults(
                this.getUri("assets/drop.md"),
                placeholderValues
            );

            this.currentItem = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Eat the current item
    *****************************************************************/
    void eat() {
        try {
            // If the player is not holding anything to eat
            if (this.currentItem == null) {
                this.gameGUIController.setResults(
                    this.getErrorMessage("You are not holding anything")
                );

                return;
            }

            // If what the player is holding is not edible
            if (! this.currentItem.isConsumable()) {
                this.gameGUIController.setResults(
                    this.getErrorMessage("No! You can't eat that.")
                );

                return;
            }

            // One clue can be consumed, the poison. If consumed the game ends
            if (this.currentItem.isConsumable() && this.currentItem.isSolvable()) {
                this.gameGUIController.setResults(
                    this.getErrorMessage(
                        "You...you ate a clue. You're dead now"
                    )
                );

                this.gameGUIController.getHealthBar().setProgress(0.0);

                this.endGame();

                return;
            }

            // Add the consumed health value to the players health
            ProgressBar healthBar = this.gameGUIController.getHealthBar();

            double currentHealth = healthBar.getProgress();
            currentHealth += this.currentItem.getHealthValue() / 10;
            this.gameGUIController
                .getHealthBar()
                .setProgress(currentHealth);

            String itemName = this.currentItem.getName();

            this.drop();

            this.currentLocation.removeItem();

            HashMap<String, String> placeholderValues = new HashMap<>();

            placeholderValues.put("{ITEM}", itemName);

            this.gameGUIController.setResults(
                this.getUri("assets/consumed.md"),
                placeholderValues
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Look around and describe the current game world
    *****************************************************************/
    void look(StringBuilder results) {
        results.append("<p>You are ");
        results.append(this.currentLocation.getDescription().toLowerCase());
        results.append(".</p>");

        this.currentLocation.getNeighbors().forEach((String, Location) -> {
            results.append("<p>");
            String neighbor = this.currentLocation
                .getNeighborDescription(Location);

            results.append("<p>");
            results.append(neighbor);
            results.append(" is the ");
            results.append(Location.getName().toLowerCase());
            results.append(".</p>");
        });

        if (this.currentLocation.getItem() != null) {
            results.append("<p>In the room there is a ");

            results.append(
                this.currentLocation
                    .getItem()
                    .getDescription()
                    .toLowerCase()
            );

            results.append(".</p>");
        }

        if (this.currentItem != null) {
            results.append("<p><strong>You are holding ");
            results.append(this.currentItem.getDescription());
            results.append("</strong></p>");
        }

        this.gameGUIController.setResults(results.toString());
    }

    /*****************************************************************
    Set the guess button handler. Display a dialog allowing the player
    to choose the suspect.
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

            // Set the window owner
            gameGuessGUIController.setDialogStage(dialogStage);

            // Set the choices
            gameGuessGUIController.setSuspectChoiceBox(this.suspects);

            // If the player has already guessed the suspect set the choice box
            if (this.correctGameSuspect.getName()
                .equals(this.gameGUIController.getSuspectGuess().getText())
            ) {
                gameGuessGUIController
                    .getSuspectChoiceBox()
                    .getSelectionModel()
                    .select(this.correctGameSuspect.getName()
                );
            }

            // Set the location to the current location
            gameGuessGUIController.setLocationChoice(
                    this.currentLocation.getName()
            );

            // If the player is holding an item set the current item
            if (this.currentItem != null) {
                gameGuessGUIController.setItemChoice(
                    this.currentItem.getName()
                );
            }

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
            final String suspectName = suspectChoiceBox
                .getSelectionModel()
                .getSelectedItem();

            final String locationName = this.currentLocation.getName();

            final String itemName = this.currentItem.getName();

            int matches = 0;

            if (this.correctGameSuspect.getName().equals(suspectName)) {
                matches++;

                this.gameGUIController.getSuspectGuess().setText(suspectName);
            }

            if (this.correctGameLocation.getName().equals(locationName)) {
                matches++;

                this.gameGUIController.getLocationGuess().setText(locationName);
            }

            if (this.correctGameItem.getName().equals(itemName)) {
                matches++;

                this.gameGUIController.getItemGuess().setText(itemName);
            }

            if (matches == 3) {
                URI file = this
                    .getClass()
                    .getResource("assets/all_clues.md").toURI();

                gameGUIController.setResults(file);

                this.solved = true;
            } else {
                HashMap<String, String> placeholderValues = new HashMap<>();

                placeholderValues.put("{MATCHES}", Integer.toString(matches));

                gameGUIController.setResults(
                    this.getUri("assets/partial_clues.md"),
                    placeholderValues
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Solve and end the game.
    *****************************************************************/
    void solve() {
        try {
            if (! this.solved) {
                this.gameGUIController.setResults(
                    this.getUri("assets/unsolved.md")
                );

                return;
            }

            HashMap<String, String> placeholderValues = new HashMap<>();

            placeholderValues
                .put("{SUSPECT}", this.correctGameSuspect.getName());

            placeholderValues
                .put("{LOCATION}", this.correctGameLocation.getName());

            placeholderValues
                .put("{ITEM}", this.correctGameItem.getName());

            this.gameGUIController.setResults(
                this.getUri("assets/won.md"),
                placeholderValues
            );

            this.endGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    End the game
    *****************************************************************/
    private void endGame() {
        for (Button button : this.gameGUIController.getButtons()) {
            button.setDisable(true);
        }

        this.gameGUIController.getHealthBar().setProgress(0);
    }

    /*****************************************************************
    Set the welcome message
    *****************************************************************/
    private void setWelcomeMessage() {
        try {
            this.gameGUIController.setResults(
                this.getUri("assets/welcome.md")
            );
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
    void setLocationsNeighbors() {
        // Loop through all the locations
        this.locations.forEach((Location location) -> {
            try {
                // Initialize the neighbors Map
                location.initNeighbors();

                // Get all the neighbors for the current iteration
                QueryBuilder<LocationNeighbors, Integer> query =
                    locationNeighborsDao.queryBuilder();

                query.where().eq(
                    LocationNeighbors.LOCATION_ID,
                    location.getId()
                );

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
    void setLocationsItems() {
        try {
            // Get the solvable game objects
            List<Location> solvableLocations = new ArrayList<>();

            List<Item> solvableItems = new ArrayList<>();

            // Get the non solvable game objects
            List<Location> nonSolvableLocations = new ArrayList<>();

            List<Item> nonSolvableItems = new ArrayList<>();

            // Set the locations
            this.locations.forEach((Location location) -> {
                if (location.isSolvable()) {
                    solvableLocations.add(location);
                } else {
                    nonSolvableLocations.add(location);
                }
            });

            // Set the items
            this.items.forEach((Item item) -> {
                if (item.isSolvable()) {
                    solvableItems.add(item);
                } else if (! item.isSolvable() && item.isConsumable()) {
                    nonSolvableItems.add(item);
                }
            });

            // Shuffle the items so they are random
            Collections.shuffle(
                solvableItems, new Random(System.nanoTime())
            );

            Collections.shuffle(
                nonSolvableItems, new Random(System.nanoTime())
            );

            int index = 0;

            // Add the solvable items to the main rooms
            for (Location location : solvableLocations) {
                location.addItem(solvableItems.get(index));

                index++;
            }

            // Get a random random game object to solve the mystery
            this.setSolution(solvableLocations.get(0));

            index = 0;

            // Add the non solvable items to the hallways
            for (Location location : nonSolvableLocations) {
                location.addItem(nonSolvableItems.get(index));

                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****************************************************************
    Set the correct game objects to solve the mystery
    *****************************************************************/
    private void setSolution(Location location) {
        try {
            this.correctGameSuspect = Suspect.getSolvableObject(
                this.suspectDao
            );

            this.correctGameLocation = location;

            this.correctGameItem = location.getItem();
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

        connectionSource += this.getClass().getResource("db/game.db")
                .getPath();

        return new JdbcConnectionSource(connectionSource);
    }

    /*****************************************************************
    Setup the data access objects
    *****************************************************************/
    void initDataAccessObjects() throws SQLException {
        try {
            locationDao = DaoManager.createDao(getDbSource(), Location.class);

            locationNeighborsDao = DaoManager
                .createDao(getDbSource(), LocationNeighbors.class);

            itemDao = DaoManager.createDao(getDbSource(), Item.class);

            suspectDao = DaoManager.createDao(getDbSource(), Suspect.class);

            configDao = DaoManager.createDao(getDbSource(), Config.class);
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

    /*****************************************************************
    Get a config item from the database
    @return String the item value
    *****************************************************************/
    private String getConfig(String configKey) {
        String value = "";
        try {
            QueryBuilder<Config, Integer> config = this.configDao
                .queryBuilder();

            config.where().eq(Config.CONFIG_KEY, configKey);

            Config configItem = this.configDao.queryForFirst(config.prepare());

            value =  configItem.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /*****************************************************************
     Get a file resource
     @return URI the file resource
     *****************************************************************/
    URI getUri(final String path) throws URISyntaxException {
        return this.getClass().getResource(path).toURI();
    }

    /*****************************************************************
    Get the game suspects
    @return the list of suspects
    *****************************************************************/
    List<Suspect> getSuspects() {
        return suspects;
    }

    /*****************************************************************
    Get the game locations
    @return the list of locations
    *****************************************************************/
    List<Location> getLocations() {
        return locations;
    }

    /*****************************************************************
    Get the game items
    @return the list of items
    *****************************************************************/
    List<Item> getItems() {
        return items;
    }

    /*****************************************************************
    Get the correct game suspect
    @return the suspect
    *****************************************************************/
    Suspect getCorrectGameSuspect() {
        return correctGameSuspect;
    }

    /*****************************************************************
    Get the correct game location
    @return the correct game location
    *****************************************************************/
    Location getCorrectGameLocation() {
        return correctGameLocation;
    }

    /*****************************************************************
    Get the correct game item
    @return the correct game item
    *****************************************************************/
    Item getCorrectGameItem() {
        return correctGameItem;
    }
}
