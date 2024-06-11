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
