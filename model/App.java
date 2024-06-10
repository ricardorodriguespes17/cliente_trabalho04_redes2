package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
  private static User user = null;
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

  public static List<Chat> getChatsByText(String text) {
    List<Chat> result = new ArrayList<>();

    for (Chat chat : chats) {
      boolean containText = chat.getChatName().toLowerCase().contains(text.toLowerCase());

      if (containText) {
        result.add(chat);
      }
    }

    Collections.sort(result);
    return result;
  }

  public static List<Chat> getChats() {
    Collections.sort(chats);
    return chats;
  }

  public static void addChat(Chat chat) {
    chats.add(chat);
    Collections.sort(chats);
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

  public static User getUser() {
    return user;
  }

  public static void setUser(User user) {
    App.user = user;
  }

  public static void setChats(List<Chat> chats) {
    App.chats = chats;
  }

  public static void setUsers(List<User> users) {
    App.users = users;
  }
}
