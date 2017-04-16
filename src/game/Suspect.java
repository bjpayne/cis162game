package game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.scene.control.ChoiceBox;

/*****************************************************************
Model the suspects table.
@author Ben Payne
@version 4/4/2017.
******************************************************************/
@DatabaseTable(tableName = "suspect")
public class Suspect implements GameObjectInterface {
    @DatabaseField(id = true)
    private long id;

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
    public long getId() {
        return id;
    }

    /*****************************************************************
    Set the suspect primary key
    @param id the suspect id
    *****************************************************************/
    public void setId(long id) {
        this.id = id;
    }

    /*****************************************************************
    @param name The suspects name
    *****************************************************************/
    public void setName(final String name) {
        this.name = name;
    }

    /*****************************************************************
    get the suspects name
    @return string the characters name
    *****************************************************************/
    public String getName() {
        return name;
    }

    /*****************************************************************
    Check if the selected Suspect is correct
    @return boolean whether its correct
    *****************************************************************/
    @Override
    public boolean belongsTo(ChoiceBox<String> choice) {
        return choice.getId().equals("suspectChoiceBox");
    }
}
