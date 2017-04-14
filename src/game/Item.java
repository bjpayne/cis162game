package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************
The Item class.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "item")
public class Item {
    /** The items id */
    @DatabaseField(id = true)
    private long id;

    /** The items name */
    @DatabaseField()
    private String name;

    /** The items description */
    @DatabaseField()
    private String description;

    /** The items weight */
    @DatabaseField()
    private int weight;

    /** Items can be consumed */
    @DatabaseField()
    private boolean isConsumable;

    /** Items can be picked up */
    @DatabaseField()
    private boolean isPickable;

    /** Items health value */
    @DatabaseField()
    private int healthValue;

    /*****************************************************************
    ORM Constructor
    *****************************************************************/
    Item () {}

    /*****************************************************************
    Initialize a new Item
    @param name The items name.
    @param description The items description.
    @param weight The items weight.
    @param isConsumable Whether the item is edible.
    *****************************************************************/
    public Item (
        final String name,
        final String description,
        final int weight,
        final boolean isConsumable
    ) {
        this.name = name;

        this.description = description;

        this.weight = weight;

        this.isConsumable = isConsumable;
    }

    /*****************************************************************
    Get the items id
    @return Long The items id
    *****************************************************************/
    public long getId() {
        return this.id;
    }

    /*****************************************************************
    Get the items id
    @param id The items id
    *****************************************************************/
    public void setId(final long id) {
        this.id = id;
    }

    /*****************************************************************
    Get the items name
    @return String The items name
    *****************************************************************/
    public String getName() {
        return name;
    }

    /*****************************************************************
    Set the items name
    @param name The items name
    *****************************************************************/
    public void setName(String name) {
        this.name = name;
    }

    /*****************************************************************
    Set the items description
    @return String the items description
    *****************************************************************/
    public String getDescription() {
        return description;
    }

    /*****************************************************************
    Set the items description
    @param description the items description
    *****************************************************************/
    public void setDescription(String description) {
        this.description = description;
    }

    /*****************************************************************
    Get the items weight
    @return int the items weight
    *****************************************************************/
    public int getWeight() {
        return weight;
    }

    /*****************************************************************
    Set the items weight
    @param weight the items weight
    *****************************************************************/
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /*****************************************************************
    Check whether the item is edible
    @return boolean whether the item is edible
    *****************************************************************/
    public boolean getIsConsumable() {
        return this.isConsumable;
    }

    /*****************************************************************
    Set whether the item is consumable
    @param isConsumable whether the item is consumable
    *****************************************************************/
    public void setIsConsumable(final boolean isConsumable) {
        this.isConsumable = isConsumable;
    }

    /*****************************************************************
    Set whether the item is consumable
    @return boolean whether the item is pickable
    *****************************************************************/
    public boolean getIsPickable() {
        return this.isPickable;
    }

    /*****************************************************************
    Set whether the item is pickable
    @param isPickable whether the item is pickable
    *****************************************************************/
    public void setIsPickable(boolean isPickable) {
        this.isPickable = isPickable;
    }

    /*****************************************************************
    Get the items health value
    @return int the items health value
    *****************************************************************/
    public int getHealthValue() {
        return this.healthValue;
    }

    /*****************************************************************
    Set the items health value
    @param healthValue the items health value
    *****************************************************************/
    public void setHealthValue(final int healthValue) {
        this.healthValue = healthValue;
    }
}
