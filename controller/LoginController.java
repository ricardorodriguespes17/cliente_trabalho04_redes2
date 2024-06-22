/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 21/06/2024
* Ultima alteracao.: 21/06/2024
* Nome.............: LoginController
* Funcao...........: Classe que controla a tela de login.
*************************************************************** */

package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.App;

public class LoginController implements Initializable {

  @FXML
  TextField inputIp;
  @FXML
  Label errorLabel;
  @FXML
  Button buttonLogin;

  private ChangeListener<Boolean> listener;

  @FXML
  private void onLogin() {
    buttonLogin.setDisable(true);
    inputIp.setDisable(true);

    String ip = inputIp.getText().trim();

    if (ip.equals(""))
      return;

    App app = App.getInstance();
    app.createClients(ip);

    listener = (obs, wasLoading, isLoading) -> {
      if (!isLoading) {
        if (app.getError() == null) {
          goToMainScreen();
          app.isLoadingProperty().removeListener(listener);
        } else {
          errorLabel.setText(app.getError());
          errorLabel.setVisible(true);
          buttonLogin.setDisable(false);
          inputIp.setDisable(false);
          app.setError(null);
        }
      }
    };

    app.isLoadingProperty().addListener(listener);
  }

  private void goToMainScreen() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/MainScreen.fxml"));
      Parent mainScreen = fxmlLoader.load();
      Stage stage = (Stage) inputIp.getScene().getWindow();
      stage.setScene(new Scene(mainScreen));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

}