package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import model.User;

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
    User user = App.getUser();

    if (value.trim().equals(""))
      return;

    LocalDateTime currentTime = LocalDateTime.now();

    chat.addMessage(new Message(value.trim(), user.getUserId(), currentTime));
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
    VBox vbox = new VBox();
    HBox hbox = new HBox();
    Label messageLabel = new Label(message.getText());
    Label messageTimeLabel = new Label(message.getTime());
    String userId = message.getUserId();
    User user = App.getUser();

    messageTimeLabel.getStyleClass().add("messageTime");
    hbox.getStyleClass().add("messageTextBox");

    if (userId.equals(user.getUserId())) {
      parent.getStyleClass().add("selfMessageBox");
      hbox.getChildren().add(messageTimeLabel);
    } else if (userId.equals("server")) {
      parent.getStyleClass().add("serverMessageBox");
    } else {
      User messageUser = App.getUserById(userId);
      parent.getStyleClass().add("otherMessageBox");
      Label userNameLabel = new Label(messageUser.getName());
      userNameLabel.getStyleClass().add("messageUserName");
      vbox.getChildren().add(userNameLabel);
      hbox.getChildren().add(messageTimeLabel);
    }

    messageLabel.setWrapText(true);

    hbox.getChildren().add(0, messageLabel);
    vbox.getChildren().add(hbox);
    parent.getChildren().add(vbox);

    return parent;
  }

  private void renderMessages() {
    mainBox.getChildren().clear();

    for (Message message : chat.getMessages()) {
      message.setRead(true);
      HBox hbox = createMessageBox(new Message(message.getText(), message.getUserId(), message.getDateTime()));
      mainBox.getChildren().add(hbox);
    }

    mainBox.heightProperty().addListener((observable, oldValue, newValue) -> {
      scrollMainBox.setVvalue(1.0);
    });

    Platform.runLater(() -> scrollMainBox.setVvalue(1.0));
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    chat = App.getChatById(chatId);
    chatNameLabel.setText(chat.getChatName());
    renderMessages();
  }

}
