package model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chat implements Comparable<Chat> {
  private String chatId;
  private String chatName;
  private String description;
  private List<Message> messages;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final Random RANDOM = new SecureRandom();
  private Runnable observer;

  public Chat(String chatId, String chatName, String description) {
    this.chatId = chatId;
    this.chatName = chatName;
    this.description = description;
    messages = new ArrayList<>();
  }

  public Chat(String chatName) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ArrayList<>();
  }

  public Chat(String chatName, String description) {
    this.chatId = generateRandomString(5);
    this.chatName = chatName;
    messages = new ArrayList<>();
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

  public void addListener(Runnable observer) {
    this.observer = observer;
  }

  public void runListener() {
    if (observer != null) {
      observer.run();
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

  public List<Message> getMessagesByText(String text) {
    List<Message> messages = new ArrayList<>();

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

    if (message.getUserId().equals("server")) {
      return null;
    }

    return message;
  }

  public int getNumberOfMessagesUnread(String userId) {
    int count = 0;

    for (Message message : messages) {
      boolean notIsServer = !message.getUserId().equals("server");
      boolean notIsSelf = !message.getUserId().equals(userId);
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
    runListener();
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

  @Override
  public int compareTo(Chat other) {
    if (this.getLastMessage() == null || other.getLastMessage() == null) {
      return 0;
    }

    return other.getLastMessage().getDateTime().compareTo(this.getLastMessage().getDateTime());
  }

}
