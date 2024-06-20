/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 12/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: JoinChatController
* Funcao...........: Controla a tela de entrar em um chat.
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;
import model.Chat;

public class JoinChatController implements Initializable {

  @FXML
  TextField inputCode;
  @FXML
  Button buttonJoin;
  @FXML
  Label errorLabel;

  private App app;
  private ChangeListener<Boolean> listener;

  @FXML
  private void onJoin() {
    if (app.isLoading())
      return;

    String chatName = inputCode.getText().trim();

    if (chatName.equals(""))
      return;

    app.join(chatName);

    buttonJoin.setText("Entrando...");
    buttonJoin.setDisable(true);
    inputCode.setDisable(true);

    listener = (obs, wasLoading, isLoading) -> {
      if (!isLoading) {
        app.isLoadingProperty().removeListener(listener);
        if (app.getError() != null) {
          errorLabel = new Label(app.getError());
        } else {
          ChatController.chatName = chatName;
          Chat chat = new Chat(chatName);
          app.addChat(chat);
          goToNewChat();
        }
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
      Stage stage = (Stage) inputCode.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void goToNewChat() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ChatScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) inputCode.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorLabel.setText("");
    app = App.getInstance();
  }

}
