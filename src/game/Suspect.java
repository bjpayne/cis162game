package game;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;
import java.util.Random;

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

    /** Get a solvable game object */
    static Suspect getSolvableObject(
        final Dao<Suspect, Integer> dao
    ) {
        try {
            QueryBuilder<Suspect, Integer> query = dao.queryBuilder();

            List<Suspect> objects = dao.query(query.prepare());

            final int index = (new Random()).nextInt(objects.size());

            return objects.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*****************************************************************
    Get the suspect primary key
    @return long the primary key
    *****************************************************************/
    public long getId() {
        return id;
    }

    /*****************************************************************
    get the suspects name
    @return string the characters name
    *****************************************************************/
    public String getName() {
        return name;
    }
}
