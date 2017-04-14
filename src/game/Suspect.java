package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "suspect")
public class Suspect {
    @DatabaseField(id = true)
    private int id;

    /** The Characters name */
    @DatabaseField()
    private String name;

    /*****************************************************************
    ORM Constructor
    *****************************************************************/
    public Suspect() {}

    /*****************************************************************
    Get the suspect primary key
    @return String the characters name
    *****************************************************************/
    public int getId() {
        return id;
    }

    /*****************************************************************
    Set the suspect primary key
    @param id the suspect id
    *****************************************************************/
    public void setId(int id) {
        this.id = id;
    }

    /*****************************************************************
    @param name The suspects name
    *****************************************************************/
    public void setName(final String name) {
        this.name = name;
    }

    /*****************************************************************
    Get the suspects name
    @return String the characters name
    *****************************************************************/
    public String getName() {
        return name;
    }
}
