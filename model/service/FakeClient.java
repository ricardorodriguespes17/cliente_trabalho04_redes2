/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 12/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: FakeClient
* Funcao...........: Monta um cliente falso, apenas para testes.
*************************************************************** */

package model.service;

import java.io.IOException;
import java.net.UnknownHostException;

import model.App;

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

    System.out.println("> Conectado ao servidor - IP: " + app.getLocalIp());
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
  public void receive() throws IOException {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("> Servidor Fake: <resposta>");
  }

}
