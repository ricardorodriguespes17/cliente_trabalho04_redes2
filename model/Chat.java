package model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
  private String chatId;
  private String chatName;
  private String description;
  private List<Message> messages;

  public Chat(String chatId, String chatName) {
    this.chatId = chatId;
    this.chatName = chatName;
    messages = new ArrayList<>();
    messages.add(new Message("Início do chat", "server", "00:00"));
  }

  public Chat(String chatId, String chatName, String description) {
    this.chatId = chatId;
    this.chatName = chatName;
    messages = new ArrayList<>();
    messages.add(new Message("Início do chat", "server", "00:00"));
    if (description == null || description.equals("")) {
      this.description = null;
    }
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

  public Message getLastMessage() {
    Message message = messages.get(messages.size() - 1);

    if (message.getUserId().equals("server")) {
      return null;
    }

    return message;
  }

  public int getNumberOfMessagesUnread() {
    int count = 0;

    for (Message message : messages) {
      boolean notIsServer = !message.getUserId().equals("server");
      boolean notIsSelf = !message.getUserId().equals("10");
      if (!message.isRead() && notIsServer && notIsSelf) {
        count++;
      }
    }

    return count;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void addMessage(Message message) {
    messages.add(message);
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
