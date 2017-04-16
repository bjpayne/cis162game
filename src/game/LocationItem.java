package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************
Model the location_item lookup table
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "location_item")
public class LocationItem {
    /** The model id */
    @DatabaseField(id = true)
    private int id;

    /** The  id */
    @DatabaseField()
    private int location_id;

    /** The item id */
    @DatabaseField()
    private int item_id;

    /** Query builder accessors */
    static final String LOCATION_ID = "location_id";
    static final String ITEM_ID = "item_id";

    /*****************************************************************
    Model Constructor
    *****************************************************************/
    public LocationItem() {}
}
