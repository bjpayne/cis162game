package game;


/*****************************************************************
Interface that allows for using a single List to hold locations,
items and suspects

@author Ben Payne
@version 4/4/2017.
******************************************************************/
public interface GameObjectInterface {
    /** Get the object ID */
    long getId();

    /** Get the object name */
    String getName();
}
