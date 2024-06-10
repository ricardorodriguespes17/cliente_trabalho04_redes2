/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 07/06/2024
* Ultima alteracao.: 10/06/2024
* Nome.............: Principal
* Funcao...........: Inicia o programa e a interface grafica.
*************************************************************** */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Principal extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/MainScreen.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.getIcons().add(new Image("view/img/logo.png"));
    stage.setTitle("Instant Messaging");
    stage.setOnCloseRequest(t -> {
      Platform.exit();
      System.exit(0);
    });
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
