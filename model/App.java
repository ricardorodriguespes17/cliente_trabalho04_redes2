package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class App {
  private static App instance;
  private User user = null;
  private List<Chat> chats;
  private List<User> users;
  private Client tcpClient;
  private BooleanProperty isLoading;

  public App() {
    isLoading = new SimpleBooleanProperty(false);
    chats = new ArrayList<>();
    users = new ArrayList<>();
    tcpClient = Client.createClient("TCP", "192.168.1.11", 6789);
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
        Thread.sleep(5000);
        System.out.println("> Server iniciado");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        Platform.runLater(() -> {
          setLoading(false);
        });
      }
    }).start();

    // new Thread(() -> {
    // try {
    // tcpClient.connect();
    // tcpClient.receive();
    // } catch (UnknownHostException e) {
    // System.out.println("> Erro: Houve um problema ao encontrar o servidor");
    // } catch (IOException e) {
    // System.out.println("> Erro: Houve um problema ao conectar o servidor");
    // } finally {
    // Platform.runLater(() -> {
    // System.out.println("> Server iniciado");
    // setLoading(false);
    // });
    // }
    // }).start();
  }

  public void send(Chat chat, User user, Message message) {
    System.out.println("> Enviando send para o server");

    new Thread(() -> {
      try {
        Thread.sleep(5000);
        System.out.println("> Send enviado com sucesso");
      } catch (InterruptedException e) {
        Platform.runLater(() -> {
          chat.getMessageByDateTime(message.getDateTime()).setError(true);
        });
      } finally {
        Platform.runLater(() -> {
          chat.getMessageByDateTime(message.getDateTime()).setSend(true);
        });
      }
    }).start();

    // new Thread(() -> {
    // try {
    // tcpClient.send(chat.getChatId(), user.getName(), message);
    // } catch (IOException e) {
    // System.out.println("> Erro: Falha ao enviar um send para servidor");
    // }
    // }).start();
  }

  public void join(Chat chat, User user) {
    System.out.println("> Enviando join para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        Thread.sleep(5000);
        System.out.println("> Join enviado com sucesso");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        Platform.runLater(() -> {
          setLoading(false);
        });
      }
    }).start();

    // new Thread(() -> {
    // try {
    // tcpClient.join(chat.getChatId(), user.getName());
    // } catch (IOException e) {
    // System.out.println("> Erro: Falha ao enviar um join para servidor");
    // }
    // }).start();
  }

  public void leave(Chat chat, User user) {
    System.out.println("> Enviando leave para o server");
    setLoading(true);

    new Thread(() -> {
      try {
        Thread.sleep(5000);
        System.out.println("> Leave enviado com sucesso");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        Platform.runLater(() -> {
          setLoading(false);
        });
      }
    }).start();

    // new Thread(() -> {
    // try {
    // tcpClient.leave(chat.getChatId(), user.getName());
    // } catch (IOException e) {
    // System.out.println("> Erro: Falha ao enviar um leave para o servidor");
    // }
    // }).start();
  }

  public void createRandonsChats() {
    Chat chatRedes = new Chat("Redes II", "Grupo da matéria de Redes II 2024.1");

    User user = new User("10", "Ricardo");
    User user2 = new User("11", "Marlos");

    LocalDateTime today = LocalDateTime.now();
    LocalDateTime dateTime = today.minusDays(0);
    Message message = new Message("Olá mundo", user2.getUserId(), dateTime);

    chatRedes.addMessage(message);
    this.addUser(user);
    this.addUser(user2);
    this.setUser(user);
    this.addChat(chatRedes);
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
    this.join(chat, user);
  }

  public void removeChat(Chat chat) {
    chats.remove(chat);
    this.leave(chat, user);
  }

  public User getUserById(String userId) {
    for (User user : users) {
      if (user.getUserId().equals(userId)) {
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

}
