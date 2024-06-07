package model;

import java.net.*;
import java.io.*;

public class TCPClient extends Client {
  private Socket socket;
  private ObjectOutputStream output;
  private ObjectInputStream input;

  public TCPClient(String host, int port) {
    super(host, port);
  }

  @Override
  public void connect() throws UnknownHostException, IOException {
    socket = new Socket(host, port);
    System.out.println("> Conectado ao servidor");
  }

  @Override
  public void send(String groupId, String user, String data) throws IOException {
    String message = new String("send/" + groupId + "/" + user + "/" + data);
    output = new ObjectOutputStream(socket.getOutputStream());
    output.writeObject(message);
    output.flush();
  }

  @Override
  public void join() {
    throw new UnsupportedOperationException("Unimplemented method 'join'");
  }

  @Override
  public void leave() {
    throw new UnsupportedOperationException("Unimplemented method 'leave'");
  }

  @Override
  public void receive() throws ClassNotFoundException, IOException {
    new Thread(() -> {
      try {
        input = new ObjectInputStream(socket.getInputStream());
        Object receivedObject;
        do {
          receivedObject = input.readObject();
          String message = (String) receivedObject;
          System.out.println("> Mensagem do servidor: " + message);
        } while (receivedObject != null);

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }).start();
  }

}
