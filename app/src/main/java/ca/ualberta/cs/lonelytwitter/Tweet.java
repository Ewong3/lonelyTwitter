package ca.ualberta.cs.lonelytwitter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import io.searchbox.annotations.JestId;

public abstract class Tweet {
    @JestId
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Date date;
    protected String message;


    public Tweet(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    public Tweet(String message) {
        this.message = message;
        this.date = new Date();
    }
    protected transient Bitmap thumbnail;
    protected String thumbnail64;

    public void addThumbnail (Bitmap newThumbnail) {
        if (newThumbnail != null) {
            thumbnail = newThumbnail;
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            newThumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] b = byteArray.toByteArray();
            thumbnail64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }
    public Bitmap getThumbnail() {
        if (thumbnail == null && thumbnail64 != null) {
            byte[] decode = Base64.decode(thumbnail64, Base64.DEFAULT);
            thumbnail = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        }
        return thumbnail;
    }
    public abstract Boolean isImportant();

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) throws TweetTooLongException {
        if (message.length() > 140) {
            throw new TweetTooLongException();
        }
        this.message = message;
    }

    @Override
    public String toString() {
        // Some people thought they would be funny and add tweets without dates...
        if(date == null) {
            if(message == null) {
                return "";
            } else {
                return message;
            }
        }
        return date.toString() + " | " + message;
    }
}
