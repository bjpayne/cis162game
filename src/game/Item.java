package game;

/*****************************************************************
The Item class.
@author benjaminp
@version 4/4/2017.
******************************************************************/
public class Item {

    /** The items name */
    private String name;

    /** The items description */
    private String description;

    /** The items weight */
    private int weight;

    /** Items edibility */
    private boolean isEdible;

    /*****************************************************************
    Initialize a new Item
    @param name The items name.
    @param description The items description.
    @param weight The items weight.
    @param isEdible Whether the item is edible.
    *****************************************************************/
    public Item (
        final String name,
        final String description,
        final int weight,
        final boolean isEdible
    ) {
        this.name = name;

        this.description = description;

        this.weight = weight;

        this.isEdible = isEdible;
    }

    /*****************************************************************
    Get the items name
    @return The items name
    *****************************************************************/
    public String getName() {
        return name;
    }

    /*****************************************************************
    Set the items name
    @param name The items name
    *****************************************************************/
    public void setName(String name) {
        this.name = name;
    }

    /*****************************************************************
    Set the items description
    @return the items description
    *****************************************************************/
    public String getDescription() {
        return description;
    }

    /*****************************************************************
    Set the items description
    @param description the items description
    *****************************************************************/
    public void setDescription(String description) {
        this.description = description;
    }

    /*****************************************************************
    Get the items weight
    @return the items weight
    *****************************************************************/
    public int getWeight() {
        return weight;
    }

    /*****************************************************************
    Set the items weight
    @param weight the items weight
    *****************************************************************/
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /*****************************************************************
    Check whether the item is edible
    @return whether the item is edible
    *****************************************************************/
    public boolean isEdible() {
        return isEdible;
    }

    /*****************************************************************
    Set the items edibility
    @param edible whether the item is edible or not
    *****************************************************************/
    public void setEdible(boolean edible) {
        isEdible = edible;
    }
}
