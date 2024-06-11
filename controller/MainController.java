/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 07/06/2024
* Ultima alteracao.: 11/06/2024
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
import javafx.scene.shape.SVGPath;
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

  private App app;
  private TextField inputSearch;

  @FXML
  private void getToCreateChat() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewChatScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void goToJoinChat() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/JoinChatScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void openSearchBox() {
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

    if (text.trim().equals("")) {
      renderChats(null);
      return;
    }

    renderChats(app.getChatsByText(text));
  }

  private void escapeSearch() {
    Label title = new Label("Instant Messaging");

    HBox.setHgrow(regionSepator, Priority.ALWAYS);
    headerBox.getChildren().remove(0);
    headerBox.getChildren().add(0, title);
    buttonSearch.setOnAction(event -> {
      openSearchBox();
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
      chats = app.getChats();
    }

    mainBox.getChildren().clear();

    if (chats.size() == 0) {
      renderEmptyChats();
    }

    for (Chat chat : chats) {
      Message lastMessage = chat.getLastMessage();
      int unreadsCount = chat.getNumberOfMessagesUnread(App.LOCAL_IP);
      String userName = "";
      String messageTime = "";
      String textLastMessage = "";

      if (lastMessage != null) {
        messageTime = lastMessage.getDateToChat();
        User messageUser = app.getUserByIp(lastMessage.getUserIp());

        if (messageUser != null) {
          userName = messageUser.getName();
        }

        if (lastMessage.getUserIp().equals(App.LOCAL_IP)) {
          userName = "Você";
        }

        textLastMessage = userName + ": " + lastMessage.getText();
      }

      HBox hbox = createChatBox(chat.getChatId(), chat.getChatName(), textLastMessage, messageTime, unreadsCount);
      mainBox.getChildren().add(hbox);
    }
  }

  private void renderEmptyChats() {
    Label label1 = new Label("Está vazio por aqui");
    Label label2 = new Label("Crie ou entre em um grupo");
    SVGPath svgIcon = new SVGPath();

    svgIcon.setContent(
        "M3 11v8h.051c.245 1.692 1.69 3 3.449 3 1.174 0 2.074-.417 2.672-1.174a3.99 3.99 0 0 0 5.668-.014c.601.762 1.504 1.188 2.66 1.188 1.93 0 3.5-1.57 3.5-3.5V11c0-4.962-4.037-9-9-9s-9 4.038-9 9zm6 1c-1.103 0-2-.897-2-2s.897-2 2-2 2 .897 2 2-.897 2-2 2zm6-4c1.103 0 2 .897 2 2s-.897 2-2 2-2-.897-2-2 .897-2 2-2z");
    svgIcon.setScaleX(4);
    svgIcon.setScaleY(4);
    VBox vbox = new VBox(label1, svgIcon, label2);
    label1.getStyleClass().add("title");
    vbox.getStyleClass().add("emptyBox");

    mainBox.getChildren().add(vbox);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    app = App.getInstance();
    renderChats(null);
  }

}
