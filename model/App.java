/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 14/06/2024
* Nome.............: App
* Funcao...........: Gerencia os cruds e informações da aplicação.
*************************************************************** */

package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.service.Client;

public class App {
  private static App instance;
  private String serverIp = "127.0.0.1";
  private String localIp = "127.0.0.1";
  private List<Chat> chats;
  private List<User> users;
  private Client tcpClient;
  private Client udpClient;
  private BooleanProperty isLoading;
  private String error;

  public App() {
    isLoading = new SimpleBooleanProperty(false);
    error = null;
    chats = new ArrayList<>();
    users = new ArrayList<>();
  }

  public static App getInstance() {
    if (instance == null) {
      instance = new App();
    }
    return instance;
  }

  public void createClients(String serverIp) {
    String serverVariableIp = getVariableServerIP();
    String mode = getVariableMode();

    if(serverVariableIp != null) {
      serverIp = serverVariableIp;
    }

    System.out.println("> IP do Servidor: " + serverIp);
    System.out.println("> Modo: " + mode);

    if (mode != null && mode.equals("TEST")) {
      tcpClient = Client.createClient(this, mode, serverIp, 6789);
      udpClient = Client.createClient(this, mode, serverIp, 6790);
    } else {
      tcpClient = Client.createClient(this, "TCP", serverIp, 6789);
      udpClient = Client.createClient(this, "UDP", serverIp, 6790);
      startClient();
    }

  }

  private String getVariableServerIP() {
    ReadFile readFile = new ReadFile();

    return readFile.getVariable("SERVER_IP");
  }

  private String getVariableMode() {
    ReadFile readFile = new ReadFile();

    return readFile.getVariable("MODE");
  }

  public void startClient() {
    System.out.println("> Conectando ao servidor");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.connect();
        udpClient.connect();
        tcpClient.receive();
        setError(null);
      } catch (IOException e) {
        setError("Houve um problema ao conectar o servidor");
        System.out.println("> Erro: Houve um problema ao conectar o servidor");
        return;
      } finally {
        Platform.runLater(() -> {
          setLoading(false);
        });
      }

      System.out.println("> Server iniciado");
    }).start();
  }

  public void send(Chat chat, Message message) {
    System.out.println("> Enviando send para o server");

    new Thread(() -> {
      try {
        udpClient.send(chat.getChatName(), localIp, message.getText());
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("> Erro: Falha ao enviar um send para servidor");
        Platform.runLater(() -> {
          chat.getMessageByDateTime(message.getDateTime()).setError(true);
        });
        return;
      } finally {
        System.out.println("> send enviado com sucesso");

        Platform.runLater(() -> {
          chat.getMessageByDateTime(message.getDateTime()).setSend(true);
        });
      }
    }).start();
  }

  public void join(String chatName) {
    System.out.println("> Enviando join para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.join(chatName, localIp);
      } catch (IOException e) {
        System.out.println("> Erro: Falha ao enviar um join para servidor");
        setError("Código inválido");
        return;
      } finally {
        System.out.println("> join enviado com sucesso");

        Platform.runLater(() -> {
          setLoading(false);
        });
      }
    }).start();
  }

  public void leave(Chat chat) {
    System.out.println("> Enviando leave para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.leave(chat.getChatName(), localIp);
      } catch (IOException e) {
        System.out.println("> Erro: Falha ao enviar um leave para servidor");
        return;
      } finally {
        System.out.println("> leave enviado com sucesso");

        Platform.runLater(() -> {
          setLoading(false);
        });
      }
    }).start();
  }

  // Gets e Sets

  public Chat getChatByName(String chatName) {
    for (Chat chat : chats) {
      if (chat.getChatName().equals(chatName)) {
        return chat;
      }
    }

    return null;
  }

  public List<Chat> getChatsByText(String text) {
    List<Chat> result = new ArrayList<>();

    for (Chat chat : chats) {
      boolean containText = chat.getChatName().toLowerCase().contains(text.toLowerCase());

      if (containText) {
        result.add(chat);
      }
    }

    Collections.sort(result);
    return result;
  }

  public List<Chat> getChats() {
    Collections.sort(chats);
    return chats;
  }

  public void addChat(Chat chat) {
    chats.add(chat);
    Collections.sort(chats);
  }

  public void removeChat(Chat chat) {
    chats.remove(chat);
  }

  public User getUserByIp(String userId) {
    for (User user : users) {
      if (user.getUserIp().equals(userId)) {
        return user;
      }
    }

    return null;
  }

  public List<User> getUsers() {
    return users;
  }

  public void addUser(User user) {
    users.add(user);
  }

  public void removeUser(User user) {
    users.remove(user);
  }

  public void setChats(List<Chat> chats) {
    this.chats = chats;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public Client getTcpClient() {
    return tcpClient;
  }

  public void setTcpClient(Client tcpClient) {
    this.tcpClient = tcpClient;
  }

  public BooleanProperty isLoadingProperty() {
    return isLoading;
  }

  public boolean isLoading() {
    return isLoading.get();
  }

  public void setLoading(boolean isLoading) {
    this.isLoading.set(isLoading);
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public static void setInstance(App instance) {
    App.instance = instance;
  }

  public String getServerIp() {
    return serverIp;
  }

  public void setServerIp(String serverIp) {
    this.serverIp = serverIp;
  }

  public String getLocalIp() {
    return localIp;
  }

  public void setLocalIp(String localIp) {
    this.localIp = localIp;
  }

  public Client getUdpClient() {
    return udpClient;
  }

  public void setUdpClient(Client udpClient) {
    this.udpClient = udpClient;
  }
  
}
