/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: Client
* Funcao...........: Gerencia os dados de um cliente.
*************************************************************** */

package model.service;

import java.io.IOException;
import java.net.UnknownHostException;

import model.App;
import model.util.DataManager;

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

  public abstract void receive() throws IOException;

  public void sanitizeReceivedData(String data) {
    String[] dataSplited = data.split("/");
    String type = dataSplited[0];

    switch (type) {
      case "send":
        DataManager.receiveSend(dataSplited[1], dataSplited[2], dataSplited[3]);
        break;
      case "chat":
        DataManager.receiveChat(dataSplited[1]);
        break;
      case "error":
        DataManager.receiveError(dataSplited[1]);
        break;
      default:
        return;
    }
  }

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
