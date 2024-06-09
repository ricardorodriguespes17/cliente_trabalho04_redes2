package controller;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.App;
import model.Chat;
import model.Message;

public class ChatController implements Initializable {

  @FXML
  ScrollPane scrollMainBox;
  @FXML
  VBox mainBox;
  @FXML
  Label chatNameLabel;
  @FXML
  TextField inputMessage;

  public static String chatId = null;
  private Chat chat;

  @FXML
  private void sendMessage() {
    String value = inputMessage.getText();

    if (value.trim() == "")
      return;

    LocalTime currentTime = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String formattedTime = currentTime.format(formatter);

    chat.addMessage(new Message(value.trim(), "10", formattedTime));
    inputMessage.setText("");
    renderMessages();
  }

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

    if (userId.equals("10")) {
      parent.getStyleClass().add("selfMessageBox");
    } else if (userId.equals("server")) {
      parent.getStyleClass().add("serverMessageBox");
    } else {
      parent.getStyleClass().add("otherMessageBox");
    }

    messageLabel.setWrapText(true);

    hbox.getChildren().add(messageLabel);
    parent.getChildren().add(hbox);

    return parent;
  }

  private void renderMessages() {
    mainBox.getChildren().clear();

    for (Message message : chat.getMessages()) {
      message.setRead(true);
      HBox hbox = createMessageBox(new Message(message.getText(), message.getUserId(), message.getDateTime()));
      mainBox.getChildren().add(hbox);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    chat = App.getChatById(chatId);
    chatNameLabel.setText(chat.getChatName());
    renderMessages();
    scrollMainBox.setVvalue(1.0);
  }

}
