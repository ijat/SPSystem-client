package my.ijat.spsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class client extends Application {

    private Stage primaryStage;
    private Scene scene1;
    private Parent root1;

    public static String title = "SPSystem" ;
    public static String sub_title = "Client Mode";
    public static String version = "0.1";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        root1 = FXMLLoader.load(getClass().getClassLoader().getResource("scene-main.fxml"));

        scene1 = new Scene(root1);

        this.primaryStage.setScene(scene1);

        this.primaryStage.setTitle(title + " " + version);

        this.primaryStage.setMaxHeight(400);
        this.primaryStage.setMaxWidth(400);
        //this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
