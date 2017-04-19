package game;

public class GameTest {
    public static void main (final String[] args) {
        try {
            Game game = new Game();

            game.initDataAccessObjects();

            game.cacheTheGameObjects();

            game.setLocationsItems();

            game.setLocationsNeighbors();

            assert game.getCorrectGameSuspect() != null :
                "Suspect did not load";

            assert game.getCorrectGameItem() != null :
                "Item did not load";

            assert game.getCorrectGameLocation() != null :
                "Location did not load";

            assert game.getSuspects().size() > 0 :
                "Suspects did not load";

            assert game.getLocations().size() > 0 :
                "Locations did not load";

            assert game.getItems().size() > 0 :
                "Items did not load";

            System.out.println("All tests passed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
