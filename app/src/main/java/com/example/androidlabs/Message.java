package com.example.androidlabs;

public class Message
{
    public String message;
    public boolean isSent;
   public boolean isReceive;
    public long id;

    public Message(long id, String message, boolean isSent,boolean isReceive)
    {
        this.message = message;
        this.isSent = isSent;
        this.id = id;
        this.isReceive = isReceive;
    }

    public Message()
    {
        this.message =" ";
        this.isSent = false;
        this.isReceive = false;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        this.isSent = sent;
    }

    public boolean getReceive() {
        return isReceive;
    }

    public void setReceive(Boolean receive) {
        isReceive = receive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
