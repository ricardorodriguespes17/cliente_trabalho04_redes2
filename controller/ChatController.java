/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 07/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: ChatController
* Funcao...........: Controla a tela do chat.
*************************************************************** */

package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
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
  @FXML
  Button buttonMenu, buttonGoBack;
  @FXML
  Region regionSeparator;
  @FXML
  HBox headerBox;

  private App app;
  public static String chatId = null;
  private Chat chat;
  private boolean menuIsOpen = false;
  private TextField inputSearch;
  private ChangeListener<Boolean> listener;
  ChangeListener<Boolean> messageSendListener;

  @FXML
  private void sendMessage() {
    String value = inputMessage.getText();

    if (value.trim().equals(""))
      return;

    LocalDateTime currentTime = LocalDateTime.now();

    Message message = new Message(value.trim(), App.LOCAL_IP, currentTime);
    messageSendListener = (obs, wasSend, isSend) -> {
      message.isSendProperty().removeListener(messageSendListener);
      renderMessages(null);
    };
    message.isSendProperty().addListener(messageSendListener);
    chat.addMessage(message);
    app.send(chat, message);
    inputMessage.setText("");
  }

  @FXML
  void openMenu(ActionEvent event) {
    if (menuIsOpen)
      return;

    menuIsOpen = true;

    ContextMenu menu = new ContextMenu();
    MenuItem menuSeach = new MenuItem("Pesquisar");
    MenuItem menuSeeDetails = new MenuItem("Ver detalhes");
    MenuItem menuLeaveGroup = new MenuItem("Sair do grupo");

    double screenX = buttonMenu.localToScreen(buttonMenu.getBoundsInLocal()).getMinX();
    double screenY = buttonMenu.localToScreen(buttonMenu.getBoundsInLocal()).getMaxY();
    menu.getItems().addAll(menuSeach, menuSeeDetails, menuLeaveGroup);
    double menuWidth = menu.prefWidth(-1);
    menu.show(buttonMenu, screenX - menuWidth, screenY);

    menuSeach.setOnAction(value -> onSearchRequest());
    menuSeeDetails.setOnAction(value -> onSeeDetails());
    menuLeaveGroup.setOnAction(value -> confirmLeaveGroup());

    menu.setOnHidden(value -> {
      menuIsOpen = false;
    });
  }

  private void onSearchRequest() {
    headerBox.getChildren().clear();
    inputSearch = new TextField();
    inputSearch.setPromptText("Pesquisar...");

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

    HBox.setHgrow(inputSearch, Priority.ALWAYS);
    headerBox.getChildren().add(inputSearch);
  }

  private void onSearch() {
    String text = inputSearch.getText();

    if (text.trim().equals("")) {
      renderMessages(null);
      return;
    }

    renderMessages(chat.getMessagesByText(text));
  }

  private void escapeSearch() {
    headerBox.getChildren().clear();
    headerBox.getChildren().addAll(buttonGoBack, chatNameLabel, regionSeparator, buttonMenu);
  }

  private void onLeaveGroup() {
    if (app.isLoading())
      return;

    app.removeChat(chat);
    app.leave(chat);

    listener = (obs, wasLoading, isLoading) -> {
      if (!isLoading) {
        goToMainScreen();
        app.isLoadingProperty().removeListener(listener);
      }
    };
    app.isLoadingProperty().addListener(listener);
  }

  private void onSeeDetails() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setGraphic(null);
    alert.setTitle("Dados do grupo");
    alert.setHeaderText(chat.getChatName());

    String details = "";
    String description = chat.getDescription();

    if (description == null) {
      description = "(Vazio)";
    }

    details += "ID:\n" + chatId + "\n\n";
    details += "Descrição:\n" + description + "\n\n";
    details += "Membros:\n" + "Marlos, Ricardo, Gil, Adryellen, Vitor, Ana Beatriz";

    alert.setContentText(details);
    alert.getButtonTypes().setAll(ButtonType.OK);
    alert.getDialogPane().getStylesheets().addAll(
        getClass().getResource("../view/css/common.css").toExternalForm(),
        getClass().getResource("../view/css/dark.css").toExternalForm());

    alert.show();
  }

  private void confirmLeaveGroup() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("");
    alert.setContentText("Deseja realmente sair do grupo?");
    alert.setGraphic(null);

    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

    alert.getDialogPane().getStylesheets().addAll(
        getClass().getResource("../view/css/common.css").toExternalForm(),
        getClass().getResource("../view/css/dark.css").toExternalForm());

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.YES) {
        onLeaveGroup();
      }
    });

  }

  @FXML
  private void goToMainScreen() {
    ChatController.chatId = null;

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openAddUserScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddUserScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) scrollMainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private HBox createMessageBox(Message message, boolean selected) {
    HBox parent = new HBox();
    VBox vbox = new VBox();
    HBox hbox = new HBox();
    Label messageLabel = new Label(message.getText());
    Label messageTimeLabel = new Label(message.getTime());
    String userIp = message.getUserIp();

    messageTimeLabel.getStyleClass().add("messageTime");
    hbox.getStyleClass().add("messageTextBox");

    if (selected) {
      parent.getStyleClass().add("selected");
    }

    if (!message.isSend()) {
      SVGPath svgIconSending = new SVGPath();
      svgIconSending.setContent(
          "M 24 4 C 12.972066 4 4 12.972074 4 24 C 4 35.027926 12.972066 44 24 44 C 35.027934 44 44 35.027926 44 24 C 44 12.972074 35.027934 4 24 4 z M 24 7 C 33.406615 7 41 14.593391 41 24 C 41 33.406609 33.406615 41 24 41 C 14.593385 41 7 33.406609 7 24 C 7 14.593391 14.593385 7 24 7 z M 23.476562 11.978516 A 1.50015 1.50015 0 0 0 22 13.5 L 22 25.5 A 1.50015 1.50015 0 0 0 23.5 27 L 31.5 27 A 1.50015 1.50015 0 1 0 31.5 24 L 25 24 L 25 13.5 A 1.50015 1.50015 0 0 0 23.476562 11.978516 z");
      Button button = new Button();
      button.setGraphic(svgIconSending);
      hbox.getChildren().add(button);
    }

    if (userIp.equals(App.LOCAL_IP)) {
      parent.getStyleClass().add("selfMessageBox");
      hbox.getChildren().add(0, messageTimeLabel);
    } else if (userIp.equals(App.SERVER_IP)) {
      for(User user : app.getUsers()) {
        String contactMessage = message.getText().replace(user.getUserIp(), user.getName());
        messageLabel = new Label(contactMessage);
      }

      parent.getStyleClass().add("serverMessageBox");
    } else {
      User messageUser = app.getUserByIp(userIp);
      String username;

      if (messageUser != null) {
        username = messageUser.getName();
      } else {
        username = userIp;
      }

      Label userNameLabel = new Label(username);

      parent.getStyleClass().add("otherMessageBox");
      userNameLabel.getStyleClass().add("messageUserName");

      userNameLabel.setOnMouseClicked(event -> {
        AddUserController.userIp = userIp;
        openAddUserScreen();
      });

      vbox.getChildren().add(userNameLabel);
      hbox.getChildren().add(messageTimeLabel);
    }

    messageLabel.setWrapText(true);

    hbox.getChildren().add(0, messageLabel);
    vbox.getChildren().add(hbox);
    parent.getChildren().add(vbox);

    return parent;
  }

  private void renderMessages(List<Message> searchedMessages) {
    mainBox.getChildren().clear();
    final HBox[] boxSearcheds = { null };

    for (Message message : chat.getMessages()) {
      message.setRead(true);
      boolean selected = searchedMessages != null
          && searchedMessages.size() > 0
          && searchedMessages.contains(message);
      HBox hbox = createMessageBox(message, selected);

      if (selected)
        boxSearcheds[0] = hbox;
      mainBox.getChildren().add(hbox);
    }

    mainBox.heightProperty().addListener((observable, oldValue, newValue) -> {
      scrollMainBox.setVvalue(1.0);
    });

    Platform.runLater(() -> {
      if (searchedMessages == null || searchedMessages.size() == 0) {
        scrollMainBox.setVvalue(1.0);
      } else {
        double vValue = boxSearcheds[0].getLayoutY() / mainBox.getHeight();
        System.out.println(vValue + " " + boxSearcheds[0].getLayoutY());
        scrollMainBox.setVvalue(vValue);
      }
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    scrollMainBox.setFitToWidth(true);
    app = App.getInstance();
    chat = app.getChatById(chatId);
    if (chat != null) {
      chatNameLabel.setText(chat.getChatName());
    } else {
      goToMainScreen();
    }
    renderMessages(null);

    chat.getMessages().addListener((message) -> {
      Platform.runLater(() -> {
        if (ChatController.chatId != null) {
          renderMessages(null);
        }
      });
    });

    chat.getChatNameProperty().addListener((chatName) -> {
      Platform.runLater(() -> {
        if (ChatController.chatId != null) {
          chatNameLabel.setText(chat.getChatName());
        }
      });
    });
  }

}
