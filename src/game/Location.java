package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.control.ChoiceBox;

import java.util.HashMap;

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

    /** The  description */
    @DatabaseField()
    private String description;

    /** The  name */
    @DatabaseField()
    private String name;

    /** The locations item */
    private Item item = null;

    /** The locations neighbors */
    private HashMap<String, Location> neighbors;

    /*****************************************************************
    Model Constructor
    *****************************************************************/
    Location () {}

    /*****************************************************************
    Initialize a new Item
    @param description The  description.
    *****************************************************************/
    public Location(final String description) {
        this.description = description;
    }

    /*****************************************************************
    Initialize a new Item
    @param description The items description.
    @param item The locations items
    *****************************************************************/
    public Location(final String description, final Item item) {
        this.description = description;

        this.item = item;
    }

    /*****************************************************************
    Get the locations id
    @return long the locations id
    *****************************************************************/
    public long getId() {
        return this.id;
    }

    /*****************************************************************
    Set the locations id
    @param id the locations id
    *****************************************************************/
    public void setId(final long id) {
        this.id = id;
    }

    /*****************************************************************
    Get the locations description
    @return String the locations description
    *****************************************************************/
    public String getDescription() {
        return description;
    }

    /*****************************************************************
    Get the locations item
    @return Item the locations item
    *****************************************************************/
    public Item getItem() {
        return item;
    }

    /*****************************************************************
    Add an item to the location
    @param item the item to add to the location
    *****************************************************************/
    public void addItem (Item item) {
        this.item = item;
    }

    /*****************************************************************
    Check if the Location has an item
    @return boolean whether the  has an item
    *****************************************************************/
    public boolean hasItem() {
        return this.getItem() == null;
    }

    /*****************************************************************
    Get the locations neighbors
    @return HashMap the locations neighbors
    *****************************************************************/
    public HashMap<String, Location> getNeighbors() {
        return neighbors;
    }

    /*****************************************************************
    Get the locations name
    @return HashMap the locations neighbors
    *****************************************************************/
    public String getName() {
        return name;
    }

    @Override
    public boolean belongsTo(ChoiceBox<String> choice) {
        return choice.getId().equals("locationChoiceBox");
    }

    /*****************************************************************
    Set the locations name
    @param String the locations name
    *****************************************************************/
    public void setName(String name) {
        this.name = name;
    }
}
