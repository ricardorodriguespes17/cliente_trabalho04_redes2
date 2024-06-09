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
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.App;
import model.Chat;
import model.Message;
import model.User;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController implements Initializable {

  @FXML
  ScrollPane scrollMainBox;
  @FXML
  HBox headerBox;
  @FXML
  Region regionSepator;
  @FXML
  Button buttonSearch;
  @FXML
  VBox mainBox;

  TextField inputSearch;

  @FXML
  private void goToCreateGroup() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewGroupScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void openSearhBox() {
    inputSearch = new TextField();
    HBox.setHgrow(inputSearch, Priority.ALWAYS);
    HBox.setHgrow(regionSepator, Priority.NEVER);

    headerBox.getChildren().remove(0);
    headerBox.getChildren().add(0, inputSearch);
    inputSearch.setOnKeyReleased(event -> {
      if (event.getCode().equals(KeyCode.ESCAPE)) {
        escapeSearch();
      } else {
        onSearch();
      }
    });
    inputSearch.setOnAction(event -> {
      escapeSearch();
    });
  }

  private void onSearch() {
    String text = inputSearch.getText();

    if (text.trim() == "") {
      renderChats(null);
      return;
    }

    renderChats(App.getChatsByText(text));
  }

  private void escapeSearch() {
    Label title = new Label("Instant Messaging");

    HBox.setHgrow(regionSepator, Priority.ALWAYS);
    headerBox.getChildren().remove(0);
    headerBox.getChildren().add(0, title);
    buttonSearch.setOnAction(event -> {
      openSearhBox();
    });
  }

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

  private void renderChats(List<Chat> listChat) {
    List<Chat> chats = listChat;

    if (listChat == null) {
      chats = App.getChats();
    }

    mainBox.getChildren().clear();
    for (Chat chat : chats) {
      Message lastMessage = chat.getLastMessage();
      int unreadsCount = chat.getNumberOfMessagesUnread();
      String userName = "";
      String messageTime = "";
      String textLastMessage = "";
      User user = App.getUser();

      if (lastMessage != null) {
        messageTime = lastMessage.getDateToChat();
        User messageUser = App.getUserById(lastMessage.getUserId());

        if (messageUser != null) {
          userName = messageUser.getName();
        }

        if (lastMessage.getUserId().equals(user.getUserId())) {
          userName = "Você";
        }

        textLastMessage = userName + ": " + lastMessage.getText();
      }

      HBox hbox = createChatBox(chat.getChatId(), chat.getChatName(), textLastMessage, messageTime, unreadsCount);
      mainBox.getChildren().add(hbox);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    renderChats(null);
  }

}
