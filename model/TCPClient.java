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
          System.out.println("Servidor: " + message);
        } while (receivedObject != null);

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }).start();
  }

}
