/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 07/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: TCPClient
* Funcao...........: Monta um cliente usando o protocolo TCP.
*************************************************************** */

package model.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.App;
import model.util.DataManager;

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

    String localIp = socket.getLocalAddress().getHostAddress();
    app.setLocalIp(localIp);
    
    System.out.println("> Conectado ao servidor - IP: " + localIp);
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
        Object receivedObject = null;
        do {
          String data = "";
          receivedObject = input.readObject();
          data = (String) receivedObject;
          System.out.println("> Servidor: " + data);
          sanitizeReceivedData(data);
        } while (receivedObject != null);
      } catch (IOException | ClassNotFoundException e) {
        System.out.println("> Erro: não foi possível ler a mensagem do servidor");
        e.printStackTrace();
      }
    }).start();
  }

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

}
