package Visualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the Main class used to build and run the GUI.
 */
public class Main extends Application {

    // fixed parameters for the GUI size
    public final int FIXED_WIDTH = 975;
    public final int FIXED_LENGTH = 1800;


    /**
     * This method is used in order to set up the main stage for the GUI.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Visualizer/GridViewer.fxml"));
        primaryStage.setTitle("Path Finder Visualizer");
        primaryStage.setScene(new Scene(root, FIXED_LENGTH, FIXED_WIDTH));
        primaryStage.show();
    } // ends the start() method


    /**
     * This method is used in order to run and start the GUI
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch(args);
    } // ends the main() method
} // ends the Main class
