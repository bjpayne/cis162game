package game;

public class LocationNeighbors {

    /** The location id */
    private int location_id;

    /** The neighboring location id */
    private int neighbor_id;

    /*****************************************************************
    Get the location id
    @return int The locations id
    *****************************************************************/
    public int getLocation_id() {
        return location_id;
    }

    /*****************************************************************
    Set the location id
    @param int the locations id
    *****************************************************************/
    public void setLocation_id(final int location_id) {
        this.location_id = location_id;
    }

    /*****************************************************************
    Get the neighboring location id
    @return int the neighboring locations id
    *****************************************************************/
    public int getNeighbor_id() {
        return neighbor_id;
    }

    /*****************************************************************
    Set the neighboring location id
    @param int The
    *****************************************************************/
    public void setNeighbor_id(final int neighbor_id) {
        this.neighbor_id = neighbor_id;
    }
}
