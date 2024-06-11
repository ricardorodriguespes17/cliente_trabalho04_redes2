package model;

import java.net.*;
import java.time.LocalDateTime;
import java.io.*;

public class TCPClient extends Client {
  private Socket socket;
  private ObjectOutputStream output;
  private ObjectInputStream input;

  public TCPClient(App app, String host, int port) {
    super(app, host, port);
  }

  @Override
  public void connect() throws UnknownHostException, IOException {
    socket = new Socket(host, port);
    output = new ObjectOutputStream(socket.getOutputStream());
    System.out.println("> Conectado ao servidor");
  }

  @Override
  public void send(String groupId, String user, String data) throws IOException {
    if (socket.isConnected()) {
      String message = new String("send/" + groupId + "/" + user + "/" + data);
      output.writeObject(message);
      output.flush();
    } else {
      System.out.println("> Não conectado");
    }
  }

  @Override
  public void join(String groupId, String user) throws IOException {
    if (socket.isConnected()) {
      String message = new String("join/" + groupId + "/" + user);
      output.writeObject(message);
      output.flush();
    } else {
      System.out.println("> Não conectado");
    }
  }

  @Override
  public void leave(String groupId, String user) throws IOException {
    if (socket.isConnected()) {
      String message = new String("leave/" + groupId + "/" + user);
      output.writeObject(message);
      output.flush();
    } else {
      System.out.println("> Não conectado");
    }
  }

  @Override
  public void receive() throws IOException {
    new Thread(() -> {
      try {
        input = new ObjectInputStream(socket.getInputStream());
        Object receivedObject;
        do {
          receivedObject = input.readObject();
          String data = (String) receivedObject;
          System.out.println("> Servidor: " + data);
          sanitizeReceivedData(data);
        } while (receivedObject != null);

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void sanitizeReceivedData(String data) throws IOException {
    String[] dataSplited = data.split("/");
    String type = dataSplited[0];
    String chatId = dataSplited[1];
    String user = dataSplited[2];

    Chat chat = app.getChatById(chatId);
    LocalDateTime localDateTime = LocalDateTime.now();

    switch (type) {
      case "send":
        String messageText = "";
        for (int i = 3; i < dataSplited.length; i++) {
          messageText += dataSplited[i] + " ";
        }
        messageText = messageText.trim();

        Message message = new Message(messageText, user, localDateTime);
        chat.addMessage(message);
        this.send(chatId, user, message.getText());
        System.out.println("> " + user + " enviou '" + messageText + "' para " + chatId);
        break;
      case "join":
        if (chat != null) {
          System.out.println("> " + user + " Entrou no grupo " + chatId);
          Message message2 = new Message(user + " entrou do grupo", "server", localDateTime);
          chat.addMessage(message2);
          this.send(chatId, "server", message2.getText());
        }
        break;
      case "leave":
        if (chat != null) {
          System.out.println("> " + user + " saiu do grupo " + chatId);
          Message message3 = new Message(user + " saiu do grupo", "server", localDateTime);
          chat.addMessage(message3);
          this.send(chatId, "server", message3.getText());
        }
        break;
      default:
        return;
    }
  }

}
