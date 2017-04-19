package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

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
    private static final String SOLVABLE = "solvable";

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
    Get the items name
    @return String The items name
    *****************************************************************/
    public String getName() {
        return name;
    }

    /*****************************************************************
    Get the solvable object for this type
    @return Item the correct game item
    *****************************************************************/
    static Item getSolvableObject(
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
    Set the items description
    @return String the items description
    *****************************************************************/
    String getDescription() {
        return description;
    }

    /*****************************************************************
    Check whether the item is edible
    @return boolean whether the item is edible
    *****************************************************************/
    boolean isConsumable() {
        return this.isConsumable;
    }

    /*****************************************************************
    Get the items health value
    @return int the items health value
    *****************************************************************/
    int getHealthValue() {
        return this.healthValue;
    }

    /*****************************************************************
    Get whether the item can solve the mystery
    @return boolean whether the item solved the mystery
    *****************************************************************/
    boolean isSolvable() {
        return solvable;
    }
}
