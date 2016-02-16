import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {
    private String id;
    private String message;
    private String author;
    private String timestamp;

    public Message(String id, String message, String author, String timestamp) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "id: " +
                this.getId() +
                " ,Author: " +
                this.getAuthor() +
                " ,timestamp: " +
                getDate() +
                " ,message: " +
                this.getMessage();
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(this.getTimestamp()));
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy ',' HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
