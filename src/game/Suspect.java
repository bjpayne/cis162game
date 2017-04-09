package game;

public class Suspect {

    /** The Characters name */
    private String name;

    /*****************************************************************
    Initialize a new Suspect
    @param name The items name.
    *****************************************************************/
    public Suspect(final String name) {
        this.name = name;
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
