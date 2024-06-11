package model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class App {
  private static App instance;
  public static String SERVER_IP = "192.168.1.11";
  private User user = null;
  private List<Chat> chats;
  private List<User> users;
  private Client tcpClient;
  private BooleanProperty isLoading;
  private String error;

  public App() {
    isLoading = new SimpleBooleanProperty(false);
    error = null;
    chats = new ArrayList<>();
    users = new ArrayList<>();
    tcpClient = Client.createClient(this, "TCP", SERVER_IP, 6789);
    startClient();
  }

  public static App getInstance() {
    if (instance == null) {
      instance = new App();
    }
    return instance;
  }

  public void startClient() {
    System.out.println("> Iniciando server");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.connect();
        tcpClient.receive();
      } catch (UnknownHostException e) {
        System.out.println("> Erro: Houve um problema ao encontrar o servidor");
        return;
      } catch (IOException e) {
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

  public void send(Chat chat, User user, Message message) {
    System.out.println("> Enviando send para o server");

    new Thread(() -> {
      try {
        tcpClient.send(chat.getChatId(), user.getName(), message.getText());
      } catch (IOException e) {
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

  public void join(String chatId, User user) {
    System.out.println("> Enviando join para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.join(chatId, user.getName());
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

      Chat chat = new Chat(chatId, "Grupo", null);
      this.addChat(chat);
      System.out.println("> Join enviado com sucesso");
    }).start();
  }

  public void leave(Chat chat, User user) {
    System.out.println("> Enviando leave para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        tcpClient.leave(chat.getChatId(), user.getName());
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

  public Chat getChatById(String chatId) {
    for (Chat chat : chats) {
      if (chat.getChatId().equals(chatId)) {
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

}
