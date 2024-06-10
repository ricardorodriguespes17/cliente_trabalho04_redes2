package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Message implements Comparable<Message> {
  private String text;
  private String userId;
  private LocalDateTime dateTime;
  private boolean read;
  private BooleanProperty send;
  private BooleanProperty error;

  public Message(String text, String userId, LocalDateTime dateTime) {
    this.text = text;
    this.userId = userId;
    this.dateTime = dateTime;
    this.read = false;
    send = new SimpleBooleanProperty(false);
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

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public String getDateToChat() {
    LocalDate date = dateTime.toLocalDate();
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1);
    boolean isEqualsYesterday = date.isEqual(yesterday);
    boolean isBeforeYesterday = date.isBefore(yesterday);

    if (isEqualsYesterday)
      return "Ontem";
    else if (isBeforeYesterday) {
      return getDate();
    } else {
      return getTime();
    }

  }

  public String getDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedTime = dateTime.format(formatter);
    return formattedTime;
  }

  public String getTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String formattedTime = dateTime.format(formatter);
    return formattedTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public BooleanProperty isSendProperty() {
    return send;
  }

  public boolean isSend() {
    return send.get();
  }

  public void setSend(boolean send) {
    this.send.set(send);
  }

  public BooleanProperty isErrorProperty() {
    return error;
  }

  public boolean isError() {
    return error.get();
  }

  public void setError(boolean error) {
    this.error.set(error);
  }

  @Override
  public int compareTo(Message other) {
    return this.dateTime.compareTo(other.dateTime);
  }

}
