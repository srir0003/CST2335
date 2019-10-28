package com.example.androidlabs;

public class Message
{
    public String message;
    public boolean isSent;
    public long id;

    public Message(long id, String message, boolean isSent)
    {
        this.message = message;
        this.isSent = isSent;
        this.id = id;
    }

    public Message()
    {
    }


    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isSent()
    {
        return isSent;
    }

    public void setSent(boolean sent)
    {
        isSent = sent;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
