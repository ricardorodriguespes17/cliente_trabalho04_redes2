package model;

import java.io.IOException;
import java.net.UnknownHostException;

public class FakeClient extends Client {

  public FakeClient(App app, String host, int port) {
    super(app, host, port);
  }

  @Override
  public void connect() throws UnknownHostException, IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("> Conectado ao servidor - IP: " + App.LOCAL_IP);
  }

  @Override
  public void send(String groupId, String user, String data) throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void join(String grupoId, String user) throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void leave(String grupoId, String user) throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void create(String grupoId, String groupName, String user) throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void receive() throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("> Servidor: <resposta>");
  }

}
