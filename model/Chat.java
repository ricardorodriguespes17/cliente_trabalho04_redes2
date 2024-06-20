/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 10/06/2024
* Ultima alteracao.: 20/06/2024
* Nome.............: Chat
* Funcao...........: Gerencia os dados de um grupo/chat
*************************************************************** */

package model;

import java.time.LocalDateTime;
import java.util.Collections;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.util.ObservableList;

public class Chat implements Comparable<Chat> {
  private StringProperty chatName;
  private ObservableList<Message> messages;

  public Chat(String chatName) {
    this.chatName = new SimpleStringProperty(chatName);
    messages = new ObservableList<>();
  }

  public StringProperty getChatNameProperty() {
    return chatName;
  }

  public String getChatName() {
    return chatName.getValue();
  }

  public void setChatName(String chatName) {
    this.chatName.set(chatName);
  }

  public ObservableList<Message> getMessagesByText(String text) {
    ObservableList<Message> messages = new ObservableList<>();

    for (Message message : this.messages) {
      if (message.getText().toLowerCase().contains(text.toLowerCase())) {
        messages.add(message);
      }
    }

    return messages;
  }

  public Message getMessageByDateTime(LocalDateTime time) {
    for (Message message : messages) {
      if (message.getDateTime().equals(time)) {
        return message;
      }
    }

    return null;
  }

  public Message getLastMessage() {
    if (messages.size() == 0) {
      return null;
    }

    Message message = messages.get(messages.size() - 1);

    if (message.getUserIp().equals(App.SERVER_IP)) {
      return null;
    }

    return message;
  }

  public int getNumberOfMessagesUnread(String userIp) {
    int count = 0;

    for (Message message : messages) {
      boolean notIsServer = !message.getUserIp().equals(App.SERVER_IP);
      boolean notIsSelf = !message.getUserIp().equals(userIp);

      if (!message.isRead() && notIsServer && notIsSelf) {
        count++;
      }
    }

    return count;
  }

  public ObservableList<Message> getMessages() {
    return messages;
  }

  public void addMessage(Message message) {
    messages.add(message);
    Collections.sort(messages);
  }

  public void setMessages(ObservableList<Message> messages) {
    Collections.sort(messages);
    this.messages = messages;
  }

  @Override
  public int compareTo(Chat other) {
    if (this.getLastMessage() == null || other.getLastMessage() == null) {
      return 0;
    }

    return other.getLastMessage().getDateTime().compareTo(this.getLastMessage().getDateTime());
  }

}
