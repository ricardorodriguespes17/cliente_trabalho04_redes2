/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 09/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: NewChatController
* Funcao...........: Controla a tela criação de chat.
*************************************************************** */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;
import model.Chat;

public class NewChatController implements Initializable {

  @FXML
  TextField inputChatName;
  @FXML
  TextArea inputDescription;
  @FXML
  Button buttonCreate;

  private App app;
  private ChangeListener<Boolean> listener;

  @FXML
  private void onCreate() {
    if (app.isLoading())
      return;

    String chatName = inputChatName.getText().trim();
    String description = inputDescription.getText().trim();

    if (chatName.equals(""))
      return;

    Chat chat = new Chat(chatName, description);
    ChatController.chatId = chat.getChatId();
    app.addChat(chat);
    app.create(chat.getChatId(), chatName);

    buttonCreate.setText("Criando...");
    buttonCreate.setDisable(true);
    inputChatName.setDisable(true);
    inputDescription.setDisable(true);

    listener = (obs, wasLoading, isLoading) -> {
      if (!isLoading) {
        app.isLoadingProperty().removeListener(listener);
        goToNewChat();
      }
    };

    app.isLoadingProperty().addListener(listener);
  }

  @FXML
  private void goToMainScreen() {
    if (app.isLoading())
      return;

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
    app = App.getInstance();
  }

}
