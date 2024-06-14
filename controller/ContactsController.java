/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 13/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: ChatController
* Funcao...........: Controla a tela de listagem de contatos.
*************************************************************** */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.App;
import model.User;

public class ContactsController implements Initializable {

  @FXML
  VBox mainBox;

  App app;
  User user = null;

  @FXML
  private void goToAddScreen() {
    openScreen("../view/AddUserScreen.fxml");
  }

  @FXML
  private void goToMainScreen() {
    openScreen("../view/MainScreen.fxml");
  }

  private void openScreen(String path) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) mainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void confirmRemoveContact() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("");
    alert.setContentText("Deseja realmente excluir esse contato?");
    alert.setGraphic(null);

    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

    alert.getDialogPane().getStylesheets().addAll(
        getClass().getResource("../view/css/common.css").toExternalForm(),
        getClass().getResource("../view/css/dark.css").toExternalForm());

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.YES) {
        onRemoveContact();
      }
    });
  }

  private void onRemoveContact() {
    app.removeUser(user);
    renderContacts();
  }

  private HBox createContactBox(User user) {
    Label labelTitle = new Label(user.getName());
    Label labelIp = new Label(user.getUserIp());
    VBox vbox = new VBox(labelTitle, labelIp);
    Region regionSepator = new Region();
    Button buttonRemove = new Button();
    SVGPath svgIcon = new SVGPath();
    svgIcon.setContent(
        "m6.084 0.797 -0.324 0.644H1.44C0.643 1.44 0 2.083 0 2.88s0.643 1.44 1.44 1.44h17.28c0.797 0 1.44 -0.643 1.44 -1.44s-0.643 -1.44 -1.44 -1.44H14.4l-0.324 -0.643A1.432 1.432 0 0 0 12.789 0H7.371c-0.544 0 -1.044 0.306 -1.287 0.797zM18.72 5.76H1.44l0.954 15.255a2.16 2.16 0 0 0 2.156 2.025H15.611a2.16 2.16 0 0 0 2.156 -2.025z");
    buttonRemove.setGraphic(svgIcon);
    HBox hbox = new HBox(vbox, regionSepator, buttonRemove);

    HBox.setHgrow(regionSepator, Priority.ALWAYS);
    labelTitle.getStyleClass().add("title");
    hbox.getStyleClass().add("boxContact");
    vbox.getStyleClass().add("main");

    buttonRemove.setOnAction(event -> {
      this.user = user;
      confirmRemoveContact();
    });

    return hbox;
  }

  private void renderContacts() {
    mainBox.getChildren().clear();

    for (User user : app.getUsers()) {
      HBox hbox = createContactBox(user);
      mainBox.getChildren().add(hbox);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    app = App.getInstance();
    renderContacts();
  }

}
