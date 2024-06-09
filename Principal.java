/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 04/06/2024
* Ultima alteracao.: 06/06/2024
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
import model.App;
import model.Chat;
import model.Client;
import model.Message;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

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
    createRandonsChats();
    launch(args);
  }

  public static void createRandonsChats() {
    Chat chatRedes = new Chat("Redes II", "Grupo da matéria de Redes II 2024.1");

    User user = new User("10", "Ricardo");
    User user2 = new User("11", "Marlos");

    LocalDateTime today = LocalDateTime.now();
    LocalDateTime dateTime = today.minusDays(0);
    Message message = new Message("Olá mundo", user2.getUserId(), dateTime);

    chatRedes.addMessage(message);
    App.addUser(user);
    App.addUser(user2);
    App.setUser(user);
    App.addChat(chatRedes);
  }

  public void startClient() {
    String grupoId = "grupo-teste";
    User user = App.getUser();

    Client tcpClient = Client.createClient("TCP", "192.168.1.11", 6789);

    try {
      tcpClient.connect();
      tcpClient.receive();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    new Thread(() -> {
      try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
          tcpClient.send(grupoId, user.getName(), userInput);
        }
      } catch (IOException e) {
        System.out.println("> Erro na conexão com o servidor");
      }
    }).start();
  }
}
