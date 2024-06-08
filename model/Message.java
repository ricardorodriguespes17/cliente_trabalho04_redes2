package model;

public class Message {
  private String text;
  private String userId;
  private String dateTime;
  private boolean read;

  public Message(String text, String userId, String dateTime) {
    this.text = text;
    this.userId = userId;
    this.dateTime = dateTime;
    this.read = false;
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

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

}
