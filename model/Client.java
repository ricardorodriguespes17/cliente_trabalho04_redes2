package model;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class Client {
  protected int port;
  protected String host;

  public Client(String host, int port) {
    this.port = port;
    this.host = host;
  }

  public abstract void connect() throws UnknownHostException, IOException;

  public abstract void send(String groupId, String user, String data) throws IOException;

  public abstract void join(String grupoId, String user) throws IOException;

  public abstract void leave(String grupoId, String user) throws IOException;

  public abstract void receive() throws IOException;

  public static Client createClient(String type, String serverAddress, int serverPort) {
    if (type.equals("TCP")) {
      return new TCPClient(serverAddress, serverPort);
    } else {
      throw new IllegalArgumentException("Tipo de cliente desconhecido: " + type);
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

}
