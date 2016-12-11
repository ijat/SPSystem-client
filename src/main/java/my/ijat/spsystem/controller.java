package my.ijat.spsystem;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class controller implements Initializable {

    Caller call = new Caller();
    private FXMLLoader fxmlLoader;
    private Timeline timeline;

    private Map<String, String> user_data;

    @FXML
    private TextField un, fn, ir, logun;

    @FXML
    private PasswordField pw, logpw;

    @FXML
    private Button regBtn, loBtn;

    @FXML
    public Label afn, auid, air, acount, aun;

    @FXML
    private void initialize() {
    }

    @FXML
    private void regEvent(ActionEvent event){
        try {
            Stage stage;
            Parent root2 = FXMLLoader.load(getClass().getClassLoader().getResource("scene-reg.fxml"));
            Scene scene2 = new Scene(root2);

            stage = (Stage) logpw.getScene().getWindow();
            stage.setScene(scene2);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void set(String s) {
        acount.setText(s);
    }

    private void update() throws Exception {
        if (user_data.get("uid").length() > 0) {
            String count = call.update(user_data.get("uid"), user_data.get("uhash"));
            //System.out.print(count);
            controller c = fxmlLoader.getController();
            c.set(count);
        }

    }

    private void openNewWindow(ActionEvent event)
    {
        Parent root;
        try {
            URL url = getClass().getClassLoader().getResource("scene-logged-main.fxml");
            this.fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            //System.out.print(user_data.get("fullname"));

            fxmlLoader.getNamespace()
                    .put("nafn", user_data.get("fullname"));

            fxmlLoader.getNamespace()
                    .put("nauid", user_data.get("uid"));

            fxmlLoader.getNamespace()
                    .put("nacount", user_data.get("counter"));

            fxmlLoader.getNamespace()
                    .put("nair", user_data.get("ir_id"));

            fxmlLoader.getNamespace()
                    .put("naun", user_data.get("username"));

            root = (Parent) fxmlLoader.load(url.openStream());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(client.title + " " + client.version);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(2),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    try {
                                        update();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logOK(ActionEvent event) {
        try {
            if (logun.getText().length() == 0 || logpw.getText().length() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(client.title);
                alert.setHeaderText("Alert");
                alert.setContentText("Please fill all empty boxes.");
                alert.showAndWait();
                return;
            }

            user_data = call.login(logun.getText(), logpw.getText());
            user_data.put("username", logun.getText());
            openNewWindow(event);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(client.title);
            alert.setHeaderText("Error");
            alert.setContentText("Wrong username/id or not registered.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void exxit(ActionEvent e) {
        System.exit(0);
    }

    @FXML
    private void regBack(ActionEvent event) {
        try {
            Stage stage;
            Parent root2 = FXMLLoader.load(getClass().getClassLoader().getResource("scene-main.fxml"));
            Scene scene2 = new Scene(root2);

            try {
                stage = (Stage) regBtn.getScene().getWindow();
            } catch (Exception e) {
                stage = (Stage) loBtn.getScene().getWindow();
            }
            stage.setScene(scene2);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regOK(ActionEvent event) {
        try {
            if (un.getText().length() == 0 || pw.getText().length() ==0 || fn.getText().length() ==0 || ir.getText().length() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(client.title);
                alert.setHeaderText("Error");
                alert.setContentText("Please fill all empty boxes.");
                alert.showAndWait();
                return;
            }

                call.newReg(un.getText(), pw.getText(), fn.getText(), ir.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(client.title);
                alert.setHeaderText("Registration completed!");
                alert.setContentText("Press OK to go back to login screen.");
                alert.showAndWait();
                regBack(null);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(client.title);
            alert.setHeaderText("Error");
            alert.setContentText("Please change username and/or recheck box id.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {}
}
