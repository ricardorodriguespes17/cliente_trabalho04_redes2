/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 14/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: UDPClient
* Funcao...........: Monta um cliente usando o protocolo UDP.
*************************************************************** */

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
    socket = new DatagramSocket(port);
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
  public void receive() throws IOException {
    new Thread(() -> {
      while (true) {
        byte[] receivedData = new byte[1024];
        DatagramPacket receivedDatagramPacket = new DatagramPacket(receivedData, receivedData.length);

        try {
          socket.receive(receivedDatagramPacket);
          String data = new String(receivedDatagramPacket.getData());
          System.out.println("> Servidor UDP: " + data);
          sanitizeReceivedData(data);
        } catch (IOException e) {
          System.out.println("> Erro: não foi possível ler a mensagem do servidor UDP");
          e.printStackTrace();
        }
      }
    }).start();
  }

}
