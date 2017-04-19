package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*****************************************************************
Model the suspects table.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "config")
public class Config {
    @DatabaseField(id = true)
    private long id;

    /** The config key */
    @DatabaseField()
    private String key;

    /** The config value */
    @DatabaseField()
    private String value;

    /** Query builder accessors */
    public static final String CONFIG_KEY = "key";

    /*****************************************************************
    ORM Constructor
    *****************************************************************/
    public Config() {}

    /*****************************************************************
    Get the config primary key
    @return long the key
    *****************************************************************/
    public long getId() {
        return id;
    }

    /*****************************************************************
    Get the config key
    @return string the characters name
    *****************************************************************/
    public String getKey() {
        return this.key;
    }

    /*****************************************************************
    Get the config value
    @return string the config value
    *****************************************************************/
    public String getValue() {
        return this.value;
    }
}
