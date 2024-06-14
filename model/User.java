/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 07/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: User
* Funcao...........: Gerencia os dados dos usuário/contatos.
*************************************************************** */

package model;

public class User {
  private String userIp;
  private String name;

  public User(String userIp, String name) {
    this.userIp = userIp;
    this.name = name;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
