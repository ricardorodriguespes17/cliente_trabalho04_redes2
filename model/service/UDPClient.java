package model.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import model.App;

public class UDPClient extends Client {
  private DatagramSocket socket;

  public UDPClient(App app, String host, int port) {
    super(app, host, port);
  }

  @Override
  public void connect() throws UnknownHostException, IOException {
    socket = new DatagramSocket();
    socket.connect(InetAddress.getByName(host), port);
  }

  @Override
  public void send(String groupId, String user, String data) throws IOException {
    String message = new String("send/" + groupId + "/" + user + "/" + data);
    byte[] byteData = message.getBytes();

    socket.send(new DatagramPacket(byteData, byteData.length));
  }

  @Override
  public void join(String grupoId, String user) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method 'join'");
  }

  @Override
  public void leave(String grupoId, String user) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method 'leave'");
  }

  @Override
  public void create(String grupoId, String grupoName, String user) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public void receive() throws IOException {
    throw new UnsupportedOperationException("Unimplemented method 'receive'");
  }
  
}
