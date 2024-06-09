package model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chat {
  private String chatId;
  private String chatName;
  private String description;
  private List<Message> messages;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final Random RANDOM = new SecureRandom();

  public Chat(String chatName) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ArrayList<>();
  }

  public Chat(String chatName, String description) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ArrayList<>();
    LocalDateTime currentDateTime = LocalDateTime.now();
    messages.add(new Message("Início do chat", "server", currentDateTime));
    if (description == null || description.equals("")) {
      this.description = null;
    }
  }

  public static String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int index = RANDOM.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }

    return sb.toString();
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
      boolean notIsSelf = !message.getUserId().equals(App.getUser().getUserId());
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
    Collections.sort(messages);
  }

  public void setMessages(List<Message> messages) {
    Collections.sort(messages);
    this.messages = messages;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
