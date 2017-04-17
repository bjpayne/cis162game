package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.control.ChoiceBox;

import java.util.List;
import java.util.Random;

/*****************************************************************
The Item class.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "item")
public class Item implements GameObjectInterface {
    /** The items id */
    @DatabaseField(id = true)
    private long id;

    /** The items name */
    @DatabaseField()
    private String name;

    /** The items description */
    @DatabaseField()
    private String description;

    /** Items can be consumed */
    @DatabaseField(columnName = "is_consumable")
    private boolean isConsumable;

    /** Items can be picked up */
    @DatabaseField(columnName = "is_pickable")
    private boolean isPickable;

    /** Items health value */
    @DatabaseField(columnName = "health_value")
    private int healthValue;

    /** Items health value */
    @DatabaseField()
    private boolean solvable;

    /** Query builder accessors */
    static final String SOLVABLE = "solvable";
    static final String NAME = "name";

    /*****************************************************************
    Model Constructor
    *****************************************************************/
    Item () {}

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
    Get the items name
    @return String The items name
    *****************************************************************/
    @Override
    public boolean belongsTo(ChoiceBox<String> choice) {
        return choice.getId().equals("itemChoiceBox");
    }

    @Override
    public boolean equals() {
        return false;
    }

    /*****************************************************************
    Get the items name
    @return String The items name
    *****************************************************************/
    static GameObjectInterface getSolvableObject(
        final Dao<Item, Integer> dao
    ) {
        try {
            QueryBuilder<Item, Integer> query = dao.queryBuilder();

            query.where().eq(Item.SOLVABLE, true);

            List<Item> locations = dao.query(query.prepare());

            final int index = (new Random()).nextInt(locations.size());

            return locations.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
    Check whether the item is edible
    @return boolean whether the item is edible
    *****************************************************************/
    public boolean isConsumable() {
        return this.isConsumable;
    }

    /*****************************************************************
    Set whether the item is consumable
    @param isConsumable whether the item is consumable
    *****************************************************************/
    public void isConsumable(final boolean isConsumable) {
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
    public void setIsPickable(final boolean isPickable) {
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

    /*****************************************************************
    Get whether the item can solve the mystery
    @return boolean whether the item solved the mystery
    *****************************************************************/
    public boolean isSolvable() {
        return solvable;
    }

    /*****************************************************************
    Set the items health value
    @param solvable whether the item solves the mystery
    *****************************************************************/
    public void setSolvable(final boolean solvable) {
        this.solvable = solvable;
    }
}
