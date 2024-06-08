package model;

import java.util.ArrayList;
import java.util.List;

public class App {
  private static List<Chat> chats = new ArrayList<>();
  private static List<User> users = new ArrayList<>();

  public static Chat getChatById(String chatId) {
    for (Chat chat : chats) {
      if (chat.getChatId().equals(chatId)) {
        return chat;
      }
    }

    return null;
  }

  public static List<Chat> getChats() {
    return chats;
  }

  public static void addChat(Chat chat) {
    chats.add(chat);
  }

  public static void removeChat(Chat chat) {
    chats.remove(chat);
  }

  public static User getUserById(String userId) {
    for (User user : users) {
      if (user.getUserId().equals(userId)) {
        return user;
      }
    }

    return null;
  }

  public static List<User> getUsers() {
    return users;
  }

  public static void addUser(User user) {
    users.add(user);
  }

  public static void removeUser(User user) {
    users.remove(user);
  }
}
