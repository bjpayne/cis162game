package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************
The Location class.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "location_neighbors")
public class LocationNeighbors {

    /** Query builder fields */
    static final String LOCATION_ID = "location_id";

    public static final String NEIGHBOR_ID = "neighbor_id";

    static final String DIRECTION = "direction";

    /** The primary key */
    @DatabaseField(id = true)
    private int id;

    /** The location id */
    @DatabaseField()
    private int location_id;

    /** The neighboring location id */
    @DatabaseField()
    private int neighbor_id;

    /** The neighbor description */
    @DatabaseField()
    private String description;

    /** The neighbor direction */
    @DatabaseField()
    private String direction;

    /*****************************************************************
    ORM Constructor
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
    Get the location id
    @return int The locations id
    *****************************************************************/
    public int getLocationId() {
        return location_id;
    }

    /*****************************************************************
    Set the location id
    @param location_id the locations id
    *****************************************************************/
    public void setLocationId(final int location_id) {
        this.location_id = location_id;
    }

    /*****************************************************************
    Get the neighboring location id
    @return int the neighboring locations id
    *****************************************************************/
    public int getNeighborId() {
        return neighbor_id;
    }

    /*****************************************************************
    Set the location neighbor ID
    @param neighbor_id the neighbors Id
    *****************************************************************/
    public void setNeighborId(final int neighbor_id) {
        this.neighbor_id = neighbor_id;
    }

    /*****************************************************************
    Get the location neighbor relationship description
    @return String the location neighbor relationship description
    *****************************************************************/
    public String getDescription() {
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
    public String getDirection() {
        return this.direction;
    }

    /*****************************************************************
    Get the direction data
    @param direction the direction data
    *****************************************************************/
    public void setDirection(final String direction) {
        this.direction = direction;
    }
}
