package model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

import model.util.ObservableList;

public class Chat implements Comparable<Chat> {
  private String chatId;
  private String chatName;
  private String description;
  private ObservableList<Message> messages;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final Random RANDOM = new SecureRandom();

  public Chat(String chatId, String chatName, String description) {
    this.chatId = chatId;
    this.chatName = chatName;
    this.description = description;
    messages = new ObservableList<>();
  }

  public Chat(String chatName) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ObservableList<>();
  }

  public Chat(String chatName, String description) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ObservableList<>();
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

  public int getNumberOfMessagesUnread(String userId) {
    int count = 0;

    for (Message message : messages) {
      boolean notIsServer = !message.getUserIp().equals(App.SERVER_IP);
      boolean notIsSelf = !message.getUserIp().equals(userId);
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int compareTo(Chat other) {
    if (this.getLastMessage() == null || other.getLastMessage() == null) {
      return 0;
    }

    return other.getLastMessage().getDateTime().compareTo(this.getLastMessage().getDateTime());
  }

}
