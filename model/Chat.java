package model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
  private String chatId;
  private String chatName;
  private List<Message> messages;

  public Chat(String chatId, String chatName) {
    this.chatId = chatId;
    this.chatName = chatName;
    messages = new ArrayList<>();
  }

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  public String getChatName() {
    return chatName;
  }

  public void setChatName(String chatName) {
    this.chatName = chatName;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

}
