package model;

import java.net.*;
import java.io.*;

public class ClienteTCP {
  public static void main(String[] args) {
    int port = 6789;
    String host = "192.168.0.1";
    Socket socket;

    try {
      socket = new Socket(host, port);
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream output = new ObjectOutputStream(outputStream);

      String message = new String("Hello world");
      output.writeObject(message);
      output.flush();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
