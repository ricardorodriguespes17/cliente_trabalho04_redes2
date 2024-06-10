package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

  public static String chatId = null;
  private Chat chat;
  private boolean menuIsOpen = false;
  private TextField inputSearch;

  @FXML
  private void sendMessage() {
    String value = inputMessage.getText();
    User user = App.getUser();

    if (value.trim().equals(""))
      return;

    LocalDateTime currentTime = LocalDateTime.now();

    chat.addMessage(new Message(value.trim(), user.getUserId(), currentTime));
    inputMessage.setText("");
    renderMessages(null);
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
    App.removeChat(chat);
    goToMainScreen();
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
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
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
    String userId = message.getUserId();
    User user = App.getUser();

    messageTimeLabel.getStyleClass().add("messageTime");
    hbox.getStyleClass().add("messageTextBox");

    if (selected) {
      parent.getStyleClass().add("selected");
    }

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

  private void renderMessages(List<Message> searchedMessages) {
    mainBox.getChildren().clear();
    final HBox[] boxSearcheds = { null };

    for (Message message : chat.getMessages()) {
      message.setRead(true);
      boolean selected = searchedMessages != null
          && searchedMessages.size() > 0
          && searchedMessages.contains(message);
      HBox hbox = createMessageBox(new Message(message.getText(), message.getUserId(), message.getDateTime()),
          selected);

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
    chat = App.getChatById(chatId);
    chatNameLabel.setText(chat.getChatName());
    renderMessages(null);
  }

}
