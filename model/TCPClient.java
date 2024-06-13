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
    App.LOCAL_IP = socket.getLocalAddress().getHostAddress();
    System.out.println("> Conectado ao servidor - IP: " + App.LOCAL_IP);
  }

  @Override
  public void send(String groupId, String user, String data) throws IOException {
    String message = new String("send/" + groupId + "/" + user + "/" + data);
    output.writeObject(message);
    output.flush();
  }

  @Override
  public void join(String groupId, String user) throws IOException {
    String message = new String("join/" + groupId + "/" + user);
    output.writeObject(message);
    output.flush();
  }

  @Override
  public void leave(String groupId, String user) throws IOException {
    String message = new String("leave/" + groupId + "/" + user);
    output.writeObject(message);
    output.flush();
  }

  @Override
  public void receive() throws IOException {
    new Thread(() -> {
      try {
        input = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }

      Object receivedObject = null;
      do {
        try {
          receivedObject = input.readObject();
          String data = (String) receivedObject;
          System.out.println("> Servidor: " + data);
          sanitizeReceivedData(data);
        } catch (IOException | ClassNotFoundException e) {
          System.out.println("> Erro: não foi possível ler a mensagem do servidor");
        }
      } while (receivedObject != null);
    }).start();
  }

  public void sanitizeReceivedData(String data) throws IOException {
    String[] dataSplited = data.split("/");
    String type = dataSplited[0];
    String chatId = dataSplited[1];
    String userId = dataSplited[2];

    Chat chat = app.getChatById(chatId);
    LocalDateTime localDateTime = LocalDateTime.now();

    switch (type) {
      case "send":
        String messageText = "";
        for (int i = 3; i < dataSplited.length; i++) {
          messageText += dataSplited[i] + " ";
        }
        messageText = messageText.trim();
        System.out.println("> " + userId + " enviou " + messageText);

        Message message = new Message(messageText, userId, localDateTime);
        message.setSend(true);
        chat.addMessage(message);
        break;
      case "error":
        System.out.println("> Servidor: " + data);
        break;
      default:
        return;
    }
  }

}
