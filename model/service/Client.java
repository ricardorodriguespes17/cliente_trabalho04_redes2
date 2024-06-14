/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: Client
* Funcao...........: Gerencia os dados de um cliente.
*************************************************************** */

package model.service;

import java.io.IOException;
import java.net.UnknownHostException;

import model.App;

public abstract class Client {
  protected int port;
  protected String host;
  protected App app;

  public Client(App app, String host, int port) {
    this.app = app;
    this.port = port;
    this.host = host;
  }

  public abstract void connect() throws UnknownHostException, IOException;

  public abstract void send(String groupId, String user, String data) throws IOException;

  public abstract void join(String grupoId, String user) throws IOException;

  public abstract void leave(String grupoId, String user) throws IOException;

  public abstract void create(String grupoId, String grupoName, String user) throws IOException;

  public abstract void receive() throws IOException;

  public static Client createClient(App app, String type, String serverAddress, int serverPort) {
    if (type.equals("TCP")) {
      return new TCPClient(app, serverAddress, serverPort);
    } else if (type.equals("UDP")) {
      return new UDPClient(app, serverAddress, serverPort);
    } else if (type.equals("TEST")) {
      return new FakeClient(app, serverAddress, serverPort);
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
