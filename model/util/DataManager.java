/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 13/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: DataManager
* Funcao...........: Trata os dados recebidos pelo cliente e
realiza a construção/atualização dos chats.
*************************************************************** */

package model.util;

import java.time.LocalDateTime;

import model.App;
import model.Chat;
import model.Message;

public class DataManager {
  private static App app = App.getInstance();

  public static void receiveSend(String chatName, String userIp, String messageText) {
    Chat chat = app.getChatByName(chatName);

    if(chat == null)
      return;

    Message message = new Message(messageText, userIp, LocalDateTime.now());
    message.setSend(true);
    chat.addMessage(message);

    System.out.println("> " + userIp + " enviou " + messageText);
  }

  public static void receiveChat(String chatName) {
    Chat chat = app.getChatByName(chatName);

    if(chat == null)
      return;

    chat.setChatName(chatName);
  }

  public static void receiveError(String reason) {
    System.out.println("> Erro: " + reason);
  }
}
