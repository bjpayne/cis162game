package game;

import java.util.HashMap;

/*****************************************************************
The Location class.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Location {

    /** The location description */
    private String description;

    /** The locations item */
    private Item item = null;

    /** The locations neighbors */
    private HashMap<String, Location> neighbors;

    /*****************************************************************
    Initialize a new Item
    @param description The location description.
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
    Initialize a new Item
    *****************************************************************/
    public String getDescription() {
        return description;
    }

    /*****************************************************************
    Initialize a new Item
    *****************************************************************/
    public Item getItem() {
        return item;
    }

    /*****************************************************************
    Initialize a new Item
    *****************************************************************/
    public void addItem (Item item) {
        this.item = item;
    }

    /*****************************************************************
    Check if the Location has an item
    *****************************************************************/
    public boolean hasItem() {
        return this.getItem() == null;
    }

    /*****************************************************************
    Initialize a new Item
    *****************************************************************/
    public HashMap<String, Location> getNeighbors() {
        return neighbors;
    }
}
