package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.control.ChoiceBox;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*****************************************************************
Model the  table.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "location")
public class Location implements GameObjectInterface {
    /** The primary id key */
    @DatabaseField(id = true)
    private long id;

    /** The description */
    @DatabaseField()
    private String description;

    /** The name */
    @DatabaseField()
    private String name;

    /** Is the location solvable */
    @DatabaseField()
    private boolean solvable;

    /** The locations item */
    private Item item = null;

    /** The locations neighbors */
    private HashMap<String, Location> neighbors;

    private HashMap<Location, String> neighborDescription;

    /** Query builder accessors */
    private static final String SOLVABLE = "solvable";

    /*****************************************************************
    Model Constructor
    *****************************************************************/
    Location () {}

    /*****************************************************************
    Initialize a new Item
    @param dao The items description.
    @return GameObjectInterface
    *****************************************************************/
    static GameObjectInterface getSolvableObject(
        final Dao<Location, Integer> dao
    ) {
        try {
            QueryBuilder<Location, Integer> query = dao.queryBuilder();

            query.where().eq(Location.SOLVABLE, true);

            List<Location> locations = dao.query(query.prepare());

            final int index = (new Random()).nextInt(locations.size());

            return locations.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*****************************************************************
    Get the locations id
    @return long the locations id
    *****************************************************************/
    public long getId() {
        return this.id;
    }

    /*****************************************************************
    Get the locations description
    @return String the locations description
    *****************************************************************/
    String getDescription() {
        return description;
    }

    /*****************************************************************
    Get the locations item
    @return Item the locations item
    *****************************************************************/
    Item getItem() {
        return item;
    }

    /*****************************************************************
    Add an item to the location
    @param item the item to add to the location
    *****************************************************************/
    void addItem(Item item) {
        this.item = item;
    }

    /*****************************************************************
    Initialize the neighbors property
    *****************************************************************/
    void initNeighbors() {
        this.neighbors = new HashMap<>();

        this.neighborDescription = new HashMap<>();
    }

    /*****************************************************************
    Get the locations neighbors
    @return HashMap the locations neighbors
    *****************************************************************/
    HashMap<String, Location> getNeighbors() {
        return this.neighbors;
    }

    /*****************************************************************
    Get the neighbors description
    @return HashMap the locations neighbors
    *****************************************************************/
    String getNeighborDescription(Location neighbor) {
        return this.neighborDescription.get(neighbor);
    }

    /*****************************************************************
    Get the locations neighbors
    @param direction the neighbors direction
    @param location the neighbor instance
    *****************************************************************/
    void addNeighbor(
        final String direction,
        final Location location,
        final String description
    ) {
        this.neighbors.put(direction, location);

        this.neighborDescription.put(location, description);
    }

    /*****************************************************************
    Get the locations name
    @return HashMap the locations neighbors
    *****************************************************************/
    public String getName() {
        return name;
    }

    /*****************************************************************
    Get the choice box this belongs to
    @param choice the location choice box
    @return boolean
    *****************************************************************/
    @Override
    public boolean belongsTo(ChoiceBox<String> choice) {
        return choice.getId().equals("locationChoiceBox");
    }

    /*****************************************************************
    Get whether the location can solve the mystery
    @return boolean is the location solvable
    *****************************************************************/
    boolean isSolvable() {
        return solvable;
    }
}
