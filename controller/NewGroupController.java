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
  TextField inputChatName;
  @FXML
  TextArea inputDescription;

  @FXML
  private void onCreate() {
    String chatName = inputChatName.getText().trim();
    String description = inputDescription.getText().trim();

    if (chatName.equals(""))
      return;

    Chat chat = new Chat(chatName, description);
    App.addChat(chat);
    ChatController.chatId = chat.getChatId();
    goToNewChat();
  }

  @FXML
  private void goToMainScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) inputChatName.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void goToNewChat() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ChatScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) inputChatName.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

}
