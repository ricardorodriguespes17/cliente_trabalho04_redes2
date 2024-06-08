package model;

public class Message {
  private String text;
  private String userId;
  private String dateTime;

  public Message(String text, String userId, String dateTime) {
    this.text = text;
    this.userId = userId;
    this.dateTime = dateTime;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

}
