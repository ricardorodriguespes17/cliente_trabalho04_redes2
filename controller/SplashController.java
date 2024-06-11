/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 10/06/2024
* Nome.............: SplashController
* Funcao...........: Controla a tela de splash.
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.App;

public class SplashController implements Initializable {

  @FXML
  private VBox mainBox;

  private App app;
  private ChangeListener<Boolean> listener;

  private void goToMainScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) mainBox.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createLoadingIndicator() {
    ProgressIndicator loadingIndicator = new ProgressIndicator();

    loadingIndicator.getStyleClass().add("progressIndicator");
    mainBox.getStyleClass().add("loadingBox");

    mainBox.getChildren().add(loadingIndicator);
  }

  private void initApp() {
    app = App.getInstance();

    listener = (obs, wasLoading, isLoading) -> {
      if (!isLoading) {
        app.isLoadingProperty().removeListener(listener);
        goToMainScreen();
      }
    };

    app.isLoadingProperty().addListener(listener);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    createLoadingIndicator();
    initApp();
  }
}
