package com.example.androidlabs;

public class Message
{
    public String message;
    public boolean isSent;

    public Message(String message, boolean isSent)
    {
        this.message = message;
        this.isSent = isSent;
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
}
