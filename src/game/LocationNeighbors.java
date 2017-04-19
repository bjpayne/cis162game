package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************
Model the location_neighbor lookup table
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "location_neighbors")
public class LocationNeighbors {
    /** The primary key */
    @DatabaseField(id = true)
    private int id;

    /** The  id */
    @DatabaseField()
    private int location_id;

    /** The neighboring  id */
    @DatabaseField()
    private int neighbor_id;

    /** The neighbor description */
    @DatabaseField()
    private String description;

    /** The neighbor direction */
    @DatabaseField()
    private String direction;

    /** Query builder accessors */
    static final String LOCATION_ID = "location_id";

    /*****************************************************************
    Model Constructor
    *****************************************************************/
    LocationNeighbors () {}

    /*****************************************************************
    Get the location_neighbor relationship id
    @return int The locations id
    *****************************************************************/
    public int getId() {
        return id;
    }

    /*****************************************************************
    Get the  id
    @return int The locations id
    *****************************************************************/
    public int getLocationId() {
        return location_id;
    }

    /*****************************************************************
    Get the neighboring  id
    @return int the neighboring locations id
    *****************************************************************/
    int getNeighborId() {
        return neighbor_id;
    }

    /*****************************************************************
    Get the  neighbor relationship description
    @return String the  neighbor relationship description
    *****************************************************************/
    String getDescription() {
        return this.description;
    }

    /*****************************************************************
    Set the location_neighbor relationship description
    @param description the description
    *****************************************************************/
    public void getDescription(final String description) {
        this.description = description;
    }

    /*****************************************************************
    Get the direction data
    @return String the direction data
    *****************************************************************/
    String getDirection() {
        return this.direction;
    }
}
