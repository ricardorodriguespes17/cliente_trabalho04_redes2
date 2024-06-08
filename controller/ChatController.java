package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Message;

public class ChatController implements Initializable {

  @FXML
  ScrollPane scrollMainBox;
  @FXML
  VBox mainBox;
  @FXML
  Label chatNameLabel;

  public static String chatId = null;

  @FXML
  private void goToMainScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private HBox createMessageBox(Message message) {
    HBox parent = new HBox();
    HBox hbox = new HBox();
    Label messageLabel = new Label(message.getText());
    String userId = message.getUserId();

    if (userId.equals("12")) {
      parent.getStyleClass().add("selfMessageBox");
    } else if (userId.equals("server")) {
      parent.getStyleClass().add("serverMessageBox");
    } else {
      parent.getStyleClass().add("otherMessageBox");
    }

    hbox.getChildren().add(messageLabel);
    parent.getChildren().add(hbox);

    return parent;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    generateRandomMessages();
    chatNameLabel.setText(chatId);
  }

  private void generateRandomMessages() {
    mainBox.getChildren().clear();

    mainBox.getChildren().add(createMessageBox(new Message("Início do chat", "server", "14:30")));

    for (int i = 1; i <= 30; i++) {
      String userId = "11";
      double random = Math.random() * 6;

      if (random <= 3) {
        userId = "12";
      }

      HBox hbox = createMessageBox(new Message("olá, mundo!", userId, "19:30"));
      mainBox.getChildren().add(hbox);
      scrollMainBox.setVvalue(1.0);
    }
  }
}
