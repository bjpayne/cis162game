package game;


import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

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

    /** Use as a HashMap key */
    int hashCode();

    /** Check for equality */
    boolean equals(Object object);

    /** Check if the guess is correct */
    boolean guess(final Map.Entry<String, String> guess, final String solution);
}
