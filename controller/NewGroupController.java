package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;
import model.Chat;

public class NewGroupController implements Initializable {

  @FXML
  TextField inputChatId, inputChatName;
  @FXML
  TextArea inputDescription;

  @FXML
  private void onCreate() {
    String chatId = inputChatId.getText().trim();
    String chatName = inputChatName.getText().trim();
    String description = inputDescription.getText().trim();

    if (chatId.equals("") || chatName.equals(""))
      return;

    Chat chat = new Chat(chatId, chatName, description);
    App.addChat(chat);
    goToMainScreen();
  }

  private void goToMainScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) inputChatId.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

}
