/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 04/06/2024
* Ultima alteracao.: 06/06/2024
* Nome.............: Controller
* Funcao...........: Controla a tela principal da aplicação.
*************************************************************** */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.App;
import model.Chat;
import model.Message;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController implements Initializable {

  @FXML
  ScrollPane scrollMainBox;
  @FXML
  VBox mainBox;

  private HBox createChatBox(String chatId, String chatName, String lastMessage, String lastMessageTime,
      int lastMessagesCount) {
    HBox hbox = new HBox();
    VBox vbox1 = new VBox();
    VBox vbox2 = new VBox();
    Label titleLabel = new Label(chatName);
    Label subTitleLabel = new Label(lastMessage);
    Label timeLabel = new Label(lastMessageTime);
    Label countLabel = new Label(lastMessagesCount + "");

    titleLabel.getStyleClass().add("title");
    subTitleLabel.getStyleClass().add("lastMessageTime");
    timeLabel.getStyleClass().add("lastMessageTime");
    countLabel.getStyleClass().add("messagesCount");
    if (lastMessagesCount > 0) {
      timeLabel.getStyleClass().add("active");
      countLabel.getStyleClass().add("active");
    }

    vbox2.getStyleClass().add("lastMessageBox");
    vbox1.getChildren().addAll(titleLabel, subTitleLabel);
    vbox2.getChildren().addAll(timeLabel, countLabel);

    HBox.setHgrow(vbox1, Priority.ALWAYS);
    hbox.getChildren().addAll(vbox1, vbox2);
    hbox.setOnMouseClicked(this::openChat);
    hbox.setId(chatId);

    applyTransitionEffect(hbox);

    return hbox;
  }

  private void applyTransitionEffect(HBox hbox) {
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), hbox);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.6);

    hbox.setOnMouseEntered(event -> {
      fadeTransition.stop();
      fadeTransition.playFromStart();
    });

    hbox.setOnMouseExited(event -> {
      fadeTransition.stop();
      fadeTransition.setRate(-1);
      fadeTransition.play();
    });
  }

  private void openChat(MouseEvent event) {
    HBox chatBox = (HBox) event.getSource();

    ChatController.chatId = chatBox.getId();

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/ChatScreen.fxml"));
      Parent newScreen = fxmlLoader.load();
      Stage stage = (Stage) chatBox.getScene().getWindow();
      stage.setTitle("Chat");
      stage.setScene(new Scene(newScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void renderChats() {
    mainBox.getChildren().clear();

    for (Chat chat : App.getChats()) {
      Message lastMessage = chat.getLastMessage();
      int unreadsCount = chat.getNumberOfMessagesUnread();

      HBox hbox = createChatBox(chat.getChatId(), chat.getChatName(), lastMessage.getText(), lastMessage.getDateTime(),
          unreadsCount);
      mainBox.getChildren().add(hbox);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    renderChats();
  }

  private void generateRandomChats() {
    mainBox.getChildren().clear();

    for (int i = 1; i <= 10; i++) {
      int count = (int) Math.floor(Math.random() * 5);

      HBox hbox = createChatBox("redes-" + i, "Redes " + i, "Marlos: Hello world!", "10:30", count);
      mainBox.getChildren().add(hbox);
    }
  }
}
