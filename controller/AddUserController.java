package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;
import model.User;

public class AddUserController implements Initializable {

  @FXML
  TextField inputIp, inputName;
  @FXML
  Button saveButton;
  @FXML
  Label errorLabel;

  public static String userIp;
  private App app;

  @FXML
  private void onSaveUser() {
    String ip = inputIp.getText().trim();
    String name = inputName.getText().trim();

    if (ip.equals("") || name.equals(""))
      return;

    if (!ipValidation(ip)) {
      errorLabel.setText("IP inválido");
      errorLabel.getStyleClass().add("active");
      return;
    }

    errorLabel.getStyleClass().remove("active");

    User user = app.getUserByIp(ip);

    if(user == null) {
      user = new User(ip, name);
      app.addUser(user);
    } else {
      //TODO - update user
    }

    goBack();
  }

  private boolean ipValidation(String value) {
    System.out.println(value);

    boolean result = true;
    String[] valueSplited = value.split("\\.");

    if (valueSplited.length != 4)
      return false;

    try {
      for (String split : valueSplited) {
        int number = Integer.parseInt(split);
        if (number < 0 || number > 256) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }

    return result;
  }

  @FXML
  private void goBack() {
    if (ChatController.chatId == null) {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ContactsScreen.fxml"));
        Parent newScreen = fxmlLoader.load();
        Stage stage = (Stage) inputIp.getScene().getWindow();
        stage.setTitle("Chat");
        stage.setScene(new Scene(newScreen));
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ChatScreen.fxml"));
        Parent newScreen = fxmlLoader.load();
        Stage stage = (Stage) inputIp.getScene().getWindow();
        stage.setTitle("Chat");
        stage.setScene(new Scene(newScreen));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    app = App.getInstance();

    if (userIp != null) {
      User user = app.getUserByIp(userIp);

      if (user != null) {
        inputName.setText(user.getName());
      }

      inputIp.setText(userIp);
      inputIp.setDisable(true);
      inputIp.setEditable(false);
    }
  }

}
