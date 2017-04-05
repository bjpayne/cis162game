package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/*****************************************************************
A ridiculous game
@author Ben Payne
@version 4/4/2017.
******************************************************************/
public class Game extends Application {

    /*****************************************************************
    Start the GUI
    @param primaryStage The gui stage
    @throws Exception throws a base exception
    *****************************************************************/
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));

        Parent root = loader.load();

        Controller controller = loader.getController();

        primaryStage.setTitle("Game");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    /*****************************************************************
    @param args Main arguments passed to application
    *****************************************************************/
    public static void main(final String[] args) {
        launch(args);
    }
}
